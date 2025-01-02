package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoRuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;

import java.security.PrivateKey;
import java.security.Provider;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author ayetgin
 */
public class SUNSigner extends Signer {

    private Signature mSignature;
    private SignatureAlg mSignatureAlg;
    private boolean mSigned;
    private AlgorithmParams algorithmParams;

    public SUNSigner(SignatureAlg aSignatureAlg) throws CryptoException {
        this(aSignatureAlg,null);
    }

    public SUNSigner(SignatureAlg aSignatureAlg, Provider aProvider) throws CryptoException {
        mSignatureAlg = aSignatureAlg;
        try {
            if (aProvider != null)
                mSignature = Signature.getInstance(SUNProviderUtil.getSignatureName(aSignatureAlg), aProvider);
            else
                mSignature = Signature.getInstance(SUNProviderUtil.getSignatureName(aSignatureAlg));
        } catch (Exception x) {
            throw new CryptoException("Error initing signature", x);
        }
    }

    public void init(PrivateKey aPrivateKey) throws CryptoException {
        init(aPrivateKey, null);
    }

    @Override
    public void init(PrivateKey aPrivateKey, AlgorithmParams algorithmParams) throws CryptoException {
        this.algorithmParams = algorithmParams;
        if (algorithmParams != null) {
            throw new ArgErrorException("Unexpected alg params for SUN provider " + algorithmParams.getClass());
        }
        try {
            mSignature.initSign(aPrivateKey);
        } catch (Exception x) {
            throw new CryptoException("Can not init verifier", x);
        }
    }

    @Override
    public void reset() throws CryptoException {
        try {
            if (!mSigned)
                mSignature.sign(); // should reset signature;
        } catch (Exception x) {
            throw new CryptoException("Error resetting signer ", x);
        }
        mSigned = false;
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

    public byte[] sign(byte[] adata) throws CryptoException {
        try {
            if (adata != null)
                update(adata);
            byte[] result = mSignature.sign();
            mSigned = true;
            return result;
        } catch (Exception x) {
            throw new CryptoException("Error signing data. ", x);
        }
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		if(algorithmParams instanceof RSAPSSParams){
			RSAPSSParams params = (RSAPSSParams)algorithmParams;
			/*DigestAlg digestAlg = params.getDigestAlg();
			MGF mgf = params.getMGF();
			String mdName = digestAlg.getName();
    		String mgfName = mgf.getName();
    		MGF1ParameterSpec mgfSpec = new MGF1ParameterSpec(mdName);
    		int saltLenght = params.getSaltLength();
    		int trailerField = params.getTrailerField();
    		PSSParameterSpec pssspec = new PSSParameterSpec(mdName, mgfName, mgfSpec, saltLenght, trailerField);
    		return pssspec;*/
			return params.toPSSParameterSpec();
		}
    	
        return algorithmParams;
    }

    public SignatureAlg getSignatureAlgorithm() {
        return mSignatureAlg;
    }
}
