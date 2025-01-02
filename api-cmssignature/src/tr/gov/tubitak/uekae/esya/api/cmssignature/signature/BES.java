package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BES extends Signer{
	
	BES(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_BES;
	}
	
	BES(BaseSignedData aSD,ESignerInfo aSigner)
	{
		mSignedData = aSD;
		mSignerInfo = aSigner;
		mSignatureType = ESignatureType.TYPE_BES;
	}

	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters)
	throws CMSSignatureException 
	{
		Boolean validateCert = (Boolean) aParameters.get(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING);
		ECertificate cer = (ECertificate) aParameters.get(AllEParameters.P_SIGNING_CERTIFICATE);
		if(mSignatureType != ESignatureType.TYPE_BES && mSignatureType != ESignatureType.TYPE_EPES){
			_validateCertificate(cer, aParameters, getCurrentTime(aParameters),true);
		}
		else if (validateCert)
		{
			_validateCertificate(cer, aParameters, getCurrentTime(aParameters),false);
		}
		
	}

	@Override
	protected List<IAttribute> _getMandatorySignedAttributes(boolean aIsCounter,DigestAlg aAlg) 
	{
		List<IAttribute> attributes = new ArrayList<IAttribute>();
		attributes.add(new MessageDigestAttr());
		if(!aIsCounter)
			attributes.add(new ContentTypeAttr());
		if(aAlg.equals(DigestAlg.SHA1))
			attributes.add(new SigningCertificateAttr());
		else
			attributes.add(new SigningCertificateV2Attr());
			
		return attributes;
	}
	
	@Override
	protected void _convert(ESignatureType aType,Map<String, Object> aParameters) throws CMSSignatureException {
		if(aType.equals(ESignatureType.TYPE_BES))
			throw new CMSSignatureException("Signature is already BES.");
		else
			throw new CMSSignatureException("Signature type:"+aType.name()+" can not be converted to BES");
		
	}
	
	/**
	 * Returns  time of signature time stamp
	 * @return Calendar
	 * @throws ESYAException
	 */
	public Calendar getTime() throws ESYAException {
		try {
			EAttribute attr = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken).get(0);
			EContentInfo contentInfo = new EContentInfo(attr.getValue(0));
			ESignedData sd = new ESignedData(contentInfo.getContent());
			ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
			return tstInfo.getTime();
		} catch (Exception ex) {
			throw new ESYAException("Cant decode timestamp time", ex);
		}
	}

	private Calendar getCurrentTime(Map<String, Object> aParameters)
	{
		if(aParameters.containsKey(AllEParameters.P_CURRENT_TIME))
			return ((Calendar)aParameters.get(AllEParameters.P_CURRENT_TIME));

		return Calendar.getInstance();
	}
}
