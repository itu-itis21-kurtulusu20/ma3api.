package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.14 Distribution Points
 *
 * @author ayetgin
 */
public class DistributionPointsTest extends BaseNISTTest {

    // 4.14.1
    @Test
    public void testValidDistributionPointTest1() throws Exception {
        test("ValiddistributionPointTest1.xml", "ValiddistributionPointTest1EE.crt", CertificateStatus.VALID);
    }

    // 4.14.2
    @Test
    public void testInvalidDistributionPointTest2() throws Exception {
        test("ValiddistributionPointTest1.xml", "InvaliddistributionPointTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.3
    @Test
    public void testInvalidDistributionPointTest3() throws Exception {
        test("ValiddistributionPointTest1.xml", "InvaliddistributionPointTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.4
    @Test
    public void testValidDistributionPointTest4() throws Exception {
        test("ValiddistributionPointTest1.xml", "ValiddistributionPointTest4EE.crt", CertificateStatus.VALID);
    }

    // 4.14.5
    @Test
    public void testValidDistributionPointTest5() throws Exception {
        test("ValiddistributionPointTest5.xml", "ValiddistributionPointTest5EE.crt", CertificateStatus.VALID);
    }

    // 4.14.6
    @Test
    public void testInvalidDistributionPointTest6() throws Exception {
        test("ValiddistributionPointTest5.xml", "InvaliddistributionPointTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.7
    @Test
    public void testValidDistributionPointTest7() throws Exception {
        test("ValiddistributionPointTest5.xml", "ValiddistributionPointTest7EE.crt", CertificateStatus.VALID);
    }

    // 4.14.8
    @Test
    public void testInvalidDistributionPointTest8() throws Exception {
        test("ValiddistributionPointTest5.xml", "InvaliddistributionPointTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.9
    @Test
    public void testInvalidDistributionPointTest9() throws Exception {
        test("ValiddistributionPointTest5.xml", "InvaliddistributionPointTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.10
    @Test
    public void testValidNoIssuingDistributionPointTest10() throws Exception {
        test("ValidNoissuingDistributionPointTest10.xml", "ValidNoissuingDistributionPointTest10EE.crt", CertificateStatus.VALID);
    }

    // 4.14.11
    @Test
    public void testInvalidOnlyContainsUserCertsCRLTest11() throws Exception {
        test("InvalidonlyContainsUserCertsTest11.xml", "InvalidonlyContainsUserCertsTest11EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.12
    @Test
    public void testInvalidOnlyContainsCACertsCRLTest12() throws Exception {
        test("InvalidonlyContainsCACertsTest12.xml", "InvalidonlyContainsCACertsTest12EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.13
    @Test
    public void testValidOnlyContainsCACertsCRLTest13() throws Exception {
        test("InvalidonlyContainsCACertsTest12.xml", "ValidonlyContainsCACertsTest13EE.crt", CertificateStatus.VALID);
    }

    // 4.14.14
    @Test
    public void testInvalidOnlyContainsAttributeCertsTest14() throws Exception {
        test("InvalidonlyContainsAttributeCertsTest14.xml", "InvalidonlyContainsAttributeCertsTest14EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.15
    @Test
    public void testInvalidOnlySomeReasonsTest15() throws Exception {
        test("InvalidonlySomeReasonsTest15.xml", "InvalidonlySomeReasonsTest15EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.16
    @Test
    public void testInvalidOnlySomeReasonsTest16() throws Exception {
        test("InvalidonlySomeReasonsTest15.xml", "InvalidonlySomeReasonsTest16EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.17
    @Test
    public void testInvalidOnlySomeReasonsTest17() throws Exception {
        test("InvalidOnlySomeReasonsTest17.xml", "InvalidonlySomeReasonsTest17EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.18
    @Test
    public void testValidOnlySomeReasonsTest18() throws Exception {
        test("ValidonlySomeReasonsTest18.xml", "ValidonlySomeReasonsTest18EE.crt", CertificateStatus.VALID);
    }

    // 4.14.19
    @Test
    public void testValidOnlySomeReasonsTest19() throws Exception {
        test("ValidonlySomeReasonsTest19.xml", "ValidonlySomeReasonsTest19EE.crt", CertificateStatus.VALID);
    }

    // 4.14.20
    @Test
    public void test() throws Exception {
        test("ValidonlySomeReasonsTest19.xml", "InvalidonlySomeReasonsTest20EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.21
    @Test
    public void testInvalidOnlySomeReasonsTest21() throws Exception {
        test("ValidonlySomeReasonsTest19.xml", "InvalidonlySomeReasonsTest21EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.22
    @Test
    public void testValidIDPWithIndirectCRLTest22() throws Exception {
        test("ValidIDPwithindirectCRLTest22.xml", "ValidIDPwithindirectCRLTest22EE.crt", CertificateStatus.VALID);
    }

    // 4.14.23 
    @Test
    public void testInvalidIDPWithIndirectCRLTest23() throws Exception {
        test("ValidIDPwithindirectCRLTest22.xml", "InvalidIDPwithindirectCRLTest23EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.24
    @Test
    public void testValidIDPWithIndirectCRLTest24() throws Exception {
        test("ValidIDPwithindirectCRLTest24.xml", "ValidIDPwithindirectCRLTest24EE.crt", CertificateStatus.VALID);
    }

    // 4.14.26
    @Test
    public void testInvalidIDPWithIndirectCRLTest26() throws Exception {
        test("ValidIDPwithindirectCRLTest24.xml", "InvalidIDPwithindirectCRLTest26EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.27
    @Test
    public void testInvalidCRLIssuerTest27() throws Exception {
        test("InvalidcRLIssuerTest27.xml", "InvalidcRLIssuerTest27EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.28
    @Test
    public void testValidCRLIssuerTest28() throws Exception {
        test("ValidcRLIssuerTest28.xml", "ValidcRLIssuerTest28EE.crt", CertificateStatus.VALID);
    }

    // 4.14.29
    @Test
    public void testValidCRLIssuerTest29() throws Exception {
        test("ValidcRLIssuerTest28.xml", "ValidcRLIssuerTest29EE.crt", CertificateStatus.VALID);
    }

    // 4.14.30
    @Test
    public void testValidCRLIssuerTest30() throws Exception {
        test("ValidcRLIssuerTest30.xml", "ValidcRLIssuerTest30EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.31
    @Test
    public void testInvalidCRLIssuerTest31() throws Exception {
        test("InvalidcRLIssuerTest31.xml", "InvalidcRLIssuerTest31EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.32
    @Test
    public void testInvalidCRLIssuerTest32() throws Exception {
        test("InvalidcRLIssuerTest31.xml", "InvalidcRLIssuerTest32EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.34
    @Test
    public void testInvalidCRLIssuerTest34() throws Exception {
        test("InvalidcRLIssuerTest34.xml", "InvalidcRLIssuerTest34EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.14.35
    @Test
    public void testInvalidCRLIssuerTest35() throws Exception {
        test("InvalidcRLIssuerTest34.xml", "InvalidcRLIssuerTest35EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

}
