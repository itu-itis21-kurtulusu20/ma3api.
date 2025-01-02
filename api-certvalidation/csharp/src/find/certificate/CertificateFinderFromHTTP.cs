using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate
{
    /**
     * Finds issuer certificate according to Authority Info Access (AIA) extension
     * information. It only searches in HTTP addresses 
     */
    public class CertificateFinderFromHTTP : CertificateFinderFromAIA
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Sertifikanın HTTP üzerindeki SM Sertifikası dağıtıcı adreslerini döner
         */
        protected override List<String> _getAddresses(ECertificate aCertificate)
        {
            EAuthorityInfoAccessSyntax aia = aCertificate.getExtensions().getAuthorityInfoAccessSyntax();

            return aia == null ? null : aia.getHttpAddresses();
        }

        /**
         * verilen HTTP adresinden Sertifika bilgisini okur
         */
        protected override ECertificate _getCertificate(String aAddress)
        {
            try
            {
                String timeOut = mParameters.getParameterAsString(PARAM_TIMEOUT);
                byte[] certBytes = BaglantiUtil.urldenVeriOku(aAddress, timeOut);                
                ECertificate certificate = new ECertificate(certBytes);
                return certificate;
            }
            catch (ESYAException e)
            {
                logger.Debug("ECertificate Asn1 decode error", e);
            }
            return null;
        }
        
    }
}
