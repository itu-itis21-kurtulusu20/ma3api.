using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace dev.esya.api.cmssignature.smartcard
{
    [TestFixture]
    class Akis25Tests
    {
        private static string PASSWORD = "12345";

        static SmartCard sc = null;
        static long sid = 0;
        static long slotNo = 0;

        [SetUp]
        public void SetUpTest()
        {
            sc = new SmartCard(CardType.AKIS);
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

        //Creating EC key pairs in v2.5.2 cards
        [Test]
        public void test_09_01_CreateECKey_secp192() 
        {
            createCurveAndSign("secp192r1");
        }

        [Test]
        public void test_09_02_CreateECKey_secp224()
        {
            createCurveAndSign("secp224r1");
        }

        [Test]
        public void test_09_03_CreateECKey_secp256()
        {
            createCurveAndSign("secp256r1");
        }

        [Test]
        public void test_09_04_CreateECKey_secp384()
        {
            createCurveAndSign("secp384r1");
        }

        [Test]
        public void test_09_05_CreateECKey_secp521()
        {
            createCurveAndSign("secp521r1");
        }

        //Creating RSA key pairs in v2.5.2 cards
        [Test]
        public void test_09_06_CreateRSAPKCS1KeyAndSign() 
        {
            createRSAKeyAndSign(1024);
        }

        [Test]
        public void test_09_07_CreateRSAPKCS1KeyAndSign()
        {
            createRSAKeyAndSign(2048);
        }
      
        //Importing EC key pairs created in memory to v2.5.2 cards  
        [Test]
        public void test_10_01_ImportECKey_secp192()
        {
            importCurveAndSign("secp192r1");
        }

        [Test]
        public void test_10_02_ImportECKey_secp224()
        {
            importCurveAndSign("secp224r1");
        }

        [Test]
        public void test_10_03_ImportECKey_secp256()
        {
            importCurveAndSign("secp256r1");
        }

        [Test]
        public void test_10_04_ImportECKey_secp384()
        {
            importCurveAndSign("secp384r1");
        }

        [Test]
        public void test_10_05_ImportECKey_secp521()
        {
            importCurveAndSign("secp521r1");
        }

        //Importing RSA key pairs created in memory to v2.5.2 cards
        [Test]
        public void test_10_06_ImportRSAPKCS1KeyAndSign()
        {
            importRSAKeyAndSign(1024);
        }

        [Test]
        public void test_10_07_ImportRSAPKCS1KeyAndSign()
        {
            importRSAKeyAndSign(2048);
        }

        public void createCurveAndSign(String curveName) 
        {
            CardTestUtil.testCreateECKeys(sc, sid, slotNo, "ecCreateKey_", curveName);
        }

        public void importCurveAndSign(String curveName)
        {
            CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, sid, slotNo, "ecImportKey_", curveName);
        }

        public void createRSAKeyAndSign(int keyLength)
        {
            CardTestUtil.testCreateRSAKeyAndSign(sc, sid, slotNo, "rsaCreateKey_", keyLength);
        }

        public void importRSAKeyAndSign(int keyLength)
        {
            CardTestUtil.testImportRSAKeyAndSign(sc, sid, slotNo, "rsaImportKey_", keyLength);
        }
    }
}
