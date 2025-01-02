package dev.esya.api.xmlsignature.legacy.bes;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithCertSerialNo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author ahmety
 * date: Sep 9, 2009
 */
public class BESSignatureAlgorithmsTest extends XMLBaseTest
{
    public static final String BASE_XADES_BES = BASE_MA3 + "XAdES\\BES\\";


    /*
    todo : esya.genel.smartkart.sunpkcs11.kartlar.islem.PKCS11Islemler
        ESYA Sertifika classini görmüyor
     */
    public void testRSASHA256() throws Exception
    {
        //KeyPair kp = KeyPairGenerator.getInstance("RSA").genKeyPair();
        SmartOp kartIslem = new SmartOp(SmartOp.findSlotNumber(CardType.AKIS), CardType.AKIS, "123456");

        byte[] certBytes = kartIslem.getSignCertificates().get(0);
        //sertifika, CardType.AKIS, "123456".toCharArray()
        SmartCard card = new SmartCard(CardType.AKIS);
        long sessionId = card.openSession(SmartOp.findSlotNumber(CardType.AKIS));
        ECertificate sertifika = new ECertificate(certBytes);

        BaseSigner signer = new SCSignerWithCertSerialNo(card, sessionId, kartIslem.getSlot(), sertifika.getSerialNumber().toByteArray(), "RSA-SHA256");

        Context context = new Context();
        context.setValidateCertificates(false);
        context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        XMLSignature signature = new XMLSignature(context);
        signature.setSignatureMethod(SignatureMethod.RSA_SHA256);

        //System.out.println(">>> 1:  "+ new String(signature.write()));

        signature.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
        //System.out.println(">>> 2 : "+ new String(signature.write()));


        //signature.addKeyInfo(kp.getPublic());
        signature.addKeyInfo(sertifika);
        //System.out.println(">>> 3 : "+ new String(signature.write()));

        //signature.sign(kp.getPrivate());
        signature.sign(signer);

        //System.out.println(">>> 4 : "+ new String(signature.write()));

        File sigFile = new File(BASE_XADES_BES + "rsa-sha256-signature.xml");

        signature.write(new FileOutputStream(sigFile));

        XMLSignature ss2 = XMLSignature.parse(
                            new FileDocument(sigFile, "text/xml"), context);

        ValidationResult result = ss2.verify();
        assertTrue(result.getType()== ValidationResultType.VALID);

    }
}
