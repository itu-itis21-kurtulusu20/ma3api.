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
 * @author aslihan.kubilay
 *
 */

namespace dev.esya.api.cmssignature.plugtest.validation.NEGATIVE
{
    [TestFixture]
    public class ESA
    {
        private static readonly String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH + "//negative//esa//";
        private static ValidationPolicy POLICY_FILE;

        public ESA()
        {
            POLICY_FILE =
                PolicyReader.readValidationPolicy(new FileStream(INPUT_DIR + "policyArchive.xml", FileMode.Open,
                                                                 FileAccess.Read));
        }


        /**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the SignatureTimeStamp is ulterior than 
	 * the time in ArchiveTimeStamp.
	 * 
	 * 	26-Oct-2008 02:00Z - SignatureTimeStamp (*)
	 *	26-Oct 2008 00:00Z - TimestampedCertsCRLs
	 *	26-Oct 2008 01:00Z - ArchiveTimeStamp
	 * 
	 */

        [Test]
        public void testVerifyingESA1()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-AN-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        /**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the TimestampedCertsCRLs is ulterior than the time in ArchiveTimeStamp.
	 * 
	 * 26-Oct-2008 00:00Z - SignatureTimeStamp
	 * 26-Oct 2008 02:00Z - TimestampedCertsCRLs (*)
	 * 26-Oct 2008 01:00Z - ArchiveTimeStamp
	 */

        [Test]
        public void testVerifyingESA2()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-AN-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }

        /**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the ESCTimeStamp is ulterior than the time in ArchiveTimeStamp.
	 * 
	 * 26-Oct-2008 00:00Z - SignatureTimeStamp
	 * 26-Oct 2008 02:00Z - ESCTimeStamp (*)
	 * 26-Oct 2008 01:00Z - ArchiveTimeStamp
	 * 
	 */

        [Test]
        public void testVerifyingESA3()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-AN-3.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}