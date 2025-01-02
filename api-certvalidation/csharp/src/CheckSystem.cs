using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Container class for the Checker objects specified by ValidationPolicy
     */
    public class CheckSystem : ICloneable
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private IssuerCheckParameters mConstraintCheckParam = new IssuerCheckParameters();

        private List<CertificateSelfChecker> mTrustedCertificateCheckers = new List<CertificateSelfChecker>();
        private List<CertificateSelfChecker> mCertificateSelfCheckers = new List<CertificateSelfChecker>();
        private List<IssuerChecker> mIssuerCheckers = new List<IssuerChecker>();
        private List<RevocationChecker> mRevocationCheckers = new List<RevocationChecker>();
        private List<CRLSelfChecker> mCRLSelfCheckers = new List<CRLSelfChecker>();
        private List<CRLIssuerChecker> mCRLIssuerCheckers = new List<CRLIssuerChecker>();
        private List<OCSPResponseChecker> mOCSPResponseCheckers = new List<OCSPResponseChecker>();
        private List<DeltaCRLChecker> mDeltaCRLCheckers = new List<DeltaCRLChecker>();

        private bool mAllChecksRequired;

        /**
         * ConstraintKontrolParam döner
         */
        public IssuerCheckParameters getConstraintCheckParam()
        {
            return mConstraintCheckParam;
        }


        /**
         * ConstraintKontrolParam belirler
         */
        public void setConstraintCheckParam(IssuerCheckParameters aConstraintCheckParam)
        {
            mConstraintCheckParam = aConstraintCheckParam;
        }


        public List<CertificateSelfChecker> getTrustedCertificateCheckers()
        {
            return mTrustedCertificateCheckers;
        }


        public void setTrustedCertificateCheckers(List<CertificateSelfChecker> aGSKontrolcu)
        {
            mTrustedCertificateCheckers = aGSKontrolcu;
        }

        public void addTrustedCertificateChecker(CertificateSelfChecker aGSKontrolcu)
        {
            mTrustedCertificateCheckers.Add(aGSKontrolcu);
        }

        public List<CertificateSelfChecker> getCertificateSelfCheckers()
        {
            return mCertificateSelfCheckers;
        }

        public void setCertificateSelfCheckers(List<CertificateSelfChecker> aTSKontrolcu)
        {
            mCertificateSelfCheckers = aTSKontrolcu;
        }

        public void addCertificateSelfChecker(CertificateSelfChecker aTSKontrolcu)
        {
            mCertificateSelfCheckers.Add(aTSKontrolcu);
        }

        public List<IssuerChecker> getIssuerCheckers()
        {
            return mIssuerCheckers;
        }

        public void setIssuerCheckers(List<IssuerChecker> aUstSMKontrolcu)
        {
            mIssuerCheckers = aUstSMKontrolcu;
        }

        public void addIssuerChecker(IssuerChecker aUstSMKontrolcu)
        {
            mIssuerCheckers.Add(aUstSMKontrolcu);
        }

        /*    public void ustSMKontrolcuCikar(Class iClass)
            {
                for (int i = 0; i < mIssuerCheckers.size(); i++) {
                    if (mIssuerCheckers.get(i).getClass().equals(iClass)) {
                        mIssuerCheckers.remove(i);
                    }
                }
            }     */

        public List<RevocationChecker> getRevocationCheckers()
        {
            return mRevocationCheckers;
        }


        public void setRevocationCheckers(List<RevocationChecker> aIptalKontrolcu)
        {
            mRevocationCheckers = aIptalKontrolcu;
        }

        public void addRevocationChecker(RevocationChecker aIptalKontrolcu)
        {
            mRevocationCheckers.Add(aIptalKontrolcu);
        }

        public List<CRLSelfChecker> getCRLSelfCheckers()
        {
            return mCRLSelfCheckers;
        }

        public void setCRLSelfCheckers(List<CRLSelfChecker> aCRLSelfChecker)
        {
            mCRLSelfCheckers = aCRLSelfChecker;
        }

        public void addCRLSelfChecker(CRLSelfChecker aCRLSelfChecker)
        {
            mCRLSelfCheckers.Add(aCRLSelfChecker);
        }

        public List<CRLIssuerChecker> getCRLIssuerCheckers()
        {
            return mCRLIssuerCheckers;
        }

        public void setCRLIssuerCheckers(List<CRLIssuerChecker> aSilSMKontrolcu)
        {
            mCRLIssuerCheckers = aSilSMKontrolcu;
        }

        public void addCRLIssuerChecker(CRLIssuerChecker aSilSMKontrolcu)
        {
            mCRLIssuerCheckers.Add(aSilSMKontrolcu);
        }

        public List<OCSPResponseChecker> getOCSPResponseCheckers()
        {
            return mOCSPResponseCheckers;
        }

        public void setOCSPResponseCheckers(List<OCSPResponseChecker> aOCSPCevabiKontrolcu)
        {
            mOCSPResponseCheckers = aOCSPCevabiKontrolcu;
        }

        public void addOCSPResponseChecker(OCSPResponseChecker aOCSPCevabiKontrolcu)
        {
            mOCSPResponseCheckers.Add(aOCSPCevabiKontrolcu);
        }

        public List<DeltaCRLChecker> getDeltaCRLCheckers()
        {
            return mDeltaCRLCheckers;
        }

        public void setDeltaCRLCheckers(List<DeltaCRLChecker> aDeltaSilKontrolcu)
        {
            mDeltaCRLCheckers = aDeltaSilKontrolcu;
        }

        public void addDeltaCRLChecker(DeltaCRLChecker aDeltaSilKontrolcu)
        {
            mDeltaCRLCheckers.Add(aDeltaSilKontrolcu);
        }


        /**
         * mTumKontrolleriYap alanını döner
         */
        public bool isAllChecksRequired()
        {
            return mAllChecksRequired;
        }

        /**
         * mTumKontrolleriYap alanını belirler
         */
        public void setAllChecksRequired(bool aTKY)
        {
            mAllChecksRequired = aTKY;
        }


        public PathValidationResult guvenilirSertifikaKontrolleriYap(CertificateStatusInfo aCertStatusInfo)
        {
            PathValidationResult b, result = PathValidationResult.SUCCESS;
            foreach (CertificateSelfChecker gsk in mTrustedCertificateCheckers)
            {
                b = gsk.check(aCertStatusInfo);
                if (b != PathValidationResult.SUCCESS && gsk.isCritical())
                {
                    if (mAllChecksRequired)
                        result = b;
                    else return b;
                }
            }
            return result;
        }

        public PathValidationResult checkCertificateSelf(CertificateStatusInfo aCertStatusInfo)
        {
            PathValidationResult b, result = PathValidationResult.SUCCESS;
            foreach (CertificateSelfChecker gsk in mCertificateSelfCheckers)
            {
                b = gsk.check(aCertStatusInfo);
                if (b != PathValidationResult.SUCCESS && gsk.isCritical())
                {
                    logger.Error(gsk.GetType().FullName + "failed.");
                    if (mAllChecksRequired)
                        result = b;
                    else return b;
                }
            }
            return result;
        }

        public PathValidationResult checkIssuer(IssuerCheckParameters aIssuerCheckParams,
                                                     ECertificate aIssuerCertificate,
                                                     ECertificate aCertificate,
                                                     CertificateStatusInfo aCertStatusInfo)
        {
            PathValidationResult b, result = PathValidationResult.SUCCESS;
            foreach (IssuerChecker issuerChecker in mIssuerCheckers)
            {
                b = issuerChecker.check(aIssuerCheckParams, aIssuerCertificate, aCertificate, aCertStatusInfo);
                if (b != PathValidationResult.SUCCESS && issuerChecker.isCritical())
                {
                    if (mAllChecksRequired)
                        result = b;
                    else return b;
                }
            }
            return result;
        }

        public PathValidationResult checkCRLSelf(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
        {
            PathValidationResult b, result = PathValidationResult.SUCCESS;
            foreach (CRLSelfChecker tsk in mCRLSelfCheckers)
            {
                b = tsk.check(aCRL, aCRLStatusInfo);
                if (b != PathValidationResult.SUCCESS && tsk.isCritical())
                {
                    logger.Error(tsk.GetType().FullName + "failed.");
                    if (mAllChecksRequired)
                        result = b;
                    else return b;
                }
            }
            return result;
        }

        public PathValidationResult checkCRLIssuer(IssuerCheckParameters aIssuerCheckParams,
                                                   ECRL aCRL,
                                                   ECertificate aIssuerCertificate,
                                                   CRLStatusInfo aCRLStatusInfo)
        {
            PathValidationResult b, result = PathValidationResult.SUCCESS;
            foreach (CRLIssuerChecker ssk in mCRLIssuerCheckers)
            {
                b = ssk.check(aIssuerCheckParams, aCRL, aIssuerCertificate, aCRLStatusInfo);
                if (b != PathValidationResult.SUCCESS && ssk.isCritical())
                {
                    if (mAllChecksRequired)
                        result = b;
                    else return b;
                }
            }
            return result;
        }

        public PathValidationResult checkRevocation(ECertificate aIssuerCertificate,
                                                    CertificateStatusInfo aCertStatusInfo)
        {
            RevokeCheckStatus result = RevokeCheckStatus.NOT_REVOKED;
            bool hasControlledOnce = false;
            ECertificate certificate = aCertStatusInfo.getCertificate();

            if (certificate.isOCSPSigningCertificate())
                if (certificate.getExtensions().getExtension(EExtensions.oid_pkix_ocsp_nocheck) != null)
                    return PathValidationResult.SUCCESS;
        
            if (logger.IsDebugEnabled)
            {
                logger.Debug("-- Check revocation for: \n" + certificate.getSubject().stringValue());
            }
            if(certificate.isOCSPSigningCertificate())
            {
                if (certificate.hasOCSPNoCheckExtention())
                {
                    logger.Debug("Certificate has oid_pkix_ocsp_nocheck extension.");
                    return PathValidationResult.SUCCESS;
                }
            }
            foreach (RevocationChecker isk in mRevocationCheckers)
            {
                logger.Debug("Checker: " + isk);
                result = isk.check(aIssuerCertificate, aCertStatusInfo);

                if (result == RevokeCheckStatus.REVOKED)
                {
                    logger.Error("Sertifika iptal kontrol hatası.");
                    return PathValidationResult.CERTIFICATE_REVOKED;
                }
                else if (result == RevokeCheckStatus.NOT_REVOKED)
                {
                    logger.Debug("Not revoked!");
                    hasControlledOnce = true;
                    if (!isk.isContinue())
                    {
                        return PathValidationResult.SUCCESS;
                    }
                }
                else if (result == RevokeCheckStatus.CANT_CHECK)
                {
                    logger.Debug("Cant check!");
                }
            }
            if (result == RevokeCheckStatus.NOT_REVOKED || hasControlledOnce)
            {
                if (logger.IsDebugEnabled)
                {
                    logger.Debug("Not revoked : " + certificate.getSubject().stringValue());
                }

                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Error("Certificate revocation info cannot be checked for  :" + certificate.getSubject().stringValue());
                return PathValidationResult.REVOCATION_CONTROL_FAILURE;// KONTROL EDILEMEDI
            }
        }


        public PathValidationResult checkOCSPResponse(OCSPResponseStatusInfo aOCSPResponseStatusInfo,
                                                      EOCSPResponse aOCSPResponse)
        {
            PathValidationResult b, result = PathValidationResult.SUCCESS;
            foreach (OCSPResponseChecker osk in mOCSPResponseCheckers)
            {
                b = osk.check(aOCSPResponseStatusInfo, aOCSPResponse);
                if (b != PathValidationResult.SUCCESS && osk.isCritical())
                {
                    logger.Error(osk.GetType().FullName + "failed.");
                    if (mAllChecksRequired)
                        result = b;
                    else return b;
                }
            }
            return result;
        }

        public PathValidationResult checkDeltaCRL(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
        {
            PathValidationResult b, result = PathValidationResult.SUCCESS;
            foreach (CRLSelfChecker dsk in mDeltaCRLCheckers)
            {
                b = dsk.check(aCRL, aCRLStatusInfo);
                if (b != PathValidationResult.SUCCESS && dsk.isCritical())
                {
                    if (mAllChecksRequired)
                        result = b;
                    else return b;
                }
            }
            return result;
        }

        public Object Clone()
        {
            CheckSystem cs = (CheckSystem) base.MemberwiseClone();

            List<RevocationChecker> rcCloneList = new List<RevocationChecker>();

            foreach (RevocationChecker rc in mRevocationCheckers)
            {
                rcCloneList.Add((RevocationChecker)rc.Clone());
            }

            cs.setRevocationCheckers(rcCloneList);
            return cs;
        }
    }
}
