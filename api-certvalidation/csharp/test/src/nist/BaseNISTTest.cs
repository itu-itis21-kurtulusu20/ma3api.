using System;
using System.Collections.Generic;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.asn.x509;
using log4net;
using log4net.Config;


namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    public class BaseNISTTest
    {
        static BaseNISTTest()
        {
            XmlConfigurator.Configure(new FileInfo(@"T:\api-certvalidation\testdata\dotNetApiLogConf.properties"));
        }        
        /*private static readonly String TESTDATADIR = "testData";
        private static readonly String CERTS = "certs";
        private static readonly String POLITIKA = "politika";*/
        public static readonly String SERTIFIKA_DIZIN = @"T:\api-certvalidation\testdata\certificate\validation\nist\certs\";//Directory.GetParent(Environment.CurrentDirectory).Parent.FullName + Path.DirectorySeparatorChar + TESTDATADIR + Path.DirectorySeparatorChar + CERTS + Path.DirectorySeparatorChar; //"E:\\ahmet\\prj\\sunucu\\YeniApi\\ESYA_YENIAPI_int\\MA3\\ESYA_NEWAPI\\testdata\\certificate\\validation\\nist\\certs\\";
        public static readonly String POLITIKA_DIZIN = @"T:\api-certvalidation\testdata\certificate\validation\nist\politika\";//Directory.GetParent(Environment.CurrentDirectory).Parent.FullName + Path.DirectorySeparatorChar + TESTDATADIR + Path.DirectorySeparatorChar + POLITIKA + Path.DirectorySeparatorChar;


        protected Asn1ObjectIdentifier NIST_POLICY_1 = new Asn1ObjectIdentifier(new int[] { 2, 16, 840, 1, 101, 3, 2, 1, 48, 1 });
        protected Asn1ObjectIdentifier NIST_POLICY_2 = new Asn1ObjectIdentifier(new int[] { 2, 16, 840, 1, 101, 3, 2, 1, 48, 2 });
        protected Asn1ObjectIdentifier NIST_POLICY_3 = new Asn1ObjectIdentifier(new int[] { 2, 16, 840, 1, 101, 3, 2, 1, 48, 3 });


        protected class TestResult
        {
            public CertificateStatusInfo mCertificateStatusInfo;
            public List<Asn1ObjectIdentifier> mAuthoritiesConstrainedPolicySet;
            public List<Asn1ObjectIdentifier> mUserConstrainedPolicySet;
            private List<PolicyQualifierInfo> mValidQualifierSet;

            public TestResult(CertificateStatusInfo aCertificateStatusInfo,
                              List<Asn1ObjectIdentifier> aAuthoritiesConstrainedPolicySet,
                              List<Asn1ObjectIdentifier> aUserConstrainedPolicySet,
                              List<PolicyQualifierInfo> aValidQualifierSet)
            {
                mCertificateStatusInfo = aCertificateStatusInfo;
                mAuthoritiesConstrainedPolicySet = aAuthoritiesConstrainedPolicySet;
                mUserConstrainedPolicySet = aUserConstrainedPolicySet;
                mValidQualifierSet = aValidQualifierSet;
            }

            public CertificateStatusInfo getCertificateStatusInfo()
            {
                return mCertificateStatusInfo;
            }

            public List<Asn1ObjectIdentifier> getAuthoritiesConstrainedPolicySet()
            {
                return mAuthoritiesConstrainedPolicySet;
            }

            public List<Asn1ObjectIdentifier> getUserConstrainedPolicySet()
            {
                return mUserConstrainedPolicySet;
            }

            public List<PolicyQualifierInfo> getValidQualifierSet()
            {
                return mValidQualifierSet;
            }
        }

        protected TestResult sertifikaDogrula(String politikaPath, String aCertPath)
        {
            ECertificate sertifika = ECertificate.readFromFile(SERTIFIKA_DIZIN + aCertPath);
            
            ValidationPolicy policy = PolicyReader.readValidationPolicy(POLITIKA_DIZIN + politikaPath);
            ValidationSystem validationSystem = CertificateValidation.createValidationSystem(policy);

            //  crl valid from Apr 19 17:57:20 EEST 2001            
            DateTime? validationDate = new DateTime(2010, 4, 20);//new SimpleDateFormat("dd.MM.yyyy").parse("20.04.2010");            
            validationSystem.setBaseValidationTime(validationDate);

            //validationSystem.setValidCertificateSet(aValidCertificates);

            CertificateController checker = new CertificateController();
            CertificateStatusInfo csi = checker.check(validationSystem, sertifika);

            return new TestResult(csi,
                                  checker.getAuthoritiesConstrainedPolicySet(),
                                  checker.getUserConstrainedPolicySet(),
                                  checker.getValidQualifierSet());
        }

        public void test(String aPolicyPath, String aCertPath, CertificateStatus aExpectedOutcome)
        {
            Assert.AreEqual(aExpectedOutcome, sertifikaDogrula(aPolicyPath, aCertPath)
                                                        .getCertificateStatusInfo().getCertificateStatus());
        }
    }
}
