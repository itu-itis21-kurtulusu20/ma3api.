package dev.esya.api.xmlsignature.legacy.bes.ma3;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.util.Arrays;

/**
 * @author ahmety
 * date: Oct 12, 2009
 */
public class BES7 extends XMLBaseTest
{
    public void testCreateBes7DataObjectFormat() throws Exception {

        PrivateKey privateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private1.key"));
        ECertificate certificate = ECertificate.readFromFile(BASE_MA3_INCLUDE+"Sertifika1.cer");

        Context context = new Context(BASE_MA3+"XAdES\\BES\\ETSI\\");
        context.setValidateCertificates(false);
        context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        XMLSignature signature  = new XMLSignature(context);
        signature.setSignatureMethod(SignatureMethod.RSA_SHA256);

        // add document
        String refId1 = signature.addDocument(BASE_XAdES+"Data\\ts_101903v010302p.txt", null, false);

        // add content as object with mime and encoding
        String objId1 = signature.addObject("signed data object".getBytes(), "text/plain", "http://www.ietf.org/rfc/rfc2279.txt");
        String refId2 = signature.getSignedInfo().addReference("#"+objId1, null, null, null);

        signature.addKeyInfo(certificate);

        //set place
        signature.getQualifyingProperties().getSignedProperties().getSignedSignatureProperties()
                            .setSignatureProductionPlace(new SignatureProductionPlace(context, "Istanbul Merkez", null, null, null));

        // add data object formats
        DataObjectFormat dof1 =
                new DataObjectFormat(context,
                                     "#"+refId1 /* object reference */,
                                     "Technical Specification" /* description*/,
                                     new ObjectIdentifier(context,
                                                          new Identifier(context, "http://uri.etsi.org/01903/v1.3.2#", null),
                                                          "ETSI TS 101 903 V1.3.2 (2006-03)",
                                                          Arrays.asList("http://www.w3.org/TR/XAdES/", "file:///test.test/schemas/xades.xsd")
                                                          ),
                                     "text/plain" /* mime */,
                                     "http://www.ietf.org/rfc/rfc2279.txt"/* encoding */
                                     );
        DataObjectFormat dof2 =
                new DataObjectFormat(context,
                                     "#"+refId2 /* object reference */,
                                     "Technical Specification" /* description*/,
                                     new ObjectIdentifier(context,
                                                          new Identifier(context, "http://uri.etsi.org/01903/v1.3.2#", null),
                                                          "ETSI TS 101 903 V1.3.2 (2006-03)",
                                                          Arrays.asList("http://www.w3.org/TR/XAdES/", "file:///test.test/schemas/xades.xsd")
                                                          ),
                                     "text/plain" /* mime */,
                                     "http://www.ietf.org/rfc/rfc2279.txt"/* encoding */
                                     );

        SignedDataObjectProperties sdop = new SignedDataObjectProperties(context);
        sdop.addDataObjectFormat(dof1);
        sdop.addDataObjectFormat(dof2);

        signature.getQualifyingProperties().getSignedProperties().setSignedDataObjectProperties(sdop);
        signature.sign(privateKey);

        File sigFile = new File(context.getBaseURIStr() + "X-BES-7.xml");

        signature.write(new FileOutputStream(sigFile));

        Context context2 = new Context(context.getBaseURIStr());
        context2.setValidateCertificates(false);
        context2.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        XMLSignature ss2 = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), context2);

        ValidationResult result = ss2.verify();

        System.out.println(result.toXml());
        assertTrue(result.getType()== ValidationResultType.VALID);

    }


}
