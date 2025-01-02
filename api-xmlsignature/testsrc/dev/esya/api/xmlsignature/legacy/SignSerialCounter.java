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
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class SignSerialCounter {

    private static ECertificate CERTIFICATE;
    private static ECertificate CERTIFICATE2;
    private static ECertificate CERTIFICATE3;
    private static String BASEDIR;
    private static String SIGNATUREFILENAME;
    private static String PLAINFILENAME;
    private static String PLAINFILENAME2;
    private static String PLAINFILEMIMETYPE;
    private static String PLAINFILEMIMETYPE2;
    private static String SIGNATUREFILETOBECOUNTERSIGNED;

    private static int caseNum;
    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        CERTIFICATE = bpg.getCertificate();
        CERTIFICATE2 = bpg.getCertificate2();
        CERTIFICATE3 = bpg.getCertificate3();
        BASEDIR = bpg.getBaseDir();
        PLAINFILENAME =UnitTestParameters.PLAINFILENAME;
        PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;
        PLAINFILENAME2 = UnitTestParameters.PLAINFILENAME2;
        PLAINFILEMIMETYPE2 = UnitTestParameters.PLAINFILE2MIMETYPE;

        caseNum = 0;
    }

    @Before
    public void setUp()
    {
        /*switch(caseNum)
        {
            case 0: SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME; break;
            case 1: SIGNATUREFILENAME = UnitTestParameters.COUNTER_ENVELOPING_SIG_FILE_NAME; break;
            case 2: SIGNATUREFILENAME = UnitTestParameters.CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME; break;
            case 3: SIGNATUREFILENAME = UnitTestParameters.COUNTER_ON_PARALLEL_SIG_FILE_NAME; break;
            case 4: SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ADDED_SIG_FILE_NAME; break;
            default: break;
        }
        caseNum++;*/
    }

    @Test
    public void createParallelSignature() //sifirdan parallel imza olusturuyor
    {
        SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            SignedDocument signatures = new SignedDocument(context);

            XMLSignature signature1 = signatures.createSignature();
            signature1.addDocument(PLAINFILENAME,PLAINFILEMIMETYPE,true);
            signature1.addDocument(PLAINFILENAME2,PLAINFILEMIMETYPE2,true);
            signature1.addKeyInfo(CERTIFICATE);
            bpg.signWithBaseSigner(signature1);

            XMLSignature signature2 = signatures.createSignature();
            //signature2.addDocument(PLAINFILENAME,PLAINFILEMIMETYPE,true);
            signature2.addDocument(PLAINFILENAME2,PLAINFILEMIMETYPE2,true);
            signature2.addKeyInfo(CERTIFICATE2);
            bpg.signWithBaseSigner2(signature2);

            signatures.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));

        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void createCounter() //basit imzaya counter signature ekliyor
    {
        SIGNATUREFILENAME = UnitTestParameters.COUNTER_ENVELOPING_SIG_FILE_NAME;
        SIGNATUREFILETOBECOUNTERSIGNED = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            Document doc = Resolver.resolve(SIGNATUREFILETOBECOUNTERSIGNED,context);
            XMLSignature signature = XMLSignature.parse(doc,context);
            XMLSignature counterSignature = signature.createCounterSignature();
            counterSignature.addKeyInfo(CERTIFICATE3);
            bpg.signWithBaseSigner3(counterSignature);
            signature.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));

        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void createCounterOnParallelSignature() //sifirdan olusturuyor butun imzalari
    {
        SIGNATUREFILENAME = UnitTestParameters.CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            SignedDocument signatures = new SignedDocument(context);

            XMLSignature signature1 = signatures.createSignature();
            signature1.addDocument(PLAINFILENAME,PLAINFILEMIMETYPE,true);
            signature1.addDocument(PLAINFILENAME2,PLAINFILEMIMETYPE2,true);
            signature1.addKeyInfo(CERTIFICATE);
            bpg.signWithBaseSigner(signature1);

            XMLSignature signature2 = signatures.createSignature();
            signature2.addDocument(PLAINFILENAME2,PLAINFILEMIMETYPE2,true);
            signature2.addKeyInfo(CERTIFICATE2);
            bpg.signWithBaseSigner2(signature2);

            XMLSignature signatureToBeCounterSigned = signatures.getSignature(0);
            XMLSignature counterSignature = signatureToBeCounterSigned.createCounterSignature();
            counterSignature.addKeyInfo(CERTIFICATE3);
            bpg.signWithBaseSigner3(counterSignature);
            signatures.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));
        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    //mevcut bir paralel imza dosyasına counter signature ekleme
    @Test
    public void addCounterSignatureToParallelSignedSignature()
    {
        SIGNATUREFILENAME = UnitTestParameters.COUNTER_ON_PARALLEL_SIG_FILE_NAME;
        try {
            Context context = new Context(BASEDIR);
            Document doc = Resolver.resolve(UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME,context);
            SignedDocument signatures = new SignedDocument(doc,context);

            XMLSignature signature = signatures.getSignature(0);
            XMLSignature counterSignature = signature.createCounterSignature();
            counterSignature.addKeyInfo(CERTIFICATE3);
            bpg.signWithBaseSigner3(counterSignature);

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
