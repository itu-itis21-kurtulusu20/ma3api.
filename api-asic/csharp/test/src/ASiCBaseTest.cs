using System;
using System.IO;
using System.Reflection;
using NUnit.Framework;
using log4net;
using log4net.Config;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.config;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;
using tr.gov.tubitak.uekae.esya.api.signature.util;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    public class ASiCBaseTest
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected static String ROOT_DIR;
        protected static String BASE_DIR;
        protected static String CONFIG;
        protected static String LICENSE;

        protected static FileInfo dataFile;
        protected static string outDir;

        protected static ECertificate CERTIFICATE;
        protected static BaseSigner SIGNER;

        [TestFixtureSetUp]
        public static void setUp()
        {
            String dir = Directory.GetCurrentDirectory();

            ROOT_DIR = Directory.GetParent(dir).Parent.Parent.Parent.Parent.FullName;
            if (dir.Contains("x86") || dir.Contains("x64"))
            {
                ROOT_DIR = Directory.GetParent(ROOT_DIR).FullName;
            }
            XmlConfigurator.Configure(new FileInfo(ROOT_DIR + "/config/log4net.xml"));
            logger.Debug("Root directory: " + ROOT_DIR);
            BASE_DIR = ROOT_DIR + "/created/";
            CONFIG = ROOT_DIR + "/config/esya-signature-config.xml";
            LICENSE = ROOT_DIR + "/lisans/lisans.xml";

            dataFile = new FileInfo(BASE_DIR + "/sample.txt");
            outDir = ROOT_DIR + "/created/";

            loadLicense();

            string pfxPath = "../../../../../../api-signature/testresources/suleyman.uslu_283255@ug.net.pfx";
            string pfxPass = "283255";

            PfxSigner signer = new PfxSigner(SignatureAlg.RSA_SHA256.getName(), pfxPath, pfxPass);
            CERTIFICATE = signer.getSignersCertificate();
            SIGNER = signer;
        }

        protected static string getFileName(PackageType packageType, SignatureFormat format, SignatureType type)
        {
            string fileName = outDir + packageType + "-" + format + "-" + type;
            switch (packageType)
            {
                case PackageType.ASiC_S: return fileName + ".asics";
                case PackageType.ASiC_E: return fileName + ".asice";
            }
            return null;
        }

        protected SignaturePackage read(PackageType packageType, SignatureFormat format, SignatureType type)
        {
            Context c = createContext();//new Context();
            FileInfo f = new FileInfo(getFileName(packageType, format, type));
            return SignaturePackageFactory.readPackage(c, f);
        }

        public static Context createContext()
        {
            Uri uri = new Uri(BASE_DIR);
            Context context = new Context(new Uri(uri.AbsoluteUri));
            context.setConfig(new Config(CONFIG));
            return context;
        }

        public static void loadLicense()
        {
            logger.Debug("License is being loaded from: " + LICENSE);
            LicenseUtil.setLicenseXml(new FileStream(LICENSE, FileMode.Open, FileAccess.Read));
        }
        
    }
}
