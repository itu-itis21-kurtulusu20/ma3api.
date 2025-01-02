package dev.esya.api.xmlsignature.legacy.bes.ma3;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.AllDataObjectsTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;

/**
 * @author ahmety
 * date: Oct 12, 2009
 */
public class BES10 extends XMLBaseTest
{
    public void testCreateBes10AllDataObjectsTimesStamp() throws Exception {

        PrivateKey privateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private1.key"));

        ECertificate certificate = ECertificate.readFromFile(BASE_MA3_INCLUDE+"Sertifika1.cer");

        Context context = new Context(BASE_MA3+"XAdES\\BES\\ETSI\\");
        context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        XMLSignature signature  = new XMLSignature(context);
        signature.setSignatureMethod(SignatureMethod.RSA_SHA256);

        signature.addDocument(BASE_XAdES + "Data\\ts_101903v010302p.txt", null, false);

        String objId1 = signature.addObject("signed data object".getBytes(), null, null);
        String objId2 = signature.addObject("signed data object".getBytes(), null, null);

        String refId1 = signature.getSignedInfo().addReference("#"+objId1, null, null, null);
        String refId2 = signature.getSignedInfo().addReference("#"+objId2, null, null, null);

        signature.addKeyInfo(certificate);

        SignedDataObjectProperties sdop = new SignedDataObjectProperties(context);

        AllDataObjectsTimeStamp adots = new AllDataObjectsTimeStamp(context, signature, DigestMethod.SHA_1, getZDAyar());

        sdop.addAllDataObjectsTimeStamp(adots);

        signature.getQualifyingProperties().getSignedProperties().setSignedDataObjectProperties(sdop);
        signature.sign(privateKey);

        File sigFile = new File(context.getBaseURIStr() + "X-BES-10.xml");

        signature.write(new FileOutputStream(sigFile));

        XMLSignature ss2 = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), context);

        ValidationResult result = ss2.verify();

        System.out.println(result.toXml());
        assertTrue(result.getType()== ValidationResultType.VALID);

    }

}
