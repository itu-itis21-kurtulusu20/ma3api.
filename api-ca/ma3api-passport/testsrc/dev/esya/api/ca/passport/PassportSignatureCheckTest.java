package dev.esya.api.ca.passport;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.passport.EPassportCVCertificate;
import tr.gov.tubitak.uekae.esya.api.ca.passport.cvc.PassportCVCUtil;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.security.PublicKey;

/**
 * Created by orcun.ertugrul on 19-Dec-17.
 */
public class PassportSignatureCheckTest
{
    @Test
    public void testCASignature() throws Exception
    {
        String folder = "T:\\api-parent\\resources\\unit-test-resources\\passport\\cvc\\";
        byte [] caCertBytes = AsnIO.dosyadanOKU(folder + "cavc.bin");
        EPassportCVCertificate caCert = new EPassportCVCertificate(caCertBytes);

        PublicKey publicKey = PassportCVCUtil.getCAPublicKey(caCert);
        PassportCVCUtil.checkSignature(caCert, publicKey);
    }


    @Test
    public void testDVCertSignature() throws Exception
    {
        String folder = "T:\\api-parent\\resources\\unit-test-resources\\passport\\cvc\\";

        byte [] caCertBytes = AsnIO.dosyadanOKU(folder + "cavc.bin");
        byte [] dvCertBytes = AsnIO.dosyadanOKU(folder + "dvcvc.bin");
        byte [] isCertBytes = AsnIO.dosyadanOKU(folder + "iscvc.bin");

        EPassportCVCertificate caCert = new EPassportCVCertificate(caCertBytes);
        EPassportCVCertificate dvCert = new EPassportCVCertificate(dvCertBytes);
        EPassportCVCertificate isCert = new EPassportCVCertificate(isCertBytes);

        PublicKey publicKey = PassportCVCUtil.getCAPublicKey(caCert);

        PassportCVCUtil.checkSignature(dvCert, publicKey);
    }


    @Test
    public void testISCertSignature() throws Exception
    {
        String folder = "T:\\api-parent\\resources\\unit-test-resources\\passport\\cvc\\";

        byte [] caCertBytes = AsnIO.dosyadanOKU(folder + "cavc.bin");
        byte [] dvCertBytes = AsnIO.dosyadanOKU(folder + "dvcvc.bin");
        byte [] isCertBytes = AsnIO.dosyadanOKU(folder + "iscvc.bin");

        EPassportCVCertificate caCert = new EPassportCVCertificate(caCertBytes);
        EPassportCVCertificate dvCert = new EPassportCVCertificate(dvCertBytes);
        EPassportCVCertificate isCert = new EPassportCVCertificate(isCertBytes);

        PublicKey publicKey = PassportCVCUtil.getPublicKey(caCert, dvCert);

        PassportCVCUtil.checkSignature(isCert, publicKey);
    }

}
