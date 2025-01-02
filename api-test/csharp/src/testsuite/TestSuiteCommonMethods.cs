using System;
using System.Collections.Generic;
using System.Reflection;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;


namespace tr.gov.tubitak.uekae.esya.api.testsuite
{
    class TestSuiteCommonMethods
    {

        public static readonly String FOLDER = "T:\\api-test\\";

        public static readonly String PLAINFILENAME = "T:\\api-parent\\resources\\testdata\\sample.txt";
        public static readonly String PLAINPDFFILENAME = "T:\\api-parent\\resources\\testdata\\sample.pdf";
        public static readonly String PLAINFILEMIMETYPE = "text/plain";

        private static String rootFolder = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\";
        private static String pfxPin = "123456";

        public static  Signer signer;
        public static  ECertificate certificate;

        public static bool runCertificateSelfController(List<CheckResult> selfCheckResultList, String checkerName, String errorMessage)
        {
            bool checkerFailed = false;
            String checkText = getChecker(checkerName).getCheckText();

            foreach (CheckResult cr in selfCheckResultList)
            {
                if (compareCheckTexts(cr, checkText, errorMessage))
                    checkerFailed = true;
            }

            return checkerFailed;
        }

        public static bool runOCSPCertificateController(CertificateStatusInfo signingCertficateInfo, String checkerName, String errorMessage)
        {
            RevocationStatus revocationStatus = null;
            RevocationStatusInfo revocationInfo = signingCertficateInfo.getRevocationInfo();
            if (revocationInfo != null)
                revocationStatus = revocationInfo.getRevocationStatus();

            bool checkerFailed = false;
            String checkText = getChecker(checkerName).getCheckText();

            if (revocationStatus != RevocationStatus.REVOKED)
            {
                List<CheckResult> ocspCertificateCheckResultList = signingCertficateInfo.getDetails();
                foreach (CheckResult cr in ocspCertificateCheckResultList)
                {
                    if (compareCheckTexts(cr, checkText, errorMessage))
                        checkerFailed = true;
                }
            }
            else
            {
                List<RevocationCheckResult> revocationCheckDetails = signingCertficateInfo.getRevocationCheckDetails();
                foreach (RevocationCheckResult rcr in revocationCheckDetails)
                {
                    if (compareCheckTexts(rcr, checkText, errorMessage))
                        checkerFailed = true;
                }
            }
            return checkerFailed;
        }

        public static bool runCertificateRevocationController(List<RevocationCheckResult> revocationCheckResultList, String checkerName, String errorMessage)
        {
            bool checkerFailed = false;
            String checkText = getChecker(checkerName).getCheckText();

            foreach (RevocationCheckResult rcr in revocationCheckResultList)
            {
                if (compareCheckTexts(rcr, checkText, errorMessage))
                    checkerFailed = true;
            }

            return checkerFailed;
        }

        public static bool runCertificateSubrootController(PathValidationRecord pathValidationRecord, String errorMessage)
        {
            bool checkerFailed = false;
            String result = pathValidationRecord.getResultCode().getMessage();

            if (pathValidationRecord.getErrorIndex() == 1 && result.Equals(errorMessage))
                checkerFailed = true;

            return checkerFailed;
        }

        public static Checker getChecker(String className)
        {
            Assembly[] assemblies = AppDomain.CurrentDomain.GetAssemblies();
            List<Type> types = new List<Type>();

            foreach (Assembly assembly in assemblies)
            {
                Type type = assembly.GetType(className);
                if (type != null)
                    types.Add(type);
            }

            Checker certificateChecker = (Checker)Activator.CreateInstance(types[0]);
            return certificateChecker;
        }

        public static bool compareCheckTexts(CheckResult checkResult, String checkText, String errorMessage)
        {
            if (checkResult.getCheckText().Equals(checkText))
            {
                String result = checkResult.getResultText();
                if (result.Equals(errorMessage))
                {
                    return true;
                }
            }
            return false;
        }

        public static void parsePfx(String pfx) 
        {
            string pfxFilePath = rootFolder + pfx; 
            PfxParser pfxParser = new PfxParser(pfxFilePath, pfxPin);
            certificate = pfxParser.getCertificate();
            signer = Crypto.getSigner(SignatureAlg.RSA_SHA256);
            signer.init(pfxParser.getPrivateKey());        
        }
    }
}
