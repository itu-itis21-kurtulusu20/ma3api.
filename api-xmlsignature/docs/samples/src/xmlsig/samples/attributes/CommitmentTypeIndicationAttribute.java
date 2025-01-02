package xmlsig.samples.attributes;

import org.junit.Test;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CommitmentTypeId;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CommitmentTypeIndication;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CommitmentTypeQualifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.Identifier;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * BES with CommitmentTypeIndication attribute sample
 * @author: suleyman.uslu
 */
public class CommitmentTypeIndicationAttribute extends SampleBase {

    public static final String SIGNATURE_FILENAME = "commitment_type_indication.xml";

    /**
     * Creates BES with CommitmentTypeIndication attribute
     * @throws Exception
     */
    @Test
    public void createBESWithCommitmentTypeIndication() throws Exception
    {
        try {
            // create context with working directory
            Context context = new Context(BASE_DIR);

            // create signature according to context,
            // with default type (XADES_BES)
            XMLSignature signature = new XMLSignature(context);

            // add document
            String ref1 = "#"+signature.addDocument("./sample.txt", "text/plain", true);
            String objId2 = signature.addPlainObject("Test data 1.", "text/plain", null);
            String ref2 = "#"+signature.addDocument("#"+objId2, null, false);

            // add certificate to show who signed the document
            signature.addKeyInfo(CERTIFICATE);

            // add commitment type indication
            signature.getQualifyingProperties().getSignedProperties().createOrGetSignedDataObjectProperties().
                    addCommitmentTypeIndication(createTestCTI(context,ref1,ref2));

            // now sign it by using private key
            signature.sign(PRIVATE_KEY);

            signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
        }
        catch (XMLSignatureException x){
            // cannot create signature
            x.printStackTrace();
        }
        catch (IOException x){
            // probably couldn't write to the file
            x.printStackTrace();
        }
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }

    private CommitmentTypeIndication createTestCTI(Context c, String ref1, String ref2) throws Exception {

        CommitmentTypeId typeId = new CommitmentTypeId(
                c,
                new Identifier(c, "http://uri.etsi.org/01903/v1.2.2#ProofOfOrigin", null),
                "Proof of origin indicates that the signer recognizes to have created, approved and sent the signed data object.",
                Arrays.asList(
                        "http://test.test/commitment1.txt</xades:DocumentationReference",
                        "file:///test/data/xml/commitment2.txt</xades:DocumentationReference",
                        "http://test.test/commitment3.txt</xades:DocumentationReference")
        );

        CommitmentTypeQualifier q1 = new CommitmentTypeQualifier(c);
        q1.addContent("test commitment a");
        q1.addContent(getQualifierSampleContent());
        q1.addContent("test commitment b");
        CommitmentTypeQualifier q2 = new CommitmentTypeQualifier(c, "commitment 2");
        CommitmentTypeQualifier q3 = new CommitmentTypeQualifier(c, "commitment 2");

        return new CommitmentTypeIndication(c, typeId,
                Arrays.asList(ref1, ref2), false,
                Arrays.asList(q1, q2, q3));
    }

    private Element getQualifierSampleContent() throws Exception {

        return stringToElement(
                "<xl:XadesLabs xmlns:xl=\"http://xadeslabs.com/xades\"> \n" +
                        "          <xl:Commitments type=\"ProofOfOrigin\">\n" +
                        "            <xl:Commitment>commitment 1</xl:Commitment>\n" +
                        "            <xl:Commitment>commitment 2</xl:Commitment>\n" +
                        "            <xl:Commitment>commitment 3</xl:Commitment>\n" +
                        "            <xl:Commitment>commitment 4</xl:Commitment>\n" +
                        "          </xl:Commitments>\n" +
                        "</xl:XadesLabs>");
    }

    private Element stringToElement(String aStr) throws Exception{

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new ByteArrayInputStream(aStr.getBytes())).getDocumentElement();
    }
}
