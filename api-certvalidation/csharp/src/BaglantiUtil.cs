using System;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.infra.directory;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Utility class for LDAP and HTTP access operations
     * @author IH
     */
    public class BaglantiUtil
    {
        private static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private static readonly String SM_SERTIFIKASI = "cacertificate";
        private static readonly String CRL = "certificaterevocationlist";

        private static String[] msAttributes;

        /**
         * Reads data from the given HTTP address.
         * @param aURLAddress address
         * @return read data
         */
        public static byte[] urldenVeriOku(String aURLAdresi)
        {
            return urldenVeriOku(aURLAdresi, null);
        }

        /**
         * Reads data from the given HTTP address.
         * @param aURLAddress
         * @param timeOut timeout in miliseconds. If it is null, default timeout value is used.
         * @return
         */
        public static byte[] urldenVeriOku(String aURLAdresi, String aTimeOut)
        {
            EWebClient client = new EWebClient();
            if (aTimeOut != null)
            {
                LOGGER.Debug("Timeout is set to " + aTimeOut);
                client.setTimeOut(Convert.ToInt32(aTimeOut));
            }
            client.UseDefaultCredentials = true;
            byte[] data = null;
            try
            {
                data = client.DownloadData(aURLAdresi);
            }
            catch (Exception e)
            {
                LOGGER.Warn(aURLAdresi + " adresinden veri alinamadi", e);
                return null;
            }
            return data;
        }

        public static Stream urldenStreamOku(String aURLAdresi)
        {
            EWebClient client = new EWebClient();
            client.UseDefaultCredentials = true;
            Stream stream = null;
            try
            {
                stream = client.OpenRead(aURLAdresi);
            }
            catch (Exception ex)
            {
                LOGGER.Warn(aURLAdresi + " adresinden veri alinamadi", ex);
                return null;
            }
            return stream;
        }

        /**
         * Reads data from the given LDAP address.
         * @param aDizinAdresi LDAP address     
         * @param aDizinTipi  LDAP Type defined inf DirectoryBase
         * @return read data    
        */
        public static byte[] dizindenVeriOku(String aDizinAdresi, String aDizinTipi)
        {
            Object[][] smcrl = _getLDAP(aDizinAdresi, aDizinTipi);
            if (smcrl == null || smcrl[0][0] == null)
            {
                LOGGER.Error("Dizinden veri okunamadi");
                return null;
            }
            byte[] crlArr = new byte[smcrl.Length];
            crlArr = (byte[]) smcrl[0][0];
            return crlArr;
        }

        private static Object[][] _getLDAP(String aDizinAdresi, String aDizinTipi)
        {
            LdapQueryParser ldapQuery = new LdapQueryParser(aDizinAdresi);

            if (aDizinTipi == null)
            {
                aDizinTipi = DirectoryBase.ACTIVE_DIRECTORY;
            }
            Uri path = ldapQuery.getPath();

            SearchDirectory da = new SearchDirectory(new StaticDirectoryInfo(path.Host,
                                                                             path.Port,
                                                                             aDizinTipi,
                                                                             "",
                                                                             ""), "");

            Object[][] userc = da.getAttributes( /*tka*/path.Query, /*attrs*/ldapQuery.getAttributes());
            return userc;
        }

        // todo bu class biraz eksik gibi, aslinda BaglantiUtil ConnectionUtil bu siniflarda yapisal farklar var,
        // todo JAVA ve .NET yapisal olarak eslenik degil, belki islevsel olarak esleniktir
    }
}