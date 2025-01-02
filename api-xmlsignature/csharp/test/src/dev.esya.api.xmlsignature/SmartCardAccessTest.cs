using System;
using System.Collections.Generic;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace dev.esya.api.xmlsignature
{
    [TestFixture]
    class SmartCardAccessTest
    {
        [Test]
        public void readCertificate()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long session = sc.openSession(1);
            List<byte[]> certs = sc.getSignatureCertificates(session);
            Console.WriteLine(certs.Count);
        }
    }
}
