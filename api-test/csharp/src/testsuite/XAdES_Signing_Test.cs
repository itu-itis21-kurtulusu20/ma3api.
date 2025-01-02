//using System;
//using System.Collections.Generic;
//using System.Globalization;
//using System.IO;
//using tr.gov.tubitak.uekae.esya.api.certificate.validation;
//using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
//using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
//using tr.gov.tubitak.uekae.esya.api.common.bundle;
//using tr.gov.tubitak.uekae.esya.api.xmlsignature;
//using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;
//using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
//using NUnit.Framework;

//namespace tr.gov.tubitak.uekae.esya.api.testsuite
//{
//    [TestFixture]
//    class XAdES_Signing_Test
//    {
//        static Context context = null;
//        static XMLSignature signature = null;
//        static String directory = TestSuiteCommonMethods.FOLDER + "data\\";

//        static MemoryStream signatureBytes = new MemoryStream();


//        [TestFixture]
//        public class XAdES_Signing_Positive
//        {

//            public static Object[] TestCases =
//            {
//                new object[] {"QCA1_1.p12"},
//                new object[] {"QCA1_2.p12"},
//                new object[] {"QCA12.p12 "}

//            };

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx) 
//            {
//                SignWithPfx(pfx);
//                CheckIfValidationResultIsValid();
//            }

//        }

//        [TestFixture]
//        public class XAdES_Signing_Negative_Due_To_Failure_Of_Certificate_Checker
//        {

//            public static Object[] TestCases =
//            {
//                new object[]   {"QCA1_3.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "Certificate key usage field is invalid."}, // Non-repudiation absent
//                new object[]   {"QCA1_4.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no usetr notice in the certificate."}, // CP user notice statement absent
//                new object[]   {"QCA1_5.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no qualified extension in the certificate."}, //ETSI QC statement id absent
//                new object[]   {"QCA1_6.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.QualifiedCertificateChecker", "There is no qualified extension in the certificate."}, //BTK QC Statement id absent
//                new object[]   {"QCA1_8.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateDateChecker", "Certificate is expired"}, //Expired
//                new object[]   {"QCA1_9.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateSignatureChecker", "Certificate Signature is not Validated"} //Signature Forged
//            };


//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String checker, String error) 
//            {
//                SignWithPfx(pfx);
//                CheckIfValidationResultIsInvalidDueToFailureOfCertificateChecker(checker, error);
//            }
//        }

//        [TestFixture]
//        public class XAdES_Signing_Negative_Due_To_Failure_Of_OCSP_Certificate_Checker
//        {
//            public static Object[] TestCases =
//            {
//                 new object[]   {"QCA1_16.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateDateChecker", "Certificate is expired"}, // Expired OCSP Certificate
//                 new object[]   {"QCA1_17.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateSignatureChecker", "Certificate Signature is not Validated"}, // OCSP Certificate Signature Forged
//                 new object[]   {"QCA1_18.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker", "Certificate is revoked in CRL"}, //Revoked OCSP Certificate
//            };


//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String checker, String error)
//            {
//                SignWithPfx(pfx);
//                CheckIfValidationResultIsInvalidDueToFailureOfOCSPCertificateChecker(checker, error);
//            }
//        }


//        [TestFixture]
//        public class XAdES_Signing_Negative_Due_To_Certificate_Revocation_Checker
//        {

//            public static Object[] TestCases =
//             {
//                 new object[]   {"QCA1_10.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker", "Certificate is revoked in CRL"}, //Revoked in CRL
//                 new object[]   {"QCA1_11.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker", "Certificate is revoked in OCSP answer"}, //Revoked in OCSP
//                 new object[]   {"QCA1_12.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker",  "CRL is not found"}, // Expired in CRL
//                 new object[]   {"QCA1_13.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromCRLChecker",  "CRL is not found"}, //CRL Signature Forged
//                 new object[]   {"QCA1_14.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //Expired OCSP Response
//                 new object[]   {"QCA1_15.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //OCSP Response Signature Forged
//                 new object[]   {"QCA1_21.p12", "tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker",  "OCSP answer is not found"}, //Wrong Certificate's OCSP Response
//            };
            

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String checker, String error) 
//            {
//                SignWithPfx(pfx);
//                CheckIfValidationResultIsInvalidDueToFailureOfCertificateRevocationChecker(checker, error);
//            }
//        }

//        [TestFixture]
//        public class XAdES_Signing_Negative_Due_To_Subroot_Failure
//        {

//            public static Object[] TestCases =
//            {
//                new object[]    {"QCA2.p12", "Certificate signature check failure."}, // Subroot Certificate Signature Forged
//                new object[]    {"QCA3.p12", "Certificate is revoked."}, // Subroot Certificate Revoked in CRL
//                new object[]    {"QCA4.p12", "Certificate revocation check failure."}, //Subroot CRL Expired
//                new object[]    {"QCA5.p12", "Certificate revocation check failure."}, // Subroot CRL Signature Forged
//                new object[]    {"QCA6.p12", "Certificate is revoked."}, // Subroot Revoked in OCSP
//                new object[]    {"QCA7.p12", "Certificate revocation check failure."}, // Subroot's OCSP Response Expired
//                new object[]    {"QCA8.p12", "Certificate revocation check failure."}, //Subroot's OCSP Response Signature Forged
//                new object[]    {"QCA9.p12", "Certificate revocation check failure."}, //Subroot's OCSP Certificate Expired
//                new object[]    {"QCA10.p12", "Certificate revocation check failure."}, //Subroot's OCSP Certificate Signature Forged
//                new object[]    {"QCB.p12", "Certificate signature check failure."} //Subroot Certificate Signature Forged
//            };
            

//            [Test, TestCaseSource("TestCases")]
//            public void RunTests(String pfx, String error) 
//            {
//                SignWithPfx(pfx);
//                CheckIfValidationResultIsInvalidDueToFailureOfSubroot(error);
//            }
//        }

//        public static void CheckIfValidationResultIsValid() 
//        {
//            InMemoryDocument xmlDocument = new InMemoryDocument(signatureBytes.ToArray(), directory, "application/xml", null);
//            XMLSignature signature = XMLSignature.parse(xmlDocument, context);
//            ValidationResult validationResult = signature.verify();
//            Assert.IsNotNull(validationResult);

//            string validationMessage = validationResult.ToString();
//            Assert.AreEqual(ValidationResultType.VALID, validationResult.getType(), validationMessage);
//        }

//        public static void CheckIfValidationResultIsInvalidDueToFailureOfCertificateChecker(String checkerName, String errorMessage) 
//        {
//            InMemoryDocument xmlDocument = new InMemoryDocument(signatureBytes.ToArray(), directory, "application/xml", null);
//            XMLSignature signature = XMLSignature.parse(xmlDocument, context);
//            ValidationResult validationResult = signature.verify();
//            Assert.IsNotNull(validationResult);

//            List<CheckResult> selfCheckResultList = ((SignatureValidationResult)validationResult).getCertificateStatusInfo().getDetails();
//            bool checkerFailed = TestSuiteCommonMethods.runCertificateSelfController(selfCheckResultList, checkerName, errorMessage);

//            Assert.IsTrue(checkerFailed);
//        }

//        public static void CheckIfValidationResultIsInvalidDueToFailureOfOCSPCertificateChecker(String checkerName, String errorMessage) 
//        {
//            InMemoryDocument xmlDocument = new InMemoryDocument(signatureBytes.ToArray(), directory, "application/xml", null);
//            XMLSignature signature = XMLSignature.parse(xmlDocument, context);
//            ValidationResult validationResult = signature.verify();
//            Assert.IsNotNull(validationResult);

//            CertificateStatusInfo signingCertficateInfo = ((SignatureValidationResult)validationResult).getCertificateStatusInfo().getOCSPResponseInfoList()[0].getSigningCertficateInfo();
//            bool checkerFailed = TestSuiteCommonMethods.runOCSPCertificateController(signingCertficateInfo, checkerName, errorMessage);

//            Assert.IsTrue(checkerFailed);
//        }

//        public static void CheckIfValidationResultIsInvalidDueToFailureOfCertificateRevocationChecker(String checkerName, String errorMessage) 
//        {
//            InMemoryDocument xmlDocument = new InMemoryDocument(signatureBytes.ToArray(), directory, "application/xml", null);
//            XMLSignature signature = XMLSignature.parse(xmlDocument, context);
//            ValidationResult validationResult = signature.verify();
//            Assert.IsNotNull(validationResult);

//            List<RevocationCheckResult> revocationCheckResultList = ((SignatureValidationResult)validationResult).getCertificateStatusInfo().getRevocationCheckDetails();
//            bool checkerFailed = TestSuiteCommonMethods.runCertificateRevocationController(revocationCheckResultList, checkerName, errorMessage);

//            Assert.IsTrue(checkerFailed);
//        }

//        public static void CheckIfValidationResultIsInvalidDueToFailureOfSubroot(String errorMessage) 
//        {

//            InMemoryDocument xmlDocument = new InMemoryDocument(signatureBytes.ToArray(), directory, "application/xml", null);
//            XMLSignature signature = XMLSignature.parse(xmlDocument, context);
//            ValidationResult validationResult = signature.verify();
//            Assert.IsNotNull(validationResult);

//            //errorIndex must be 1 for the subroot
//            PathValidationRecord pathValidationRecord = ((SignatureValidationResult)validationResult).getCertificateStatusInfo().getValidationHistory()[0];
//            bool checkerFailed = TestSuiteCommonMethods.runCertificateSubrootController(pathValidationRecord, errorMessage);

//            Assert.IsTrue(checkerFailed);
//        }

//        public static void SignWithPfx(String pfx) 
//        {
//            TestSuiteCommonMethods.parsePfx(pfx);

//            PrepareValidationPolicy();
//            I18nSettings.setLocale(new CultureInfo("en-US"));

//            signature = new XMLSignature(context);
//            signature.addDocument(TestSuiteCommonMethods.PLAINFILENAME, TestSuiteCommonMethods.PLAINFILEMIMETYPE, true);
//            signature.addKeyInfo(TestSuiteCommonMethods.certificate);
//            signature.SigningTime = DateTime.Now;
//            signature.sign(TestSuiteCommonMethods.signer);

//            signatureBytes.SetLength(0);
//            signature.write(signatureBytes);
//        }

//        private static void PrepareValidationPolicy()
//        {
//            String xmlConfig = "xmlsignature-config.xml";
//            context = new Context(directory);
//            context.Config = new Config(TestSuiteCommonMethods.FOLDER + "config\\" + xmlConfig);
//        }
//    }
//}




