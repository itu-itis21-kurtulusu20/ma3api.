using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.common.crypto
{
    /**
 * Algorithms class contains the String name values of signature algorithms, digest algorithms, and cipher algorithms. It also includes 
 * several utility methods. 
 */
    public static class Algorithms
    {
        public static readonly string ASYM_ALGO_RSA = "RSA";
        public static readonly string ASYM_ALGO_ECDSA = "ECDSA";
        public static readonly string ASYM_ALGO_EC = "EC";
        public static readonly string ASYM_ALGO_DSA = "DSA";
                               
        public static readonly string CIPHER_RSA_PKCS1 = "RSA/NONE/PKCS1";
        public static readonly string CIPHER_RSAOAEP = "RSA/NONE/OAEP";
                               
        public static readonly string SIGNATURE_RSA = "RSA-with-NONE";
        public static readonly string SIGNATURE_RSA_RAW = "RSA-RAW";
        public static readonly string SIGNATURE_RSA_MD5 = "RSA-with-MD5";
        public static readonly string SIGNATURE_RSA_SHA1 = "RSA-with-SHA1";
        public static readonly string SIGNATURE_RSA_SHA224 = "RSA-with-SHA224";
        public static readonly string SIGNATURE_RSA_SHA256 = "RSA-with-SHA256";
        public static readonly string SIGNATURE_RSA_SHA384 = "RSA-with-SHA384";
        public static readonly string SIGNATURE_RSA_SHA512 = "RSA-with-SHA512";
                               
        public static readonly string SIGNATURE_RSA_PSS = "RSAPSS";
        public static readonly string SIGNATURE_RSA_PSS_SHA1 = "RSA-PSS-with-SHA1";
        public static readonly string SIGNATURE_RSA_PSS_SHA224 = "RSA-PSS-with-SHA224";
        public static readonly string SIGNATURE_RSA_PSS_SHA256 = "RSA-PSS-with-SHA256";
        public static readonly string SIGNATURE_RSA_PSS_SHA384 = "RSA-PSS-with-SHA384";
        public static readonly string SIGNATURE_RSA_PSS_SHA512 = "RSA-PSS-with-SHA512";
                               
                               
        public static readonly string SIGNATURE_RSA_ISO9796_2_SHA1 = "RSA-ISO9796-2-with-SHA1";
        public static readonly string SIGNATURE_RSA_ISO9796_2_SHA224 = "RSA-ISO9796-2-with-SHA224";
        public static readonly string SIGNATURE_RSA_ISO9796_2_SHA256 = "RSA-ISO9796-2-with-SHA256";
        public static readonly string SIGNATURE_RSA_ISO9796_2_SHA384 = "RSA-ISO9796-2-with-SHA384";
        public static readonly string SIGNATURE_RSA_ISO9796_2_SHA512 = "RSA-ISO9796-2-with-SHA512";

        public static readonly string SIGNATURE_ECDSA = "ECDSA";
        public static readonly string SIGNATURE_ECDSA_SHA1 = "ECDSA-with-SHA1";
        public static readonly string SIGNATURE_ECDSA_SHA224 = "ECDSA-with-SHA224";
        public static readonly string SIGNATURE_ECDSA_SHA256 = "ECDSA-with-SHA256";
        public static readonly string SIGNATURE_ECDSA_SHA384 = "ECDSA-with-SHA384";
        public static readonly string SIGNATURE_ECDSA_SHA512 = "ECDSA-with-SHA512";
                               
        public static readonly string SIGNATURE_DSA = "DSA";
        public static readonly string SIGNATURE_DSA_SHA1 = "DSA-with-SHA1";
        public static readonly string SIGNATURE_DSA_SHA256 = "DSA-with-SHA256";

        public static string DIGEST_MD5 = "MD5";
        public static string DIGEST_SHA1 = "SHA1";
        public static string DIGEST_SHA224 = "SHA-224";
        public static string DIGEST_SHA256 = "SHA-256";
        public static string DIGEST_SHA384 = "SHA-384";
        public static string DIGEST_SHA512 = "SHA-512";
        public static string DIGEST_RIPEMD160 = "RIPEMD";

        private static readonly Dictionary<string, int> DIGEST_LENGTH_MAP = new Dictionary<string, int>();

        static Algorithms()
        {
            DIGEST_LENGTH_MAP[DIGEST_MD5] = 16;
            DIGEST_LENGTH_MAP[DIGEST_SHA1] = 20;
            DIGEST_LENGTH_MAP[DIGEST_SHA224] = 28;
            DIGEST_LENGTH_MAP[DIGEST_SHA256] = 32;
            DIGEST_LENGTH_MAP[DIGEST_SHA384] = 48;
            DIGEST_LENGTH_MAP[DIGEST_SHA512] = 64;
        }
        /**
         * Returns the name of digest algorithm for the given signature algorithm. 
         * @param aSignatureAlg Signature algorithm 
         * @return Digest algorithm
         * @throws ESYAException
         */
        public static string getDigestAlgOfSignatureAlg(string aSignatureAlg)
        {
            if (aSignatureAlg.Contains(DIGEST_MD5))
                return DIGEST_MD5;

            if (aSignatureAlg.Contains("SHA1"))
                return DIGEST_SHA1;

            if (aSignatureAlg.Contains("SHA224"))
                return DIGEST_SHA224;

            if (aSignatureAlg.Contains("SHA256"))
                return DIGEST_SHA256;

            if (aSignatureAlg.Contains("SHA384"))
                return DIGEST_SHA384;

            if (aSignatureAlg.Contains("SHA512"))
                return DIGEST_SHA512;

            if (aSignatureAlg.Contains("RIPEMD"))
                return DIGEST_RIPEMD160;
            if (aSignatureAlg.Contains("NONE"))
                return null;

            throw new ESYAException("UnKnown Algorithm: " + aSignatureAlg);
        }
        /**
         * Returns signature algorithm without digest algorithm. For example, if RSA-with-SHA1 is given as parameter, this method returns RSA. 
         * @param aSignatureAlg Name of the signature algorithm
         * @return signature algorithm without digest algorithm
         * @throws ESYAException If given signature algorithm is unknown for this class
         */
        public static string getAsymAlgOfSignatureAlg(string aSignatureAlg)
        {
            if (aSignatureAlg.Contains(ASYM_ALGO_RSA))
                return ASYM_ALGO_RSA;

            if (aSignatureAlg.Contains(ASYM_ALGO_ECDSA))
                return ASYM_ALGO_ECDSA;

            if (aSignatureAlg.Contains(ASYM_ALGO_DSA))
                return ASYM_ALGO_DSA;

            throw new ESYAException("UnKnown Algorithm: " + aSignatureAlg);
        }
        /**
         * Returns the length of the digest in bytes 
         * @param aDigestAlg Digest algorithm. 
         * @return The length of the digest in bytes. If the given digest algorithm is unknown for this class, 0 is returned.
         */
        public static int getLengthofDigestAlg(string aDigestAlg)
        {
            return DIGEST_LENGTH_MAP[aDigestAlg];
        }
    }
}