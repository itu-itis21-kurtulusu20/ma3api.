package test.esya.api.certificate.validation.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.certificate.validation.CertificateValidationTest;
import test.esya.api.certificate.validation.nist.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CertificateValidationTest.class,
        SignatureVerificationTest.class, //4.1
        ValidityPeriodsTest.class, //4.2
        NameChainingTest.class, //4.3
        BasicRevocationTest.class, //4.4
        SelfIssuedTest.class, //4.5
        BasicConstraintsTest.class, //4.6
        KeyUsageTest.class, //4.7
        CertificatePoliciesTest.class, //4.8
        RequireExplicitPolicyTest.class, //4.9
        PolicyMappingsTest.class, //4.10
        InhibitPolicyTest.class, //4.11
        InhibitAnyPolicyTest.class, //4.12
        NameConstraintsTest.class, //4.13
        DistributionPointsTest.class, //4.14
        DeltaCRLTest.class, //4.15
        PrivateExtensionsTest.class //4.16
})
public class CertValidationTestSuite {
}
