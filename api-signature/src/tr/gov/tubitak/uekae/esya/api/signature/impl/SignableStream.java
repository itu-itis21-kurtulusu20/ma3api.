package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.InputStream;

public class SignableStream extends BaseSignable
{
    InputStream inputStream;
    private String uri;
    private String mime;

    public SignableStream(InputStream inputStream, String uri, String mime)
    {
        this.inputStream = inputStream;
        this.uri = uri;
        this.mime = mime;
    }


    @Override
    public InputStream getContent() throws SignatureException
    {
        return inputStream;
    }

    @Override
    public String getURI() {
        return uri;
    }

    @Override
    public String getMimeType()
    {
        return mime;
    }
}
