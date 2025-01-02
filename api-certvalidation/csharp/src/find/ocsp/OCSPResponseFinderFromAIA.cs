using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.ocsp;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp
{
    /**
     * Finds OCSP Response from the OCSP Servers specified in the
     * AuthorityInfoAccess extension information in the certificate
     *
     * @author IH
     */
    public class OCSPResponseFinderFromAIA : OCSPResponseFinder
    {                
        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private DigestAlg _digestAlgForOcspFinder;
        /**
        * Verilen Sertifika için AuthorityInfoAccess eklentisinden OCSP Cevabı bulur
        */
        protected override EOCSPResponse _findOCSPResponse(ECertificate aCertificate, ECertificate aIssuerCert)
        {
            if (Logger.IsDebugEnabled)
            {
                Logger.Debug("Find ocsp response from AIA for : '" + aCertificate.getSubject().stringValue() + "'");
            }
            List<String> addresses = aCertificate.getOCSPAdresses();            
            if (addresses != null && addresses.Count != 0)
            {
                String address = aCertificate.getOCSPAdresses()[0];
                if (Logger.IsDebugEnabled)
                    Logger.Debug("found address : " + address);
                
                try
                {
                    Chronometer c = new Chronometer("Find OCSP Response");
                    c.start();

                    OCSPClient ocspClient = new OCSPClient(address);
                    if (_digestAlgForOcspFinder != null)
                        ocspClient.setDigestAlgForOcspRequest(_digestAlgForOcspFinder);

                    String timeOut = mParameters.getParameterAsString(PARAM_TIMEOUT);
                    if (timeOut != null)
                    {
                        Logger.Debug("Timeout is set to " + timeOut);
                        ocspClient.setTimeOut(timeOut);                        
                    }

                    ocspClient.queryCertificate(aCertificate, aIssuerCert);
                    EOCSPResponse  response = ocspClient.getOCSPResponse();

                    Logger.Info(c.stopSingleRun());
                    
                    if (response.getResponseStatus() == OCSPResponseStatus.successful().mValue)
                    {
                        if (mParentSystem != null)
                            mParentSystem.getSaveSystem().registerOCSP(aCertificate, response);
                        return response;
                    }
                    else
                    {
                        Logger.Error("OCSPResponseStatus is not _SUCCESSFUL. It is " + response.getResponseStatus());
                    }
                }
                catch (Exception x)
                {
                    Logger.Error("OCSP protokolunde hata olustu", x);
                }
            }
            else
            {
                Logger.Debug("No OCSP addresses found in AIA");
            }

            return null;
        }

        public void setDigestAlgForOcspFinder(DigestAlg digestAlg)
        {
            _digestAlgForOcspFinder = digestAlg;
        }
    }
}
