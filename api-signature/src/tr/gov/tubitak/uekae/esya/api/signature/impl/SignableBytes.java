package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Signable byte array that will be used to add content to a signature.
 *
 * @see tr.gov.tubitak.uekae.esya.api.signature.Signable
 * @see tr.gov.tubitak.uekae.esya.api.signature.Signature#addContent(tr.gov.tubitak.uekae.esya.api.signature.Signable, boolean)
 *
 * @author ayetgin
 */
public class SignableBytes extends BaseSignable
{
    private byte[] bytes;
    private String uri;
    private String mime;

    public SignableBytes(byte[] aBytes, String aUri, String aMime)
    {
        bytes = aBytes;
        uri = aUri;
        mime = aMime;
    }

    public InputStream getContent() throws SignatureException
    {
        return new ByteArrayInputStream(bytes);
    }

    public String getURI()
    {
        return uri;
    }

    public String getMimeType()
    {
        return mime;
    }
}
