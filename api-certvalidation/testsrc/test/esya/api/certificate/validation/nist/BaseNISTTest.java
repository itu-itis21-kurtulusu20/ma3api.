package test.esya.api.certificate.validation.nist;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyQualifierInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author ayetgin
 */
public class BaseNISTTest {

    public static final String SERTIFIKA_DIZIN = "T:\\api-certvalidation\\testdata\\nist\\certs\\";
    public static final String POLITIKA_DIZIN = "T:\\api-certvalidation\\testdata\\nist\\politika\\";

    protected Asn1ObjectIdentifier NIST_POLICY_1 = new Asn1ObjectIdentifier(new int[]{2, 16, 840, 1, 101, 3, 2, 1, 48, 1});
    protected Asn1ObjectIdentifier NIST_POLICY_2 = new Asn1ObjectIdentifier(new int[]{2, 16, 840, 1, 101, 3, 2, 1, 48, 2});
    protected Asn1ObjectIdentifier NIST_POLICY_3 = new Asn1ObjectIdentifier(new int[]{2, 16, 840, 1, 101, 3, 2, 1, 48, 3});

    protected class TestResult {

        public CertificateStatusInfo mCertificateStatusInfo;
        public List<Asn1ObjectIdentifier> mAuthoritiesConstrainedPolicySet;
        public List<Asn1ObjectIdentifier> mUserConstrainedPolicySet;
        private List<PolicyQualifierInfo> mValidQualifierSet;

        public TestResult(CertificateStatusInfo aCertificateStatusInfo,
                          List<Asn1ObjectIdentifier> aAuthoritiesConstrainedPolicySet,
                          List<Asn1ObjectIdentifier> aUserConstrainedPolicySet,
                          List<PolicyQualifierInfo> aValidQualifierSet) {
            mCertificateStatusInfo = aCertificateStatusInfo;
            mAuthoritiesConstrainedPolicySet = aAuthoritiesConstrainedPolicySet;
            mUserConstrainedPolicySet = aUserConstrainedPolicySet;
            mValidQualifierSet = aValidQualifierSet;
        }

        public CertificateStatusInfo getCertificateStatusInfo() {
            return mCertificateStatusInfo;
        }

        public List<Asn1ObjectIdentifier> getAuthoritiesConstrainedPolicySet() {
            return mAuthoritiesConstrainedPolicySet;
        }

        public List<Asn1ObjectIdentifier> getUserConstrainedPolicySet() {
            return mUserConstrainedPolicySet;
        }

        public List<PolicyQualifierInfo> getValidQualifierSet() {
            return mValidQualifierSet;
        }
    }

    protected TestResult sertifikaDogrula(String politikaPath, String aCertPath)
            throws Exception {
        ECertificate sertifika = ECertificate.readFromFile(SERTIFIKA_DIZIN + aCertPath);

        ValidationPolicy policy = PolicyReader.readValidationPolicy(POLITIKA_DIZIN + politikaPath);
        ValidationSystem validationSystem = CertificateValidation.createValidationSystem(policy);

        //  crl valid from Apr 19 17:57:20 EEST 2001
        Calendar validationTime = Calendar.getInstance();
        Date validationDate = new SimpleDateFormat("dd.MM.yyyy").parse("20.04.2010");
        validationTime.setTime(validationDate);
        validationSystem.setBaseValidationTime(validationTime);

        //validationSystem.setValidCertificateSet(aValidCertificates);
        CertificateController checker = new CertificateController();
        CertificateStatusInfo csi = checker.check(validationSystem, sertifika);


        return new TestResult(csi,
                checker.getAuthoritiesConstrainedPolicySet(),
                checker.getUserConstrainedPolicySet(),
                checker.getValidQualifierSet());
    }

    public void test(String aPolicyPath, String aCertPath, CertificateStatus aExpectedOutcome)
            throws Exception {
        assertEquals(aExpectedOutcome, sertifikaDogrula(aPolicyPath, aCertPath)
                .getCertificateStatusInfo().getCertificateStatus());
    }

}
