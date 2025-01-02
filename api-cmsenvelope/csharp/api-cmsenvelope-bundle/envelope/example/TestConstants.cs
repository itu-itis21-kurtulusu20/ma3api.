using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;


namespace tr.gov.tubitak.uekae.esya.api.envelope.example
{
    public class TestConstants
    {

        // Şifreleme lisansı API  paketi ile birlikte dağıtılmamaktadır. Lisans almak için lütfen KamuSM'e başvuruda bulununuz.
        private static readonly string LICENSE_PATH = "";
        private static readonly string LICENSE_PWD = "";

        private static readonly string DIRECTORY = "C:\\ma3api-cmsenvelope";
        private static readonly string POLICY_FILE = "\\config\\certval-encryption-policy-test.xml";
     
        public static string GetPolicyFile() 
        {
            throw new ESYAException("Set path for policy file!");
            //return DIRECTORY + POLICY_FILE;
        }

        public static void setLicense()
        {
            throw new ESYAException("Set path and password for license file!");
            //LicenseUtil.setLicenseXml(new FileStream(LICENSE_PATH, FileMode.Open, FileAccess.Read), "61MTy55Fdv7");
        }

        public static ECertificate getReceiverCert()
        {
            // Normalde şifreleme işlemi başka bir kişiye yapılmaktadır. Bu durumda sertifikanın  akıllı kart dışında bir yerden alınması geremektedir.
            // Kullanıcı kendisine şifreleme yapmak istiyorsa akıllı kart içindeki sertifika kullanılabilir.
            SmartCard sc = new SmartCard(CardType.AKIS);
            long session = sc.openSession(1);
            List<byte[]> certs = sc.getEncryptionCertificates(session);
            return new ECertificate(certs[0]);
        }

        public static string getPIN()
        {
            throw new ESYAException("Set PIN of SmartCard!");
            //return "12345";
        }

        public static string getEMailAddressForLDAP()
        {
            throw new ESYAException("Set EMail for LDAP search!");
            //return "test@test.net";
        }
      }      
    }

