package dev.esya.api.asn.passport;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.EncodeMethod;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

import java.io.IOException;

public class ECertificateTest {

    @Test
    public void testTKComplianceStr() throws Exception{
        ECertificate cert = ECertificate.readFromFile("C:\\a\\certs\\user\\OrcunNES0.cer");
        String plainTKComplianceStr = cert.getExtensions().getQCStatements().getPlainTKComplianceStr();
        System.out.println(plainTKComplianceStr);
    }


    @Test
    public void getCertificateTemplate() throws Exception{
        ECertificate cert = ECertificate.readFromFile("C:\\a\\UnitTest\\certs\\tokensigncert.cer");
        String plainTKComplianceStr = cert.getExtensions().getCertificateTemplateOID().toString();
        System.out.println(plainTKComplianceStr);
    }

    @Test
    public void getCertificatePolicies() throws Exception{
        ECertificate cert = ECertificate.readFromFile("C:\\a\\UnitTest\\certs\\tokensigncert.cer");
        String plainTKComplianceStr = cert.getExtensions().getWinApplicationCertificatePolicies().toString();
        System.out.println(plainTKComplianceStr);
    }

    @Test
    public void encodingTestWithBASE64() throws IOException, ESYAException {
        ECertificate eCertificate1 = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");

        byte[] eCertificateEncodedAsBASE64ByteArray = eCertificate1.getEncoded(EncodeMethod.BASE64);
        String eCertificateEncodedAsBASE64 = new String(eCertificateEncodedAsBASE64ByteArray);

        ECertificate eCertificate2 = new ECertificate(eCertificateEncodedAsBASE64);

        boolean equals = eCertificate1.equals(eCertificate2);

        Assert.assertTrue(equals);
    }

    @Test 
    public void encodingTestWithASN1() throws IOException, ESYAException {
        ECertificate eCertificate1 = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");

        byte[] eCertificateEncodedAsASN1 = eCertificate1.getEncoded(EncodeMethod.ASN1);

        ECertificate eCertificate2 = new ECertificate(eCertificateEncodedAsASN1);

        boolean equals = eCertificate1.equals(eCertificate2);

        Assert.assertTrue(equals);
    }

    @Test
    public void BASE64EncodingTestWithgetBase64EncodedMethod() throws IOException, ESYAException {
        ECertificate eCertificate1 = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");

        String base64Encoded = eCertificate1.getBase64Encoded();

        ECertificate eCertificate2 = new ECertificate(base64Encoded);

        boolean equals = eCertificate1.equals(eCertificate2);

        Assert.assertTrue(equals);
    }
}
