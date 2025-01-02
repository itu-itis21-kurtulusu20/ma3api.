using System;
using System.Collections.Generic;
using System.IO;
using dev.esya.api.cmssignature.plugtest.validation;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.validation.plugtests.ENT
{
    [TestFixture]
    public class BES
    {
        private static readonly String INPUT_PATH = CMSValidationTest.INPUT_DIRECTORY_PATH + "entrust//bes//";
        private static readonly ValidationPolicy POLICY_FILE;

        static BES()
        {
            try
            {
                POLICY_FILE =
                    PolicyReader.readValidationPolicy(new FileStream(INPUT_PATH + "policyBES.xml", FileMode.Open,
                                                                     FileAccess.Read));
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Console.WriteLine(e.StackTrace);
            }
            catch (ESYAException e)
            {
                // TODO Auto-generated catch block
                Console.WriteLine(e.StackTrace);
            }
        }

        [Test]
        public void testVerifingBES1()
        {
            Console.WriteLine("----------------------testVerifingBES1-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + "Signature-C-BES-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES2()
        {
            Console.WriteLine("----------------------testVerifingBES2-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + "Signature-C-BES-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES3()
        {
            Console.WriteLine("----------------------testVerifingBES3-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + "Signature-C-BES-3.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES4()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + "Signature-C-BES-4.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

            //POLICY_FILE.close();
        }

        [Test]
        public void testVerifingBES11()
        {
            Console.WriteLine("----------------------testVerifingBES11-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + "Signature-C-BES-11.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

            //POLICY_FILE.close();
        }

        [Test]
        public void testVerifingBES15()
        {
            Console.WriteLine("----------------------testVerifingBES15-----------------------");
            CMSSignatureTest testConstants = new CMSSignatureTest();
            String ma3Bes = testConstants.getDirectory() + "creation\\plugtests\\bes\\";

            ValidationPolicy ma3POLICY_FILE = testConstants.getPolicy();

            byte[] input = AsnIO.dosyadanOKU(ma3Bes + "Signature-C-BES-15.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = ma3POLICY_FILE;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}