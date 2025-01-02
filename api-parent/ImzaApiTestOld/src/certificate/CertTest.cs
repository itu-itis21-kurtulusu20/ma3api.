using System;
using System.IO;
using NETAPI_TEST.src.testconstants;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;

namespace NETAPI_TEST.src.certificate
{
    public class CertTest
    {
        private static ITestConstants testconstants = new TestConstants();
       
        
        public static void testGetTCKimlikNo()
        {

            ECertificate cert = new ECertificate(new FileInfo("E:\\tempInput\\NES0.crt"));
           
		    Console.WriteLine(cert.getSubject().getSerialNumberAttribute());
        }
        public static void SertifikaKontrolu()
        {
            
            //add signer
            ECertificate cert = testconstants.getSignerCertificate();
            if (!checkCertificate(cert, testconstants.getPolicy()))
            {
                throw new Exception("Dogrulanamayan sertifika\n" + cert.ToString());
            }
            else
            {
                Console.WriteLine("SERTIFIKA DOGRULAMA BASARILI" + cert.ToString());
            }

        }
        
        public static bool checkCertificate(ECertificate aCert, ValidationPolicy aPolicy)
        {
            //Check Certificate
            //ValidationPolicy policy = POLICY_FILE;
            ValidationSystem cv = CertificateValidation.createValidationSystem(aPolicy);
            cv.setBaseValidationTime(DateTime.UtcNow);
            cv.setLastRevocationTime(aCert.getNotAfter());
            
            CertificateStatusInfo csi = CertificateValidation.validateCertificate(cv, aCert);
            if (csi.getCertificateStatus() != CertificateStatus.VALID)
            {
                Console.WriteLine("Sertifika dogrulanamadi");
                return false;
            }
            return true;
        }
    }
}
