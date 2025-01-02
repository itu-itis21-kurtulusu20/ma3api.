package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.algorithm;

import sun.security.pkcs11.wrapper.PKCS11Constants;

public class PKCS11AlgorithmUtil {
    public static boolean isECMechanism(long value) {
        return (
               value == PKCS11Constants.CKM_ECDSA
            || value == PKCS11Constants.CKM_ECDSA_SHA1
            || value == PKCS11Constants.CKM_ECDSA_SHA224
            || value == PKCS11Constants.CKM_ECDSA_SHA256
            || value == PKCS11Constants.CKM_ECDSA_SHA384
            || value == PKCS11Constants.CKM_ECDSA_SHA512
        );
    }
}
