package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CompleteCertRefAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CompleteRevRefAttr;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

public class ESC extends EST 
{

	ESC(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_ESC;
	}
	
	ESC(BaseSignedData aSD,ESignerInfo aSigner)
	{
		super(aSD,aSigner);
		mSignatureType = ESignatureType.TYPE_ESC;
	}
	
	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters) 
	throws CMSSignatureException 
	{
		super._addUnsignedAttributes(aParameters);
		_addESCAttributes(aParameters);
	}
	
	private void _addESCAttributes(Map<String, Object> aParameters)
	throws CMSSignatureException
	{
		CompleteCertRefAttr certRefs = new CompleteCertRefAttr();
		CompleteRevRefAttr revRefs = new CompleteRevRefAttr();
		
		certRefs.setParameters(aParameters);
		revRefs.setParameters(aParameters);
		
		certRefs.setValue();
		revRefs.setValue();
		mSignerInfo.addUnsignedAttribute(certRefs.getAttribute());
		mSignerInfo.addUnsignedAttribute(revRefs.getAttribute());
	}
	
	@Override
	protected void _convert(ESignatureType aType,Map<String, Object> aParameters) throws CMSSignatureException {
		if(aType.equals(ESignatureType.TYPE_BES) ||aType.equals(ESignatureType.TYPE_EPES))
		{
			super._convert(aType, aParameters);
			_addESCAttributes(aParameters);
		}
		else if(aType == ESignatureType.TYPE_EST)
		{
			ECertificate cer = (ECertificate) aParameters.get(AllEParameters.P_SIGNING_CERTIFICATE);
			
			Calendar c;
			try 
			{
				c = getTime();
			} 
			catch (ESYAException e) 
			{
				throw new CMSSignatureException(e);
			}
			//CertificateStatusInfo csi = _validateCertificate(cer, aParameters, d);
			
			CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
			CertificateStatusInfo csi = finder.validateCertificate(cer, aParameters, c);
			List<CertRevocationInfo> list = finder.getCertRevRefs(csi);
			aParameters.put(AllEParameters.P_CERTIFICATE_REVOCATION_LIST, list);
			
			_addESCAttributes(aParameters);
		}
		else if(aType == ESignatureType.TYPE_ESC)
			throw new CMSSignatureException("Signature is already ES_C.");
		else
			throw new CMSSignatureException("Signature type:"+aType.name()+" can not be converted to ES_C");
		
	}
	
	/*@Override
	public ESignatureType getType() 
	{
		return ESignatureType.TYPE_ESC;
	}*/
	
}
