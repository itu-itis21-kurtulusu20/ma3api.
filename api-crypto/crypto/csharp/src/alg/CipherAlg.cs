using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.x509;

/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class CipherAlg : IAlgorithm
    {
        private static readonly Dictionary<OID, CipherAlg> msOidRegistry = new Dictionary<OID, CipherAlg>();
        private static readonly Dictionary<String, CipherAlg> msNameRegistry = new Dictionary<String, CipherAlg>();

        // todo OID degerleri ogren ve duzelt

        public static readonly CipherAlg AES128_CBC = new CipherAlg(_aesValues.id_aes128_CBC, "AES128/CBC/PKCS7", 16, Mod.CBC, Padding.PKCS7);
        public static readonly CipherAlg AES128_CFB = new CipherAlg(_aesValues.id_aes128_CFB, "AES128/CFB/PKCS7", 16, Mod.CFB, Padding.PKCS7);
        public static readonly CipherAlg AES128_ECB = new CipherAlg(_aesValues.id_aes128_ECB, "AES128/ECB/PKCS7", 16, Mod.ECB, Padding.PKCS7);
        public static readonly CipherAlg AES128_OFB = new CipherAlg(_aesValues.id_aes128_OFB, "AES128/OFB/PKCS7", 16, Mod.OFB, Padding.PKCS7);
        //GCM için no padding olması lazım.
        public static readonly CipherAlg AES128_GCM = new CipherAlg(_aesValues.id_aes128_GCM, "AES128/GCM/NOPADDING", 16, Mod.GCM, Padding.NONE);

        public static readonly CipherAlg AES192_CBC = new CipherAlg(_aesValues.id_aes192_CBC, "AES192/CBC/PKCS7", 16, Mod.CBC, Padding.PKCS7);
        public static readonly CipherAlg AES192_CFB = new CipherAlg(_aesValues.id_aes192_CFB, "AES192/CFB/PKCS7", 16, Mod.CFB, Padding.PKCS7);
        public static readonly CipherAlg AES192_ECB = new CipherAlg(_aesValues.id_aes192_ECB, "AES192/ECB/PKCS7", 16, Mod.ECB, Padding.PKCS7);
        public static readonly CipherAlg AES192_OFB = new CipherAlg(_aesValues.id_aes192_OFB, "AES192/OFB/PKCS7", 16, Mod.OFB, Padding.PKCS7);
        //GCM için no padding olması lazım.
        public static readonly CipherAlg AES192_GCM    = new CipherAlg(_aesValues.id_aes192_GCM, "AES192/GCM/NOPADDING", 16, Mod.GCM, Padding.NONE);

        public static readonly CipherAlg AES256_CBC = new CipherAlg(_aesValues.id_aes256_CBC, "AES256/CBC/PKCS7", 16, Mod.CBC, Padding.PKCS7);
        public static readonly CipherAlg AES256_CFB = new CipherAlg(_aesValues.id_aes256_CFB, "AES256/CFB/PKCS7", 16, Mod.CFB, Padding.PKCS7);
        public static readonly CipherAlg AES256_ECB = new CipherAlg(_aesValues.id_aes256_ECB, "AES256/ECB/PKCS7", 16, Mod.ECB, Padding.PKCS7);
        public static readonly CipherAlg AES256_OFB = new CipherAlg(_aesValues.id_aes256_OFB, "AES256/OFB/PKCS7", 16, Mod.OFB, Padding.PKCS7);
        //GCM için no padding olması lazım.
        public static readonly CipherAlg AES256_GCM    = new CipherAlg(_aesValues.id_aes256_GCM, "AES256/GCM/NOPADDING", 16, Mod.GCM, Padding.NONE);

        // rc
        public static readonly CipherAlg RC2_CBC = new CipherAlg(_algorithmsValues.rc2_cbc, "RC2/CBC/PKCS7", 8, Mod.CBC, Padding.PKCS7);

        //triple des
        public static readonly CipherAlg DES_EDE3_CBC = new CipherAlg(_algorithmsValues.des_ede3_cbc, "3DES/CBC/PKCS7", 8, Mod.CBC, Padding.PKCS7);

        // asymetric 
        public static readonly CipherAlg RSA_PKCS1 = new CipherAlg(_algorithmsValues.rsaEncryption, "RSA/NONE/PKCS1", 0, Mod.NONE, Padding.PKCS1);
        [Obsolete]
        public static readonly CipherAlg RSA_OAEP = new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP", 0, Mod.NONE, OAEPPadding.OAEP_SHA1_MGF1);
        public static readonly CipherAlg RSA_ECB_PKCS1 = new CipherAlg(null, "RSA/ECB/PKCS1", 0, Mod.ECB, Padding.PKCS1);
        [Obsolete]
        public static readonly CipherAlg RSA_ECB_OAEP = new CipherAlg(null, "RSA/ECB/OAEP", 0, Mod.ECB, OAEPPadding.OAEP_SHA1_MGF1);
        public static readonly CipherAlg RSA_OAEP_SHA256 = new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP/SHA256",  0,  Mod.NONE, OAEPPadding.OAEP_SHA256_MGF1);
        public static readonly CipherAlg RSA_OAEP_SHA512 = new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP/SHA512",  0,  Mod.NONE, OAEPPadding.OAEP_SHA512_MGF1);

        // todo rsa pss


        protected int[] mOID;
        protected String mName;

        protected int mBlockSize;
        protected Mod mMod;
        protected Padding mPadding;

        public CipherAlg(int[] aOId, String aName, int aBlockSize, Mod aMod, Padding aPadding)
        {
            mOID = aOId;
            mName = aName;
            mBlockSize = aBlockSize;
            mMod = aMod;
            mPadding = aPadding;

            if (aOId != null)
            {
                OID oid = new OID(aOId);                
                msOidRegistry[oid]= this;
            }
            if (aName != null)
            {                
                msNameRegistry[aName] = this;
            }
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


        public EAlgorithmIdentifier toAlgorithmIdentifier(byte[] aIV)
        {
            EAlgorithmIdentifier algId = new EAlgorithmIdentifier(new AlgorithmIdentifier());

            algId.setAlgorithm(new Asn1ObjectIdentifier(getOID()));

            if (aIV != null)
            {
                try
                {
                    Asn1OctetString ivOctet = new Asn1OctetString(aIV);
                    Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                    ivOctet.Encode(encBuf);
                    Asn1OpenType open = new Asn1OpenType(encBuf.MsgCopy);
                    algId.setParameters(open);
                }
                catch (Asn1Exception ex)
                {
                    // todo i18n
                    throw new CryptoException("Asn1 işlemlerinde hata çıktı.", ex);
                }

            }

            return algId;
        }

        public static CipherAlg fromOID(int[] aOid)
        {
            OID oid = new OID(aOid);
            CipherAlg cipherAlg = null;
            msOidRegistry.TryGetValue(oid, out cipherAlg);
            return cipherAlg;
        }

        public static CipherAlg fromName(String aAlgName)
        {
            CipherAlg cipherAlg = null;
            msNameRegistry.TryGetValue(aAlgName, out cipherAlg);
            return cipherAlg;
        }

        public static Pair<CipherAlg, IAlgorithmParams> fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgoritma)
        {
            int[] oid = aAlgoritma.getAlgorithm().mValue;
            Asn1OpenType parameters = aAlgoritma.getParameters();  
            Asn1OctetString ivString = new Asn1OctetString();
            ParamsWithIV algoParams = null;
            // 05 00 = asn1null

            if (oid.SequenceEqual(_algorithmsValues.id_RSAES_OAEP))
            {
                try
                {
                    OAEPPadding paddingAlg = null;
                    if (parameters.Equals(new byte[] {0x30, 00}))
                        paddingAlg = OAEPPadding.OAEP_SHA1_MGF1;
                    else
                        paddingAlg = OAEPPadding.fromRSAES_OAEP_params(parameters.mValue);

                    return new Pair<CipherAlg, IAlgorithmParams>(
                    new CipherAlg(_algorithmsValues.id_RSAES_OAEP, "RSA/NONE/OAEP", 0, Mod.NONE, paddingAlg), null);

                }
                catch (IOException Exception)
                {
                    
                    throw new CryptoException("Algoritma içinden OAEP parametreleri alınırken hata oluştu", Exception);
                }
            }

            if (parameters != null && parameters.mValue.Length > 1 && (!(parameters.mValue[0] == 5 && parameters.mValue[1] == 0)))
            {
                try
                {
                    if (isGCMOID(oid))
                    {
                        Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(parameters.mValue);
                        GCMParameters gcmparams = new GCMParameters();
                        gcmparams.Decode(buff);
                        ivString = gcmparams.aes_nonce;
                        algoParams = new ParamsWithGCMSpec(ivString.mValue);
                    }
                    else
                    {
                        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                        parameters.Encode(encBuf);
                        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.MsgCopy);
                        ivString.Decode(decBuf);

                        algoParams = new ParamsWithIV(ivString.mValue);
                    }
                }
                catch (Asn1Exception aEx)
                {
                    throw new CryptoException("Algorithm içinden IV değeri alınırken hata oluştu", aEx);
                }
                catch (IOException aEx)
                {
                    throw new CryptoException("Algorithm içinden IV değeri alınırken hata oluştu", aEx);
                }
            }

            CipherAlg alg = fromOID(oid);
            return new Pair<CipherAlg, IAlgorithmParams>(
                    new CipherAlg(alg.getOID(), alg.getName(), alg.getBlockSize(), alg.getMod(), alg.getPadding()),
                    algoParams);
        }

        public static bool isGCMOID(int[] oid)
        {
           if (oid.SequenceEqual(_aesValues.id_aes128_GCM) || oid.SequenceEqual(_aesValues.id_aes192_GCM) || oid.SequenceEqual(_aesValues.id_aes256_GCM))
             return true;
           else
             return false;
        }


        //@Override
        public override String ToString()
        {
            return mName;
        }

        //@Override
        public override bool Equals(Object o)
        {
            if (this == o) return true;
            if (o == null || /*getClass() != o.getClass()*/!(o is CipherAlg)) return false;

            CipherAlg cipherAlg = (CipherAlg)o;

            if (mBlockSize != cipherAlg.mBlockSize) return false;
            if (mMod != cipherAlg.mMod) return false;
            if (mName != null ? !mName.Equals(cipherAlg.mName) : cipherAlg.mName != null) return false;
            if (/*!Arrays.equals(mOID, cipherAlg.mOID)*/!mOID.SequenceEqual<int>(cipherAlg.mOID)) return false;
            if (mPadding != cipherAlg.mPadding) return false;

            return true;
        }

        //@Override
        public override int GetHashCode()
        {
            int result = mOID != null ? /*Arrays.hashCode(mOID)*/mOID.GetHashCode() : 0;
            result = 31 * result + (mName != null ? mName.GetHashCode() : 0);
            result = 31 * result + mBlockSize;
            result = 31 * result + (mMod != null ? mMod.GetHashCode() : 0);
            result = 31 * result + (mPadding != null ? mPadding.GetHashCode() : 0);
            return result;
        }
    }

}
