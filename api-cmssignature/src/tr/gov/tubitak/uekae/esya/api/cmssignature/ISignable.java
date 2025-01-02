package tr.gov.tubitak.uekae.esya.api.cmssignature;

import java.io.IOException;
import java.io.InputStream;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

/**
 * Encapsulates data to be signed.
 */

public interface ISignable 
{
    /**
     * Returns content data to add content to the signature. Not used at external sign.
     * @return data to be signed
     */
	byte[] getContentData();

    /**
     * Returns digest of content to sign. Cache digest if function takes time, it is called several times for one signature. 
     * @param aDigestAlg algorithm for digest operation
     * @return digest value of the data according t digest alg
     */
	byte[] getMessageDigest(DigestAlg aDigestAlg) throws CryptoException,IOException;
	
	
	/**
	 * Returns content as input stream. Used while creating ESA type signatures.
	 * @return
	 * @throws IOException
	 */
	InputStream getAsInputStream() throws IOException;

}
