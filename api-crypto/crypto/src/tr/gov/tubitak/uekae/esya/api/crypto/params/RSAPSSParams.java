package tr.gov.tubitak.uekae.esya.api.crypto.params;

import tr.gov.tubitak.uekae.esya.api.asn.algorithms.ERSASSA_PSS_params;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MGF;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

/**
 * @author ayetgin
 */
public class RSAPSSParams implements AlgorithmParams
{
    protected final int TRAILER_DEFAULT = 0x01;

    private DigestAlg mDigestAlg;
    private MGF mMGF;
    private int mSaltLength;
    private int mTrailerField;

    public RSAPSSParams()
    {
        mDigestAlg = DigestAlg.SHA256;
        mMGF = MGF.MGF1;
        mSaltLength = mDigestAlg.getDigestLength();
        mTrailerField = TRAILER_DEFAULT;
    }

    //NCipher salt len olarak özet algoritmasının uzunluğu kadar istemektedir.
    //Atik HSM için fark etmemektedir.
    public RSAPSSParams(DigestAlg aDigestAlg)
    {
        init(aDigestAlg, MGF.MGF1, aDigestAlg.getDigestLength(), TRAILER_DEFAULT);
    }

    public RSAPSSParams(DigestAlg aDigestAlg, MGF aMGF, int aSaltLength, int aTrailerField)
    {
        init(aDigestAlg, aMGF, aSaltLength, aTrailerField);
    }

    public void init(DigestAlg aDigestAlg, MGF aMGF, int aSaltLength, int aTrailerField)
    {
        mDigestAlg = aDigestAlg;
        mMGF = aMGF;
        mSaltLength = aSaltLength;
        mTrailerField = aTrailerField;
    }
    
    
    
    /*
	NOT TESTED
     public RSAPSSParams(PSSParameterSpec aPssParameterSpec) throws CryptoException
    {
    	String digestAlg = aPssParameterSpec.getDigestAlgorithm();
    	mDigestAlg = DigestAlg.fromName(digestAlg);
    	
    	String mgfName = aPssParameterSpec.getMGFAlgorithm();
    	
    	if(mgfName == "MGF1")
    		mMGF = MGF.MGF1;
    	else
    		throw new CryptoException("Only MGF1 is supported");
    	
    	mSaltLength = aPssParameterSpec.getSaltLength();
    	mTrailerField = aPssParameterSpec.getTrailerField();
    }*/

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    public void setDigestAlg(DigestAlg aDigestAlg)
    {
        mDigestAlg = aDigestAlg;
    }

    public MGF getMGF()
    {
        return mMGF;
    }

    public void setMGF(MGF aMGF)
    {
        mMGF = aMGF;
    }

    public int getSaltLength()
    {
        return mSaltLength;
    }

    public void setSaltLength(int aSaltLength)
    {
        mSaltLength = aSaltLength;
    }

    public int getTrailerField()
    {
        return mTrailerField;
    }

    public void setTrailerField(int aTrailerField)
    {
        mTrailerField = aTrailerField;
    }

    public PSSParameterSpec toPSSParameterSpec()
    {
    	if(mMGF==MGF.MGF1)
    	{
    		String mdName = mDigestAlg.getName();
    		String mgfName = "MGF1";
    		MGF1ParameterSpec mgfSpec = new MGF1ParameterSpec(mdName);
    		PSSParameterSpec spec = new PSSParameterSpec(mdName, mgfName, mgfSpec, mSaltLength, mTrailerField);
    		return spec;
    	}

    	throw new RuntimeException("MGF olarak sadece MGF1 desteklenmektedir");
    }

    public byte[] getEncoded() throws CryptoException
    {
        try {
            ERSASSA_PSS_params pss_params = new ERSASSA_PSS_params(toPSSParameterSpec());
            return pss_params.getEncoded();
        }
        catch (Exception x){
            throw new CryptoException(x.getMessage(), x);
        }
    }

    @Override
    public boolean equals(Object anObject) {

        if (anObject instanceof PSSParameterSpec) {
            PSSParameterSpec paramsObj = (PSSParameterSpec) anObject;

            if(paramsObj.getDigestAlgorithm().equals(mDigestAlg.getName())
                    && paramsObj.getMGFAlgorithm().equals(mMGF.getName())
                    && paramsObj.getSaltLength() == mSaltLength
                    && paramsObj.getTrailerField() == mTrailerField)
                return true;
            else
                return false;
        }

        if (anObject instanceof  RSAPSSParams) {
            RSAPSSParams paramsObj = (RSAPSSParams) anObject;

            if(paramsObj.getDigestAlg().equals(mDigestAlg)
                    && paramsObj.getMGF().equals(mMGF)
                    && paramsObj.getSaltLength() == mSaltLength
                    && paramsObj.getTrailerField() == mTrailerField)
                return true;
            else
                return false;
        }


        return false;


    }
}
