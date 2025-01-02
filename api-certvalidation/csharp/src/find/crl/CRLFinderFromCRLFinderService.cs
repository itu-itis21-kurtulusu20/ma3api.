using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.service;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    public class CRLFinderFromCRLFinderService : CRLFinder
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected override List<ECRL> _findCRL(ECertificate aCertificate)
        {
            List<ECRL> crls = new List<ECRL>();

            aCertificate.getCRLIssuer();

            DateTime? baseValidationTime = mParentSystem.getBaseValidationTime();

            String serviceAddress = getCRLServiceAddress();
            try
            {
                CrlFinderClient finderClient = null;
                String timeout = mParameters.getParameterAsString(PARAM_TIMEOUT);
                if (timeout != null)
                {
                    finderClient = new CrlFinderClient(new Uri(serviceAddress), Convert.ToInt32(timeout) / 1000);
                }
                else
                {
                    finderClient = new CrlFinderClient(new Uri(serviceAddress));
                }

                List<String> addresses = finderClient.silSorgulaTarihindenSonraki(aCertificate.getCRLIssuer(), baseValidationTime);
                //  kok sili için onceki siller de kullanılabilir
			    if(!mParentSystem.isDoNotUsePastRevocationInfo()){
			    List<String> addressesOnceki =  finderClient.silSorgulaTarihindenOnceki(aCertificate.getCRLIssuer(), baseValidationTime);
			    addresses.AddRange(addressesOnceki);
			    }
			   
                foreach (String aCrlAddress in addresses)
                {
                    try
                    {
                        String timeOut = mParameters.getParameterAsString(PARAM_TIMEOUT);
                        // dirty url fix hack
                        int breakingBad = aCrlAddress.LastIndexOf('/'); 
                        String last = aCrlAddress.Substring(breakingBad + 1);
                        // URL encoder does not work because server do not accept
                        // '+' for spaces and expects "%20" instead.
                        String encoded = last.Replace(" ", "%20");
                        String fixedAddress = aCrlAddress.Substring(0, breakingBad + 1) + encoded;

                        byte[] crlBytes = BaglantiUtil.urldenVeriOku(fixedAddress, timeOut);
                        ECRL crl = new ECRL(crlBytes);
                        crls.Add(crl);
                    }
                    catch (IOException ex)
                    {
                        logger.Error("Can not read CRL from the address: " + aCrlAddress, ex);
                    }
                }
            }
            catch (Exception e)
            {
                logger.Error("Can not query crl from service at " + serviceAddress, e);
            }
            return crls;
        }

    }
}
