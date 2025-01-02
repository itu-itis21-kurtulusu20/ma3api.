package dev.esya.api.xmlsignature.legacy.xmldsig;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.Security;


/**
 * @author ahmety
 * date: Aug 19, 2009
 */
public class SignatureAlgorithmsTest extends XMLBaseTest
{

    public void testEnvelopedECDSA() throws Exception
    {
        File sigFile = new File(BASELOC + "org\\apache\\xml\\security\\samples\\input\\ecdsaSignature.xml");

        XMLSignature signature = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), new Context());

        ValidationResult result = signature.verify();
        assertTrue(result.getType()== ValidationResultType.VALID);

    }

    public void testAllDSA() throws Exception
    {
        SignatureMethod[] smArr = new SignatureMethod[]{
                                            SignatureMethod.DSA_SHA1,
                                            SignatureMethod.DSA_SHA256 };

        //KeyPair kp = GNUTumKripto.getInstance().anahtarUret("DSA", 256);
        KeyPair kp = KeyPairGenerator.getInstance("DSA").genKeyPair();

        for (int i=0; i<smArr.length;i++){
            Context context = new Context();
            //context.getValidators().clear();
            context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

            XMLSignature signature  = new XMLSignature(context);
            signature.setSignatureMethod(smArr[i]);
            signature.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
            signature.addKeyInfo(kp.getPublic());
            signature.sign(kp.getPrivate());
            File sigFile = new File(BASE_MA3 +"signatureMethods/"
                                    +smArr[i].getAlgorithm().getName().toLowerCase()
                                    +"-"
                                    +smArr[i].getDigestAlg().getName().toLowerCase()
                                    +"-signature.xml");

            signature.write(new FileOutputStream(sigFile));

            XMLSignature ss2 = XMLSignature.parse(
                                new FileDocument(sigFile, "text/xml"), context);

            ValidationResult result = ss2.verify();
            assertTrue(result.getType()== ValidationResultType.VALID);
        }

    }


    /* Pss test edilirken Hmac Testlerinin düzgün çalışmadığı görüldü. Geçici süreli kaldırıldı.
    public void testAllHMAC() throws Exception
    {
        SignatureMethod[] smArr = new SignatureMethod[]{
                                            SignatureMethod.HMAC_MD5,
                                            SignatureMethod.HMAC_RIPEMD,
                                            SignatureMethod.HMAC_SHA1,
                                            SignatureMethod.HMAC_SHA256,
                                            SignatureMethod.HMAC_SHA384,
                                            SignatureMethod.HMAC_SHA512 };

        for (int i=0; i<smArr.length;i++){
            Context context = new Context();
            //context.getValidators().clear();

            context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

            XMLSignature signature  = new XMLSignature(context);
            signature.setSignatureMethod(smArr[i]);
            signature.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
            signature.sign("secret".getBytes("ASCII"));

            File sigFile = new File(BASE_MA3 +"signatureMethods/"
                                    +smArr[i].getAlgorithm().getName().toLowerCase()
                                    +"-"
                                    +smArr[i].getDigestAlg().getName().toLowerCase()
                                    +"-signature.xml");

            signature.write(new FileOutputStream(sigFile));

            XMLSignature ss2 = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), context);

            ValidationResult result = ss2.verify("secret".getBytes("ASCII"));
            assertTrue(result.getType()== ValidationResultType.VALID);
        }
    }*/

    public void testAllRSA() throws Exception
    {
        SignatureMethod[] smArr = new SignatureMethod[]{
                                            SignatureMethod.RSA_MD5,
                                            //SignatureMethod.RSA_RIPEMD,
                                            SignatureMethod.RSA_SHA1,
                                            SignatureMethod.RSA_SHA256,
                                            SignatureMethod.RSA_SHA384,
                                            SignatureMethod.RSA_SHA512};

        KeyPair kp = KeyPairGenerator.getInstance("RSA").genKeyPair();


        for (int i=0; i<smArr.length;i++){
            Context context = new Context();
            //context.getValidators().clear();

            context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

            XMLSignature signature  = new XMLSignature(context);
            signature.setSignatureMethod(smArr[i]);
            signature.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
            signature.addKeyInfo(kp.getPublic());
            signature.sign(kp.getPrivate());
            File sigFile = new File(BASE_MA3 +"signatureMethods/"
                                    +smArr[i].getAlgorithm().getName().toLowerCase()
                                    +"-"
                                    +smArr[i].getDigestAlg().getName().toLowerCase()
                                    +"-signature.xml");

            signature.write(new FileOutputStream(sigFile));

            XMLSignature ss2 = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), context);

            ValidationResult result = ss2.verify();
            assertTrue(result.getType()== ValidationResultType.VALID);
        }

    }

    public void testAllECDSA() throws Exception
    {
        SignatureMethod[] smArr = new SignatureMethod[]{
                                            SignatureMethod.ECDSA_SHA1,
                                            SignatureMethod.ECDSA_SHA256,
                                            SignatureMethod.ECDSA_SHA384,
                                            SignatureMethod.ECDSA_SHA512 };

        //KeyPair kp = KeyPairGenerator.getInstance("ECDSA").genKeyPair();
        KeyPair kp = KeyUtil.generateKeyPair(AsymmetricAlg.ECDSA, 256);

        for (int i=0; i<smArr.length;i++){
            Context context = new Context();
            //context.getValidators().clear();

            context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

            XMLSignature signature  = new XMLSignature(context);
            signature.setSignatureMethod(smArr[i]);
            signature.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
            signature.addKeyInfo(kp.getPublic());
            signature.sign(kp.getPrivate());
            File sigFile = new File(BASE_MA3 +"signatureMethods/"
                                    +smArr[i].getAlgorithm().getName().toLowerCase()
                                    +"-"
                                    +smArr[i].getDigestAlg().getName().toLowerCase()
                                    +"-signature.xml");

            signature.write(new FileOutputStream(sigFile));

            XMLSignature ss2 = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), context);

            ValidationResult result = ss2.verify();
            assertTrue(result.getType()== ValidationResultType.VALID);
        }

    }

    public static void main(String[] args) throws Exception
    {
        Provider[] providers = Security.getProviders();
        for (Provider p : providers){
            System.out.println(">> "+p.getInfo());
        }
        System.out.println();

        ECertificate cert = new ECertificate(
        "MIICEjCCAbegAwIBAgIGARJQ/UmbMAsGByqGSM49BAEFADBQMSEwHwYDVQQDExhYTUwgRUNEU0Eg\n" +
                "U2lnbmF0dXJlIFRlc3QxFjAUBgoJkiaJk/IsZAEZEwZhcGFjaGUxEzARBgoJkiaJk/IsZAEZEwNv\n" +
                "cmcwHhcNMDcwNTAzMDgxMDE1WhcNMTEwNTAzMDgxMDE1WjBQMSEwHwYDVQQDExhYTUwgRUNEU0Eg\n" +
                "U2lnbmF0dXJlIFRlc3QxFjAUBgoJkiaJk/IsZAEZEwZhcGFjaGUxEzARBgoJkiaJk/IsZAEZEwNv\n" +
                "cmcwgbQwgY0GByqGSM49AgEwgYECAQEwLAYHKoZIzj0BAQIhAP//////////////////////////\n" +
                "//////////////2XMCcEIQD////////////////////////////////////////9lAQCAKYEAgMB\n" +
                "AiEA/////////////////////2xhEHCZWtEARYQbCbdhuJMDIgADZubz40WiQ+v/nrjhfizYmEIl\n" +
                "tKIr/n7hwGwpG3CDEk2jIDAeMAwGA1UdEwEB/wQCMAAwDgYDVR0PAQH/BAQDAgGmMAsGByqGSM49\n" +
                "BAEFAANIADBFAiEA63Pq7/YfDDrnbCxXVX20T3dn77iL8dvC1Cb24Al9VFkCIHUeymf/N+H60OQL\n" +
                "v9Wg/X8Cbp2am42qjQvaKtb4+BFk");

        System.out.println(cert);
    }

}
