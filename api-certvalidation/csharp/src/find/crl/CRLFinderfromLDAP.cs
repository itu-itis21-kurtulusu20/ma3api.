using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    /**
     * Finds CRL for a given certificate according to the CRL Distribution Points
     * extension information in the certificate.It only searches in LDAP addresses
     * specified in the CDP extension.
     *
     * @author IH
     */
    public class CRLFinderfromLDAP : CRLFinderFromCDP
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static readonly String DIZIN_TIPI = "dizintipi";

        /**
         * Sertifikanın LDAP üzerindeki SİL dağıtıcı adreslerini döner
         */
        protected override List<String> _getAddresses(ECertificate aCertificate)
        {

            ECRLDistributionPoints points = aCertificate.getExtensions().getCRLDistributionPoints();
            if (points != null)
            {
                List<String> addresses = points.getLdapAddresses();
                if ((addresses != null) && addresses.Count != 0)
                {
                    return addresses;
                }
            }

            return new List<string>();
        }

        /**
         * Verilen LDAP adresinden SİL bilgisini okur
         */       
        protected override ECRL _getCRL(String aAddress)
        {
            logger.Debug("Getting CRL from address: " + aAddress);
            String dizinTipi = mParameters.getParameterAsString(DIZIN_TIPI);
            byte[] crlData = BaglantiUtil.dizindenVeriOku(aAddress, dizinTipi);
            if (crlData != null)
            {
                ECRL crl = null;
                try
                {
                    crl = new ECRL(crlData);
                }
                catch (ESYAException e)
                {
                    logger.Error("CRL Asn1 decode error", e);
                }
                return crl;
            }
            else
            {
                logger.Error("CRL can not be read from LDAP");
            }
            return null;
        }


        /**
         * Sertifikanın LDAP üzerindeki SİL dağıtıcısını döner
         */
        protected override EName _getCRLIssuer(ECertificate aCertificate)
        {
            List<EName> issuers = aCertificate.getExtensions().getCRLDistributionPoints().getLdapIssuers();
            if (issuers.Count > 0)
            {
                return issuers[0];
            }
            return null;
        }
    }
}
