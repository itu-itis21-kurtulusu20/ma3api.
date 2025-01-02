using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * <p>Container class for the Finder objects specified by FindingPolicy
     *
     * @author IH
     */
    public class FindSystem
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private static readonly List<ECertificate> EMPTY_CERTIFICATE_LIST = new List<ECertificate>(0);
        private static readonly List<ECRL> EMPTY_CRL_LIST = new List<ECRL>(0);

        private readonly List<ECertificate> mTrustedCertificates = new List<ECertificate>(0);
        private bool mTrustedCertificatesFound;

        // sertifika bulucu
        private List<CertificateFinder> mCertificateFinders = new List<CertificateFinder>(0);
        // sil bulucu
        private List<CRLFinder> mCRLFinders = new List<CRLFinder>(0);
        // OCSP cevabı bulucu
        private List<OCSPResponseFinder> mOCSPResponseFinders = new List<OCSPResponseFinder>(0);
        // guvenilir sertifika buluc
        private List<TrustedCertificateFinder> mTrustedCertificateFinders = new List<TrustedCertificateFinder>();
        // Çapraz sertifika bulucu
        private List<CrossCertificateFinder> mCrossCertificateFinders = new List<CrossCertificateFinder>(0);
        // delta sil bulucu
        private List<DeltaCRLFinder> mDeltaCRLFinders = new List<DeltaCRLFinder>(0);

        //private CertificateCriteriaMatcher mMatcher = new CertificateCriteriaMatcher();

        public FindSystem()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.SERTIFIKADOGRULAMA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }


        public List<ECertificate> getTrustedCertificates()
        {
            return mTrustedCertificates;
        }

        public void findTrustedCertificates()
        {
            if (!mTrustedCertificatesFound)
            {
                foreach (TrustedCertificateFinder gsBulucu in mTrustedCertificateFinders)
                {
                    //if (gsBulucu)
                    // Guvenilir Sertifika Bulucu Eklenmiş mi ?
                    mTrustedCertificates.AddRange(gsBulucu.findTrustedCertificate());
                }
                mTrustedCertificatesFound = true;
            }
        }

        public void addTrustedCertificate(ECertificate aCertificate)
        {
            if (mTrustedCertificates.IndexOf(aCertificate) < 0)
                mTrustedCertificates.Add(aCertificate);
        }

        public bool isTrustedCertificate(ECertificate aCertificate)
        {
            foreach (ECertificate cert in mTrustedCertificates)
            {
                if (aCertificate.Equals(cert))
                {
                    return true;
                }
            }
            return false;
        }


        public List<CertificateFinder> getCertificateFinders()
        {
            return mCertificateFinders;
        }

        public void setCertificateFinders(List<CertificateFinder> aCertificateFinders)
        {
            mCertificateFinders = aCertificateFinders;
        }

        public void addCertificateFinder(CertificateFinder aCertificateFinder)
        {
            mCertificateFinders.Add(aCertificateFinder);
        }

        public List<TrustedCertificateFinder> getTrustedCertificateFinders()
        {
            return mTrustedCertificateFinders;
        }

        public void setTrustedCertificateFinders(List<TrustedCertificateFinder> aTrustedCertificateFinders)
        {
            mTrustedCertificateFinders = aTrustedCertificateFinders;
        }

        public void addTrustedCertificateFinder(TrustedCertificateFinder aTrustedCertificateFinder)
        {
            mTrustedCertificateFinders.Add(aTrustedCertificateFinder);
        }

        public List<CrossCertificateFinder> getCrossCertificateFinders()
        {
            return mCrossCertificateFinders;
        }

        public void setCrossCertificateFinders(List<CrossCertificateFinder> aCrossCertificateFinders)
        {
            mCrossCertificateFinders = aCrossCertificateFinders;
        }

        public void addCrossCertificateFinder(CrossCertificateFinder aCrossCertificateFinder)
        {
            mCrossCertificateFinders.Add(aCrossCertificateFinder);
        }

        public List<CRLFinder> getCRLFinders()
        {
            return mCRLFinders;
        }

        public void setCRLFinders(List<CRLFinder> aCRLFinders)
        {
            mCRLFinders = aCRLFinders;
        }

        public void addCRLFinder(CRLFinder aCRLFinder)
        {
            mCRLFinders.Add(aCRLFinder);
        }

        public List<OCSPResponseFinder> getOCSPResponseFinders()
        {
            return mOCSPResponseFinders;
        }

        public void setOCSPResponseFinders(List<OCSPResponseFinder> aOCSPResponseFinders)
        {
            mOCSPResponseFinders = aOCSPResponseFinders;
        }

        public void addOCSPResponseFinder(OCSPResponseFinder aOCSPResponseFinder)
        {
            mOCSPResponseFinders.Add(aOCSPResponseFinder);
        }

        public List<DeltaCRLFinder> getDeltaCRLFinders()
        {
            return mDeltaCRLFinders;
        }

        public void setDeltaCRLFinders(List<DeltaCRLFinder> aDeltaCRLFinders)
        {
            mDeltaCRLFinders = aDeltaCRLFinders;
        }

        public void addDeltaCRLFinder(DeltaCRLFinder aDeltaCRLFinder)
        {
            mDeltaCRLFinders.Add(aDeltaCRLFinder);
        }

        public List<ECertificate> findTrustedCertificates(ValidationSystem aValidationSystem)
        {
            if (mTrustedCertificateFinders.Count == 0)
                return EMPTY_CERTIFICATE_LIST;

            List<ECertificate> gsList = new List<ECertificate>(0);

            foreach (TrustedCertificateFinder gsb in mTrustedCertificateFinders)
            {
                gsList.AddRange(gsb.findTrustedCertificate());
            }

            return gsList;
        }

        public ECertificate findIssuerCertificate(ValidationSystem aValidationSystem,
                                                  CertificateStatusInfo aCertStatusInfo)
        {
            ECertificate cert = aCertStatusInfo.getCertificate();
            MatchSystem matchSystem = aValidationSystem.getMatchSystem();

            String subject = cert.getSubject().stringValue();

            if (logger.IsDebugEnabled)
                logger.Debug(subject + " sertifika SM sertifikası bulunacak");

            ECertificate certificate = null;

            // Kontrol Edilmiş Sertifikalar arasında mı kontrol edelim
            List<ECertificate> certList = aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
            certificate = Find.fromList(matchSystem, certList, cert);
            if (certificate != null)
            {
                CertificateStatusInfo csi = aValidationSystem.getCertificateValidationCache().getCheckResult(certificate);
                aCertStatusInfo.setSigningCertficateInfo(csi/*.Clone()*/);//TODO clone gerekli mi??
                if (csi.getCertificateStatus() == CertificateStatus.VALID)
                    return certificate;
                else return null;
            }

            // Güvenilir Sertifikalar arasında mı kontrol edelim
            List<ECertificate> trustedCertificates = getTrustedCertificates();
            certificate = Find.fromList(matchSystem, trustedCertificates, cert);
            if (certificate != null)
            {
                CertificateController certificateController = new CertificateController();
                CertificateStatusInfo csi = certificateController.check(aValidationSystem, certificate);
                aCertStatusInfo.setSigningCertficateInfo(csi);
                if (csi.getCertificateStatus() == CertificateStatus.VALID)
                    return certificate;
                else return null;
            }


            foreach (CertificateFinder certificateFinder in mCertificateFinders)
            {
                certList = certificateFinder.findCertificate(aCertStatusInfo.getCertificate());
                certificate = Find.fromList(matchSystem, certList, cert);

                if ((certificate == null) ||        // Sertifika bulunmuş mu?
                        (certificateFinder.isToBeChecked()
                                && !_checkIssuer(aValidationSystem, aCertStatusInfo, certificate)))    // geçerli mi?
                {
                    certificate = null;
                }
                else
                {
                    if (certificateFinder.isToBeChecked())
                        Find.saveCertificate(aValidationSystem.getSaveSystem(), certificate);

                    return certificate; // geçerli sertifika bulundu geri dönülecek.
                }
            }

            return certificate;

        }


        /*
        public List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria)
        {
            List<ECertificate> certList = new List<ECertificate>();

            foreach (CertificateFinder certificateFinder in mCertificateFinders)
            {
                certList = certificateFinder.searchCertificates(aCriteria);

                if (certList != null && certList.Count > 0) // Sertifika bulunmuş mu?
                {
                    return certList;
                }
            }

            foreach (CrossCertificateFinder crossCertificateFinder in mCrossCertificateFinders)
            {
                List<ECertificate> crossCerts = crossCertificateFinder.findCrossCertificate();
                foreach (ECertificate crossCert in crossCerts)
                {
                    if (mMatcher.match(aCriteria, crossCert))
                    {
                        if (certList == null)
                            certList = new List<ECertificate>();
                        certList.Add(crossCert);
                    }
                }
                if (certList != null && certList.Count > 0) // Sertifika bulunmuş mu?
                {
                    return certList;
                }

            }

            foreach (TrustedCertificateFinder trustedCertificateFinder in mTrustedCertificateFinders)
            {
                List<ECertificate> trustedCerts = trustedCertificateFinder.findTrustedCertificate();
                foreach (ECertificate trustedCert in trustedCerts)
                {
                    if (mMatcher.match(aCriteria, trustedCert))
                    {
                        if (certList == null)
                            certList = new List<ECertificate>();
                        certList.Add(trustedCert);
                    }
                }
                if (certList != null && certList.Count > 0) // Sertifika bulunmuş mu?
                {
                    return certList;
                }

            }
            return certList;
        }
        public List<EOCSPResponse> searchOCSPResponses(OCSPSearchCriteria aCriteria)
        {
            // todo
            return null;
        }

        public List<ECRL> searchCRLs(CRLSearchCriteria aCriteria)
        {
            // todo
            return null;
        }
        */

        public ECertificate findCRLIssuerCertificate(ValidationSystem aValidationSystem,
                                                     CRLStatusInfo aCRLStatusInfo)
        {
            /*if (mCertificateFinders.Count == 0)
                return null;*/
            //Console.WriteLine("## findCRLIssuerCertificate baslıyor");
            ECertificate certificate = null;
            MatchSystem matchSystem = aValidationSystem.getMatchSystem();
            FindSystem findSystem = aValidationSystem.getFindSystem();

            ECRL crl = aCRLStatusInfo.getCRL();

            // Kontrol Edilmiş Sertifikalar arasında mı kontrol edelim
            List<ECertificate> certList = aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
            //Console.WriteLine("## Kontrol edilmis sertifikalar arasında mi?");
            certificate = Find.issuerCertificateFromList(matchSystem, certList, crl);
            if (certificate != null)
            {
                CertificateStatusInfo csi = aValidationSystem.getCertificateValidationCache().getCheckResult(certificate);
                aCRLStatusInfo.setSigningCertficateInfo(csi/*.Clone()*/); //todo clone gerekli mi??
                if (csi.getCertificateStatus() == CertificateStatus.VALID)
                {
                    //Console.WriteLine("## Bu sertifika bulundu" + certificate.ToString());
                    return certificate;
                }
                else
                {
                    //Console.WriteLine("## Bulunan birşey yok!");
                    return null;
                }
            }

            // Güvenilir Sertifikalar arasında mı kontrol edelim
            List<ECertificate> trustedCertificates = findSystem.getTrustedCertificates();
            //Console.WriteLine("## Güvenilir sertifikalar arasında mi?");
            certificate = Find.issuerCertificateFromList(matchSystem, trustedCertificates, crl);
            if (certificate != null)
            {
                CertificateController certificateController = new CertificateController();
                CertificateStatusInfo csi = certificateController.check(aValidationSystem, certificate);
                aCRLStatusInfo.setSigningCertficateInfo(csi);
                if (csi.getCertificateStatus() == CertificateStatus.VALID)
                    return certificate;
                else return null;
            }


            // Dışarıdan Verilmiş Sertifikalar arasında mı kontrol edelim
            List<ECertificate> userInitialCertList = aValidationSystem.getUserInitialCertificateSet();
            if (userInitialCertList != null)
            {
                //Console.WriteLine("## Dışarıdan Verilmiş Sertifikalar arasında mı?");
                certificate = Find.issuerCertificateFromList(matchSystem, userInitialCertList, crl);
            }
            if (certificate != null && _checkIssuer(aValidationSystem, aCRLStatusInfo, certificate))
            {
                return certificate;
            }
            certificate = null;

            // CA Cache e bakalim.
            List<ECertificate> cachedCACerts = aValidationSystem.getCachedCACertificates(crl.getIssuer().stringValue());
            certificate = Find.issuerCertificateFromList(matchSystem, cachedCACerts, crl);
            if ((certificate != null) && _checkIssuer(aValidationSystem, aCRLStatusInfo, certificate))
            {
                return certificate;
            }

            certificate = null;

            // todo AIA varsa oradan arasin, yoksa bir kritere gore arasin 
            // Hala bulamadıysak bulucular arasın
            //Console.WriteLine("## Sertifika bulamadik?");
            foreach (CertificateFinder pSB in mCertificateFinders)
            {
                certList = pSB.findCertificate();
                certificate = Find.issuerCertificateFromList(matchSystem, certList, crl);
                if ((certificate == null) ||									// Sertifika bulunmus mu?
                    (!_checkIssuer(aValidationSystem, aCRLStatusInfo, certificate)))	// gecerli mi?
                {
                    certificate = null;
                }
                else // Bulduk ve dogruladik
                {
                    break;
                }
            }

            if (certificate != null)
            {
                Find.saveCertificate(aValidationSystem.getSaveSystem(), certificate);
            }
            //Console.WriteLine("## findCRLIssuerCertificate BİTİYOR");
            return certificate;
        }



        public EOCSPResponse findOCSPResponseFromInitial(ValidationSystem aValidationSystem, ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo)
        {
            EOCSPResponse ocspResponse;

            ECertificate certificate = aCertStatusInfo.getCertificate();

            List<EOCSPResponse> initial = aValidationSystem.getUserInitialOCSPResponseSet();
            if (initial != null && initial.Count > 0)
            {
                logger.Debug("Initial ocsp set size: " + initial.Count);
                for (int i = 0; i < initial.Count; i++)
                {
                    EOCSPResponse response = initial[i];

                    EResponseData rd = response.getBasicOCSPResponse().getTbsResponseData();
                    for (int j = 0; j < rd.getSingleResponseCount(); j++)
                    {
                        ECertID certId = rd.getSingleResponse(j).getCertID();
                        if (_matches(aValidationSystem, certId, certificate))
                            return response;
                    }
                }
            }
            return null;
        }

        public EOCSPResponse findOCSPResponse(ValidationSystem aValidationSystem, ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo)
        {
            EOCSPResponse ocspResponse;

            ECertificate certificate = aCertStatusInfo.getCertificate();

            //Console.WriteLine("Ocsp response finders: " + mOCSPResponseFinders.Count);
            foreach (OCSPResponseFinder ocspResponseFinder in mOCSPResponseFinders)
            {
                logger.Debug("Ocsp Response finder : " + ocspResponseFinder);
                ocspResponse = ocspResponseFinder.findOCSPResponse(aCertStatusInfo.getCertificate(), aIssuerCertificate);
                if (ocspResponse != null)
                    return ocspResponse;
            }
            return null;
        }



        private bool _matches(ValidationSystem aVS, ECertID aCertId, ECertificate aCertificate)
        {
            if (!aCertId.getSerialNumber().Equals(aCertificate.getSerialNumber()))
                return false;

            try
            {
                DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(aCertId.getHashAlgorithm());
                byte[] issuerName = aCertificate.getIssuer().getBytes();
                byte[] issuerNameHash = DigestUtil.digest(digestAlg, issuerName);

                //if (!Arrays.equals(aCertId.getIssuerNameHash(), issuerNameHash))
                if (!aCertId.getIssuerNameHash().SequenceEqual(issuerNameHash))
                    return false;

                List<ECertificate> issuers = findIssuerCertificate(aVS, aCertificate, true);
                ECertificate issuer = issuers[0];
                byte[] issuerKey = issuer.getSubjectPublicKeyInfo().getSubjectPublicKey();
                byte[] issuerKeyHash = DigestUtil.digest(digestAlg, issuerKey);

                //if (!Arrays.equals(aCertId.getIssuerKeyHash(), issuerKeyHash))
                if (!aCertId.getIssuerKeyHash().SequenceEqual(issuerKeyHash))
                    return false;

            }
            catch (Exception x)
            {
                //x.printStackTrace();
                Console.WriteLine(x.ToString());
                return false;
            }

            return true;
        }


        public List<ECRL> findCRL(ValidationSystem aValidationSystem, CertificateStatusInfo aCertStatusInfo)
        {
            List<ECRL> crlList = new List<ECRL>();

            /*if (mCRLFinders.Count == 0)
                return crlList;*/

            MatchSystem matchSystem = aValidationSystem.getMatchSystem();
            ECertificate certificate = aCertStatusInfo.getCertificate();
            ECRL pSIL = null;

            String subject = certificate.getSubject().stringValue();
            if (logger.IsDebugEnabled)
                logger.Debug(subject + " sertifika sili bulunacak");


            List<ECRL> matchingCRLList = new List<ECRL>();

            logger.Debug("CRL Count: " + aValidationSystem.getUserInitialCRLSet().Count);

            // Kullanıcı tanımlı sillerden eslesenleri alalım
            matchingCRLList = Find.crlsFromList(matchSystem, aValidationSystem.getUserInitialCRLSet(), matchingCRLList, certificate);

            logger.Debug("Matching CRL Count:" + matchingCRLList.Count);

            // Bulucularımız silleri bulsun
            foreach (CRLFinder pSB in mCRLFinders)
            {
                List<ECRL> altCRLList = pSB.findCRL(certificate);
                matchingCRLList = Find.crlsFromList(matchSystem, altCRLList, matchingCRLList, certificate);
            }

            // Bulduklarımızı kontrol edelim
            for (int i = 0; i < matchingCRLList.Count; i++)
            {
                ECRL sil = matchingCRLList[i];
                if ((checkCRL(aValidationSystem, aCertStatusInfo, sil)))    // Geçerli Mi?
                {
                   crlList.Add(sil);
                }
            }


            return crlList;
        }

        //public List<ECRL> silleriEslestirmesizBul(ECertificate iCert);

        public ECRL findDeltaCRL(ValidationSystem aValidationSystem, ECRL aBaseCRL, CertificateStatusInfo aCertStatusInfo)
        {
            /*if (mDeltaCRLFinders.Count == 0)
                return null;*/

            ECRL crl = null;
            MatchSystem es = aValidationSystem.getMatchSystem();

            // Dışarıdan verilmiş siller arasında mı bakalım
            List<ECRL> uiCRLList = aValidationSystem.getUserInitialCRLSet();
            crl = Find.deltaCRLFromList(es, uiCRLList, aBaseCRL);

            if (crl == null || !_isDeltaCRLValid(aValidationSystem, crl, aCertStatusInfo))
            {
                crl = null;
                foreach (DeltaCRLFinder deltaCRLFinder in mDeltaCRLFinders)
                {
                    List<ECRL> crlList = deltaCRLFinder.findDeltaCRL(aBaseCRL);
                    crl = Find.deltaCRLFromList(es, crlList, aBaseCRL);

                    if ((crl == null) ||    // deltaSIL bulundu mu?
                            (deltaCRLFinder.isToBeChecked()
                                    && !_isDeltaCRLValid(aValidationSystem, crl, aCertStatusInfo)))// deltaSIL geçerli mi?
                    {
                        crl = null;
                    }
                    else return crl;
                }
            }

            return crl;
        }

        public ECRL findDeltaCRL(ValidationSystem aValidationSystem, ECertificate aCertificate, CertificateStatusInfo aCertStatusInfo)
        {
            /*if (mDeltaCRLFinders.Count == 0)
                return null;*/

            ECRL crl = null;
            MatchSystem matchSystem = aValidationSystem.getMatchSystem();

            // Dışarıdan verilmiş siller arasında mı bakalım
            List<ECRL> uiCRLList = aValidationSystem.getUserInitialCRLSet();
            crl = Find.deltaCRLFromList(matchSystem, uiCRLList, aCertificate);

            if ((crl == null) || !_isDeltaCRLValid(aValidationSystem, crl, aCertStatusInfo))
            {
                crl = null;
                foreach (DeltaCRLFinder pDSB in mDeltaCRLFinders)
                {
                    List<ECRL> crlList = pDSB.findDeltaCRL(aCertificate);
                    crl = Find.deltaCRLFromList(matchSystem, crlList, aCertificate);

                    if ((crl == null) ||    // deltaSIL bulundu mu?
                            (pDSB.isToBeChecked()
                                    && !_isDeltaCRLValid(aValidationSystem, crl, aCertStatusInfo)))// deltaSIL geçerli mi?
                    {
                        crl = null;
                    }
                    else return crl;
                }
            }

            return crl;
        }

        public ECertificate findCrossCertificate(ValidationSystem aValidationSystem, ECertificate aCertificate)
        {
            /*if (!(mCrossCertificateFinders.Count == 0))
                return null;*/

            ECertificate certificate = null;
            MatchSystem matchSystem = aValidationSystem.getMatchSystem();

            // Geçerli Sertifikalar arasında var mı bakalım
            List<ECertificate> validCertList = aValidationSystem.getValidCertificateSet();
            certificate = Find.crossCertificateFromList(matchSystem, validCertList, aCertificate);
            if (certificate != null)
            {
                return certificate;
            }


            // Dışarıdan verilmiş sertifikalar arasında var mı bakalım
            List<ECertificate> uiCertList = aValidationSystem.getUserInitialCertificateSet();
            certificate = Find.crossCertificateFromList(matchSystem, uiCertList, aCertificate);


            if (certificate == null)
            {
                // Güvenilir Sertifikalar arasında var mı bakalım
                List<ECertificate> gsList = getTrustedCertificates();
                certificate = Find.crossCertificateFromList(matchSystem, gsList, aCertificate);
            }


            if ((certificate == null) || !isCrossCertValid(aValidationSystem, certificate))
            {
                certificate = null; //DELETE_MEMORY(pSertifika)
                // Hala bulamadıysak bulucularımız arasın
                foreach (CrossCertificateFinder crossCertFinder in mCrossCertificateFinders)
                {
                    List<ECertificate> certList = crossCertFinder.findCrossCertificate();
                    certificate = Find.crossCertificateFromList(matchSystem, certList, aCertificate);
                    if ((certificate == null) ||                    // Çapraz Sertifika Bulundu Mu ?
                            (!isCrossCertValid(aValidationSystem, certificate)))    // Geçerli Mi?
                    {
                        certificate = null;
                    }
                    else return certificate;
                }
            }

            return certificate;
        }


        bool _checkIssuer(ValidationSystem aValidationSystem, StatusInfo aStatusInfo, ECertificate aIssuerCertificate)
        {
            CertificateStatusInfo statusInfo;

            IssuerCheckParameters icp = aValidationSystem.getCheckSystem().getConstraintCheckParam();
            icp.increaseCertificateOrder();

            CertificateController certificateController = new CertificateController();
            statusInfo = certificateController.check(aValidationSystem, aIssuerCertificate);

            aStatusInfo.setSigningCertficateInfo(statusInfo);
            icp.decreaseCertificateOrder();


            // Sertifika gecerli ya da döngü korumadan dolayı kontrol edilmemiş olabilir. aksi durumda false dönmeliyiz
            return ((statusInfo.getCertificateStatus() == CertificateStatus.VALID) ||
                    (statusInfo.getCertificateStatus() == CertificateStatus.NOT_CHECKED));


        }

        public bool checkCRL(ValidationSystem aValidationSystem,
                                CertificateStatusInfo aCertStatusInfo,
                                ECRL aCRL)
        {
            // 		if (eslesenCRLList[i].getTBSCertList().getIssuer() == aSDB.sertifikaAl().getTBSCertificate().getSubject())
            // 			return true;
            // buraya bakılacak

            CRLController crlController = new CRLController();
            CRLStatusInfo csi = crlController.check(aValidationSystem, aCRL);

            if (csi.getCRLStatus() == CRLStatus.VALID)	// Geçerli Mi?
            {
                if (!aCertStatusInfo.getTrustCertificate().Equals(csi.getTrustCertificate()))
                {
                    return false;
                }
            }
            else
            {
                aCertStatusInfo.addCRLInfo(csi);
                return false;
            }
            aCertStatusInfo.addCRLInfo(csi);
            return true;
         }


        bool _isDeltaCRLValid(ValidationSystem aValidationSystem, ECRL aDeltaCRL, CertificateStatusInfo aCertStatusInfo)
        {
            DeltaCRLController dsk = new DeltaCRLController();
            CRLStatusInfo crlStatusInfo = dsk.check(aValidationSystem, aDeltaCRL);
            aCertStatusInfo.addDeltaCRLInfo(crlStatusInfo);

            return (crlStatusInfo.getCRLStatus() == CRLStatus.VALID);
        }

        bool isCrossCertValid(ValidationSystem aValidationSystem, ECertificate aCrossCertificate)
        {
            CertificateController certificateController = new CertificateController();

            CertificateStatusInfo csi = certificateController.check(aValidationSystem, aCrossCertificate);

            return (csi.getCertificateStatus() == CertificateStatus.VALID);
        }


        // todo iRemoteSearch default true

        public List<ECertificate> findIssuerCertificate(ValidationSystem aValidationSystem, ECertificate aCertificate, bool aRemoteSearch)
        {
            MatchSystem matchSystem = aValidationSystem.getMatchSystem();

            String subject = aCertificate.getSubject().stringValue();
            if (logger.IsDebugEnabled)
                logger.Debug(subject + " sertifika SM sertifikalari bulunacak");

            List<ECertificate> foundList = new List<ECertificate>();

            // Kontrol Edilmis Sertifikalar arasinda mi kontrol edelim
            List<ECertificate> certList = aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
            foundList = Find.issuerCertificatesFromList(matchSystem, certList, aCertificate, foundList);

            // Guvenilir Sertifikalar arasında mi kontrol edelim
            List<ECertificate> gsList = getTrustedCertificates();
            foundList = Find.issuerCertificatesFromList(matchSystem, gsList, aCertificate, foundList);

            // Disaridan Verilmis Sertifikalar arasinda mi kontrol edelim
            List<ECertificate> uiCertList = aValidationSystem.getUserInitialCertificateSet();
            foundList = Find.issuerCertificatesFromList(matchSystem, uiCertList, aCertificate, foundList);

            foreach (CertificateFinder certificateFinder in mCertificateFinders)
            {
                if (!aRemoteSearch && certificateFinder.isRemote())
                    continue; // Local modda arama yapiyorsak uzak buluculari geciyoruz.
                certList = certificateFinder.findCertificate(aCertificate);
                foundList = Find.issuerCertificatesFromList(matchSystem, certList, aCertificate, foundList);
            }

            return foundList;

        }
    }
}
