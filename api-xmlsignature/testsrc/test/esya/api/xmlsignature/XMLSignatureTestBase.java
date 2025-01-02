package test.esya.api.xmlsignature;

import dev.esya.api.xmlsignature.legacy.UnitTestParameters;
import org.junit.Ignore;
import org.w3c.dom.Document;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Ignore
public class XMLSignatureTestBase extends CMSSignatureTest {

    public String BASEDIR = "T:/api-xmlsignature/test-output/java/ma3/";
    public String PLAINFILENAME = UnitTestParameters.PLAINFILENAME;
    public String PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;
    protected ByteArrayOutputStream signatureBytes = new ByteArrayOutputStream();


    public Context createContext() throws XMLSignatureException {
        return createContext(BASEDIR);
    }

    public Context createContext(String directory) throws XMLSignatureException {
        Context context = new Context(directory);
        context.setConfig(new Config("T:\\api-parent\\resources\\ug\\config\\xmlsignature-config.xml"));
        return context;
    }



    private static final String ENVELOPE_XML =
            "<envelope>\n" +
                    "  <data id=\"data1\">\n" +
                    "    <item>Item 1</item>\n"+
                    "    <item>Item 2</item>\n"+
                    "    <item>Item 3</item>\n"+
                    "  </data>\n" +
                    "</envelope>\n";


    public Document createSimpleEnvelope() throws ESYAException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(new ByteArrayInputStream(ENVELOPE_XML.getBytes()));
        }
        catch (Exception x){
            throw new ESYAException("Cant construct envelope xml", x);
        }
    }
}
