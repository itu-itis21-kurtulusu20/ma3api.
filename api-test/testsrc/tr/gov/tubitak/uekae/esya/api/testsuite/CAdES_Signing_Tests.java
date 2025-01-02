package tr.gov.tubitak.uekae.esya.api.testsuite;


import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.PathValidationRecord;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevocationCheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;

import java.io.FileInputStream;
import java.util.*;

import static tr.gov.tubitak.uekae.esya.api.testsuite.TestSuiteCommonMethods.*;

/*
#     API kapsamının dışındaki senaryolar:
#      | QCA1_19.p12 |  Monetary Limit Included             |                                                               |
#      | QCA1_20.p12 |  Usage Limit Included                |                                                               |
#      | QCA11.p12   |  Subroot's OCSP Certificate Revoked  | (SIL ile doğrulanıyor)                                        |
#      | QCB.p12     |  SIGNATURE_CONTROL_FAILURE           | Root Certificate Signature Forged(Subroot ta hata alınıyor)   |
*/

@RunWith(Enclosed.class)
public class CAdES_Signing_Tests {

    static byte[] signedData;

    static ValidationPolicy validationPolicy = null;
    static Hashtable<String, Object> params = new Hashtable<String, Object>();

    @RunWith(Parameterized.class)
    public static class CAdES_Signing_Positive {

        private String pfx;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"QCA1_1.p12"},
                    {"QCA1_2.p12"},
                    {"QCA12.p12 "}
            });
        }

        public CAdES_Signing_Positive(String pfx){
            this.pfx = pfx;
        }

        @Test
        public void runTests() throws Throwable
        {
            signWithPfx(pfx);
            checkIfValidationResultIsValid();
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_Signing_Negative_Due_To_Failure_Of_Certificate_Checker{

        private String pfx;
        private String checker;
        private String error;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"QCA1_3.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "Certificate key usage field is invalid."}, // Non-repudiation absent
                    {"QCA1_4.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no usetr notice in the certificate."}, // CP user notice statement absent
                    {"QCA1_5.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no qualified extension in the certificate."}, //ETSI QC statement id absent
                    {"QCA1_6.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no qualified extension in the certificate."}, //BTK QC Statement id absent
                    {"QCA1_8.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateDateChecker", "Certificate is expired"}, //Expired
                    {"QCA1_9.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateSignatureChecker", "Certificate Signature is not Validated"} //Signature Forged
            });
        }

        public CAdES_Signing_Negative_Due_To_Failure_Of_Certificate_Checker(String pfx, String checker, String error){
           this.pfx = pfx;
           this.checker = checker;
           this.error = error;
        }

        @Test
        public void runTests() throws Throwable
        {
            signWithPfx(pfx);
            checkIfValidationResultIsInvalidDueToFailureOfCertificateChecker(checker, error);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_Signing_Negative_Due_To_Failure_Of_OCSP_Certificate_Checker{

        private String pfx;
        private String checker;
        private String error;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"QCA1_16.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateDateChecker", "Certificate is expired"}, // Expired OCSP Certificate
                    {"QCA1_17.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateSignatureChecker", "Certificate Signature is not Validated"}, // OCSP Certificate Signature Forged
                    {"QCA1_18.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker", "Certificate is revoked in CRL"}, //Revoked OCSP Certificate
                   });
        }

        public CAdES_Signing_Negative_Due_To_Failure_Of_OCSP_Certificate_Checker(String pfx, String checker, String error){
            this.pfx = pfx;
            this.checker = checker;
            this.error = error;
        }

        @Test
        public void runTests() throws Throwable
        {
            signWithPfx(pfx);
            checkIfValidationResultIsInvalidDueToFailureOfOCSPCertificateChecker(checker, error);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_Signing_Negative_Due_To_Certificate_Revocation_Checker {

        private String pfx;
        private String checker;
        private String error;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"QCA1_10.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker", "Certificate is revoked in CRL"}, //Revoked in CRL
                    {"QCA1_11.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker", "Certificate is revoked in OCSP answer"}, //Revoked in OCSP
                    {"QCA1_12.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker",  "CRL is not found"}, // Expired in CRL
                    {"QCA1_13.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker",  "CRL is not found"}, //CRL Signature Forged
                    {"QCA1_14.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //Expired OCSP Response
                    {"QCA1_15.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //OCSP Response Signature Forged
                    {"QCA1_21.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //Wrong Certificate's OCSP Response
            });
        }

        public CAdES_Signing_Negative_Due_To_Certificate_Revocation_Checker(String pfx, String checker, String error){
            this.pfx = pfx;
            this.checker = checker;
            this.error = error;
        }

        @Test
        public void runTests() throws Throwable
        {
            signWithPfx(pfx);
            checkIfValidationResultIsInvalidDueToFailureOfCertificateRevocationChecker(checker, error);
        }
    }

    @RunWith(Parameterized.class)
    public static class CAdES_Signing_Negative_Due_To_Subroot_Failure {

        private String pfx;
        private String error;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{
                    {"QCA2.p12", "Certificate signature check failure."}, // Subroot Certificate Signature Forged
                    {"QCA3.p12", "Certificate is revoked."}, // Subroot Certificate Revoked in CRL
                    {"QCA4.p12", "Certificate revocation check failure."}, //Subroot CRL Expired
                    {"QCA5.p12", "Certificate revocation check failure."}, // Subroot CRL Signature Forged
                    {"QCA6.p12", "Certificate is revoked."}, // Subroot Revoked in OCSP
                    {"QCA7.p12", "Certificate revocation check failure."}, // Subroot's OCSP Response Expired
                    {"QCA8.p12", "Certificate revocation check failure."}, //Subroot's OCSP Response Signature Forged
                    {"QCA9.p12", "Certificate revocation check failure."}, //Subroot's OCSP Certificate Expired
                    {"QCA10.p12", "Certificate revocation check failure."}, //Subroot's OCSP Certificate Signature Forged
                    {"QCB.p12", "Certificate signature check failure."} //Subroot Certificate Signature Forged
            });
        }

        public CAdES_Signing_Negative_Due_To_Subroot_Failure(String pfx, String error){
            this.pfx = pfx;
            this.error = error;
        }

        @Test
        public void runTests() throws Throwable
        {
            signWithPfx(pfx);
            checkIfValidationResultIsInvalidDueToFailureOfSubroot(error);
        }
    }

    private static void prepareValidationPolicy() throws Exception {

        String rootFolder = TestSuiteCommonMethods.FOLDER;
        FileInputStream fileInputStream = new FileInputStream(rootFolder + "config\\policy\\" + "testsuite-policy.xml");
        validationPolicy = PolicyReader.readValidationPolicy(fileInputStream);
        params.put(EParameters.P_IGNORE_GRACE, true);
        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);
        params.put(EParameters.P_FORCE_STRICT_REFERENCE_USE, true);
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
        fileInputStream.close();
    }

    public static void signWithPfx(String pfx) throws Throwable {

        parsePfx(pfx);

        prepareValidationPolicy();
        I18nSettings.setLocale(Locale.getDefault());

        BaseSignedData baseSignedData = new BaseSignedData();
        ISignable content = new SignableByteArray("test".getBytes());
        baseSignedData.addContent(content);
        baseSignedData.addSigner(ESignatureType.TYPE_BES, certificate, signer, null, params);
        signer.reset();
        signedData = baseSignedData.getEncoded();
    }

    public static void checkIfValidationResultIsValid() throws Throwable {

        SignedDataValidation signedDataValidation = new SignedDataValidation();
        SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, params);

        System.out.println(validationResult);
        Assert.assertNotNull(validationResult);

        SignedData_Status status = validationResult.getSDStatus();
        Assert.assertEquals(validationResult.toString(), SignedData_Status.ALL_VALID, status);
    }

    public static void checkIfValidationResultIsInvalidDueToFailureOfCertificateChecker(String checkerName, String errorMessage) throws Throwable {

        SignedDataValidation signedDataValidation = new SignedDataValidation();
        SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, params);

        System.out.println(validationResult);
        Assert.assertNotNull(validationResult);

        List<CheckResult> selfCheckResultList = validationResult.getSDValidationResults().get(0).getCertificateStatusInfo().getDetails();
        boolean checkerFailed = TestSuiteCommonMethods.runCertificateSelfController(selfCheckResultList, checkerName, errorMessage);

        Assert.assertTrue(checkerFailed);
    }

    public static void checkIfValidationResultIsInvalidDueToFailureOfOCSPCertificateChecker(String checkerName, String errorMessage) throws Throwable {

        SignedDataValidation signedDataValidation = new SignedDataValidation();
        SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, params);

        System.out.println(validationResult);
        Assert.assertNotNull(validationResult);

        CertificateStatusInfo signingCertficateInfo = validationResult.getSDValidationResults().get(0).getCertificateStatusInfo().getOCSPResponseInfoList().get(0).getSigningCertficateInfo();
        boolean checkerFailed = TestSuiteCommonMethods.runOCSPCertificateController(signingCertficateInfo, checkerName, errorMessage);

        Assert.assertTrue(checkerFailed);
    }

    public static void checkIfValidationResultIsInvalidDueToFailureOfCertificateRevocationChecker(String checkerName, String errorMessage) throws Throwable {

        SignedDataValidation signedDataValidation = new SignedDataValidation();
        SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, params);

        System.out.println(validationResult);
        Assert.assertNotNull(validationResult);

        List<RevocationCheckResult> revocationCheckResultList = validationResult.getSDValidationResults().get(0).getCertificateStatusInfo().getRevocationCheckDetails();
        boolean checkerFailed = TestSuiteCommonMethods.runCertificateRevocationController(revocationCheckResultList, checkerName, errorMessage);

        Assert.assertTrue(checkerFailed);
    }

    public static void checkIfValidationResultIsInvalidDueToFailureOfSubroot(String errorMessage) throws Throwable {

        SignedDataValidation signedDataValidation = new SignedDataValidation();
        SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, params);

        System.out.println(validationResult);
        Assert.assertNotNull(validationResult);

        //errorIndex must be 1 for the subroot
        PathValidationRecord pathValidationRecord = validationResult.getSDValidationResults().get(0).getCertificateStatusInfo().getValidationHistory().get(0);
        boolean checkerFailed = TestSuiteCommonMethods.runCertificateSubrootController(pathValidationRecord, errorMessage);

        Assert.assertTrue(checkerFailed);
    }
}
