using System;
using System.Linq;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    [TestFixture]
    public class SymmetricCipherTest
    {
        private static String TEST_DATA = "Hey, privacy is not a perfume!";

        public void aes(byte[] iv)
        {
            byte[] secretKey = new byte[32];
            IAlgorithmParams params_ = new ParamsWithIV(iv);

            BufferedCipher cipher = Crypto.getEncryptor(CipherAlg.AES256_CBC);

            cipher.init(secretKey, params_);

            byte[] encrypted = cipher.doFinal(ASCIIEncoding.ASCII.GetBytes(TEST_DATA));

            byte[] decrypted = CipherUtil.decrypt(CipherAlg.AES256_CBC, params_, encrypted, secretKey);

            Assert.AreEqual(TEST_DATA, ASCIIEncoding.ASCII.GetString(decrypted));
        }
        [Test]
        public void testAESNoIV()
        {
            aes(null);
        }
        [Test]
        public void testAESWithIV()
        {
            aes(RandomUtil.generateRandom(16));
        }
        [Test]
        public void testAesReset()
        {
            byte[] secretKey = new byte[32];
            IAlgorithmParams params_ = new ParamsWithIV(RandomUtil.generateRandom(16));

            BufferedCipher cipher = Crypto.getEncryptor(CipherAlg.AES256_CBC);

            cipher.init(secretKey, params_);

            byte[] encrypted = cipher.doFinal(ASCIIEncoding.ASCII.GetBytes(TEST_DATA));

            cipher.reset();
            byte[] encryptedAgain = cipher.doFinal(ASCIIEncoding.ASCII.GetBytes(TEST_DATA));

            Assert.True(encrypted.SequenceEqual(encryptedAgain));

            BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.AES256_CBC);
            decryptor.init(secretKey, params_);

            byte[] decrypted = decryptor.doFinal(encrypted);
            decryptor.reset();
            byte[] decryptedAgain = decryptor.doFinal(encrypted);
            Assert.IsTrue(decrypted.SequenceEqual(decryptedAgain));

            Assert.AreEqual(TEST_DATA, ASCIIEncoding.ASCII.GetString(decrypted));
        }
    }
}
