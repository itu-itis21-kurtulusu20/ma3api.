package tr.gov.tubitak.uekae.esya.api.cmssignature;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

/**
 * Encapsulates data to be signed, when in byte[] format.
 *
 * @see tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable
 */

public class SignableByteArray implements ISignable 
{
	private byte[]  mBytes;
	private HashMap<DigestAlg, byte[]> mOzetTablo = new HashMap<DigestAlg, byte[]>(); 

    public SignableByteArray(byte[]  aBytes)
	{
            mBytes = aBytes;
    }


    /**
     * @return data to be signed
     */
	public byte[] getContentData() {
		
		return mBytes;
	}

    /**
     * @return digest of the data according to digest algorithm
     */
	public byte[] getMessageDigest(DigestAlg aOzetAlg)
	throws CryptoException
	{
		byte[] ozet = mOzetTablo.get(aOzetAlg);
		if(ozet == null)
		{
			ozet = DigestUtil.digest(aOzetAlg, mBytes);
			mOzetTablo.put(aOzetAlg, ozet);
			
		}

		return ozet;
	}


	public InputStream getAsInputStream() throws IOException
	{
		return new ByteArrayInputStream(mBytes);
	}

}
