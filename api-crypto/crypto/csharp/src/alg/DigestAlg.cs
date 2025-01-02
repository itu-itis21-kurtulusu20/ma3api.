using System;
using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.x509;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    /**
 * Definition for Message Digest algorithm, such as MD5 or SHA. Message digests are secure one-way hash functions that
 * take arbitrary-sized data and output a fixed-length hash value. 
 */
    public class DigestAlg : IAlgorithm
    {

        private static readonly Dictionary<int[], DigestAlg> msOidRegistry = new Dictionary<int[], DigestAlg>();

        public static readonly DigestAlg SHA1 = new DigestAlg(_algorithmsValues.sha_1, "SHA1", 20); //todo bu SHA-1 yazılmıştı, smarcard dll'in crypto.dll'de bağımlılığını kaldırmak için SHA1 yapıldı
        public static readonly DigestAlg SHA224 = new DigestAlg(_algorithmsValues.id_sha224, "SHA-224", 28);
        public static readonly DigestAlg SHA256 = new DigestAlg(_algorithmsValues.id_sha256, "SHA-256", 32);
        public static readonly DigestAlg SHA384 = new DigestAlg(_algorithmsValues.id_sha384, "SHA-384", 48);
        public static readonly DigestAlg SHA512 = new DigestAlg(_algorithmsValues.id_sha512, "SHA-512", 64);
        public static readonly DigestAlg MD5 = new DigestAlg(_algorithmsValues.md5, "MD5", 16);
        public static readonly DigestAlg RIPEMD160 = new DigestAlg(_algorithmsValues.ripemd_160, "RIPEMD160", 20);

        private readonly int[] mOID;
        private readonly String mName;
        private readonly int mDigestLength;

        private DigestAlg(int[] aOid, String aName, int aDigestLength)
        {
            mOID = aOid;
            mName = aName;
            mDigestLength = aDigestLength;
            if (aOid != null)
            {                
                msOidRegistry[aOid]= this;
            }
        }
        /**
         * Returns OID of digest algorithm
         * @return 
         */
        public int[] getOID()
        {
            return mOID;
        }
        /**
         * Returns name of digest algorithm
         * @return 
         */
        public String getName()
        {
            return mName;
        }

        /**
         * Returns the length of the digest in bytes
         * @return the length of the digest in bytes
         */
        public int getDigestLength()
        {
            return mDigestLength;
        }


        public override string ToString()
        {
            return getName();
        }

        /**
         * Find  digest algorithm from AlgorithmIdentifier by comparing it with known ones and return
         * @return 
         */
        public static DigestAlg fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgorithmIdentifier)
        {
            foreach (DigestAlg alg in msOidRegistry.Values)
            {
                if (aAlgorithmIdentifier.getAlgorithm().mValue.SequenceEqual<int>(alg.getOID()))
                    return alg;
            }
            return null;
        }
        /**
         * Find  digest algorithm from OID by comparing it with known ones and return
         * @return 
         */
        public static DigestAlg fromOID(int[] aOID)
        {
            DigestAlg digestAlg = null;
            foreach (int[] key in msOidRegistry.Keys)
            {
                if (key.SequenceEqual<int>(aOID))
                {
                    msOidRegistry.TryGetValue(key, out digestAlg);
                    return digestAlg;
                }
            }
            return null;
        }
        /**
         * Find  digest algorithm from name by comparing it with known ones and return
         * @return 
         */
        public static DigestAlg fromName(String aAlgName)
        {
            foreach (KeyValuePair<int[], DigestAlg> keyValuePair in msOidRegistry)
            {
                if (keyValuePair.Value.getName().Equals(aAlgName, StringComparison.OrdinalIgnoreCase))
                    return keyValuePair.Value;
            }
            return null;
            //throw new CryptoRuntimeException(aAlgName + " isimli ozet algoritmasi tanimlanmamis");
        }


        public override bool Equals(object obj)
        {
            // If parameter is null return false.
            if (obj == null)
            {
                return false;
            }

            // If parameter cannot be cast to Point return false.
            DigestAlg p = obj as DigestAlg;
            if ((System.Object)p == null)
            {
                return false;
            }

            // Return true if the fields match:
            //return (x == p.x) && (y == p.y);
            return (mOID.SequenceEqual<int>(p.mOID) && mName.Equals(p.mName) && mDigestLength.Equals(p.mDigestLength));
        }

        public bool Equals(DigestAlg p)
        {
            // If parameter is null return false:
            if ((object)p == null)
            {
                return false;
            }
            // Return true if the fields match:
            return (mOID.SequenceEqual<int>(p.mOID) && mName.Equals(p.mName) && mDigestLength.Equals(p.mDigestLength));
        }

        public override int GetHashCode()
        {
            return mOID.GetHashCode() ^ mName.GetHashCode() ^ mDigestLength.GetHashCode();
            //return base.GetHashCode();
        }
        /**
         * Create an EAlgorithmIdentifier from digest algorithm and return
         * @return 
         */
        public EAlgorithmIdentifier toAlgorithmIdentifier()
        {
            return new EAlgorithmIdentifier(new AlgorithmIdentifier(mOID));
        }
    }
}
