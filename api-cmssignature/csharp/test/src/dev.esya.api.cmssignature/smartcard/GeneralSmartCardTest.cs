using System;
using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace dev.esya.api.cmssignature.smartcard
{
    [TestFixture]
    class GeneralSmartCardTest
    {


        [Test]
        public void TestGettingAllCertificates()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long session = sc.openSession(1);
            List<byte[]> certs = sc.getCertificates(session);
            foreach (byte[] cert in certs)
            {
                ECertificate eCertificate = new ECertificate(cert);
                Console.WriteLine(eCertificate);
            }
        }


        [Test]
        public void TestGettingEncryptionCertificates()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long session = sc.openSession(1);
            List<byte[]> certs = sc.getEncryptionCertificates(session);
            foreach (byte[] cert in certs)
            {
                ECertificate eCertificate = new ECertificate(cert);
                Console.WriteLine(eCertificate);
            }
        }


        [Test]
        public void TestGettingSignatureCertificates()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long session = sc.openSession(1);
            List<byte []> certs = sc.getSignatureCertificates(session);
            foreach (byte[] cert in certs)
            {
                ECertificate eCertificate = new ECertificate(cert);
                Console.WriteLine(eCertificate);
            }
        }
    }
}
