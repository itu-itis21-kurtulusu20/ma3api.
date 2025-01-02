package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.spec.RSAKeyGenParameterSpec;

import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_KEY_TYPE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKK_RSA;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/10/12 - 11:47 PM <p>
 * <b>Description</b>: <br>
 * KeyTemplate for RSA Keys, Public or Private .Asymmetric Key plus
 * <pre>
 *  CK_ATTRIBUTE(CKA_KEY_TYPE, CKK_RSA)
 * </pre>
 */
public abstract class RSAKeyTemplate extends AsymmKeyTemplate<RSAKeyGenParameterSpec> implements RSAKey {
    protected MessageDigest OZET_ALICI;
    protected BigInteger modulus;

    protected byte [] encoded;


    public RSAKeyTemplate(String label, RSAKeyGenParameterSpec spec) {
        super(label, spec);
        add(new CK_ATTRIBUTE(CKA_KEY_TYPE, CKK_RSA));
        try {
            OZET_ALICI = MessageDigest.getInstance("SHA-1");
        }  catch (NoSuchAlgorithmException e) {
            throw new ESYARuntimeException(e);
        }
    }

    public int getKeysize() {
        return getSpec().getKeysize();
    }

    public BigInteger getModulus() {
        return modulus;
    }

    public String getAlgorithm() {
        return "RSA";
    }
}
