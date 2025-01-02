using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010.validation
{
    //todo @RunWith (value = Parameterized.class)
    [TestFixture]
    public class ESX : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR;

        private readonly String SigTYPE_INPUT_PATH;

        public String INPUT_PATH;

        private readonly String mCompany;


        public ESX(String company)
        {
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-X.SCOK\\TU\\";
            SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-X.SCOK\\";

            Console.WriteLine(company);
            mCompany = company;
            INPUT_PATH = SigTYPE_INPUT_PATH + company + "//";
        }

        //todo @Parameters
        public static List<Object[]> data()
        {
            //Object [][]data = new Object [][]{{"DIC"}};
            //									   0		1		2		3		4		5		6		7		   8		9		10		11		12		13		
            //Object [][]data = new Object [][]{/*{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"},{"ENI"},*/{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};

            //Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};
            //Object [][] data = new Object[][]{{"TU"}};
            //return Arrays.asList(data);
            Object[][] data = new[] {new Object[] {"TU"}};
            return data.ToList();
        }

        private SignedDataValidationResult testVerifingESX(String file, Dictionary<String, Object> params_)
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

            Dictionary<String, Object> params_ = getParams("", "crl", getPolicyNoOCSPNoCRL());

            SignedDataValidationResult sdvr = testVerifingESX("Signature-C-X-1.p7s", params_);


            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESX2()
        {
            Console.WriteLine("----------------------testVerifingESX2-----------------------");

            Dictionary<String, Object> params_ = getParams("", "crl", getPolicyNoOCSPNoCRL());

            SignedDataValidationResult sdvr = testVerifingESX("Signature-C-X-2.p7s", params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESX3()
        {
            Console.WriteLine("----------------------testVerifingESX3-----------------------");

            Dictionary<String, Object> params_ = getParams("3", "der", getPolicyNoOCSPNoCRL());

            SignedDataValidationResult sdvr = testVerifingESX("Signature-C-X-3.p7s", params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESX4()
        {
            Console.WriteLine("----------------------testVerifingESX4-----------------------");

            Dictionary<String, Object> params_ = getParams("4", "der", getPolicyNoOCSPNoCRL());

            SignedDataValidationResult sdvr = testVerifingESX("Signature-C-X-4.p7s", params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        private Dictionary<String, Object> getParams(String searchStr, String fileType, ValidationPolicy policy)
        {
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;

            List<String> reqFiles = new List<String>();
            DirectoryInfo f = new DirectoryInfo(INPUT_PATH);
            FileInfo[] fileNames = f.GetFiles();
            foreach (FileInfo file in fileNames)
            {
                if (file.FullName.Contains(searchStr) && file.FullName.Contains(fileType))
                    reqFiles.Add(file.FullName);
            }

            if (fileType.Contains("crl"))
            {
                List<ECRL> crlList = new List<ECRL>();
                foreach (String files in reqFiles)
                {
                    byte[] crlBytes = AsnIO.dosyadanOKU(INPUT_PATH + files);
                    ECRL ecrl = new ECRL(crlBytes);
                    crlList.Add(ecrl);
                }
                params_[EParameters.P_INITIAL_CRLS] = crlList;
            }
            else if (fileType.Contains("der"))
            {
                List<EOCSPResponse> ocsplist = new List<EOCSPResponse>();
                foreach (String files in reqFiles)
                {
                    byte[] ocspBytes = AsnIO.dosyadanOKU(INPUT_PATH + files);
                    EBasicOCSPResponse eBasicOcsp = new EBasicOCSPResponse(ocspBytes);
                    EOCSPResponse eocsp =
                        new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                           new ResponseBytes(new[] {1, 2}, eBasicOcsp.getBytes())));
                    ocsplist.Add(eocsp);
                }
                params_[EParameters.P_INITIAL_OCSP_RESPONSES] = ocsplist;
            }
            return params_;
        }
    }
}