package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.FindSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevocationCheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.ocsp.CertStatus;

import java.util.Calendar;

/**
 * Checks certificate's revocation status by using ocsp query to the OCSS
 * servers specified in the certificate 
 */
public class RevocationFromInitialOCSPChecker extends RevocationChecker
{
    private static final Logger logger = LoggerFactory.getLogger(RevocationFromInitialOCSPChecker.class);

    /** todo refactor with OCSP checker
     * Checks certificate revocation status from OCSP
     */
    protected RevokeCheckStatus _check(ECertificate aIssuerCertificate,
                                       CertificateStatusInfo aCertificateStatusInfo)
            throws ESYAException
    {
        if (mParentSystem == null)
            return RevokeCheckStatus.CANT_CHECK;

        ECertificate cert = aCertificateStatusInfo.getCertificate();
        FindSystem bs = mParentSystem.getFindSystem();

        EOCSPResponse ocspResponse = bs.findOCSPResponseFromInitial(mParentSystem, aIssuerCertificate, aCertificateStatusInfo);

        if (ocspResponse == null) {
            logger.debug("Cant find OCSP response for "+cert.getSubject().stringValue());
            aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCheckText(), RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.OCSP_RESPONSE_NOT_FOUND.getText(), RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.OCSP_RESPONSE_NOT_FOUND, RevokeCheckStatus.CANT_CHECK));
            return RevokeCheckStatus.CANT_CHECK;
        }
        OCSPResponseController ocspKontrolcu = new OCSPResponseController();
        OCSPResponseStatusInfo pDurumBilgi = ocspKontrolcu.check(mParentSystem, ocspResponse);
        aCertificateStatusInfo.addOCSPResponseInfo(pDurumBilgi);

        ESingleResponse response = ocspResponse.getSingleResponse();
        ocspResponse = null;

        if (pDurumBilgi.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID) {
            if (response.getCertificateStatus() == CertStatus._GOOD) {
                aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.CERTIFICATE_VALID,  RevokeCheckStatus.NOT_REVOKED);
                return RevokeCheckStatus.NOT_REVOKED;
            }
            else if(response.getCertificateStatus() == CertStatus._REVOKED)
            {
            	Calendar revocTime = response.getRevocationTime();
            	Calendar baseValidationTime = mParentSystem.getBaseValidationTime();
            	if(baseValidationTime.after(revocTime))
            	{
            		 RevocationStatusInfo iptalDurumu = new RevocationStatusInfo();
                     iptalDurumu.setRevocationDate(response.getRevocationTime().getTime());
                     iptalDurumu.setRevocationCause(response.getRevokationReason());
                     aCertificateStatusInfo.setRevocationInfo(iptalDurumu);
                     aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID, RevokeCheckStatus.REVOKED);

                     return RevokeCheckStatus.REVOKED;
            	}
            	else
            	{
            		 aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPCheckStatus.CERTIFICATE_VALID,  RevokeCheckStatus.NOT_REVOKED);
                     return RevokeCheckStatus.NOT_REVOKED;
            	}
            }
        }
        aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.INVALID_OCSP_RESPONSE, RevokeCheckStatus.CANT_CHECK);
        return RevokeCheckStatus.CANT_CHECK;
    }


    public String getCheckText()
    {
        return CertI18n.message(CertI18n.OCSPDEN_IPTAL_KONTROLU);
    }

}

