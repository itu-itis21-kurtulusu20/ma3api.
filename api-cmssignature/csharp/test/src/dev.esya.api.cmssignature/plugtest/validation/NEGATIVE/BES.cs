using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.asn.util;

/**
 * negative test cases in plugtests
 * @author aslihan.kubilay
 *
 */
namespace dev.esya.api.cmssignature.plugtest.validation.NEGATIVE
{
    [TestFixture]
    public class BES
    {
        private static readonly String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH + "//negative//bes//";
        private static ValidationPolicy POLICY_FILE = null;

        static BES()
        {
            try
            {
                POLICY_FILE = PolicyReader.readValidationPolicy(new FileStream(INPUT_DIR + "policyBES.xml", FileMode.Open, FileAccess.Read));
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Console.WriteLine(e.StackTrace);
            }
        }

        /**
         * This is a negative test case for checking ESSSigningCertificate.
         * This test data has an ESSSigningCertificate attribute however its valid of certHash field 
         * does *NOT* match to the hash value of signer certificate.
         *
         */
        [Test]
        public void testVerifingBES4()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-BESN-4.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            sdvr.printDetails();

            List<SignatureValidationResult> results = sdvr.getSDValidationResults();
            SignatureValidationResult result1 = results[0];

            List<CheckerResult> checkResults = result1.getCheckerResults();

            foreach (CheckerResult checkerResult in checkResults)
            {
                if (checkerResult.getCheckerName().Equals("Signing Certificate Attribute Checker"))
                    Assert.AreEqual(Types.CheckerResult_Status.UNSUCCESS, checkerResult.getResultStatus());
            }

        }


        /**
         * This is a negative test case for checking ESSSigningCertificateV2.
         * This test data has an ESSSigningCertificateV2 attribute with SHA-512 however its valid of 
         * certHash field does *NOT* match to the hash value of signer certificate.
         * 
         */
        [Test]
        public void testVerifingBES5()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-BESN-5.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            sdvr.printDetails();

            List<SignatureValidationResult> results = sdvr.getSDValidationResults();
            SignatureValidationResult result1 = results[0];

            List<CheckerResult> checkResults = result1.getCheckerResults();

            foreach (CheckerResult checkerResult in checkResults)
            {
                if (checkerResult.getCheckerName().Equals("Signing Certificate Attribute Checker"))
                    Assert.AreEqual(Types.CheckerResult_Status.UNSUCCESS, checkerResult.getResultStatus());
            }
        }
    }
}
