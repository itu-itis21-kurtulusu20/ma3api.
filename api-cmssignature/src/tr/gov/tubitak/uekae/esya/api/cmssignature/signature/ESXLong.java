package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CertValuesAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.RevocationValuesAttr;

public class ESXLong extends ESC {
	
	ESXLong(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_ESXLong;
	}
	
	ESXLong(BaseSignedData aSD,ESignerInfo aSigner)
	{
		super(aSD,aSigner);
		mSignatureType = ESignatureType.TYPE_ESXLong;
	}
	
	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters) 
	throws CMSSignatureException 
	{
		super._addUnsignedAttributes(aParameters);
		_addESXLongAttributes(aParameters);
	}
	
	private void _addESXLongAttributes(Map<String, Object> aParameters)
	throws CMSSignatureException 
	{
		CertValuesAttr certValues = new CertValuesAttr();
		RevocationValuesAttr revValues = new RevocationValuesAttr();
		
		certValues.setParameters(aParameters);
		revValues.setParameters(aParameters);
		
		certValues.setValue();
		revValues.setValue();
		
		mSignerInfo.addUnsignedAttribute(certValues.getAttribute());
		mSignerInfo.addUnsignedAttribute(revValues.getAttribute());

	}
	
	protected void _convert(ESignatureType aType,Map<String, Object> aParameters) 
	throws CMSSignatureException
	{
		if(aType == ESignatureType.TYPE_BES ||aType == ESignatureType.TYPE_EPES ||
				aType == ESignatureType.TYPE_EST )
		{
			super._convert(aType, aParameters);
			_addESXLongAttributes(aParameters);
		}
		else if(aType == ESignatureType.TYPE_ESC)
		{
			aParameters.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
			_addESXLongAttributes(aParameters);
		}
		else if(aType == ESignatureType.TYPE_ESXLong)
		{
			throw new CMSSignatureException("Signature type is already ESXLong.");
		}
		else
			throw new CMSSignatureException("Signature type:"+aType.name()+" can not be converted to ESXLong");
	}
	
	/*@Override
	public ESignatureType getType() 
	{
		return ESignatureType.TYPE_ESXLong;
	}*/

}
