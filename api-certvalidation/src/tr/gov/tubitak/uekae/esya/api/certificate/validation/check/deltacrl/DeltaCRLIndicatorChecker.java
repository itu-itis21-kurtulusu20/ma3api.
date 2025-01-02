package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;

/**
 * Checks the validity of the given delta-CRL by validating the following condition;
 * 
 * RFC 3280 5.2.4 Delta CRL Indicator says:
 * When a conforming CRL issuer generates a delta CRL, the delta CRL
 * MUST include a critical delta CRL indicator extension.
 * @author IH
 */
public class DeltaCRLIndicatorChecker extends DeltaCRLChecker
{
    private static final Logger logger = LoggerFactory.getLogger(DeltaCRLIndicatorChecker.class);

    public enum DeltaCRLIndicatorcheckStatus implements CheckStatus
    {

        DELTA_CRL_INDICATOR_EXISTS,
        DELTA_CRL_INDICATOR_NOT_EXISTS;

        public String getText()
        {
            switch (this) {
                case DELTA_CRL_INDICATOR_EXISTS:
                    return CertI18n.message(CertI18n.DELTA_CRL_INDICATOR_VAR);
                case DELTA_CRL_INDICATOR_NOT_EXISTS:
                    return CertI18n.message(CertI18n.DELTA_CRL_INDICATOR_YOK);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * DeltaCRLIndicator eklentisini kontrol eder
     */
    protected PathValidationResult _check(ECRL aDeltaCRL, CRLStatusInfo aCRLStatusInfo)
    {
        EExtension extension = aDeltaCRL.getCRLExtensions().getDeltaCRLIndicator();
        if (extension == null) {
            logger.error("Delta silde Delta CRL Indicator extension yok");
            aCRLStatusInfo.addDetail(this, DeltaCRLIndicatorcheckStatus.DELTA_CRL_INDICATOR_NOT_EXISTS, false);
            return PathValidationResult.CRL_DELTACRLINDICATOR_CONTROL_FAILURE;
        }
        if (logger.isDebugEnabled())
            logger.debug("Delta silde Delta CRL Indicator extension var");
        aCRLStatusInfo.addDetail(this, DeltaCRLIndicatorcheckStatus.DELTA_CRL_INDICATOR_EXISTS, true);
        return PathValidationResult.SUCCESS;
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.DELTA_CRL_INDICATOR_KONTROLU);
    }

}
