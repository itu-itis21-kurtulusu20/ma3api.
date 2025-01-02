using System;
using System.Collections.Generic;
using System.Globalization;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace test.esya.api.cmssignature.parameters
{
    class CurrentTimeParameterTest : CMSSignatureTest
    {
        //OCSP gerçerlilik süresi dolduğu için imza atarken sertifika doğrulamanın başarısız olması bekleniyor.
        [Test]
        public void Signing_with_Expired_OCSP()
        {
            DateTime? cal = DateTime.UtcNow;
            cal = cal.Value.AddDays(2);

            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_CURRENT_TIME] = cal;

            try
            {
                bs.addSigner(ESignatureType.TYPE_BES, 
                    getSignerCertificate(),
                    getSignerInterface(SignatureAlg.RSA_SHA1), 
                    null, 
                    parameters);
            }
            catch (Exception ex)
            {
                CertificateStatusInfo statusInfo = ((CertificateValidationException) ex).getCertStatusInfo();
                PathValidationResult path1Result = statusInfo.getValidationHistory()[0].getResultCode();
                Assert.AreEqual(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);
                return;
            }

            throw new Exception("Function must be failed");
        }

        //OCSP gerçerlilik süresi dolduğu için imza doğrulanırken sertifika doğrulamanın başarısız olması bekleniyor.
        [Test]
        public void Validation_with_Expired_OCSP()
        {
            byte[] signatureBytes = CreateBasicCMSSignature();

            DateTime? cal = DateTime.UtcNow;
            cal = cal.Value.AddDays(2);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_CURRENT_TIME] = cal;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(signatureBytes, parameters);
        
            CertificateStatusInfo statusInfo = sdvr.getSDValidationResults()[0].getCertificateStatusInfo();
            PathValidationResult path1Result = statusInfo.getValidationHistory()[0].getResultCode();
            
            Assert.AreEqual(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);     
        }

        //CRL süresi dolduğu için imza atma sırasında sertifika doğrulamanın başarısız olması bekleniyor
        [Test]
        public void Signing_with_Expired_CRL()
        {
            string pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
            IPfxParser pfxParser = Crypto.getPfxParser();
            pfxParser.loadPfx(pfxPath, "123456");

            ECertificate cert = pfxParser.getFirstCertificate();
            IPrivateKey privateKey = pfxParser.getFirstPrivateKey();

            tr.gov.tubitak.uekae.esya.api.crypto.Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
            signer.init(privateKey);

            //CRL Next Update CRL bitiminden sonra.
            DateTime? cal = DateTime.ParseExact("30/05/2019 13:04", "dd/MM/yyyy HH:mm", new CultureInfo("en-US"), DateTimeStyles.None);
            

            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_CURRENT_TIME] = cal;

            try
            {
                bs.addSigner(ESignatureType.TYPE_BES, cert,signer, null, parameters);
            }
            catch (Exception ex)
            {
                CertificateStatusInfo statusInfo = ((CertificateValidationException)ex).getCertStatusInfo();
                PathValidationResult path1Result = statusInfo.getValidationHistory()[0].getResultCode();
                Assert.AreEqual(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);
                return;
            }

            throw new Exception("Function must be failed");
        }

    

        //CRL süresi dolduğu için imza doğrulama sırasında sertifika doğrulamanın başarısız olması bekleniyor.
        [Test]
        public void Validation_with_Expired_CRL()
        {
            string pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
            IPfxParser pfxParser = Crypto.getPfxParser();
            pfxParser.loadPfx(pfxPath, "123456");

            ECertificate cert = pfxParser.getFirstCertificate();
            IPrivateKey privateKey = pfxParser.getFirstPrivateKey();

            tr.gov.tubitak.uekae.esya.api.crypto.Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
            signer.init(privateKey);


            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, parameters);

            byte[] signatureBytes = bs.getEncoded();

            DateTime? cal = DateTime.ParseExact("30/05/2019 13:04", "dd/MM/yyyy HH:mm", new CultureInfo("en-US"), DateTimeStyles.None);

            parameters[EParameters.P_CURRENT_TIME] = cal;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(signatureBytes, parameters);

            CertificateStatusInfo statusInfo = sdvr.getSDValidationResults()[0].getCertificateStatusInfo();
            PathValidationResult path1Result = statusInfo.getValidationHistory()[0].getResultCode();

            Assert.AreEqual(PathValidationResult.REVOCATION_CONTROL_FAILURE, path1Result);
        }

        //CRL tarihi geçerli olduğu için imza atma sırasında sertifikanın doğrulanması gerekiyor.
        [Test]
        public void Signing_with_Valid_CRL()
        {
            string pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
            IPfxParser pfxParser = Crypto.getPfxParser();
            pfxParser.loadPfx(pfxPath, "123456");

            ECertificate cert = pfxParser.getFirstCertificate();
            IPrivateKey privateKey = pfxParser.getFirstPrivateKey();

            tr.gov.tubitak.uekae.esya.api.crypto.Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
            signer.init(privateKey);

            //CRL geçerlilik tarihi içinde
            DateTime? cal = DateTime.ParseExact("22/04/2019 13:04", "dd/MM/yyyy HH:mm", new CultureInfo("en-US"), DateTimeStyles.None);


            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_CURRENT_TIME] = cal;


            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, parameters);

        }


        //CRL tarihi geçerli olduğu için imza doğrulama sırasında sertifikanın doğrulanması gerekiyor.
        [Test]
        public void Validation_with_Valid_CRL()
        {
            string pfxPath = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_12.p12";
            IPfxParser pfxParser = Crypto.getPfxParser();
            pfxParser.loadPfx(pfxPath, "123456");

            ECertificate cert = pfxParser.getFirstCertificate();
            IPrivateKey privateKey = pfxParser.getFirstPrivateKey();

            tr.gov.tubitak.uekae.esya.api.crypto.Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
            signer.init(privateKey);


            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, parameters);

            byte[] signatureBytes = bs.getEncoded();

            DateTime? cal = DateTime.ParseExact("22/04/2019 13:04", "dd/MM/yyyy HH:mm", new CultureInfo("en-US"), DateTimeStyles.None);

            parameters[EParameters.P_CURRENT_TIME] = cal;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(signatureBytes, parameters);

            Assert.AreEqual(ValidationResultType.VALID, sdvr.getSDValidationResults()[0].getResultType());
        }
    }
}
