package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import sun.security.pkcs11.wrapper.CK_GCM_PARAMS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ConstantsExtended;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.io.IOException;

public class ConstantsUtil {
    public static long convertHashAlgToPKCS11Constant(String aDigestAlg) throws SmartCardException {
        if (aDigestAlg.equals(Algorithms.DIGEST_MD5)) {
            return PKCS11Constants.CKM_MD5;
        } else if (aDigestAlg.equals(Algorithms.DIGEST_RIPEMD160)) {
            return PKCS11Constants.CKM_RIPEMD160;
        } else if (aDigestAlg.equals(Algorithms.DIGEST_SHA1)) {
            return PKCS11Constants.CKM_SHA_1;
        } else if (aDigestAlg.equals(Algorithms.DIGEST_SHA224)) {
            return PKCS11ConstantsExtended.CKM_SHA224;
        } else if (aDigestAlg.equals(Algorithms.DIGEST_SHA256)) {
            return PKCS11Constants.CKM_SHA256;
        } else if (aDigestAlg.equals(Algorithms.DIGEST_SHA384)) {
            return PKCS11Constants.CKM_SHA384;
        } else if (aDigestAlg.equals(Algorithms.DIGEST_SHA512)) {
            return PKCS11Constants.CKM_SHA512;
        }

        throw new SmartCardException("Unknown digest algorithm: " + aDigestAlg);
    }

    public static long convertSymmetricAlgToPKCS11Constant(String aCipherAlg) throws ESYAException {
        if (aCipherAlg.equals(Algorithms.CIPHER_AES128_CBC)
                || aCipherAlg.equals(Algorithms.CIPHER_AES192_CBC)
                || aCipherAlg.equals(Algorithms.CIPHER_AES256_CBC)) {
            return PKCS11Constants.CKM_AES_CBC_PAD;
        } else if (aCipherAlg.equals(Algorithms.CIPHER_AES128_CBC_NOPADDING)
                || aCipherAlg.equals(Algorithms.CIPHER_AES192_CBC_NOPADDING)
                || aCipherAlg.equals(Algorithms.CIPHER_AES256_CBC_NOPADDING)) {
            return PKCS11Constants.CKM_AES_CBC;
        } else if (aCipherAlg.equals(Algorithms.CIPHER_AES128_ECB)
                || aCipherAlg.equals(Algorithms.CIPHER_AES192_ECB)
                || aCipherAlg.equals(Algorithms.CIPHER_AES256_ECB)
                || aCipherAlg.equals(Algorithms.CIPHER_AES128_ECB_NOPADDING)
                || aCipherAlg.equals(Algorithms.CIPHER_AES192_ECB_NOPADDING)
                || aCipherAlg.equals(Algorithms.CIPHER_AES256_ECB_NOPADDING)) { /* note that there is no "ECB without padding" defined in PKCS#11- please do handling accordingly */
            return PKCS11Constants.CKM_AES_ECB;
        } else if (aCipherAlg.equals(CipherAlg.DES_CBC.getName())) {
            return PKCS11Constants.CKM_DES_CBC_PAD;
        } else if (aCipherAlg.equals(CipherAlg.DES_CBC_NOPADDING.getName())) {
            return PKCS11Constants.CKM_DES_CBC;
        } else if (aCipherAlg.equals(CipherAlg.DES_ECB.getName())
                || aCipherAlg.equals(CipherAlg.DES_ECB_NOPADDING.getName())) { /* note that there is no "ECB without padding" defined in PKCS#11- please do handling accordingly */
            return PKCS11Constants.CKM_DES_ECB;
        } else if (aCipherAlg.equals(CipherAlg.DES_EDE3_CBC.getName())) {
            return PKCS11Constants.CKM_DES3_CBC_PAD;
        } else if (aCipherAlg.equals(CipherAlg.DES_EDE3_CBC_NOPADDING.getName())) {
            return PKCS11Constants.CKM_DES3_CBC;
        } else if (aCipherAlg.equals(CipherAlg.DES_EDE3_ECB.getName())
                || aCipherAlg.equals(CipherAlg.DES_EDE3_ECB_NOPADDING.getName())) { /* note that there is no "ECB without padding" defined in PKCS#11- please do handling accordingly */
            return PKCS11Constants.CKM_DES3_ECB;
        } else if (aCipherAlg.equals(CipherAlg.AES128_GCM.getName()) || aCipherAlg.equals(CipherAlg.AES192_GCM.getName()) || aCipherAlg.equals(CipherAlg.AES256_GCM.getName())) {
            return PKCS11Constants.CKM_AES_GCM;
        }


		throw new ESYAException("Unknown symmetric algorithm");
	}

    public static long getKeyDerivationPKCS11Constant(DigestAlg digestAlg) throws CryptoException {
        if(digestAlg == null) {
            return PKCS11Constants.CKD_NULL;
        } else if (digestAlg.equals(DigestAlg.SHA1)) {
            return PKCS11Constants.CKD_SHA1_KDF;
        } else if(digestAlg.equals(DigestAlg.SHA224)) {
            return PKCS11ConstantsExtended.CKD_SHA224_KDF;
        } else if(digestAlg.equals(DigestAlg.SHA256)) {
            return PKCS11ConstantsExtended.CKD_SHA256_KDF;
        } else if(digestAlg.equals(DigestAlg.SHA384)) {
            return PKCS11ConstantsExtended.CKD_SHA384_KDF;
        } else if(digestAlg.equals(DigestAlg.SHA512)) {
            return PKCS11ConstantsExtended.CKD_SHA512_KDF;
        }

        throw new CryptoException("Unknown KDF algorithm");
    }

    public static long convertWrapAlgToPKCS11Constant(String wrapAlg) throws ESYAException {
        if (wrapAlg.equals(WrapAlg.RSA_PKCS1.getName()) || wrapAlg.equals(WrapAlg.RSA_ECB_PKCS1.getName())) {
            return PKCS11Constants.CKM_RSA_PKCS;
        } else if (wrapAlg.equals(WrapAlg.AES128.getName()) || wrapAlg.equals(WrapAlg.AES192.getName()) || wrapAlg.equals(WrapAlg.AES256.getName())) {
            return PKCS11Constants.CKM_AES_KEY_WRAP;
        } else if (wrapAlg.equals(WrapAlg.AES128_CBC.getName()) || wrapAlg.equals(WrapAlg.AES192_CBC.getName()) || wrapAlg.equals(WrapAlg.AES256_CBC.getName())) {
            return PKCS11Constants.CKM_AES_CBC_PAD;
            //return PKCS11Constants.CKM_AES_CBC;
        }

        throw new ESYAException("Unknown Wrap Alg: " + wrapAlg);
    }

    public static CK_MECHANISM addParamsToMech(long mech, AlgorithmParams algorithmParams) throws ESYAException {
        CK_MECHANISM mechanism = new CK_MECHANISM(mech);
        mechanism.pParameter = convertAlgorithmParamsToPKCS11Params(algorithmParams);
        return mechanism;
    }

    public static Object convertAlgorithmParamsToPKCS11Params(AlgorithmParams algorithmParams) throws ESYAException {
        if (algorithmParams instanceof ParamsWithGCMSpec) {
            ParamsWithGCMSpec paramsWithGCMSpec = (ParamsWithGCMSpec) algorithmParams;

            byte[] iv = paramsWithGCMSpec.getIV();
            // todo: tagLen nasıl olmalı?
            int tagLen = 96;
            byte[] aad = null;
            if (paramsWithGCMSpec.getAAD() != null) {
                try {
                    aad = StreamUtil.readAll(paramsWithGCMSpec.getAAD());
                } catch (IOException e) {
                    throw new ESYAException(e);
                }
            }

            CK_GCM_PARAMS gcmParams = new CK_GCM_PARAMS(tagLen, iv, aad);
            return gcmParams;
        } else if (algorithmParams instanceof ParamsWithIV) {
            ParamsWithIV paramsWithIV = (ParamsWithIV) algorithmParams;
            return paramsWithIV.getIV();
        } else if (algorithmParams == null) {
            return null;
        }

        throw new ESYAException("Unknown algorithm params");
    }

    public static long getMGFAlgorithm(long aHashAlgorithm) throws SmartCardException {
        if (aHashAlgorithm == PKCS11Constants.CKM_SHA_1)
            return PKCS11Constants.CKG_MGF1_SHA1;
        else if (aHashAlgorithm == PKCS11ConstantsExtended.CKM_SHA224)
            return PKCS11ConstantsExtended.CKG_MGF1_SHA224;
        else if (aHashAlgorithm == PKCS11Constants.CKM_SHA256)
            return PKCS11ConstantsExtended.CKG_MGF1_SHA256;
        else if (aHashAlgorithm == PKCS11Constants.CKM_SHA384)
            return PKCS11ConstantsExtended.CKG_MGF1_SHA384;
        else if (aHashAlgorithm == PKCS11Constants.CKM_SHA512)
            return PKCS11ConstantsExtended.CKG_MGF1_SHA512;

        throw new SmartCardException("Unknown MGF algorithm");
    }
}
