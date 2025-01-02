using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.ocsp;


namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp
{
    /**
     * Performs OCSP Response validation 
     */
    public class OCSPResponseController
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * OCSP Cevabının doğruluğunu kontrol eder.
         */
        public OCSPResponseStatusInfo check(ValidationSystem aValidationSystem, EOCSPResponse aOCSPResponse)
        {
            logger.Debug("OCSP cevabı kontrolü yapılacak");

            OCSPResponseStatusInfo responseStatusInfo = new OCSPResponseStatusInfo(aOCSPResponse);

            if (aOCSPResponse.getResponseStatus() == OCSPResponseStatus.successful().mValue)
            {

                CheckSystem ks = aValidationSystem.getCheckSystem();

                PathValidationResult p = ks.checkOCSPResponse(responseStatusInfo, aOCSPResponse);

                if (p == PathValidationResult.SUCCESS)
                {
                    responseStatusInfo.setOCSPResponseStatus(OCSPResponseStatusInfo.OCSPResponseStatus.VALID);
                }
                else
                { // path validation not successfull
                    responseStatusInfo.setOCSPResponseStatus(OCSPResponseStatusInfo.OCSPResponseStatus.PATH_VALIDATION_FAILURE);
                }
            }
            else
            { // response status not successfull
                responseStatusInfo.setOCSPResponseStatus(OCSPResponseStatusInfo.OCSPResponseStatus.INVALID_RESPONSE);
            }
            return responseStatusInfo;
        }
    }
}
