using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public class HashInfo
    {
        DigestAlg digestAlg;
        byte[] hash;
        
        public HashInfo(DigestAlg digestAlg, byte[] hash)
        {
            this.digestAlg = digestAlg;
            this.hash = hash;
        }

        public DigestAlg getDigestAlg()
        {
            return digestAlg;
        }

        public byte[] getHash()
        {
            return hash;
        }
    }
}
