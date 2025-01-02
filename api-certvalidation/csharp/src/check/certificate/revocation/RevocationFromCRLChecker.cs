using System;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * Checks certificate's revocation status by using the matching CRLs which are
     * found by the finders specified in the validation policy
     */
    public class RevocationFromCRLChecker : RevocationChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static readonly String CEVRIM_DISI_CALIS = "cevrimdisicalis";
        public static readonly String CHECK_ALL_CRLS = "checkallcrls";
        public static readonly String PREVIOUSANDNEXT = "checkpreviousandnext";
        
        [Serializable]
        public class RevocationFromCRLCheckStatus : CheckStatus
        {

            public static readonly RevocationFromCRLCheckStatus INVALID_CRL = new RevocationFromCRLCheckStatus(_enum.INVALID_CRL);
            public static readonly RevocationFromCRLCheckStatus CERTIFICATE_IN_LIST = new RevocationFromCRLCheckStatus(_enum.CERTIFICATE_IN_LIST);
            public static readonly RevocationFromCRLCheckStatus CERTIFICATE_NOT_IN_LIST = new RevocationFromCRLCheckStatus(_enum.CERTIFICATE_NOT_IN_LIST);
            public static readonly RevocationFromCRLCheckStatus CRL_NOT_FOUND = new RevocationFromCRLCheckStatus(_enum.CRL_NOT_FOUND);
            public static readonly RevocationFromCRLCheckStatus HOLD_ON_BASE_REMOVED_ON_DELTA = new RevocationFromCRLCheckStatus(_enum.HOLD_ON_BASE_REMOVED_ON_DELTA);
            public static readonly RevocationFromCRLCheckStatus CERTIFICATE_ON_DELTA_CRL = new RevocationFromCRLCheckStatus(_enum.CERTIFICATE_ON_DELTA_CRL);
            public static readonly RevocationFromCRLCheckStatus CERTIFICATE_REMOVED_ON_DELTA_CRL = new RevocationFromCRLCheckStatus(_enum.CERTIFICATE_REMOVED_ON_DELTA_CRL);

            enum _enum
            {
                INVALID_CRL,
                CERTIFICATE_IN_LIST,
                CERTIFICATE_NOT_IN_LIST,
                CRL_NOT_FOUND,
                HOLD_ON_BASE_REMOVED_ON_DELTA,
                CERTIFICATE_ON_DELTA_CRL,
                CERTIFICATE_REMOVED_ON_DELTA_CRL
            }
            readonly _enum mValue;
            RevocationFromCRLCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.INVALID_CRL:
                        return Resource.message(Resource.SIL_GECERSIZ);
                    case _enum.CERTIFICATE_IN_LIST:
                        return Resource.message(Resource.SERTIFIKA_LISTEDE);
                    case _enum.CERTIFICATE_NOT_IN_LIST:
                        return Resource.message(Resource.SERTIFIKA_LISTEDE_DEGIL);
                    case _enum.CRL_NOT_FOUND:
                        return Resource.message(Resource.SIL_BULUNAMADI);
                    case _enum.HOLD_ON_BASE_REMOVED_ON_DELTA:
                        return Resource.message(Resource.BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS);
                    case _enum.CERTIFICATE_ON_DELTA_CRL:
                        return Resource.message(Resource.SERTIFIKA_DELTA_SILDE);
                    case _enum.CERTIFICATE_REMOVED_ON_DELTA_CRL:
                        return Resource.message(Resource.SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }


        public static readonly int CRL_REASON_UNSPECIFIED = 0;
        public static readonly int CRL_REASON_KEY_COMPROMISE = 1;
        public static readonly int CRL_REASON_CA_COMPROMISE = 2;
        public static readonly int CRL_REASON_AFFILIATION_CHANGED = 3;
        public static readonly int CRL_REASON_SUPERSEDED = 4;
        public static readonly int CRL_REASON_CESSATION_OF_OPERATION = 5;
        public static readonly int CRL_REASON_CERTIFICATE_HOLD = 6;
        public static readonly int CRL_REASON_REMOVE_FROM_CRL = 8;

        public static RevokeCheckStatus checkFromCRL(ValidationSystem aParentSystem, CertificateStatusInfo aCertStatusInfo,
                                                 ECRL crl, ECRL deltaCRL, RevocationCoverage rc)
        {

            RevocationStatusInfo revocationStatusInfo = new RevocationStatusInfo();
            revocationStatusInfo.setRevocationStatus(RevocationStatus.VALID);
            int baseListeNo = -1;
            int deltaListeNo = -1;

            ECertificate certificate = aCertStatusInfo.getCertificate();

            baseListeNo = _listedeVarMi(crl, certificate, aParentSystem.getBaseValidationTime());

            rc.mHistoricalCoverage = !aParentSystem.isDoNotUsePastRevocationInfo();

            //_updateCoverage(crl, cert, reasonCoverage, certTypeCoverage, coveredReasons);
            // start update coverage
            if (!(rc.mReasonCoverage && rc.mCertTypeCoverage && rc.mHistoricalCoverage))
            {

                if (!rc.mHistoricalCoverage)
                {
                    //Calendar due = crl.getNextUpdate();
                    DateTime? from = crl.getThisUpdate();

                    if ( from < aParentSystem.getBaseValidationTime() )
                        rc.mPreviousCRLCoverage = true;
                    else
                        rc.mNextCRLCoverage = true;

                    if (rc.mNextCRLCoverage && rc.mPreviousCRLCoverage)
                        rc.mHistoricalCoverage = true;
                }

                EIssuingDistributionPoint idp = crl.getCRLExtensions().getIssuingDistributionPoint();

                if (idp == null)
                {
                    rc.mReasonCoverage = true;
                    rc.mCertTypeCoverage = true;
                }
                else
                {
                    if (idp.isOnlyContainsAttributeCerts())
                    {
                        rc.mCertTypeCoverage = false;
                    }
                    else if ((idp.isOnlyContainsCACerts() && certificate.isCACertificate())
                            || ((idp.isOnlyContainsUserCerts() && !certificate.isCACertificate()))
                            || (!idp.isOnlyContainsCACerts() && !idp.isOnlyContainsUserCerts()))
                    {
                        rc.mCertTypeCoverage = true;
                    }

                    if (!rc.mReasonCoverage)
                    {
                        //      if (!idp.isOnlySomeReasonsPresent()) { todo?
                        if (idp.getOnlySomeReasons() == null)
                        {
                            rc.mReasonCoverage = true;
                        }
                        else
                        {
                            ReasonFlags rf = idp.getOnlySomeReasons();
                            //String bits = idp.getOnlySomeReasons().toString();//getOnlySomeReasons().toBitString();

                            try
                            {
                                for (int i = 0; i < rf.Length; i++)
                                {
                                    rc.mCoveredReasons[i] = rc.mCoveredReasons[i] || (rf.Get(i));
                                }
                            }
                            catch (Asn1InvalidLengthException x)
                            {
                                Console.WriteLine(x.StackTrace);
                            }

                            rc.mReasonCoverage = rc.mCoveredReasons[0];
                            for (int i = 0; i < 9; i++)
                            {
                                rc.mReasonCoverage = rc.mReasonCoverage && rc.mCoveredReasons[i];
                            }
                        }
                    }
                }
            }
            // end update coverage

            //_updateCoverage(crl, cert, reasonCoverage, certTypeCoverage, coveredReasons);

            if (deltaCRL != null)
            {
                deltaListeNo = _listedeVarMi(deltaCRL, certificate, aParentSystem.getBaseValidationTime());
            }

            if (baseListeNo != -1)
            {    // Certificate exists in the CRL
                ERevokedCertificateElement revCertElm = crl.getRevokedCerticateElement(baseListeNo);
                int crlReason = revCertElm.getCRLReason();
                // Suspended in Base-CRL
                if (crlReason == CRL_REASON_CERTIFICATE_HOLD)
                {
                    // Check the detail
                    if (deltaListeNo != -1)
                    {
                        // todo baseListeNo mu deltaListeNo mu?
                        int deltaCRLReason = deltaCRL.getRevokedCerticateElement(deltaListeNo).getCRLReason();
                        // Removed from Delta-CRL
                        // X.509 Page 36 says:
                        // An entry with this reason code shall be used in
                        // delta-CRLs for which the corresponding base CRL or any subsequent (delta or complete for scope) CRL contains an
                        // entry for the same certificate with reason code certificateHold.
                        if (deltaCRLReason == CRL_REASON_REMOVE_FROM_CRL)
                        {
                            aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCRLCheckText(), RevocationFromCRLCheckStatus.HOLD_ON_BASE_REMOVED_ON_DELTA.getText(), RevocationFromCRLCheckStatus.HOLD_ON_BASE_REMOVED_ON_DELTA, RevokeCheckStatus.NOT_REVOKED));

                            if (!rc.isCovered())
                                return RevokeCheckStatus.CANT_CHECK;
                            else
                                return RevokeCheckStatus.NOT_REVOKED;
                        }
                    }
                }
                revocationStatusInfo.setRevocationStatus(RevocationStatus.REVOKED);
                revocationStatusInfo.setRevocationCause(crlReason);
                revocationStatusInfo.setRevocationDate(revCertElm.getRevocationDate());
                aCertStatusInfo.setRevocationInfo(revocationStatusInfo);
                aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCRLCheckText(), RevocationFromCRLCheckStatus.CERTIFICATE_IN_LIST.getText(), RevocationFromCRLCheckStatus.CERTIFICATE_IN_LIST, RevokeCheckStatus.REVOKED));
                aCertStatusInfo.setCertificateStatus(CertificateStatus.REVOCATION_CHECK_FAILURE);

                logger.Debug(RevocationFromCRLCheckStatus.CERTIFICATE_IN_LIST.getText());

                return RevokeCheckStatus.REVOKED;   // Certificate found in a valid CRL, no need to continue
            }
            else if (deltaListeNo != -1)
            {
                ERevokedCertificateElement revokedCert = deltaCRL.getRevokedCerticateElement(deltaListeNo);
                int deltaSilNedeni = revokedCert.getCRLReason();
                // RFC 3280 Page 57 says:
                // It is appropriate to list a certificate with reason code
                // removeFromCRL on a delta CRL even if the certificate was not on hold
                // in the referenced base CRL.
                if (deltaSilNedeni == CRL_REASON_REMOVE_FROM_CRL)
                {
                    aCertStatusInfo.addRevocationCheckDetail(
                            new RevocationCheckResult(getCRLCheckText(),
                                    RevocationFromCRLCheckStatus.CERTIFICATE_REMOVED_ON_DELTA_CRL.getText(),
                                    RevocationFromCRLCheckStatus.CERTIFICATE_REMOVED_ON_DELTA_CRL,
                                    RevokeCheckStatus.NOT_REVOKED));
                    if (!rc.isCovered())
                        return RevokeCheckStatus.CANT_CHECK;
                    else
                        return RevokeCheckStatus.NOT_REVOKED;
                }
                else
                {
                    aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCRLCheckText(), RevocationFromCRLCheckStatus.CERTIFICATE_ON_DELTA_CRL.getText(), RevocationFromCRLCheckStatus.CERTIFICATE_ON_DELTA_CRL, RevokeCheckStatus.REVOKED));
                    return RevokeCheckStatus.REVOKED;
                }
            }
            else
            {
                aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCRLCheckText(), RevocationFromCRLCheckStatus.CERTIFICATE_NOT_IN_LIST.getText(), RevocationFromCRLCheckStatus.CERTIFICATE_NOT_IN_LIST, RevokeCheckStatus.NOT_REVOKED));
                if (!rc.isCovered())
                    return RevokeCheckStatus.CANT_CHECK;
                else
                    return RevokeCheckStatus.NOT_REVOKED;
            }
        }

        protected override RevokeCheckStatus _check(ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo)
        {
            if (mParentSystem==null)
                return RevokeCheckStatus.CANT_CHECK;

            RevocationCoverage rc = new RevocationCoverage();

            bool workOffline = mCheckParams.getParameterBoolean(CEVRIM_DISI_CALIS);
            bool checkAllCRLs = mCheckParams.getParameterBoolean(CHECK_ALL_CRLS);

            bool historicalCoverageNeeded	= mCheckParams.getParameterBoolean(PREVIOUSANDNEXT);

            if (!historicalCoverageNeeded)
                rc.mHistoricalCoverage = true;  // If historical coverage is not needed, we pass it


            ECertificate certificate = aCertStatusInfo.getCertificate();
            //String subjectStr = cert.strOzneAl();

            //CRLFinderIteration crlFinder = new CRLFinderIteration();
            CRLFinderIteration crlFinder = new CRLFinderIteration(certificate, mParentSystem);
            crlFinder.addItemSource(new FinderCRLSource<Finder>(certificate, mFinders));

            while (crlFinder.nextIteration(mParentSystem)) {
                ECRL crl = crlFinder.getCurrentItem();

                if (!mParentSystem.getFindSystem().checkCRL(mParentSystem, aCertStatusInfo,crl))
                    continue;

                ECRL deltaCRL = _deltaSilAl(crl, aCertStatusInfo);

                RevocationStatusInfo revocationStatusInfo = new RevocationStatusInfo();
                revocationStatusInfo.setRevocationStatus(RevocationStatus.VALID);
                aCertStatusInfo.setRevocationInfo(revocationStatusInfo);

                RevokeCheckStatus rcs = checkFromCRL(mParentSystem,aCertStatusInfo,crl,deltaCRL,rc);

                if(checkAllCRLs || rcs == RevokeCheckStatus.CANT_CHECK)
                    continue;

                //aCertStatusInfo.

                /*switch (rcs) {
                    case REVOKED:
                        return RevokeCheckStatus.REVOKED;
                    case NOT_REVOKED:
                        return RevokeCheckStatus.NOT_REVOKED;
                }*/
                
                if (rcs == RevokeCheckStatus.REVOKED)
                    return RevokeCheckStatus.REVOKED;
                else if (rcs == RevokeCheckStatus.NOT_REVOKED)
                    return RevokeCheckStatus.NOT_REVOKED;
                
                /*return rcs;*/
            }

            if(rc.isCovered() && checkAllCRLs)
                return RevokeCheckStatus.NOT_REVOKED;

            aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCheckText(),RevocationFromCRLCheckStatus.CRL_NOT_FOUND.getText(),RevocationFromCRLCheckStatus.CRL_NOT_FOUND,RevokeCheckStatus.CANT_CHECK));

            if(!workOffline)
                return RevokeCheckStatus.CANT_CHECK;
            else
                return RevokeCheckStatus.NOT_REVOKED;
        }

        /**
         * Finds Delta-CRL if exists of given Base-CRL
         */
        ECRL _deltaSilAl(ECRL aBaseSil, CertificateStatusInfo aSDB)
        {
            if (mParentSystem == null)
                return null;
            // Find the Delta-CRL
            return mParentSystem.getFindSystem().findDeltaCRL(mParentSystem, aBaseSil, aSDB);
        }


        /**
         * Returns the check process as text
         */
        public static String getCRLCheckText()
        {
            return Resource.message(Resource.SILDEN_IPTAL_KONTROLU);
        }

        private static int _listedeVarMi(ECRL aSil, ECertificate aSertifika, DateTime? aDate)
        {
            if (aSil == null)
            {
                return -1;
            }

            for (int i = 0; i < aSil.getSize(); i++)
            {
                ERevokedCertificateElement silinen = aSil.getRevokedCerticateElement(i);
                if (aSertifika.getSerialNumber().Equals(silinen.getUserCertificate()))
                {
                    if (aDate == null || aDate.Value > silinen.getRevocationDate())
                        return i;
                    return -1;
                }
            }
            return -1;
        }

        public class RevocationCoverage
        {
            public bool mReasonCoverage = false;
            public bool mCertTypeCoverage = false;
            public bool mHistoricalCoverage = false;
            public bool mPreviousCRLCoverage = false;
            public bool mNextCRLCoverage = false;

            public bool []mCoveredReasons;// = new bool[10];

            public RevocationCoverage()
            {
                mCoveredReasons = new bool[10];
                for(int i=0; i<10; ++i)
                    mCoveredReasons[i] = false;
            }

            public bool isCovered()
            {
                return (mReasonCoverage && mCertTypeCoverage && mHistoricalCoverage);
            }
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SILDEN_IPTAL_KONTROLU);
        }
    }
}
