using System;
using tr.gov.tubitak.uekae.esya.asn.cmp;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class MACAlg : IAlgorithm
    {
        // todo algs...

        public static readonly MACAlg HMAC_MD5 = new MACAlg(null, "HMAC-MD5", DigestAlg.MD5);
        public static readonly MACAlg HMAC_RIPEMD = new MACAlg(null, "HMAC-RIPEMD160", DigestAlg.RIPEMD160);
        public static readonly MACAlg HMAC_SHA1 = new MACAlg(_cmpValues.id_HMAC_SHA1, "HMAC-SHA1", DigestAlg.SHA1);
        public static readonly MACAlg HMAC_SHA256 = new MACAlg(null, "HMAC-SHA256", DigestAlg.SHA256);
        public static readonly MACAlg HMAC_SHA384 = new MACAlg(null, "HMAC-SHA384", DigestAlg.SHA384);
        public static readonly MACAlg HMAC_SHA512 = new MACAlg(null, "HMAC-512", DigestAlg.SHA512);

        private readonly String mName;
        private readonly DigestAlg mDigestAlg;
        private readonly int[] mOID;

        private MACAlg(int[] aOID, String aName, DigestAlg aDigestAlg)
        {
            mName = aName;
            mDigestAlg = aDigestAlg;
            mOID = aOID;
        }

        public String getName()
        {
            return mName;
        }

        public int[] getOID()
        {
            return mOID;
        }

        public DigestAlg getDigestAlg()
        {
            return mDigestAlg;
        }
    }
}
