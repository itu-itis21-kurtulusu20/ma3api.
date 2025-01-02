using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;

namespace test.esya.api.cmssignature.crypto
{
    /**
    *     MACsec GCM-AES Test Vectors
    *     April 11, 2011
    */
    [TestFixture]
    public class GCMTest
    {
        [Test]
        public void TestGCM_01()
        {
            byte[] data = StringUtil.ToByteArray("08000F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A0002");
            byte[] key = StringUtil.ToByteArray("AD7A2BD03EAC835A6F620FDCB506B345");
            byte[] iv = StringUtil.ToByteArray("12153524C0895E81B2C28465");
            byte[] aad = StringUtil.ToByteArray("D609B1F056637A0D46DF998D88E52E00B2C2846512153524C0895E81");
            byte[] expectedCT = StringUtil.ToByteArray("701AFA1CC039C0D765128A665DAB69243899BF7318CCDC81C9931DA17FBE8EDD7D17CB8B4C26FC81E3284F2B7FBA713D");
            byte[] expectedTag = StringUtil.ToByteArray("4F8D55E7D3F06FD5A13C0C29B9D5B880");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
        }

        [Test]
        public void TestGCM_02()
        {
            byte[] data = StringUtil.ToByteArray("");
            byte[] key = StringUtil.ToByteArray("AD7A2BD03EAC835A6F620FDCB506B345");
            byte[] iv = StringUtil.ToByteArray("12153524C0895E81B2C28465");
            byte[] aad = StringUtil.ToByteArray("D609B1F056637A0D46DF998D88E5222AB2C2846512153524C0895E8108000F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F30313233340001");
            byte[] expectedCT = StringUtil.ToByteArray("");
            byte[] expectedTag = StringUtil.ToByteArray("F09478A9B09007D06F46E9B6A1DA25DD");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
        }

        [Test]
        public void TestGCM_03()
        {
            byte[] data = StringUtil.ToByteArray("");
            byte[] key = StringUtil.ToByteArray("013FE00B5F11BE7F866D0CBBC55A7A90");
            byte[] iv = StringUtil.ToByteArray("7CFDE9F9E33724C68932D612");
            byte[] aad = StringUtil.ToByteArray("84C5D513D2AAF6E5BBD2727788E523008932D6127CFDE9F9E33724C608000F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F0005");
            byte[] expectedCT = StringUtil.ToByteArray("");
            byte[] expectedTag = StringUtil.ToByteArray("217867E50C2DAD74C28C3B50ABDF695A");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
        }

        [Test]
        public void TestGCM_04()
        {
            byte[] data = StringUtil.ToByteArray("c6b99ae06316234f44a44a2d8f9a843097ec7e8a6d110976c1c03eeddf7aaff5d45b0d1e84084f9b8b00761e75d8ea2266e0b8");
            byte[] key = StringUtil.ToByteArray("0e844d5ced6522093ccbecfa04b343d0");
            byte[] iv = StringUtil.ToByteArray("e1b5a334201d5e3e3b482122");
            byte[] aad = StringUtil.ToByteArray("");
            byte[] expectedCT = StringUtil.ToByteArray("53d4cf059640af722d34bfa0a39f4693ab1bfe06ed91d50adaa58448580df161a3472df4eb076480eaef22d866d326d2e184c0");
            byte[] expectedTag = StringUtil.ToByteArray("f6fae6354f5f7836928328918d");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
        }

        [Test]
        public void TestGCM_05()
        {
            byte[] aad = StringUtil.ToByteArray("E20106D7CD0DF0761E8DCD3D88E54C2A76D457ED");
            byte[] data = StringUtil.ToByteArray("08000F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F30313233340004");
            byte[] key = StringUtil.ToByteArray("071B113B0CA743FECCCF3D051F737382");
            byte[] iv = StringUtil.ToByteArray("F0761E8DCD3D000176D457ED");
            byte[] expectedCT = StringUtil.ToByteArray("13B4C72B389DC5018E72A171DD85A5D3752274D3A019FBCAED09A425CD9B2E1C9B72EEE7C9DE7D52B3F3");
            byte[] expectedTag = StringUtil.ToByteArray("D6A5284F4A6D3FE22A5D6C2B960494C3");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
        }

        [Test]
        public void TestGCM_06()
        {
            byte[] data = StringUtil.ToByteArray("08000F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B0006");
            byte[] key = StringUtil.ToByteArray("013FE00B5F11BE7F866D0CBBC55A7A90");
            byte[] iv = StringUtil.ToByteArray("7CFDE9F9E33724C68932D612");
            byte[] aad = StringUtil.ToByteArray("84C5D513D2AAF6E5BBD2727788E52F008932D6127CFDE9F9E33724C6");
            byte[] expectedCT = StringUtil.ToByteArray("3A4DE6FA32191014DBB303D92EE3A9E8A1B599C14D22FB080096E13811816A3C9C9BCF7C1B9B96DA809204E29D0E2A7642");
            byte[] expectedTag = StringUtil.ToByteArray("BFD310A4837C816CCFA5AC23AB003988");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
        }

        [Test]
        public void TestGCM_07()
        {
            byte[] aad = StringUtil.ToByteArray("68F2E77696CE7AE8E2CA4EC588E541002E58495C08000F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D0007");
            byte[] data = StringUtil.ToByteArray("");
            byte[] key = StringUtil.ToByteArray("88EE087FD95DA9FBF6725AA9D757B0CD");
            byte[] iv = StringUtil.ToByteArray("7AE8E2CA4EC500012E58495C");
            byte[] expectedCT = StringUtil.ToByteArray("");
            byte[] expectedTag = StringUtil.ToByteArray("07922B8EBCF10BB2297588CA4C614523");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
        }

        [Test]
        public void TestGCM_08()
        {
            byte[] aad = StringUtil.ToByteArray("68F2E77696CE7AE8E2CA4EC588E54D002E58495C");
            byte[] data = StringUtil.ToByteArray("08000F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748490008");
            byte[] key = StringUtil.ToByteArray("88EE087FD95DA9FBF6725AA9D757B0CD");
            byte[] iv = StringUtil.ToByteArray("7AE8E2CA4EC500012E58495C");
            byte[] expectedCT = StringUtil.ToByteArray("C31F53D99E5687F7365119B832D2AAE70741D593F1F9E2AB3455779B078EB8FEACDFEC1F8E3E5277F8180B43361F6512ADB16D2E38548A2C719DBA7228D840");
            byte[] expectedTag = StringUtil.ToByteArray("88F8757ADB8AA788D8F65AD668BE70E7");

            testGCM(aad, data, key, iv, expectedCT, expectedTag);
       }

      public void testGCM(byte[] aad, byte[] plaintext, byte[] key, byte[] iv, byte[] expectedCT, byte[] expectedTag)
        {
            MemoryStream aadStream = new MemoryStream(aad);
            ParamsWithGCMSpec gcmParams = new ParamsWithGCMSpec(iv, aadStream);
                       
            BufferedCipher encryptor = Crypto.getEncryptor(CipherAlg.AES128_GCM);
            encryptor.init(key, gcmParams);

            byte[] encrypted = encryptor.doFinal(plaintext);
            byte [] tag = new byte[expectedTag.Length];
            Array.Copy(gcmParams.getTag(), 0, tag, 0, expectedTag.Length);

            Assert.AreEqual(expectedTag, tag);
            Assert.AreEqual(expectedCT, encrypted);
            
            BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.AES128_GCM);
            decryptor.init(key, new ParamsWithGCMSpec(iv,aadStream,tag));

            byte[] decrypted = decryptor.doFinal(encrypted);
            Assert.AreEqual(decrypted, plaintext);
        }
    }
}
