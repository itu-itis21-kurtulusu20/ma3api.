using System;
using iaik.pkcs.pkcs11.wrapper;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace dev.esya.api.cmssignature.smartcard
{
    [TestFixture]
    public class KeyTypeAndSigAlgTest
    {
        string rsaKeyLabel = "QCA1_1";
        string ecKeyLabel = "qc d1 2";

        string cardPIN = "12345";

        // inconsistent key and signing algs

        [Test]
        public void rsaSignWithECKeyTest()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            long sid = sc.openSession(slot);
            sc.login(sid, cardPIN);

            byte[] tbsData = RandomUtil.generateRandom(16);

            bool success = false;
            try
            {
                SmartOp.sign(sc, sid, slot, ecKeyLabel, tbsData, SignatureAlg.RSA_SHA256.getName(),
                    null);
            }
            catch (SmartCardException ex)
            {
                success = ex.Message.Equals(Resource.message(Resource.IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU));
            }
            finally
            {
                sc.logout(sid);
                sc.closeSession(sid);
            }

            Assert.True(success);
        }

        [Test]
        public void ecSignWithRSAKeyTest()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            long sid = sc.openSession(slot);
            sc.login(sid, cardPIN);

            byte[] tbsData = RandomUtil.generateRandom(16);

            bool success = false;
            try
            {
                SmartOp.sign(sc, sid, slot, rsaKeyLabel, tbsData,
                    SignatureAlg.ECDSA_SHA256.getName(), null);
            }
            catch (SmartCardException ex)
            {
                success = ex.Message.Equals(Resource.message(Resource.IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU));
            }
            finally
            {
                sc.logout(sid);
                sc.closeSession(sid);
            }

            Assert.True(success);
        }

        // consistent key and signing algs

        [Test]
        public void rsaSignWithRSAKeyTest()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            long sid = sc.openSession(slot);
            sc.login(sid, cardPIN);

            string keyLabel = rsaKeyLabel;
            byte[] tbsData = RandomUtil.generateRandom(16);

            bool success = false;
            try
            {
                SmartOp.sign(sc, sid, slot, keyLabel, tbsData, SignatureAlg.RSA_SHA256.getName(),
                    null);
                success = true;
            }
            finally
            {
                sc.logout(sid);
                sc.closeSession(sid);
            }

            Assert.True(success);
        }

        [Test]
        public void ecSignWithECKeyTest()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            long sid = sc.openSession(slot);
            sc.login(sid, cardPIN);

            string keyLabel = ecKeyLabel;
            byte[] tbsData = RandomUtil.generateRandom(16);

            bool success = false;
            try
            {
                SmartOp.sign(sc, sid, slot, keyLabel, tbsData, SignatureAlg.ECDSA_SHA256.getName(),
                    null);
                success = true;
            }
            finally
            {
                sc.logout(sid);
                sc.closeSession(sid);
            }

            Assert.True(success);
        }
    }
}
