package test.esya.api.cmssignature.parameters;

import org.junit.Assert;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.CertificateValidationException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CurrentTimeParameterTest extends CMSSignatureTest {

    //OCSP gerçerlilik süresi dolduğu için sertifika doğrulamanın başarısız olması bekleniyor
    @Test
    public void test_Signing_with_Expired_OCSP() throws Exception
    {

        Calendar cal = 	Calendar.getInstance();
        cal.add(Calendar.DATE, 2);

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_CURRENT_TIME, cal);

        try
        {
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(),
                    getSignerInterface(SignatureAlg.RSA_SHA1), null, params);
        }
        catch(Exception ex)
        {
            CertificateStatusInfo statusInfo = ((CertificateValidationException) ex).getCertStatusInfo();
            PathValidationResult path1Result = statusInfo.getValidationHistory().get(0).getResultCode();
            Assert.assertEquals(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);
            return;
        }

        throw new Exception("Function must be failed");
    }

    //OCSP gerçerlilik süresi dolduğu için sertifika doğrulamanın başarısız olması bekleniyor
    @Test
    public void test_Validation_with_Expired_OCSP() throws Exception
    {
        byte [] signatureBytes = createBasicCMSSignature();
        Calendar cal = 	Calendar.getInstance();
        cal.add(Calendar.DATE, 2);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_CURRENT_TIME, cal);

        SignedDataValidation sdv = new SignedDataValidation();
        SignedDataValidationResult sdvr = sdv.verify(signatureBytes, params);


        CertificateStatusInfo statusInfo = sdvr.getSDValidationResults().get(0).getCertificateStatusInfo();
        PathValidationResult path1Result = statusInfo.getValidationHistory().get(0).getResultCode();

        Assert.assertEquals(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);
    }


    //CRL süresi dolduğu için sertifika doğrulamanın başarısız olması bekleniyor
    //QCA1_12 sertifikası için OCSP hizmeti olmadığı için default politika dosyasını kullanabiliriz.
    @Test
    public void test_Signing_with_Expired_CRL() throws Exception
    {
        String pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
        PfxParser pfxParser = new PfxParser(new FileInputStream(pfxPath), "123456");

        ECertificate cert = pfxParser.getFirstCertificate();
        PrivateKey privateKey = pfxParser.getFirstPrivateKey();

        Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
        signer.init(privateKey);

        //CRL Next Update CRL bitiminden sonra.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse("30/05/2019 13:04"));

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_CURRENT_TIME, cal);

        try
        {
            bs.addSigner(ESignatureType.TYPE_BES, cert,
                    signer, null, params);
        }
        catch(Exception ex)
        {
            CertificateStatusInfo statusInfo = ((CertificateValidationException) ex).getCertStatusInfo();
            PathValidationResult path1Result = statusInfo.getValidationHistory().get(0).getResultCode();
            Assert.assertEquals(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);
            return;
        }

        throw new Exception("Function must be failed");
    }



    //CRL süresi dolduğu için sertifika doğrulamanın başarısız olması bekleniyor
    @Test
    public void test_Validation_with_Expired_CRL() throws Exception
    {
        String pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
        PfxParser pfxParser = new PfxParser(new FileInputStream(pfxPath), "123456");

        ECertificate cert = pfxParser.getFirstCertificate();
        PrivateKey privateKey = pfxParser.getFirstPrivateKey();

        Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
        signer.init(privateKey);

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        bs.addSigner(ESignatureType.TYPE_BES,
                cert,
                signer,
                null,
                params);

        byte [] signatureBytes = bs.getEncoded();

        //CRL Next Update CRL bitiminden sonra.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse("30/05/2019 13:04"));

        params.put(EParameters.P_CURRENT_TIME, cal);

        SignedDataValidation sdv = new SignedDataValidation();
        SignedDataValidationResult sdvr = sdv.verify(signatureBytes, params);


        CertificateStatusInfo statusInfo = sdvr.getSDValidationResults().get(0).getCertificateStatusInfo();
        PathValidationResult path1Result = statusInfo.getValidationHistory().get(0).getResultCode();

        Assert.assertEquals(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);
    }


    @Test
    public void test_Signing_with_Valid_CRL() throws Exception
    {
        String pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
        PfxParser pfxParser = new PfxParser(new FileInputStream(pfxPath), "123456");

        ECertificate cert = pfxParser.getFirstCertificate();
        PrivateKey privateKey = pfxParser.getFirstPrivateKey();

        Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
        signer.init(privateKey);

        //CRL Next Update CRL bitiminden sonra.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse("22/04/2019 13:04"));

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_CURRENT_TIME, cal);


        bs.addSigner(ESignatureType.TYPE_BES, cert,
                    signer, null, params);
    }

    @Test
    public void test_Validation_with_Valid_CRL() throws Exception
    {
        String pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
        PfxParser pfxParser = new PfxParser(new FileInputStream(pfxPath), "123456");

        ECertificate cert = pfxParser.getFirstCertificate();
        PrivateKey privateKey = pfxParser.getFirstPrivateKey();

        Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
        signer.init(privateKey);

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        bs.addSigner(ESignatureType.TYPE_BES,
                cert,
                signer,
                null,
                params);

        byte [] signatureBytes = bs.getEncoded();

        //CRL Next Update CRL bitiminden sonra.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse("22/04/2019 13:04"));

        params.put(EParameters.P_CURRENT_TIME, cal);

        SignedDataValidation sdv = new SignedDataValidation();
        SignedDataValidationResult sdvr = sdv.verify(signatureBytes, params);

        Assert.assertEquals(ValidationResultType.VALID, sdvr.getSDValidationResults().get(0).getResultType());
    }
}
