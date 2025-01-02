using System;
using System.IO;
using NUnit.Framework;
using Org.BouncyCastle.Crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;

namespace test.src.test.esya.api.cmssignature.crypto
{
    /**
    * NIST CSRC Cryptography Toolkit Test Vectors
    * https://github.com/coruus/nist-testvectors/tree/master/csrc.nist.gov/groups/STM/cavp/documents/mac/gcmtestvectors
    */

    [TestFixture]
    public class NISTGCMTest
    {

        int totalTestofGCMEncryption;
        int totalTestofGCMDecryption;

        [Test]
        public void testGCMEncryption()
        {
            testGCMFromFile("T:\\api-parent\\resources\\unit-test-resources\\crypto\\gcm\\gcmEncryptExtIV128.rsp", CipherAlg.AES128_GCM, true);
        }

        [Test]
        public void testGCMDecryption()
        {
            testGCMFromFile("T:\\api-parent\\resources\\unit-test-resources\\crypto\\gcm\\gcmDecrypt128.rsp", CipherAlg.AES128_GCM, false);
        }

        public void testGCMFromFile(String fileName, CipherAlg cipherAlg, bool isEncryption)
        {
            byte[] data;
            byte[] key;
            byte[] iv;
            byte[] aad;
            byte[] expectedCT;
            byte[] expectedTag;
            bool fail;

            int testSkippedCauseofEmptyCipherText = 0;
            int testSkippedCauseofUnsupportedTagLength = 0;

            StreamReader reader = new StreamReader(fileName);
            String st;

            while ((st = reader.ReadLine()) != null)
            {
                data = StringUtil.ToByteArray("");
                key = StringUtil.ToByteArray("");
                iv = StringUtil.ToByteArray("");
                aad = StringUtil.ToByteArray("");
                expectedCT = StringUtil.ToByteArray("");
                expectedTag = StringUtil.ToByteArray("");
                fail = false;

                if (st.StartsWith("Count"))
                {
                    while (true)
                    {
                        st = reader.ReadLine();

                        if (string.IsNullOrEmpty(st))
                            break;

                        st = st.Replace(" ", "");
                        String[] values = st.Split("=".ToCharArray());

                        if (values[0].Equals("Key") && values.Length == 2)
                        {
                            key = StringUtil.ToByteArray(values[1]);
                        }
                        else if (values[0].Equals("IV") && values.Length == 2)
                        {
                            iv = StringUtil.ToByteArray(values[1]);
                        }
                        else if (values[0].Equals("CT") && values.Length == 2)
                        {
                            expectedCT = StringUtil.ToByteArray(values[1]);
                        }
                        else if (values[0].Equals("AAD") && values.Length == 2)
                        {
                            aad = StringUtil.ToByteArray(values[1]);
                        }
                        else if (values[0].Equals("Tag") && values.Length == 2)
                        {
                            expectedTag = StringUtil.ToByteArray(values[1]);
                        }
                        else if (values[0].Equals("PT") && values.Length == 2)
                        {
                            data = StringUtil.ToByteArray(values[1]);
                        }
                        else if (values[0].Equals("FAIL"))
                        {
                            fail = true;
                        }
                    }

                    //Mac sizes written below is not supported in Bouncy Castle (GCMBlockcipher.cs) 
                    if (expectedTag.Length < 12 || expectedTag.Length > 16)
                    {
                        testSkippedCauseofUnsupportedTagLength++;
                        continue;
                    }

                    if (isEncryption)
                        testGCM(aad, data, key, iv, expectedCT, expectedTag, fail, cipherAlg);
                    else
                    {
                        if (expectedCT.Length == 0)
                        {
                            testSkippedCauseofEmptyCipherText++;
                            continue;
                        }

                        testGCMDecrypt(aad, data, key, iv, expectedCT, expectedTag, fail, cipherAlg);
                    }
                }
            }

            Console.WriteLine("Skipped tests cause of empty cipher text: " + testSkippedCauseofEmptyCipherText);
            Console.WriteLine("Skipped tests cause of unsupported tag lenght: " + testSkippedCauseofUnsupportedTagLength);
            Console.WriteLine("Total test of GCM Encryption: " + totalTestofGCMEncryption);
            Console.WriteLine("Total test of GCM Decryption: " + totalTestofGCMDecryption);
        }

        public void testGCM(byte[] aad, byte[] plaintext, byte[] key, byte[] iv, byte[] expectedCT, byte[] expectedTag, bool fail, CipherAlg cipherAlg)
        {
            MemoryStream aadStream = new MemoryStream(aad);
            ParamsWithGCMSpec params_ = new ParamsWithGCMSpec(iv, aadStream);

            BufferedCipher encryptor = Crypto.getEncryptor(cipherAlg);
            encryptor.init(key, params_);

            byte[] encrypted = encryptor.doFinal(plaintext);
            byte[] tag = new byte[expectedTag.Length];
            Array.Copy(params_.getTag(), 0, tag, 0, expectedTag.Length);

            Assert.AreEqual(expectedTag, tag);
            Assert.AreEqual(expectedCT, encrypted);

            BufferedCipher decryptor = Crypto.getDecryptor(cipherAlg);
            decryptor.init(key, new ParamsWithGCMSpec(iv, aadStream, tag));
            byte[] decrypted = decryptor.doFinal(encrypted);

            Assert.AreEqual(decrypted, plaintext);
            totalTestofGCMEncryption++;
        }
        public void testGCMDecrypt(byte[] aad, byte[] plaintext, byte[] key, byte[] iv, byte[] CT, byte[] expectedTag, bool fail, CipherAlg cipherAlg)
        {
            MemoryStream aadStreamDec = new MemoryStream(aad);
            BufferedCipher decryptor = Crypto.getDecryptor(cipherAlg);
            decryptor.init(key, new ParamsWithGCMSpec(iv, aadStreamDec, expectedTag));
            try
            {
                byte[] decrypted = decryptor.doFinal(CT);
                Assert.AreEqual(plaintext, decrypted);
                totalTestofGCMDecryption++;
            }
            catch (InvalidCipherTextException e)
            {
                if (fail)
                {
                    Assert.AreEqual("mac check in GCM failed", e.Message);
                    totalTestofGCMDecryption++;
                }
                else
                    throw e;
            }
        }
    }
}
