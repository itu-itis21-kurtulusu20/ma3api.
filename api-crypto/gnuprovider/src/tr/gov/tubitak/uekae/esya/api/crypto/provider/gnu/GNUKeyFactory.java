package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.key.dss.DSSKeyPairX509Codec;
import gnu.crypto.key.ecdsa.ECDSAKeyPairX509Codec;
import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.key.rsa.GnuRSAPrivateKey;
import gnu.crypto.key.rsa.GnuRSAPublicKey;
import gnu.crypto.key.rsa.RSAKeyPairX509Codec;
import gnu.crypto.sig.ecdsa.ecmath.curve.CurveF2m;
import gnu.crypto.sig.ecdsa.ecmath.curve.CurveFp;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECPointF2mPolynomial;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECPointFp;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldF2mPolynomial;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldFp;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyFactory;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;
import tr.gov.tubitak.uekae.esya.api.crypto.params.KeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.PBEKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECField;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author ayetgin
 */
public class GNUKeyFactory implements KeyFactory
{
    public PublicKey decodePublicKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes) throws CryptoException
    {
        switch (aAsymmetricAlg){
            case RSA    :
                return new RSAKeyPairX509Codec().decodePublicKey(aBytes);
            case DSA    :
                return new DSSKeyPairX509Codec().decodePublicKey(aBytes);
            case ECDSA  :
                return new ECDSAKeyPairX509Codec().decodePublicKey(aBytes);
        }
        throw new UnknownElement(GenelDil.mesaj(GenelDil.IMZALG_0_BILINMIYOR, new String[]{aAsymmetricAlg.name()})); 
    }

    public PrivateKey decodePrivateKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes) throws CryptoException
    {
        switch (aAsymmetricAlg){
            case RSA    :
                return new RSAKeyPairX509Codec().decodePrivateKey(aBytes);
            case DSA    :
                return new DSSKeyPairX509Codec().decodePrivateKey(aBytes);
            case ECDSA  :
                return new ECDSAKeyPairX509Codec().decodePrivateKey(aBytes);
        }
        throw new UnknownElement(GenelDil.mesaj(GenelDil.IMZALG_0_BILINMIYOR, new String[]{aAsymmetricAlg.name()}));

    }

    public SecretKey generateSecretKey(KeySpec aKeySpec) throws CryptoException
    {
        if (aKeySpec instanceof PBEKeySpec)
            return new PBEKeyGen((PBEKeySpec)aKeySpec).generateKey();
        throw new UnknownElement("Unknown key spec "+aKeySpec);
    }

    public SecretKey generateSecretKey(CipherAlg alg,int keyLength) throws CryptoException {
        if(alg == null || ! alg.getName().startsWith("AES") )
            throw new CryptoException("Only AES is supported:"+alg);
        return new SecretKeySpec(RandomUtil.generateRandom(keyLength / 8),"AES");
    }

    public byte[] generateKey(CipherAlg aAlg, int aBitLength)
    {
        return RandomUtil.generateRandom(aBitLength / 8);
    }

    public PublicKey generatePublicKey(java.security.spec.KeySpec aKeySpec) throws CryptoException
    {
	if(aKeySpec instanceof RSAPublicKeySpec)
	{
	    RSAPublicKeySpec pubspec = (RSAPublicKeySpec)aKeySpec;
	    return new GnuRSAPublicKey(pubspec.getModulus(),pubspec.getPublicExponent());
	}
	else if(aKeySpec instanceof ECPublicKeySpec)
	{
	    ECPublicKeySpec pubspec = (ECPublicKeySpec) aKeySpec;

	    EllipticCurve curve = pubspec.getParams().getCurve();
	    ECField field = curve.getField();
	    ECPoint point = pubspec.getW();

	    ECDomainParameter params = ECDomainParameter.getInstance(pubspec.getParams());
	    ECGNUPoint gpoint = null;
	    try
	    {
		if(field instanceof ECFieldF2m)
		{
		    ECFieldF2m jfield = (ECFieldF2m) field;
		    FieldF2mPolynomial gfield = FieldF2mPolynomial.getInstance(jfield.getM(), jfield.getReductionPolynomial());
		    CurveF2m gcurve = new CurveF2m(gfield, curve.getA(), curve.getB());
		    gpoint = new ECPointF2mPolynomial(gcurve, point.getAffineX(), point.getAffineY());
		}
		else
		{
		    ECFieldFp jfield = (ECFieldFp) field;
		    FieldFp gfield = FieldFp.getInstance(jfield.getP());
		    CurveFp gcurve = new CurveFp(gfield, curve.getA(), curve.getB());
		    gpoint = new ECPointFp(gcurve, point.getAffineX(), point.getAffineY());
		}
	    }
	    catch(Exception aEx)
	    {
		throw new CryptoException("Error in key genarating", aEx);
	    }

	    ECDSAPublicKey pubkey = new ECDSAPublicKey(params, gpoint);
	    return pubkey;
	}
	else
	    throw new UnknownElement("Unknown key spec "+aKeySpec);

    }

    public PrivateKey generatePrivateKey(java.security.spec.KeySpec aKeySpec) throws CryptoException
    {
	if(aKeySpec instanceof RSAPrivateCrtKeySpec)
	{
	   RSAPrivateCrtKeySpec prispec = (RSAPrivateCrtKeySpec) aKeySpec;
	   return new GnuRSAPrivateKey(prispec.getPrimeExponentP(), prispec.getPrimeExponentQ(), prispec.getPublicExponent(), prispec.getPrivateExponent());
	}
	else if(aKeySpec instanceof ECPrivateKeySpec)
	{
	    ECPrivateKeySpec prispec = (ECPrivateKeySpec) aKeySpec;
	    
	    ECParameterSpec pspec = prispec.getParams();
	    
	    ECDomainParameter params = ECDomainParameter.getInstance(pspec);
	    
	    return new ECDSAPrivateKey(params, prispec.getS());
	}
	else
	    throw new UnknownElement("Unknown key spec "+aKeySpec);
	}


}
