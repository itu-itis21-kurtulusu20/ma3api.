package tr.gov.tubitak.uekae.esya.api.asic.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ayetgin
 */
public abstract class BaseASiCXMLDocument implements ASiCDocument, XMLElement
{
    protected Document document;
    protected byte[] initialBytes;

    protected boolean documentChanged=false;

    protected String documentName;

    public void read(InputStream inputStream) throws SignatureException
    {
        try {
            initialBytes = StreamUtil.readAll(inputStream);
            Source s = new StreamSource(new ByteArrayInputStream(initialBytes));
            DOMResult dom = new DOMResult();
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(s, dom);
            document = (Document)dom.getNode();
            init(document.getDocumentElement());
        }catch (Exception x){
            throw new SignatureException("Error in reading XML Signature", x); // todo
        }

    }

    public void write(OutputStream outputStream)
        throws SignatureException
    {
        try {
            if (initialBytes!=null){
                outputStream.write(initialBytes);
                return;
            }
            Source source = new DOMSource(document);
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(source, new StreamResult(outputStream));
        }catch (Exception x){
            throw new SignatureException("Cannot output signature.", x);
        }
    }

    protected abstract void init(Element element) throws SignatureException;

    protected Document createDocument(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            return db.newDocument();
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }


    public String getASiCDocumentName()
    {
        return documentName;
    }

    public void setASiCDocumentName(String aName)
    {
        documentName = aName;
    }

    public Element getElement()
    {
        return document.getDocumentElement();
    }

    public byte[] getInitialBytes()
    {
        return initialBytes;
    }
}
