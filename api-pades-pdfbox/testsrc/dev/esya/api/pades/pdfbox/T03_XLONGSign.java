package dev.esya.api.pades.pdfbox;

import org.junit.Test;
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
import java.util.List;

public class T03_XLONGSign extends PAdESBaseTest{

    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
    private RSAPSSParams signatureAlgParams = null;

    @Test
    public void signXlong() throws Exception
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
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_XL, context);
        checkType(1, SignatureType.ES_BES, context); //Zaman Damgası

        AsnIO.dosyayaz(signatureBytes.toByteArray(),"T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox.pdf");
    }




    @Test
    public void signXlongXLong() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);
        signatureContainer.read(new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox.pdf"));

        BaseSigner signer = settings.getSecondSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSecondSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_A, context);
        checkType(1, SignatureType.ES_A, context); //Zaman Damgası
        checkType(2, SignatureType.ES_XL, context);
        checkType(3, SignatureType.ES_BES, context); //Zaman Damgası

        AsnIO.dosyayaz(signatureBytes.toByteArray(),"T:\\api-parent\\temp\\pdfbox\\xlong-xlong-pdfBox.pdf");
    }


    @Test
    public void signXlongXLongXLong() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);
        signatureContainer.read(new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-xlong-pdfBox.pdf"));

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_A, context);
        checkType(1, SignatureType.ES_A, context); //Zaman Damgası
        checkType(2, SignatureType.ES_A, context);
        checkType(3, SignatureType.ES_A, context); //Zaman Damgası
        checkType(4, SignatureType.ES_A, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(),"T:\\api-parent\\temp\\pdfbox\\xlong-xlong-xlong-pdfBox.pdf");
    }

    @Test
    public void signBesOnXlong() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox.pdf"));

        BaseSigner signer = settings.getSecondSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSecondSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_XL, context);
        checkType(1, SignatureType.ES_BES, context); //Zaman Damgası
        checkType(2, SignatureType.ES_BES, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\bes-from-xlong-pdfBox.pdf");
    }

    @Test
    public void signEstOnXlong() throws Exception
    {

        PAdESContext context = testSettings.createContext();
        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox.pdf");
        signatureContainer.read(fis);

        BaseSigner signer = settings.getSecondSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSecondSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_T);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(),"T:\\api-parent\\temp\\pdfbox\\est-from-xlong-pdfBox.pdf");
    }

    @Test
    public void signXlongOnXlong() throws Exception
    {
        PAdESContext context = testSettings.createContext();
        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox.pdf");
        signatureContainer.read(fis);

        BaseSigner signer = settings.getSecondSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSecondSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(),"T:\\api-parent\\temp\\pdfbox\\xlong-from-xlong-pdfBox.pdf");
    }

    @Test
    public void signXlongOnDoubleBes() throws Exception
    {
        PAdESContext context = testSettings.createContext();
        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\bes-2.pdf");
        signatureContainer.read(fis);

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        //ECertificate eCertificate = settings.getSignerCertificate();

        //add signature
        List<Signature> signatures = signatureContainer.getSignatures();
        PAdESSignature signature = (PAdESSignature) signatures.get(signatures.size() - 1);

        signature.setSigningTime(Calendar.getInstance());
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(),"T:\\api-parent\\temp\\pdfbox\\from-double-bes-to-xlong-pdfBox.pdf");
    }

    @Test
    public void signXlongTestInTwoSteps() throws Exception
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

        checkType(0, SignatureType.ES_XL, context);

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
        signatureContainer.getSignatures().get(0).upgrade(SignatureType.ES_XL);

        signatureContainer.write(signatureBytes);
    }
}
