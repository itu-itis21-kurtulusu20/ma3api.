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
    public class BES : CMSSignatureTest
    {
        public static String COMPANY;

        private readonly String SigTYPE_PATH;
        private readonly String OUTPUT_PATH;

        public String INPUT_PATH;
        public String mCompany;

        public BES(String company)
        {
            SigTYPE_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-BES.SCOK\\";

            mCompany = company;
            OUTPUT_PATH = SigTYPE_PATH + "TU\\";
            INPUT_PATH = SigTYPE_PATH + mCompany + "\\";
        }

        //todo @Parameters
        public static List<Object[]> data()
        {
            //Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};


            //Object [][]data = new Object [][]{{"UPC"}};

            //									0		1		2		3		4		5		6		7		   8		9		10		11		12		13		
            //Object [][]data = new Object [][]{{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"},{"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};

            //Object [][] data = new Object[][]{ {"TU"}};
            Object[][] data = new[] {new Object[] {"TU"}};
            return data.ToList();
            //return Arrays.asList(data);
        }

        private SignedDataValidationResult testVerifingBES(String file)
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
            TestConstants.writeXml(sdvr, OUTPUT_PATH + "Verification_of_" + mCompany + "_" + xmlFile);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }

        [Test]
        public void testVerifingBES1()
        {
            Console.WriteLine("----------------------testVerifingBES1-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-1.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES2()
        {
            Console.WriteLine("----------------------testVerifingBES2-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-2.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES3()
        {
            Console.WriteLine("----------------------testVerifingBES3-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-3.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        public void testVerifingBES4()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-4.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        /*@Test
	public void testVerifingBES5()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-5.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}*/

        public void testVerifingBES6()
        {
            Console.WriteLine("----------------------testVerifingBES3-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-6.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        public void testVerifingBES7()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-7.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES8()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-8.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        public void testVerifingBES10()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-10.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        public void testVerifingBES11()
        {
            Console.WriteLine("----------------------testVerifingBES11-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-11.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES15()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-15.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingBES16()
        {
            Console.WriteLine("----------------------testVerifingBES4-----------------------");

            SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-16.p7s");

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}