package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CertValuesAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.RevocationValuesAttr;

public class ESXLong1 extends ESX1 {
	
	ESXLong1(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_ESXLong_Type1;
	}
	
	ESXLong1(BaseSignedData aSD,ESignerInfo aSigner)
	{
		super(aSD,aSigner);
		mSignatureType = ESignatureType.TYPE_ESXLong_Type1;
	}
	
	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters) 
	throws CMSSignatureException 
	{
		super._addUnsignedAttributes(aParameters);
		_addESXLong1Attributes(aParameters);
	}
	
	protected void _addESXLong1Attributes(Map<String, Object> aParameters)
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
	
	
	protected void _convert(ESignatureType aType,Map<String,Object> aParamMap)
	throws CMSSignatureException
	{
		aParamMap.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
		if(aType == ESignatureType.TYPE_BES ||aType == ESignatureType.TYPE_EPES ||
				aType == ESignatureType.TYPE_EST || aType == ESignatureType.TYPE_ESC)
		{
			super._convert(aType, aParamMap);
			_addESXLong1Attributes(aParamMap);
		}
		else if(aType == ESignatureType.TYPE_ESX_Type1)
		{
			
			_addESXLong1Attributes(aParamMap);
		}
		else if(aType == ESignatureType.TYPE_ESXLong_Type1)
		{
			throw new CMSSignatureException("Signature type is already ESXLong1.");
		}	
		else
			throw new CMSSignatureException("Signature type:"+aType.name()+" can not be converted to ES_XLong1");
	}
	
	/*@Override
	public ESignatureType getType() 
	{
		return ESignatureType.TYPE_ESXLong_Type1;
	}*/

}
