package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;

/**
 * Performs OCSP Response validation 
 */
public class OCSPResponseController
{
    private static final Logger logger = LoggerFactory.getLogger(OCSPResponseController.class);

    /**
     * OCSP Cevabının doğruluğunu kontrol eder.
     */
    public OCSPResponseStatusInfo check(ValidationSystem aValidationSystem, EOCSPResponse aOCSPResponse)
            throws ESYAException
    {
        logger.debug("OCSP cevabı kontrolü yapılacak");

        OCSPResponseStatusInfo responseStatusInfo = new OCSPResponseStatusInfo(aOCSPResponse);

        if (aOCSPResponse.getResponseStatus()== OCSPResponseStatus._SUCCESSFUL){

            CheckSystem ks = aValidationSystem.getCheckSystem();

            PathValidationResult p = ks.checkOCSPResponse(responseStatusInfo, aOCSPResponse);

            if (p == PathValidationResult.SUCCESS){
                responseStatusInfo.setOCSPResponseStatus(OCSPResponseStatusInfo.OCSPResponseStatus.VALID);
            }
            else { // path validation not successfull
                responseStatusInfo.setOCSPResponseStatus(OCSPResponseStatusInfo.OCSPResponseStatus.PATH_VALIDATION_FAILURE);
            }
        }
        else { // response status not successfull
            responseStatusInfo.setOCSPResponseStatus(OCSPResponseStatusInfo.OCSPResponseStatus.INVALID_RESPONSE);
        }
        return responseStatusInfo;
    }

}
