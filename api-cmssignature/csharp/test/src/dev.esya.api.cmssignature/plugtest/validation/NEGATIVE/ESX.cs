using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.asn.util;

/**
 * Certificates and revocation references must be in certificate store in order to verify
 * the signature for ESX types
 * @author aslihan.kubilay
 *
 */

namespace dev.esya.api.cmssignature.plugtest.validation.NEGATIVE
{
    [TestFixture]
    public class ESX
    {
        private static readonly String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH + "//negative//esx//";
        private static ValidationPolicy POLICY_FILE;
        private static readonly String CERTS_CRLS_PATH = CMSValidationTest.INPUT_DIRECTORY_PATH + "//certscrls//";

        //Add reference values before running test cases
        static ESX()
        {
            try
            {
                CertStore cs = new CertStore();

                CertStoreCertificateOps cerOps = new CertStoreCertificateOps(cs);
                cerOps.writeCertificate(AsnIO.dosyadanOKU(CERTS_CRLS_PATH + "Signature-C-XN-1.CERT-SIG-LevelA.cer"), 1);
                cerOps.writeCertificate(AsnIO.dosyadanOKU(CERTS_CRLS_PATH + "Signature-C-XN-1.CERT-SIG-LevelB.cer"), 1);
                cerOps.writeCertificate(AsnIO.dosyadanOKU(CERTS_CRLS_PATH + "Signature-C-XN-1.CERT-SIG-Root.cer"), 1);


                CertStoreCRLOps crlOps = new CertStoreCRLOps(cs);
                crlOps.writeCRL(AsnIO.dosyadanOKU(CERTS_CRLS_PATH + "Signature-C-XN-1.CERT-SIG-LevelA-20090206.crl"), 1L);
                crlOps.writeCRL(AsnIO.dosyadanOKU(CERTS_CRLS_PATH + "Signature-C-XN-1.CERT-SIG-LevelB-20090206.crl"), 1L);
                crlOps.writeCRL(AsnIO.dosyadanOKU(CERTS_CRLS_PATH + "Signature-C-XN-1.CERT-SIG-Root-20090206.crl"), 1L);
            }
            catch (Exception e)
            {
                //System.out.println(e.getMessage());
                Console.WriteLine(e.Message);
            }
        }

        public ESX()
        {
            POLICY_FILE =
                PolicyReader.readValidationPolicy(new FileStream(INPUT_DIR + "policyESX.xml", FileMode.Open,
                                                                 FileAccess.Read));
        }

        /**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the SignatureTimeStamp is ulterior than the time 
	 * in TimestampedCertsCRLs
	 * 
	 * ETSI Invalid-Sig Valid-Cert
	 *	26-Oct-2008 01:00Z - SignatureTimeStamp
	 *	26-Oct 2008 00:00Z - TimestampedCertsCRLs
	 *
	 *
	 *	In order to validate the signer certificate,reference values must be added to certstore prior
	 * 
	 */

        [Test]
        public void testVerifyingESX1()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XN-1.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        /**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the SignatureTimeStamp is ulterior than the time in ESCTimeStamp.
	 * 
	 * 
	 * ETSI Invalid-Sig Valid-Cert
	 *  26-Oct-2008 01:00Z - SignatureTimeStamp
	 *  26-Oct 2008 00:00Z - ESCTimeStamp
	 *  
	 *  
	 * In order to validate the signer certificate,reference values must be added to certstore prior
	 */

        [Test]
        public void testVerifyingESX2()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XN-2.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }


        /**
	 * This is a negative test case for TimestampedCertsCRLs.
	 * The hash value of messageImprint in TimestampedCertsCRLs does *NOT* match to the hash value of
	 * corresponding CompleteCertificateRefs and CompleteRevocationRefs.
	 * 
	 * ETSI Invalid-Sig Valid-Cert
	 *	26-Oct-2008 00:00Z - SignatureTimeStamp
	 *	26-Oct 2008 01:00Z - TimestampedCertsCRLs
	 * 
	 * In order to validate the signer certificate,reference values must be added to certstore prior
	 * 
	 */

        [Test]
        public void testVerifyingESX3()
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-XN-3.p7s");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            params_[EParameters.P_CERT_VALIDATION_POLICY] = POLICY_FILE;

            SignedDataValidation sd = new SignedDataValidation();
            SignedDataValidationResult sdvr = sd.verify(input, params_);

            Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
        }
    }
}