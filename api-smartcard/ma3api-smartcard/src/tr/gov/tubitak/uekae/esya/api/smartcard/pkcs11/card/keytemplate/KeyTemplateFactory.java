package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.DES3KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;

import java.security.Key;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 12/18/12 - 11:33 PM <p>
 * <b>Description</b>: <br>
 */
public class KeyTemplateFactory {
    public static KeyTemplate getKeyTemplate(String label, Key key) {
        String algorithm = key.getAlgorithm();
        if (algorithm.equalsIgnoreCase("AES"))
            return new AESKeyTemplate(label);
        else if (algorithm.equalsIgnoreCase("GENERIC") || algorithm.equalsIgnoreCase("HMAC"))
            return new HMACKeyTemplate(label);
        else if (algorithm.equalsIgnoreCase("GENERIC"))
            return new DES3KeyTemplate("DES");
        else if (algorithm.equalsIgnoreCase("RSA")) {
            if (key instanceof RSAPublicKey)
                return new RSAPublicKeyTemplate(label, (RSAPublicKey) key);
            else if (key instanceof PrivateKey)
                return new RSAPrivateKeyTemplate(label, null);
            else
                throw new ESYARuntimeException("Unknown RSA Key:" + key.getClass())  ;
        } else if (algorithm.equalsIgnoreCase("EC")) {
            throw new ESYARuntimeException("EC is not supported:" + algorithm);
        }
        else throw new ESYARuntimeException("Unknown Key Algorithm:" + algorithm)  ;

    }
}
