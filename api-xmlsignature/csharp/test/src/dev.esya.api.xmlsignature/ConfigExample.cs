using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

namespace ImzaApiTest.src.dev.esya.api.xmlsignature
{
    class ConfigExample
    {
        static void Main(string[] args)
        {
            Config c = new Config();
            string port = c.HttpConfig.ProxyPort;
            Console.WriteLine("port: " + port);

            IList<Type> validators1 = c.ValidationConfig.getProfile(SignatureType.XAdES_C).Validators;
            IList<Type> validators2 = c.ValidationConfig.getProfile(SignatureType.XAdES_BES).Validators;
            IList<Type> validators3 = c.ValidationConfig.getProfile(SignatureType.XMLDSig).Validators;
            IList<Type> validators4 = c.ValidationConfig.getProfile(SignatureType.XAdES_A).Validators;
            Console.WriteLine("v1: " + validators1.Count);
            Console.WriteLine("v2: " + validators2.Count);
            Console.WriteLine("v3: " + validators3.Count);
            Console.WriteLine("v4: " + validators4.Count);

            Console.WriteLine("write: " + c.Parameters.WriteReferencedValidationDataToFileOnUpgrade);
        }
    }
}
