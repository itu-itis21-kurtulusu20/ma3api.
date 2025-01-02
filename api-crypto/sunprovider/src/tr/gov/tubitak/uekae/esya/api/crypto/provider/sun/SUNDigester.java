package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.Digester;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;

import java.security.MessageDigest;
import java.security.Provider;

/**
 * @author ayetgin
 */
public class SUNDigester implements Digester {
    MessageDigest mDigest;

    public SUNDigester(DigestAlg aDigestAlg) throws CryptoException {
        this(aDigestAlg, null);
    }

    public SUNDigester(DigestAlg aDigestAlg, Provider aProvider) throws UnknownElement {
        try {
            if (aProvider != null)
                mDigest = MessageDigest.getInstance(aDigestAlg.getName(), aProvider);
            else
                mDigest = MessageDigest.getInstance(aDigestAlg.getName());
        } catch (Exception x) {
            throw new UnknownElement("Unknown digest alg " + aDigestAlg, x);
        }
    }

    public void update(byte[] aData) {
        mDigest.update(aData);
    }

    public void update(byte[] aData, int aOffset, int aLength) {
        mDigest.update(aData, aOffset, aLength);
    }

    public byte[] digest() {
        return mDigest.digest();
    }
}
