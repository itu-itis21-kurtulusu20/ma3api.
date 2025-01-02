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
     * Finds CRL for a given certificate according to the CRL Distribution Points
     * (CDP) extension information in the certificate. It only searches in HTTP
     * addresses specified in the CDP extension. 
     */
    public class CRLFinderFromHTTP : CRLFinderFromCDP
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Sertifikanın HTTP üzerindeki SİL dağıtıcı adreslerini döner
         */
        protected override List<String> _getAddresses(ECertificate aCertificate)
        {

            ECRLDistributionPoints points =  aCertificate.getExtensions().getCRLDistributionPoints();
            if (points != null)
            {
                List<String> addresses = points.getHttpAddresses();
                if ((addresses != null) && addresses.Count != 0)
                 {
                     return addresses;
                 }
            }

            return new List<string>();
        }

        /**
         * verilen HTTP adresinden SİL bilgisini okur
         */        
        protected override ECRL _getCRL(String aAddress)
        {
            try
            {
                logger.Debug("Getting CRL from address: " + aAddress);
                String timeOut = mParameters.getParameterAsString(PARAM_TIMEOUT);
                byte[] crlData = BaglantiUtil.urldenVeriOku(aAddress, timeOut);
                ECRL crl = new ECRL(crlData);
                return crl;
            }
            catch (WebException e)
            {
                logger.Error("The URL address is not valid or An error occurred while downloading data: " + aAddress, e);
            }
            catch (IOException e)
            {
                logger.Error("CRL can not be read.", e);
            }
            catch (ESYAException e)
            {
                logger.Error("CRL Asn1 decode error", e);
            }
            return null;
        }

        /**
         * Sertifikanın HTTP üzerindeki SİL dağıtıcısını döner
         */
        protected override EName _getCRLIssuer(ECertificate aCertificate)
        {
            List<EName> issuers = aCertificate.getExtensions().getCRLDistributionPoints().getHttpIssuers();
            if ((issuers != null) && issuers.Count != 0)
            {
                return issuers[0];
            }
            return null;
        }
    }
}
