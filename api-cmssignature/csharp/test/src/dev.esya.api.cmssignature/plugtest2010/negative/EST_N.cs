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
    public class EST_N : CMSSignatureTest
    {
        private readonly String INPUT_PATH;

        public EST_N()
        {
            INPUT_PATH = getDirectory() + "CAdES_InitialPackage//CAdES-TN.SCOK//ETSI//";
        }

        private SignedDataValidationResult testVerifingEST(String file)
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
        public void testVerifingEST1()
        {
            Console.WriteLine("----------------------testNegativeESTN1-----------------------");

            SignedDataValidationResult sdvr = testVerifingEST("Signature-C-TN-1.p7s");

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingEST2()
        {
            Console.WriteLine("----------------------testNegativeESTN2-----------------------");

            SignedDataValidationResult sdvr = testVerifingEST("Signature-C-TN-1.p7s");

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingEST3()
        {
            Console.WriteLine("----------------------testNegativeESTN3-----------------------");

            SignedDataValidationResult sdvr = testVerifingEST("Signature-C-TN-3.p7s");

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }
    }
}