package test.esya.api.signature;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.CertValidationReferences;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.CertValidationValues;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampInfo;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;

import java.util.List;

/**
 * @uathor ayetgin
 */
public class AtributesTest extends BaseTest {

    @Test
    public void testSignatureTimestamp() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_T);
        List<TimestampInfo> tsInfo = sc.getSignatures().get(0).getTimestampInfo(TimestampType.SIGNATURE_TIMESTAMP);
        Assert.assertTrue("Bir zaman damgası olmalı", tsInfo.size()==1);
        System.out.println(tsInfo.get(0).getTSTInfo().getTime().getTime());
    }

    @Test
    public void testSigAndRefsTimestamp() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_XL);
        List<TimestampInfo> tsInfo = sc.getSignatures().get(0).getTimestampInfo(TimestampType.SIG_AND_REFERENCES_TIMESTAMP);
        Assert.assertTrue("Bir sigAndRefs zaman damgası olmalı", tsInfo.size()==1);
        System.out.println(tsInfo.get(0).getTSTInfo().getTime().getTime());
    }

    @Test
    public void testReferences() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_C);
        CertValidationReferences refs = sc.getSignatures().get(0).getCertValidationReferences();
        Assert.assertTrue("Bir sil referansı olmalı", refs.getCrlReferences().size()>0);
        Assert.assertTrue("Bir sertifika referansı olmalı", refs.getCertificateReferences().size() > 0);
    }

    @Test
    public void testValues() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_XL);
        CertValidationValues values = sc.getSignatures().get(0).getCertValidationValues();
        Assert.assertTrue("Bir sil olmalı", values.getCrls().size()>0);
        Assert.assertTrue("Bir sertifika olmalı", values.getCertificates().size() > 0);
    }

    @Test
    public void testSignatureAlg() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.UPGRADED_BES_XL);
        SignatureAlg alg = (SignatureAlg)sc.getSignatures().get(0).getSignatureAlg();
        Assert.assertTrue("Imza alg RSA-SHA256 olmalı", alg.equals(SignatureAlg.RSA_SHA256));
    }



}
