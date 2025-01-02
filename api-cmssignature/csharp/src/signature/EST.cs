using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.src.util;

//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class EST : BES
    {
        public EST(BaseSignedData aSD)
            : base(aSD)
        {
            mSignatureType = ESignatureType.TYPE_EST;
        }

        public EST(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {
            mSignatureType = ESignatureType.TYPE_EST;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {
            _addESTAttributes(aParameters);
        }

        private void _addESTAttributes(Dictionary<String, Object> aParameters)
        {
            aParameters[AllEParameters.P_SIGNER_INFO] = mSignerInfo;

            //Add signaturetimestamp attribute
            SignatureTimeStampAttr attr = new SignatureTimeStampAttr();
            attr.setParameters(aParameters);
            attr.setValue();
            mSignerInfo.addUnsignedAttribute(attr.getAttribute());

            bool isP4;
            try
            { //if it is P4, add signature timestamp validation data
                isP4 = getSignerInfo().getProfile() == TurkishESigProfile.P4_1;
                if (isP4)
                {
                    EAttribute signatureTimeStamp = attr.getAttribute();
                    _addTSCertRevocationValues(signatureTimeStamp, aParameters, true);
                }
            }
            catch (Exception e)
            {
                 throw new CMSSignatureException(Msg.getMsg(Msg.GETTING_TS_CERTIFICATE_VALIDATION_DATA_ERROR), e);
            }

            Boolean validateTS=(Boolean)aParameters[EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING];

		if (validateTS && !isP4) {
			SignatureTimeStampAttrChecker signatureTimeStampAttrChecker = new SignatureTimeStampAttrChecker();
			Dictionary<String, Object> paramaters  = new Dictionary<String, Object>(aParameters);
			paramaters.Remove(AllEParameters.P_SIGNING_CERTIFICATE);
            signatureTimeStampAttrChecker.setParameters(paramaters);
			CheckerResult aCheckerResult = new CheckerResult();
			Boolean result = signatureTimeStampAttrChecker.check(this,aCheckerResult);
			if (!result) {
				logger.Error(aCheckerResult.ToString());
				throw new CMSSignatureException(
                        Msg.getMsg(Msg.SIGNATURE_TIMESTAMP_INVALID));
			}
		}



            Object validateCert = null;
            aParameters.TryGetValue(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, out validateCert);

            //If signature type is above from EST, it must validate certificate to collect validation
            //data
            if (mSignatureType == ESignatureType.TYPE_EST && (Boolean)validateCert == false)
                return;
            else
            {
                //Validate certificate for the time in signaturetimestamp
                Object cer = null;
                aParameters.TryGetValue(AllEParameters.P_SIGNING_CERTIFICATE, out cer);
                //Get time information from timestamp
                DateTime? d = _getTimeFromSignatureTS(mSignerInfo);

                if (logger.IsDebugEnabled)
                {
                    logger.Debug(cer.ToString());
                    logger.Debug("Certificate will be validated according to the time in signaturetimestamp:" + DateUtil.formatDateByDayMonthYear24hours(d.Value));
                }

                _validateCertificate((ECertificate)cer, aParameters, d,true);
            }
        }

        //@Override
        internal /*protected*/ override void _convert(ESignatureType aType, Dictionary<String, Object> aParameters)
        {
            if (aType.Equals(ESignatureType.TYPE_BES) || aType.Equals(ESignatureType.TYPE_EPES))
            {
                _addESTAttributes(aParameters);
            }
            else if (aType == ESignatureType.TYPE_EST)
            {
                throw new CMSSignatureException("Signature type is already ES_T.");
            }
            else
                throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to ES_T");
        }
    }
}