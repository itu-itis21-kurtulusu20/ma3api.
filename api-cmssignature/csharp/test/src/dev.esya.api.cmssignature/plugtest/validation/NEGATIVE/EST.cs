using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

/**
 * negative test cases in plugtests
 * 
 * @author aslihan.kubilay
 *
 */

namespace dev.esya.api.cmssignature.plugtest.validation.NEGATIVE
{
    [TestFixture]
    public class EST
    {
        private static readonly String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH + "//negative//est//";
        private static readonly ValidationPolicy POLICY_FILE;


        static EST()
        {
            try
            {
                POLICY_FILE =
                    PolicyReader.readValidationPolicy(new FileStream(INPUT_DIR + "policyEST.xml", FileMode.Open,
                                                                     FileAccess.Read));
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Console.WriteLine(e.StackTrace);
            }
        }

        /**
	 * This is a negative test case for verifying signer certificate at the time in SignatureTimeStamp.
	 * At the time in SignatureTimeStamp, the signer certificate has been already expired.
	 * 
	 * ETSI Invalid-Cert Expired
	 *  26-Oct-2008 10:29Z - signer certificate expired
	 *  26-Oct 2008 11:00Z - SignatureTimeStamp
	 *  
	 *  
	 * Since we dont have a valid crl for the time in signaturetimestamp, the certificate validation
	 * will fail also //TODO
	 * 
	 */
        [Test]
        public void testVerifyingEST1()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-TN-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        /**
	 * This is a negative test case for verifying signer certificate at the time in SignatureTimeStamp.
	 * At the time in SignatureTimeStamp, the signer certificate has been already revoked.
	 *
	 *
	 *  ETSI Invalid-Cert Revoked SN:51ABEDC95B57
	 *  25-Oct-2008 10:15Z - signer certificate revoked SN:51ABEDC95B57
     *  26-Oct 2008 00:00Z - SignatureTimeStamp
     *  
     *  
     * Since we dont have a valid crl for the time in signaturetimestamp, the certificate validation
	 * will fail also //TODO
	 */
        [Test]
        public void testVerifyEST2()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-TN-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        /**
	 * This is a negative test case for SignatureTimeStamp.
	 * The hash value of messageImprint in SignatureTimeStamp does *NOT* match to the hash value of corresponding signature value of signerInfo.
	 * 
	 * 
	 * 
	 * Since we dont have a valid crl for the time in signaturetimestamp, the certificate validation
	 * will fail also //TODO
	 */
        [Test]
        public void verifyEST3()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-TN-3.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}