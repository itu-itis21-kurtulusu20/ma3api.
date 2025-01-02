package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import org.w3c.dom.Document;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 29.11.2012
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class AddExtraArchiveTimeStamp {

    private static String BASEDIR;
    private static String SIGNATUREFILENAME;
    private static String ENVELOPE_XML =
            "<envelope>\n" +
                    "  <data id=\"data1\">\n" +
                    "    <item>Item 1</item>\n"+
                    "    <item>Item 2</item>\n"+
                    "    <item>Item 3</item>\n"+
                    "  </data>\n" +
                    "</envelope>\n";

    private static int caseNum;
    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        BASEDIR = bpg.getBaseDir();

        caseNum=0;
    }

    @Before
    public void setUp()
    {
        /*switch (caseNum)
        {
            case 0: SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME; break;
            case 1: SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME; break;
            case 2: SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME; break;
            default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
        }
        caseNum++;*/
    }

    // create sample envelope xml
    // that will contain signature inside
    private org.w3c.dom.Document newEnvelope() throws ESYAException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(new ByteArrayInputStream(ENVELOPE_XML.getBytes()));
        }
        catch (Exception x){
            // we shouldnt be here if ENVELOPE_XML is valid
            x.printStackTrace();
        }
        throw new ESYAException("Cant construct envelope xml ");
    }

    @Test
    public void createEnvelopingAddArchiveTimeStamp()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+"xades_esa_enveloping.xml")),
                    new Context(BASEDIR)) ;
            signature.addArchiveTimeStamp();
            signature.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));
            assertTrue(true);
        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        }
    }

    @Test
    public void createEnvelopedAddArchiveTimeStamp()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR + "xades_esa_enveloped.xml")),
                    new Context(BASEDIR)) ;
            Document document =  signature.getDocument();

            signature.addArchiveTimeStamp();

            Source source = new DOMSource(document);
            Transformer transformer = /*(Transformer)*/ TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, new StreamResult(new FileOutputStream(BASEDIR+SIGNATUREFILENAME)));
            assertTrue(true);
        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void createDetachedAddArchiveTimeStamp()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+"xades_esa_detached.xml")),
                    new Context(BASEDIR)) ;
            signature.addArchiveTimeStamp();
            signature.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));
            assertTrue(true);
        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        }
    }

    @After
    public void tearDown()
    {

    }

    @AfterClass
    public static void finish()
    {

    }
}
