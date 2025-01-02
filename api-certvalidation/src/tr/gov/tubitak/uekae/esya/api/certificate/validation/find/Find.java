package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.MatchSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.SaveSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.CertificateSaver;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.List;

/**
 * Utility class for several finding operations 
 */
public class Find
{
    /**
     * Verilen sertifikanı Ust SM sertifikasını verilen bir listeden bulur.
     */
    public static ECertificate fromList(MatchSystem aMatchSystem,
                                        List<ECertificate> aCertList,
                                        ECertificate aCertificate)
    {
        if (aCertList!=null){
            for (ECertificate certificate : aCertList) {
                if (isMatchingIssuer(aMatchSystem, aCertificate, certificate))
                    return certificate;
            }
        }
        return null;
    }

    /**
     * Finds the issuer certificate of the given certificate from a list. by
     * using the given MatchSystem.
     */
    public static List<ECertificate> issuerCertificatesFromList(MatchSystem aMatchSystem,
                                                                List<ECertificate> aCertList,
                                                                ECertificate aCertificate,
                                                                List<ECertificate> aMatches)
    {
        if (aCertList!=null){
            for (ECertificate certificate : aCertList) {
                if (certificate.equals(aCertificate))
                    continue;// bunu sonradan ekledik. loopları engellesin diye
                if (isMatchingIssuer(aMatchSystem, aCertificate, certificate) && !aMatches.contains(certificate))
                    aMatches.add(certificate);
            }
        }
        return aMatches;
    }

    /**
     * Finds the issuer certificate of the given CRL from a list. by using the
     * given MatchSystem.
     */
    public static ECertificate issuerCertificateFromList(MatchSystem aMatchSystem,
                                                         List<ECertificate> aCertList,
                                                         ECRL aCRL)
    {
        if (aCertList!=null){
            for (ECertificate certificate : aCertList) {
                if (isMatchingIssuer(aMatchSystem, aCRL, certificate))
                    return certificate;
            }
        }
        return null;
    }

    /**
     * Finds the issuer certificate(s) of the given certificate from a list.
     */
    public static List<ECertificate> issuerCertificatesFromList(MatchSystem aMatchSystem,
                                                                List<ECertificate> aCertList,
                                                                ECRL aCRL,
                                                                List<ECertificate> aMatches)
    {
        if (aCertList!=null){
            for (ECertificate certificate : aCertList) {
                if (isMatchingIssuer(aMatchSystem, aCRL, certificate) && !aMatches.contains(certificate))
                    aMatches.add(certificate);
            }
        }
        return aMatches;
    }

    /**
     * 	Finds the delta-CRL()s of the given certificate from a list.
     */
    public static List<ECRL> crlsFromList(MatchSystem aMatchSystem,
                                          List<ECRL> aCRLList,
                                          List<ECRL> aMatches,
                                          ECertificate aCertificate)
    {
        for (ECRL crl : aCRLList) {
            if (!aMatches.contains(crl) && isMatcingCRL(aMatchSystem, aCertificate, crl))
                aMatches.add(crl);
        }
        return aMatches;
    }

    /**
     * Finds the delta-CRL()s of the given certificate from a list. 
     */
    public static ECRL deltaCRLFromList(MatchSystem aMatchSystem,
                                        List<ECRL> aCRLS,
                                        ECertificate aCertificate)
    {
        if (aCRLS.size() > 0)
            return aCRLS.get(0);
        return null;
    }

    /**
     * Finds the delta-CRL()s of the given base CRL from a list.
     */
    public static ECRL deltaCRLFromList(MatchSystem aMatchSystem,
                                        List<ECRL> aCRLList,
                                        ECRL aBaseCRL)
    {
        if (aCRLList!=null ){
            for (ECRL crl : aCRLList) {
                if (isMatchingDeltaCRL(aMatchSystem, aBaseCRL, crl))
                    return crl;
            }
        }
        return null;
    }

    /**
     * Finds the cross certificate of the given certificate from a list.
     */
    public static ECertificate crossCertificateFromList(MatchSystem aMatchSystem,
                                                        List<ECertificate> aCertificates,
                                                        ECertificate aCertificate)
    {
        if (aCertificates!=null){
            for (ECertificate certificate : aCertificates) {
                if (isMatchingCrossCertificate(aMatchSystem, aCertificate, certificate))
                    return certificate;
            }
        }
        return null;
    }

    /**
     * Checks whether the given CRL and Issuer Certificate are matching
     */
    public static boolean isMatchingIssuer(MatchSystem aMatchSystem,
                                           ECRL aCRL,
                                           ECertificate aIssuerCertificate)
    {
        return aMatchSystem.matchCertificate(aCRL, aIssuerCertificate);
    }

    /**
     * Checks whether the given Certificate and Issuer Certificate are matching
     */
    public static boolean isMatchingIssuer(MatchSystem aMatchSystem,
                                           ECertificate aCertificate,
                                           ECertificate aIssuerCertificate)
    {
        return aMatchSystem.matchCertificate(aCertificate, aIssuerCertificate);
    }

    /**
     * Checks whether the given Certificate and CRL are matching
     */
    public static boolean isMatcingCRL(MatchSystem aMatchSystem,
                                       ECertificate aCertificate,
                                       ECRL aCRL)
    {
        return aMatchSystem.matchCRL(aCertificate, aCRL);
    }

    public static boolean isMatchingOCSPResponse	(MatchSystem aEslestirmeSistemi, ECertificate aCertificate, ECertificate aIssuer , EOCSPResponse aOCSPResponse)
    {
        return aEslestirmeSistemi.matchOCSPResponse(aCertificate,aIssuer,aOCSPResponse);
    }

    /**
     * Checks whether the given Certificate and Cross Certificate are matching 
     */
    public static boolean isMatchingCrossCertificate(MatchSystem aMatchSystem,
                                                     ECertificate aCertificate,
                                                     ECertificate aCrossCertificate)
    {
        return aMatchSystem.matchCrossCertificate(aCertificate, aCrossCertificate);
    }

    /**
     * match CRL and Delte CRL
     */
    public static boolean isMatchingDeltaCRL(MatchSystem aMatchSystem,
                                             ECRL aBaseCRL,
                                             ECRL aDeltaCRL)
    {
        return aMatchSystem.matchDeltaCRL(aBaseCRL, aDeltaCRL);
    }

    /**
     * Saves given certificate acording to savesytem
     */
    public static void saveCertificate(SaveSystem aSaveSystem,
                                       ECertificate aCertificate)
            throws ESYAException
    {
        List<CertificateSaver> certificateSavers = aSaveSystem.getCertificateSavers();
        for (CertificateSaver saver : certificateSavers) {
            saver.addCertificate(aCertificate);
        }
    }

    /*
     * Saves given CRL according to saveSystem
     *
    public static void saveCRL(SaveSystem aSaveSystem,
                               ECRL aCRL)
            throws ESYAException
    {
        List<CRLSaver> crlSavers = aSaveSystem.getCRLSavers();
        for (CRLSaver crlSaver : crlSavers) {
            crlSaver.addCRL(aCRL);
        }
    }

    public static void	saveOCSPResponse( SaveSystem aKaydetmeSistemi, EOCSPResponse aOCSP, ECertificate aCertificate)
            throws ESYAException
    {
        List<OCSPResponseSaver> osList = aKaydetmeSistemi.getOCSPResponseSavers();
        for (int i = 0 ; i <osList.size() ; i++)
        {
            osList.get(i).addOCSP(aOCSP.getBasicOCSPResponse(), aCertificate);
        }
    }
    */


}
