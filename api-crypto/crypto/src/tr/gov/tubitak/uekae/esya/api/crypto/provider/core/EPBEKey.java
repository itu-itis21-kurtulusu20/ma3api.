package tr.gov.tubitak.uekae.esya.api.crypto.provider.core;

import javax.crypto.interfaces.PBEKey;

/**
 * @author ayetgin
 */
public class EPBEKey implements PBEKey
{
    private byte[] mKey;
    private char[] mPassword;
    private byte[] mSalt;
    private int mIterationCount;

    public EPBEKey(byte[] aKey, char[] aPassword, byte[] aSalt, int aIterationCount)
    {
        mKey = aKey;
        mPassword = aPassword;
        mSalt = aSalt;
        mIterationCount = aIterationCount;
    }

    public char[] getPassword()
    {
        return mPassword;
    }

    public byte[] getSalt()
    {
        return mSalt;
    }

    public int getIterationCount()
    {
        return mIterationCount;
    }

    public String getAlgorithm()
    {
        return "PBE";
    }

    public String getFormat()
    {
        return null;
    }

    public byte[] getEncoded()
    {
        return mKey;
    }
}
