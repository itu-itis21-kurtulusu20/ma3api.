package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.TimeStampedCertsCrlsAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.TimeStampedCertsCrlsRefsAttrChecker;

public class ESX2 extends ESC {
	
	ESX2(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_ESX_Type2;
	}
	
	ESX2(BaseSignedData aSD,ESignerInfo aSigner)
	{
		super(aSD,aSigner);
		mSignatureType = ESignatureType.TYPE_ESX_Type2;
	}
	
	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters) 
	throws CMSSignatureException 
	{
		super._addUnsignedAttributes(aParameters);
		_addESX2Attributes(aParameters);
	}
	
	private void _addESX2Attributes(Map<String, Object> aParameters) 
	throws CMSSignatureException 
	{
        _addTSCertRevocationValues(aParameters, AttributeOIDs.id_aa_signatureTimeStampToken, true);

		TimeStampedCertsCrlsAttr timestamp = new TimeStampedCertsCrlsAttr();
		timestamp.setParameters(aParameters);
		//aParameters.put(EParameter.P_SIGNER_INFO, aSignerInfo);
		timestamp.setValue();
		mSignerInfo.addUnsignedAttribute(timestamp.getAttribute());
		
		Boolean validateTS = (Boolean) aParameters.get(EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING);

		if (validateTS){
			TimeStampedCertsCrlsRefsAttrChecker timeStampedCertsCrlsRefsAttrChecker=new TimeStampedCertsCrlsRefsAttrChecker();
			Map<String,Object> params = new HashMap<String, Object>(aParameters);
		    params.remove(AllEParameters.P_SIGNING_CERTIFICATE);
		    timeStampedCertsCrlsRefsAttrChecker.setParameters(params);
			CheckerResult aCheckerResult =new CheckerResult();
			boolean result=timeStampedCertsCrlsRefsAttrChecker.check(this, aCheckerResult);
			if(!result){
				logger.error(aCheckerResult.toString());
				throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.CERTSCRLS_TIMESTAMP_INVALID));
			}
		}
	}
	
	@Override
	protected void _convert(ESignatureType aType,Map<String, Object> aParameters) throws CMSSignatureException {
		aParameters.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
		if(aType.equals(ESignatureType.TYPE_BES) ||aType.equals(ESignatureType.TYPE_EPES) || aType == ESignatureType.TYPE_EST)
		{
			super._convert(aType, aParameters);
			_addESX2Attributes(aParameters);
		}
		else if(aType == ESignatureType.TYPE_ESC || aType == ESignatureType.TYPE_ESX_Type2 )
		{
			
			_addESX2Attributes(aParameters);
		}
		else
			throw new CMSSignatureException("Signature type:"+aType.name()+" can not be converted to ES_X2");
		
	}
	
	/*@Override
	public ESignatureType getType() 
	{
		return ESignatureType.TYPE_ESX_Type2;
	}*/

}
