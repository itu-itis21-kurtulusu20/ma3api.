package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevocationCheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.FinderOCSPResponseSource;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.OCSPResponseFinderIteration;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.ocsp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;

import java.util.Calendar;

/**
 * Checks certificate's revocation status by using ocsp query to the OCSS
 * servers specified in the certificate
 */
public class RevocationFromOCSPChecker extends RevocationChecker {

    private static final Logger logger = LoggerFactory.getLogger(RevocationFromOCSPChecker.class);

    public enum RevocationFromOCSPCheckStatus implements CheckStatus
    {
        INVALID_OCSP_RESPONSE,
        OCSP_RESPONSE_NOT_FOUND,
        ISSUER_CERTIFICATE_NOT_FOUND,
        CERTIFICATE_VALID,
        CERTIFICATE_INVALID;

        public String getText()
        {
            switch (this) {
                case INVALID_OCSP_RESPONSE:
                    return CertI18n.message(CertI18n.OCSP_CEVABI_GECERSIZ);
                case OCSP_RESPONSE_NOT_FOUND:
                    return CertI18n.message(CertI18n.OCSP_CEVABI_BULUNAMADI);
                case ISSUER_CERTIFICATE_NOT_FOUND:
                    return CertI18n.message(CertI18n.SM_SERTIFIKASI_BULUNAMADI);
                case CERTIFICATE_VALID:
                    return CertI18n.message(CertI18n.SERTIFIKA_OCSPDE_GECERLI);
                case CERTIFICATE_INVALID:
                    return CertI18n.message(CertI18n.SERTIFIKA_OCSPDE_GECERLI_DEGIL);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    public static RevokeCheckStatus checkFromOCSP(ValidationSystem aParentSystem, CertificateStatusInfo aCertificateStatusInfo, EOCSPResponse ocspResp) throws ESYAException {

        if(aCertificateStatusInfo == null)
            return RevokeCheckStatus.CANT_CHECK;

        ECertificate cert = aCertificateStatusInfo.getCertificate();

        OCSPResponseController ocspKontrolcu = new OCSPResponseController();
        OCSPResponseStatusInfo pStatusInfo = ocspKontrolcu.check(aParentSystem, ocspResp);
        aCertificateStatusInfo.addOCSPResponseInfo(pStatusInfo);

        if (ocspResp.getResponseStatus() != OCSPResponseStatus._SUCCESSFUL)
            return RevokeCheckStatus.CANT_CHECK;

        if (ocspResp.getSingleResponseCount()==0)
            return RevokeCheckStatus.CANT_CHECK;

        ESingleResponse response =  ocspResp.getSingleResponse(0);

        if(pStatusInfo.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID) {
            if(response.getCertificateStatus() == CertStatus._GOOD) {
                aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(CertI18n.message(CertI18n.OCSPDEN_IPTAL_KONTROLU),
                        RevocationFromOCSPCheckStatus.CERTIFICATE_VALID.getText(), RevocationFromOCSPCheckStatus.CERTIFICATE_VALID, RevokeCheckStatus.NOT_REVOKED));
                return RevokeCheckStatus.NOT_REVOKED;
            }
            else if(response.getCertificateStatus() == CertStatus._REVOKED) {
                Calendar revocTime = response.getRevocationTime();
                Calendar baseValidationTime = aParentSystem.getBaseValidationTime();

                if(baseValidationTime.after(revocTime)) {
                    RevocationStatusInfo iptalDurumu = new RevocationStatusInfo();
                    iptalDurumu.setRevocationDate(response.getRevocationTime().getTime());
                    iptalDurumu.setRevocationCause(response.getRevokationReason());
                    aCertificateStatusInfo.setRevocationInfo(iptalDurumu);
                    aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(CertI18n.message(CertI18n.OCSPDEN_IPTAL_KONTROLU),
                            RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID.getText(), RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID, RevokeCheckStatus.REVOKED));
                    aCertificateStatusInfo.setCertificateStatus(CertificateStatus.REVOCATION_CHECK_FAILURE);

                    logger.debug(RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID.getText());

                    return RevokeCheckStatus.REVOKED;
                }
                else {
                    aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(CertI18n.message(CertI18n.OCSPDEN_IPTAL_KONTROLU),
                            RevocationFromOCSPCheckStatus.CERTIFICATE_VALID.getText(), RevocationFromOCSPCheckStatus.CERTIFICATE_VALID, RevokeCheckStatus.NOT_REVOKED));
                    return RevokeCheckStatus.NOT_REVOKED;
                }
            }
            else
                return RevokeCheckStatus.CANT_CHECK;
        }
        return RevokeCheckStatus.CANT_CHECK;
    }

    protected RevokeCheckStatus _check(ECertificate aIssuerCertificate,
                                       CertificateStatusInfo aCertificateStatusInfo)
            throws ESYAException
    {
        if (mParentSystem == null)
            return RevokeCheckStatus.CANT_CHECK;

        ECertificate cert = aCertificateStatusInfo.getCertificate();
        //FindSystem bs = mParentSystem.getFindSystem();

        OCSPResponseFinderIteration ocspFinder = new OCSPResponseFinderIteration(cert, aIssuerCertificate, mParentSystem);
        ocspFinder.addItemSource(new FinderOCSPResponseSource(cert, aIssuerCertificate, mFinders));

        RevokeCheckStatus rcs;
        while(ocspFinder.nextIteration(mParentSystem)) {
            EOCSPResponse ocspResp = ocspFinder.getCurrentItem();
            rcs = checkFromOCSP(mParentSystem, aCertificateStatusInfo, ocspResp);

            switch (rcs) {
                case REVOKED:
                    return RevokeCheckStatus.REVOKED;
                case NOT_REVOKED:
                    return RevokeCheckStatus.NOT_REVOKED;
                case CANT_CHECK:
                    continue;
            }
        }
        aCertificateStatusInfo.addRevocationCheckDetail( this, RevocationFromOCSPCheckStatus.OCSP_RESPONSE_NOT_FOUND, RevokeCheckStatus.CANT_CHECK);
        return RevokeCheckStatus.CANT_CHECK;
    }

    public String getCheckText() {
        return CertI18n.message(CertI18n.OCSPDEN_IPTAL_KONTROLU);
    }
}
