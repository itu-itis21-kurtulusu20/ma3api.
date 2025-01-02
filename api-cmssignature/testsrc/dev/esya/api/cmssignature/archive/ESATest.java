package dev.esya.api.cmssignature.archive;

import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import test.esya.api.cmssignature.validation.ValidationUtil;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.HashMap;


public class ESATest extends CMSSignatureTest {

    String OUTPUT_FOLDER = "T:\\api-cmssignature\\test-output\\java\\esa\\";

    @Test
    public void testCreateXLong() throws Exception {
        BaseSignedData baseSignedData = new BaseSignedData();

        ISignable content = new SignableByteArray("test".getBytes());
        baseSignedData.addContent(content);

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        ECertificate cert = getSignerCertificate();
        BaseSigner signer = getSignerInterface(SignatureAlg.RSA_SHA256);
        baseSignedData.addSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, params);

        byte[] encodedSignature = baseSignedData.getEncoded();

        FileUtil.writeBytes(OUTPUT_FOLDER + "Xlong.p7s", encodedSignature);
        
        ValidationUtil.checkSignatureIsValid(encodedSignature, null);
    }

    @Test
    public void convertXLongToESA() throws Exception
    {
        byte [] signatureBytes = FileUtil.readBytes(OUTPUT_FOLDER + "Xlong.p7s");

        BaseSignedData bsd = new BaseSignedData(signatureBytes);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_FORCE_STRICT_REFERENCE_USE, true);

        bsd.getSignerList().get(0).convert(ESignatureType.TYPE_ESAv2, params);

        FileUtil.writeBytes(OUTPUT_FOLDER + "ESAv2.p7s", bsd.getEncoded());
    }

    @Test
    public void convertESAv2ToESAv2ESAv2() throws Exception
    {
        byte [] signatureBytes = FileUtil.readBytes(OUTPUT_FOLDER + "ESAv2.p7s");

        BaseSignedData bsd = new BaseSignedData(signatureBytes);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_FORCE_STRICT_REFERENCE_USE, true);

        bsd.getSignerList().get(0).convert(ESignatureType.TYPE_ESAv2, params);

        FileUtil.writeBytes(OUTPUT_FOLDER + "ESAv2ESAv2.p7s", bsd.getEncoded());
    }


    @Test
    public void convertESAv2ESAv2To_3_ESAv2() throws Exception
    {
        byte [] signatureBytes = FileUtil.readBytes(OUTPUT_FOLDER + "ESAv2ESAv2.p7s");

        BaseSignedData bsd = new BaseSignedData(signatureBytes);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_FORCE_STRICT_REFERENCE_USE, true);

        bsd.getSignerList().get(0).convert(ESignatureType.TYPE_ESAv2, params);

        FileUtil.writeBytes(OUTPUT_FOLDER + "3_ESAv2.p7s", bsd.getEncoded());
    }

    @Test
    public void convert_ESA101() throws Exception
    {
        byte [] fileBytes = FileUtil.readBytes("C:\\a\\ESA101\\ESA_101.pdf.p7s");

        BaseSignedData bsd = new BaseSignedData(fileBytes);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_FORCE_STRICT_REFERENCE_USE, true);

        bsd.getSignerList().get(0).convert(ESignatureType.TYPE_ESAv2, params);

        FileUtil.writeBytes(OUTPUT_FOLDER + "3_ESAv2.p7s", bsd.getEncoded());
    }

    @Test
    public void validate_ESA101() throws Exception
    {
        //BasicConfigurator.configure();
        byte [] fileBytes = FileUtil.readBytes("C:\\a\\ESA101\\ESA_101.pdf.p7s");
        bundle.esya.api.cmssignature.validation.ValidationUtil.checkSignatureIsValid(fileBytes, null);
    }

}
