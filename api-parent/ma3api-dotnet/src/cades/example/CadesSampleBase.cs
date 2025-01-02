using System;
using System.IO;
using System.Reflection;
using System.Runtime.CompilerServices;
using ImzaApiTest.src.tr.gov.tubitak.uekae.esya.api;
using log4net;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace tr.gov.tubitak.uekae.esya.api.cades.example
{
    /**
     * Provides required variables and functions for ASiC examples
     */

    public class CadesSampleBase : SampleBase
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private static readonly string testDataFolder;
        private static readonly string policyFile;
        private static readonly TSSettings tsSettings;
        private static ValidationPolicy policy;

        static CadesSampleBase()
        {
            try
            {
                testDataFolder = getRootDir() + @"\testVerileri\";
                policyFile = getRootDir() + @"\config\certval-policy-test.xml";

                // 2. argümana customerID'nizi girmelisiniz.
                tsSettings = new TSSettings("http://tzd.kamusm.gov.tr", 2, "PASSWORD", DigestAlg.SHA256);
            }
            catch (Exception e)
            {
                logger.Error(e);
            }
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        public static ValidationPolicy getPolicy()
        {
            if (policy == null)
                try
                {
                    policy = PolicyReader.readValidationPolicy(new FileStream(policyFile, FileMode.Open));
                }
                catch (FileNotFoundException e)
                {
                    throw new SystemException("Policy file could not be found", e);
                }
            return policy;
        }

        public static string getTestDataFolder()
        {
            return testDataFolder;
        }

        public static TSSettings getTSSettings()
        {
            //return tsSettings;

            /*for getting test TimeStamp or qualified TimeStamp account, mail to bilgi@kamusm.gov.tr.
            This configuration, user ID (2) and password (PASSWORD), is invalid. */

            throw new ESYAException("\n- Zaman Damgası kullanmak için zaman damgası hesap bilgilerini, static block içerisinde tanımlı TSSettings nesnesine parametre geçiniz. Varsayılan olarak tanımlı olan user ID (2) ve password (PASSWORD) geçersizdir. Zaman damgası hesap bilgilerini ayarladıktan sonra \"return tsSettings;\" satırını aktifleştiriniz.\n" +
                                    "- MA3 API sadece KamuSM Zaman Damgası ile çalışabilmektedir.\n" +
                                    "- Zaman damgası test kullanıcısı talep etmek amacıyla Kamu SM (bilgi[at]kamusm.gov.tr)'ye e-posta gönderilmesi gerekmektedir. İlgili e-posta'nın konu kısmında \"Zamane test kullanıcı talebi\", içeriğinde ise \"Kurum adı, kurum vergi kimlik numarası, kurum adresi, kurum sabit telefon, yetkili kişi adı ve soyadı, cep telefonu numarası, yetkili kişi e-posta\" bilgilerinin yer alması gerekmektedir.\n" +
                                    " Ayrıntılar için: https://kamusm.bilgem.tubitak.gov.tr/urunler/zaman_damgasi/ucretsiz_zaman_damgasi_istemci_yazilimi.jsp");
        }
    }
}