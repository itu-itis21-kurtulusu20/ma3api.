//using NUnit.Framework;
//using tr.gov.tubitak.uekae.esya.api.common.util;
//using tr.gov.tubitak.uekae.esya.api.crypto.alg;
//using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
//namespace tr.gov.tubitak.uekae.esya.api.crypto
//{
//    [TestFixture]
//    public class AESWrapperTest
//    {

//        byte[] kek;
//        byte[] keyData;
//        //expected result
//        byte[] eResult;

//        BouncyWrapper wrapper;
//        //actual result
//        byte[] aResult;

//        [Test]
//        public void test192bitKeyData192bitKEK()
//        {
//            kek = StringUtil.ToByteArray("000102030405060708090A0B0C0D0E0F1011121314151617");
//            keyData = StringUtil.ToByteArray("00112233445566778899AABBCCDDEEFF0001020304050607");
//            eResult = StringUtil.ToByteArray("031D33264E15D33268F24EC260743EDCE1C6C7DDEE725A936BA814915C6762D2");

//            testWrapper(kek, keyData, eResult);
//        }

//        private void testWrapper(byte[] kek, byte[] keyData, byte[] eResult)
//        {
//            wrapper = new BouncyWrapper(WrapAlg.AES192, true);

//            //aResult = wrapper.wrap(keyData, 0, keyData.Length, kek);
//            wrapper.init(kek);
//            aResult = wrapper.process(keyData);

//            Assert.AreEqual(eResult, aResult);

//            wrapper = new BouncyWrapper(WrapAlg.AES192, false);
//            //aResult = wrapper.unwrap(eResult, 0, eResult.length, kek);
//            wrapper.init(kek);
//            aResult = wrapper.process(eResult);

//            Assert.AreEqual(keyData, aResult);

//        }
//    }
//}
