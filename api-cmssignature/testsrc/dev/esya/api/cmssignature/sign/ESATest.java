package dev.esya.api.cmssignature.sign;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// Testlerin sırayla çalışmasını istediğimiz için, test metot isimlerinin başına sayı yazıldı.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class ESATest extends CMSSignatureTest {

    ESignatureType eSignatureType;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ESignatureType.TYPE_ESA},
                {ESignatureType.TYPE_ESAv2}
        });
    }

    public ESATest(ESignatureType eSignatureType){
        this.eSignatureType = eSignatureType;
    }

    @Test
    public void _1_testCreateESA_Attached() throws Exception {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(eSignatureType, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    @Test
    public void _2_testCreateESA_Detached() throws Exception {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent(), false);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(eSignatureType, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
    }

    // esa'ya convert için ilk önce xlong imza oluşturmamız gerekiyor
    @Test
    public void _3_testCreateXLONG_Detached() throws Exception {
        BaseSignedData bs = new BaseSignedData();

        File file = new File("T:\\api-parent\\resources\\testdata\\sample.txt");
        ISignable externalContent = new SignableFile(file, 32 * 1024);

        bs.addContent(externalContent, false);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(EParameters.P_TSS_INFO, getTSSettings());
        parameters.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, parameters);

        AsnIO.dosyayaz(bs.getEncoded(), "T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_detached.p7s");

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), externalContent);
    }

    // esa'ya convert için ilk önce xlong imza oluşturmamız gerekiyor
    @Test
    public void _4_testCreateXLONG_Attached() throws Exception {
        BaseSignedData bs = new BaseSignedData();

        File file = new File("T:\\api-parent\\resources\\testdata\\sample.txt");
        ISignable content = new SignableFile(file, 32 * 1024);

        bs.addContent(content);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(EParameters.P_TSS_INFO, getTSSettings());
        parameters.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, parameters);

        AsnIO.dosyayaz(bs.getEncoded(), "T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_attached.p7s");

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    @Test
    public void _5_testConvertESA_Attached() throws Exception {
        byte[] signatureFile = AsnIO.dosyadanOKU("T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_attached.p7s");
        BaseSignedData bs = new BaseSignedData(signatureFile);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(EParameters.P_TSS_INFO, getTSSettings());
        parameters.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.getSignerList().get(0).convert(eSignatureType, parameters);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    @Test
    public void _6_testConvertESA_Detached() throws Exception {
        byte[] signatureFile = AsnIO.dosyadanOKU("T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_detached.p7s");
        BaseSignedData bs = new BaseSignedData(signatureFile);

        File file = new File("T:\\api-parent\\resources\\testdata\\sample.txt");
        ISignable externalContent = new SignableFile(file, 32 * 1024);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(EParameters.P_TSS_INFO, getTSSettings());
        parameters.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        parameters.put(EParameters.P_EXTERNAL_CONTENT, externalContent);

        bs.getSignerList().get(0).convert(eSignatureType, parameters);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), externalContent);
    }
}
