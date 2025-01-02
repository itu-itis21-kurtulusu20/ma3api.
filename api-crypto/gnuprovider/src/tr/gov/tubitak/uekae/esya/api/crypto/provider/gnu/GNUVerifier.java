package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.sig.BaseSignature;
import gnu.crypto.sig.ISignature;
import tr.gov.tubitak.uekae.esya.api.crypto.Verifier;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.PublicKey;
import java.util.HashMap;

/**
 * @author ayetgin
 */
public class GNUVerifier implements Verifier
{

    private ISignature mSignature;
    private SignatureAlg mSignatureAlg;
    private PublicKey mKey;
    private AlgorithmParams mParams;

    public GNUVerifier(SignatureAlg aSignatureAlg) throws CryptoException
    {
        mSignatureAlg = aSignatureAlg;
    }


    public void init(PublicKey aPublicKey) throws CryptoException
    {
        init(aPublicKey, null);
    }

    public void init(PublicKey aPublicKey, AlgorithmParams aParams) throws CryptoException
    {
        mKey = aPublicKey;
        mParams = aParams;

        mSignature = GNUProviderUtil.resolveSignature(mSignatureAlg, mParams);

        HashMap map = new HashMap();
        map.put(BaseSignature.VERIFIER_KEY, aPublicKey);
        mSignature.setupVerify(map);
    }

    public void reset() throws CryptoException
    {
        mSignature = GNUProviderUtil.resolveSignature(mSignatureAlg, mParams);
        init(mKey);
    }

    public void update(byte[] aData)
    {
        update(aData, 0, aData.length);
    }

    public void update(byte[] aData, int aOffset, int aLength)
    {
        mSignature.update(aData, aOffset, aLength);
    }

    public boolean verifySignature(byte[] aSignature)
    {
        return mSignature.verify(aSignature);  
    }
}
