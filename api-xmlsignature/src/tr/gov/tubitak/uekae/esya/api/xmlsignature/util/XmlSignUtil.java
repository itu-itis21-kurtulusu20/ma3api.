package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.PathValidationRecord;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

import java.util.List;

/**
 * @author ayetgin
 */
public class XmlSignUtil
{

    public static String verificationInfo(CertificateStatusInfo csi)
    {

        List<CheckResult> details = csi.getDetails();
        List<PathValidationRecord> pathItems = csi.getValidationHistory();

        StringBuffer info = new StringBuffer();
        ECertificate signingCert = csi.getCertificate();
        if (signingCert!=null){
            info.append("Issuer: ").append(signingCert.getIssuer().stringValue()).append("\n");
            info.append("Subject: ").append(signingCert.getSubject().stringValue()).append("\n");
            info.append("Seri no: ").append(signingCert.getSerialNumberHex()).append("\n");
        }
        info.append("Certificate status: ").append(csi.getCertificateStatus()).append("\n");
        info.append("Certificate verification time: ").append(csi.getValidationTime().getTime()).append('\n');
        if (details != null)
            for (CheckResult checkResult : details) {
                info.append("- ").append(checkResult.getResultText()).append('\n');
            }

        info.append("\nPATH\n");
        if (pathItems != null)
            for (PathValidationRecord pathItem : pathItems) {
                List<ECertificate> path = pathItem.getPath();
                for (ECertificate cert : path) {
                    info.append("- '").append(cert.getSubject().stringValue())
                            .append("'; serial: ").append(cert.getSerialNumberHex()).append('\n');
                }
            }

        return info.toString();
    }

    /*
    public static void addTimestampValidationData(XAdESTimeStamp aTimeStamp, XMLSignature aSignature) throws XMLSignatureException
    {
        for (int i = 0; i < aTimeStamp.getEncapsulatedTimeStampCount(); i++) {
            EncapsulatedTimeStamp ets = aTimeStamp.getEncapsulatedTimeStamp(i);

            SignedDataValidationResult sdvr = XmlSignUtil.validateTimeStamp(ets);

            CertificateValues certVals = new CertificateValues(aTimeStamp.getContext());
            RevocationValues revVals = new RevocationValues(aTimeStamp.getContext());

            for (SignatureValidationResult svr : sdvr.getSDValidationResults()) {
                XmlSignUtil.fillValidationData(svr.getCertStatusInfo(), certVals, revVals);
            }

            List<ECertificate> tsCertificates = ets.getSignedData().getCertificates();
            if (tsCertificates != null && tsCertificates.size() > 0) {
                certVals = removeDuplicateValidationData(certVals, tsCertificates);
            }

            TimeStampValidationData tsvd = new TimeStampValidationData(aTimeStamp.getContext(), certVals, revVals);
            tsvd.setURI("#" + aTimeStamp.getId());

            aSignature.createOrGetQualifyingProperties().createOrGetUnsignedProperties()
                    .getUnsignedSignatureProperties().addTimeStampValidationData(tsvd, aTimeStamp);
        }

    }


    public static void fillValidationData(CertificateStatusInfo csi,
                                          CertificateValues aCertificateValues,
                                          RevocationValues aRevocationValues)
            throws XMLSignatureException
    {
        int counter = 0;
        while (csi != null) {

            aCertificateValues.addCertificate(csi.getCertificate());

            CRLStatusInfo crlStatus = csi.getCRLInfo();
            if (crlStatus != null) {
                aRevocationValues.addCRL(crlStatus.getCRL());
            }

            CRLStatusInfo deltaCrlStatus = csi.getDeltaCRLInfo();
            if (deltaCrlStatus != null) {
                aRevocationValues.addCRL(deltaCrlStatus.getCRL());
            }

            OCSPResponseStatusInfo ocspStatus = csi.getOCSPResponseInfo();
            if (ocspStatus != null) {
                aRevocationValues.addOCSPResponse(ocspStatus.getOCSPResponse());
            }

            csi = csi.getSigningCertficateInfo();
            counter++;
        }
    }

    // todo

    public static CertificateValues removeDuplicateValidationData(CertificateValues aCertificateValues,
                                                                  List<ECertificate> aCertificates)
            throws XMLSignatureException
    {
        CertificateValues result = new CertificateValues(aCertificateValues.getContext());
        for (int i = 0; i < aCertificateValues.getCertificateCount(); i++) {
            ECertificate current = aCertificateValues.getCertificate(i);
            if (!aCertificates.contains(current)) {
                result.addCertificate(current);
            }
        }

        return result;
    }

    public static SignedDataValidationResult validateTimeStamp(EncapsulatedTimeStamp aTimeStamp)
            throws XMLSignatureException
    {
        byte[] input = aTimeStamp.getContentInfo().getEncoded();

        Hashtable<String, Object> params = new Hashtable<String, Object>();

        ValidationConfig validationConfig = aTimeStamp.getContext().getConfig().getValidationConfig();
        String configFile = validationConfig.getCertificateValidationPolicyFile();

        try {

            ValidationPolicy policy = PolicyReader.readValidationPolicy(configFile);

            params.put(AllEParameters.P_CERT_VALIDATION_POLICY, policy);

            SignedDataValidation sd = new SignedDataValidation();

            Context context = aTimeStamp.getContext();
            CertValidationData validationData = context.getValidationData();

            params.put(AllEParameters.P_INITIAL_CERTIFICATES, validationData.getCertificates());
            params.put(AllEParameters.P_INITIAL_CRLS, validationData.getCrls());
            params.put(AllEParameters.P_INITIAL_OCSP_RESPONSES, validationData.getOcspResponses());

            params.put(AllEParameters.P_SIGNING_TIME, aTimeStamp.getTime());
            params.put(AllEParameters.P_GRACE_PERIOD, new Long(validationConfig.getGracePeriodInSeconds()));
            params.put(AllEParameters.P_REVOCINFO_PERIOD, new Long(validationConfig.getLastRevocationPeriodInSeconds()));

            return sd.verify(input, params);
        }
        catch (Exception e) {
            throw new XMLSignatureException(e, "Cant verify timestamp");
        }
    }     */

}
