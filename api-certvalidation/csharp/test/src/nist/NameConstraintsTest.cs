using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 *      4.13 Name Constraints
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class NameConstraintsTest : BaseNISTTest
    {
        // 4.13.1
        [Test]
        public void testValidDNnameConstraintsTest1()
        {
            test("ValidDNnameConstraintsTest1.xml", "ValidDNnameConstraintsTest1EE.crt", CertificateStatus.VALID);
        }

        // 4.13.2
        [Test]
        public void testInvalidDNnameConstraintsTest2()
        {
            test("InvalidDNnameConstraintsTest2.xml", "InvalidDNnameConstraintsTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.3
        [Test]
        public void testInvalidDNnameConstraintsTest3()
        {
            test("InvalidDNnameConstraintsTest3.xml", "InvalidDNnameConstraintsTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.4
        [Test]
        public void testValidDNnameConstraintsTest4()
        {
            test("ValidDNnameConstraintsTest4.xml", "ValidDNnameConstraintsTest4EE.crt", CertificateStatus.VALID);
        }

        // 4.13.5
        [Test]
        public void testValidDNnameConstraintsTest5()
        {
            test("ValidDNnameConstraintsTest5.xml", "ValidDNnameConstraintsTest5EE.crt", CertificateStatus.VALID);
        }

        // 4.13.6
        [Test]
        public void testValidDNnameConstraintsTest6()
        {
            test("ValidDNnameConstraintsTest6.xml", "ValidDNnameConstraintsTest6EE.crt", CertificateStatus.VALID);
        }

        // 4.13.7
        [Test]
        public void testInvalidDNnameConstraintsTest7()
        {
            test("InvalidDNnameConstraintsTest7.xml", "InvalidDNnameConstraintsTest7EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.8
        [Test]
        public void testInvalidDNnameConstraintsTest8()
        {
            test("InvalidDNnameConstraintsTest8.xml", "InvalidDNnameConstraintsTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.9
        [Test]
        public void testInvalidDNnameConstraintsTest9()
        {
            test("InvalidDNnameConstraintsTest9.xml", "InvalidDNnameConstraintsTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.10
        [Test]
        public void testInvalidDNnameConstraintsTest10()
        {
            test("InvalidDNnameConstraintsTest10.xml", "InvalidDNnameConstraintsTest10EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.11
        [Test]
        public void testValidDNnameConstraintsTest11()
        {
            test("ValidDNnameConstraintsTest11.xml", "ValidDNnameConstraintsTest11EE.crt", CertificateStatus.VALID);
        }

        // 4.13.12
        [Test]
        public void testInvalidDNnameConstraintsTest12()
        {
            test("InvalidDNnameConstraintsTest12.xml", "InvalidDNnameConstraintsTest12EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.13
        [Test]
        public void testInvalidDNnameConstraintsTest13()
        {
            test("InvalidDNnameConstraintsTest13.xml", "InvalidDNnameConstraintsTest13EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.14
        [Test]
        public void testValidDNnameConstraintsTest14()
        {
            test("ValidDNnameConstraintsTest14.xml", "ValidDNnameConstraintsTest14EE.crt", CertificateStatus.VALID);
        }

        // 4.13.15
        [Test]
        public void testInvalidDNnameConstraintsTest15()
        {
            test("InvalidDNnameConstraintsTest15.xml", "InvalidDNnameConstraintsTest15EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.16
        [Test]
        public void testInvalidDNnameConstraintsTest16()
        {
            test("InvalidDNnameConstraintsTest16.xml", "InvalidDNnameConstraintsTest16EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.17
        [Test]
        public void testInvalidDNnameConstraintsTest17()
        {
            test("InvalidDNnameConstraintsTest17.xml", "InvalidDNnameConstraintsTest17EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.18
        [Test]
        public void testValidDNnameConstraintsTest18()
        {
            test("ValidDNnameConstraintsTest18.xml", "ValidDNnameConstraintsTest18EE.crt", CertificateStatus.VALID);
        }

        // 4.13.19
        [Test]
        public void testValidSelfIssuedDNnameConstraintsTest19()
        {
            test("ValidSelfIssuedDNnameConstraintsTest19.xml", "ValidDNnameConstraintsTest19EE.crt", CertificateStatus.VALID);
        }

        // 4.13.20
        [Test]
        public void testInvalidSelfIssuedDNnameConstraintsTest20()
        {
            test("InvalidSelfIssuedDNnameConstraintsTest20.xml", "InvalidDNnameConstraintsTest20EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.21
        [Test]
        public void testValidRFC822nameConstraintsTest21()
        {
            test("ValidRFC822nameConstraintsTest21.xml", "ValidRFC822nameConstraintsTest21EE.crt", CertificateStatus.VALID);
        }

        // 4.13.22
        [Test]
        public void testInvalidRFC822nameConstraintsTest22()
        {
            test("InvalidRFC822nameConstraintsTest22.xml", "InvalidRFC822nameConstraintsTest22EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.23
        [Test]
        public void testValidRFC822nameConstraintsTest23()
        {
            test("ValidRFC822nameConstraintsTest23.xml", "ValidRFC822nameConstraintsTest23EE.crt", CertificateStatus.VALID);
        }

        // 4.13.24
        [Test]
        public void testInvalidRFC822nameConstraintsTest24()
        {
            test("InvalidRFC822nameConstraintsTest24.xml", "InvalidRFC822nameConstraintsTest24EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.25
        [Test]
        public void testValidRFC822nameConstraintsTest25()
        {
            test("ValidRFC822nameConstraintsTest25.xml", "ValidRFC822nameConstraintsTest25EE.crt", CertificateStatus.VALID);
        }

        // 4.13.26
        [Test]
        public void testInvalidRFC822nameConstraintsTest26()
        {
            test("InvalidRFC822nameConstraintsTest26.xml", "InvalidRFC822nameConstraintsTest26EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.27
        [Test]
        public void testValidDNandRFC822nameConstraintsTest27()
        {
            test("ValidDNandRFC822nameConstraintsTest27.xml", "ValidDNandRFC822nameConstraintsTest27EE.crt", CertificateStatus.VALID);
        }

        // 4.13.28
        [Test]
        public void testInvalidDNandRFC822nameConstraintsTest28()
        {
            test("InvalidDNandRFC822nameConstraintsTest28.xml", "InvalidDNandRFC822nameConstraintsTest28EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.29
        [Test]
        public void testInvalidDNandRFC822nameConstraintsTest29()
        {
            test("InvalidDNandRFC822nameConstraintsTest29.xml", "InvalidDNandRFC822nameConstraintsTest29EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.30
        [Test]
        public void testValidDNSnameConstraintsTest30()
        {
            test("ValidDNSnameConstraintsTest30.xml", "ValidDNSnameConstraintsTest30EE.crt", CertificateStatus.VALID);
        }

        // 4.13.31
        [Test]
        public void testInvalidDNSnameConstraintsTest31()
        {
            test("InvalidDNSnameConstraintsTest31.xml", "InvalidDNSnameConstraintsTest31EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.32
        [Test]
        public void testValidDNSnameConstraintsTest32()
        {
            test("ValidDNSnameConstraintsTest32.xml", "ValidDNSnameConstraintsTest32EE.crt", CertificateStatus.VALID);
        }

        // 4.13.33
        [Test]
        public void testInvalidDNSnameConstraintsTest33()
        {
            test("InvalidDNSnameConstraintsTest33.xml", "InvalidDNSnameConstraintsTest33EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.34
        [Test]
        public void testValidURInameConstraintsTest34()
        {
            test("ValidURInameConstraintsTest34.xml", "ValidURInameConstraintsTest34EE.crt", CertificateStatus.VALID);
        }

        // 4.13.35
        [Test]
        public void testInvalidURInameConstraintsTest35()
        {
            test("InvalidURInameConstraintsTest35.xml", "InvalidURInameConstraintsTest35EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.36
        [Test]
        public void testValidURInameConstraintsTest36()
        {
            test("ValidURInameConstraintsTest36.xml", "ValidURInameConstraintsTest36EE.crt", CertificateStatus.VALID);
        }

        // 4.13.37
        [Test]
        public void testInvalidURInameConstraintsTest37()
        {
            test("InvalidURInameConstraintsTest37.xml", "InvalidURInameConstraintsTest37EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.13.38
        [Test]
        public void testInvalidDNSnameConstraintsTest38()
        {
            test("InvalidDNSnameConstraintsTest38.xml", "InvalidDNSnameConstraintsTest38EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }
    }
}
