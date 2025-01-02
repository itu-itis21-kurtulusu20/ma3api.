using System;
using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class AsymmetricAlg : IAlgorithm
    {
        private static readonly Dictionary<OID, AsymmetricAlg> msOidRegistry = new Dictionary<OID, AsymmetricAlg>();

        public static readonly AsymmetricAlg RSA = new AsymmetricAlg(_algorithmsValues.rsaEncryption, "RSA");
        //public static readonly AsymmetricAlg RSA_PSS = new AsymmetricAlg(_algorithmsValues.id_RSASSA_PSS, "RSA_PSS");
        //public static readonly AsymmetricAlg RSA_ISO9796d2 = new AsymmetricAlg(_algorithmsValues.id_RSASS_ISO9796d2, "RSA_ISO9796d2");
        public static readonly AsymmetricAlg DSA = new AsymmetricAlg(_algorithmsValues.id_dsa, "DSA");
        public static readonly AsymmetricAlg ECDSA = new AsymmetricAlg(_algorithmsValues.id_ecPublicKey, "ECDSA");

        private readonly string _mName;
        private readonly int[] _mOid;

        private AsymmetricAlg(int[] aOID, String aName)
        {
            _mName = aName;
            _mOid = aOID;
            if (aOID != null)
            {
                OID oid = new OID(aOID);                
                msOidRegistry[oid] = this;
            }
        }

        public String getName()
        {
            return _mName;
        }

        public int[] getOID()
        {
            return _mOid;
        }

        public static AsymmetricAlg fromOID(int[] aOID)
        {
            OID oid = new OID(aOID);
            AsymmetricAlg asymmetricAlg = null;
            msOidRegistry.TryGetValue(oid, out asymmetricAlg);
            return asymmetricAlg;

        }
        public static AsymmetricAlg fromName(String aAlgName)
        {
            foreach (KeyValuePair<OID, AsymmetricAlg> keyValue in msOidRegistry)
            {                    
                if (keyValue.Value.getName().Equals(aAlgName, StringComparison.OrdinalIgnoreCase))
                    return keyValue.Value;
            }
            throw new CryptoRuntimeException(aAlgName + " isimli asimetrik algoritma tanımlanmamış");
        }
        public override bool Equals(object obj)
        {
            AsymmetricAlg p = obj as AsymmetricAlg;
            if (p == null)
            {
                return false;
            }

            // Return true if the fields match:
            //return (x == p.x) && (y == p.y);
            return (_mOid.SequenceEqual<int>(p._mOid) && _mName.Equals(p._mName));
        }

        public bool Equals(AsymmetricAlg p)
        {
            // If parameter is null return false:
            if (p == null)
            {
                return false;
            }

            // Return true if the fields match:
            return (_mOid.SequenceEqual<int>(p._mOid) && _mName.Equals(p._mName));
        }
        public override int GetHashCode()
        {
            return _mName.GetHashCode() ^ _mOid.GetHashCode();
        }

    }
}
