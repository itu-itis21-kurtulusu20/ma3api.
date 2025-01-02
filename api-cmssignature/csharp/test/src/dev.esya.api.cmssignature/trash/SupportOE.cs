using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Reflection;
using System.Resources;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;


namespace api_cmssignature_test.src.dev.esya.api.cmssignature
{
    [TestFixture]
    public class SupportTest
    { 
        [Test]
        public void testRandom()
        {
            ResourceManager mResources = new ResourceManager("tr.gov.tubitak.uekae.esya.api.common.Properties.Resource", Assembly.GetAssembly(typeof(ESYAException)));
            CultureInfo cultureInfo = I18nSettings.getLocale(); // CultureInfo.GetCultureInfo("en-US");
            string msg = mResources.GetString("SIL_TARIH_KONTROLU", cultureInfo);

            Console.WriteLine(msg);

        }

        [Test]
        public void testTrustedCerts()
        {
            TrustedCertificateFinderFromECertStore store = new TrustedCertificateFinderFromECertStore();
            ParameterList parameterList = new ParameterList();
            parameterList.addParameter("securitylevel", "organizational");
            store.setParameters(parameterList);


            /*ValidationPolicy policy = new ValidationPolicy();
            String policyPath = "D:\\Projeler\\Destek\\SignatureValidationTest\\SignatureValidationTest\\Policies\\1\\certval-policy-malimuhur.xml";
            policy = PolicyReader.readValidationPolicy(policyPath);

            // generate validation system
            ValidationSystem vs = CertificateValidation.createValidationSystem(policy);
            vs.setBaseValidationTime(DateTime.Now);

            store.setParentSystem(vs);*/



            List<ECertificate> trusterCerts = store.findTrustedCertificate();

            foreach (ECertificate cert in trusterCerts)
            {
                Console.WriteLine(cert.ToString());
            }
        }


        [Test]
        public void validateCert()
        {
            ECertificate cert = ECertificate.readFromFile("D:\\Projeler\\Destek\\SignatureValidationTest\\certvalidation\\a1.cer");

            String policyPath = "D:\\Projeler\\Destek\\SignatureValidationTest\\SignatureValidationTest\\Policies\\1\\certval-policy-malimuhur.xml";
            ValidationSystem vs = CertificateValidation.createValidationSystem(PolicyReader.readValidationPolicy(policyPath));
            vs.setBaseValidationTime(DateTime.UtcNow);
            CertificateStatusInfo csi = CertificateValidation.validateCertificate(vs, cert);


            Console.WriteLine(csi.printDetailedValidationReport());



            if (csi.getCertificateStatus() != CertificateStatus.VALID)
                throw new Exception("Not Verified");
        }

        [Test]
        public void testSC()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long sessionID = sc.openSession(1);
            byte [] randBytes = sc.getRandomData(sessionID, 32);

            Console.WriteLine(StringUtil.ToHexString(randBytes));
        }

    }
}
