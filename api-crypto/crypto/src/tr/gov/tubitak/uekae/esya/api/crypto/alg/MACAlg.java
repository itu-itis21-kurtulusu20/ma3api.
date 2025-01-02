package tr.gov.tubitak.uekae.esya.api.crypto.alg;

import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

/**
 * @author ayetgin
 */

public class MACAlg implements Algorithm
{
    // todo algs...

    public static final MACAlg HMAC_MD5     = new MACAlg(null,                          "HMAC-MD5",       DigestAlg.MD5);
    public static final MACAlg HMAC_RIPEMD  = new MACAlg(null,                          "HMAC-RIPEMD160", DigestAlg.RIPEMD);
    public static final MACAlg HMAC_SHA1    = new MACAlg(_algorithmsValues.hMAC_SHA1,   "HMAC-SHA1",      DigestAlg.SHA1);
    public static final MACAlg HMAC_SHA256  = new MACAlg(null,                          "HMAC-SHA256",    DigestAlg.SHA256);
    public static final MACAlg HMAC_SHA384  = new MACAlg(null,                          "HMAC-SHA384",    DigestAlg.SHA384);
    public static final MACAlg HMAC_SHA512  = new MACAlg(null,                          "HMAC-512",       DigestAlg.SHA512);

    private String mName;
    private DigestAlg mDigestAlg;
    private int[] mOID;

    public MACAlg(int[] aOID, String aName, DigestAlg aDigestAlg)
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
