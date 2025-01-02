package dev.esya.api.pades.pdfbox;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Calendar;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class T01_BESSignTest extends PAdESBaseTest{

    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
    private RSAPSSParams signatureAlgParams = null;

    @Test
    public void T01_BESSignTest() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_BES, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\bes-1.pdf");
    }

    @Test
    public void T02_AddBEStoBESTest() throws Exception
    {
        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\bes-1.pdf");

        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(fis);

        BaseSigner signer = settings.getSecondSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSecondSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_BES, context);
        checkType(1, SignatureType.ES_BES, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\bes-2.pdf");
    }

    @Test
    public void T03_Two_BESSignTest() throws Exception{

        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);

        signatureContainer.write(signatureBytes);


        context = testSettings.createContext();
        signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);
        signatureContainer.read(new ByteArrayInputStream(signatureBytes.toByteArray()));



        BaseSigner signer2 = settings.getSecondSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate certificate2 = settings.getSecondSignerCertificate();
        Signature signature2 = signatureContainer.createSignature(certificate2);
        signature2.setSigningTime(Calendar.getInstance());
        signature2.sign(signer2);

        signatureBytes.reset();
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_BES, context);
        checkType(1, SignatureType.ES_BES, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\bes-3.pdf");

    }

    @Test
    public void T04_TestParseBESSignature() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\bes-1.pdf");


        signatureContainer.read(fis);

        // close the container
        signatureContainer.close();
    }

    @Test
    public void T05_BESSignTestInTwoSteps() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        ECertificate eCertificate = settings.getSignerCertificate();

        // add signature
        PAdESSignature signature = (PAdESSignature) signatureContainer.createSignature(eCertificate);

        byte [] dtbs = signature.initSign(signatureAlg, signatureAlgParams);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        signatureContainer.write(bos);
        bos.close();

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        byte [] signatureValue = signer.sign(dtbs);

        byte[] unfinishedBytes = bos.toByteArray();
        finishSigning(unfinishedBytes, signatureValue);

        checkIsValid(context);

        checkType(0, SignatureType.ES_BES, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\bes-5.pdf");
    }

    private void finishSigning(byte[] unfinishedBytes, byte [] signature) throws Exception
    {
        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\unfinished-1.pdf", unfinishedBytes);

        PAdESContext context = testSettings.createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        ByteArrayInputStream bis = new ByteArrayInputStream(unfinishedBytes);
        signatureContainer.read(bis);
        bis.close();

        signatureContainer.finishSign(signature);

        signatureContainer.write(signatureBytes);
    }
}
