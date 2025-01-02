using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross
{
    /**
     * Finds cross certificate from a specific file location.
     */
    public class CrossCertificateFinderFromFile : CrossCertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Dosyadan Çapraz sertifika okur
         */
        protected override List<ECertificate> _findCrossCertificate()
        {
            List<ECertificate> certList = new List<ECertificate>();

            String dosyaYolu;
            if ((mParameters != null) && ((dosyaYolu = mParameters.getParameterAsString(DOSYA_YOLU)) != null))
            {
                try
                {
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
                DirectoryInfo folder = new DirectoryInfo(dizinYolu);
                FileInfo[] fileNames = folder.GetFiles();
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
