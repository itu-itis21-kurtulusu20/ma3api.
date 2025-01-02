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
    public class ESX_N : CMSSignatureTest
    {
        private readonly String INPUT_PATH;

        public ESX_N()
        {
            INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-XN.SCOK\\ETSI\\";
        }

        private SignedDataValidationResult testVerifingESX(String file)
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
        public void testVerifingESX1()
        {
            Console.WriteLine("----------------------testNegativeESX1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESX("Signature-C-XN-1.p7s");

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }


        [Test]
        public void testVerifingESX2()
        {
            Console.WriteLine("----------------------testNegativeESX1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESX("Signature-C-XN-2.p7s");

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }


        [Test]
        public void testVerifingESX3()
        {
            Console.WriteLine("----------------------testNegativeESX1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESX("Signature-C-XN-3.p7s");

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }
    }
}