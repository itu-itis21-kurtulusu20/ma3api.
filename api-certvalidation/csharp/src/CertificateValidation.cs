using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.save;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.tools;//.Chronometer;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * CertificateValidation class includes the main certificate validation routines
     *
     * After creating ( {@link tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy} ),
     * the validation can be performed by calling the static functions listed in
     * this class
     *
     * The certificate validation algorithm consist of two process.The first one is
     * Path Building and the second is Path Validation. Both of these operations are
     * implemented according to Path Building and Basic Path Validation Algorithms
     * described in RFC 5280(6.1). First a certificate path is built by path 
     * building process and then that path is tried to be validated by path
     * validation process. Until a successfull path is found these two steps
     * continue.
     *
     * @see tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate
     * @see tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL
     * @see tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse
     * @see tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo
     * @see tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo
     * @see tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo
     *
     * @author IH
     */
    public class CertificateValidation
    {

        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * BulmaParam objesinin icine Guvenilir Sertifika Buluculari Ekler
         */
        static void createTrustedCertificateFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
        {
            List<PolicyClassInfo> trustedCertificateFinders = aFindingPolicy.getTrustedCertificateFinders();

            foreach (PolicyClassInfo policyClassInfo in trustedCertificateFinders)
            {
                try
                {
                    TrustedCertificateFinder finder = (TrustedCertificateFinder)Activator.CreateInstance(Type.GetType(policyClassInfo.getClassName()));
                    finder.setParameters(policyClassInfo.getParameters());
                    finder.setParentSystem(aValidationSystem);
                    aFindSystem.addTrustedCertificateFinder(finder);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }
        }

        /**
         * BulmaParam objesinin icine Sertifika Buluculari Ekler
         */
        static void createCertificateFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
        {
            List<PolicyClassInfo> sertifikaBulucular = aFindingPolicy.getCertificateFinders();

            foreach (PolicyClassInfo classInfo in sertifikaBulucular)
            {
                try
                {
                    CertificateFinder bulucu = (CertificateFinder)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    bulucu.setParameters(classInfo.getParameters());
                    bulucu.setParentSystem(aValidationSystem);
                    aFindSystem.addCertificateFinder(bulucu);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * BulmaParam objesinin icine SİL Buluculari Ekler
         */
        static void createCRLFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
        {
            List<PolicyClassInfo> crlFinders = aFindingPolicy.getCRLFinders();
            foreach (PolicyClassInfo classInfo in crlFinders)
            {
                try
                {
                    CRLFinder crlFinder = (CRLFinder)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    crlFinder.setParameters(classInfo.getParameters());
                    crlFinder.setParentSystem(aValidationSystem);
                    aFindSystem.addCRLFinder(crlFinder);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }

        }

        /**
         * BulmaParam objesinin icine delta-SİL Buluculari Ekler
         */
        static void createDeltaCRLFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
        {
            List<PolicyClassInfo> deltaCRLFinders = aFindingPolicy.getDeltaCRLFinders();
            foreach (PolicyClassInfo classInfo in deltaCRLFinders)
            {
                try
                {
                    DeltaCRLFinder deltaCRLFinder = (DeltaCRLFinder)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    deltaCRLFinder.setParameters(classInfo.getParameters());
                    deltaCRLFinder.setParentSystem(aValidationSystem);
                    aFindSystem.addDeltaCRLFinder(deltaCRLFinder);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }
        }

        /**
         * BulmaParam objesinin icine capraz Sertifika Buluculari Ekler
         */
        static void createCrossCertificateFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
        {
            List<PolicyClassInfo> crossCertificateFinders = aFindingPolicy.getCrossCertificateFinders();
            foreach (PolicyClassInfo sinif in crossCertificateFinders)
            {
                try
                {
                    CrossCertificateFinder finder = (CrossCertificateFinder)Activator.CreateInstance(Type.GetType(sinif.getClassName()));
                    finder.setParameters(sinif.getParameters());
                    finder.setParentSystem(aValidationSystem);
                    aFindSystem.addCrossCertificateFinder(finder);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }
        }


        /**
         * BulmaParam objesinin icine OCSP Cevabı Buluculari ekler
         */
        static void createOCSPResponseFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
        {
            List<PolicyClassInfo> ocspResponseFinders = aFindingPolicy.getOCSPResponseFinders();
            foreach (PolicyClassInfo classInfo in ocspResponseFinders)
            {
                try
                {
                    OCSPResponseFinder finder = (OCSPResponseFinder)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    finder.setParameters(classInfo.getParameters());
                    finder.setParentSystem(aValidationSystem);
                    aFindSystem.addOCSPResponseFinder(finder);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * Bulma objesinin icine Buluculari Ekler
         */
        static void createFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
        {
            createTrustedCertificateFinders(aFindingPolicy, aFindSystem, aValidationSystem);
            createCertificateFinders(aFindingPolicy, aFindSystem, aValidationSystem);
            createCRLFinders(aFindingPolicy, aFindSystem, aValidationSystem);
            createDeltaCRLFinders(aFindingPolicy, aFindSystem, aValidationSystem);
            createOCSPResponseFinders(aFindingPolicy, aFindSystem, aValidationSystem);
            createCrossCertificateFinders(aFindingPolicy, aFindSystem, aValidationSystem);

        }


        /**
         * EslestirmeParam objesinin icine Sertifika Eslestiricileri Ekler
         */
        static void createCertificateMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
        {
            List<PolicyClassInfo> certificateMatchers = aMatchingPolicy.getCertificateMatchers();
            foreach (PolicyClassInfo classInfo in certificateMatchers)
            {
                try
                {
                    CertificateMatcher certificateMatcher = (CertificateMatcher)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    //certificateMatcher.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                    aMatchSystem.addCertificateMatcher(certificateMatcher);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }


        /**
         * EslestirmeParam objesinin içine SİL Eslestiricileri Ekler
         */
        static void createCRLMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
        {
            List<PolicyClassInfo> crlMatchers = aMatchingPolicy.getCRLMatchers();
            foreach (PolicyClassInfo classInfo in crlMatchers)
            {
                try
                {
                    CRLMatcher crlMatcher = (CRLMatcher)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    //crlMatcher.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                    aMatchSystem.addCRLMatcher(crlMatcher);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * EslestirmeParam objesinin içine delta-SİL Eslestiricileri Ekler
         */
        static void createDeltaCRLMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
        {
            List<PolicyClassInfo> deltaCRLMatchers = aMatchingPolicy.getDeltaCRLMatchers();
            foreach (PolicyClassInfo classInfo in deltaCRLMatchers)
            {
                try
                {
                    DeltaCRLMatcher deltaCRLMatcher = (DeltaCRLMatcher)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    //eslestiriciClass.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                    aMatchSystem.addDeltaCRLMatcher(deltaCRLMatcher);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * EslestirmeParam objesinin içine OCSP rresponse Eslestiricileri Ekler
         */
        static void createOCSPResponseMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
        {
            List<PolicyClassInfo> ocspMatchers = aMatchingPolicy.getOCSPResponseMatchers();
            foreach (PolicyClassInfo classInfo in ocspMatchers)
            {
                try
                {
                    //OCSPResponseMatcher ocspResponseMatcher = (OCSPResponseMatcher) Class.forName(classInfo.getClassName()).newInstance();
                    OCSPResponseMatcher ocspResponseMatcher = (OCSPResponseMatcher)Activator.CreateInstance(Type.GetType(classInfo.getClassName()));
                    aMatchSystem.addOCSPResponseMatcher(ocspResponseMatcher);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }
        }


        /**
         * EslestirmeParam objesinin içine Çapraz Sertifika Eslestiricileri Ekler
         */
        static void createCrosscertificateMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
        {
            List<PolicyClassInfo> crossCertificateMatchers = aMatchingPolicy.getCrossCertificateMatchers();
            foreach (PolicyClassInfo aCaprazSertifikaEslestiriciler in crossCertificateMatchers)
            {
                try
                {
                    CrossCertificateMatcher crossCertificateMatcher = (CrossCertificateMatcher)Activator.CreateInstance(Type.GetType(aCaprazSertifikaEslestiriciler.getClassName()));
                    //eslestiriciClass.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                    aMatchSystem.addCrossCertificateMatcher(crossCertificateMatcher);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * EslestirmeParam objesinin içine Eslestiricileri Ekler
         */
        static void createMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
        {
            createCertificateMatchers(aMatchingPolicy, aMatchSystem);
            createCRLMatchers(aMatchingPolicy, aMatchSystem);
            createDeltaCRLMatchers(aMatchingPolicy, aMatchSystem);
            createOCSPResponseMatchers(aMatchingPolicy, aMatchSystem);
            createCrosscertificateMatchers(aMatchingPolicy, aMatchSystem);
        }

        /**
         * Creates certificate savers according to the given SavePolicy
         */
        static void createCertificateSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem) 
        {
    	    SaveSystem ss = oValidationSystem.getSaveSystem();
    	    List<PolicyClassInfo> certSaversInfo = aValidationPolicy.bulmaPolitikasiAl().getSavePolicy().getCertificateSavers();
            foreach (PolicyClassInfo policyClassInfo in certSaversInfo)
            {
               try
    		    {
                    CertificateSaver saver = (CertificateSaver)Activator.CreateInstance(Type.GetType(policyClassInfo.getClassName()));
                    saver.setParameters(policyClassInfo.getParameters());
    			    saver.setParentSystem(oValidationSystem);
    			    ss.addCertificateSaver(saver);
    		    }
    		    catch(Exception aEx)
    		    {
    			    throw new ESYAException(aEx);
    		    } 
            }
        }

        /**
         * Creates CRL savers according to the given SavePolicy
         * @param aValidationPolicy
         * @param oValidationSystem
         * @throws ESYAException
         */
        static void createCRLSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem) 
        {
    	    SaveSystem ss = oValidationSystem.getSaveSystem();
    	    List<PolicyClassInfo> crlSaversInfo = aValidationPolicy.bulmaPolitikasiAl().getSavePolicy().getCRLSavers();
    	    foreach (PolicyClassInfo crlSaverInfo in crlSaversInfo) 
    	    {
    		    try
    		    {
                    CRLSaver saver = (CRLSaver)Activator.CreateInstance(Type.GetType(crlSaverInfo.getClassName()));
    			    saver.setParameters(crlSaverInfo.getParameters());
    			    saver.setParentSystem(oValidationSystem);
    			    ss.addCRLSaver(saver);
    		    }
    		    catch(Exception aEx)
    		    {
    			    throw new ESYAException(aEx);
    		    }
		    }
        }

        /**
         * Creates OCSPResponseSavers according to the given SavePolicy
         * @param aValidationPolicy
         * @param oValidationSystem
         * @throws ESYAException
         */
        static void createOCSPResponseSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem)
        {
    	    SaveSystem ss = oValidationSystem.getSaveSystem();
    	    List<PolicyClassInfo> ocspSaversInfo = aValidationPolicy.bulmaPolitikasiAl().getSavePolicy().getOCSPResponseSavers();
    	    foreach (PolicyClassInfo ocspSaverInfo in ocspSaversInfo) 
    	    {
    		    try
    		    {
    			    OCSPResponseSaver saver = (OCSPResponseSaver)Activator.CreateInstance(Type.GetType(ocspSaverInfo.getClassName()));
    			    saver.setParameters(ocspSaverInfo.getParameters());
    			    saver.setParentSystem(oValidationSystem);
    			    ss.addOCSPResponseSaver(saver);
    		    }
    		    catch(Exception aEx)
    		    {
    			    throw new ESYAException(aEx);
    		    }
		    }
        }

        /**
         * Creates savers according to the given SavePolicy
         * @param aValidationPolicy
         * @param oValidationSystem
         * @throws ESYAException 
         */
        static void createSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem)
        {
    	    createCertificateSavers(aValidationPolicy, oValidationSystem);
    	    createCRLSavers(aValidationPolicy, oValidationSystem);
    	    createOCSPResponseSavers(aValidationPolicy, oValidationSystem);
        }

        /**
         * DogrulamaParam objesinin içine SM Sertifika Kontrolculeri Ekler
         */
        static void createIssuerCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem checkSystem = aValidationSystem.getCheckSystem();
            List<PolicyClassInfo> ustSMKontrolcular = aValidationPolicy.getCertificateIssuerCheckers();
            foreach (PolicyClassInfo anUstSMKontrolcular in ustSMKontrolcular)
            {
                try
                {
                    IssuerChecker kontrolcuClass = (IssuerChecker)Activator.CreateInstance(Type.GetType(anUstSMKontrolcular.getClassName()));
                    kontrolcuClass.setCheckParameters(anUstSMKontrolcular.getParameters());
                    kontrolcuClass.setParentSystem(aValidationSystem);
                    checkSystem.addIssuerChecker(kontrolcuClass);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * DogrulamaParam objesinin içine Güvenilir Sertifika Kontrolculeri Ekler
         */
        static void createTrustedCertificateCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem ks = aValidationSystem.getCheckSystem();
            List<PolicyClassInfo>
                    gsKontrolcular = aValidationPolicy.getTrustedCertificateCheckers();
            foreach (PolicyClassInfo aGsKontrolcular in gsKontrolcular)
            {
                try
                {
                    CertificateSelfChecker kontrolcuClass = (CertificateSelfChecker)Activator.CreateInstance(Type.GetType(aGsKontrolcular.getClassName()));
                    kontrolcuClass.setCheckParameters(aGsKontrolcular.getParameters());
                    kontrolcuClass.setParentSystem(aValidationSystem);
                    ks.addTrustedCertificateChecker(kontrolcuClass);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }
        }


        /**
         * DogrulamaParam objesinin içine Tek Sertifika Kontrolculeri Ekler
         */
        static void createCertificateSelfCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem ks = aValidationSystem.getCheckSystem();
            List<PolicyClassInfo>
                    tekSerKontrolcular = aValidationPolicy.getCertificateSelfCheckers();
            foreach (PolicyClassInfo aTekSerKontrolcular in tekSerKontrolcular)
            {
                try
                {
                    CertificateSelfChecker kontrolcuClass = (CertificateSelfChecker)Activator.CreateInstance(Type.GetType(aTekSerKontrolcular.getClassName()));
                    kontrolcuClass.setCheckParameters(aTekSerKontrolcular.getParameters());
                    kontrolcuClass.setParentSystem(aValidationSystem);
                    ks.addCertificateSelfChecker(kontrolcuClass);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }
        }

        /**
         * DogrulamaParam objesinin içine iptal Kontrolcuları Ekler
         */
        static void createRevocationCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem ks = aValidationSystem.getCheckSystem();
            List<RevocationPolicyClassInfo> iptalKontrolcular = aValidationPolicy.getRevocationCheckers();
            for (int i = 0; i < iptalKontrolcular.Count; i++)
            {
                PolicyClassInfo iptalKontrolcu = iptalKontrolcular[i];
                try
                {
                    RevocationChecker kontrolcuClass = (RevocationChecker)Activator.CreateInstance(Type.GetType(iptalKontrolcu.getClassName()));//(RevocationChecker)Class.forName(iptalKontrolcu.getClassName()).newInstance();
                    kontrolcuClass.setCheckParameters(iptalKontrolcu.getParameters());
                    kontrolcuClass.setParentSystem(aValidationSystem);

                    List<PolicyClassInfo> finders = iptalKontrolcular[i].getFinders();
                    for (int ifk = 0; ifk < finders.Count; ifk++)
                    {
                        Finder finder = (Finder)Activator.CreateInstance(Type.GetType(finders[ifk].getClassName()));//Class.forName(finders[ifk].getClassName()).newInstance();
                        finder.setParameters(finders[ifk].getParameters());
                        finder.setParentSystem(aValidationSystem);
                        kontrolcuClass.addFinder(finder);
                    }

                    ks.addRevocationChecker(kontrolcuClass);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }

        }

        /**
         * DogrulamaParam objesinin içine Tek SİL Kontrolcuları Ekler
         */
        static void createCRLSelfCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem ks = aValidationSystem.getCheckSystem();
            List<PolicyClassInfo>
                    silKontrolcular = aValidationPolicy.getCRLSelfCheckers();
            foreach (PolicyClassInfo aSilKontrolcular in silKontrolcular)
            {
                try
                {
                    CRLSelfChecker kontrolcuClass = (CRLSelfChecker)Activator.CreateInstance(Type.GetType(aSilKontrolcular.getClassName()));
                    kontrolcuClass.setCheckParameters(aSilKontrolcular.getParameters());
                    kontrolcuClass.setParentSystem(aValidationSystem);
                    ks.addCRLSelfChecker(kontrolcuClass);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }


        /**
         * DogrulamaParam objesinin içine SİL-SM  Kontrolculeri Ekler
         */
        static void createCRLCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem ks = aValidationSystem.getCheckSystem();
            List<PolicyClassInfo>
                    silSMKontrolcular = aValidationPolicy.getCRLIssuerCheckers();
            foreach (PolicyClassInfo aSilSMKontrolcular in silSMKontrolcular)
            {
                try
                {
                    CRLIssuerChecker kontrolcuClass = (CRLIssuerChecker)Activator.CreateInstance(Type.GetType(aSilSMKontrolcular.getClassName()));
                    kontrolcuClass.setCheckParameters(aSilSMKontrolcular.getParameters());
                    kontrolcuClass.setParentSystem(aValidationSystem);
                    ks.addCRLIssuerChecker(kontrolcuClass);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }


        /**
         * DogrulamaParam objesinin içine delta-SİL Kontrolcüleri Ekler
         */
        static void createDeltaCRLCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem ks = aValidationSystem.getCheckSystem();
            List<PolicyClassInfo>
                    deltaCRLCheckers = aValidationPolicy.getDeltaCRLCheckers();
            foreach (PolicyClassInfo aDeltaCRLcheckerInfo in deltaCRLCheckers)
            {
                try
                {
                    DeltaCRLChecker deltaCRLChecker = (DeltaCRLChecker)Activator.CreateInstance(Type.GetType(aDeltaCRLcheckerInfo.getClassName()));
                    deltaCRLChecker.setCheckParameters(aDeltaCRLcheckerInfo.getParameters());
                    deltaCRLChecker.setParentSystem(aValidationSystem);
                    ks.addDeltaCRLChecker(deltaCRLChecker);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * DogrulamaParam objesinin içine OCSP Kontrolculeri Ekler
         */
        static void createOCSPResponseCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            CheckSystem ks = aValidationSystem.getCheckSystem();
            List<PolicyClassInfo>
                    ocspKontrolcular = aValidationPolicy.getOCSPResponseCheckers();
            foreach (PolicyClassInfo anOcspKontrolcular in ocspKontrolcular)
            {
                try
                {
                    OCSPResponseChecker kontrolcuClass = (OCSPResponseChecker)Activator.CreateInstance(Type.GetType(anOcspKontrolcular.getClassName()));
                    kontrolcuClass.setCheckParameters(anOcspKontrolcular.getParameters());
                    kontrolcuClass.setParentSystem(aValidationSystem);
                    ks.addOCSPResponseChecker(kontrolcuClass);
                }
                catch (Exception x)
                {
                    throw new ESYAException(x);
                }
            }


        }

        /**
         * DogrulamaParam objesinin içine Kontrolcuları Ekler
         */
        static void createCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            createIssuerCheckers(aValidationPolicy, aValidationSystem);
            createTrustedCertificateCheckers(aValidationPolicy, aValidationSystem);
            createCertificateSelfCheckers(aValidationPolicy, aValidationSystem);
            createRevocationCheckers(aValidationPolicy, aValidationSystem);
            createCRLSelfCheckers(aValidationPolicy, aValidationSystem);
            createCRLCheckers(aValidationPolicy, aValidationSystem);
            createDeltaCRLCheckers(aValidationPolicy, aValidationSystem);
            createOCSPResponseCheckers(aValidationPolicy, aValidationSystem);
        }


        /**
         * ValidationSystem objesinin icindeki parametreleri belirler
         */
        static void initParameters(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
        {
            aValidationSystem.setUserInitialPolicySet(aValidationPolicy.getUserInitialPolicySet());
            aValidationSystem.setInitialExplicitPolicy(aValidationPolicy.isInitialExplicitPolicy());
            aValidationSystem.setInitialPolicyMappingInhibit(aValidationPolicy.isInitialPolicyMappingInhibit());
            aValidationSystem.setInitialAnyPolicyInhibit(aValidationPolicy.isInitialAnyPolicyInhibit());
            aValidationSystem.setDefaultStorePath(aValidationPolicy.getDefaultStorePath());
        }

        /**
         * Create validation system from poliy
         *
         * @param aValidationPolicy policy for
         * @return a validation system
         * @throws ESYAException if anything goes wrong
         */
        public static ValidationSystem createValidationSystem(ValidationPolicy aValidationPolicy)
        {
            ValidationSystem vs = new ValidationSystem();

            FindingPolicy bpolitika = aValidationPolicy.bulmaPolitikasiAl();
            MatchingPolicy epolitika = bpolitika.getMatchingPolicy();

            // bulma politikamızdan bulucuları alıp bulma parametresini oluşturalım
            createFinders(bpolitika, vs.getFindSystem(), vs);
            // eslestirme politikamızdan eslestiricileri alıp eslestirme parametresini oluştural�m
            createMatchers(epolitika, vs.getMatchSystem());
            // dogrulama politikamızdan kontrolculeri alıp kontrol parametresini oluşturalım
            createCheckers(aValidationPolicy, vs);
            // Kaydedicileri olusturur.
            createSavers(aValidationPolicy, vs);
            // Dogrulama giris parametrelerini alır.
            initParameters(aValidationPolicy, vs);

            return vs;
        }

        /**
         * Validates multiple certificates
         *
         * @param aPolicyFile          XML policy config file
         * @param aCertificates        list of ceritificates for validation
         * @param aValidCertificates   known valid certificates
         * @param aUserInitialCertList initial user defined ceritificates
         * @return validation result per certificate
         * @throws ESYAException if anything goes wrong
         */
        public static Dictionary<ECertificate, CertificateStatusInfo> validateCertificates(
                                    String aPolicyFile,
                                    List<ECertificate> aCertificates,
                                    List<ECertificate> aValidCertificates,
                                    List<ECertificate> aUserInitialCertList)
        {

            ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(aPolicyFile);

            ValidationSystem validationSystem = createValidationSystem(validationPolicy);
            validationSystem.setValidCertificateSet(aValidCertificates);
            validationSystem.setUserInitialCertificateSet(aUserInitialCertList);
            return validateCertificates(validationSystem, aCertificates, false);
        }

        /**
         * Validates multiple certificates
         *
         * @param aValidationPolicy    validation policy
         * @param aCertificates        list of ceritificates to be validated
         * @param aValidCertificates   known valid certificates
         * @param aUserInitialCertList initial user defined ceritificates
         * @return validation result
         * @throws ESYAException if anything goes wrong
         */
        public static Dictionary<ECertificate, CertificateStatusInfo> validateCertificates(
                    ValidationPolicy aValidationPolicy,
                    List<ECertificate> aCertificates,
                    List<ECertificate> aValidCertificates,
                    List<ECertificate> aUserInitialCertList)
        {

            ValidationSystem validationSytem = createValidationSystem(aValidationPolicy);
            validationSytem.setValidCertificateSet(aValidCertificates);
            validationSytem.setUserInitialCertificateSet(aUserInitialCertList);

            return validateCertificates(validationSytem, aCertificates, false);
        }

        /**
         * Validates multiple ceritificates
         *
         * @param aValidationSystem validation parameters
         * @param aCertificates     list of ceritificates for validation
         * @return validation result per certificate
         * @throws ESYAException if anything goes wrong
         */
        public static Dictionary<ECertificate, CertificateStatusInfo> validateCertificates(
                                ValidationSystem aValidationSystem,
                                List<ECertificate> aCertificates,
                                bool aDoNotUsePastRevocationInfo)
        {

            Dictionary<ECertificate, CertificateStatusInfo> sdbList = new Dictionary<ECertificate, CertificateStatusInfo>();

            foreach (ECertificate certificate in aCertificates)
            {
                //CertificateStatusInfo csi = validateCertificate(aValidationSystem, certificate);
                CertificateStatusInfo csi = validateCertificate(aValidationSystem, certificate, aDoNotUsePastRevocationInfo);
                if (!sdbList.ContainsKey(certificate))
                    sdbList[certificate] = csi;
            }

            return sdbList;
        }


        private static void checkLicense(ECertificate aCer)
        {
            try
            {
                bool isTest = LV.getInstance().isTestLicense(LV.Products.SERTIFIKADOGRULAMA);
                if (isTest && !aCer.isOCSPSigningCertificate() && !aCer.isTimeStampingCertificate()
                        && !aCer.isCACertificate())
                {
                    if (!aCer.getSubject().getCommonNameAttribute().ToLower().Contains("test"))
                    {
                        throw new SystemException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                    }
                }
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }

        }


        /**
         * Validates certificate
         *
         * @param aPolicyFile          XML policy config file
         * @param aCertificate         for validation
         * @param aValidCertificates   known valid certificates
         * @param aUserInitialCertList initial user defined ceritificates
         * @return validation result
         * @throws ESYAException if anything goes wrong
         */
        public static CertificateStatusInfo validateCertificate(String aPolicyFile,
                                                                ECertificate aCertificate,
                                                                List<ECertificate> aValidCertificates,
                                                                List<ECertificate> aUserInitialCertList)
        {
            ValidationPolicy policy = PolicyReader.readValidationPolicy(aPolicyFile);
            return validateCertificate(policy, aCertificate, aValidCertificates, aUserInitialCertList);
        }

        // todo bu method JAVA'da comment out edilmis
        /**
         * Validates certificate
         *
         * @param aPolicyFile          XML policy config file
         * @param aCertificate         for validation
         * @param aValidCertificates   known valid certificates
         * @param aUserInitialCertList initial user defined ceritificates
         * @return validation result
         * @throws ESYAException if anything goes wrong
         */
        public static CertificateStatusInfo validateCertificateInPast(
                                                String aPolicyFile,
                                                ECertificate aCertificate,
                                                List<ECertificate> aValidCertificates,
                                                List<ECertificate> aUserInitialCertList,
                                                DateTime? aBaseValidationTime,
                                                DateTime? aLastRevocationTime,
                                                bool aDoNotUsePastRevocationInfo)
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.SERTIFIKADOGRULAMA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
            ValidationPolicy policy = PolicyReader.readValidationPolicy(aPolicyFile);
            return validateCertificateInPast(policy, aCertificate, aValidCertificates, aUserInitialCertList, aBaseValidationTime, aLastRevocationTime, aDoNotUsePastRevocationInfo);
        }



        /**
         * Validate certificate according to policy
         *
         * @param aValidationPolicy    policy for validation
         * @param aCertificate         for validation
         * @param aValidCertificates   known valid certificates
         * @param aUserInitialCertList initial user defined ceritificates
         * @return validation result for certificate
         * @throws ESYAException if anything goes wrong
         */
        public static CertificateStatusInfo validateCertificate(ValidationPolicy aValidationPolicy,
                                                                ECertificate aCertificate,
                                                                List<ECertificate> aValidCertificates,
                                                                List<ECertificate> aUserInitialCertList)
        {
            ValidationSystem validationSystem = createValidationSystem(aValidationPolicy);
            validationSystem.setValidCertificateSet(aValidCertificates);
            validationSystem.setUserInitialCertificateSet(aUserInitialCertList);

            return validateCertificate(validationSystem, aCertificate);
        }

        // todo bu method JAVA'da comment out edilmis
        /**
         * Validate certificate according to policy in pat time 
         *
         * @param aValidationPolicy    policy for validation
         * @param aCertificate         for validation
         * @param aValidCertificates   known valid certificates
         * @param aUserInitialCertList initial user defined ceritificates
         * @return validation result for certificate
         * @throws ESYAException if anything goes wrong
         */
        public static CertificateStatusInfo validateCertificateInPast(
                                                ValidationPolicy aValidationPolicy,
                                                ECertificate aCertificate,
                                                List<ECertificate> aValidCertificates,
                                                List<ECertificate> aUserInitialCertList,
                                                DateTime? aBaseValidationTime,
                                                DateTime? aLastRevocationTime,
            bool aDoNotUsePastRevocationInfo)
        {
            ValidationSystem validationSystem = createValidationSystem(aValidationPolicy);
            validationSystem.setValidCertificateSet(aValidCertificates);
            validationSystem.setUserInitialCertificateSet(aUserInitialCertList);
            validationSystem.setBaseValidationTime(aBaseValidationTime);
            validationSystem.setLastRevocationTime(aLastRevocationTime);
            //return validateCertificate(validationSystem, aCertificate);
            return validateCertificate(validationSystem, aCertificate, aDoNotUsePastRevocationInfo);
        }


        /**
         * Validate certificate
         *
         * @param aValidationSystem that will be used for validation
         * @param aCertificate      for validation
         * @return validation result for certificate
         * @throws ESYAException if anything goes wrong
         */
        public static CertificateStatusInfo validateCertificate(ValidationSystem aValidationSystem, ECertificate aCertificate)
        {
            return validateCertificate(aValidationSystem, aCertificate, false);
        }

        /**
         * Validate certificate
         *
         * @param aValidationSystem that will be used for validation
         * @param aCertificate      for validation
         * @return validation result for certificate
         * @throws ESYAException if anything goes wrong
         */
        public static CertificateStatusInfo validateCertificate(ValidationSystem aValidationSystem,
                ECertificate aCertificate,
                bool aDoNotUsePastRevocationInfo)
        {
            Chronometer c = new Chronometer("Validate Certificate");
            c.start();
            checkLicense(aCertificate);
            CertificateController controller = new CertificateController();
            controller.setDoNotUsePastRevocationInfo(aDoNotUsePastRevocationInfo);
            CertificateStatusInfo csi = controller.check(aValidationSystem, aCertificate);
            logger.Info(c.stopSingleRun());
            return csi;
        }


        /**
        * validate CRL according to policy
        *
        * @param aPolicyFile validation policy file 
        * @param aCRL to be validated
        * @return CRL validation result
        * @throws ESYAException if anything goes wrong
        */
        public static CRLStatusInfo validateCRL(String aPolicyFile, ECRL aCRL)
        {
            ValidationPolicy policy = PolicyReader.readValidationPolicy(aPolicyFile);
            return validateCRL(policy, aCRL);
        }


        /**
         * validate CRL according to policy
         *
         * @param aValidationPolicy policy for validation
         * @param aCRL to be validated
         * @return CRL validation result
         * @throws ESYAException if anything goes wrong
         */
        public static CRLStatusInfo validateCRL(ValidationPolicy aValidationPolicy, ECRL aCRL)
        {
            ValidationSystem ds = createValidationSystem(aValidationPolicy);
            return validateCRL(ds, aCRL);
        }


        /**
         * validate CRL according to validation system
         *
         * @param aValidationSystem validation system for validation
         * @param aCRL to be validated
         * @return CRL validation result
         * @throws ESYAException if anything goes wrong
         */
        public static CRLStatusInfo validateCRL(ValidationSystem aValidationSystem, ECRL aCRL)
        {
            CRLController kontrolcu = new CRLController();
            return kontrolcu.check(aValidationSystem, aCRL);
        }

    }
}
