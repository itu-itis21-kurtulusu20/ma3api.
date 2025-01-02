package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.sig.BaseSignature;
import gnu.crypto.sig.ISignature;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;

import java.security.PrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;

/**
 * @author ayetgin
 */
public class GNUSigner extends Signer
{
    private ISignature mSignature;
    private SignatureAlg mSignatureAlg;
    private AlgorithmParams mAlgorithmParams;
    private PrivateKey mKey;

    public GNUSigner(SignatureAlg aSignatureAlg) throws CryptoException
    {
        mSignatureAlg = aSignatureAlg;

    }

    @SuppressWarnings("unchecked")
    public void init(PrivateKey aPrivateKey) throws CryptoException
    {
        init(aPrivateKey, null);
    }

    @Override
    public void init(PrivateKey aPrivateKey, AlgorithmParams aParams) throws CryptoException
    {
        mAlgorithmParams = aParams;
        mSignature = GNUProviderUtil.resolveSignature(mSignatureAlg, aParams);

        mKey = GNUProviderUtil.resolvePrivateKey(aPrivateKey);
        HashMap map = new HashMap();
        try
        {
             map.put(BaseSignature.SIGNER_KEY, mKey);
             mSignature.setupSign(map);
        }
        catch (Exception x){
            throw new ArgErrorException(GenelDil.mesaj(GenelDil.IMZANAH_HATALI), x);
        }
    }

    @Override
    public void reset() throws CryptoException
    {
        mSignature = GNUProviderUtil.resolveSignature(mSignatureAlg, mAlgorithmParams);
        init(mKey);
    }

    public void update(byte[] aData)
    {
        if (aData!=null)
            update(aData, 0, aData.length);
    }

    public void update(byte[] aData, int aOffset, int aLength)
    {
        if (aData!=null)
        mSignature.update(aData, aOffset, aLength);
    }

    public byte[] sign(byte[] aData)
    {
        if (aData!=null)
            update(aData);
        return (byte[])mSignature.sign();
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
		if(mAlgorithmParams instanceof RSAPSSParams){
			RSAPSSParams params = (RSAPSSParams)mAlgorithmParams;
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
        return mAlgorithmParams;
    }

    public SignatureAlg getSignatureAlgorithm()
    {
        return mSignatureAlg;  
    }

}
