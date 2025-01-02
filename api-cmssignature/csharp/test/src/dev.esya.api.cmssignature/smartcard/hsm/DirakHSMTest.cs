
using System;
using System.Diagnostics;
using iaik.pkcs.pkcs11.wrapper;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.x509;


namespace dev.esya.api.cmssignature.smartcard
{
    [TestFixture]
    class DirakHSMTest
    {
        private static string PASSWORD = "123456";

        static SmartCard sc = null;
        static long sid = 0;
        static long slotNo = 0;


        [SetUp]
        public void SetUpTest()
        {
            sc = new SmartCard(CardType.DIRAKHSM);
            slotNo = getSlot();
            sid = sc.openSession(slotNo);
            sc.login(sid, PASSWORD);
        }

        [TearDown]
        public void TearDown()
        {
            sc.logout(sid);
            sc.closeSession(sid);
        }


        private static long getSlot()
        {
            long[] slots = sc.getTokenPresentSlotList();
            return slots[0];
        }


        [Test]
        public void TestRSA_PSS_SHA1_2048()
        {
            CreateKeyAndRSAPSSTest(DigestAlg.SHA1, 2048);
        }

        [Test]
        public void TestRSA_PSS_SHA256_2048()
        {
            CreateKeyAndRSAPSSTest(DigestAlg.SHA256, 2048);
        }

        [Test]
        public void TestRSAPSS()
        {
            DigestAlg digestAlg = DigestAlg.SHA256;
            int keyLen = 2048;

            string keyLabel = "RSA_PSS_" + digestAlg.ToString() + "_" + keyLen;

            CardTestUtil.ClearKeyPairBeforeTest(sc, sid, keyLabel);

            sc.createRSAKeyPair(sid, keyLabel, keyLen, true, false);
            bool priFound = sc.isPrivateKeyExist(sid, keyLabel);
            bool pubFound = sc.isPublicKeyExist(sid, keyLabel);

            Assert.True(priFound && pubFound);

            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

            byte [] signature = SmartOp.sign(sc, sid, getSlot(), keyLabel, tobesigned, SignatureAlg.RSA_PSS.getName(),
                new RSAPSSParams(digestAlg));

            ERSAPublicKey publicKey = sc.readRSAPublicKey(sid, keyLabel);

            RSAPSSParams rsapssParams = new RSAPSSParams(digestAlg);

            bool result = CryptoVerify(SignatureAlg.RSA_PSS, rsapssParams, publicKey, tobesigned, signature);
            Assert.True(result);

            CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
        }

        [Test]
        public void TestRSAPKCS()
        {
            DigestAlg digestAlg = DigestAlg.SHA256;
            int keyLen = 2048;

            string keyLabel = "RSA_PKCS_" + digestAlg.ToString() + "_" + keyLen;

            CardTestUtil.ClearKeyPairBeforeTest(sc, sid, keyLabel);

            sc.createRSAKeyPair(sid, keyLabel, keyLen, true, false);
            bool priFound = sc.isPrivateKeyExist(sid, keyLabel);
            bool pubFound = sc.isPublicKeyExist(sid, keyLabel);

            Assert.True(priFound && pubFound);

            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte[] tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

            byte[] signature = SmartOp.sign(sc, sid, getSlot(), keyLabel, tobesigned, SignatureAlg.RSA_SHA256.getName(),
                new RSAPSSParams(digestAlg));

            ERSAPublicKey publicKey = sc.readRSAPublicKey(sid, keyLabel);

            RSAPSSParams rsapssParams = new RSAPSSParams(digestAlg);

            bool result = CryptoVerify(SignatureAlg.RSA_SHA256, rsapssParams, publicKey, tobesigned, signature);
            Assert.True(result);

            CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
        }


        [Test]
        public void TestMultiblePSS_Sign()
        {
            string keyLabel = "RSAPerformanceKey";

            int keyLen = 2048;
            DigestAlg digestAlg = DigestAlg.SHA256;
            int signingCount = 10;

            CardTestUtil.ClearKeyPairBeforeTest(sc, sid, keyLabel);

            sc.createRSAKeyPair(sid, keyLabel, keyLen, true, false);
            bool priFound = sc.isPrivateKeyExist(sid, keyLabel);
            bool pubFound = sc.isPublicKeyExist(sid, keyLabel);

            Assert.True(priFound && pubFound);

            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte []tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

            CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(digestAlg);


            Stopwatch watch = new Stopwatch();
            watch.Start();
            for(int i = 0; i<signingCount; i++ ){
                byte [] signature = sc.signData(sid, keyLabel, tobesigned, mech);
            }
            watch.Stop();
            Console.WriteLine(signingCount + " RSAPSS " + keyLen + " signature time: " + watch.ElapsedMilliseconds / 1000 + " Seconds");
        
            //System.out.println("ObjeAra Seconds: " + ((double)(PKCS11Ops.objeAraSure))/1000);
            //System.out.println("Signing Seconds: " + ((double)(PKCS11Ops.signingSure))/1000);

            CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
        }

        public void CreateKeyAndRSAPSSTest(DigestAlg digestAlg, int keyLen)
        {
            string keyLabel = "RSA_PSS_" + digestAlg.ToString() + "_" + keyLen;

            CardTestUtil.ClearKeyPairBeforeTest(sc, sid, keyLabel);

            sc.createRSAKeyPair(sid, keyLabel, keyLen, true, false);
            bool priFound = sc.isPrivateKeyExist(sid, keyLabel);
            bool pubFound = sc.isPublicKeyExist(sid, keyLabel);

            Assert.True(priFound && pubFound);

            SignAtHSMAndVerifyRSA_PSS(digestAlg, keyLabel);

            CardTestUtil.DeleteKeyPair(sc, sid, keyLabel);
        }

        private void SignAtHSMAndVerifyRSA_PSS(DigestAlg digestAlg, string keyLabel)
        {
            int dataToBeSignedLen = 10 + new Random().Next(30);
            byte [] tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

            CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(digestAlg);

            byte []signature = sc.signData(sid, keyLabel, tobesigned, mech);

            ERSAPublicKey publicKey = sc.readRSAPublicKey(sid, keyLabel);

            RSAPSSParams rsapssParams = new RSAPSSParams(digestAlg);

            bool result = CryptoVerify(SignatureAlg.RSA_PSS, rsapssParams, publicKey, tobesigned, signature);
            Assert.True(result);

            
        }

        private bool CryptoVerify(SignatureAlg signatureAlg, RSAPSSParams rsapssParams, ERSAPublicKey publicKey, byte[] tobesigned, byte[] signature)
        {
          
            ESubjectPublicKeyInfo publicKeyInfo = ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(
                          new AlgorithmIdentifier(_algorithmsValues.rsaEncryption), publicKey);

            PublicKey pubKey = new PublicKey(publicKeyInfo);

            return SignUtil.verify(signatureAlg, rsapssParams, tobesigned, signature, pubKey);
        }
    }
}
