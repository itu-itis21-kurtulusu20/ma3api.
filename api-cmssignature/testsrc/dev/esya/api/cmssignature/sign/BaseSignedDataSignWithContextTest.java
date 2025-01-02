package dev.esya.api.cmssignature.sign;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolverFromCertStore;
import tr.gov.tubitak.uekae.esya.api.signature.config.*;

import java.util.*;

@RunWith(Parameterized.class)
public class BaseSignedDataSignWithContextTest extends CMSSignatureTest {

    private SignatureAlg signatureAlg;
    private RSAPSSParams rsaPSSParams;
    private ESignatureType eSignatureType;

    @Parameterized.Parameters(name = "{0} / {2}")
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESA},

                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESA},

                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESA},

                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESA},

                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESA},

                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESA},

                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESA},

                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_BES},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_EST},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESC},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESX_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESX_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESXLong},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESXLong_Type1},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESXLong_Type2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESAv2},
                {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESA}
        });
    }

    public BaseSignedDataSignWithContextTest(SignatureAlg signatureAlg, RSAPSSParams rsapssParams, ESignatureType eSignatureType) {
        this.signatureAlg = signatureAlg;
        this.rsaPSSParams = rsapssParams;
        this.eSignatureType = eSignatureType;
    }

    public byte[] signWithContext(boolean aIsContentIncluded) throws Exception {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent(), aIsContentIncluded);

        Context context = new Context();
        Config config = new Config();

        CertValidationPolicies certValidationPolicies = new CertValidationPolicies();
        certValidationPolicies.register(null, getPolicy());

        config.setCertificateValidationPolicies(certValidationPolicies);
        config.setTimestampConfig(new TimestampConfig(getTSSettings().getHostUrl(), String.valueOf(getTSSettings().getCustomerID()), getTSSettings().getCustomerPassword(), getTSSettings().getDigestAlg()));

        //EParameters veya ALLEParameters'da karşılığı yok
        //HttpConfig httpConfig = new HttpConfig();
        //httpConfig.setBasicAuthenticationUsername("TestUsername");
        //httpConfig.setBasicAuthenticationPassword("TestPassword");
        //httpConfig.setConnectionTimeoutInMilliseconds(10000);
        //httpConfig.setProxyHost("TestProxyHost");
        //httpConfig.setProxyUsername("TestProxyUsername");
        //httpConfig.setProxyPassword("TestProxyPassword");
        //httpConfig.setProxyPort("TestProxyPort");
        //config.setHttpConfig(httpConfig);

        AlgorithmsConfig algorithmsConfig = new AlgorithmsConfig();
        algorithmsConfig.setDigestAlg(DigestAlg.SHA256);
        //EParameters veya ALLEParameters'da karşılığı yok
        //algorithmsConfig.setSignatureAlg(SignatureAlg.RSA_SHA256);
        algorithmsConfig.setDigestAlgForOCSP(DigestAlg.SHA256);
        config.setAlgorithmsConfig(algorithmsConfig);

        CertificateValidationConfig certificateValidationConfig = new CertificateValidationConfig();
        certificateValidationConfig.setValidateWithResourcesWithinSignature(false);
        certificateValidationConfig.setLastRevocationPeriodInSeconds(1800000);
        certificateValidationConfig.setGracePeriodInSeconds(29000);
        certificateValidationConfig.setValidationProfile(TurkishESigProfile.getSignatureProfileFromName("P2_1"));
        certificateValidationConfig.setValidateCertificateBeforeSigning(false);
        certificateValidationConfig.setUseValidationDataPublishedAfterCreation(false);
        Map<String, String> aCertificateValidationPolicyFiles = new HashMap<>();
        aCertificateValidationPolicyFiles.put("Test", "Test");
        certificateValidationConfig.setCertificateValidationPolicyFile(aCertificateValidationPolicyFiles);
        config.setCertificateValidationConfig(certificateValidationConfig);

        Parameters parameters = new Parameters();
        parameters.setTrustSigningTime(false);
        parameters.setValidateTimestampWhileSigning(false);
        parameters.setForceStrictReferenceUse(false);
        //EParameters veya ALLEParameters'da karşılığı yok. P_POLICY_ID olabilir mi ?
        //parameters.setCheckPolicyUri(false);
        //EParameters veya ALLEParameters'da karşılığı yok. P_ESAV3_ABOVE_EST, P_PARENT_ESA_TIME ?
        //parameters.setUseCAdESATSv2(false);
        //EParameters veya ALLEParameters'da karşılığı yok.
        //parameters.setWriteReferencedValidationDataToFileOnUpgrade(false);
        config.setParameters(parameters);

        context.setConfig(config);

        ValidationInfoResolver vir = new ValidationInfoResolverFromCertStore();
        context.setValidationInfoResolver(vir);

        bs.addSigner(eSignatureType, getSignerCertificate(),  getSignerInterface(signatureAlg, rsaPSSParams), null, context);

        return bs.getEncoded();
    }

    @Test
    public void verifyDataSignedWithContext_Attached() throws Exception {
        byte[] signatureBytes = signWithContext(true);
        ValidationUtil.checkSignatureIsValid(signatureBytes, null);
    }

    @Test
    public void verifyDataSignedWithContext_Detached() throws Exception {
        byte[] signatureBytes = signWithContext(false);
        ValidationUtil.checkSignatureIsValid(signatureBytes, getSimpleContent());
    }
}
