using System;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;



//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.infra.tsclient
{
    /**
     * Zaman Damgasi Sunucusu baglanti ayarlarini tutan siniftir
     *
     * @author zeldal.ozdemir
     */
    public class TSSettings
    {
        private readonly int connectionTimeOut = 15000;
        private readonly String hostUrl;
        private readonly bool useIdentity;
        private readonly int customerID;
        private readonly String customerPassword;
        private readonly DigestAlg digestAlg = DigestAlg.SHA256;


        /**
         * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
         *
         * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
         */
        public TSSettings(String aHostUrl)
        {
            hostUrl = aHostUrl;
        }
        /**
         * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
         *
         * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
         * @param digestAlg        Digest Algoritması
         */
        public TSSettings(String aHostUrl, DigestAlg digestAlg)
        {
            hostUrl = aHostUrl;
            this.digestAlg = digestAlg;

        }
        /**
         * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
         *
         * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
         * @param customerID       Kullanıcı Numarası
         * @param customerPassword Kullanıcı Parolası
         */

        public TSSettings(String aHostUrl, int customerID, String customerPassword)
            : this(aHostUrl)
        {
            useIdentity = true;
            this.customerID = customerID;
            this.customerPassword = customerPassword;
        }
        /**
         * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
         *
         * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
         * @param customerID       Kullanıcı Numarası
         * @param customerPassword Kullanıcı Parolası
         * @param digestAlg        Digest Algoritması
         */

        public TSSettings(String aHostUrl, int customerID, String customerPassword, DigestAlg digestAlg)
            : this(aHostUrl,customerID,customerPassword)
        {
            this.digestAlg = digestAlg;
        }

        /**
    * Time Stampt Server connection parameters
    * @param hostUrl Server address (ex: http://127.0.0.1:8080/zd .....)
    * @param customerID user id
    * @param customerPassword user password
    * @param connectionTimeOut connection time out in miliseconds
    */

        public TSSettings(String hostUrl, int customerID, String customerPassword, int connectionTimeOut)
            : this(hostUrl, customerID, customerPassword)
        {
            this.connectionTimeOut = connectionTimeOut;
        }
        /**
         * Time Stampt Server connection parameters
         * @param hostUrl Server address (ex: http://127.0.0.1:8080/zd .....)
         * @param customerID user id
         * @param customerPassword user password
         * @param connectionTimeOut connection time out in miliseconds
         * @param digestAlg        Digest Algoritması
         */
        public TSSettings(String hostUrl, int customerID, String customerPassword, int connectionTimeOut, DigestAlg digestAlg)
            : this(hostUrl, customerID, customerPassword,connectionTimeOut)
        {
            this.digestAlg = digestAlg;
        }

        /**
         * Zaman Damgasi Sunucusu baglanti parametrelerini olusturur
         *
         * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
         * @param customerID       Kullanici Numarasi
         * @param customerPassword Kullanici Parolasi
         */

        public TSSettings(String hostUrl, int customerID, char[] customerPassword)
            : this(hostUrl, customerID, new String(customerPassword))
        {
        }
        /**
         * Zaman Damgası Sunucusu bağlantı parametrelerini oluşturur
         *
         * @param hostUrl          Sunucu Adresi(http://127.0.0.1:8080/zd ..... gibi)
         * @param customerID       Kullanıcı Numarası
         * @param customerPassword Kullanıcı Parolası
         * @param digestAlg        Digest Algoritması
         */

        public TSSettings(String hostUrl, int customerID, char[] customerPassword, DigestAlg digestAlg)
            : this(hostUrl, customerID, new String(customerPassword))
        {
            this.digestAlg = digestAlg;
        }
        /**
         * Returns the HostUrl of TSSetting
         * @return String
         */
        public DigestAlg DigestAlg
        {
            get { return digestAlg; }
        }

        public String getHostUrl()
        {
            return hostUrl;
        }
        /**
         * Checks whether TSSettings has CustomerID and CustomerPassword.
         * @return True if TSSettings has CustomerID and CustomerPassword,false otherwise.
         */
        public bool isUseIdentity()
        {
            return useIdentity;
        }
        /**
         * Returns the CustomerID of TSSetting
         * @return int
         */
        public int getCustomerID()
        {
            return customerID;
        }
        /**
         * Returns the CustomerPassword of TSSetting
         * @return String
         */
        public String getCustomerPassword()
        {
            return customerPassword;
        }
        /**
         * Returns the ConnectionTimeOut(millisecond) of TSSetting
         * @return int
         */
        public int getConnectionTimeOut()
        {
            return connectionTimeOut;
        }
        /**
         * Returns the DigestAlg of TSSetting
         * @return DigestAlg
         */
        public DigestAlg getDigestAlg()
        {
            return digestAlg;
        }
    }
}