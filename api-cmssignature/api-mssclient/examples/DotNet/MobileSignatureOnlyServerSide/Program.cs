using System;
using System.Net;
using System.Windows.Forms;
using log4net.Config;

//Gelen giden SOAP mesajlarını log'lamak için TraceExtension sınıfını kontrol ediniz. 

namespace MobileSignatureOnlyServerSide
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            //Türk Telekom mobil imza için eklendi.
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;

            BasicConfigurator.Configure();

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Form1());
        }
    }
}
