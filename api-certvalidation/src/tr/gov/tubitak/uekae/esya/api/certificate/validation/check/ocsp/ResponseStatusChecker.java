package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;

/**
 * Checks the validity of OCSP Response status 
 */
public class ResponseStatusChecker extends OCSPResponseChecker {

    private static Logger logger = LoggerFactory.getLogger(ResponseStatusChecker.class);

    public enum ResponseStatusCheckStatus implements CheckStatus {

        SUCCESSFUL,
        MALFORMED_REQUEST,
        INTERNAL_ERROR,
        TRY_LATER,
        SIG_REQUIRED,
        UNAUTHORIZED;

        // todo

        public String getText()
        {
            switch (this) {
                case SUCCESSFUL:
                    return CertI18n.message(CertI18n.BASARILI);
                case MALFORMED_REQUEST:
                    return CertI18n.message(CertI18n.MALFORMED_REQUEST);
                case INTERNAL_ERROR:
                    return CertI18n.message(CertI18n.INTERNAL_ERROR);
                case TRY_LATER:
                    return CertI18n.message(CertI18n.TRY_LATER);
                case SIG_REQUIRED:
                    return CertI18n.message(CertI18n.SIG_REQUIRED);
                case UNAUTHORIZED:
                    return CertI18n.message(CertI18n.UNAUTHORIZED);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }


    /**
     * OCSP Cevabı Cevap Durumu bilgisini kontrol eder.
     */
    PathValidationResult _check(EOCSPResponse aOCSPResponse, OCSPResponseStatusInfo aOCSPCevapBilgisi)
    {
        int status = aOCSPResponse.getResponseStatus();
        if (logger.isDebugEnabled())
            logger.debug("Gelen cevap status : " + status);
        switch (status) {
            case OCSPResponseStatus._SUCCESSFUL:
                if (logger.isDebugEnabled())
                    logger.debug("Cevap başarılı");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.SUCCESSFUL, true);
                return PathValidationResult.SUCCESS; //successful
            case OCSPResponseStatus._MALFORMEDREQUEST:
                logger.error("Cevap status: malformedRequest");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.MALFORMED_REQUEST, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //malformedRequest
            case OCSPResponseStatus._INTERNALERROR:
                logger.error("Cevap status: internalError");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.INTERNAL_ERROR, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //internalError
            case OCSPResponseStatus._TRYLATER:
                logger.error("Cevap status: tryLater");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.TRY_LATER, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //tryLater
            case OCSPResponseStatus._SIGREQUIRED:
                logger.error("Cevap status: sigRequired");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.SIG_REQUIRED, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //sigRequired
            case OCSPResponseStatus._UNAUTHORIZED:
                logger.error("Cevap status: unauthorized");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.UNAUTHORIZED, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //unauthorized
            default:
                logger.error("Cevap status: unknown");
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.OCSP_CEVABI_KONTROLCU);
    }

}
