package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import com.objsys.asn1j.runtime.Asn1InvalidLengthException;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EIssuingDistributionPoint;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ERevokedCertificateElement;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevocationCheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.CRLFinderIteration;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.FinderCRLSource;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.x509.ReasonFlags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Checks certificate's revocation status by using the matching CRLs which are
 * found by the finders specified in the validation policy
 */
public class RevocationFromCRLChecker extends RevocationChecker {

    protected static Logger logger = LoggerFactory.getLogger(RevocationFromCRLChecker.class);

    public static final String CEVRIM_DISI_CALIS = "cevrimdisicalis";
    public static final String CHECK_ALL_CRLS = "checkallcrls";
    public static final String PREVIOUSANDNEXT = "checkpreviousandnext";

    public enum RevocationFromCRLCheckStatus implements CheckStatus {

        INVALID_CRL,
        CERTIFICATE_IN_LIST,
        CERTIFICATE_NOT_IN_LIST,
        CRL_NOT_FOUND,
        HOLD_ON_BASE_REMOVED_ON_DELTA,
        CERTIFICATE_ON_DELTA_CRL,
        CERTIFICATE_REMOVED_ON_DELTA_CRL;

        public String getText() {
            switch (this) {
                case INVALID_CRL:
                    return CertI18n.message(CertI18n.SIL_GECERSIZ);
                case CERTIFICATE_IN_LIST:
                    return CertI18n.message(CertI18n.SERTIFIKA_LISTEDE);
                case CERTIFICATE_NOT_IN_LIST:
                    return CertI18n.message(CertI18n.SERTIFIKA_LISTEDE_DEGIL);
                case CRL_NOT_FOUND:
                    return CertI18n.message(CertI18n.SIL_BULUNAMADI);
                case HOLD_ON_BASE_REMOVED_ON_DELTA:
                    return CertI18n.message(CertI18n.BASE_SILDE_ASKIYA_ALINAN_DELTADA_REMOVE_EDILMIS);
                case CERTIFICATE_ON_DELTA_CRL:
                    return CertI18n.message(CertI18n.SERTIFIKA_DELTA_SILDE);
                case CERTIFICATE_REMOVED_ON_DELTA_CRL:
                    return CertI18n.message(CertI18n.SERTIFIKA_DELTA_SILDE_REMOVE_EDILMIS);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    public static final int CRL_REASON_UNSPECIFIED = 0;
    public static final int CRL_REASON_KEY_COMPROMISE = 1;
    public static final int CRL_REASON_CA_COMPROMISE = 2;
    public static final int CRL_REASON_AFFILIATION_CHANGED = 3;
    public static final int CRL_REASON_SUPERSEDED = 4;
    public static final int CRL_REASON_CESSATION_OF_OPERATION = 5;
    public static final int CRL_REASON_CERTIFICATE_HOLD = 6;
    public static final int CRL_REASON_REMOVE_FROM_CRL = 8;

    public static RevokeCheckStatus checkFromCRL(ValidationSystem aParentSystem, CertificateStatusInfo aCertStatusInfo,
                                                 ECRL crl, ECRL deltaCRL, RevocationCoverage rc) {

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

            if(!rc.mHistoricalCoverage) {
                //Calendar due = crl.getNextUpdate();
                Calendar from = crl.getThisUpdate();

                if (from.compareTo(aParentSystem.getBaseValidationTime())<0)
                    rc.mPreviousCRLCoverage = true;
                else
                    rc.mNextCRLCoverage = true;

                if (rc.mNextCRLCoverage && rc.mPreviousCRLCoverage)
                    rc.mHistoricalCoverage= true;
            }

            EIssuingDistributionPoint idp = crl.getCRLExtensions().getIssuingDistributionPoint();

            if (idp == null) {
                rc.mReasonCoverage = true;
                rc.mCertTypeCoverage = true;
            }
            else {
                if (idp.isOnlyContainsAttributeCerts()) {
                    rc.mCertTypeCoverage = false;
                }
                else if ((idp.isOnlyContainsCACerts() && certificate.isCACertificate())
                        || ((idp.isOnlyContainsUserCerts() && !certificate.isCACertificate()))
                        || (!idp.isOnlyContainsCACerts() && !idp.isOnlyContainsUserCerts())) {
                    rc.mCertTypeCoverage = true;
                }

                if (!rc.mReasonCoverage)
                {
                    //      if (!idp.isOnlySomeReasonsPresent()) { todo?
                    if (idp.getOnlySomeReasons() == null) {
                        rc.mReasonCoverage = true;
                    }
                    else {
                        ReasonFlags rf = idp.getOnlySomeReasons();
                        //String bits = idp.getOnlySomeReasons().toString();//getOnlySomeReasons().toBitString();

                        try {
                            for (int i = 0; i < rf.getLength(); i++) {
                                rc.mCoveredReasons[i] = rc.mCoveredReasons[i] || (rf.isSet(i));
                            }
                        } catch (Asn1InvalidLengthException x){
                            logger.error("Error in RevocationFromCRLChecker", x);
                        }

                        rc.mReasonCoverage = rc.mCoveredReasons[0];
                        for (int i = 0; i < 9; i++) {
                            rc.mReasonCoverage = rc.mReasonCoverage && rc.mCoveredReasons[i];
                        }
                    }
                }
            }
        }
        // end update coverage

        //_updateCoverage(crl, cert, reasonCoverage, certTypeCoverage, coveredReasons);

        if (deltaCRL != null) {
            deltaListeNo = _listedeVarMi(deltaCRL, certificate, aParentSystem.getBaseValidationTime());
        }

        if (baseListeNo != -1) {    // Certificate exists in the CRL
            ERevokedCertificateElement revCertElm = crl.getRevokedCerticateElement(baseListeNo);
            int crlReason = revCertElm.getCRLReason();
            // Suspended in Base-CRL
            if (crlReason == CRL_REASON_CERTIFICATE_HOLD) {
                // Check the detail
                if (deltaListeNo != -1) {
                    // todo baseListeNo mu deltaListeNo mu?
                    int deltaCRLReason = deltaCRL.getRevokedCerticateElement(deltaListeNo).getCRLReason();
                    // Removed from Delta-CRL
                    // X.509 Page 36 says:
                    // An entry with this reason code shall be used in
                    // delta-CRLs for which the corresponding base CRL or any subsequent (delta or complete for scope) CRL contains an
                    // entry for the same certificate with reason code certificateHold.
                    if (deltaCRLReason == CRL_REASON_REMOVE_FROM_CRL) {
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

            logger.debug(RevocationFromCRLCheckStatus.CERTIFICATE_IN_LIST.getText());

            return RevokeCheckStatus.REVOKED;   // Certificate found in a valid CRL, no need to continue
        }
        else if(deltaListeNo != -1) {
            ERevokedCertificateElement revokedCert = deltaCRL.getRevokedCerticateElement(deltaListeNo);
            int deltaSilNedeni = revokedCert.getCRLReason();
            // RFC 3280 Page 57 says:
            // It is appropriate to list a certificate with reason code
            // removeFromCRL on a delta CRL even if the certificate was not on hold
            // in the referenced base CRL.
            if (deltaSilNedeni == CRL_REASON_REMOVE_FROM_CRL) {
                aCertStatusInfo.addRevocationCheckDetail(
                        new RevocationCheckResult(getCRLCheckText(),
                                RevocationFromCRLCheckStatus.CERTIFICATE_REMOVED_ON_DELTA_CRL.getText(),
                                RevocationFromCRLCheckStatus.CERTIFICATE_REMOVED_ON_DELTA_CRL,
                                RevokeCheckStatus.NOT_REVOKED));
                if(!rc.isCovered())
                    return RevokeCheckStatus.CANT_CHECK;
                else
                    return RevokeCheckStatus.NOT_REVOKED;
            }
            else {
                aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCRLCheckText(), RevocationFromCRLCheckStatus.CERTIFICATE_ON_DELTA_CRL.getText(), RevocationFromCRLCheckStatus.CERTIFICATE_ON_DELTA_CRL, RevokeCheckStatus.REVOKED));
                return RevokeCheckStatus.REVOKED;
            }
        }
        else {
            aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCRLCheckText(),RevocationFromCRLCheckStatus.CERTIFICATE_NOT_IN_LIST.getText(), RevocationFromCRLCheckStatus.CERTIFICATE_NOT_IN_LIST, RevokeCheckStatus.NOT_REVOKED));
            if(!rc.isCovered())
                return RevokeCheckStatus.CANT_CHECK;
            else
                return RevokeCheckStatus.NOT_REVOKED;
        }
    }

    protected RevokeCheckStatus _check(ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        if (mParentSystem==null)
            return RevokeCheckStatus.CANT_CHECK;

        RevocationCoverage rc = new RevocationCoverage();

        boolean workOffline = mCheckParams.getParameterBoolean(CEVRIM_DISI_CALIS);
        boolean checkAllCRLs = mCheckParams.getParameterBoolean(CHECK_ALL_CRLS);

        boolean historicalCoverageNeeded	= mCheckParams.getParameterBoolean(PREVIOUSANDNEXT);

        if (!historicalCoverageNeeded)
            rc.mHistoricalCoverage = true;  // If historical coverage is not needed, we pass it


        ECertificate certificate = aCertStatusInfo.getCertificate();
        //String subjectStr = cert.strOzneAl();

        //CRLFinderIteration crlFinder = new CRLFinderIteration();
        CRLFinderIteration crlFinder = new CRLFinderIteration(certificate, mParentSystem);
        crlFinder.addItemSource(new FinderCRLSource(certificate, mFinders));

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

            switch (rcs) {
                case REVOKED:
                    return RevokeCheckStatus.REVOKED;
                case NOT_REVOKED:
                    return RevokeCheckStatus.NOT_REVOKED;
            }
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
            throws ESYAException
    {
        if (mParentSystem == null)
            return null;
        // Find the Delta-CRL
        return mParentSystem.getFindSystem().findDeltaCRL(mParentSystem, aBaseSil, aSDB);
    }

    public static String getCRLCheckText() {
        return CertI18n.message(CertI18n.SILDEN_IPTAL_KONTROLU);
    }

    private static int _listedeVarMi(ECRL aSil, ECertificate aSertifika, Calendar aDate) {
        if (aSil == null) {
            return -1;
        }

        for (int i = 0; i < aSil.getSize(); i++) {
            ERevokedCertificateElement silinen = aSil.getRevokedCerticateElement(i);
            if (aSertifika.getSerialNumber().equals(silinen.getUserCertificate())) {
                if (aDate==null || aDate.getTime().after(silinen.getRevocationDate()))
                    return i;
                return -1;
            }
        }
        return -1;
    }

    public static class RevocationCoverage {
        public boolean mReasonCoverage = false;
        public boolean mCertTypeCoverage = false;
        public boolean mHistoricalCoverage = false;
        public boolean mPreviousCRLCoverage = false;
        public boolean mNextCRLCoverage = false;

        public boolean mCoveredReasons[] = new boolean[10];

        public RevocationCoverage() {
            for(int i=0; i<10; ++i)
                mCoveredReasons[i] = false;
        }

        boolean isCovered() {
            return (mReasonCoverage && mCertTypeCoverage && mHistoricalCoverage);
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SILDEN_IPTAL_KONTROLU);
    }
}
