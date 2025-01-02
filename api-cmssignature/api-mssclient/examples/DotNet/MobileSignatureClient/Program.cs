using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using log4net.Config;

namespace MobileSignatureClient
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            BasicConfigurator.Configure();

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Form1());
        }
    }
}
