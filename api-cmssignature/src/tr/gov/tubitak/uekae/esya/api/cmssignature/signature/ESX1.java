package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CAdES_C_TimeStampAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CAdES_C_TimeStampAttrChecker;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;

public class ESX1 extends ESC {
	ESX1(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_ESX_Type1;
	}
	
	ESX1(BaseSignedData aSD,ESignerInfo aSigner)
	{
		super(aSD,aSigner);
		mSignatureType = ESignatureType.TYPE_ESX_Type1;
	}
	
	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters) 
	throws CMSSignatureException 
	{
		super._addUnsignedAttributes(aParameters);
		_addESX1Attributes(aParameters);
	}
	
	private void _addESX1Attributes(Map<String, Object> aParameters) 
	throws CMSSignatureException
	{
		CAdES_C_TimeStampAttr timestamp = new CAdES_C_TimeStampAttr();
		timestamp.setParameters(aParameters);

        _addTSCertRevocationValues(aParameters, AttributeOIDs.id_aa_signatureTimeStampToken, true);
		
		//aParameters.put(EParameter.P_SIGNER_INFO, aSignerInfo);
		timestamp.setValue();
		mSignerInfo.addUnsignedAttribute(timestamp.getAttribute());
		
		Boolean validateTS = (Boolean) aParameters.get(EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING);

		if (validateTS){
			CAdES_C_TimeStampAttrChecker cAdES_C_TimeStampAttrChecker=new CAdES_C_TimeStampAttrChecker();
			Map<String,Object> params = new HashMap<String, Object>(aParameters);
		    params.remove(AllEParameters.P_SIGNING_CERTIFICATE);
		    cAdES_C_TimeStampAttrChecker.setParameters(params);
			CheckerResult aCheckerResult =new CheckerResult();
			boolean result=cAdES_C_TimeStampAttrChecker.check(this, aCheckerResult);
			if(!result){
				logger.error(aCheckerResult.toString());
				throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.CADES_C_TIMESTAMP_INVALID));
			}
		}
	
	}
	
	@Override
	protected void _convert(ESignatureType aType,Map<String, Object> aParameters) throws CMSSignatureException {
		
		aParameters.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
		if(aType.equals(ESignatureType.TYPE_BES) ||aType.equals(ESignatureType.TYPE_EPES) || aType == ESignatureType.TYPE_EST)
		{
			super._convert(aType, aParameters);
			_addESX1Attributes(aParameters);
		}
		else if(aType == ESignatureType.TYPE_ESC || aType == ESignatureType.TYPE_ESX_Type1 )
		{
			
			_addESX1Attributes(aParameters);
		}
		else
			throw new CMSSignatureException("Signature type:"+aType.name()+" can not be converted to ES_X1");
		
	}
	
	/*@Override
	public ESignatureType getType() 
	{
		return ESignatureType.TYPE_ESX_Type1;
	}*/
}
