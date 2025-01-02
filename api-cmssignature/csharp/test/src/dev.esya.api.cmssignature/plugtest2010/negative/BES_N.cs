using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010.negative
{
    [TestFixture]
    public class BES_N : CMSSignatureTest
    {
        private readonly String INPUT_PATH; // = getDirectory() + "CAdES_InitialPackage//CAdES-BESN.SCOK//ETSI//";

        public BES_N()
        {
            INPUT_PATH = getDirectory() + "CAdES_InitialPackage//CAdES-BESN.SCOK//ETSI//";
        }

        private SignedDataValidationResult testVerifing(String file, ValidationPolicy policy)
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + file);

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }

        [Test]
        public void testBESN_4()
        {
            Console.WriteLine("----------------------testNegativeBES4-----------------------");

            SignedDataValidationResult sdvr = testVerifing("Signature-C-BESN-4.p7s", getPolicy());

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testBESN_5()
        {
            Console.WriteLine("----------------------testNegativeBES5-----------------------");

            SignedDataValidationResult sdvr = testVerifing("Signature-C-BESN-5.p7s", getPolicy());

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }
    }
}