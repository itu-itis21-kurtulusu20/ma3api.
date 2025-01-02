package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.MatchSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl.DeltaCRLController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Find;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks the certificate revocation according to the map which matches
 * revocation references and values
 * @author suleyman.uslu
 */
public class RevocationCheckerFromMap extends RevocationChecker {

    protected RevocationInfoMap mRevocationInfoMap;

    public RevocationCheckerFromMap(RevocationInfoMap aRevocationInfoMap) {
        this.mRevocationInfoMap = aRevocationInfoMap;
        setCheckParameters(new ParameterList());
        mCheckParams.addParameter(DEVAM_EDILSIN, Boolean.TRUE);
    }

    protected RevokeCheckStatus _checkFromCRL(ECertificate aIssuerCert, CertificateStatusInfo iCSI, RevocationInfoMap.RevocationInfoItems iRI) throws ESYAException {

        if(mParentSystem == null)
            return RevokeCheckStatus.CANT_CHECK;

        MatchSystem es = mParentSystem.getMatchSystem();

        RevocationFromCRLChecker.RevocationCoverage rc = new RevocationFromCRLChecker.RevocationCoverage();

        ECertificate cert = iCSI.getCertificate();

        List<byte[]> checkedCRLs = new ArrayList<byte[]>();

        for(int i=0; i<iRI.getCRLList().size(); ++i) {
            ECRL crl = iRI.getCRLList().get(i);
            byte[] crlSignature = crl.getSignature();

            ECRL pDeltaCRL = _deltaSilAl(crl,iRI.getCRLList(),iCSI);

            if(checkedCRLs.contains(crlSignature))
                continue;

            checkedCRLs.add(crlSignature);

            if(!Find.isMatcingCRL(es,cert,crl))
                continue;

            if(!mParentSystem.getFindSystem().checkCRL(mParentSystem,iCSI,crl))
                continue;

            RevokeCheckStatus rcs = RevocationFromCRLChecker.checkFromCRL(mParentSystem,iCSI,crl,pDeltaCRL,rc);
            if(rcs != RevokeCheckStatus.CANT_CHECK)
                return rcs;
        }
        return RevokeCheckStatus.CANT_CHECK;
    }

    protected RevokeCheckStatus _checkFromOCSP(ECertificate aIssuerCert, CertificateStatusInfo iCSI, RevocationInfoMap.RevocationInfoItems iRI) throws ESYAException {

        if(mParentSystem == null)
            return RevokeCheckStatus.CANT_CHECK;

        MatchSystem es = mParentSystem.getMatchSystem();
        ECertificate cert = iCSI.getCertificate();

        for(int i=0; i<iRI.getOCSPResponseList().size(); ++i) {
            EOCSPResponse ocspResponse = iRI.getOCSPResponseList().get(i);

            if(!Find.isMatchingOCSPResponse(es,cert,aIssuerCert,ocspResponse))
                continue;

            RevokeCheckStatus rcs = RevocationFromOCSPChecker.checkFromOCSP(mParentSystem,iCSI,ocspResponse);

            if(rcs != RevokeCheckStatus.CANT_CHECK)
                return rcs;
        }
        return RevokeCheckStatus.CANT_CHECK;
    }

    protected RevokeCheckStatus _check(ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo) throws ESYAException {

        RevokeCheckStatus status = null;
        ECertificate cert = aCertStatusInfo.getCertificate();

        if(mParentSystem == null)
            return RevokeCheckStatus.CANT_CHECK;

        if(!mRevocationInfoMap.contains(cert))
            return RevokeCheckStatus.CANT_CHECK;

        RevocationInfoMap.RevocationInfoItems ri = mRevocationInfoMap.getMap().get(cert);

        if(!ri.getCRLList().isEmpty())
            status = _checkFromCRL(aIssuerCertificate,aCertStatusInfo,ri);

        if(status != RevokeCheckStatus.NOT_REVOKED && !ri.getOCSPResponseList().isEmpty())
            status = _checkFromOCSP(aIssuerCertificate,aCertStatusInfo,ri);

        // todo bu return'u ben yazdim yanlis olabilir
        return status;
    }

    protected ECRL _deltaSilAl(ECRL aBaseSil, List<ECRL> aCRLList, CertificateStatusInfo aSDB) throws ESYAException {

        if(mParentSystem == null)
            return null;

        MatchSystem es = mParentSystem.getMatchSystem();

        for(int i=0; i<aCRLList.size(); ++i) {

            if(!Find.isMatchingDeltaCRL(es,aBaseSil,aCRLList.get(i)))
                continue;

            DeltaCRLController dsk = new DeltaCRLController();
            CRLStatusInfo pSDB = dsk.check(mParentSystem,aCRLList.get(i));
            aSDB.addDeltaCRLInfo(pSDB);

            if(pSDB.getCRLStatus() == CRLStatus.VALID)
                return aCRLList.get(i);

        }
        // todo bu return'u ben yazdim, yanlis olabilir
        return null;
    }
}
