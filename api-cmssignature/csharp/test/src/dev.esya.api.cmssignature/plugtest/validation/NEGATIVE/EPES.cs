using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtest.validation.NEGATIVE
{
    [TestFixture]
    public class EPES
    {
        private static readonly String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH + "//negative//epes//";
        private static readonly ValidationPolicy POLICY_FILE;
        private static readonly String SIGNATURE_POLICY_FILE = INPUT_DIR + "TARGET-SIGPOL-ETSI3.der";


        static EPES()
        {
            try
            {
                POLICY_FILE =
                    PolicyReader.readValidationPolicy(new FileStream(INPUT_DIR + "policyEPES.xml", FileMode.Open,
                                                                     FileAccess.Read));
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Console.WriteLine(e.StackTrace);
            }
        }

        /**
         * This is a negative test case for checking SignaturePolicyIdentifier. 
         * This test data has an SignaturePolicyIdentifier attribute with explicit 
         * SignaturePolicyId however its valid of SignaturePolicyId.sigPolicyHash field does 
         * *NOT* match to the hash value of signer policy file:TARGET-SIGPOL-ETSI3.der
         * 
         * 
         * 0707070707070707070707070707070707070707 in SignaturePolicyIdentifier attribute (*WRONG HASH*)
         * d5e01820d303705081085410c6ef25ee93dce930 calculated by SignaturePolicy.signPolicyInfo properly
         */

        [Test]
        public void testVerifingEPES1()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-EPESN-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;
            params_[EParameters.P_POLICY_VALUE] = signaturePolicyFile;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        /**
         * Since we dont have signature policy file,we cannot run this one//TODO
         * 
         */

        [Test]
        public void testVerifingEPES2()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-EPESN-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;
            params_[EParameters.P_POLICY_VALUE] = signaturePolicyFile;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}