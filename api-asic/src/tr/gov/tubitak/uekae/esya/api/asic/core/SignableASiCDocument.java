package tr.gov.tubitak.uekae.esya.api.asic.core;

import tr.gov.tubitak.uekae.esya.api.asic.model.ASiCDocument;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.impl.BaseSignable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author ayetgin
 */
public class SignableASiCDocument extends BaseSignable
{
    ASiCDocument document;

    public SignableASiCDocument(ASiCDocument aDocument)
    {
        document = aDocument;
    }

    public InputStream getContent() throws SignatureException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.write(baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public String getURI()
    {
        return document.getASiCDocumentName();
    }

    public String getMimeType()
    {
        return "text/xml";
    }
}

