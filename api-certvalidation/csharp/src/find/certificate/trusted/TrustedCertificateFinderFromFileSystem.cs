using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted
{
    /**
     * Finds trusted certificates from the local file system. 
     */
    public class TrustedCertificateFinderFromFileSystem : TrustedCertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Dosyadan Güvenilir Sertifika okur
         */
        protected override List<ECertificate> _findTrustedCertificate()
        {
            List<ECertificate> certList = new List<ECertificate>();

            String dosyaYolu;
            if ((mParameters != null) && ((dosyaYolu = mParameters.getParameterAsString(DOSYA_YOLU)) != null))
            {
                try
                {
                    dosyaYolu = PathUtil.getRawPath(dosyaYolu);
                    certList.Add(ECertificate.readFromFile(dosyaYolu));
                }
                catch (Exception x)
                {
                    logger.Error("Dosyadan (" + dosyaYolu + ") sertifika bilgisi alinamadi", x);
                }
            }

            String dizinYolu;
            if ((mParameters != null) && ((dizinYolu = mParameters.getParameterAsString(DIZIN)) != null))
            {
                dizinYolu = PathUtil.getRawPath(dizinYolu);
                DirectoryInfo folder = null;
                if (dizinYolu.Length == 0)
                {
                    string currentDirectory = Directory.GetCurrentDirectory();
                    folder = new DirectoryInfo(currentDirectory);
                }
                else
                {
                    folder = new DirectoryInfo(dizinYolu);
                }

                FileInfo[] fileNames = null; 
                if(folder.Exists)
                    fileNames = folder.GetFiles();
                else
                {
                    fileNames = new FileInfo[0];
                    logger.Error(folder.FullName + " dizini bulunamadı.");
                }

                foreach (FileInfo fileName in fileNames)
                {
                    try
                    {
                        certList.Add(new ECertificate(fileName));
                    }
                    catch (Exception e)
                    {
                        logger.Error("Dosyadan (" + fileName.FullName + ") sertifika bilgisi alinamadi", e);
                    }
                }
            }

            return certList;
        }
    }
}
