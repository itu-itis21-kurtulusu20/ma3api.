using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    /**
     * Finds CRL for a given certificate from local file system 
     */
    public class CRLFinderFromFile : CRLFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
 
        /**
         * Dosyadan SİL okur
         */
        protected override List<ECRL> _findCRL(ECertificate aCertificate)
        {
            List<ECRL> list = new List<ECRL>();

            String dosyaYolu;
            if ((mParameters != null) && ((dosyaYolu = mParameters.getParameterAsString(DOSYA_YOLU)) != null))
            {
                try
                {
                    list.Add(new ECRL(new FileInfo(dosyaYolu)));
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
                        list.Add(new ECRL(fileName));
                    }
                    catch (Exception e)
                    {
                        logger.Error("Dosyadan (" + fileName.FullName + ") sertifika bilgisi alinamadi", e);
                    }
                }
            }

            return list;
        }
  }
}
