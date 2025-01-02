using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 *      4.14 Distribution Points
 * 
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    class DistributionPointsTest : BaseNISTTest
    {
        // 4.14.1
        [Test]
        public void testValidDistributionPointTest1()
        {
            test("ValiddistributionPointTest1.xml", "ValiddistributionPointTest1EE.crt", CertificateStatus.VALID);
        }

        // 4.14.2
        [Test]
        public void testInvalidDistributionPointTest2()
        {
            test("ValiddistributionPointTest1.xml", "InvaliddistributionPointTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.3
        [Test]
        public void testInvalidDistributionPointTest3()
        {
            test("ValiddistributionPointTest1.xml", "InvaliddistributionPointTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.4
        [Test]
        public void testValidDistributionPointTest4()
        {
            test("ValiddistributionPointTest1.xml", "ValiddistributionPointTest4EE.crt", CertificateStatus.VALID);
        }

        // 4.14.5
        [Test]
        public void testValidDistributionPointTest5()
        {
            test("ValiddistributionPointTest5.xml", "ValiddistributionPointTest5EE.crt", CertificateStatus.VALID);
        }

        // 4.14.6
        [Test]
        public void testInvalidDistributionPointTest6()
        {
            test("ValiddistributionPointTest5.xml", "InvaliddistributionPointTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.7
        [Test]
        public void testValidDistributionPointTest7()
        {
            test("ValiddistributionPointTest5.xml", "ValiddistributionPointTest7EE.crt", CertificateStatus.VALID);
        }

        // 4.14.8
        [Test]
        public void testInvalidDistributionPointTest8()
        {
            test("ValiddistributionPointTest5.xml", "InvaliddistributionPointTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.9
        [Test]
        public void testInvalidDistributionPointTest9()
        {
            test("ValiddistributionPointTest5.xml", "InvaliddistributionPointTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.10
        [Test]
        public void testValidNoIssuingDistributionPointTest10()
        {
            test("ValidNoissuingDistributionPointTest10.xml", "ValidNoissuingDistributionPointTest10EE.crt", CertificateStatus.VALID);
        }

        // 4.14.11
        [Test]
        public void testInvalidOnlyContainsUserCertsCRLTest11()
        {
            test("InvalidonlyContainsUserCertsTest11.xml", "InvalidonlyContainsUserCertsTest11EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.12
        [Test]
        public void testInvalidOnlyContainsCACertsCRLTest12()
        {
            test("InvalidonlyContainsCACertsTest12.xml", "InvalidonlyContainsCACertsTest12EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.13
        [Test]
        public void testValidOnlyContainsCACertsCRLTest13()
        {
            test("InvalidonlyContainsCACertsTest12.xml", "ValidonlyContainsCACertsTest13EE.crt", CertificateStatus.VALID);
        }

        // 4.14.14
        [Test]
        public void testInvalidOnlyContainsAttributeCertsTest14()
        {
            test("InvalidonlyContainsAttributeCertsTest14.xml", "InvalidonlyContainsAttributeCertsTest14EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.15
        [Test]
        public void testInvalidOnlySomeReasonsTest15()
        {
            test("InvalidonlySomeReasonsTest15.xml", "InvalidonlySomeReasonsTest15EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.16
        [Test]
        public void testInvalidOnlySomeReasonsTest16()
        {
            test("InvalidonlySomeReasonsTest15.xml", "InvalidonlySomeReasonsTest16EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.17
        [Test]
        public void testInvalidOnlySomeReasonsTest17()
        {
            test("InvalidOnlySomeReasonsTest17.xml", "InvalidonlySomeReasonsTest17EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.18
        [Test]
        public void testValidOnlySomeReasonsTest18()
        {
            test("ValidonlySomeReasonsTest18.xml", "ValidonlySomeReasonsTest18EE.crt", CertificateStatus.VALID);
        }

        // 4.14.19
        [Test]
        public void testValidOnlySomeReasonsTest19()
        {
            test("ValidonlySomeReasonsTest19.xml", "ValidonlySomeReasonsTest19EE.crt", CertificateStatus.VALID);
        }

        // 4.14.20
        [Test]
        public void test()
        {
            test("ValidonlySomeReasonsTest19.xml", "InvalidonlySomeReasonsTest20EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.21
        [Test]
        public void testInvalidOnlySomeReasonsTest21()
        {
            test("ValidonlySomeReasonsTest19.xml", "InvalidonlySomeReasonsTest21EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.22
        [Test]
        public void testValidIDPWithIndirectCRLTest22()
        {
            test("ValidIDPwithindirectCRLTest22.xml", "ValidIDPwithindirectCRLTest22EE.crt", CertificateStatus.VALID);
        }

        // 4.14.23 
        [Test]
        public void testInvalidIDPWithIndirectCRLTest23()
        {
            test("ValidIDPwithindirectCRLTest22.xml", "InvalidIDPwithindirectCRLTest23EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.24
        [Test]
        public void testValidIDPWithIndirectCRLTest24()
        {
            test("ValidIDPwithindirectCRLTest24.xml", "ValidIDPwithindirectCRLTest24EE.crt", CertificateStatus.VALID);
        }

        // 4.14.25
        [Test]
        public void testValidIDPWithIndirectCRLTest25()
        {
            test("ValidIDPwithindirectCRLTest24.xml", "ValidIDPwithindirectCRLTest25EE.crt", CertificateStatus.VALID);
        }

        // 4.14.26
        [Test]
        public void testInvalidIDPWithIndirectCRLTest26()
        {
            test("ValidIDPwithindirectCRLTest24.xml", "InvalidIDPwithindirectCRLTest26EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.27
        [Test]
        public void testInvalidCRLIssuerTest27()
        {
            test("InvalidcRLIssuerTest27.xml", "InvalidcRLIssuerTest27EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.28
        [Test]
        public void testValidCRLIssuerTest28()
        {
            test("ValidcRLIssuerTest28.xml", "ValidcRLIssuerTest28EE.crt", CertificateStatus.VALID);
        }

        // 4.14.29
        [Test]
        public void testValidCRLIssuerTest29()
        {
            test("ValidcRLIssuerTest28.xml", "ValidcRLIssuerTest29EE.crt", CertificateStatus.VALID);
        }

        // 4.14.30
        [Test]
        public void testValidCRLIssuerTest30()
        {
            test("ValidcRLIssuerTest30.xml", "ValidcRLIssuerTest30EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.31
        [Test]
        public void testInvalidCRLIssuerTest31()
        {
            test("InvalidcRLIssuerTest31.xml", "InvalidcRLIssuerTest31EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.32
        [Test]
        public void testInvalidCRLIssuerTest32()
        {
            test("InvalidcRLIssuerTest31.xml", "InvalidcRLIssuerTest32EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.33
        [Test]
        public void testValidCRLIssuerTest33()
        {
            test("InvalidcRLIssuerTest31.xml", "ValidcRLIssuerTest33EE.crt", CertificateStatus.VALID);
        }

        // 4.14.34
        [Test]
        public void testInvalidCRLIssuerTest34()
        {
            test("InvalidcRLIssuerTest34.xml", "InvalidcRLIssuerTest34EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.14.35
        [Test]
        public void testInvalidCRLIssuerTest35()
        {
            test("InvalidcRLIssuerTest34.xml", "InvalidcRLIssuerTest35EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

    }
}
