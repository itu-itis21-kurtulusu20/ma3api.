package tr.gov.tubitak.uekae.esya.api.common.util.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ec.ECPointUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.BigIntegerUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECPoint;

public class CertificateUtil {

    private static final Logger logger = LoggerFactory.getLogger(CertificateUtil.class);

    public static byte[] calculateId(X509Certificate certificate){

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Warning in ECertificate", e);
            throw new ESYARuntimeException(e);
        }

        PublicKey publicKey = certificate.getPublicKey();
        byte[] id;
        if(publicKey.getAlgorithm().contains(Algorithms.ASYM_ALGO_RSA))
        {
            byte[] modulus = BigIntegerUtil.toByteArrayWithoutSignByte(((RSAPublicKey)publicKey).getModulus());
            messageDigest.update(modulus);
            id = messageDigest.digest();
        }
        else if(publicKey.getAlgorithm().contains(Algorithms.ASYM_ALGO_EC)||
                (publicKey.getAlgorithm().contains(Algorithms.ASYM_ALGO_ECDSA))){
            ECPublicKey ecPublicKey = (ECPublicKey) certificate.getPublicKey();
            ECPoint ecPoint = (ECPoint)ecPublicKey.getW();
            byte[] pointValue = ECPointUtil.encodePoint(ecPoint, ecPublicKey.getParams().getCurve());
            id = messageDigest.digest(pointValue);
        }
        else
        {
            messageDigest.update(publicKey.getEncoded());
            id = messageDigest.digest();
        }
        return id;
    }
}
