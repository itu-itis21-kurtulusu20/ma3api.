using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * Checks the certificate revocation according to the map which matches
     * revocation references and values
     * @authot suleyman.uslu
     */
    public class RevocationCheckerFromMap : RevocationChecker
    {
        protected RevocationInfoMap mRevocationInfoMap;

        public RevocationCheckerFromMap(RevocationInfoMap aRevocationInfoMap) {
            this.mRevocationInfoMap = aRevocationInfoMap;
            setCheckParameters(new ParameterList());
            mCheckParams.addParameter(DEVAM_EDILSIN, true);
        }

        protected RevokeCheckStatus _checkFromCRL(ECertificate aIssuerCert, CertificateStatusInfo iCSI, RevocationInfoMap.RevocationInfoItems iRI)
        {

            if(mParentSystem == null)
                return RevokeCheckStatus.CANT_CHECK;

            MatchSystem es = mParentSystem.getMatchSystem();

            RevocationFromCRLChecker.RevocationCoverage rc = new RevocationFromCRLChecker.RevocationCoverage();

            ECertificate cert = iCSI.getCertificate();

            List<byte[]> checkedCRLs = new List<byte[]>();

            for(int i=0; i<iRI.getCRLList().Count; ++i) {
                ECRL crl = iRI.getCRLList().ElementAt(i);
                byte[] crlSignature = crl.getSignature();

                ECRL pDeltaCRL = _deltaSilAl(crl,iRI.getCRLList(),iCSI);

                if(checkedCRLs.Contains(crlSignature))
                    continue;

                checkedCRLs.Add(crlSignature);

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

        protected RevokeCheckStatus _checkFromOCSP(ECertificate aIssuerCert, CertificateStatusInfo iCSI, RevocationInfoMap.RevocationInfoItems iRI)
        {

            if(mParentSystem == null)
                return RevokeCheckStatus.CANT_CHECK;

            MatchSystem es = mParentSystem.getMatchSystem();
            ECertificate cert = iCSI.getCertificate();

            for(int i=0; i<iRI.getOCSPResponseList().Count; ++i) {
                EOCSPResponse ocspResponse = iRI.getOCSPResponseList().ElementAt(i);

                if(!Find.isMatchingOCSPResponse(es,cert,aIssuerCert,ocspResponse))
                    continue;

                RevokeCheckStatus rcs = RevocationFromOCSPChecker.checkFromOCSP(mParentSystem,iCSI,ocspResponse);

                if(rcs != RevokeCheckStatus.CANT_CHECK)
                    return rcs;
            }
            return RevokeCheckStatus.CANT_CHECK;
        }

        protected override RevokeCheckStatus _check(ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo)
        {

            RevokeCheckStatus status = null;
            ECertificate cert = aCertStatusInfo.getCertificate();

            if(mParentSystem == null)
                return RevokeCheckStatus.CANT_CHECK;

            if(!mRevocationInfoMap.contains(cert))
                return RevokeCheckStatus.CANT_CHECK;

            RevocationInfoMap.RevocationInfoItems ri = mRevocationInfoMap.getMap()[cert];

            if( ri.getCRLList().Count != 0 )
                status = _checkFromCRL(aIssuerCertificate,aCertStatusInfo,ri);

            if(status != RevokeCheckStatus.NOT_REVOKED  &&  ri.getOCSPResponseList().Count != 0 )
                status = _checkFromOCSP(aIssuerCertificate,aCertStatusInfo,ri);

            // todo bu return'u ben yazdim yanlis olabilir
            return status;
        }

        protected ECRL _deltaSilAl(ECRL aBaseSil, List<ECRL> aCRLList, CertificateStatusInfo aSDB)
        {

            if(mParentSystem == null)
                return null;

            MatchSystem es = mParentSystem.getMatchSystem();

            for(int i=0; i<aCRLList.Count; ++i) {

                if(!Find.isMatchingDeltaCRL(es,aBaseSil,aCRLList.ElementAt(i)))
                    continue;

                DeltaCRLController dsk = new DeltaCRLController();
                CRLStatusInfo pSDB = dsk.check(mParentSystem,aCRLList.ElementAt(i));
                aSDB.addDeltaCRLInfo(pSDB);

                if(pSDB.getCRLStatus() == CRLStatus.VALID)
                    return aCRLList.ElementAt(i);

            }
            // todo bu return'u ben yazdim, yanlis olabilir
            return null;
        }
    }
}
