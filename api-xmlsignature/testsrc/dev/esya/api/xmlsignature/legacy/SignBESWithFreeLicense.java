package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.helper.XmlSignatureTestHelper;
import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 04.01.2013
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class SignBESWithFreeLicense {

    private static ECertificate CERTIFICATE;
    private static String BASEDIR;
    private static String ROOTDIR;
    private static String SIGNATUREFILENAME;
    //private static String SIGNATUREFILE_TOBE_UPGRADED;
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

    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        PLAINFILENAME = UnitTestParameters.PLAINFILENAME;
        PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

        CERTIFICATE = bpg.getCertificate();
        BASEDIR = bpg.getBaseDir();
        ROOTDIR = bpg.getRootDir();

        try {
            XmlSignatureTestHelper.getInstance().loadFreeLicense();
        }
        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (ESYAException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Before
    public void setUp() throws FileNotFoundException, ESYAException
    {
    }

    private void loadFreeLicense() throws FileNotFoundException, ESYAException  //Imza dosyasi olusturmak icin
    {
        String lisansFilePath = ROOTDIR + "\\docs\\config\\lisans\\lisansFree.xml";
        LicenseUtil.setLicenseXml(new FileInputStream(lisansFilePath));
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
        throw new ESYAException("Cant construct envelope xml");
    }


    @Test
    public void createBESEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIGNED_WITH_FREE_LICENSE;
        try {
            //loadFreeLicense();
            Context context = new Context(BASEDIR);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(CERTIFICATE);
            bpg.signWithBaseSigner(signature);
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
    public void createBESEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIGNED_WITH_FREE_LICENSE;
        try {
            org.w3c.dom.Document envelopeDoc = newEnvelope();
            Context context = context = new Context(BASEDIR);
            context.setDocument(envelopeDoc);
            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.getDocumentElement().appendChild(signature.getElement());
            signature.addDocument("#data1","text/xml",false);
            signature.addKeyInfo(CERTIFICATE);
            bpg.signWithBaseSigner(signature);
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
    public void createBESDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIGNED_WITH_FREE_LICENSE;;
        try {
            Context context = new Context(BASEDIR);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(BASEDIR + PLAINFILENAME,PLAINFILEMIMETYPE,false);
            signature.addKeyInfo(CERTIFICATE);
            bpg.signWithBaseSigner(signature);
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
