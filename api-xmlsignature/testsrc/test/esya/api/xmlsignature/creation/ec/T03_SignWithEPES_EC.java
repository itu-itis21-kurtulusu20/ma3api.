package test.esya.api.xmlsignature.creation.ec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.w3c.dom.Document;
import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by sura.emanet on 31.12.2019.
 */
@RunWith(Parameterized.class)
public class T03_SignWithEPES_EC extends XMLSignatureTestBase {

    public static final int[] OID_POLICY_P2 = new int[]{2,16,792,1,61,0,1,5070,3,1,1};

    public static String POLICY_FILE = "T:/api-parent/resources/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf";

    public static OfflineResolver POLICY_RESOLVER;

    static
    {
        POLICY_RESOLVER = new OfflineResolver();
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.1.1", POLICY_FILE, "text/plain");
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.2.1", POLICY_FILE, "text/plain");
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.3.1", POLICY_FILE, "text/plain");
    }

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T03_SignWithEPES_EC(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }


    @Test
    public void testCreateEnveloping() throws Exception
    {
        Context context = createContext();
        context.addExternalResolver(POLICY_RESOLVER);
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);

        signature.addKeyInfo(getECSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.setPolicyIdentifier(OID_POLICY_P2,
                "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
                "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
        );

        signature.sign(getECSignerInterface(signatureAlg));
        signature.upgrade(SignatureType.ES_T);
        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void testCreateEnveloped()  throws Exception
    {
        Document envelopeDoc = createSimpleEnvelope();
        Context context =  createContext();
        context.addExternalResolver(POLICY_RESOLVER);
        context.setDocument(envelopeDoc);

        XMLSignature signature = new XMLSignature(context, false);
        envelopeDoc.getDocumentElement().appendChild(signature.getElement());
        signature.addDocument("#data1","text/xml",false);
        signature.addKeyInfo(getECSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.setPolicyIdentifier(OID_POLICY_P2,
                "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
                "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
        );
        signature.sign(getECSignerInterface(signatureAlg));
        signature.upgrade(SignatureType.ES_T);

        Source source = new DOMSource(envelopeDoc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, new StreamResult(signatureBytes));

        XMLValidationUtil.checkSignatureIsValid( BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void testCreateDetached()  throws Exception
    {
        Context context = createContext();
        context.addExternalResolver(POLICY_RESOLVER);
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE,false);
        signature.addKeyInfo(getECSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.setPolicyIdentifier(OID_POLICY_P2,
                "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
                "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
        );
        signature.sign(getECSignerInterface(signatureAlg));

        signature.write(signatureBytes);
        signature.upgrade(SignatureType.ES_T);

        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }
}
