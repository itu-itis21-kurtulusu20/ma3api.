package tr.gov.tubitak.uekae.esya.api.crypto.util;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.Verifier;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ayetgin
 */

public class SignUtil
{

    public static byte[] sign(SignatureAlg aSignatureAlg, AlgorithmParams aParams, byte[] aToBeSigned, PrivateKey aSignPrivateKey)
            throws CryptoException
    {
        Signer signer = Crypto.getSigner(aSignatureAlg);
        signer.init(aSignPrivateKey, aParams);
        //signer.update(aToBeSigned);
        return signer.sign(aToBeSigned);
    }

    public static byte[] sign(SignatureAlg aSignatureAlg, byte[] aToBeSigned, PrivateKey aSignPrivateKey)
            throws CryptoException
    {
        return sign(aSignatureAlg, null, aToBeSigned, aSignPrivateKey);
    }


    public static boolean verify(SignatureAlg aSignatureAlg, AlgorithmParams aParams, byte[] aToBeSigned, byte[] aSigned, PublicKey aSignPublicKey)
            throws CryptoException
    {
        Verifier verifier = Crypto.getVerifier(aSignatureAlg);
        verifier.init(aSignPublicKey, aParams);
        verifier.update(aToBeSigned);
        return verifier.verifySignature(aSigned);
    }

    public static boolean verify(SignatureAlg aSignatureAlg, byte[] aToBeSigned, byte[] aSigned, PublicKey aSignPublicKey)
            throws CryptoException
    {
        return verify(aSignatureAlg, null, aToBeSigned, aSigned, aSignPublicKey);
    }

    public static boolean verify(SignatureAlg aSignatureAlg, byte[] aImzalanan, byte[] aImzali, ECertificate aCertificate)
            throws CryptoException
    {
        return verify(aSignatureAlg, null, aImzalanan, aImzali, KeyUtil.decodePublicKey(aCertificate.getSubjectPublicKeyInfo()));
    }

    public static boolean verify(SignatureAlg aSignatureAlg, AlgorithmParams aParams, byte[] aImzalanan, byte[] aImzali, ECertificate aCertificate)
            throws CryptoException
    {
        return verify(aSignatureAlg, aParams, aImzalanan, aImzali, KeyUtil.decodePublicKey(aCertificate.getSubjectPublicKeyInfo()));
    }

}
