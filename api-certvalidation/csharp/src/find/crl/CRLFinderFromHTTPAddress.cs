using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    /**
     * Finds CRL for a given certificate from the given HTTP address 
     */
    public class CRLFinderFromHTTPAddress : CRLFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static readonly String HTTP_ADRESI = "httpadresi";

        /**
         * Find CRL at http address for given certificate
         */
        protected override List<ECRL> _findCRL(ECertificate aCertificate)
        {
            String address = mParameters.getParameterAsString(HTTP_ADRESI);
            String timeOut = mParameters.getParameterAsString(PARAM_TIMEOUT);
            try
            {
                logger.Debug("Getting CRL from address: " + address);
                byte[] crlBytes = BaglantiUtil.urldenVeriOku(address, timeOut);

                ECRL crl = new ECRL(crlBytes);
                List<ECRL> crlList = new List<ECRL>(1);
                crlList.Add(crl);

                return crlList;
            }
            catch (WebException e)
            {
                throw new ESYAException("The URL address is not valid or An error occurred while downloading data: " + address, e);
            }
            catch (IOException e)
            {
                throw new ESYAException("CRL can not be read.", e);
            }
            
        }
    }
}
