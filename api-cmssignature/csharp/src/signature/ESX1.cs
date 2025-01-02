using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class ESX1 : ESC
    {
        public ESX1(BaseSignedData aSD)
            : base(aSD)
        {
            mSignatureType = ESignatureType.TYPE_ESX_Type1;
        }

        public ESX1(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {

            mSignatureType = ESignatureType.TYPE_ESX_Type1;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {
            base._addUnsignedAttributes(aParameters);
            _addESX1Attributes(aParameters);
        }

        private void _addESX1Attributes(Dictionary<String, Object> aParameters)
        {
            _addTSCertRevocationValues(aParameters, AttributeOIDs.id_aa_signatureTimeStampToken, true);

            CAdES_C_TimeStampAttr timestamp = new CAdES_C_TimeStampAttr();
            timestamp.setParameters(aParameters);
            //aParameters.put(EParameter.P_SIGNER_INFO, aSignerInfo);
            timestamp.setValue();
            mSignerInfo.addUnsignedAttribute(timestamp.getAttribute());

            Boolean validateTS = (Boolean) aParameters[EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING];

		if (validateTS){
			CAdES_C_TimeStampAttrChecker cAdES_C_TimeStampAttrChecker=new CAdES_C_TimeStampAttrChecker();
			Dictionary<String,Object> paramaters = new Dictionary<String, Object>(aParameters);
		    paramaters.Remove(AllEParameters.P_SIGNING_CERTIFICATE);
            cAdES_C_TimeStampAttrChecker.setParameters(paramaters);
			CheckerResult aCheckerResult =new CheckerResult();
			Boolean result=cAdES_C_TimeStampAttrChecker.check(this, aCheckerResult);
			if(!result){
				logger.Error(aCheckerResult.ToString());
                throw new CMSSignatureException(Msg.getMsg(Msg.CADES_C_TIMESTAMP_INVALID));
			}
		}
        }

        //@Override
        override /*protected*/internal void _convert(ESignatureType aType, Dictionary<String, Object> aParameters)
        {
            aParameters[AllEParameters.P_SIGNER_INFO] = mSignerInfo;
            if (aType.Equals(ESignatureType.TYPE_BES) || aType.Equals(ESignatureType.TYPE_EPES) || aType == ESignatureType.TYPE_EST)
            {
                base._convert(aType, aParameters);
                _addESX1Attributes(aParameters);
            }
            else if (aType == ESignatureType.TYPE_ESC || aType == ESignatureType.TYPE_ESX_Type1)
            {
                _addESX1Attributes(aParameters);
            }
            else
                throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to ES_X1");

        }

        /*@Override
        public ESignatureType getType() 
        {
            return ESignatureType.TYPE_ESX_Type1;
        }*/
    }
}
