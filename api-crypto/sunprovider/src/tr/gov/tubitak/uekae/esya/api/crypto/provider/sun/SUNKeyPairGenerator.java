package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.KeyPairGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;

import java.security.KeyPair;
import java.security.Provider;
import java.security.spec.ECParameterSpec;

/**
 * @author ayetgin
 */
public class SUNKeyPairGenerator implements KeyPairGenerator {
    private java.security.KeyPairGenerator mKeyPairGenerator;

    public SUNKeyPairGenerator(AsymmetricAlg aAsymmetricAlg) throws CryptoException {
        this(aAsymmetricAlg, null);
    }

    public SUNKeyPairGenerator(AsymmetricAlg aAsymmetricAlg, Provider mProvider) throws CryptoException{
        String keyPairGenAlg = SUNProviderUtil.getKeyPairGenName(aAsymmetricAlg);
        try {
            if (mProvider == null)
                mKeyPairGenerator = java.security.KeyPairGenerator.getInstance(keyPairGenAlg);
            else
                mKeyPairGenerator = java.security.KeyPairGenerator.getInstance(keyPairGenAlg, mProvider);
        } catch (Exception x) {
            throw new CryptoException("Cant generate key pair generation ", x);
        }
    }

    public KeyPair generateKeyPair(AlgorithmParams aParams) throws CryptoException {
        try {
            if (aParams instanceof ParamsWithLength) {
                mKeyPairGenerator.initialize(((ParamsWithLength) aParams).getLength());
            } else if (aParams instanceof ParamsWithECParameterSpec) {
                ECParameterSpec domainParams = ((ParamsWithECParameterSpec) aParams).getECDomainParams();
                mKeyPairGenerator.initialize(domainParams);
            } else if (aParams instanceof ParamsWithAlgorithmIdentifier) {
                throw new RuntimeException("Key pair generation by algo identifier not supported yet");
            }

            return mKeyPairGenerator.generateKeyPair();

        } catch (Exception x) {
            throw new CryptoException("Cant generate key pair", x);
        }
    }

}
