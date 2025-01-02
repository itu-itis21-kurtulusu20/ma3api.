package tr.gov.tubitak.uekae.esya.api.crypto.alg;

import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.common.crypto.algorithms._aesValues;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */

public class WrapAlg implements Algorithm
{
    private static Map<OID, WrapAlg> msOidRegistry = new HashMap<OID, WrapAlg>();


    public static final WrapAlg AES128 = new WrapAlg(_aesValues.id_aes128_wrap,     "AES128", Mod.NONE, Padding.NONE);
    public static final WrapAlg AES128_CBC = new WrapAlg(CipherAlg.AES128_CBC);

    public static final WrapAlg AES192 = new WrapAlg(_aesValues.id_aes192_wrap,     "AES192", Mod.NONE, Padding.NONE);
    public static final WrapAlg AES192_CBC = new WrapAlg(CipherAlg.AES192_CBC);

    public static final WrapAlg AES256 = new WrapAlg(_aesValues.id_aes256_wrap,     "AES256", Mod.NONE, Padding.NONE);
    public static final WrapAlg AES256_CBC = new WrapAlg(CipherAlg.AES256_CBC);

    public static final WrapAlg RSA_PKCS1 = new WrapAlg(CipherAlg.RSA_PKCS1);
    public static final WrapAlg RSA_ECB_PKCS1 = new WrapAlg(CipherAlg.RSA_ECB_PKCS1);

    public static final WrapAlg RSA_OAEP = new WrapAlg(CipherAlg.RSA_OAEP);
    public static final WrapAlg RSA_ECB_OAEP = new WrapAlg(CipherAlg.RSA_ECB_OAEP);


    private int[] mOID;
    protected Mod mMod;
    protected Padding mPadding;
    private String mName;
    private CipherAlg cipherAlg;


    public WrapAlg(int[] aOID, String aName, Mod aMod, Padding aPadding)
    {
        mOID = aOID;
        mName = aName;
        mMod = aMod;
        mPadding = aPadding;
        if (aOID != null){
            msOidRegistry.put(new OID(mOID), this);
        }
    }
    public WrapAlg(CipherAlg cipherAlg)
    {
        this.cipherAlg = cipherAlg;
        mOID = cipherAlg.getOID();
        mName = cipherAlg.getName();
        mMod = cipherAlg.getMod();
        mPadding = cipherAlg.getPadding();
        mOID = cipherAlg.getOID();
        if (mOID != null){
            msOidRegistry.put(new OID(mOID), this);
        }
    }

    public String getName()
    {
        return mName;
    }

    public int[] getOID()
    {
        return mOID;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WrapAlg wrapAlg = (WrapAlg) o;

        if (mName != null ? !mName.equals(wrapAlg.mName) : wrapAlg.mName != null) return false;
        if (!Arrays.equals(mOID, wrapAlg.mOID)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = mOID != null ? Arrays.hashCode(mOID) : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        return result;
    }

    public static WrapAlg fromOID(int[] aOid){
    	OID oid = new OID(aOid);
        return msOidRegistry.get(oid);
    }

    public CipherAlg getCipherAlg() {
        return cipherAlg;
    }
}
