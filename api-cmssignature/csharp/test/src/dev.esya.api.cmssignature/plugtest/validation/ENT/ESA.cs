using System;
using System.Collections.Generic;
using System.IO;
using dev.esya.api.cmssignature.plugtest.validation;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

/**
 * Since archive signatures are verified, all validation data necessary for signature
 * verification must be in signeddata. So in policy file,no certificate,crl or ocsp finder
 * is specified
 * 
 * @author aslihan.kubilay
 *
 */
namespace dev.esya.api.cmssignature.validation.plugtests.ENT
{
    [TestFixture]
    public class ESA
    {
        private static readonly String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH + "entrust//esa//";
        public ValidationPolicy POLICY_FILE;

        public ESA()
        {
            POLICY_FILE = PolicyReader.readValidationPolicy(new FileStream(INPUT_DIR + "policyArchive.xml", FileMode.Open, FileAccess.Read));
            //BasicConfigurator.configure();
        }


        [Test]
        public void testVerifingESA1()
        {
            Console.WriteLine("----------------------testVerifingESA1-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            //System.out.println(sdvr.toString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }
        [Test]
        public void testVerifingESA2()
        {
            Console.WriteLine("----------------------testVerifingESA2-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
        [Test]
        public void testVerifingESA3()
        {
            Console.WriteLine("----------------------testVerifingESA3-----------------------");

            //BasicConfigurator.configure();

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-3.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }
        [Test]
        public void testVerifingESA4()
        {
            Console.WriteLine("----------------------testVerifingESA4-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-4.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }
        [Test]
        public void testVerifingESA5()
        {
            Console.WriteLine("----------------------testVerifingESA5-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-5.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }


        [Test]
        public void testVerifingESA6()
        {
            Console.WriteLine("----------------------testVerifingESA6-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-6.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }

        [Test]
        public void testVerifingESA7()
        {
            Console.WriteLine("----------------------testVerifingESA7-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-7.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }
        [Test]
        public void testVerifingESA8()
        {
            Console.WriteLine("----------------------testVerifingESA8-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-8.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }
        [Test]
        public void testVerifingESA9()
        {
            Console.WriteLine("----------------------testVerifingESA9-----------------------");

            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-9.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }
    }
}
