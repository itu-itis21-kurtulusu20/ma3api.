using System;
using tr.gov.tubitak.uekae.esya.api.infra.directory;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.tools;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find
{
    public class EGuvenCertificateFinder:ICertificateFinder{

        public static String EGUVEN_LDAP_IP = "85.235.93.62";
        public byte[] find(String certSerial)
        {
            String dizinAdresi = "ldap://" + EGUVEN_LDAP_IP + "/CERTIFICATESERIALNUMBER=" + certSerial + ",C=TR,dc=e-guven,dc=com?userCert?sub?(objectClass=*)";
            byte[] bytes = BaglantiUtil.dizindenVeriOku(dizinAdresi,null);
            if (bytes == null)
            {
                if (certSerial.StartsWith("00"))
                {
                    String serialOther = certSerial.Substring(2);
                    dizinAdresi = "ldap://" + EGUVEN_LDAP_IP + "/CERTIFICATESERIALNUMBER=" + serialOther + ",C=TR,dc=e-guven,dc=com?userCert?sub?(objectClass=*)";
                    bytes = BaglantiUtil.dizindenVeriOku(dizinAdresi,null);
                }
            }
            return bytes;
        }
    }
}
