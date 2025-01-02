using System.IO;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl;

/**
 *     4.15 Delta-CRLs
 * 
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    public class MyMatcher :CRLNumberMatcher
    {
        public bool macthDeltaCRL(asn.x509.ECRL aCRL, asn.x509.ECRL aDeltaCRL)
        {
            return base._macthDeltaCRL(aCRL, aDeltaCRL);
        }
    }

    [TestFixture]
    class DeltaCRLTest : BaseNISTTest
    {
        

        [Test]
        public void testCRLNumberMatcher()
        {
            MyMatcher matcher = new MyMatcher();
            //ECRL firstCRL = new ECRL(new FileInfo(@"E:\tempInput\1.crl"));
            bool b = matcher.macthDeltaCRL(new ECRL(new FileInfo(@"E:\tempInput\1.crl")),
                                           new ECRL(new FileInfo(@"E:\tempInput\2.crl")));



        }

        // 4.15.1
        [Test]
        public void testInvaliddeltaCRLIndicatorNoBaseTest1()
        {
            test("deltaCRLIndicatorNoBaseTest1EE.xml", "InvaliddeltaCRLIndicatorNoBaseTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.15.2
        [Test]
        public void testValiddeltaCRLTest2()
        {
            test("validdeltaCRLTest2EE.xml", "ValiddeltaCRLTest2EE.crt", CertificateStatus.VALID);
        }

        // 4.15.3
        [Test]
        public void testInvaliddeltaCRLTest3()
        {
            test("validdeltaCRLTest2EE.xml", "InvaliddeltaCRLTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.15.4
        [Test]
        public void testInvaliddeltaCRLTest4()
        {
            test("validdeltaCRLTest2EE.xml", "InvaliddeltaCRLTest4EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }


        // 4.15.5   removeFromCRL
        [Test]
        public void testValiddeltaCRLTest5()
        {
            test("validdeltaCRLTest2EE.xml", "ValiddeltaCRLTest5EE.crt", CertificateStatus.VALID);
        }

        // 4.15.6
        //The end entity certificate is listed as on hold on the
        //complete CRL and the delta-CRL indicates that it has been revoked.
        [Test]
        public void testInvaliddeltaCRLTest6()
        {
            test("validdeltaCRLTest2EE.xml", "InvaliddeltaCRLTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.15.7
        //removeFromCRL
        [Test]
        public void testValiddeltaCRLTest7()
        {
            test("validdeltaCRLTest2EE.xml", "ValiddeltaCRLTest7EE.crt", CertificateStatus.VALID);
        }

        // 4.15.8
        [Test]
        public void testValiddeltaCRLTest8()
        {
            test("ValiddeltaCRLTest8.xml", "ValiddeltaCRLTest8EE.crt", CertificateStatus.VALID);
        }

        // 4.15.9
        [Test]
        public void testInvaliddeltaCRLTest9()
        {
            test("ValiddeltaCRLTest8.xml", "InvaliddeltaCRLTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.15.10
        [Test]
        public void testInvaliddeltaCRLTest10()
        {
            test("InvaliddeltaCRLTest10.xml", "InvaliddeltaCRLTest10EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }
    }
}
