using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{
    public class EncapsulatedTimeStampValidator
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly XAdESTimeStamp timeStamp;
        private readonly XMLSignature signature;

        public EncapsulatedTimeStampValidator(XMLSignature signature, XAdESTimeStamp timeStamp)
        {
            this.signature = signature;
            this.timeStamp = timeStamp;
        }

        /**
         * Validate timestamp in validationTime and repeat validation in timestamps own time
         *
         * @param aTimeStamp
         * @param validationTime Give this parameter if and only if you dont want to validate the timestamp in its own time
         *                       first
         * @return
         */
        public ValidationResult verify(EncapsulatedTimeStamp aTimeStamp, DateTime? validationTime){
            
            ValidationResult result = new ValidationResult(GetType());
            byte[] input = aTimeStamp.ContentInfo.getEncoded();
            bool lastTimestamp = validationTime == null;

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            ValidationConfig validationConfig = aTimeStamp.Context.Config.ValidationConfig;

            try {

                //ValidationPolicy policy = validationConfig.getCertificateValidationPolicy(validationTime==null);

                parameters.Add(AllEParameters.P_CERT_VALIDATION_POLICIES, validationConfig.getCertValidationPolicies());

                SignedDataValidation sd = new SignedDataValidation();
                CertRevInfoExtractor extractor = new CertRevInfoExtractor();
                Context context = aTimeStamp.Context;

                /*
                List<ECertificate> certificates = new List<ECertificate>();
                List<ECRL> crls = new List<ECRL>();
                List<EOCSPResponse> ocspResponses = new List<EOCSPResponse>();

                CertValidationData validationData = context.getValidationData(signature);
                certificates.AddRange(validationData.Certificates);
                crls.AddRange(validationData.Crls);
                ocspResponses.AddRange(validationData.OcspResponses);

                foreach (TimeStampValidationData vd in validationData.TSValidationData.Values){
                    if (vd.CertificateValues!=null){
                        certificates.AddRange(vd.CertificateValues.AllCertificates);
                    }
                    if (vd.RevocationValues!=null){
                        crls.AddRange(vd.RevocationValues.AllCRLs);
                        ocspResponses.AddRange(vd.RevocationValues.AllOCSPResponses);
                    }
                }

                parameters.Add(AllEParameters.P_INITIAL_CERTIFICATES, certificates);
                parameters.Add(AllEParameters.P_INITIAL_CRLS, crls);
                parameters.Add(AllEParameters.P_INITIAL_OCSP_RESPONSES, ocspResponses);*/

                IDictionary<String, Object> collectedParams = extractor.collectAllInitialValidationDataFromContextAsParams(context);
                foreach (String key in collectedParams.Keys)
                {
                    parameters[key] = collectedParams[key];
                }



                parameters.Add(AllEParameters.P_GRACE_PERIOD, (long)(validationConfig.GracePeriodInSeconds));
                parameters.Add(AllEParameters.P_REVOCINFO_PERIOD, (long)(validationConfig.LastRevocationPeriodInSeconds));
                parameters.Add(AllEParameters.P_IGNORE_GRACE, true);//Boolean.valueOf(!validationConfig.isUseValidationDataPublishedAfterCreation()));

                parameters.Add(AllEParameters.P_VALIDATION_WITHOUT_FINDERS, !lastTimestamp);

                SignedDataValidationResult sdvr = null;
                if (!lastTimestamp){
                    parameters.Add(AllEParameters.P_SIGNING_TIME, validationTime);
                    sdvr = sd.verify(input, parameters);
                }
                // todo decide if second validation should be binded to strictreferenceuse parameter
                bool secondPass = false;
                if (sdvr==null || (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)){
                    logger.Debug("SignedData is not verified, try validation in timestamp time");
                    // JAVA'daki put metodu key'in aynisi varsa da uzerine yaziyor, burda once remove etmek icab etti, 13.9.13/su
                    parameters.Remove(AllEParameters.P_SIGNING_TIME);
                    parameters.Add(AllEParameters.P_SIGNING_TIME, aTimeStamp.Time);
                    sdvr = sd.verify(input, parameters);
                    secondPass = true;
                }

                if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID){

                    context.addTimestampValidationResult(signature, sdvr);
                    CertValidationData validationData = context.getValidationData(signature);

                    if (validationData.getTSValidationDataForTS(timeStamp) == null)
                    {

                        // add used validation data so that it can bu used for other (counter signature) timestamp validation
                        CertificateStatusInfo csi = sdvr.getSDValidationResults()[0].getCertStatusInfo();

                        Pair<CertificateValues, RevocationValues> vd = extractor.extractValidationDataCSI(context, csi);
                        vd = extractor.removeDuplicateReferences(context, signature, vd.getmObj1(), vd.getmObj2(), aTimeStamp.SignedData);

                        TimeStampValidationData timeStampValidationData = new TimeStampValidationData(context,
                                                                                                      vd.getmObj1(),
                                                                                                      vd.getmObj2());
                        validationData.addTSValidationData(timeStamp, timeStampValidationData);
                    }
                    logger.Debug("SignedData is verified");
                    ValidationResultType resultType = secondPass && !lastTimestamp ? ValidationResultType.WARNING : ValidationResultType.VALID;
                    // todo i18n
                    String message = secondPass ? "Timestamp signature verified in own timestamp time." : I18n.translate("validation.timestamp.signature.verified");

                    result.setStatus(resultType, I18n.translate("validation.check.encapsulatedTS"), message, sdvr.ToString());
                    return result;
                }
                else {
                    //result.setStatus(ValidationResultType.INVALID, I18n.translate("validation.check.encapsulatedTS"), I18n.translate("validation.timestamp.signature.invalid"), sdvr.ToString());
                    String message = null;
                    cmssignature.validation.SignatureValidationResult svr = sdvr.getSDValidationResults()[0];
                    CertificateStatusInfo csi = svr.getCertificateStatusInfo();
                    if (csi.getCertificateStatus() != CertificateStatus.VALID)
                    {
                        message = csi.getDetailedMessage();
                    }
                    else
                    {
                        message = svr.getCheckResult();
                    }
                    result.setStatus(ValidationResultType.INVALID, I18n.translate("validation.check.encapsulatedTS"), message, sdvr.ToString());
                    return result;
                }

            } catch (Exception x){
                logger.Error("Cant verify Timestamp signature", x);
                result.setStatus(ValidationResultType.INCOMPLETE, I18n.translate("validation.check.encapsulatedTS"), I18n.translate("validation.timestamp.signature.verificationError"), x.Message);
                return result;
            }

        }
    }
}
