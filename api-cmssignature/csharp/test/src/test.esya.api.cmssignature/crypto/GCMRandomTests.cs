using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;

namespace test.esya.api.cmssignature.crypto
{
    public class GCMRandomTests
    {
        [Test]
        public void randomTests()
        {
            SecureRandom rng = new SecureRandom();
            rng.SetSeed(DateTime.Now.Ticks);
            double counter = 0;
            double COUNT = 100000;

            try
            {
                int mod = (int)(COUNT / 1000);
                mod = (mod == 0) ? 1 : mod;
                for (int i = 0; i < COUNT; i++)
                {
                    randomTests(rng);
                    if (i % mod == 0)
                        Console.WriteLine("Counter:" + counter);
                    counter++;
                }
                Console.WriteLine("Counter:" + counter);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        private void randomTests(Random rng)
        {
            int keyLength = 16;
            byte[] key = new byte[keyLength];
            rng.NextBytes(key);

            int pLength = rng.Next(65000);
            byte[] data = new byte[pLength];
            rng.NextBytes(data);

            int aLength = rng.Next(256);
            byte[] aad = new byte[aLength];
            rng.NextBytes(aad);

            int ivLength = 1 + rng.Next(256);
            byte[] iv = new byte[ivLength];
            rng.NextBytes(iv);

            testGCM(aad, data, key, iv, CipherAlg.AES128_GCM);
        }

        public void testGCM(byte[] aad, byte[] plaintext, byte[] key, byte[] iv, CipherAlg cipherAlg)
        {
            MemoryStream aadStream = new MemoryStream(aad);
            ParamsWithGCMSpec params_ = new ParamsWithGCMSpec(iv, aadStream);

            BufferedCipher encryptor = Crypto.getEncryptor(cipherAlg);
            encryptor.init(key, params_);

            byte[] encrypted = encryptor.doFinal(plaintext);
            byte[] tag = params_.getTag();

            BufferedCipher decryptor = Crypto.getDecryptor(cipherAlg);

            decryptor.init(key, new ParamsWithGCMSpec(iv, aadStream, tag));
            byte[] decrypted = decryptor.doFinal(encrypted);

            //Console.WriteLine("DecryptedText: "+ StringUtil.ToString(decrypted));
            Assert.AreEqual(decrypted, plaintext);
        }
    }
}