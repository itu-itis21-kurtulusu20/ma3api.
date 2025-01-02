using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate
{
    /**
     * Finds issuer certificate of a given certificate from local file.
     */
    public class CertificateFinderFromFile : CertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
       
        //private CertificateCriteriaMatcher mMatcher = new CertificateCriteriaMatcher();

        /**
         * Find issuer certificate from file
         */
        protected override List<ECertificate> _findCertificate()
        {
            return _findCertificate(null);
        }

        /**
         * Find issuer certificate from file
         */
        protected override List<ECertificate> _findCertificate(ECertificate aSertifika)
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
                DirectoryInfo folder = new DirectoryInfo(dizinYolu);
                FileInfo[] fileNames = null;

                if (folder.Exists)
                    fileNames = folder.GetFiles();
                else
                {
                    fileNames = new FileInfo[0];
                    logger.Error( folder.FullName + " dizini bulunamadı.");
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
        
        /*
        override public List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria)
        {
            List<ECertificate> certs = findCertificate();

            if (certs != null && certs.Count > 0)
            {
                // finds only one cert from file anyway
                ECertificate cert = certs[0];

                if (mMatcher.match(aCriteria, cert))
                    return certs;
            }
            return null;
        }
        //*/
    }
}
