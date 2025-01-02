using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;
using log4net;
using System.DirectoryServices;
using tr.gov.tubitak.uekae.esya.api.common.license;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.infra.directory
{
    /**
     * <p>Title: MA3 Dizin islemleri</p>
     * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
     * classlar bu pakette bulunmaktadir. DizinBase classi, dizine baglantiyi yapan,
     * ve diger classlarin ortak ozelliklerini icinde toplayan ana classdir.
     * Diger classlar bu classi exten etmelidirler.</p>
     * <p>Copyright: Copyright (c) 2002</p>
     * <p>Company: TUBITAK/UEKAE</p>
     * @author M. Serdar SORAN
     * @version 1.0
     *
     */
    public class DirectoryBase
    {
        protected /*DirContext*/DirectorySearcher mConnection = null;
        protected DirectoryInfo mDirectoryInfo = null;
        //protected bool mConnected = false;
        private readonly String mMessage = null;
        private Dictionary<String, String> mTemp_hash;

        //protected String mDizinTipi = null;

        //Buraya yeni bir sey eklenirse asagidaki attribute adlari da buna gore update edilmeli...
        public readonly static String ACTIVE_DIRECTORY = "Active Directory";
        public readonly static String NETSCAPE = "Netscape";
        public readonly static String CRITICAL_PATH = "Critical Path";

        public readonly static String ATTR_CN = "cn";
        public readonly static String ATTR_O = "o";
        public readonly static String ATTR_OU = "ou";
        public readonly static String ATTR_INSTANCETYPE = "instanceType";
        public readonly static String ATTR_OBJECTCLASS = "objectClass";
        public readonly static String ATTR_CAPRAZSERTIFIKA = "crossCertificatePair";
        public readonly static String ATTR_KULLANICISERTIFIKASI = "userCert";
        public readonly static String ATTR_YETKISERTIFIKASI = "attributeCertificateAttribute";
        public readonly static String ATTR_SMSERTIFIKASI = "cacertificate";
        public readonly static String ATTR_ARL = "authorityRevocationList";
        public readonly static String ATTR_CRL = "certificateRevocationList";
        public readonly static String ATTR_ACRL = "attributeCertificateRevocationList";
        public readonly static String ATTR_DELTACRL = "deltaRevocationList";
        public readonly static String ATTR_MAIL = "mail";
        public readonly static String ATTR_PAROLAESKIMESURESI = "businessCategory";
        public readonly static String ATTR_PAROLAUZUNUGU = "description";
        public readonly static String ATTR_PAROLABUYUK = "street";
        public readonly static String ATTR_PAROLAKUCUK = "st";
        public readonly static String ATTR_PAROLAALFANUM = "postalCode";
        public readonly static String ATTR_PAROLARAKAM = "postOfficeBox";
        public readonly static String ATTR_SN = "sn";
        public readonly static String ATTR_SAMACCOUNTNAME = "sAMAccountName";
        public readonly static String ATTR_GIVENNAME = "givenName";
        public readonly static String ATTR_USERACCOUNTCONTROL = "userAccountControl";
        public readonly static String ATTR_NAME = "Name";
        public readonly static String ATTR_OBJECTGUID = "ObjectGUID";

        public readonly static String ATTR_OSVERSION = "operatingSystemVersion";
        public readonly static String ATTR_OS = "operatingSystem";
        public readonly static String ATTR_DNSHOSTNAME = "dNSHostName";
        public readonly static String ATTR_COUNTRYCODE = "countryCode";
        public readonly static String ATTR_DN = "distinguishedName";
        public readonly static String ATTR_OSSERVICEPACK = "operatingSystemServicePack";


        //   ** @todo Buradaki hashtable size'lari optimize edilmeli... */
        //

        private static Dictionary<String, String> msHashAD = new Dictionary<String, String>(10);
        private static Dictionary<String, String> msHashNetscape = new Dictionary<String, String>(10);
        private static Dictionary<String, String> msHashCP = new Dictionary<String, String>(10);

        //private static final Logger LOGCU = Logger.getLogger(DirectoryBase.class);
        private static readonly ILog LOGCU = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        //protected static java.util.ResourceBundle msRB = java.util.ResourceBundle.getBundle("tr.gov.tubitak.uekae.esya.api.common.bundle.GenelBundle"
        //        , java.util.Locale.getDefault());


        public static String[] getAttributeList(String aDizinTipi)
        {
            Dictionary<String, String> temp_hash;
            List<String> v = new List<String>();
            //IEnumerator<String> en;
            String[] r;

            if (aDizinTipi.Equals(ACTIVE_DIRECTORY))
            {
                temp_hash = msHashAD;
            }
            else if (aDizinTipi.Equals(NETSCAPE))
            {
                temp_hash = msHashNetscape;
            }
            else if (aDizinTipi.Equals(CRITICAL_PATH))
            {
                temp_hash = msHashCP;
            }
            else
            {
                //throw new Exception("CIDDI HATA - "+msRB.getString(GenelDil.LDAP_TIP_HATALI));
                throw new Exception("CIDDI HATA - " + "LDAP_TYPE_ERROR");
            }

            //en = temp_hash.keys();
            Dictionary<String, String>.KeyCollection en = temp_hash.Keys;
            //if(en != null)
            //{
            //    while (en.hasMoreElements())
            //    {
            //        v.add(en.nextElement());
            //    }
            //}
            return en.ToArray();
        }


        static DirectoryBase()
        {
            //Active directory attribute isimlerini yazalim...
            msHashAD[ATTR_INSTANCETYPE]= "instanceType";
            msHashAD[ATTR_SAMACCOUNTNAME]= "sAMAccountName";
            msHashAD[ATTR_GIVENNAME]= "givenName";
            msHashAD[ATTR_CN]= "cn";
            msHashAD[ATTR_SN]= "sn";
            msHashAD[ATTR_O]= "o";
            msHashAD[ATTR_OU]= "ou";
            msHashAD[ATTR_OBJECTCLASS]= "objectClass";
            msHashAD[ATTR_CAPRAZSERTIFIKA]= "crossCertificatePair";
            msHashAD[ATTR_KULLANICISERTIFIKASI]= "userCertificate";
            msHashAD[ATTR_YETKISERTIFIKASI]= "attributeCertificateAttribute";  // todo check
            msHashAD[ATTR_SMSERTIFIKASI]= "caCertificate";
            msHashAD[ATTR_ARL]= "authorityRevocationList";
            msHashAD[ATTR_CRL]= "certificateRevocationList";
            msHashAD[ATTR_ACRL]= "attributeCertificateRevocationList";   // todo check
            msHashAD[ATTR_DELTACRL]= "deltaRevocationList";
            msHashAD[ATTR_MAIL]= "mail";
            msHashAD[ATTR_PAROLAESKIMESURESI]= "businessCategory";
            msHashAD[ATTR_PAROLAUZUNUGU]= "description";
            msHashAD[ATTR_PAROLABUYUK]= "street";
            msHashAD[ATTR_PAROLAKUCUK]= "st";
            msHashAD[ATTR_PAROLAALFANUM]= "postalCode";
            msHashAD[ATTR_PAROLARAKAM]= "postOfficeBox";
            msHashAD[ATTR_USERACCOUNTCONTROL]= "UserAccountControl";
            msHashAD[ATTR_NAME]= "Name";
            msHashAD[ATTR_OBJECTGUID]= "ObjectGUID";
            msHashAD[ATTR_OSVERSION]= "operatingSystemVersion";
            msHashAD[ATTR_OS]= "operatingSystem";
            msHashAD[ATTR_DNSHOSTNAME]= "dNSHostName";
            msHashAD[ATTR_COUNTRYCODE]= "countryCode";
            msHashAD[ATTR_DN]= "distinguishedName";
            msHashAD[ATTR_OSSERVICEPACK]= "operatingSystemServicePack";

            //netscape attribute isimlerini yazalim
            msHashNetscape[ATTR_GIVENNAME]= "givenName";

            msHashNetscape[ATTR_CN]= "cn";
            msHashNetscape[ATTR_SN]= "sn";
            msHashNetscape[ATTR_O]= "o";
            msHashNetscape[ATTR_OU]= "ou";
            msHashNetscape[ATTR_OBJECTCLASS]= "objectClass";
            msHashNetscape[ATTR_CAPRAZSERTIFIKA]= "crossCertificatePair";
            msHashNetscape[ATTR_KULLANICISERTIFIKASI]= "userCertificate";
            msHashNetscape[ATTR_YETKISERTIFIKASI]= "attributeCertificateAttribute"; // todo check
            msHashNetscape[ATTR_SMSERTIFIKASI]= "cACertificate";
            msHashNetscape[ATTR_CRL]= "certificateRevocationList";
            msHashNetscape[ATTR_ACRL]= "attributeCertificateRevocationList"; // todo check
            msHashNetscape[ATTR_DELTACRL]= "deltaRevocationList";
            msHashNetscape[ATTR_MAIL]= "mail";
            msHashNetscape[ATTR_PAROLAESKIMESURESI]= "businessCategory";
            msHashNetscape[ATTR_PAROLAUZUNUGU]= "description";
            msHashNetscape[ATTR_PAROLABUYUK]= "street";
            msHashNetscape[ATTR_PAROLAKUCUK]= "st";
            msHashNetscape[ATTR_PAROLAALFANUM]= "postalCode";
            msHashNetscape[ATTR_PAROLARAKAM]= "postOfficeBox";
            msHashNetscape[ATTR_NAME]= "Name";

            //critical path attribute isimlerini yazalim
            msHashCP[ATTR_GIVENNAME]= "givenName";
            msHashCP[ATTR_CN]= "cn";
            msHashCP[ATTR_SN]= "sn";
            msHashCP[ATTR_O]= "o";
            msHashCP[ATTR_OU]= "ou";
            msHashCP[ATTR_OBJECTCLASS]= "objectClass";
            msHashCP[ATTR_CAPRAZSERTIFIKA]= "crossCertificatePair;binary";
            msHashCP[ATTR_KULLANICISERTIFIKASI]= "userCertificate;binary";
            msHashCP[ATTR_YETKISERTIFIKASI]= "attributeCertificateAttribute;binary"; // todo check
            msHashCP[ATTR_SMSERTIFIKASI]= "caCertificate;binary";
            msHashCP[ATTR_CRL]= "certificateRevocationList;binary";
            msHashCP[ATTR_ACRL]= "attributeCertificateRevocationList;binary"; // todo check
            msHashCP[ATTR_DELTACRL]= "deltaRevocationList;binary";
            msHashCP[ATTR_MAIL]= "mail";
            msHashCP[ATTR_PAROLAESKIMESURESI]= "businessCategory";
            msHashCP[ATTR_PAROLAUZUNUGU]= "description";
            msHashCP[ATTR_PAROLABUYUK]= "street";
            msHashCP[ATTR_PAROLAKUCUK]= "st";
            msHashCP[ATTR_PAROLAALFANUM]= "postalCode";
            msHashCP[ATTR_PAROLARAKAM] = "postOfficeBox";
            msHashCP[ATTR_NAME]= "Name";

        }


        /**
         * Verilen dizin tipine gore, gerekli bilgileri verilen BaglantiBilgileri'nden
         * alarak directory'ye baglanir.
         * @param aBB Baglanti bilgileri almak icin kullanilacak  class
         */
        public DirectoryBase(DirectoryInfo aBB)
        {
            mDirectoryInfo = aBB;
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.ORTAK);
            }
            catch (LE ex)
            {
               throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }

            //Dictionary<String, String> env = new Dictionary<String, String>(10);
            String dizinTipi = aBB.getType();
            //ldap tipine gore gerekli argumanlari hash icine yazalim
            if (dizinTipi.Equals(ACTIVE_DIRECTORY) ||
                    dizinTipi.Equals(NETSCAPE) ||
                    dizinTipi.Equals(CRITICAL_PATH))
            {
                //env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

                //String protokol = System.getProperty("esya.ldap." + aBB.getPort());
                //if (protokol == null || protokol.equals(""))
                //    protokol = "ldap://";
                //env.put(Context.PROVIDER_URL, protokol + aBB.getIP() + ":" + aBB.getPort());
                /** @todo  SECURITY_AUTHENTICATION simple olmamali. Guvenlik acigi...*/
                //env.put(Context.SECURITY_AUTHENTICATION, "simple");
                //env.put(Context.SECURITY_PRINCIPAL, aBB.getUserName());
                //env.put(Context.SECURITY_CREDENTIALS, aBB.getUserPassword());
                if (dizinTipi.Equals(ACTIVE_DIRECTORY))
                {
                    //env.put("java.naming.ldap.attributes.binary", "objectGUID");
                }
            }
            else
            {
                //_hataBildirCiddi(GenelDil.LDAP_TIP_HATALI);
                _hataBildirCiddi("LDAP_TYPE_ERROR");
                return;
            }
            //ldap baglantisini saglayalim.

            /*try
            {
                mConnection = new DirectorySearcher(new DirectoryEntry("LDAP://" + aBB.getIP() + ":" + aBB.getPort(), aBB.getUserName(), aBB.getUserPassword()));
            }
            catch (Exception ex)
            {
                _hataBildir(ex, "LDAP HATA");
                return;
            }*/
            //catch (AuthenticationException ex)
            //{
            //    _hataBildir(ex, GenelDil.DIZINSIF_VEYA_KULLADI_HATALI);
            //    return;
            //}
            //catch (CommunicationException ex)
            //{
            //    _hataBildir(ex, GenelDil.IP_PORTTA_DIZIN_BULUNAMADI);
            //    return;
            //}
            //catch (NamingException ex)
            //{
            //    _hataBildir(ex, GenelDil.LDAPA_BAGLANILAMADI);
            //    return;
            //}
            //Hata cikarsa bu noktaya yetisemeden return ile cikmali...
            //Hata cikarsa bu noktaya yetisemeden return ile cikmali...
            //mConnected = true;
        }


        /**
         * Attribute adlari ile ilgili zaman zaman problem cikabilmektedir. Ornegin
         * bazi adlar case sensitive olabilmektedir. Veya bazi serverlarda attribute adi
         * biraz farkli olabilmektedir. Bu sorunlardan kurtulmak icin bizim standart
         * olarak kullanacagimiz adlar bu fonksiyon tarafindan dogru sekle cevrilecektir.
         * Bu fonksiyon sadece dizinislemleri icinde kullanilmak icindir. Buna ragmen
         * kullanmak isteyene acik olmasi amaciyla public tanimlanmistir. Dizin tipi
         * construction sirasinda belirlenmistir.
         * @param aAttrAdi Bizim standart olarak belirledigimiz attribute adlarindan biri
         * @return Dizin tipine gore uyarlanmis attribute adi.
         */
        public String normalizeAttrName(String aAttrAdi)
        {
            String r;
            String dizinTipi = mDirectoryInfo.getType();
            if (dizinTipi.Equals(ACTIVE_DIRECTORY))
            {
                mTemp_hash = msHashAD;
            }
            else if (dizinTipi.Equals(NETSCAPE))
            {
                mTemp_hash = msHashNetscape;
            }
            else if (dizinTipi.Equals(CRITICAL_PATH))
            {
                mTemp_hash = msHashCP;
            }
            else
            {
                //_hataBildirCiddi(GenelDil.LDAP_TIP_HATALI);
                _hataBildirCiddi("LDAP_TYPE_ERROR");
                return "";
            }

            r = null;
            mTemp_hash.TryGetValue(aAttrAdi, out r);
            if (r == null)
            {
                //_hataBildirCiddi(GenelDil.ATTRIBUTE_BILINMIYOR);
                _hataBildirCiddi("ATTRIBUTE_UNKNOWN");
                r = "";
            }
            return r;
        }


        /**
         * Directory'ye baglanip baglanamadigimi doner.
         * @return Directory ile baglantim varsa true, yoksa false doner.
         */
        //public bool isConnected()  //NOPMD
        //{
        //    return mConnected;
        //}


        /**
         * Son hata mesaji devamli tutulmaktadir. Bu fonksiyon ile olusan son hata mesaji
         * alinabilir. Herhangi bir ldap isleminde, hata cikmasi durumunda (ki bu durumda
         * hatanin olustugu fonksiyon false donecektir) kullaniciya hata mesaji getMesaj
         * kullanilarak donulebilir.
         * @return Son olusan hata mesaji
         */
        public String getMessage()  //NOPMD
        {
            return mMessage;
        }


        /*
            protected void hataBildir(Exception e, int hataid)
            {
           mesaj = Logger.getLogger(null).LogaYaz(e, hataid);
            }
            protected void hataBildir(Exception e, int hataid,String[] argumanlar)
            {
           mesaj = Logger.getLogger(null).LogaYaz(e, hataid,argumanlar);
            }
            protected void hataBildir(int hataid)
            {
           mesaj = Logger.getLogger(null).LogaYaz(hataid);
            }
         */

        protected void _hataBildir(Exception aEx, String aHataMesaji)
        {
            //mMessage = msRB.getString(aHataMesaji);
            LOGCU.Error(mMessage, aEx);
        }


        protected void _hataBildir(Exception aEx, String aMesajKod, Object[] aArgumanlar)
        {
            //MessageFormat formatter;

            //mMessage = msRB.getString(aMesajKod);
            //formatter = new MessageFormat(mMessage);

            //LOGCU.Error(formatter.format(aArgumanlar), aEx);
            LOGCU.Error(String.Format(aMesajKod, aArgumanlar), aEx);
        }


        protected void _hataBildir(String aHataMesaji)
        {
            //mMessage = msRB.getString(aHataMesaji);
            LOGCU.Error(mMessage);
        }


        protected void _hataBildirCiddi(String aHataMesaji)
        {
/*            int i;
            
            for (i = 0; i < 10; i++)
            {
                Console.WriteLine(
                        "**********************************************************");
            }
            Console.WriteLine(
                    "**************** Ciddi Programlama Hatasi ****************\n" + mMessage);
            for (i = 0; i < 10; i++)
            {
                Console.WriteLine(
                        "**********************************************************");

            }*/
            LOGCU.Fatal(mMessage);
        }


        //public void showMessage(Component aCom)
        //{

        //    JOptionPane.showMessageDialog(aCom, mMessage, "Hata", JOptionPane.ERROR_MESSAGE);
        //}
    }
}
