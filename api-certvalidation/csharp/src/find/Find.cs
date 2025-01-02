using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
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
            foreach (ECertificate certificate in aCertList)
            {
                if (isMatchingIssuer(aMatchSystem, aCertificate, certificate))
                    return certificate;
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
            foreach (ECertificate certificate in aCertList)
            {
                if (certificate.Equals(aCertificate))
                    continue;// bunu sonradan ekledik. loopları engellesin diye
                if (isMatchingIssuer(aMatchSystem, aCertificate, certificate) && !aMatches.Contains(certificate))
                    aMatches.Add(certificate);
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
            if (aCertList != null)
            {
                foreach (ECertificate certificate in aCertList)
                {
                    //Console.WriteLine("*****    " + certificate.ToString());
                    if (isMatchingIssuer(aMatchSystem, aCRL, certificate))
                        return certificate;
                }
            }
            //Console.WriteLine("bU SİL İCİN BİRSEY BULAMADIM" + aCRL.ToString());
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
            foreach (ECertificate certificate in aCertList)
            {
                if (isMatchingIssuer(aMatchSystem, aCRL, certificate) && !aMatches.Contains(certificate))
                    aMatches.Add(certificate);
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
            foreach (ECRL crl in aCRLList)
            {
                if (!aMatches.Contains(crl) && isMatcingCRL(aMatchSystem, aCertificate, crl))
                    aMatches.Add(crl);
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
            if (aCRLS.Count > 0)
                return aCRLS[0];
            return null;
        }

        /**
         * Finds the delta-CRL()s of the given base CRL from a list.
         */
        public static ECRL deltaCRLFromList(MatchSystem aMatchSystem,
                                            List<ECRL> aCRLList,
                                            ECRL aBaseCRL)
        {
            if (aCRLList != null)
            {
                foreach (ECRL crl in aCRLList)
                {
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
            foreach (ECertificate certificate in aCertificates)
            {
                if (isMatchingCrossCertificate(aMatchSystem, aCertificate, certificate))
                    return certificate;
            }

            return null;
        }

        /**
         * Checks whether the given CRL and Issuer Certificate are matching
         */
        public static bool isMatchingIssuer(MatchSystem aMatchSystem,
                                               ECRL aCRL,
                                               ECertificate aIssuerCertificate)
        {
            return aMatchSystem.matchCertificate(aCRL, aIssuerCertificate);
        }

        /**
        * Checks whether the given Certificate and Issuer Certificate are matching
        */
        public static bool isMatchingIssuer(MatchSystem aMatchSystem,
                                               ECertificate aCertificate,
                                               ECertificate aIssuerCertificate)
        {
            return aMatchSystem.matchCertificate(aCertificate, aIssuerCertificate);
        }

        /**
         * Checks whether the given Certificate and CRL are matching
         */
        public static bool isMatcingCRL(MatchSystem aMatchSystem,
                                           ECertificate aCertificate,
                                           ECRL aCRL)
        {
            return aMatchSystem.matchCRL(aCertificate, aCRL);
        }
        public static bool isMatchingOCSPResponse(MatchSystem aEslestirmeSistemi, ECertificate aCertificate, ECertificate aIssuer, EOCSPResponse aOCSPResponse)
        {
            return aEslestirmeSistemi.matchOCSPResponse(aCertificate, aIssuer, aOCSPResponse);
        }

        /**
         * Checks whether the given Certificate and Cross Certificate are matching 
         */
        public static bool isMatchingCrossCertificate(MatchSystem aMatchSystem,
                                                         ECertificate aCertificate,
                                                         ECertificate aCrossCertificate)
        {
            return aMatchSystem.matchCrossCertificate(aCertificate, aCrossCertificate);
        }

        /**
         * match CRL and Delta CRL
         */
        public static bool isMatchingDeltaCRL(MatchSystem aMatchSystem,
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
        {
            List<CertificateSaver> certificateSavers = aSaveSystem.getCertificateSavers();
            foreach (CertificateSaver saver in certificateSavers)
            {
                saver.addCertificate(aCertificate);
            }
        }

        /*
         * Saves given CRL according to saveSystem
         *
        public static void saveCRL(SaveSystem aSaveSystem,
                                   ECRL aCRL)
        {
            List<CRLSaver> crlSavers = aSaveSystem.getCRLSavers();
            foreach (CRLSaver crlSaver in crlSavers)
            {
                crlSaver.addCRL(aCRL);
            }
        }

        public static void saveOCSPResponse(SaveSystem aKaydetmeSistemi, EOCSPResponse aOCSP, ECertificate aCertificate)
        {
            List<OCSPResponseSaver> osList = aKaydetmeSistemi.getOCSPResponseSavers();
            for (int i = 0; i < osList.Count; i++)
            {
                osList[i].addOCSP(aOCSP.getBasicOCSPResponse(), aCertificate);
            }
        }*/

    }
}
