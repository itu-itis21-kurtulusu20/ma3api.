using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    /**
     * Finds CRL for a given certificate from the given LDAP address 
     */
    public class CRLFinderFromLDAPAddress : CRLFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static readonly String DIZIN_TIPI = "dizintipi";
        public static readonly String DIZIN_ADRESI = "dizinadresi";

        /**
         * Sertifikanın verilen LDAP adresindeki SİL ini bulur
         */
        protected override List<ECRL> _findCRL(ECertificate aCertificate)
        {
            String dizinTipi = mParameters.getParameterAsString(DIZIN_TIPI);
            String adres = mParameters.getParameterAsString(DIZIN_ADRESI);

            logger.Debug("Getting CRL from address: " + adres);

            byte[] crlBytes = BaglantiUtil.dizindenVeriOku(adres, dizinTipi);

            ECRL crl = new ECRL(crlBytes);
            List<ECRL> crlList = new List<ECRL>(1);
            crlList.Add(crl);

            return crlList;
        }
    }
}
