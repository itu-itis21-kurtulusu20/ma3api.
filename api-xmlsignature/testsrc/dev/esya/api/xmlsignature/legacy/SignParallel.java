package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignedDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 23.11.2012
 * Time: 08:58
 * To change this template use File | Settings | File Templates.
 */
public class SignParallel {

    private static ECertificate CERTIFICATE;
    private static ECertificate CERTIFICATE2;
    private static String BASEDIR;
    private static String SIGNATUREFILENAME;
    private static String PLAINFILENAME;
    private static String PLAINFILEMIMETYPE;

    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        CERTIFICATE = bpg.getCertificate();
        CERTIFICATE2 = bpg.getCertificate2();
        BASEDIR = bpg.getBaseDir();
        PLAINFILENAME =UnitTestParameters.PLAINFILENAME;
        PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;
    }

    @Before
    public void setUp()
    {
        //SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPED_SIG_FILE_NAME;
    }

    @Test
    public void createParallel()
    {
        SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPED_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            SignedDocument signatures = new SignedDocument(context);
            Document doc = Resolver.resolve(PLAINFILENAME,context);
            String fragment = signatures.addDocument(doc);

            XMLSignature signature1 = signatures.createSignature();
            signature1.addDocument("#"+fragment,PLAINFILEMIMETYPE,false);
            signature1.addKeyInfo(CERTIFICATE);
            bpg.signWithBaseSigner(signature1);

            XMLSignature signature2 = signatures.createSignature();
            signature2.addDocument("#"+fragment,PLAINFILEMIMETYPE,false);
            signature2.addKeyInfo(CERTIFICATE2);
            bpg.signWithBaseSigner2(signature2);

            signatures.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));

        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
