using System;
using System.IO;
using System.Reflection;
using log4net;
using log4net.Config;
using NETAPI_TEST.src.certificate;
//using NETAPI_TEST.src.cmsenvelope;
using NETAPI_TEST.src.signature;
using tr.gov.tubitak.uekae.esya.api.common.util;


/*Asagida lisansin ve loglamada kullanilan log4net'in konfigurasyonunu bulabilirsiniz.
Eger lisansi ornekteki gibi kullanmak istemiyorsaniz, 
lisans.xml'i projenizin exe'sinin bulundugu dizin altinda olusturacaginiz lisans klasoru altina atmaniz gerekir(lisans\lisans.xml)
config.xml'den log konfigurasyonunu degistirebilirsiniz*/


/*
 * KartTipi, pin, zaman damgasi bilgilerini kendinize gore duzenleyiniz!
 * */

namespace NETAPI_TEST
{
    class Test
    {


        public static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        static Test()
        {
            //log configuration
            XmlConfigurator.Configure(new FileInfo("logConfig.xml"));

            //Set license
            using (Stream license = new FileStream("lisans.xml", FileMode.Open, FileAccess.Read))
            {
                LicenseUtil.setLicenseXml(license);
            }
        }

        static void Main(string[] args)
        {
            LOGGER.Info("Start of main");
            
            //CertStoreTest.CertStoreCreate();
            
            

            //Console.WriteLine("------------------Sertifika Kontrolu------------------------");
         //   CertTest.SertifikaKontrolu();
            
            Console.WriteLine("------------------BESImza at------------------------");
            SignatureTest.BESImza();
            //Console.WriteLine("------------------ESTImza at------------------------");
            //SignatureTest.ESTImza();
            //Console.WriteLine("------------------Long Imza at------------------");
            //SignatureTest.testExlongSign();
            //Console.WriteLine("------------------BES TO EST yap------------------");
            //SignatureTest.convertToESTImza();
            //Console.WriteLine("finished!");


        }
    }
}
