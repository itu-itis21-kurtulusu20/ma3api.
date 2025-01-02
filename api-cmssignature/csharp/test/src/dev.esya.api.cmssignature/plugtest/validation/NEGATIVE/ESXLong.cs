using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtest.validation.NEGATIVE
{
    [TestFixture]
    public class ESXLong
    {
        private static readonly String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH + "//negative//esxlong//";
        private static ValidationPolicy POLICY_FILE = null;


        public ESXLong()
        {
            POLICY_FILE = PolicyReader.readValidationPolicy(new FileStream(INPUT_DIR + "policyESXLong.xml", FileMode.Open, FileAccess.Read));
        }
        /**
         * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
         * In this test case, The crlIdentifier.issuedTime field does *NOT* match to the thisUpdate of 
         * corresponding CRL in the RevocationValues.
         * 
         */
        [Test]
        public void testVerifyingESXLong1()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XLN-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        /**
         * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues.
         * In this test case, The crlIdentifier.crlNumber field does *NOT* match to the crlNumber extension of 
         * corresponding CRL in the RevocationValues.
         * 
         */
        [Test]
        public void testVerifyingESXLong2()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XLN-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }


        /**
         * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
         * In this test case, The crlHash field does *NOT* match to the hash value of corresponding CRL in the RevocationValues.
         * @throws Exception
         */
        [Test]
        public void testVerifyingESXLong3()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XLN-3.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }

        /**
         * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
         * In this test case, The ocspIdentifier.responderID field does *NOT* match to the responderID field of 
         * corresponding BasicOCSPResponse in the RevocationValues.
         * 
         */
        [Test]
        public void testVerifyingESXLong4()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XLN-4.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }


        /**
         * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
         * In this test case, The ocspIdentifier.producedAt field does *NOT* match to the producedAt field of corresponding 
         * BasicOCSPResponse in the RevocationValues.
         * 
         */
        [Test]
        public void testVerifyingESXLong5()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XLN-5.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }

        /**
         * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
         * In this test case, The ocspRepHash field does *NOT* match to the hash value of corresponding BasicOCSPResponse in the RevocationValues.
         * 
         */
        [Test]
        public void testVerifyingESXLong6()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XLN-6.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

        }
    }
}
