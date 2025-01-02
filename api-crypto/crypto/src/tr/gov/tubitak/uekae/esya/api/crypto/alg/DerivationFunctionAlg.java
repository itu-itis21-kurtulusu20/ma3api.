package tr.gov.tubitak.uekae.esya.api.crypto.alg;


/**
 * @author ayetgin
 */

public class DerivationFunctionAlg implements Algorithm
{


	private DerivationFunctionType mFunctionType;
	private DigestAlg mDigestAlg;
	
	DerivationFunctionAlg(DerivationFunctionType aFunctionType, DigestAlg aDigestAlg)
	{
		this.mFunctionType = aFunctionType;
		this.mDigestAlg = aDigestAlg;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getOID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DerivationFunctionType getFunctionType() {
		return mFunctionType;
	}

	public DigestAlg getDigestAlg() {
		return mDigestAlg;
	}

}
