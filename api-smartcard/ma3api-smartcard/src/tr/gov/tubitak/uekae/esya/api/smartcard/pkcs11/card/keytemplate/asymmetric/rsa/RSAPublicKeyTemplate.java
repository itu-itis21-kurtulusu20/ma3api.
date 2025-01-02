package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import tr.gov.tubitak.uekae.esya.api.common.util.BigIntegerUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/11/12 - 8:27 PM <p>
 * <b>Description</b>: <br>
 *  RSAPublicKeyTemplate with RSAKeyTemplate plus:
 *  <pre>
 *      CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY)
 *      CK_ATTRIBUTE(CKA_PRIVATE, false)
 *      CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT, CardTemplate.toByteArray(publicExponent))
 *      CK_ATTRIBUTE(CKA_MODULUS_BITS, spec.getKeysize()
 *  </pre>
 */
public class RSAPublicKeyTemplate extends RSAKeyTemplate implements RSAPublicKey {

    private BigInteger publicExponent;

    /**
     * Key Template with default RSA Public Key Parameters
     * @param label
     * @param spec
     */
    public RSAPublicKeyTemplate(String label, RSAKeyGenParameterSpec spec) {
        super(label, spec);

        publicExponent = spec.getPublicExponent();
        if(publicExponent==null)
            publicExponent = RSAKeyGenParameterSpec.F4;
        add(new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY));
        add(new CK_ATTRIBUTE(CKA_PRIVATE, false));
        add(new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT, BigIntegerUtil.toByteArrayWithoutSignByte(publicExponent)));
        add(new CK_ATTRIBUTE(CKA_MODULUS_BITS, spec.getKeysize()));
    }

    /**
     * Key Template with RSA Public Key To import, default RSAPublicKeyTemplate plus:
     * <pre>
     *      CK_ATTRIBUTE(CKA_MODULUS, modulusBytes)
     *      CK_ATTRIBUTE(CKA_ID, id(SHA1 digest of Modulus))
     * </pre>
     * @param label
     * @param publicKey
     */
    public RSAPublicKeyTemplate(String label, RSAPublicKey publicKey) {
        super(label,new RSAKeyGenParameterSpec(publicKey.getModulus().bitLength(),publicKey.getPublicExponent()));
        modulus = publicKey.getModulus();
        publicExponent = spec.getPublicExponent();
        if(publicExponent == null)
            publicExponent = RSAKeyGenParameterSpec.F4;
        add(new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY));
        add(new CK_ATTRIBUTE(CKA_PRIVATE, false));
        add(new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT, BigIntegerUtil.toByteArrayWithoutSignByte(publicExponent)));

        byte[] modulusBytes = BigIntegerUtil.toByteArrayWithoutSignByte(modulus);

        OZET_ALICI.update(modulusBytes);
        byte[] id = OZET_ALICI.digest();

        add(new CK_ATTRIBUTE(CKA_MODULUS, modulusBytes));
        add(new CK_ATTRIBUTE(CKA_ID, id));
    }

    public BigInteger getPublicExponent() {
        return getSpec().getPublicExponent();
    }


    public RSAPublicKeyTemplate getAsTokenTemplate(boolean verify, boolean encrypt,boolean isWrap){
        add(new CK_ATTRIBUTE(CKA_TOKEN, true));
        add(new CK_ATTRIBUTE(CKA_ENCRYPT, encrypt));
        add(new CK_ATTRIBUTE(CKA_VERIFY, verify));
        add(new CK_ATTRIBUTE(CKA_VERIFY_RECOVER, verify));
        add(new CK_ATTRIBUTE(CKA_WRAP,isWrap));
        return this;
    }

    /**
     *  convert as Wrapper Public Key Template, adding:
     *  <pre>
     *      CK_ATTRIBUTE(CKA_TOKEN, false)
     *      CK_ATTRIBUTE(CKA_WRAP, true)
     *  </pre>
     * @return
     */
    public RSAPublicKeyTemplate getAsWrapperTemplate(){
        return (RSAPublicKeyTemplate) this
                .add(new CK_ATTRIBUTE(CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE(CKA_WRAP, true));
    }

    @Override
    public AsymmKeyTemplate.KeyType getKeyType() {
        return KeyType.PUBLIC;
    }
}
