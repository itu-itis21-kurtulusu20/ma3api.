package dev.esya.api.xmlsignature.legacy.bes.ma3;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.util.Arrays;

/**
 * @author ahmety
 * date: Sep 25, 2009
 */
public class BES8 extends XMLBaseTest
{
    public void testCreateBes8CommitmentTypeIndication() throws Exception {

        PrivateKey privateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private1.key"));
        ECertificate certificate = ECertificate.readFromFile(BASE_MA3_INCLUDE+"Sertifika1.cer");

        Context context = new Context(BASE_MA3+"XAdES\\BES\\ETSI\\");
        context.setValidateCertificates(false);
        context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        XMLSignature signature  = new XMLSignature(context);
        signature.setSignatureMethod(SignatureMethod.RSA_SHA256);

        signature.addDocument(BASE_XAdES+"Data\\ts_101903v010302p.txt", null, false);

        String objId1 = signature.addObject("signed data object".getBytes(), null, null);
        String objId2 = signature.addObject("signed data object".getBytes(), null, null);

        signature.addKeyInfo(certificate);

        // set uri for signing certificate
        signature.getQualifyingProperties().getSignedProperties()
                .getSignedSignatureProperties().getSigningCertificate()
                .getCertID(0).setURI("..\\..\\..\\include\\Sertifika1.cer");

        CommitmentTypeQualifier ctq1 = new CommitmentTypeQualifier(context);
        ctq1.addContent("test commitment a");
        ctq1.addContent(getQualifierSampleContent());
        ctq1.addContent("test commitment b");

        CommitmentTypeQualifier ctq2 = new CommitmentTypeQualifier(context);
        ctq2.addContent("commitment 2");

        CommitmentTypeQualifier ctq3 = new CommitmentTypeQualifier(context);
        ctq3.addContent("commitment 3");

        CommitmentTypeIndication cti =
                new CommitmentTypeIndication(
                        context,
                        // commitment type id
                        new CommitmentTypeId(context,
                                             // identifier
                                             new Identifier(context,
                                                            "http://uri.etsi.org/01903/v1.2.2#ProofOfOrigin",
                                                            null),
                                             // description
                                             "Proof of origin indicates that the signer recognizes to have created, approved and sent the signed data object.",
                                             // documentation references
                                             Arrays.asList("http://test.test/commitment1.txt",
                                                           "file:///test/data/xml/commitment2.txt", 
                                                           "http://test.test/commitment3.txt")),
                        //object references
                        Arrays.asList(objId1, objId2),
                        //for all signed data objects ?
                        false,
                        // commitment type qualifiers
                        Arrays.asList(ctq1, ctq2, ctq3)
                );

        SignedDataObjectProperties sdop = new SignedDataObjectProperties(context);
        sdop.addCommitmentTypeIndication(cti);
        signature.getQualifyingProperties().getSignedProperties().setSignedDataObjectProperties(sdop);
        signature.sign(privateKey);

        File sigFile = new File(context.getBaseURIStr() + "X-BES-8.xml");

        signature.write(new FileOutputStream(sigFile));

        XMLSignature ss2 = XMLSignature.parse(
                            new FileDocument(sigFile, "text/xml"), context);

        ValidationResult result = ss2.verify();
        assertTrue(result.getType()== ValidationResultType.VALID);

    }

    private Element getQualifierSampleContent() throws Exception
    {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(new ByteArrayInputStream(
                      ("<xl:XadesLabs xmlns:xl=\"http://xadeslabs.com/xades\"> \n" +
                       "  <xl:Commitments type=\"ProofOfOrigin\">\n" +
                       "    <xl:Commitment>commitment 1</xl:Commitment>\n" +
                       "    <xl:Commitment>commitment 2</xl:Commitment>\n" +
                       "    <xl:Commitment>commitment 3</xl:Commitment>\n" +
                       "    <xl:Commitment>commitment 4</xl:Commitment>\n" +
                       "  </xl:Commitments>\n" +
                       "</xl:XadesLabs>\n").getBytes()
            )).getDocumentElement();
    }

}
