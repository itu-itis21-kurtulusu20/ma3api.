using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 *      4.7 Key Usage
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class KeyUsageTest : BaseNISTTest
    {
        //4.7.1
        [Test]
        public void testInvalidKeyUsageCriticalKeyCertSignFalseTest1()
        {
            test("InvalidkeyUsageCriticalkeyCertSignFalseTest1.xml", "InvalidkeyUsageCriticalkeyCertSignFalseTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.7.2
        [Test]
        public void testInvalidKeyUsageNotCriticalKeyCertSignFalseTest2()
        {
            test("InvalidkeyUsageNotCriticalkeyCertSignFalseTest2.xml", "InvalidkeyUsageNotCriticalkeyCertSignFalseTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.7.3
        [Test]
        public void testValidKeyUsageNotCriticalTest3()
        {
            test("ValidkeyUsageNotCriticalTest3.xml", "ValidkeyUsageNotCriticalTest3EE.crt", CertificateStatus.VALID);
        }

        //4.7.4
        [Test]
        public void testInvalidKeyUsageCriticalCRLSignFalseTest4()
        {
            test("InvalidkeyUsageCriticalcRLSignFalseTest4.xml", "InvalidkeyUsageCriticalcRLSignFalseTest4EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.7.5
        [Test]
        public void testInvalidKeyUsageNotCriticalCRLSignFalseTest5()
        {
            test("InvalidkeyUsageNotCriticalcRLSignFalseTest5.xml", "InvalidkeyUsageNotCriticalcRLSignFalseTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

    }
}
