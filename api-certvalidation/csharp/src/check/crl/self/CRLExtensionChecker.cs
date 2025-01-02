using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self
{
    /**
     * Checks the extension information in the CRL is valid 
     */
    public class CRLExtensionChecker : CRLSelfChecker
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private static List<Asn1ObjectIdentifier> knownExtensions = new List<Asn1ObjectIdentifier>();

        static CRLExtensionChecker()
        {
            knownExtensions.Add(EExtensions.oid_ce_authorityKeyIdentifier);//Authority Key Identifier
            knownExtensions.Add(EExtensions.oid_ce_cRLNumber);//CRL Number
            knownExtensions.Add(EExtensions.oid_ce_issuingDistributionPoint);//Issuing Distribution Point
        }

        /*RFC 3280de tanımlanan SIL extensionlar:
          5.2.1 Authority Key Identifier
          5.2.2 Issuer Alternative Name
          5.2.3 CRL Number
          5.2.4 Delta CRL Indicator
          5.2.5 Issuing Distribution Point
          5.2.6 Freshest CRL
          */

        /**
         * SIL eklentilerinin geçerli olup olmadığını kontrol eder
         */
        protected override PathValidationResult _check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
        {
            EExtensions exts = aCRL.getCRLExtensions();
            if (exts.getExtensionCount() == 0)
            {
                logger.Debug("CRL yapısında eklenti yok.");
                aCRLStatusInfo.addDetail(this, ExtensionCheckStatus.NO_EXTENSION, true);
                return PathValidationResult.SUCCESS;
            }

            for (int i = 0; i < exts.getExtensionCount(); i++)
            {
                EExtension ext = exts.getExtension(i);
                if (ext.isCritical())
                {
                    logger.Debug("CRL yapısında eklenti var.");
                    if (!_isKnownExtension(ext.getIdentifier()))
                    {
                        logger.Error("Extension tanımlı extensionlar içinde değil: " + ext.getIdentifier());
                        aCRLStatusInfo.addDetail(this, ExtensionCheckStatus.INVALID_EXTENSION, false);
                        return PathValidationResult.CRL_EXTENSIONS_CONTROL_FAILURE;
                    }
                }
            }
            aCRLStatusInfo.addDetail(this, ExtensionCheckStatus.VALID_EXTENSIONS, true);
            return PathValidationResult.SUCCESS;
        }

        private bool _isKnownExtension(Asn1ObjectIdentifier aOID)
        {
            foreach (Asn1ObjectIdentifier extension in knownExtensions)
            {
                if (aOID.Equals(extension))
                {
                    if (logger.IsDebugEnabled)
                        logger.Debug("Extension tanımlı extensionlar içinde bulundu:" + aOID);
                    return true;
                }
            }
            return false;
        }


        public override String getCheckText()
        {
            return Resource.message(Resource.SIL_EKLENTI_KONTROLU);
        }

    }
}
