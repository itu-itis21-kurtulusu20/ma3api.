package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ayetgin
 */
public class XmlSigDetector
{
    private static Logger logger = LoggerFactory.getLogger(XmlSigDetector.class);

    public boolean isSignature(InputStream stream)
    {
        final AtomicBoolean result = new AtomicBoolean(false);
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(stream, new DefaultHandler(){

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
                {
                    System.out.println(qName);
                    if (localName.equals("envelope") || localName.equals("Signature"))
                        result.set(true);
                    else if (qName.contains(":Signature")
                            || qName.contains(":envelope")
                            || qName.equals("Signature")
                            || qName.equals("envelope"))
                        result.set(true);
                    throw new MySaxException();
                }
            });
        } catch (Exception x){
            if (!(x instanceof MySaxException))
                logger.debug("Problem in parsing", x);
        }

        return result.get();
    }

    private class MySaxException extends SAXException
    {

    }
}
