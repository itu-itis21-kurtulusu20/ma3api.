package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.asn.x509.Extension;

/**
 * Checks the validity of the following condition for the delta-CRL RFC 3280
 * 5.2.6 Freshest CRL says: This extension MUST NOT appear in delta CRLs.
 */
public class FreshestCRLChecker extends DeltaCRLChecker {

    private static Logger logger = LoggerFactory.getLogger(FreshestCRLChecker.class);

    public enum FreshestCRLCheckStatus implements CheckStatus {

        FRESHEST_CRL_EXISTS,
        FRESHEST_CRL_NOT_EXISTS;

        public String getText()
        {
            switch (this) {
                case FRESHEST_CRL_EXISTS:
                    return CertI18n.message(CertI18n.FRESHEST_CRL_VAR);
                case FRESHEST_CRL_NOT_EXISTS:
                    return CertI18n.message(CertI18n.FRESHEST_CRL_YOK);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * FreshestCRL eklentisini kontrol eder
     */
    protected PathValidationResult _check(ECRL aDeltaCRL, CRLStatusInfo aCRLStatusInfo)
    {
        Extension extension = aDeltaCRL.getCRLExtensions().getFreshestCRL();
        if (extension == null) {
            logger.debug("Delta silde FreshestCRL extension yok");
            aCRLStatusInfo.addDetail(this, FreshestCRLCheckStatus.FRESHEST_CRL_NOT_EXISTS, true);
            return PathValidationResult.SUCCESS;
        }
        logger.error("Delta silde FreshestCRL extension var");
        aCRLStatusInfo.addDetail(this, FreshestCRLCheckStatus.FRESHEST_CRL_EXISTS, false);
        return PathValidationResult.CRL_FRESHESTCRL_CONTROL_FAILURE;
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.FRESHEST_CRL_KONTROLU);
    }

}
