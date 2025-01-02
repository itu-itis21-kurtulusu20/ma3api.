using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using test.esya.api.cmssignature;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.signature.config;

namespace dev.esya.api.cmssignature.sign
{
    [TestFixture]
    public class BaseSignedDataSignWithContextTest : CMSSignatureTest
    {
        SignatureAlg signatureAlg;
        RSAPSSParams rsaPSSParams;
        ESignatureType eSignatureType;

        public static Object[] TestCases =
        {
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_SHA1,   null, ESignatureType.TYPE_ESA},

            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_SHA256, null, ESignatureType.TYPE_ESA},

            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_SHA384, null, ESignatureType.TYPE_ESA},

            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_SHA512, null, ESignatureType.TYPE_ESA},

            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), ESignatureType.TYPE_ESA},

            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), ESignatureType.TYPE_ESA},

            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384), ESignatureType.TYPE_ESA},

            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_BES},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_EST},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESC},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESX_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESX_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESXLong},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESXLong_Type1},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESXLong_Type2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESAv2},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512), ESignatureType.TYPE_ESA}
        };

        public byte[] signWithContext(bool aIsContentIncluded, SignatureAlg signatureAlg, RSAPSSParams rsaPSSParams, ESignatureType eSignatureType)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), aIsContentIncluded);

            Context context = new Context();
            Config config = new Config();

            CertValidationPolicies certValidationPolicies = new CertValidationPolicies();
            certValidationPolicies.register(null, getPolicy());

            config.setCertificateValidationPolicies(certValidationPolicies);
            config.setTimestampConfig(new TimestampConfig(getTSSettings().getHostUrl(), getTSSettings().getCustomerID().ToString(), getTSSettings().getCustomerPassword(), getTSSettings().getDigestAlg()));

            //EParameters veya ALLEParameters'da karşılığı yok
            //HttpConfig httpConfig = new HttpConfig();
            //httpConfig.BasicAuthenticationUsername = "TestUsername";
            //httpConfig.BasicAuthenticationPassword = "TestPassword";
            //httpConfig.ConnectionTimeoutInMilliseconds = 10000;
            //httpConfig.ProxyHost = "TestProxyHost";
            //httpConfig.ProxyUsername = "TestProxyUsername";
            //httpConfig.ProxyPassword = "TestProxyPassword";
            //httpConfig.ProxyPort = "TestProxyPort";
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
            Dictionary<String, String> aCertificateValidationPolicyFiles = new Dictionary<String, String>();
            aCertificateValidationPolicyFiles.Add("Test", "Test");
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

            bs.addSigner(eSignatureType, getSignerCertificate(), getSignerInterface(signatureAlg, rsaPSSParams), null, context);

            return bs.getEncoded();
        }

        [Test, TestCaseSource("TestCases")]
        public void verifyDataSignedWithContext_Attached(SignatureAlg signatureAlg, RSAPSSParams rsaPSSParams, ESignatureType eSignatureType)
        {
            byte[] signatureBytes = signWithContext(true, signatureAlg, rsaPSSParams, eSignatureType);
            ValidationUtil.checkSignatureIsValid(signatureBytes, null);
        }

        [Test, TestCaseSource("TestCases")]
        public void verifyDataSignedWithContext_Detached(SignatureAlg signatureAlg, RSAPSSParams rsaPSSParams, ESignatureType eSignatureType)
        {
            byte[] signatureBytes = signWithContext(false, signatureAlg, rsaPSSParams, eSignatureType);
            ValidationUtil.checkSignatureIsValid(signatureBytes, getSimpleContent());
        }
    }
}
