package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 27.11.2012
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class SignWithDifferentCERPRIVATEKEY {

    private static ECertificate CERTIFICATE;
    private static String BASEDIR;
    private static String SIGNATUREFILENAME;
    private static String PLAINFILENAME;
    private static String PLAINFILEMIMETYPE;
    private static final String ENVELOPE_XML =
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
        CERTIFICATE = bpg.getCertificate();
        BASEDIR = bpg.getBaseDir();
        PLAINFILENAME = UnitTestParameters.PLAINFILENAME;
        PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

        caseNum=0;
    }

    @Before
    public void setUp()
    {
        /*switch (caseNum)
        {
            case 0: SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME; break;
            case 1: SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME; break;
            case 2: SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME; break;
            case 3: SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME; break;
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
    public void createEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(CERTIFICATE);
            bpg.signWithifferentBaseSigner(signature);
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
    public void createEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME;
        try {
            org.w3c.dom.Document envelopeDoc = newEnvelope();
            Context context = context = new Context(BASEDIR);
            context.setDocument(envelopeDoc);
            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.getDocumentElement().appendChild(signature.getElement());
            signature.addDocument("#data1","text/xml",false);
            signature.addKeyInfo(CERTIFICATE);
            bpg.signWithifferentBaseSigner(signature);
            Source source = new DOMSource(envelopeDoc);
            Transformer transformer = /*(Transformer)*/ TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, new StreamResult(new FileOutputStream(BASEDIR+SIGNATUREFILENAME)));
            assertTrue(true);
        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (ESYAException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        }
    }

    @Test
    public void createDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(BASEDIR + PLAINFILENAME,PLAINFILEMIMETYPE,false);
            signature.addKeyInfo(CERTIFICATE);
            bpg.signWithifferentBaseSigner(signature);
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
