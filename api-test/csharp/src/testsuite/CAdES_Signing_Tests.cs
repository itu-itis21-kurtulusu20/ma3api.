using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.testsuite
{

/*
#     API kapsamının dışındaki senaryolar:
#      | QCA1_19.p12 |  Monetary Limit Included             |                                                               |
#      | QCA1_20.p12 |  Usage Limit Included                |                                                               |
#      | QCA11.p12   |  Subroot's OCSP Certificate Revoked  | (SIL ile doğrulanıyor)                                        |
#      | QCB.p12     |  SIGNATURE_CONTROL_FAILURE           | Root Certificate Signature Forged(Subroot ta hata alınıyor)   |
*/
    
    [TestFixture]
    class CAdES_Signing_Tests
    {
        static byte[] signedData;

        static ValidationPolicy validationPolicy = null;
        static Dictionary<String, Object> parameters = new Dictionary<String, Object>();

        [TestFixture]
        public class CAdES_Signing_Positive
        {
            public static Object[] TestCases =
            {
                new object[] {"QCA1_1.p12"},
                new object[] {"QCA1_2.p12"},
                new object[] {"QCA12.p12 "}
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx)
            {
                SignWithPfx(pfx);
                CheckIfValidationResultIsValid();
            }
        }

        [TestFixture]
        public class CAdES_Signing_Negative_Due_To_Failure_Of_Certificate_Checker
        {
            public static Object[] TestCases =         
            {
                new object[] {"QCA1_3.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "Certificate key usage field is invalid."}, // Non-repudiation absent
                new object[] {"QCA1_4.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no usetr notice in the certificate."}, // CP user notice statement absent
                new object[] {"QCA1_5.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no qualified extension in the certificate."}, //ETSI QC statement id absent
                new object[] {"QCA1_6.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no qualified extension in the certificate."}, //BTK QC Statement id absent
                new object[] {"QCA1_8.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateDateChecker", "Certificate is expired"}, //Expired
                new object[] {"QCA1_9.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateSignatureChecker", "Certificate Signature is not Validated"} //Signature Forged
            };

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String checker, String error)
            {
                SignWithPfx(pfx);
                CheckIfValidationResultIsInvalidDueToFailureOfCertificateChecker(checker, error);
            }
        }

        [TestFixture]
        public class CAdES_Signing_Negative_Due_To_Failure_Of_OCSP_Certificate_Checker
        {

            public static Object[] TestCases =
            {
                new object[] {"QCA1_16.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateDateChecker", "Certificate is expired"}, // Expired OCSP Certificate
                new object[] {"QCA1_17.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateSignatureChecker", "Certificate Signature is not Validated"}, // OCSP Certificate Signature Forged
                new object[] {"QCA1_18.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker", "Certificate is revoked in CRL"}, //Revoked OCSP Certificate
            };
            

            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String checker, String error) 
            {
                SignWithPfx(pfx);
                CheckIfValidationResultIsInvalidDueToFailureOfOCSPCertificateChecker(checker, error);
            }
        }

        [TestFixture]
        public class CAdES_Signing_Negative_Due_To_Certificate_Revocation_Checker
        {
            public static Object[] TestCases =
            {
                new object[] {"QCA1_10.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker", "Certificate is revoked in CRL"}, //Revoked in CRL
                new object[] {"QCA1_11.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker", "Certificate is revoked in OCSP answer"}, //Revoked in OCSP
                new object[] {"QCA1_12.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker",  "CRL is not found"}, // Expired in CRL
                new object[] {"QCA1_13.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker",  "CRL is not found"}, //CRL Signature Forged
                new object[] {"QCA1_14.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //Expired OCSP Response
                new object[] {"QCA1_15.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //OCSP Response Signature Forged
                new object[] {"QCA1_21.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //Wrong Certificate's OCSP Response
            };
        
            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String checker, String error)
            {
                SignWithPfx(pfx);
                CheckIfValidationResultIsInvalidDueToFailureOfCertificateRevocationChecker(checker, error);
            }
        }

        [TestFixture]
        public class CAdES_Signing_Negative_Due_To_Subroot_Failure
        {       
            public static Object[] TestCases =
            {
                new object[] {"QCA2.p12", "Certificate signature check failure."}, // Subroot Certificate Signature Forged
                new object[] {"QCA3.p12", "Certificate is revoked."}, // Subroot Certificate Revoked in CRL
                new object[] {"QCA4.p12", "Certificate revocation check failure."}, //Subroot CRL Expired
                new object[] {"QCA5.p12", "Certificate revocation check failure."}, // Subroot CRL Signature Forged
                new object[] {"QCA6.p12", "Certificate is revoked."}, // Subroot Revoked in OCSP
                new object[] {"QCA7.p12", "Certificate revocation check failure."}, // Subroot's OCSP Response Expired
                new object[] {"QCA8.p12", "Certificate revocation check failure."}, //Subroot's OCSP Response Signature Forged
                new object[] {"QCA9.p12", "Certificate revocation check failure."}, //Subroot's OCSP Certificate Expired
                new object[] {"QCA10.p12", "Certificate revocation check failure."}, //Subroot's OCSP Certificate Signature Forged
                new object[] {"QCB.p12", "Certificate signature check failure."} //Subroot Certificate Signature Forged
            };
            
            [Test, TestCaseSource("TestCases")]
            public void RunTests(String pfx, String error) 
            {
                SignWithPfx(pfx);
                CheckIfValidationResultIsInvalidDueToFailureOfSubroot(error);
            }
         }

        private static void PrepareValidationPolicy()
        {
            String rootFolder = TestSuiteCommonMethods.FOLDER;
            FileStream fileInputStream = new FileStream(rootFolder + "config\\policy\\" + "testsuite-policy.xml", FileMode.Open);
            validationPolicy = PolicyReader.readValidationPolicy(fileInputStream);
            parameters[EParameters.P_IGNORE_GRACE] = true;
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = validationPolicy;
            parameters[EParameters.P_FORCE_STRICT_REFERENCE_USE] = true;
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;
            fileInputStream.Close();
        }

        public static void SignWithPfx(String pfx) 
        {
            TestSuiteCommonMethods.parsePfx(pfx);

            PrepareValidationPolicy();
            I18nSettings.setLocale(new CultureInfo("en-US"));

            BaseSignedData baseSignedData = new BaseSignedData();
            ISignable content = new SignableByteArray(Encoding.ASCII.GetBytes("test"));
            baseSignedData.addContent(content);
            baseSignedData.addSigner(ESignatureType.TYPE_BES, TestSuiteCommonMethods.certificate, TestSuiteCommonMethods.signer, null, parameters);

            TestSuiteCommonMethods.signer.reset();
            signedData = baseSignedData.getEncoded();
        }

        public static void CheckIfValidationResultIsValid() 
        {    
            SignedDataValidation signedDataValidation = new SignedDataValidation();
            SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, parameters);

            Console.WriteLine(validationResult);
            Assert.IsNotNull(validationResult);

            SignedData_Status status = validationResult.getSDStatus();
            Assert.AreEqual(SignedData_Status.ALL_VALID, status, validationResult.ToString());
        }

        public static void CheckIfValidationResultIsInvalidDueToFailureOfCertificateChecker(String checkerName, String errorMessage) 
        {
            SignedDataValidation signedDataValidation = new SignedDataValidation();
            SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, parameters);

            Console.WriteLine(validationResult);
            Assert.IsNotNull(validationResult);

            List<CheckResult> selfCheckResultList =
                validationResult.getSDValidationResults()[0].getCertificateStatusInfo().getDetails();

            bool checkerFailed = TestSuiteCommonMethods.runCertificateSelfController(selfCheckResultList, checkerName, errorMessage);

            Assert.True(checkerFailed);
        }

        public static void CheckIfValidationResultIsInvalidDueToFailureOfOCSPCertificateChecker(String checkerName, String errorMessage) 
        {
            SignedDataValidation signedDataValidation = new SignedDataValidation();
            SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, parameters);

            Console.WriteLine(validationResult);
            Assert.IsNotNull(validationResult);

            CertificateStatusInfo signingCertficateInfo = validationResult.getSDValidationResults()[0].getCertificateStatusInfo().getOCSPResponseInfoList()[0].getSigningCertficateInfo();
            bool checkerFailed = TestSuiteCommonMethods.runOCSPCertificateController(signingCertficateInfo, checkerName, errorMessage);

            Assert.True(checkerFailed);
        }

        public static void CheckIfValidationResultIsInvalidDueToFailureOfCertificateRevocationChecker(String checkerName, String errorMessage)
        {
            SignedDataValidation signedDataValidation = new SignedDataValidation();
            SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, parameters);

            Console.WriteLine(validationResult);
            Assert.IsNotNull(validationResult);

            List<RevocationCheckResult> revocationCheckResultList = validationResult.getSDValidationResults()[0].getCertificateStatusInfo().getRevocationCheckDetails();
            bool checkerFailed = TestSuiteCommonMethods.runCertificateRevocationController(revocationCheckResultList, checkerName, errorMessage);

            Assert.True(checkerFailed);
        }

        public static void CheckIfValidationResultIsInvalidDueToFailureOfSubroot(String errorMessage) 
        {
            SignedDataValidation signedDataValidation = new SignedDataValidation();
            SignedDataValidationResult validationResult = signedDataValidation.verify(signedData, parameters);

            Console.WriteLine(validationResult);
            Assert.IsNotNull(validationResult);

            //errorIndex must be 1 for the subroot
            PathValidationRecord pathValidationRecord = validationResult.getSDValidationResults()[0].getCertificateStatusInfo().getValidationHistory()[0];
            bool checkerFailed = TestSuiteCommonMethods.runCertificateSubrootController(pathValidationRecord, errorMessage);

            Assert.True(checkerFailed);
        }
    }
}

