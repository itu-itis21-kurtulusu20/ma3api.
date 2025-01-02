package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.Verifier;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoRuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;

/**
 * @author ayetgin
 */
public class SUNVerifier implements Verifier {
    private Signature mSignature;
    private boolean mVerified;

    public SUNVerifier(SignatureAlg aSignatureAlg) throws CryptoException {
        this(aSignatureAlg,null);
    }

    public SUNVerifier(SignatureAlg aSignatureAlg, Provider aProvider) throws CryptoException {
        try {
            if (aProvider != null)
                mSignature = Signature.getInstance(SUNProviderUtil.getSignatureName(aSignatureAlg), aProvider);
            else
                mSignature = Signature.getInstance(SUNProviderUtil.getSignatureName(aSignatureAlg));
        } catch (Exception x) {
            throw new CryptoException("Error initing signature", x);
        }
    }

    public void init(PublicKey aPublicKey) throws CryptoException {
        init(aPublicKey, null);
    }

    public void init(PublicKey aPublicKey, AlgorithmParams aParams) throws CryptoException {
        if (aParams != null) {
            throw new ArgErrorException("Unexpected alg params for SUN provider " + aParams.getClass());
        }
        try {
            mSignature.initVerify(aPublicKey);
        } catch (Exception x) {
            throw new CryptoException("Can not init verifier", x);
        }
    }

    public void reset() throws CryptoException {
        try {
            if (!mVerified)
                mSignature.verify(null); // should reset underlying signature
        } catch (Exception x) {
            throw new CryptoException("Error resetting verifier ", x);
        }
        mVerified = false;
    }

    public void update(byte[] aData) {
        try {
            mSignature.update(aData);
        } catch (Exception x) {
            throw new CryptoRuntimeException("Verification update error", x);
        }
    }

    public void update(byte[] aData, int aOffset, int aLength) {
        try {
            mSignature.update(aData, aOffset, aLength);
        } catch (Exception x) {
            throw new CryptoRuntimeException("Verification update error", x);
        }
    }

    public boolean verifySignature(byte[] aSignature) {
        try {
            boolean result = mSignature.verify(aSignature);
            mVerified = true;
            return result;
        } catch (Exception x) {
            x.printStackTrace();
            return false;
        }
    }
}
