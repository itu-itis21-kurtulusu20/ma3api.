package tr.gov.tubitak.uekae.esya.api.crypto.params;



public class ParamsWithLength implements AlgorithmParams 
{
	private int mLength;

	public ParamsWithLength(int aLength) {
		mLength = aLength;
	}
	
	public int getLength() {
		return mLength;
	}

    public byte[] getEncoded()
    {
        throw new RuntimeException("Not applicable!");
    }
}
