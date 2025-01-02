using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * Checks certificate's revocation status by using ocsp query to the OCSS
     * servers specified in the certificate 
     */
    public class RevocationFromOCSPChecker : RevocationChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        [Serializable]
        public class RevocationFromOCSPCheckStatus : CheckStatus
        {
            public static readonly RevocationFromOCSPCheckStatus INVALID_OCSP_RESPONSE = new RevocationFromOCSPCheckStatus(_enum.INVALID_OCSP_RESPONSE);
            public static readonly RevocationFromOCSPCheckStatus OCSP_RESPONSE_NOT_FOUND = new RevocationFromOCSPCheckStatus(_enum.OCSP_RESPONSE_NOT_FOUND);
            public static readonly RevocationFromOCSPCheckStatus ISSUER_CERTIFICATE_NOT_FOUND = new RevocationFromOCSPCheckStatus(_enum.ISSUER_CERTIFICATE_NOT_FOUND);
            public static readonly RevocationFromOCSPCheckStatus CERTIFICATE_VALID = new RevocationFromOCSPCheckStatus(_enum.CERTIFICATE_VALID);
            public static readonly RevocationFromOCSPCheckStatus CERTIFICATE_INVALID = new RevocationFromOCSPCheckStatus(_enum.CERTIFICATE_INVALID);

            enum _enum
            {
                INVALID_OCSP_RESPONSE,
                OCSP_RESPONSE_NOT_FOUND,
                ISSUER_CERTIFICATE_NOT_FOUND,
                CERTIFICATE_VALID,
                CERTIFICATE_INVALID
            }

            readonly _enum mValue;

            RevocationFromOCSPCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }

            public String getText()
            {
                switch (mValue)
                {
                    case _enum.INVALID_OCSP_RESPONSE:
                        return Resource.message(Resource.OCSP_CEVABI_GECERSIZ);
                    case _enum.OCSP_RESPONSE_NOT_FOUND:
                        return Resource.message(Resource.OCSP_CEVABI_BULUNAMADI);
                    case _enum.ISSUER_CERTIFICATE_NOT_FOUND:
                        return Resource.message(Resource.SM_SERTIFIKASI_BULUNAMADI);
                    case _enum.CERTIFICATE_VALID:
                        return Resource.message(Resource.SERTIFIKA_OCSPDE_GECERLI);
                    case _enum.CERTIFICATE_INVALID:
                        return Resource.message(Resource.SERTIFIKA_OCSPDE_GECERLI_DEGIL);
                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        public static RevokeCheckStatus checkFromOCSP(ValidationSystem aParentSystem, CertificateStatusInfo aCertificateStatusInfo, EOCSPResponse ocspResp)
        {
            if(aCertificateStatusInfo == null)
                return RevokeCheckStatus.CANT_CHECK;

            ECertificate cert = aCertificateStatusInfo.getCertificate();

            OCSPResponseController ocspKontrolcu = new OCSPResponseController();
            OCSPResponseStatusInfo pStatusInfo = ocspKontrolcu.check(aParentSystem, ocspResp);
            aCertificateStatusInfo.addOCSPResponseInfo(pStatusInfo);

            if (ocspResp.getResponseStatus() != OCSPResponseStatus.successful().mValue)
                return RevokeCheckStatus.CANT_CHECK;

            if (ocspResp.getSingleResponseCount()==0)
                return RevokeCheckStatus.CANT_CHECK;

            ESingleResponse response =  ocspResp.getSingleResponse(0);

            if(pStatusInfo.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID) {
                if(response.getCertificateStatus() == CertStatus._GOOD) {
                    aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(Resource.message(Resource.OCSPDEN_IPTAL_KONTROLU),
                            RevocationFromOCSPCheckStatus.CERTIFICATE_VALID.getText(), RevocationFromOCSPCheckStatus.CERTIFICATE_VALID, RevokeCheckStatus.NOT_REVOKED));
                    return RevokeCheckStatus.NOT_REVOKED;
                }
                else if(response.getCertificateStatus() == CertStatus._REVOKED) {
                    DateTime? revocTime = response.getRevocationTime();
                    DateTime? baseValidationTime = aParentSystem.getBaseValidationTime();

                    if ( baseValidationTime > revocTime ) {
                        RevocationStatusInfo iptalDurumu = new RevocationStatusInfo();
                        iptalDurumu.setRevocationDate(response.getRevocationTime());
                        iptalDurumu.setRevocationCause(response.getRevocationReason());
                        aCertificateStatusInfo.setRevocationInfo(iptalDurumu);
                        aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(Resource.message(Resource.OCSPDEN_IPTAL_KONTROLU),
                                RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID.getText(), RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID, RevokeCheckStatus.REVOKED));
                        aCertificateStatusInfo.setCertificateStatus(CertificateStatus.REVOCATION_CHECK_FAILURE);

                        logger.Debug(RevocationFromOCSPCheckStatus.CERTIFICATE_INVALID.getText());

                        return RevokeCheckStatus.REVOKED;
                    }
                    else {
                        aCertificateStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(Resource.message(Resource.OCSPDEN_IPTAL_KONTROLU),
                                RevocationFromOCSPCheckStatus.CERTIFICATE_VALID.getText(), RevocationFromOCSPCheckStatus.CERTIFICATE_VALID, RevokeCheckStatus.NOT_REVOKED));
                        return RevokeCheckStatus.NOT_REVOKED;
                    }
                }
                else
                    return RevokeCheckStatus.CANT_CHECK;
            }
            return RevokeCheckStatus.CANT_CHECK;
        }

        protected override RevokeCheckStatus _check(ECertificate aIssuerCertificate,
                                       CertificateStatusInfo aCertificateStatusInfo)
        {
            if (mParentSystem == null)
                return RevokeCheckStatus.CANT_CHECK;

            ECertificate cert = aCertificateStatusInfo.getCertificate();
            //FindSystem bs = mParentSystem.getFindSystem();

            OCSPResponseFinderIteration ocspFinder = new OCSPResponseFinderIteration(cert, aIssuerCertificate, mParentSystem);
            ocspFinder.addItemSource(new FinderOCSPResponseSource<Finder>(cert, aIssuerCertificate, mFinders));

            RevokeCheckStatus rcs;
            while(ocspFinder.nextIteration(mParentSystem)) {
                EOCSPResponse ocspResp = ocspFinder.getCurrentItem();
                rcs = checkFromOCSP(mParentSystem, aCertificateStatusInfo, ocspResp);

                /*switch (rcs) {
                    case REVOKED:
                        return RevokeCheckStatus.REVOKED;
                    case NOT_REVOKED:
                        return RevokeCheckStatus.NOT_REVOKED;
                    case CANT_CHECK:
                        continue;
                }*/

                if (rcs == RevokeCheckStatus.REVOKED)
                    return RevokeCheckStatus.REVOKED;
                else if (rcs == RevokeCheckStatus.NOT_REVOKED)
                    return RevokeCheckStatus.NOT_REVOKED;
                else
                    continue;
            }
            aCertificateStatusInfo.addRevocationCheckDetail( this, RevocationFromOCSPCheckStatus.OCSP_RESPONSE_NOT_FOUND, RevokeCheckStatus.CANT_CHECK);
            return RevokeCheckStatus.CANT_CHECK;
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.OCSPDEN_IPTAL_KONTROLU);
        }
    }
}
