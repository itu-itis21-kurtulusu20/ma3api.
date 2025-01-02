package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transform;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyInfoElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509SubjectName;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 03.01.2013
 * Time: 10:07
 * To change this template use File | Settings | File Templates.
 */
public class SignEDefter {

    private static ECertificate CERTIFICATE;
    private static String BASEDIR;
    private static String SIGNATUREFILENAME;
    private static String PLAINFILENAME;
    private static String PLAINFILEMIMETYPE;
    private static String SIGNATURE_FILE_TOBE_UPGRADED;

    private static int caseNum;
    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        CERTIFICATE = bpg.getCertificate();
        BASEDIR = bpg.getBaseDir();
        PLAINFILENAME =UnitTestParameters.PLAINFILENAME;
        PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

        caseNum = 0;
    }

    @Before
    public void setUp()
    {
       /*switch (caseNum)
        {
            case 0: SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME; break;
            case 1: SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_EST_ENVELOPED_SIG_FILE_NAME; break;
            case 2: SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESC_ENVELOPED_SIG_FILE_NAME; break;
            case 3: SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESX_ENVELOPED_SIG_FILE_NAME; break;
            case 4: SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESXL_ENVELOPED_SIG_FILE_NAME; break;
            case 5: SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESA_ENVELOPED_SIG_FILE_NAME; break;
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

            return db.parse(new FileInputStream(BASEDIR + UnitTestParameters.TEST_EDEFTER_FILE_NAME));
        }
        catch (Exception x){
            // we shouldnt be here if ENVELOPE_XML is valid
            x.printStackTrace();
        }
        throw new ESYAException("Cant construct envelope xml ");
    }

    @Test
    public void createEnvelopedBesEDefter()
    {
        SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME;
        try {
            org.w3c.dom.Document envelopeDoc = newEnvelope();
            Context context = context = new Context(BASEDIR);
            context.setDocument(envelopeDoc);
            XMLSignature signature = new XMLSignature(context, false);
            signature.setSigningTime(Calendar.getInstance());
            envelopeDoc.getDocumentElement().appendChild(signature.getElement());

            Transforms transform = new Transforms(context);
            transform.addTransform(new Transform(context, TransformType.ENVELOPED.getUrl()));
            java.security.cert.X509Certificate msCert = CERTIFICATE.asX509Certificate();


            signature.addDocument("","text/xml", transform, DigestMethod.SHA_256, false);
            //signature.addKeyInfo(msCert.getPublicKey());
            signature.addKeyInfo(CERTIFICATE);

            KeyInfo keyInfo = signature.createOrGetKeyInfo();
            int elementCount = keyInfo.getElementCount();
            for (int i=0;i<elementCount;i++)
            {
                KeyInfoElement kiElement = keyInfo.get(i);
                if(kiElement instanceof X509Data)
                {
                    X509Data x509Data = (X509Data)kiElement;
                    X509SubjectName x509SubjectName = new X509SubjectName(context,CERTIFICATE.getSubject().stringValue());
                    x509Data.add(x509SubjectName);
                    break;
                }
            }

            bpg.signWithBaseSigner(signature);
            Source source = new DOMSource(envelopeDoc);
            Transformer transformer = /*(Transformer)*/ TransformerFactory.newInstance().newTransformer();
            (new File(BASEDIR+SIGNATUREFILENAME)).delete();
            transformer.transform(source, new StreamResult(new FileOutputStream(BASEDIR + SIGNATUREFILENAME)));
            SIGNATURE_FILE_TOBE_UPGRADED = SIGNATUREFILENAME;
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

    @After
    public void tearDown()
    {

    }

    @AfterClass
    public static void finish()
    {

    }
}
