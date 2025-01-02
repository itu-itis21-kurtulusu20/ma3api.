package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ValidationConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CertificateValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.TimeStampValidationData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.CertRevInfoExtractor;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

/**
 * @author ayetgin
 */
public class EncapsulatedTimeStampValidator {

    private static Logger logger = LoggerFactory.getLogger(EncapsulatedTimeStampValidator.class);

    private XAdESTimeStamp timeStamp;
    private XMLSignature signature;

    public EncapsulatedTimeStampValidator(XMLSignature signature, XAdESTimeStamp timeStamp){
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
    public ValidationResult verify(EncapsulatedTimeStamp aTimeStamp, Calendar validationTime){
        ValidationResult result = new ValidationResult(getClass());
        byte[] input = aTimeStamp.getContentInfo().getEncoded();
        boolean lastTimestamp = validationTime==null;

        Hashtable<String, Object> params = new Hashtable<String, Object>();
        ValidationConfig validationConfig = aTimeStamp.getContext().getConfig().getValidationConfig();

        try {

            //ValidationPolicy policy = validationConfig.getCertificateValidationPolicy(validationTime==null);
            //ValidationPolicy policy = validationConfig.getCertValidationPolicies().getPolicyFor(CertValidationPolicies.CertificateType.TimeStampingCertificate);

            params.put(AllEParameters.P_CERT_VALIDATION_POLICIES, validationConfig.getCertValidationPolicies());

            SignedDataValidation sd = new SignedDataValidation();
            CertRevInfoExtractor extractor = new CertRevInfoExtractor();

            Context context = aTimeStamp.getContext();

            /*
            List<ECertificate> certificates = new ArrayList<ECertificate>();
            List<ECRL> crls = new ArrayList<ECRL>();
            List<EOCSPResponse> ocspResponses = new ArrayList<EOCSPResponse>();

            CertValidationData validationData = context.getValidationData(signature);
            certificates.addAll(validationData.getCertificates());
            crls.addAll(validationData.getCrls());
            ocspResponses.addAll(validationData.getOcspResponses());

            for (TimeStampValidationData vd : validationData.getTSValidationData().values()){
                if (vd.getCertificateValues()!=null){
                    certificates.addAll(vd.getCertificateValues().getAllCertificates());
                }
                if (vd.getRevocationValues()!=null){
                    crls.addAll(vd.getRevocationValues().getAllCRLs());
                    ocspResponses.addAll(vd.getRevocationValues().getAllOCSPResponses());
                }
            }

            params.put(AllEParameters.P_INITIAL_CERTIFICATES, certificates);
            params.put(AllEParameters.P_INITIAL_CRLS, crls);
            params.put(AllEParameters.P_INITIAL_OCSP_RESPONSES, ocspResponses);  */

            params.putAll(extractor.collectAllInitialValidationDataFromContextAsParams(context));

            params.put(AllEParameters.P_GRACE_PERIOD, new Long(validationConfig.getGracePeriodInSeconds()));
            params.put(AllEParameters.P_REVOCINFO_PERIOD, new Long(validationConfig.getLastRevocationPeriodInSeconds()));
            params.put(AllEParameters.P_IGNORE_GRACE, true);//Boolean.valueOf(!validationConfig.isUseValidationDataPublishedAfterCreation()));

            params.put(AllEParameters.P_VALIDATION_WITHOUT_FINDERS, !lastTimestamp);

            SignedDataValidationResult sdvr = null;
            if (!lastTimestamp){
                params.put(AllEParameters.P_SIGNING_TIME, validationTime);
                sdvr = sd.verify(input, params);
            }
            // todo decide if second validation should be binded to strictreferenceuse parameter
            boolean secondPass = false;
            if (sdvr==null || (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)){
                logger.debug("SignedData is not verified, try validation in timestamp time");

                params.put(AllEParameters.P_SIGNING_TIME, aTimeStamp.getTime());
                sdvr = sd.verify(input, params);
                secondPass = true;
            }

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID){

                context.addTimestampValidationResult(signature, sdvr);
                CertValidationData validationData = context.getValidationData(signature);

                if (validationData.getTSValidationDataForTS(timeStamp)==null) {

                    // add used validation data so that it can bu used for other (counter signature) timestamp validation
                    CertificateStatusInfo csi = sdvr.getSDValidationResults().get(0).getCertStatusInfo();
                    Pair<CertificateValues, RevocationValues> vd = extractor.extractValidationDataFromCSI(context, csi);
                    vd = extractor.removeDuplicateReferences(context, signature, vd.getObject1(), vd.getObject2(), aTimeStamp.getSignedData());

                    TimeStampValidationData timeStampValidationData = new TimeStampValidationData(context, vd.getObject1(), vd.getObject2());
                    validationData.addTSValidationData(timeStamp, timeStampValidationData);
                }
                logger.debug("SignedData is verified");
                ValidationResultType resultType = secondPass && !lastTimestamp ? ValidationResultType.WARNING : ValidationResultType.VALID;
                // todo i18n
                String message = secondPass ? "Timestamp signature verified in own timestamp time." : I18n.translate("validation.timestamp.signature.verified");

                result.setStatus(resultType, I18n.translate("validation.check.encapsulatedTS"), message, sdvr.toString());
                return result;
            }
            else {
                String message = null;
                SignatureValidationResult svr = sdvr.getSDValidationResults().get(0);
                CertificateStatusInfo csi =  svr.getCertificateStatusInfo();
                if (csi.getCertificateStatus()!= CertificateStatus.VALID)
                    message = csi.getDetailedMessage();
                else
                    message = svr.getCheckResult();

                result.setStatus(ValidationResultType.INVALID, I18n.translate("validation.check.encapsulatedTS"), message, sdvr.toString());
                return result;
            }

        } catch (Exception x){
            logger.error("Cant verify Timestamp signature", x);
            result.setStatus(ValidationResultType.INCOMPLETE, I18n.translate("validation.check.encapsulatedTS"), I18n.translate("validation.timestamp.signature.verificationError"), x.getMessage());
            return result;
        }

    }

}
