package tr.gov.tubitak.uekae.esya.api.cmssignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;

/**
 * Encapsulates data to be signed, when in byte[] forSmat.
 *
 * @see tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable
 */

public class SignableFile implements ISignable 
{
	
	private static Logger logger =LoggerFactory.getLogger(SignableFile.class);
	
	private static final int DEFAULT_BUF_SIZE = 32 * 1024;

	private final byte[] mBuffer;
	private final File mFile;
	private HashMap<DigestAlg, byte[]> mOzetTablo = new HashMap<DigestAlg, byte[]>();

	public SignableFile(File aFile) 
	{
		this(aFile, DEFAULT_BUF_SIZE);
	}

	public SignableFile(File aFile, int aBufferSize) 
	{
		mFile = aFile;
		mBuffer = new byte[aBufferSize];
	}
    /**
     * @return digest of the data according to digest algorithm
     */
	public byte[] getMessageDigest(DigestAlg aDigestAlg) 
	throws CryptoException,IOException 
	{
		byte[] ozet = mOzetTablo.get(aDigestAlg);
		if (ozet == null) 
		{
			ozet = DigestUtil.digestStream(aDigestAlg, new FileInputStream(mFile), mBuffer.length);
			mOzetTablo.put(aDigestAlg, ozet);
		}

		return ozet;

	}
    /**
     * @return data to be signed
     */
	public byte[] getContentData()
	{
		try
		{
			return Files.readAllBytes(mFile.toPath());
		}
		catch (IOException e)
		{
			throw new ESYARuntimeException(e);
		}
	}

	public InputStream getAsInputStream() throws IOException
	{
		return new FileInputStream(mFile);
	}
	
	public File getFile()
	{
		return mFile;
	}
	
	
	
	
}
