package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.ContentTypeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.MessageDigestAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningCertificateAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningCertificateV2Attr;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

public class EPES extends BES{
	
	EPES(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_EPES;
	}
	
	EPES(BaseSignedData aSD,ESignerInfo aSigner)
	{
		super(aSD, aSigner);
		mSignatureType = ESignatureType.TYPE_EPES;
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
		if(aType.equals(ESignatureType.TYPE_EPES))
			throw new CMSSignatureException("Signature is already TYPE_EPES.");
		else
			throw new CMSSignatureException("Signature type:"+aType.name()+" can not be converted to EPES");
		
	}

}
