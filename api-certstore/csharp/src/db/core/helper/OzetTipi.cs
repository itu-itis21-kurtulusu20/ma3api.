using tr.gov.tubitak.uekae.esya.api.crypto.alg;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class OzetTipi
    {
        public static readonly OzetTipi SHA1 = new OzetTipi(_enum.SHA1);
        public static readonly OzetTipi SHA224 = new OzetTipi(_enum.SHA224);
        public static readonly OzetTipi SHA256 = new OzetTipi(_enum.SHA256);
        public static readonly OzetTipi SHA384 = new OzetTipi(_enum.SHA384);
        public static readonly OzetTipi SHA512 = new OzetTipi(_enum.SHA512);

        enum _enum
        {
            SHA1 = 1,
            SHA224 = 2,
            SHA256 = 3,
            SHA384 = 4,
            SHA512 = 5
        }
        private readonly _enum mValue;
        private OzetTipi(_enum aEnum)
        {
            mValue = aEnum;
        }

        public int getIntValue()
        {
            return (int)mValue;
        }


        public static OzetTipi getNesne(int aTip)
        {
            switch (aTip)
            {
                case 1: return OzetTipi.SHA1;
                case 2: return OzetTipi.SHA224;
                case 3: return OzetTipi.SHA256;
                case 4: return OzetTipi.SHA384;
                case 5: return OzetTipi.SHA512;
                default: return null;
            }
        }


        public DigestAlg toDigestAlg()
        {
            switch ((int)mValue)
            {
                case 1: return DigestAlg.SHA1;
                case 2: return DigestAlg.SHA224;
                case 3: return DigestAlg.SHA256;
                case 4: return DigestAlg.SHA384;
                case 5: return DigestAlg.SHA512;
                default: return null;
            }
        }


        public static OzetTipi fromDigestAlg(DigestAlg aDigestAlg)
        {
            //switch (aDigestAlg)
            //{
            //    case SHA1: return OzetTipi.SHA1;
            //    case SHA224: return OzetTipi.SHA224;
            //    case SHA256: return OzetTipi.SHA256;
            //    case SHA384: return OzetTipi.SHA384;
            //    case SHA512: return OzetTipi.SHA512;
            //    default: return null;
            //}           
            if (aDigestAlg.Equals(DigestAlg.SHA1))
            {
                return OzetTipi.SHA1;
            }
            else if (aDigestAlg.Equals(DigestAlg.SHA224))
            {
                return OzetTipi.SHA224;
            }
            else if (aDigestAlg.Equals(DigestAlg.SHA256))
            {
                return OzetTipi.SHA256;
            }
            else if (aDigestAlg.Equals(DigestAlg.SHA384))
            {
                return OzetTipi.SHA384;
            }
            else if (aDigestAlg.Equals(DigestAlg.SHA512))
            {
                return OzetTipi.SHA512;
            }
            else
            {
                return null;
            }
        }
    }
}
