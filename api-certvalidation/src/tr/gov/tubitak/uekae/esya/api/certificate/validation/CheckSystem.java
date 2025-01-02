package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateSelfChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer.CRLIssuerChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self.CRLSelfChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl.DeltaCRLChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for the Checker objects specified by ValidationPolicy
 */
public class CheckSystem implements Cloneable
{
    private static final Logger logger = LoggerFactory.getLogger(CheckSystem.class);

    private IssuerCheckParameters mConstraintCheckParam = new IssuerCheckParameters();

    private List<CertificateSelfChecker> mTrustedCertificateCheckers = new ArrayList<CertificateSelfChecker>();
    private List<CertificateSelfChecker> mCertificateSelfCheckers = new ArrayList<CertificateSelfChecker>();
    private List<IssuerChecker> mIssuerCheckers = new ArrayList<IssuerChecker>();
    private List<RevocationChecker> mRevocationCheckers = new ArrayList<RevocationChecker>();
    private List<CRLSelfChecker> mCRLSelfCheckers = new ArrayList<CRLSelfChecker>();
    private List<CRLIssuerChecker> mCRLIssuerCheckers = new ArrayList<CRLIssuerChecker>();
    private List<OCSPResponseChecker> mOCSPResponseCheckers = new ArrayList<OCSPResponseChecker>();
    private List<DeltaCRLChecker> mDeltaCRLCheckers = new ArrayList<DeltaCRLChecker>();

    private boolean mAllChecksRequired;

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
        mTrustedCertificateCheckers.add(aGSKontrolcu);
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
        mCertificateSelfCheckers.add(aTSKontrolcu);
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
        mIssuerCheckers.add(aUstSMKontrolcu);
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
        mRevocationCheckers.add(aIptalKontrolcu);
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
        mCRLSelfCheckers.add(aCRLSelfChecker);
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
        mCRLIssuerCheckers.add(aSilSMKontrolcu);
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
        mOCSPResponseCheckers.add(aOCSPCevabiKontrolcu);
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
        mDeltaCRLCheckers.add(aDeltaSilKontrolcu);
    }


    /**
     * mTumKontrolleriYap alanını döner
     */
    public boolean isAllChecksRequired()
    {
        return mAllChecksRequired;
    }

    /**
     * mTumKontrolleriYap alanını belirler
     */
    public void setAllChecksRequired(boolean aTKY)
    {
        mAllChecksRequired = aTKY;
    }


    public PathValidationResult guvenilirSertifikaKontrolleriYap(CertificateStatusInfo aCertStatusInfo)
    {
        PathValidationResult b, result = PathValidationResult.SUCCESS;
        for (CertificateSelfChecker gsk : mTrustedCertificateCheckers) {
            b = gsk.check(aCertStatusInfo);
            if (b != PathValidationResult.SUCCESS && gsk.isCritical()) {
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
        for (CertificateSelfChecker gsk : mCertificateSelfCheckers) {
            b = gsk.check(aCertStatusInfo);
            if (b != PathValidationResult.SUCCESS && gsk.isCritical()) {
            	logger.error(gsk.getClass().getName() + " failed");
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
        for (IssuerChecker issuerChecker : mIssuerCheckers) {
            b = issuerChecker.check(aIssuerCheckParams, aIssuerCertificate, aCertificate, aCertStatusInfo);
            if (b != PathValidationResult.SUCCESS && issuerChecker.isCritical()) {
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
        for (CRLSelfChecker tsk : mCRLSelfCheckers) {
            b = tsk.check(aCRL, aCRLStatusInfo);
            if (b != PathValidationResult.SUCCESS && tsk.isCritical()) {
            	logger.error(tsk.getClass().getName() + " failed");
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
        for (CRLIssuerChecker ssk : mCRLIssuerCheckers) {
            b = ssk.check(aIssuerCheckParams, aCRL, aIssuerCertificate, aCRLStatusInfo);
            if (b != PathValidationResult.SUCCESS && ssk.isCritical()) {
                if (mAllChecksRequired)
                    result = b;
                else return b;
            }
        }
        return result;
    }

    public PathValidationResult checkRevocation(ECertificate aIssuerCertificate,
                                                CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        RevokeCheckStatus result = RevokeCheckStatus.NOT_REVOKED;
        boolean hasControlledOnce = false;
        ECertificate certificate = aCertStatusInfo.getCertificate();

        if (logger.isDebugEnabled()){
            logger.debug("-- Check revocation for: \n"+certificate.getSubject().stringValue());
        }
        
        
        if (certificate.isOCSPSigningCertificate())
            if (certificate.hasOCSPNoCheckExtention())
            {
            	logger.debug("Certificate has oid_pkix_ocsp_nocheck extension.");
                return PathValidationResult.SUCCESS;
            }

        for (RevocationChecker isk : mRevocationCheckers) {
            logger.debug("Checker: "+isk);
            result = isk.check(aIssuerCertificate, aCertStatusInfo);

            switch (result) {
                case CANT_CHECK: {
                    logger.debug("Cant check!");
                    break;
                }
                case REVOKED: {
                    logger.error("Sertifika iptal kontrol hatası.");
                    return PathValidationResult.CERTIFICATE_REVOKED;
                }
                case NOT_REVOKED: {
                    logger.debug("Not revoked!");
                    hasControlledOnce = true;
                    if (!isk.isContinue()) {
                        return PathValidationResult.SUCCESS;
                    }
                }
            }
        }
        if ((result == RevokeCheckStatus.NOT_REVOKED) || hasControlledOnce ) {
            if (logger.isDebugEnabled()){
                logger.debug("Not revoked : "+certificate.getSubject().stringValue());
            }
            return PathValidationResult.SUCCESS;
        }
        else {
            logger.error("Certificate revocation info cannot be checked for  :"+certificate.getSubject().stringValue());
            return PathValidationResult.REVOCATION_CONTROL_FAILURE;// KONTROL EDILEMEDI
        }
    }


    public PathValidationResult checkOCSPResponse(OCSPResponseStatusInfo aOCSPResponseStatusInfo,
                                                  EOCSPResponse aOCSPResponse)
            throws ESYAException
    {
        PathValidationResult b, result = PathValidationResult.SUCCESS;
        for (OCSPResponseChecker osk : mOCSPResponseCheckers) {
            b = osk.check(aOCSPResponseStatusInfo, aOCSPResponse);
            if (b != PathValidationResult.SUCCESS && osk.isCritical()) {
            	logger.error(osk.getClass().getName() + " failed");
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
        for (CRLSelfChecker dsk : mDeltaCRLCheckers) {
            b = dsk.check(aCRL, aCRLStatusInfo);
            if (b != PathValidationResult.SUCCESS && dsk.isCritical()) {
                if (mAllChecksRequired)
                    result = b;
                else return b;
            }
        }
        return result;
    }

    //@Override
    protected Object clone() throws CloneNotSupportedException {

        CheckSystem cs = (CheckSystem) super.clone();
        List<RevocationChecker> rcCloneList = new ArrayList<RevocationChecker>();
        for (RevocationChecker rc : mRevocationCheckers){
            rcCloneList.add((RevocationChecker)rc.clone());
        }

        cs.setRevocationCheckers(rcCloneList);
        return cs;
    }
}
