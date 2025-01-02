using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class ConstantsUtil
    {
        public static readonly long CKM_SHA224 = 597L;

        public static readonly long CKG_MGF1_SHA256 = 2L;
        public static readonly long CKG_MGF1_SHA384 = 3L;
        public static readonly long CKG_MGF1_SHA512 = 4L;

        public static long convertHashAlgToPKCS11Constant(String aDigestAlg)
        {

            if (aDigestAlg.Equals(Algorithms.DIGEST_MD5))
            {
                return PKCS11Constants_Fields.CKM_MD5;
            }
            else if (aDigestAlg.Equals(Algorithms.DIGEST_RIPEMD160))
            {
                return PKCS11Constants_Fields.CKM_RIPEMD160;
            }
            else if (aDigestAlg.Equals(Algorithms.DIGEST_SHA1))
            {
                return PKCS11Constants_Fields.CKM_SHA_1;
            }
            else if (aDigestAlg.Equals(Algorithms.DIGEST_SHA224))
            {
                return CKM_SHA224;
            }
            else if (aDigestAlg.Equals(Algorithms.DIGEST_SHA256))
            {
                return PKCS11Constants_Fields.CKM_SHA256;
            }
            else if (aDigestAlg.Equals(Algorithms.DIGEST_SHA384))
            {
                return PKCS11Constants_Fields.CKM_SHA384;
            }
            else if (aDigestAlg.Equals(Algorithms.DIGEST_SHA512))
            {
                return PKCS11Constants_Fields.CKM_SHA512;
            }

            throw new ESYAException("Unknown digest algorithm");

        }


        public static long getMGFAlgorithm(long aHashAlgorithm)
        {
            if (aHashAlgorithm == PKCS11Constants_Fields.CKM_SHA_1)
                return PKCS11Constants_Fields.CKG_MGF1_SHA1;
            else if (aHashAlgorithm == PKCS11Constants_Fields.CKM_SHA256)
                return CKG_MGF1_SHA256;
            else if (aHashAlgorithm == PKCS11Constants_Fields.CKM_SHA384)
                return CKG_MGF1_SHA384;
            else if (aHashAlgorithm == PKCS11Constants_Fields.CKM_SHA512)
                return CKG_MGF1_SHA512;


            throw new ESYAException("Unknown MGF algorithm");
        }

        public static long convertSymmetricAlgToPKCS11Constant(CipherAlg aCipherAlg)
        {
            if (aCipherAlg.Equals(CipherAlg.AES128_CBC) || aCipherAlg.Equals(CipherAlg.AES192_CBC) || aCipherAlg.Equals(CipherAlg.AES256_CBC))
            {
                return PKCS11Constants_Fields.CKM_AES_CBC_PAD;
            }

            if (aCipherAlg.Equals(CipherAlg.AES128_ECB) || aCipherAlg.Equals(CipherAlg.AES192_ECB) ||
                aCipherAlg.Equals(CipherAlg.AES256_ECB))
            {
                return PKCS11Constants_Fields.CKM_AES_ECB;
            }

            throw new ESYAException("Unknown symmetric algorithm");
        }
    }
}
