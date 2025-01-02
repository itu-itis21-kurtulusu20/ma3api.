using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;

namespace tr.gov.tubitak.uekae.esya.api.tsl.CertificateFinder
{
    public class CertificateFinderFromTSL : tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.CertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [DllImport("shell32.dll", CharSet = CharSet.Auto)]
        private static extern int SHGetSpecialFolderPath(IntPtr hwndOwner, StringBuilder lpszPath, int nFolder, bool fCreate);
        private static readonly String DEFAULT_DEPO_DOSYA_ADI = "tsl.xml";

        override protected List<ECertificate> _findCertificate()
        {
            return _findCertificate(null);
        }

        protected override List<ECertificate> _findCertificate(ECertificate aSertifika)
        {
            return getAllCertificates();
        }
        protected List<ECertificate> getAllCertificates()
        {
            String storePath = mParameters.getParameterAsString(PARAM_STOREPATH);
            if (storePath == null)
            {
                const int MAX_PATH = 260;
                StringBuilder userFolder = new StringBuilder(MAX_PATH);

                const int CSIDL_PROFILE = 40;
                SHGetSpecialFolderPath(IntPtr.Zero, userFolder, CSIDL_PROFILE, false);
                String filePath = userFolder.ToString() +
                Path.DirectorySeparatorChar +
                CertStoreUtil.DEPO_DIZIN_ADI +
                Path.DirectorySeparatorChar +
                DEFAULT_DEPO_DOSYA_ADI;
                logger.Debug("TSL storepath is not defined. " + filePath + " is used to find TSL file as default path");
                storePath = filePath;
            }
            TSL tsl = null;
            try
            {
                tsl = TSL.parse(storePath);
            }
            catch (Exception exc)
            {
                logger.Error("An error occurred while reading TSL file", exc);
            }
            bool validationRslt = false;
            try
            {
                validationRslt = tsl.validateTSL();
            }
            catch (Exception exc)
            {
                logger.Error("An error occurred while validating TSL file", exc);
            }
            if (!validationRslt)
            {
                logger.Error("TSL file cannot be validated");
                return null;
            }
            return (List<ECertificate>)tsl.getValidCertificates();
        }
    }
}
