package tr.gov.tubitak.uekae.esya.api.ca.passport.cvc;

import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECPointFp;
import tr.gov.tubitak.uekae.esya.api.asn.passport.EPassportCVCertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECSignatureTLVUtil;
import tr.gov.tubitak.uekae.esya.asn.passport.ElcPuK;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.spec.*;
import java.util.Arrays;

/**
 * Created by orcun.ertugrul on 20-Dec-17.
 */
public class PassportCVCUtil
{
    /**
     * Bizim EC eğrimiz sun.security paketi içinde tanımlı olduğundan sun.security sınıflarını
     * kullanabiliyoruz. Başka bir eğri kullanılmak istendiğinde geliştirme yapılması gerekebilir.
     */
    public static PublicKey getCAPublicKey(EPassportCVCertificate caCert) throws ESYAException
    {
        return getPublicKey(caCert, caCert);
    }

    /**
     *
     * @param caCert Eğri parametreli CA sertifikasında tanımlı. Eğri parametrelerini almak için CA certifikası gerekmektedir.
     * @param cert
     * @return
     * @throws ESYAException
     */
    public static PublicKey getPublicKey(EPassportCVCertificate caCert, EPassportCVCertificate cert) throws ESYAException
    {
        try
        {
            ElcPuK caPublicKeyAsn = caCert.getPassportCertificateBody().getObject().puk;
            BigInteger p = new BigInteger(1, caPublicKeyAsn.p.value);
            ECField field = new ECFieldFp(p);
            BigInteger a = new BigInteger(1, caPublicKeyAsn.a.value);
            BigInteger b = new BigInteger(1, caPublicKeyAsn.b.value);
            EllipticCurve curve = new EllipticCurve(field, a, b);

            ECPoint pointG = getECPoint(caPublicKeyAsn.g.value);
            BigInteger n = new BigInteger(1, caPublicKeyAsn.r.value);
            int h = (int) caPublicKeyAsn.f.value;

            ECParameterSpec parameterSpec = new ECParameterSpec(curve, pointG, n, h);

            ElcPuK certPublicKeyAsn = cert.getPassportCertificateBody().getObject().puk;
            ECPoint publicPoint = getECPoint(certPublicKeyAsn.y.value);

            ECDomainParameter ecDomainParameter = ECDomainParameter.getInstance(parameterSpec);
            ECGNUPoint point = new ECPointFp(ecDomainParameter.getMCurve(), publicPoint.getAffineX(), publicPoint.getAffineY());

            ECDSAPublicKey publicKey = new ECDSAPublicKey(ecDomainParameter, point);

            return publicKey;
        }
        catch (Exception ex)
        {
            throw new ESYAException(ex);
        }
    }

    public static void checkSignature(EPassportCVCertificate aCertificate, PublicKey aSignerPublicKey) throws  ESYAException
    {
        SignatureAlg signatureAlg = SignatureAlg.ECDSA_SHA512;

        byte []certBody = aCertificate.getPassportCertificateBody().getEncoded();
        byte []signature = aCertificate.getObject().signature.value;

        signature = ECSignatureTLVUtil.addTLVToSignature(signature);

        boolean result = SignUtil.verify(signatureAlg, certBody, signature, aSignerPublicKey);

        if(result == false)
            throw new ESYAException(aCertificate.toString() + " certificate signature could not be verified!");
    }
    

    public static ECPoint getECPoint(byte [] xAndy) throws ESYAException
    {
        if(xAndy[0] != 0x04)
            throw new ESYAException("Only uncompressed EC Point is supported!....");

        int axisLen = (xAndy.length - 1) / 2;

        byte [] xBytes = Arrays.copyOfRange(xAndy, 1, 1 + axisLen);
        byte [] yBytes = Arrays.copyOfRange(xAndy, 1 + axisLen, 1 + axisLen + axisLen);

        return new ECPoint(new BigInteger(1, xBytes), new BigInteger(1, yBytes));
    }
}
