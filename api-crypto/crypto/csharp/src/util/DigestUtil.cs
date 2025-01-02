using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.util
{
    public static class DigestUtil
    {
        private static readonly int BLOCK_SIZE = 10000;

        public static byte[] digest(DigestAlg aDigestAlg, byte[] aInput, int aOffset, int aLength)
        {
            IDigester digester = Crypto.getDigester(aDigestAlg);
            digester.update(aInput, aOffset, aLength);
            return digester.digest();
        }

        public static byte[] digest(DigestAlg aDigestAlg, byte[] aInput)
        {
            return digest(aDigestAlg, aInput, 0, aInput.Length);
        }

        public static byte[] digestFile(DigestAlg aDigestAlg, String aFileName)
        {
            return digestFile(aDigestAlg, aFileName, BLOCK_SIZE);
        }

        public static byte[] digestFile(DigestAlg aDigestAlg, String aFileName, int aBlockSize)
        {
            byte[] ozet = null;
            using (FileStream is_ = new FileStream(aFileName, FileMode.Open, FileAccess.Read))
            {
                ozet = digestStream(aDigestAlg, is_, aBlockSize);
            }
            //is_.Close();
            return ozet;
        }

        public static byte[] digestStream(DigestAlg aDigestAlg, Stream aInputStream)
        {
            return digestStream(aDigestAlg, aInputStream, BLOCK_SIZE);
        }

        public static byte[] digestStream(DigestAlg aDigestAlg, Stream aInputStream, int aBlockSize)
        {
            IDigester digester = Crypto.getDigester(aDigestAlg);
            byte[] block = new byte[aBlockSize];
            int fRead;
            //DotNet standardında stream sonunda 0 dönülüyor.
            while ((fRead = aInputStream.Read(block, 0, block.Length)) != 0)
            {
                digester.update(block, 0, fRead);
            }
            aInputStream.Close();
            return digester.digest();
        }
    }
}
