using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * Checks certificate's revocation status by using ocsp query to the OCSS
     * servers specified in the certificate 
     */
    public class RevocationFromInitialOCSPChecker : RevocationChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /** todo refactor with OCSP checker
         * Checks certificate revocation status from OCSP
         */
        protected override RevokeCheckStatus _check(ECertificate aIssuerCertificate,
                                           CertificateStatusInfo aCertificateStatusInfo)
        {
            if (mParentSystem == null)
                return RevokeCheckStatus.CANT_CHECK;

            ECertificate cert = aCertificateStatusInfo.getCertificate();
            FindSystem bs = mParentSystem.getFindSystem();

            EOCSPResponse ocspResponse = bs.findOCSPResponseFromInitial(mParentSystem, aIssuerCertificate, aCertificateStatusInfo);

            if (ocspResponse == null)
            {
                logger.Debug("Cant find OCSP response for " + cert.getSubject().stringValue());
                aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCheckText(), RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.OCSP_RESPONSE_NOT_FOUND.getText(), RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.OCSP_RESPONSE_NOT_FOUND, RevokeCheckStatus.CANT_CHECK));
                return RevokeCheckStatus.CANT_CHECK;
            }
            OCSPResponseController ocspKontrolcu = new OCSPResponseController();
            OCSPResponseStatusInfo pDurumBilgi = ocspKontrolcu.check(mParentSystem, ocspResponse);
            aCertificateStatusInfo.addOCSPResponseInfo(pDurumBilgi);

            ESingleResponse response = ocspResponse.getSingleResponse();
            ocspResponse = null;

            if (pDurumBilgi.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID)
            {
                if (response.getCertificateStatus() == CertStatus._GOOD)
                {
                    aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.CERTIFICATE_VALID, RevokeCheckStatus.NOT_REVOKED);
                    return RevokeCheckStatus.NOT_REVOKED;
                }
                else if(response.getCertificateStatus() == CertStatus._REVOKED)
                {
                    DateTime? revocTime = response.getRevocationTime();
                    DateTime? baseValidationTime = mParentSystem.getBaseValidationTime();

                    if (baseValidationTime.Value.CompareTo(revocTime) > 0)
                    {
                        RevocationStatusInfo iptalDurumu = new RevocationStatusInfo();
                        iptalDurumu.setRevocationDate(response.getRevocationTime());
                        iptalDurumu.setRevocationCause(response.getRevocationReason());
                        aCertificateStatusInfo.setRevocationInfo(iptalDurumu);
                        aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID, RevokeCheckStatus.REVOKED);

                        return RevokeCheckStatus.REVOKED;
                    }
                    else
                    {
                        aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.CERTIFICATE_VALID, RevokeCheckStatus.NOT_REVOKED);
                        return RevokeCheckStatus.NOT_REVOKED;
                    }
                }
            }
            aCertificateStatusInfo.addRevocationCheckDetail(this, RevocationFromOCSPChecker.RevocationFromOCSPCheckStatus.INVALID_OCSP_RESPONSE, RevokeCheckStatus.CANT_CHECK);
            return RevokeCheckStatus.CANT_CHECK;
        }


        public override String getCheckText()
        {
            return Resource.message(Resource.OCSPDEN_IPTAL_KONTROLU);
        }

    }
}
