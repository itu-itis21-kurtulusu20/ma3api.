using System;
using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class WrapAlg : IAlgorithm
    {
        private static Dictionary<OID, WrapAlg> msOidRegistry = new Dictionary<OID, WrapAlg>();

        public static readonly WrapAlg AES128 = new WrapAlg(_aesValues.id_aes128_wrap, "AES128", Mod.NONE, Padding.NONE);
        public static readonly WrapAlg AES128_CBC = new WrapAlg(CipherAlg.AES128_CBC);


        public static readonly WrapAlg AES192 = new WrapAlg(_aesValues.id_aes192_wrap,     "AES192", Mod.NONE, Padding.NONE);
        public static readonly WrapAlg AES192_CBC = new WrapAlg(CipherAlg.AES192_CBC);
       
        public static readonly WrapAlg AES256 = new WrapAlg(_aesValues.id_aes256_wrap,     "AES256", Mod.NONE, Padding.NONE);
        public static readonly WrapAlg AES256_CBC = new WrapAlg(CipherAlg.AES256_CBC);

        public static readonly WrapAlg RSA_PKCS1 = new WrapAlg(CipherAlg.RSA_PKCS1);
        public static readonly WrapAlg RSA_ECB_PKCS1 = new WrapAlg(CipherAlg.RSA_ECB_PKCS1);

        public static readonly WrapAlg RSA_OAEP = new WrapAlg(CipherAlg.RSA_OAEP);
        public static readonly WrapAlg RSA_ECB_OAEP = new WrapAlg(CipherAlg.RSA_ECB_OAEP);

        private readonly int[] mOID;
        private readonly String mName;

        protected Mod mMod;
        protected Padding mPadding;
        private readonly CipherAlg cipherAlg;

        public WrapAlg(int[] aOID, String aName, Mod aMod, Padding aPadding)
        {
            mOID = aOID;
            mName = aName;
            mMod = aMod;
            mPadding = aPadding;
            if (aOID != null)
            {
                OID oid = new OID(mOID);
                msOidRegistry[oid] = this;
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
            if (mOID != null)
            {
                msOidRegistry[new OID(mOID)]=this;
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

        public override bool Equals(Object o)
        {
            if (this == o) return true;
            if (o == null/* || getClass() != o.getClass()*/) return false;

            WrapAlg wrapAlg = o as WrapAlg;
            if (wrapAlg == null)
                return false;

            if (mName != null ? !mName.Equals(wrapAlg.mName) : wrapAlg.mName != null) return false;
            if (!mOID.SequenceEqual<int>(wrapAlg.mOID)) return false;

            return true;
        }


        public override int GetHashCode()
        {
            return mName.GetHashCode() ^ mOID.GetHashCode();
        }

        public static WrapAlg fromOID(int[] aOid)
        {
            WrapAlg wrapAlg = null;
            OID oid = new OID(aOid);
            msOidRegistry.TryGetValue(oid, out wrapAlg);
            return wrapAlg;
        }

        public static WrapAlg fromCipherAlg(CipherAlg cipherAlg)
        {
          foreach (WrapAlg wrapAlg in msOidRegistry.Values) 
          {
            if(wrapAlg.getOID().SequenceEqual(cipherAlg.getOID()))
                return wrapAlg;
          }
            return null;
        }

    }
}
