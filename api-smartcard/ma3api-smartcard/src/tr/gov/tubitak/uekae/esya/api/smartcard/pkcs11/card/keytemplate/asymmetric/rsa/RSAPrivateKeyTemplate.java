package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.util.BigIntegerUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAKeyGenParameterSpec;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/11/12 - 8:04 PM <p>
 * <b>Description</b>: <br>
 * Herein our precious RSA Private Key Template, defining default RSAKeyTemplate attributes plus:
 * <pre>
 *     CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY)
 *     CK_ATTRIBUTE(CKA_PRIVATE, true)*
 * </pre>
 */
public class RSAPrivateKeyTemplate extends RSAKeyTemplate implements RSAPrivateKey {
    /**
     * RSA Private Key Generation constructor
     *
     * @param label
     * @param spec  keysize, parameters etc. (RSA_PSS support?)
     */
    public RSAPrivateKeyTemplate(String label, RSAKeyGenParameterSpec spec) {
        super(label, spec);
        add(new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY));
        add(new CK_ATTRIBUTE(CKA_PRIVATE, true));
    }

    /**
     * RSA Private Key import structure with Given RSAPrivateCrtKey
     * Calculating and Adding to RSAPrivateKeyTemplate:
     * <pre>
     *      CK_ATTRIBUTE(CKA_SENSITIVE, true)
     *      CK_ATTRIBUTE(CKA_MODULUS, modBytes)
     *      CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT, RSA_Public_Exponent)
     *      CK_ATTRIBUTE(CKA_PRIVATE_EXPONENT, RSA_Private_Exponent)
     *      CK_ATTRIBUTE(CKA_PRIME_1, RSA_Prime_1)
     *      CK_ATTRIBUTE(CKA_PRIME_2, RSA_Prime_2)
     *      CK_ATTRIBUTE(CKA_EXPONENT_1, RSA_Exponent_1)
     *      CK_ATTRIBUTE(CKA_EXPONENT_2, RSA_Exponent_2)
     *      CK_ATTRIBUTE(CKA_COEFFICIENT, RSA_Coefficient)
     *      CK_ATTRIBUTE(CKA_ID, id(SHA1 digest of Modulus))
     *      CK_ATTRIBUTE(PKCS11Constants.CKA_SUBJECT, subject) // optional if certificate is provided*
     *  </pre>
     *
     * @param label       key label
     * @param privKey     RSAPrivateCrtKey cannot be null
     * @param certificate X509Certificate to associate private key
     */
    public RSAPrivateKeyTemplate(String label, RSAPrivateCrtKey privKey, X509Certificate certificate) {
        this(label, new RSAKeyGenParameterSpec(privKey.getModulus().bitLength(), privKey.getPublicExponent()));

        encoded = privKey.getEncoded();

        byte[] modBytes = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getModulus());
        byte[] RSA_Public_Exponent = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getPublicExponent());
        byte[] RSA_Private_Exponent = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getPrivateExponent());
        byte[] RSA_Prime_1 = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getPrimeP()); //p
        byte[] RSA_Prime_2 = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getPrimeQ()); //q
        byte[] RSA_Exponent_1 = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getPrimeExponentP()); //dp
        byte[] RSA_Exponent_2 = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getPrimeExponentQ()); //dq
        byte[] RSA_Coefficient = BigIntegerUtil.toByteArrayWithoutSignByte(privKey.getCrtCoefficient());//qInv
        OZET_ALICI.update(modBytes);
        byte[] id = OZET_ALICI.digest();

        add(new CK_ATTRIBUTE(CKA_SENSITIVE, true));
        add(new CK_ATTRIBUTE(CKA_EXTRACTABLE, false));

        add(new CK_ATTRIBUTE(CKA_MODULUS, modBytes));
        add(new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT, RSA_Public_Exponent));
        add(new CK_ATTRIBUTE(CKA_PRIVATE_EXPONENT, RSA_Private_Exponent));
        add(new CK_ATTRIBUTE(CKA_PRIME_1, RSA_Prime_1));
        add(new CK_ATTRIBUTE(CKA_PRIME_2, RSA_Prime_2));
        add(new CK_ATTRIBUTE(CKA_EXPONENT_1, RSA_Exponent_1));
        add(new CK_ATTRIBUTE(CKA_EXPONENT_2, RSA_Exponent_2));
        add(new CK_ATTRIBUTE(CKA_COEFFICIENT, RSA_Coefficient));
        add(new CK_ATTRIBUTE(CKA_ID, id));
        if (certificate != null) {
            byte[] subject = certificate.getSubjectX500Principal().getEncoded();
            add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SUBJECT, subject));
        }
    }

    public RSAPrivateKeyTemplate getAsTokenTemplate(boolean sign, boolean decrypt, boolean isUnwrap) {
        add(new CK_ATTRIBUTE(CKA_TOKEN, true));
        add(new CK_ATTRIBUTE(CKA_SENSITIVE, true));
        add(new CK_ATTRIBUTE(CKA_EXTRACTABLE, false));
        add(new CK_ATTRIBUTE(CKA_DECRYPT, decrypt));
        add(new CK_ATTRIBUTE(CKA_SIGN, sign));
        add(new CK_ATTRIBUTE(CKA_SIGN_RECOVER, sign));
        add(new CK_ATTRIBUTE(CKA_UNWRAP, isUnwrap));
        return this;
    }

    /**
     * converts as Unwrapper RSA Private Key Template, adding:
     * <pre>
     *     CK_ATTRIBUTE(CKA_TOKEN, false)
     *     CK_ATTRIBUTE(CKA_UNWRAP, true)
     * </pre>
     *
     * @return
     */
    public RSAPrivateKeyTemplate getAsUnwrapperTemplate() {
        return (RSAPrivateKeyTemplate) this
                .add(new CK_ATTRIBUTE(CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE(CKA_UNWRAP, true));
    }

    public BigInteger getPrivateExponent() {
        return null;
    }

    @Override
    public byte[] getEncoded()
    {
        return encoded;
    }

    @Override
    public AsymmKeyTemplate.KeyType getKeyType() {
        return KeyType.PRIVATE;
    }
}
