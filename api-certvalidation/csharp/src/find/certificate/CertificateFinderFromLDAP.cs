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
     * information. It only searches in LDAP addresses 
     * @author IH
     */
    public class CertificateFinderFromLDAP : CertificateFinderFromAIA
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static readonly String DIZIN_TIPI = "dizintipi";

        /**
         * Sertifikanın LDAP üzerindeki SM Sertifikası dağıtıcı adreslerini döner
         */
        protected override List<String> _getAddresses(ECertificate aSertifika)
        {
            EAuthorityInfoAccessSyntax aia = aSertifika.getExtensions().getAuthorityInfoAccessSyntax();
            if (aia != null)
            {
                return aia.getLdapAddresses();
            }
            return null;
        }

        /**
         * verilen LDAP adresinden Sertifika bilgisini okur
         */
        //override protected byte[] _getCertificateData(String aAdres)
        //{
        //    //String dizinTipi = mParameters.getParameterAsString(DIZIN_TIPI);
        //    return BaglantiUtil.dizindenVeriOku(aAdres/*, dizinTipi*/);
        //}
        
        /**
         * verilen LDAP adresinden Sertifika bilgisini okur
         */
        protected override ECertificate _getCertificate(String aAdres)
        {
            try
            {
                String dizinTipi = mParameters.getParameterAsString(DIZIN_TIPI);
                byte[] certData = BaglantiUtil.dizindenVeriOku(aAdres, dizinTipi);
                ECertificate cert = new ECertificate(certData);
                return cert;
            }
            catch (Exception e)
            {
                logger.Debug(String.Format("{0} LDAP adresinden sertifika alınamadı.", aAdres), e);
            }
            
            return null;
        }

    }

}
