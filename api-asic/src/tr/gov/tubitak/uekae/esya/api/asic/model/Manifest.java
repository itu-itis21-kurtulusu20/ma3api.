package tr.gov.tubitak.uekae.esya.api.asic.model;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public class Manifest extends BaseASiCXMLDocument
{
    public Manifest(InputStream aStream) throws SignatureException
    {
        read(aStream);
        setASiCDocumentName("META-INF/manifest.xml");
    }

    @Override
    protected void init(Element element)
    {
        // todo
    }
}
