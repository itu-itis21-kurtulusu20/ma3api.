using System;
using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010.validation
{
    //todo @RunWith (value = Parameterized.class)
    [TestFixture]
    public class ESA : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR;

        public String mCompany;

        private readonly String SigTYPE_INPUT_PATH;

        public String INPUT_PATH;


        public ESA(String company)
        {
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-A.SCOK\\TU\\";
            SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-A.SCOK\\";
            Console.WriteLine(company);
            mCompany = company;
            INPUT_PATH = SigTYPE_INPUT_PATH + company + "//";
        }

        //todo @Parameters
        public static List<Object[]> data()
        {
            //Object [] [] data = new Object [][]{{"MSEC"}};
            //Object [][]data = new Object [][]{{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"}};
            //Object [][]data =  {{"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
            //Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};

            //Object [][] data = new Object[][]{{"TU"}};
            //return Arrays.asList(data);

            Object[][] data = new[] {new Object[] {"TU"}};
            return data.ToList();
        }

        private SignedDataValidationResult testVerifingESA(String file)
        {
            byte[] input = null;
            try
            {
                input = AsnIO.dosyadanOKU(INPUT_PATH + file);
            }
            catch (Exception ex)
            {
                input = null;
            }

            //org.junit.Assume.assumeNotNull(input);

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicyNoOCSPNoCRL();

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            String xmlFile = file.Replace("\\.p7s", ".xml");
            TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_" + mCompany + "_" + xmlFile);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }

        [Test]
        public void testVerifingESA1()
        {
            Console.WriteLine("----------------------testVerifingESA1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-1.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA2()
        {
            Console.WriteLine("----------------------testVerifingEPES2-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-2.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA3()
        {
            Console.WriteLine("----------------------testVerifingESA1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-3.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        public void testVerifingESA4()
        {
            Console.WriteLine("----------------------testVerifingEPES2-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-4.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA5()
        {
            Console.WriteLine("----------------------testVerifingESA1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-5.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA6()
        {
            Console.WriteLine("----------------------testVerifingEPES2-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-6.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA7()
        {
            Console.WriteLine("----------------------testVerifingESA1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-7.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA8()
        {
            Console.WriteLine("----------------------testVerifingESA8-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-8.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESA9()
        {
            Console.WriteLine("----------------------testVerifingESA9-----------------------");

            SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-9.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}