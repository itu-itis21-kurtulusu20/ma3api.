package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyAgreement;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;


import gnu.crypto.agreement.BaseAgreement;
import gnu.crypto.derivationFunctions.DerivationFunction;
import gnu.crypto.sig.ecdsa.ECKeyAgreement;

import javax.crypto.SecretKey;
import java.security.Key;

/**
 * @author ayetgin
 */
public class GNUKeyAgreement implements KeyAgreement
{
    private KeyAgreement mKeyAgreement;

    public GNUKeyAgreement(KeyAgreementAlg aKeyAgreementAlg) throws CryptoException
    {
        if (aKeyAgreementAlg==null)
            throw new ArgErrorException("KeyAgreementAlg null");

        if (aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECCDH && aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECDH)
            throw new UnknownElement(GenelDil.AGREEMENTALG_BILINMIYOR);

        
        
        BaseAgreement agreement = GNUProviderUtil.resolveAgreement(aKeyAgreementAlg.getAgreementAlg());
        DerivationFunction df = GNUProviderUtil.resolveDerivationFunction(aKeyAgreementAlg.getDerivationFunctionAlg());
        
        if (aKeyAgreementAlg.getAgreementAlg() == AgreementAlg.ECCDH || aKeyAgreementAlg.getAgreementAlg() == AgreementAlg.ECDH)
        	mKeyAgreement = new ECKeyAgreement(agreement,df);
        

    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException {
    	mKeyAgreement.init(aKey, aParams);
    }

    public SecretKey generateKey(Key aKey, Algorithm aAlg) throws CryptoException {
        return mKeyAgreement.generateKey(aKey, aAlg);
    }
}
