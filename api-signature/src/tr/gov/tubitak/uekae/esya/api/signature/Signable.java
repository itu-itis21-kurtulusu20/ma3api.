package tr.gov.tubitak.uekae.esya.api.signature;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

import java.io.InputStream;
import java.net.URI;

/**
 * Signable is the interface to the data to signed.
 *
 * @see tr.gov.tubitak.uekae.esya.api.signature.impl.SignableBytes
 * @see tr.gov.tubitak.uekae.esya.api.signature.impl.SignableFile
 * @author ayetgin
 */
public interface Signable
{
    /**
     * @return data to be sigend as stream
     * @throws SignatureException
     */
    InputStream getContent() throws SignatureException;

    /**
     * Used to calculate digest output of the data. This method is here because
     * it provide means to cache the digest output.
     * @param aDigestAlg algorithm that will be used for digest operation
     * @return digest output
     * @throws SignatureException
     */
    byte[] getDigest(DigestAlg aDigestAlg) throws SignatureException;

    /**
     * @return location of signed data. Used for detached signature types
     * that has ability to reference signed data (for example XAdES)
     */
    String getURI();
    String getResourceName();

    /**
     * @return mime type of data to be signed
     */
    String getMimeType();
}
