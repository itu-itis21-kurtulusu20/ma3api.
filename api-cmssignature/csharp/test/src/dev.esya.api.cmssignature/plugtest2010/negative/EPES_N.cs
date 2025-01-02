using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010.negative
{
    [TestFixture]
    public class EPES_N : CMSSignatureTest
    {
        private readonly String INPUT_PATH;

        private readonly String SIGNATURE_POLICY_PATH;

        private readonly int[] SIGNATURE_POLICY_OID = new[] {1, 2, 3, 4, 5, 1};

        public EPES_N()
        {
            INPUT_PATH = getDirectory() + "CAdES_InitialPackag\\CAdES-EPESN.SCOK\\ETSI\\";
            SIGNATURE_POLICY_PATH = getDirectory() + "CAdES_InitialPackage\\data\\TARGET-SIGPOL-ETSI3.der";
        }

        private SignedDataValidationResult testVerifingEPES(String file)
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + file);

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            byte[] policyValue = AsnIO.dosyadanOKU(SIGNATURE_POLICY_PATH);
            params_[EParameters.P_POLICY_VALUE] = policyValue;
            params_[EParameters.P_POLICY_ID] = SIGNATURE_POLICY_OID;
            params_[EParameters.P_POLICY_DIGEST_ALGORITHM] = DigestAlg.SHA1;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }

        [Test]
        public void testEPESN_1()
        {
            Console.WriteLine("----------------------testNegativeEPES1-----------------------");

            SignedDataValidationResult sdvr = testVerifingEPES("Signature-C-EPESN-1.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testEPESN_2()
        {
            Console.WriteLine("----------------------testNegativeEPES2-----------------------");

            SignedDataValidationResult sdvr = testVerifingEPES("Signature-C-EPESN-2.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}