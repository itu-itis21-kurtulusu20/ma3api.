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
    public class EST : CMSSignatureTest
    {
        public static String COMPANY;

        private readonly String SigTYPE_INPUT_PATH;
        private readonly String OUTPUT_DIR;

        public String INPUT_PATH;

        private readonly String mCompany;

        public EST(String company)
        {
            SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-T.SCOK\\";
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-T.SCOK\\TU\\";

            mCompany = company;
            INPUT_PATH = SigTYPE_INPUT_PATH + company + "//";
        }


        //todo @Parameters
        public static List<Object[]> data()
        {
            //Object [][]data = new Object [][]{{"ASC"}};
            //Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};
            //									0		1		2		3		4		5		6		7		   8		9		10		11		12		13		
            //Object [][]data = new Object [][]{{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"},{"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
            //Object [][] data = new Object[][]{{"TU"}};
            //return Arrays.asList(data);
            Object[][] data = new[] {new Object[] {"TU"}};
            return data.ToList();
        }

        private SignedDataValidationResult testVerifingEST(String file)
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
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            String xmlFile = file.Replace("\\.p7s", ".xml");
            TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_" + mCompany + "_" + xmlFile);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }

        [Test]
        public void testVerifingEST1()
        {
            Console.WriteLine("----------------------testVerifingESA1-----------------------");

            SignedDataValidationResult sdvr = testVerifingEST("Signature-C-T-1.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}