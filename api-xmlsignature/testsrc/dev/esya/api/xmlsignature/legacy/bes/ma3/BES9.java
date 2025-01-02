package dev.esya.api.xmlsignature.legacy.bes.ma3;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.IndividualDataObjectsTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.Include;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;

/**
 * @author ahmety
 * date: Oct 12, 2009
 */
public class BES9 extends XMLBaseTest
{
    public void testCreateBes9IndividualDataObjectsTimesStamp() throws Exception {

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

        //set time
        signature.getQualifyingProperties().getSignedProperties().getSignedSignatureProperties().setSigningTime(XmlUtil.createDate());

        // add indivdual data objects timestamp
        SignedDataObjectProperties sdop = new SignedDataObjectProperties(context);

        IndividualDataObjectsTimeStamp timeStamp = new IndividualDataObjectsTimeStamp(context);
        timeStamp.addInclude(new Include(context, "#"+refId1, Boolean.TRUE));
        timeStamp.addInclude(new Include(context, "#"+refId2, Boolean.TRUE));

        timeStamp.addEncapsulatedTimeStamp(signature, DigestMethod.SHA_1, getZDAyar());

        sdop.addIndividualDataObjectsTimeStamp(timeStamp);

        signature.getQualifyingProperties().getSignedProperties().setSignedDataObjectProperties(sdop);
        signature.sign(privateKey);

        File sigFile = new File(context.getBaseURIStr() + "X-BES-9.xml");

        signature.write(new FileOutputStream(sigFile));

        XMLSignature ss2 = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), context);

        ValidationResult result = ss2.verify();

        System.out.println(result.toXml());
        assertTrue(result.getType()== ValidationResultType.VALID);

    }


}
