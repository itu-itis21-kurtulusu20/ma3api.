using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010.negative
{
    [TestFixture]
    public class ESA_N : CMSSignatureTest
    {
        private readonly String INPUT_PATH;

        public ESA_N()
        {
            INPUT_PATH = getDirectory() + "CAdES_InitialPackage//CAdES-AN.SCOK//ETSI//";
        }

        private SignedDataValidationResult testVerifingESA(String file)
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + file);

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }

        [Test]
        public void testVerifingESA1()
        {
            Console.WriteLine("----------------------testVerifingESA1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-AN-1.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA2()
        {
            Console.WriteLine("----------------------testVerifingESA2-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-AN-1.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA3()
        {
            Console.WriteLine("----------------------testVerifingESA3-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-AN-1.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}