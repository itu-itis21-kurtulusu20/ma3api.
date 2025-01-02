package dev.esya.api.xmlsignature.legacy.bes;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.ClaimedRole;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureProductionPlace;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignerRole;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;

/**
 * @author ahmety
 * date: Sep 11, 2009
 */
public class SignedSignaturePropsTest extends XMLBaseTest
{
    public void testSignerRoleClaimed() throws Exception
    {
        // read certificate & private key from file for test
        PrivateKey privateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private1.key"));

        ECertificate certificate = new ECertificate(
                        AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"Sertifika1.cer"));
        String base = BASE_MA3 + "XAdES/BES/";

        // construct certificate
        Context c = new Context(base);
        c.setValidateCertificates(false);
        XMLSignature signature  = new XMLSignature(c);
        signature.setSignatureMethod(SignatureMethod.RSA_SHA256);

        // add data to sign
        signature.addDocument(BASE_MA3_INCLUDE+"SampleData.xml", null, false);

        // add signed signature properties
        SignedSignatureProperties ssp =
                signature.createOrGetQualifyingProperties()
                                        .getSignedProperties()
                                        .getSignedSignatureProperties();
        ssp.setSignerRole(
                new SignerRole(c, new ClaimedRole[] {
                                        new ClaimedRole(c, "Man-ager"),
                                        new ClaimedRole(c, "Zucker")
                                  }
                )
        );
        ssp.setSignatureProductionPlace(new SignatureProductionPlace(
                                      c, "Istanbul", null, "81340", "Turkiye"));
        ssp.setSigningTime(XmlUtil.createDate());

        // add keyinfo & sign
        signature.addKeyInfo(certificate);
        signature.sign(privateKey);

        // write signature to file
        File sigFile = new File(BASE_MA3 + "XAdES/BES/signerRole-signature.xml");
        signature.write(new FileOutputStream(sigFile));

        // read signature from file
        XMLSignature ss2 = XMLSignature.parse(
                    new FileDocument(sigFile, "text/xml"), new Context(base));
        ss2.getContext().setValidateCertificates(false);
        // verify it
        ValidationResult result = ss2.verify();
        assertTrue(result.getType()==ValidationResultType.VALID);


    }
}
