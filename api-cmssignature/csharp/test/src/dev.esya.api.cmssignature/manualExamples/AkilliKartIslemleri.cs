
using System;
using System.Collections.Generic;
using System.IO;
using iaik.pkcs.pkcs11.wrapper;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace dev.esya.api.cmssignature.manualExamples
{
    [TestFixture]
    public class AkilliKartIslemleri
    {
        private readonly String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() +
                                                           "testdata\\support\\yasemin_sign.cer";
        [Test]
        public void testAkilliKart1()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long[] slots = sc.getSlotList();
            foreach (long slot in slots)
            {
                CK_SLOT_INFO slotInfo = sc.getSlotInfo(slot);
                Console.WriteLine(new String(slotInfo.slotDescription).Trim());
            }
        }
        [Test]
        public void testKarttanSertifikaOkuma()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long[] slots = sc.getSlotList();
            long slot = 0;
            if (slots.Length == 1)
                slot = slots[0];
            else
                slot = selectSlot();
            long session = sc.openSession(slot);
            sc.login(session, "12345");
            List<byte[]> certBytes = sc.getSignatureCertificates(session);
            foreach (byte[] bs in certBytes)
            {
                ECertificate cert = new ECertificate(bs);
                //cert.isQualifiedCertificate()
                Console.WriteLine(cert.getSubject().getCommonNameAttribute());
            }
            sc.logout(session);
            sc.closeSession(session);
        }

        [Test]
        public void testAnahtarEtiketlerininOkunmasi()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            long session = sc.openSession(slot);
            sc.login(session, "12345");
            String[] labels = sc.getSignatureKeyLabels(session);
            foreach (String label in labels)
            {
                Console.WriteLine(label);
            }
        }
        [Test]
        public void testSeriNoIleImzaciOlusturulmasi()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long[] slots = sc.getSlotList();
            long session = sc.openSession(slots[0]);
            sc.login(session, "12345");
            ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], cert.getSerialNumber().GetData(),
                                                             SignatureAlg.RSA_SHA1.getName());
        }
        [Test]
        public void testLabelIleImzaciOlusturulmasi()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            long session = sc.openSession(slot);
            sc.login(session, "12345");
            BaseSigner signer = new SCSignerWithKeyLabel(sc, session, slot, "yasemin.akturk#ug.netSIGN0",
                                                         SignatureAlg.RSA_SHA1.getName());
        }

        private long selectSlot()
        {
            return 1;
        }
    }
}