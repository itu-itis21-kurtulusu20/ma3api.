package tr.gov.tubitak.uekae.esya.api.crypto.alg;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.asn.algorithms.GCMParameters;
import tr.gov.tubitak.uekae.esya.api.common.crypto.algorithms._aesValues;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */

public class CipherAlg implements Algorithm
{
    private static Map<OID, CipherAlg> msOidRegistry = new HashMap<OID, CipherAlg>();
    private static Map<String, CipherAlg> msNameRegistry = new HashMap<String, CipherAlg>();


    // todo OID degerleri ogren ve duzelt
    
    public static final CipherAlg AES128_CBC    = new CipherAlg(_aesValues.id_aes128_CBC, Algorithms.CIPHER_AES128_CBC, 16, Mod.CBC, Padding.PKCS7);
    public static final CipherAlg AES128_CBC_NOPADDING = new CipherAlg(_aesValues.id_aes128_CBC, Algorithms.CIPHER_AES128_CBC_NOPADDING, 16, Mod.CBC, Padding.NONE, false);
    public static final CipherAlg AES128_CFB    = new CipherAlg(_aesValues.id_aes128_CFB, Algorithms.CIPHER_AES128_CFB, 16, Mod.CFB, Padding.PKCS7);
    public static final CipherAlg AES128_ECB    = new CipherAlg(_aesValues.id_aes128_ECB, Algorithms.CIPHER_AES128_ECB, 16, Mod.ECB, Padding.PKCS7);
    public static final CipherAlg AES128_ECB_NOPADDING = new CipherAlg(_aesValues.id_aes128_ECB, Algorithms.CIPHER_AES128_ECB_NOPADDING, 16, Mod.ECB, Padding.NONE, false);
    public static final CipherAlg AES128_OFB    = new CipherAlg(_aesValues.id_aes128_OFB, Algorithms.CIPHER_AES128_OFB, 16, Mod.OFB, Padding.PKCS7);
    //GCM için no padding olması lazım.
    public static final CipherAlg AES128_GCM    = new CipherAlg(_aesValues.id_aes128_GCM, Algorithms.CIPHER_AES128_GCM, 16, Mod.GCM, Padding.NONE);

    public static final CipherAlg AES192_CBC    = new CipherAlg(_aesValues.id_aes192_CBC, Algorithms.CIPHER_AES192_CBC, 16, Mod.CBC, Padding.PKCS7);
    public static final CipherAlg AES192_CBC_NOPADDING = new CipherAlg(_aesValues.id_aes192_CBC, Algorithms.CIPHER_AES192_CBC_NOPADDING, 16, Mod.CBC, Padding.NONE, false);
    public static final CipherAlg AES192_CFB    = new CipherAlg(_aesValues.id_aes192_CFB, Algorithms.CIPHER_AES192_CFB, 16, Mod.CFB, Padding.PKCS7);
    public static final CipherAlg AES192_ECB    = new CipherAlg(_aesValues.id_aes192_ECB, Algorithms.CIPHER_AES192_ECB, 16, Mod.ECB, Padding.PKCS7);
    public static final CipherAlg AES192_ECB_NOPADDING = new CipherAlg(_aesValues.id_aes192_ECB, Algorithms.CIPHER_AES192_ECB_NOPADDING, 16, Mod.ECB, Padding.NONE, false);
    public static final CipherAlg AES192_OFB    = new CipherAlg(_aesValues.id_aes192_OFB, Algorithms.CIPHER_AES192_OFB, 16, Mod.OFB, Padding.PKCS7);
    //GCM için no padding olması lazım.
    public static final CipherAlg AES192_GCM    = new CipherAlg(_aesValues.id_aes192_GCM, Algorithms.CIPHER_AES192_GCM, 16, Mod.GCM, Padding.NONE);

    public static final CipherAlg AES256_CBC    = new CipherAlg(_aesValues.id_aes256_CBC, Algorithms.CIPHER_AES256_CBC, 16, Mod.CBC, Padding.PKCS7);
    public static final CipherAlg AES256_CBC_NOPADDING = new CipherAlg(_aesValues.id_aes256_CBC, Algorithms.CIPHER_AES256_CBC_NOPADDING, 16, Mod.CBC, Padding.NONE, false);
    public static final CipherAlg AES256_CFB    = new CipherAlg(_aesValues.id_aes256_CFB, Algorithms.CIPHER_AES256_CFB, 16, Mod.CFB, Padding.PKCS7);
    public static final CipherAlg AES256_ECB    = new CipherAlg(_aesValues.id_aes256_ECB, Algorithms.CIPHER_AES256_ECB, 16, Mod.ECB, Padding.PKCS7);
    public static final CipherAlg AES256_ECB_NOPADDING = new CipherAlg(_aesValues.id_aes256_ECB, Algorithms.CIPHER_AES256_ECB_NOPADDING, 16, Mod.ECB, Padding.NONE, false);
    public static final CipherAlg AES256_OFB    = new CipherAlg(_aesValues.id_aes256_OFB, Algorithms.CIPHER_AES256_OFB, 16, Mod.OFB, Padding.PKCS7);
    //GCM için no padding olması lazım.
    public static final CipherAlg AES256_GCM    = new CipherAlg(_aesValues.id_aes256_GCM, Algorithms.CIPHER_AES256_GCM, 16, Mod.GCM, Padding.NONE);
    

    // rc
    public static final CipherAlg RC2_CBC       = new CipherAlg(_algorithmsValues.rc2_cbc, "RC2/CBC/PKCS7",    8, Mod.CBC, Padding.PKCS7);

    // DES
    public static final CipherAlg DES_CBC       = new CipherAlg(_algorithmsValues.des_cbc, Algorithms.CIPHER_DES_CBC, 8, Mod.CBC, Padding.PKCS7);
    public static final CipherAlg DES_CBC_NOPADDING = new CipherAlg(_algorithmsValues.des_cbc, Algorithms.CIPHER_DES_CBC_NOPADDING, 8, Mod.CBC, Padding.NONE, false);
    public static final CipherAlg DES_ECB       = new CipherAlg(_algorithmsValues.des_ecb, Algorithms.CIPHER_DES_ECB, 8, Mod.ECB, Padding.PKCS7);
    public static final CipherAlg DES_ECB_NOPADDING = new CipherAlg(_algorithmsValues.des_ecb, Algorithms.CIPHER_DES_ECB_NOPADDING, 8, Mod.ECB, Padding.NONE, false);

    // triple des
    public static final CipherAlg DES_EDE3_CBC  = new CipherAlg(_algorithmsValues.des_EDE3_CBC, Algorithms.CIPHER_DES_EDE3_CBC, 8, Mod.CBC, Padding.PKCS7);
    public static final CipherAlg DES_EDE3_CBC_NOPADDING = new CipherAlg(_algorithmsValues.des_EDE3_CBC, Algorithms.CIPHER_DES_EDE3_CBC_NOPADDING, 8, Mod.CBC, Padding.NONE, false);
    public static final CipherAlg DES_EDE3_ECB  = new CipherAlg(_algorithmsValues.des_EDE3_ECB, Algorithms.CIPHER_DES_EDE3_ECB, 8, Mod.ECB, Padding.PKCS7);
    public static final CipherAlg DES_EDE3_ECB_NOPADDING = new CipherAlg(_algorithmsValues.des_EDE3_ECB, Algorithms.CIPHER_DES_EDE3_ECB_NOPADDING, 8, Mod.ECB, Padding.NONE, false);

    // asymetric 
    public static final CipherAlg RSA_PKCS1     = new CipherAlg(_algorithmsValues.rsaEncryption, "RSA/NONE/PKCS1", 0,  Mod.NONE, Padding.PKCS1);
    public static final CipherAlg RSA_RAW		= new CipherAlg(null,							  Algorithms.CIPHER_RSA_RAW, 0,  Mod.NONE, Padding.NONE);
    @Deprecated
    public static final CipherAlg RSA_OAEP      = new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP",  0,  Mod.NONE, OAEPPadding.OAEP_SHA1_MGF1);
    public static final CipherAlg RSA_ECB_PKCS1 = new CipherAlg(null,                            "RSA/ECB/PKCS1",  0,  Mod.ECB,  Padding.PKCS1);
    @Deprecated
    public static final CipherAlg RSA_ECB_OAEP  = new CipherAlg(null,                            "RSA/ECB/OAEP",   0,  Mod.ECB,  OAEPPadding.OAEP_SHA1_MGF1);
    public static final CipherAlg RSA_OAEP_SHA256 = new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP/SHA256",  0,  Mod.NONE, OAEPPadding.OAEP_SHA256_MGF1);
    public static final CipherAlg RSA_OAEP_SHA512 = new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP/SHA512",  0,  Mod.NONE, OAEPPadding.OAEP_SHA512_MGF1);



    // todo rsa pss

    protected int[] mOID;
    protected String mName;

    protected int mBlockSize;
    protected Mod mMod;
    protected Padding mPadding;

    public CipherAlg(int[] aOId, String aName, int aBlockSize, Mod aMod, Padding aPadding) {
        this(aOId, aName, aBlockSize, aMod, aPadding, true);
    }

    public CipherAlg(int[] aOId, String aName, int aBlockSize, Mod aMod, Padding aPadding, boolean addToRegistry)
    {
        mOID = aOId;
        mName = aName;
        mBlockSize = aBlockSize;
        mMod = aMod;
        mPadding = aPadding;

        if (addToRegistry && (aOId != null))
        {
        	OID oid = new OID(aOId);
            msOidRegistry.put(oid, this);
        }
        msNameRegistry.put(aName, this);
    }

    public int[] getOID()
    {
        return mOID;
    }

    public String getName()
    {
        return mName;
    }

    public int getBlockSize()
    {
        return mBlockSize;
    }

    public Mod getMod()
    {
        return mMod;
    }

    public Padding getPadding()
    {
        return mPadding;
    }


    public EAlgorithmIdentifier toAlgorithmIdentifier(byte[] aIV) throws CryptoException
    {
        EAlgorithmIdentifier algId = new EAlgorithmIdentifier(new AlgorithmIdentifier());

        algId.setAlgorithm(new Asn1ObjectIdentifier(getOID()));

        if(aIV != null)
        {
            try
            {
                Asn1OctetString ivOctet = new Asn1OctetString(aIV);
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                ivOctet.encode(encBuf);
                Asn1OpenType open = new Asn1OpenType(encBuf.getMsgCopy());
                algId.setParameters(open);
            }
            catch (Asn1Exception ex)
            {
                // todo i18n
                 throw new CryptoException("Asn1 islemlerinde hata cikti.",ex);
            }

        }

        return algId;
    }

    public static CipherAlg fromOID(int[] aOid){
    	OID oid = new OID(aOid);
        return msOidRegistry.get(oid);
    }

    public static CipherAlg fromName(String aAlgName){
        return msNameRegistry.get(aAlgName);
    }

    public static Pair<CipherAlg, AlgorithmParams> fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgoritma) throws CryptoException {
        int[] oid = aAlgoritma.getAlgorithm().value;
        Asn1OpenType params = aAlgoritma.getParameters();
        Asn1OctetString ivString = new Asn1OctetString();
        ParamsWithIV algoParams = null;

        if(Arrays.equals(oid, _algorithmsValues.id_RSAES_OAEP))
        {
            try
            {
                OAEPPadding paddingAlg = null;
                if(params.equals(new byte []{0x30, 00}))
                    paddingAlg = OAEPPadding.OAEP_SHA1_MGF1;
                else
                    paddingAlg = OAEPPadding.fromRSAES_OAEP_params(params.value);

            return new Pair<CipherAlg, AlgorithmParams>(
                    new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP", 0, Mod.NONE, paddingAlg),
                    null
                );

            }
            catch (IOException aEx)
            {
                throw new CryptoException("Algorithm içinden OAEP parametreleri alınırken hata oluştu", aEx);
            }
        }

        // 05 00 = asn1null
         if(params != null && params.value.length > 1 && (!(params.value[0]==5 && params.value[1]==0)))
        {
            try
            {
                if(isGCMOID(oid))
                {
                    Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(params.value);
                    GCMParameters gcmparams = new GCMParameters();
                    gcmparams.decode(buff);
                    ivString = gcmparams.aes_nonce;
                    algoParams = new ParamsWithGCMSpec(ivString.value);

                }else{

                    Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                    params.encode(encBuf);
                    Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                    ivString.decode(decBuf);

                    algoParams = new ParamsWithIV(ivString.value);
                }

            }catch(Asn1Exception aEx)
            {
                throw new CryptoException("Algorithm içinden IV değeri alınırken hata oluştu", aEx);
            }
            catch (IOException aEx)
            {
                throw new CryptoException("Algorithm içinden IV değeri alınırken hata oluştu", aEx);
            }
        }

        CipherAlg alg = fromOID(oid);
        return new Pair<CipherAlg, AlgorithmParams>(
                new CipherAlg(alg. getOID(), alg.getName(), alg.getBlockSize(), alg.getMod(), alg.getPadding()),
                algoParams);
    }

    public static boolean isGCMOID(int[] oid){

        if(Arrays.equals(oid, _aesValues.id_aes128_GCM) || Arrays.equals(oid,_aesValues.id_aes192_GCM) || Arrays.equals(oid,_aesValues.id_aes256_GCM))
           return true;
        else
            return false;
    }

    @Override
    public String toString()
    {
        return mName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CipherAlg cipherAlg = (CipherAlg) o;

        if (mBlockSize != cipherAlg.mBlockSize) return false;
        if (mMod != cipherAlg.mMod) return false;
        if (mName != null ? !mName.equals(cipherAlg.mName) : cipherAlg.mName != null) return false;
        if (!Arrays.equals(mOID, cipherAlg.mOID)) return false;
        if (mPadding != cipherAlg.mPadding) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = mOID != null ? Arrays.hashCode(mOID) : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + mBlockSize;
        result = 31 * result + (mMod != null ? mMod.hashCode() : 0);
        result = 31 * result + (mPadding != null ? mPadding.hashCode() : 0);
        return result;
    }


}
