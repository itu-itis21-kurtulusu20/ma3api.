package tr.gov.tubitak.uekae.esya.api.crypto.util;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.math.BigInteger;
import java.text.MessageFormat;

public class ECAlgorithmUtil {

    /**
     * Check if both the key length and the signature algorithm hash suit the requirements provided in RFC 5656, 6.2.1.
     *
     * @param signerCertificate The certificate to be used for signing.
     * @param signatureAlg      The algorithm to be used in signing.
     * @throws ESYAException Exception explaining that key and digest lengths do not meet the minimum requirements.
     */
    public static void checkDigestAlgForECCAlgorithm(ECertificate signerCertificate, SignatureAlg signatureAlg) throws ESYAException {

        // imzalama algoritması ECC değilse zaten bakmaya gerek yok
        if (!signatureAlg.getAsymmetricAlg().equals(AsymmetricAlg.ECDSA)) {
            return;
        }

        // sertifikanın imzalandığı public key algoritması EC olmalı.
        SignatureAlg publicKeyAlg = SignatureAlg.fromAlgorithmIdentifier(signerCertificate.getPublicKeyAlgorithm()).getObject1();
        if (!publicKeyAlg.getAsymmetricAlg().equals(AsymmetricAlg.ECDSA)) {
            throw new ESYAException("Non-ECDSA certificate algorithm, ECDSA signature algorithm: " + publicKeyAlg.getName() + " vs. " + signatureAlg.getName());
        }

        // bu noktada iki algoritmanın da ECC olduğundan eminiz.
        // geriye anahtar ve özet uzunluklarının karşılaştırılması kalıyor:

        int pubKeyLength = KeyUtil.getKeyLength(signerCertificate);

        /*
        (RFC 5656, 6.2.1)
        +----------------+----------------+
        |   Curve Size   | Hash Algorithm |
        +----------------+----------------+
        |    b <= 256    |    SHA-256     |
        |                |                |
        | 256 < b <= 384 |    SHA-384     |
        |                |                |
        |    384 < b     |    SHA-512     |
        +----------------+----------------+
        */

        DigestAlg targetDigestAlg;
        if (pubKeyLength <= 256) {
            targetDigestAlg = DigestAlg.SHA256;
        } else if (pubKeyLength <= 384) {
            targetDigestAlg = DigestAlg.SHA384;
        } else {
            targetDigestAlg = DigestAlg.SHA512;
        }

        if (signatureAlg.getDigestAlg() != targetDigestAlg) {
            throw new ESYAException(MessageFormat.format("Signature digest algorithm ({0}) and EC key size ({1}) do not match. You can use {2} algorithm for digest.", signatureAlg.getDigestAlg().getName(), pubKeyLength, targetDigestAlg.getName()));
        }
    }

    public static SignatureAlg getConvenientECSignatureAlgForECCertificate(ECertificate signerCertificate) throws ESYAException {

        {
            SignatureAlg publicKeyAlg = SignatureAlg.fromAlgorithmIdentifier(signerCertificate.getPublicKeyAlgorithm()).getObject1();
            if (!publicKeyAlg.getAsymmetricAlg().equals(AsymmetricAlg.ECDSA)) {
                throw new ESYAException("Non-ECDSA certificate algorithm: " + publicKeyAlg.getName());
            }
        }

        SignatureAlg ecSignatureAlg;
        int pubKeyLength = KeyUtil.getKeyLength(signerCertificate);
        if (pubKeyLength <= 256) {
            ecSignatureAlg = SignatureAlg.ECDSA_SHA256;
        } else if (pubKeyLength <= 384) {
            ecSignatureAlg = SignatureAlg.ECDSA_SHA384;
        } else {
            ecSignatureAlg = SignatureAlg.ECDSA_SHA512;
        }

        return ecSignatureAlg;
    }

    public static void checkKeyAndSigningAlgorithmConsistency(ECertificate signerCertificate, SignatureAlg signatureAlg) throws ESYAException {
        SignatureAlg publicKeyAlg = SignatureAlg.fromAlgorithmIdentifier(signerCertificate.getPublicKeyAlgorithm()).getObject1();
        if (!publicKeyAlg.getAsymmetricAlg().getName().equals(signatureAlg.getAsymmetricAlg().getName())) {
            throw new ESYAException("The Public Key algorithm and the Signing algorithm are incompatible. Public Key algorithm: " + publicKeyAlg.getName() + " Signing algorithm: " + signatureAlg.getName());
        }
    }
}
