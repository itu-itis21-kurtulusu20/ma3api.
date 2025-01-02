using System;
using System.Collections.Generic;
using dev.esya.api.cmssignature.plugtest.validation;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.validation.plugtests.ENT
{
    [TestFixture]
    public class EPES
    {
        private static readonly String INPUT_PATH = CMSValidationTest.INPUT_DIRECTORY_PATH + "//entrust//epes//";
        private static readonly String SIGNATURE_POLICY_FILE = INPUT_PATH + "TARGET-SIGPOL-ETSI3.der";
        private readonly ValidationPolicy POLICY_FILE;

        public EPES()
        {
            POLICY_FILE = PolicyReader.readValidationPolicy(INPUT_PATH + "policyEPES.xml");
        }
        [Test]
        public void testVerifingEPES1()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + "Signature-C-EPES-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;
            params_[EParameters.P_POLICY_VALUE] = signaturePolicyFile;


            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingEPES2()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + "Signature-C-EPES-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;
            params_[EParameters.P_POLICY_VALUE] = signaturePolicyFile;

            var sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}