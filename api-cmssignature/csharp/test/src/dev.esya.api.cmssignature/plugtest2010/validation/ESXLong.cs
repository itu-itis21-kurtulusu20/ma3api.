using System;
using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010.validation
{
    //todo @RunWith (value = Parameterized.class)
    [TestFixture]
    public class ESXLong : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR;

        private readonly String SigTYPE_INPUT_PATH;

        public String INPUT_PATH;

        private readonly String mCompany;


        public ESXLong(String company)
        {
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage//CAdES-XL.SCOK//TU//";
            SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage//CAdES-XL.SCOK//";

            Console.WriteLine(company);
            mCompany = company;
            INPUT_PATH = SigTYPE_INPUT_PATH + company + "//";
        }

        //todo @Parameters
        public static List<Object[]> data()
        {
            //		        0		1		2		3		4		   5		6		7		   8		9		10		11		12		13		
            //Object [][]data = new Object [][]{/*{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"}};*/ {"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
            //Object [][]data = new Object [][]{{"ELD"},{"TU"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};

            //Object [][] data = new Object[][]{{"TU"}};
            //return Arrays.asList(data);
            Object[][] data = new[] {new Object[] {"TU"}};
            return data.ToList();
        }

        private SignedDataValidationResult testVerifingESXLong(String file, ValidationPolicy policy)
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
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            String xmlFile = file.Replace("\\.p7s", ".xml");
            TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_" + mCompany + "_" + xmlFile);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }

        [Test]
        public void testVerifingESX1()
        {
            Console.WriteLine("----------------------testVerifingESX1-----------------------");

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-1.p7s", getPolicyNoOCSPNoCRL());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESX2()
        {
            Console.WriteLine("----------------------testVerifingESX2-----------------------");

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-2.p7s", getPolicyNoOCSPNoCRL());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESX3()
        {
            Console.WriteLine("----------------------testVerifingESX3-----------------------");

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-3.p7s", getPolicyNoOCSPNoCRL());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESX4()
        {
            Console.WriteLine("----------------------testVerifingESX4-----------------------");

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-4.p7s", getPolicyNoOCSPNoCRL());

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}