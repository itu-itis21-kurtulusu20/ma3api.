package tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.impl.BaseSignable;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public class SignableDocument extends BaseSignable
{
    Document document;

    public SignableDocument(Document aDocument)
    {
        document = aDocument;
    }

    public InputStream getContent() throws SignatureException
    {
        return document.getStream();
    }

    public String getURI()
    {
        return document.getURI();
    }

    public String getMimeType()
    {
        return document.getMIMEType();
    }
}
