package gnu.crypto.sig.ecdsa;

import gnu.crypto.agreement.BaseAgreement;
import gnu.crypto.derivationFunctions.DerivationFuncParamsWithSharedInfo;
import gnu.crypto.derivationFunctions.DerivationFunction;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.util.Util;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyAgreement;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithSharedInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.ECPublicKey;

public class ECKeyAgreement implements KeyAgreement
{
    private BaseAgreement mAgreement;
    private DerivationFunction mKDF;
    private AlgorithmParams mParams;

    public ECKeyAgreement(BaseAgreement aBaseAgreement, DerivationFunction aKDF)
    {
        mAgreement = aBaseAgreement;
        mKDF = aKDF;
    }

    
    public void init(Key key, AlgorithmParams params)
    {
        mAgreement.init(key);
        mParams = params;
    }

    /**
     * 
     */
    public SecretKey generateKey(Key key, Algorithm alg)
    {
        byte[] agreementValue = mAgreement.calculateAgreement(key);

        DerivationFuncParamsWithSharedInfo siParam = new DerivationFuncParamsWithSharedInfo(((ParamsWithSharedInfo) mParams).getSharedInfo());
        mKDF.init(agreementValue, siParam);
        int len = KeyUtil.getKeyLength(alg) / 8;
        byte[] secretKeyBytes = mKDF.generateBytes(len);

        return new SecretKeySpec(secretKeyBytes,"KDF"); // KDF ??
    }


}





