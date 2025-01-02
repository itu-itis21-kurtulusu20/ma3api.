package tr.gov.tubitak.uekae.esya.api.crypto.params;


/**
 * @author ayetgin
 */

public class ParamsWithIV implements AlgorithmParams
{
    private byte[] mIV;

    public ParamsWithIV(byte[] aIV)
    {
        mIV = aIV;
    }

    public byte[] getIV()
    {
        return mIV;
    }

    public byte[] getEncoded()
    {
        return mIV;
    }
}
