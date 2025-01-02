using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.common;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class KeyAgreementAlg : IAlgorithm
    {
        private static Dictionary<OID, KeyAgreementAlg> msOidRegistry = new Dictionary<OID, KeyAgreementAlg>();
    
        public static readonly KeyAgreementAlg ECDH_SHA1KDF = new KeyAgreementAlg(new int[]{1,3,133,16,840,63,0,2}, AgreementAlg.ECDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA1));
        public static readonly KeyAgreementAlg ECDH_SHA224KDF = new KeyAgreementAlg(new int[]{1,3,132,1,11,0}, AgreementAlg.ECDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA224));
        public static readonly KeyAgreementAlg ECDH_SHA256KDF = new KeyAgreementAlg(new int[]{1,3,132,1,11,1}, AgreementAlg.ECDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA256));
        public static readonly KeyAgreementAlg ECDH_SHA384KDF = new KeyAgreementAlg(new int[]{1,3,132,1,11,2}, AgreementAlg.ECDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA384));
        public static readonly KeyAgreementAlg ECDH_SHA512KDF = new KeyAgreementAlg(new int[]{1,3,132,1,11,3}, AgreementAlg.ECDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA512));

        public static readonly KeyAgreementAlg ECCDH_SHA1KDF = new KeyAgreementAlg(new int[]{1,3,133,16,840,63,0,3}, AgreementAlg.ECCDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA1));
        public static readonly KeyAgreementAlg ECCDH_SHA224KDF = new KeyAgreementAlg(new int[]{1,3,132,1,14,0}, AgreementAlg.ECCDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA224));
        public static readonly KeyAgreementAlg ECCDH_SHA256KDF = new KeyAgreementAlg(new int[]{1,3,132,1,14,1}, AgreementAlg.ECCDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA256));
        public static readonly KeyAgreementAlg ECCDH_SHA384KDF = new KeyAgreementAlg(new int[]{1,3,132,1,14,2}, AgreementAlg.ECCDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA384));
        public static readonly KeyAgreementAlg ECCDH_SHA512KDF = new KeyAgreementAlg(new int[] { 1, 3, 132, 1, 14, 3 }, AgreementAlg.ECCDH, new DerivationFunctionAlg(DerivationFunctionType.ECDHKEK, DigestAlg.SHA512));
    
        
        private readonly int[] mOID;
        private readonly AgreementAlg mAgreementAlg;
        private readonly DerivationFunctionAlg mDerivationFunctionAlg;

        public KeyAgreementAlg(int[] aOID, AgreementAlg aAgreementAlg, DerivationFunctionAlg aDerivationFunction)
        {
            mOID = aOID;
            mAgreementAlg = aAgreementAlg;
            mDerivationFunctionAlg = aDerivationFunction;
            if (aOID != null)
            {
                OID oid = new OID(aOID);                
                msOidRegistry[oid] = this;
            }
        }

        public int[] getOID()
        {
            return mOID;
        }

        public AgreementAlg getAgreementAlg()
        {
            return mAgreementAlg;
        }

        public DerivationFunctionAlg getDerivationFunctionAlg()
        {
            return mDerivationFunctionAlg;
        }

        public static KeyAgreementAlg fromOID(int[] aOID)
        {
            KeyAgreementAlg keyAgreementAlg = null;
            OID oid = new OID(aOID);
            msOidRegistry.TryGetValue(oid, out keyAgreementAlg);
            return keyAgreementAlg;
        }

        public String getName()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
