package gnu.crypto.derivationFunctions;

public class DerivationFuncParamsWithSharedInfo implements DerivationFuncParams
{
	
	private byte []mSharedInfo;
	
	public DerivationFuncParamsWithSharedInfo(byte [] aSharedInfo)
	{
		mSharedInfo = aSharedInfo;
	}
	
	public byte [] getSharedInfo()
	{
		return mSharedInfo;
	}
	
}
