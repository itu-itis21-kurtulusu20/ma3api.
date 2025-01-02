package tr.gov.tubitak.uekae.esya.api.asic.model;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public class OCFMetadata extends BaseASiCXMLDocument
{

    public OCFMetadata(InputStream aStream) throws SignatureException
    {
        read(aStream);
        setASiCDocumentName("META-INF/metadata.xml");
    }

    @Override
    protected void init(Element element)
    {
        // todo
    }
}
