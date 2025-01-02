package tr.gov.tubitak.uekae.esya.api.crypto.params;



public class ParamsWithSharedInfo implements AlgorithmParams
{

	private byte[] mSharedInfo;

	public ParamsWithSharedInfo(byte[] aSharedInfo)
	{
		mSharedInfo = aSharedInfo;
	}

	public byte[] getSharedInfo()
	{
		return mSharedInfo;
	}

    public byte[] getEncoded()
    {
        return mSharedInfo;
    }
}
