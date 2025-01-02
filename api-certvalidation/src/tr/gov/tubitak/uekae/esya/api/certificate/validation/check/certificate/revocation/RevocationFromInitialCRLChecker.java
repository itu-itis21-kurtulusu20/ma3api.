package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import com.objsys.asn1j.runtime.Asn1InvalidLengthException;

import tr.gov.tubitak.uekae.esya.api.asn.x509.*;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.MatchSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevocationCheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Find;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.IssuerAltName;
import tr.gov.tubitak.uekae.esya.asn.x509.ReasonFlags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Checks certificate's revocation status by using the matching CRLs which are
 * found by the finders specified in the validation policy
 */  
public class RevocationFromInitialCRLChecker extends RevocationFromCRLChecker {

    protected static Logger logger = LoggerFactory.getLogger(RevocationFromInitialCRLChecker.class);

    //public static final String CEVRIM_DISI_CALIS = "cevrimdisicalis";
    //public static final String CHECK_ALL_CRLS = "checkallcrls";

    /** todo refactor
     * Checks certificate revocation status from CRL
     */
    protected RevokeCheckStatus _check(ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        boolean reasonCoverage = false, certTypeCoverage = false;
        boolean[] coveredReasons = new boolean[]{
                false, false, false, false, false, false, false, false, false, false
        };

        boolean workOffline = mCheckParams.getParameterBoolean(CEVRIM_DISI_CALIS);
        boolean checkAllCRLs = mCheckParams.getParameterBoolean(CHECK_ALL_CRLS);


        ECertificate certificate = aCertStatusInfo.getCertificate();
        //String subjectStr = cert.strOzneAl();

        MatchSystem matchSystem = mParentSystem.getMatchSystem();
        
        //CRLFinderIteration crlFinder = new CRLFinderIteration();
        List<ECRL> crls = mParentSystem.getUserInitialCRLSet();

        for (ECRL crl : crls) {
            //ECRL crl = crlFinder.getCRL(mParentSystem);

        	if (!Find.isMatcingCRL(matchSystem, certificate, crl))
                continue;
        	
            if (!mParentSystem.getFindSystem().checkCRL(mParentSystem, aCertStatusInfo, crl))    // Geçerli Mi?
            	continue;
            
               

            RevocationStatusInfo revocationStatusInfo = new RevocationStatusInfo();
            revocationStatusInfo.setRevocationStatus(RevocationStatus.VALID);
            int baseListeNo = -1;
            int deltaListeNo = -1;

            baseListeNo = _listedeVarMi(crl, certificate);

            //_updateCoverage(crl, cert, reasonCoverage, certTypeCoverage, coveredReasons);
            // start update coverage
            if (!(reasonCoverage && certTypeCoverage))
            {
                EIssuingDistributionPoint idp = crl.getCRLExtensions().getIssuingDistributionPoint();

                if (idp == null) {
                    reasonCoverage = true;
                    certTypeCoverage = true;
                }
                else {
                    if (idp.isOnlyContainsAttributeCerts()) {
                        certTypeCoverage = false;
                    }
                    else if ((idp.isOnlyContainsCACerts() && certificate.isCACertificate())
                            || ((idp.isOnlyContainsUserCerts() && !certificate.isCACertificate()))
                            || (!idp.isOnlyContainsCACerts() && !idp.isOnlyContainsUserCerts())) {
                        certTypeCoverage = true;
                    }

                    if (!reasonCoverage)
                    {
                        //      if (!idp.isOnlySomeReasonsPresent()) { todo?
                        if (idp.getOnlySomeReasons() == null) {
                            reasonCoverage = true;
                        }
                        else {
                            ReasonFlags rf = idp.getOnlySomeReasons();
                            //String bits = idp.getOnlySomeReasons().toString();//getOnlySomeReasons().toBitString();

                            try {
                                for (int i = 0; i < rf.getLength(); i++) {
                                    coveredReasons[i] = coveredReasons[i] || (rf.isSet(i));
                                }
                            } catch (Asn1InvalidLengthException x){
                                logger.error("Error in RevocationFromInitialCRLChecker", x);
                            }

                            reasonCoverage = coveredReasons[0];
                            for (int i = 0; i < 9; i++) {
                                reasonCoverage = reasonCoverage && coveredReasons[i];
                            }
                        }
                    }
                }
            }
            // end update coverage

            //_updateCoverage(crl, cert, reasonCoverage, certTypeCoverage, coveredReasons);

            ECRL deltaCRL = _deltaSilAl(crl, aCertStatusInfo);

            if (deltaCRL != null) {
                deltaListeNo = _listedeVarMi(deltaCRL, certificate);
            }

            if (baseListeNo != -1)  // Certificate is in the CRL
            {
                if (crl.isIndirectCRL())   // We need extra check if indirect CRL
                {
                    ECertificateIssuer ci = crl.getCertificateIssuer(baseListeNo);
                    // todo: buraya certIssuer eklentisi ile ilgili kod yazılacak.
                    if (!ci.hasIssuer(certificate.getIssuer())) {
                        // todo SubjectAltName ian
                        IssuerAltName ian = certificate.getExtensions().getIssuerAltName();
                        if (ian == null || !Arrays.equals(ian.elements, ci.getObject().elements)) {
                            deltaCRL = null;

                            if (!reasonCoverage || !certTypeCoverage || checkAllCRLs)
                                continue;
                            else return RevokeCheckStatus.NOT_REVOKED;
                        }
                    }
                }

                ERevokedCertificateElement revCertElm = crl.getRevokedCerticateElement(baseListeNo);
                int crlReason = revCertElm.getCRLReason();
                // Suspended in Base-CRL
                if (crlReason == CRL_REASON_CERTIFICATE_HOLD) {
                    // Check the detail
                    if (deltaListeNo != -1) {
                        int deltaCRLReason = deltaCRL.getRevokedCerticateElement(baseListeNo).getCRLReason();
                        // Removed from Delta-CRL
                        // X.509 Page 36 says:
                        // An entry with this reason code shall be used in
                        // delta-CRLs for which the corresponding base CRL or any subsequent (delta or complete for scope) CRL contains an
                        // entry for the same certificate with reason code certificateHold.
                        if (deltaCRLReason == CRL_REASON_REMOVE_FROM_CRL) {
                            aCertStatusInfo.setRevocationInfo(revocationStatusInfo);
                            aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCheckText(), RevocationFromCRLCheckStatus.HOLD_ON_BASE_REMOVED_ON_DELTA.getText(), RevocationFromCRLCheckStatus.HOLD_ON_BASE_REMOVED_ON_DELTA, RevokeCheckStatus.NOT_REVOKED));
                            deltaCRL = null;

                            if (!reasonCoverage || !certTypeCoverage || checkAllCRLs)
                                continue;
                            else return RevokeCheckStatus.NOT_REVOKED;
                        }
                    }
                }
                revocationStatusInfo.setRevocationStatus(RevocationStatus.REVOKED);
                revocationStatusInfo.setRevocationCause(crlReason);
                revocationStatusInfo.setRevocationDate(revCertElm.getRevocationDate());
                aCertStatusInfo.setRevocationInfo(revocationStatusInfo);
                aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCheckText(), RevocationFromCRLCheckStatus.CERTIFICATE_IN_LIST.getText(), RevocationFromCRLCheckStatus.CERTIFICATE_IN_LIST, RevokeCheckStatus.REVOKED));
                deltaCRL = null;

                return RevokeCheckStatus.REVOKED;   // Certificate found in a valid CRL, no need to continue
            }
            else if (deltaListeNo != -1) {
                if (deltaCRL.isIndirectCRL())   // We need extra check if indirect CRL
                {
                    ECertificateIssuer ci = deltaCRL.getCertificateIssuer(deltaListeNo);
                    // todo: buraya certIssuer eklentisi ile ilgili kod yazılacak.
                    if (!ci.hasIssuer(certificate.getIssuer())) {
                        ESubjectAltName ian = certificate.getExtensions().getSubjectAltName();
                        if (ian != null || !ian.equals(ci)) {
                            deltaCRL = null;

                            if (!reasonCoverage || !certTypeCoverage || checkAllCRLs)
                                continue;
                            else return RevokeCheckStatus.NOT_REVOKED;
                        }
                    }
                }

                ERevokedCertificateElement revokedCert = deltaCRL.getRevokedCerticateElement(deltaListeNo);
                int deltaSilNedeni = revokedCert.getCRLReason();
                // RFC 3280 Page 57 says:
                // It is appropriate to list a certificate with reason code
                // removeFromCRL on a delta CRL even if the certificate was not on hold
                // in the referenced base CRL.
                if (deltaSilNedeni == CRL_REASON_REMOVE_FROM_CRL) {
                    aCertStatusInfo.setRevocationInfo(revocationStatusInfo);
                    aCertStatusInfo.addRevocationCheckDetail(
                            new RevocationCheckResult(getCheckText(),
                                                      RevocationFromCRLCheckStatus.CERTIFICATE_REMOVED_ON_DELTA_CRL.getText(),
                                                      RevocationFromCRLCheckStatus.CERTIFICATE_REMOVED_ON_DELTA_CRL,
                                                      RevokeCheckStatus.NOT_REVOKED));
                    deltaCRL = null;

                    if (!reasonCoverage || !certTypeCoverage || checkAllCRLs)
                        continue;
                    else return RevokeCheckStatus.NOT_REVOKED;
                }
                else {
                    revocationStatusInfo.setRevocationStatus(RevocationStatus.REVOKED);
                    revocationStatusInfo.setRevocationCause(deltaSilNedeni);
                    revocationStatusInfo.setRevocationDate(revokedCert.getRevocationDate());
                    aCertStatusInfo.setRevocationInfo(revocationStatusInfo);
                    aCertStatusInfo.addRevocationCheckDetail(new RevocationCheckResult(getCheckText(), RevocationFromCRLCheckStatus.CERTIFICATE_ON_DELTA_CRL.getText(), RevocationFromCRLCheckStatus.CERTIFICATE_ON_DELTA_CRL, RevokeCheckStatus.REVOKED));
                    deltaCRL = null;

                    return RevokeCheckStatus.REVOKED;
                }
            }
            else {
                aCertStatusInfo.setRevocationInfo(revocationStatusInfo);
                aCertStatusInfo.addRevocationCheckDetail(this, RevocationFromCRLCheckStatus.CERTIFICATE_NOT_IN_LIST, RevokeCheckStatus.NOT_REVOKED);
                deltaCRL = null;

                if (!reasonCoverage || !certTypeCoverage || checkAllCRLs)
                    continue;
                else
                    return RevokeCheckStatus.NOT_REVOKED;
            }
            //DELETE_MEMORY(deltaCRL)
        }

        aCertStatusInfo.addRevocationCheckDetail(this, RevocationFromCRLCheckStatus.CRL_NOT_FOUND, RevokeCheckStatus.CANT_CHECK);
        if (!workOffline)
            return RevokeCheckStatus.CANT_CHECK;
        else return RevokeCheckStatus.NOT_REVOKED;
    }


    /**
     * Finds Delta-CRL if exists of given Base-CRL
     */
    ECRL _deltaSilAl(ECRL aBaseSil, CertificateStatusInfo aSDB)
            throws ESYAException
    {
        if (mParentSystem == null)
            return null;
        //delta silimizi bulalım
        return mParentSystem.getFindSystem().findDeltaCRL(mParentSystem, aBaseSil, aSDB);
    }


    /**
     * Returns check process as text
     */
    public String getCheckText() {
        return CertI18n.message(CertI18n.SILDEN_IPTAL_KONTROLU);
    }

    private int _listedeVarMi(ECRL aSil, ECertificate aSertifika) {
        if (aSil == null) {
            return -1;
        }

        for (int i = 0; i < aSil.getSize(); i++) {
            ERevokedCertificateElement silinen = aSil.getRevokedCerticateElement(i);
            if (aSertifika.getSerialNumber().equals(silinen.getUserCertificate())) {
                return i;
            }
        }
        return -1;
    }

}
