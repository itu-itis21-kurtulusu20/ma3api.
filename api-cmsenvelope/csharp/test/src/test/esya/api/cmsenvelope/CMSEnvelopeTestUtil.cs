using System;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace test.esya.api.cmsenvelope
{
    public class CMSEnvelopeTestUtil
    {
        public static void DecryptWithStream(IPrivateKey privKey, ECertificate cert, String encryptedFileName, String decryptedFileName)
        {
            using (FileStream encryptedInputStream = new FileStream(encryptedFileName, FileMode.Open), decryptedOutputStream = new FileStream(decryptedFileName, FileMode.Create))
            {
                Pair<ECertificate, IPrivateKey> recipient = new Pair<ECertificate, IPrivateKey>(cert, privKey);
                IDecryptorStore decryptor = new MemoryDecryptor(recipient);
                CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
                cmsParser.open(decryptedOutputStream, decryptor);

                decryptedOutputStream.Close();
                encryptedInputStream.Close();
            }
        }

        public static byte[] DecryptWithStream(IPrivateKey privKey, ECertificate cert, Stream encryptedInputStream)
        {
            MemoryStream decryptedOutputStream = new MemoryStream();

            Pair<ECertificate, IPrivateKey> recipient = new Pair<ECertificate, IPrivateKey>(cert, privKey);
            IDecryptorStore decryptor = new MemoryDecryptor(recipient);
            CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
            cmsParser.open(decryptedOutputStream, decryptor);

            decryptedOutputStream.Close();
            encryptedInputStream.Close();

            return decryptedOutputStream.ToArray();
        }

        public static byte[] DecryptWithMemory(ECertificate cert, IPrivateKey privKey, byte[] encryptedCMS)
        {
            Pair<ECertificate, IPrivateKey> recipient = new Pair<ECertificate, IPrivateKey>(cert, privKey);
            IDecryptorStore decryptor = new MemoryDecryptor(recipient);
            CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedCMS);
            byte[] plainData = cmsParser.open(decryptor);

            return plainData;
        }

        public static byte[] GetDigest(String expected)
        {          
            SHA1 sha = new SHA1CryptoServiceProvider();
            using (FileStream expectedInputStream = new FileStream(expected, FileMode.Open, FileAccess.Read))
            {        
              return sha.ComputeHash(expectedInputStream);
            }
        }

      /**
      * 
      * @param expected file path
      * @param actual file path
      */
        public static bool CompareFiles(String expected, String actual)
        {
           byte[] expectedDigest = GetDigest(expected);
           byte[] actualDigest = GetDigest(actual);

           if(expectedDigest.SequenceEqual(actualDigest))
               return true;
           else
               return false;
        }
    }
}
