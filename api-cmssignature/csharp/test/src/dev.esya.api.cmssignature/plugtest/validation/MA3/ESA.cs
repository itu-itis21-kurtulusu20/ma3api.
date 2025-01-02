using System;
using System.Collections.Generic;
using System.IO;
using dev.esya.api.cmssignature.plugtest.validation;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.validation.plugtests.MA3
{
    [TestFixture]
    public class ESA
    {
        private static readonly String INPUT_DIR = "T://MA3//api-cmssignature//testdata//cmssignature//imza//convertion//plugtests//esa//";
        private static ValidationPolicy POLICY_FILE = null;

        public ESA()
        {
            POLICY_FILE = PolicyReader.readValidationPolicy(new FileStream(CMSValidationTest.INPUT_DIRECTORY_PATH + "ma3//esa//policyArchive.xml", FileMode.Open, FileAccess.Read));
        }

        [Test]
        public void testVerifingESA1()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA2()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA3()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-3.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA4()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-4.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA5()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-5.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA6()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-6.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA7()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-7.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA8()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-8.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
        [Test]
        public static void testVerifingESA9()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-A-9.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            if (sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
                Console.WriteLine("SignedData is verified");
            else
            {
                Console.WriteLine("SignedData is not verified");
                sdvr.printDetails();
            }

        }
    }
}
