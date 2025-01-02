package test.esya.api.xmlsignature.attributes;

import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;

import java.util.Arrays;
import java.util.Calendar;

public class TestBESCommitmentTypeAttr extends XMLSignatureTestBase {

    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256 ;
    private RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

    public void test() throws Exception {

        Context context = createContext();

        context.setValidateCertificates(false);

        XMLSignature signature  = new XMLSignature(context);

        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());

        CommitmentTypeIndication cti = createCommitmentTypeIndicationAttr(context);

        SignedDataObjectProperties sdop = new SignedDataObjectProperties(context);

        sdop.addCommitmentTypeIndication(cti);

        signature.getQualifyingProperties().getSignedProperties().setSignedDataObjectProperties(sdop);

        signature.sign(getSignerInterface(signatureAlg, rsapssParams));

        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }

    private CommitmentTypeIndication createCommitmentTypeIndicationAttr(Context context) throws XMLSignatureException {

        CommitmentTypeQualifier ctq = new CommitmentTypeQualifier(context);
        ctq.addContent("Test Signature");

        return new CommitmentTypeIndication(
                context,
                // commitment type id
                new CommitmentTypeId(context,
                        // identifier
                        new Identifier(context,
                                "http://kamusm.bilgem.gov.tr/depo/nesne_belirtec_listesi#2.16.792.1.2.1.1.5.9.2.1",
                                null),
                        // description
                        null,
                        // documentation references
                        null),
                //object references
                null,
                //for all signed data objects ?
                true,
                // commitment type qualifiers
                Arrays.asList(ctq)
        );
    }
}
