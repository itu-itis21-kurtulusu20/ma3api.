using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using log4net.Config;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.manualExamples
{
    [TestFixture]
    public class ImzaDogrulamaIslemleri
    {
        private readonly String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";
        //private readonly String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
        private readonly String SIGNATURE_FILE = "E:\\tempInput\\imza.txt";
        private readonly String CONTENT_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\test.dat";

        private readonly String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() +
                                                           "testdata\\support\\zeldal.ozdemir#ug.netSIGN0.cer";

        private readonly String NES_SIGNATURE = TestConstants.getDirectory() + "testdata\\support\\nes\\ESXLong-1.p7s";

        [Test]
        public void testImzaDogrulamaIslemleri()
        {
            byte[] signedData = AsnIO.dosyadanOKU(SIGNATURE_FILE);
            TestConstants.setLicence();
            ValidationPolicy policy = TestConstants.getPolicy();
            //PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;

            SignedDataValidation sdv = new SignedDataValidation();

            SignedDataValidationResult sdvr = sdv.verify(signedData, params_);

            if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
                Console.WriteLine("İmzaların hepsi doğrulamadı");

            Console.WriteLine(sdvr.ToString());
        }

        [Test]
        public void testBirImzaDogrulamaIslemleri()
        {
            ECertificate searchingCert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);
            SignedDataValidationResult sdvr = getValidationResult();
            List<SignatureValidationResult> results = sdvr.getSDValidationResults();
            foreach (SignatureValidationResult svr in results)
            {
                ECertificate cert = svr.getSignerCertificate();
                if (searchingCert.Equals(cert))
                {
                    if (svr.getSignatureStatus() != Types.Signature_Status.VALID)
                    {
                        Console.WriteLine("İmza doğrulamadı");
                        Console.WriteLine(svr);
                    }
                }
            }
        }

    
        [Test]
        public void testImzaSonucu()
        {
            BaseSignedData bs = getBaseSignedData();
            SignedDataValidationResult sdvr = getValidationResult();
            Signer firstCounterSigner = bs.getSignerList()[0].getCounterSigners()[0];
            SignatureValidationResult firstCounterSignerVR =
                sdvr.getSDValidationResults()[0].getCounterSigValidationResults()[0];
        }

        public void testOnDogrulama()
        {
            SignedDataValidationResult sdvr = getValidationResult();
            if (sdvr.getSDValidationResults()[0].getValidationState() == ValidationState.PREMATURE)
                Console.WriteLine("Ön doğrulama yapıldı");
        }
        [Test]
        public void testAyrikİmzaninDogrulanmasi()
        {
            byte[] signedData = AsnIO.dosyadanOKU(SIGNATURE_FILE);
            ISignable content = new SignableFile(new FileInfo(CONTENT_FILE));

            ValidationPolicy policy =
                PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;
            params_[EParameters.P_EXTERNAL_CONTENT] = content;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(signedData, params_);

            if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
                Console.WriteLine("İmzaların hepsi doğrulamadı");

            Console.WriteLine(sdvr.ToString());
        }

        public void testSertifikaDogrulama()
        {
            BasicConfigurator.Configure();
            ValidationPolicy policy = PolicyReader.readValidationPolicy(POLICY_FILE);
            ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

            ValidationSystem vs = CertificateValidation.createValidationSystem(policy);
            vs.setBaseValidationTime(DateTime.UtcNow);
            CertificateStatusInfo csi = CertificateValidation.validateCertificate(vs, cert);
            if (csi.getCertificateStatus() != CertificateStatus.VALID)
                Console.WriteLine("Sertifika dogrulanamadi");
        }

        public void testImzaAtan()
        {
            byte[] signedData = AsnIO.dosyadanOKU(NES_SIGNATURE);

            BaseSignedData bsd = new BaseSignedData(signedData);
            ECertificate cert = bsd.getSignerList()[0].getSignerCertificate();

            if (cert == null)
            {
                Console.WriteLine("Imzaci bilgisi yok");
            }
            else
            {
                Console.WriteLine("Isim & Soyisim: " + cert.getSubject().getCommonNameAttribute());
                Console.WriteLine("TC Kimlik No: " + cert.getSubject().getSerialNumberAttribute());
            }
        }


        private BaseSignedData getBaseSignedData()
        {
            // TODO Auto-generated method stub
            return null;
        }

        private SignedDataValidationResult getValidationResult()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}