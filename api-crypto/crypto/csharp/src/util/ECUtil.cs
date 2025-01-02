using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.crypto.util
{
    public class ECUtil
    {
        /// <summary>
        /// Check if both the key length and the signature algorithm hash suit the requirements provided in RFC 5656, 6.2.1.
        /// </summary>
        /// <param name="signerCertificate">The certificate to be used for signing.</param>
        /// <param name="signatureAlg">The algorithm to be used in signing.</param>
        /// <exception cref="ESYAException">Exception explaining that key and digest lengths do not meet the minimum requirements.</exception>
        public static void checkDigestAlgForECCAlgorithm(ECertificate signerCertificate, SignatureAlg signatureAlg)
        {
            // imzalama algoritması ECC değilse zaten bakmaya gerek yok
            if (!signatureAlg.getAsymmetricAlg().Equals(AsymmetricAlg.ECDSA))
            {
                return;
            }

            // sertifikanın imzalandığı public key algoritması EC olmalı.
            SignatureAlg publicKeyAlg = SignatureAlg.fromAlgorithmIdentifier(signerCertificate.getPublicKeyAlgorithm()).getmObj1();
            if (!publicKeyAlg.getAsymmetricAlg().Equals(AsymmetricAlg.ECDSA))
            {
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
            if (pubKeyLength <= 256)
            {
                targetDigestAlg = DigestAlg.SHA256;
            }
            else if (pubKeyLength <= 384)
            {
                targetDigestAlg = DigestAlg.SHA384;
            }
            else
            {
                targetDigestAlg = DigestAlg.SHA512;
            }

            if (signatureAlg.getDigestAlg() != targetDigestAlg)
            {
                throw new ESYAException($"Signature digest algorithm ({signatureAlg.getDigestAlg().getName()}) and EC key size ({pubKeyLength}) do not match. You can use {targetDigestAlg.getName()} algorithm for digest.");
            }
        }

        public static SignatureAlg getConvenientECSignatureAlgForECCertificate(ECertificate signerCertificate)
        {
            {
                SignatureAlg publicKeyAlg = SignatureAlg.fromAlgorithmIdentifier(signerCertificate.getSignatureAlgorithm()).getmObj1();
                if (!publicKeyAlg.getAsymmetricAlg().Equals(AsymmetricAlg.ECDSA))
                {
                    throw new ESYAException("Non-ECDSA certificate algorithm: " + publicKeyAlg.getName());
                }
            }

            SignatureAlg ecSignatureAlg;
            int pubKeyLength = KeyUtil.getKeyLength(signerCertificate);

            if (pubKeyLength <= 256)
            {
                ecSignatureAlg = SignatureAlg.ECDSA_SHA256;
            }
            else if (pubKeyLength <= 384)
            {
                ecSignatureAlg = SignatureAlg.ECDSA_SHA384;
            }
            else
            {
                ecSignatureAlg = SignatureAlg.ECDSA_SHA512;
            }

            return ecSignatureAlg;
        }

        public static void checkKeyAndSigningAlgorithmConsistency(ECertificate signerCertificate, SignatureAlg signatureAlg)
        {
            SignatureAlg publicKeyAlg = SignatureAlg.fromAlgorithmIdentifier(signerCertificate.getPublicKeyAlgorithm()).getmObj1();
            if (!publicKeyAlg.getAsymmetricAlg().getName().Equals(signatureAlg.getAsymmetricAlg().getName())) {
                throw new ESYAException("The Public Key algorithm and the Signing algorithm are incompatible. Public Key algorithm: " + publicKeyAlg.getName() + " Signing algorithm: " + signatureAlg.getName());
            }
        }
    }
}
