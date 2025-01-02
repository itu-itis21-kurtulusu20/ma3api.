package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateSelfChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer.CRLIssuerChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self.CRLSelfChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl.DeltaCRLChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.FindingPolicy;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.CertificateFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross.CrossCertificateFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted.TrustedCertificateFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.CRLFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta.DeltaCRLFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp.OCSPResponseFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate.CertificateMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl.CRLMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross.CrossCertificateMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl.DeltaCRLMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp.OCSPResponseMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.MatchingPolicy;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyClassInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.RevocationPolicyClassInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.CRLSaver;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.CertificateSaver;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.OCSPResponseSaver;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;
import tr.gov.tubitak.uekae.esya.api.common.util.VersionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 private static final Logger logger = LoggerFactory.getLogger(CertificateValidation.class);

	 static {
         String apiVersion = VersionUtil.getAPIVersion();
         logger.debug("MA3 API version: " + apiVersion);
     }

    /**
     * BulmaParam objesinin içine Guvenilir Sertifika Buluculari Ekler
     */
    static void createTrustedCertificateFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> trustedCertificateFinders = aFindingPolicy.getTrustedCertificateFinders();

        for (PolicyClassInfo policyClassInfo : trustedCertificateFinders) {
            try {
                TrustedCertificateFinder finder = (TrustedCertificateFinder) Class.forName(policyClassInfo.getClassName()).newInstance();
                finder.setParameters(policyClassInfo.getParameters());
                finder.setParentSystem(aValidationSystem);
                aFindSystem.addTrustedCertificateFinder(finder);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }

    /**
     * BulmaParam objesinin içine Sertifika Buluculari Ekler
     */
    static void createCertificateFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> sertifikaBulucular = aFindingPolicy.getCertificateFinders();

        for (PolicyClassInfo classInfo : sertifikaBulucular) {
            try {
                CertificateFinder bulucu = (CertificateFinder) Class.forName(classInfo.getClassName()).newInstance();
                bulucu.setParameters(classInfo.getParameters());
                bulucu.setParentSystem(aValidationSystem);
                aFindSystem.addCertificateFinder(bulucu);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }

    /**
     * BulmaParam objesinin içine SİL Buluculari Ekler
     */
    static void createCRLFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> crlFinders = aFindingPolicy.getCRLFinders();
        for (PolicyClassInfo classInfo : crlFinders) {
            try {
                CRLFinder crlFinder = (CRLFinder) Class.forName(classInfo.getClassName()).newInstance();
                crlFinder.setParameters(classInfo.getParameters());
                crlFinder.setParentSystem(aValidationSystem);
                aFindSystem.addCRLFinder(crlFinder);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }

    }

    /**
     * BulmaParam objesinin içine delta-SİL Buluculari Ekler
     */
    static void createDeltaCRLFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> deltaCRLFinders = aFindingPolicy.getDeltaCRLFinders();
        for (PolicyClassInfo classInfo : deltaCRLFinders) {
            try {
                DeltaCRLFinder deltaCRLFinder = (DeltaCRLFinder) Class.forName(classInfo.getClassName()).newInstance();
                deltaCRLFinder.setParameters(classInfo.getParameters());
                deltaCRLFinder.setParentSystem(aValidationSystem);
                aFindSystem.addDeltaCRLFinder(deltaCRLFinder);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }

    /**
     * BulmaParam objesinin içine çapraz Sertifika Buluculari Ekler
     */
    static void createCrossCertificateFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> crossCertificateFinders = aFindingPolicy.getCrossCertificateFinders();
        for (PolicyClassInfo sinif : crossCertificateFinders) {
            try {
                CrossCertificateFinder finder = (CrossCertificateFinder) Class.forName(sinif.getClassName()).newInstance();
                finder.setParameters(sinif.getParameters());
                finder.setParentSystem(aValidationSystem);
                aFindSystem.addCrossCertificateFinder(finder);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }


    /**
     * BulmaParam objesinin içine OCSP Cevabı Buluculari ekler
     */
    static void createOCSPResponseFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> ocspResponseFinders = aFindingPolicy.getOCSPResponseFinders();
        for (PolicyClassInfo classInfo : ocspResponseFinders) {
            try {
                OCSPResponseFinder finder = (OCSPResponseFinder) Class.forName(classInfo.getClassName()).newInstance();
                finder.setParameters(classInfo.getParameters());
                finder.setParentSystem(aValidationSystem);
                aFindSystem.addOCSPResponseFinder(finder);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }

    }

    /**
     * Bulma objesinin içine Buluculari Ekler
     */
    static void createFinders(FindingPolicy aFindingPolicy, FindSystem aFindSystem, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        createTrustedCertificateFinders(aFindingPolicy, aFindSystem, aValidationSystem);
        createCertificateFinders(aFindingPolicy, aFindSystem, aValidationSystem);
        createCRLFinders(aFindingPolicy, aFindSystem, aValidationSystem);
        createDeltaCRLFinders(aFindingPolicy, aFindSystem, aValidationSystem);
        createOCSPResponseFinders(aFindingPolicy, aFindSystem, aValidationSystem);
        createCrossCertificateFinders(aFindingPolicy, aFindSystem, aValidationSystem);
    }


    /**
     * EslestirmeParam objesinin içine Sertifika Eslestiricileri Ekler
     */
    static void createCertificateMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> certificateMatchers = aMatchingPolicy.getCertificateMatchers();
        for (PolicyClassInfo classInfo : certificateMatchers) {
            try {
                CertificateMatcher certificateMatcher = (CertificateMatcher) Class.forName(classInfo.getClassName()).newInstance();
                //certificateMatcher.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                aMatchSystem.addCertificateMatcher(certificateMatcher);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }


    /**
     * EslestirmeParam objesinin içine SİL Eslestiricileri Ekler
     */
    static void createCRLMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> crlMatchers = aMatchingPolicy.getCRLMatchers();
        for (PolicyClassInfo classInfo : crlMatchers) {
            try {
                CRLMatcher crlMatcher = (CRLMatcher) Class.forName(classInfo.getClassName()).newInstance();
                //crlMatcher.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                aMatchSystem.addCRLMatcher(crlMatcher);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }

    /**
     * EslestirmeParam objesinin içine delta-SİL Eslestiricileri Ekler
     */
    static void createDeltaCRLMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> deltaCRLMatchers = aMatchingPolicy.getDeltaCRLMatchers();
        for (PolicyClassInfo classInfo : deltaCRLMatchers) {
            try {
                DeltaCRLMatcher deltaCRLMatcher = (DeltaCRLMatcher) Class.forName(classInfo.getClassName()).newInstance();
                //eslestiriciClass.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                aMatchSystem.addDeltaCRLMatcher(deltaCRLMatcher);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }

    /**
     * EslestirmeParam objesinin içine OCSP rresponse Eslestiricileri Ekler
     */
    static void createOCSPResponseMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> ocspMatchers = aMatchingPolicy.getOCSPResponseMatchers();
        for (PolicyClassInfo classInfo : ocspMatchers) {
            try {
                OCSPResponseMatcher ocspResponseMatcher = (OCSPResponseMatcher) Class.forName(classInfo.getClassName()).newInstance();
                aMatchSystem.addOCSPResponseMatcher(ocspResponseMatcher);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }

    /**
     * EslestirmeParam objesinin içine Çapraz Sertifika Eslestiricileri Ekler
     */
    static void createCrosscertificateMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
            throws ESYAException
    {
        List<PolicyClassInfo> crossCertificateMatchers = aMatchingPolicy.getCrossCertificateMatchers();
        for (PolicyClassInfo aCaprazSertifikaEslestiriciler : crossCertificateMatchers) {
            try {
                CrossCertificateMatcher crossCertificateMatcher = (CrossCertificateMatcher)
                        Class.forName(aCaprazSertifikaEslestiriciler.getClassName()).newInstance();
                //eslestiriciClass.eslestiriciParamBelirle(eslestirici.parametreleriAl());
                aMatchSystem.addCrossCertificateMatcher(crossCertificateMatcher);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }

    }

    /**
     * EslestirmeParam objesinin içine Eslestiricileri Ekler
     */
    static void createMatchers(MatchingPolicy aMatchingPolicy, MatchSystem aMatchSystem)
            throws ESYAException
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
    static void createCertificateSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem) throws ESYAException
    {
    	SaveSystem ss = oValidationSystem.getSaveSystem();
    	List<PolicyClassInfo> certSaversInfo = aValidationPolicy.bulmaPolitikasiAl().getSavePolicy().getCertificateSavers();
    	for (PolicyClassInfo certSaverInfo : certSaversInfo) 
    	{
    		try
    		{
    			CertificateSaver saver = (CertificateSaver)Class.forName(certSaverInfo.getClassName()).newInstance();
    			saver.setParameters(certSaverInfo.getParameters());
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
    static void createCRLSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem) throws ESYAException
    {
    	SaveSystem ss = oValidationSystem.getSaveSystem();
    	List<PolicyClassInfo> crlSaversInfo = aValidationPolicy.bulmaPolitikasiAl().getSavePolicy().getCRLSavers();
    	for (PolicyClassInfo crlSaverInfo : crlSaversInfo) 
    	{
    		try
    		{
    			CRLSaver saver = (CRLSaver)Class.forName(crlSaverInfo.getClassName()).newInstance();
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
    static void createOCSPResponseSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem) throws ESYAException
    {
    	SaveSystem ss = oValidationSystem.getSaveSystem();
    	List<PolicyClassInfo> ocspSaversInfo = aValidationPolicy.bulmaPolitikasiAl().getSavePolicy().getOCSPResponseSavers();
    	for (PolicyClassInfo ocspSaverInfo : ocspSaversInfo) 
    	{
    		try
    		{
    			OCSPResponseSaver saver = (OCSPResponseSaver)Class.forName(ocspSaverInfo.getClassName()).newInstance();
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
    static void createSavers(ValidationPolicy aValidationPolicy, ValidationSystem oValidationSystem) throws ESYAException
    {
    	createCertificateSavers(aValidationPolicy, oValidationSystem);
    	createCRLSavers(aValidationPolicy, oValidationSystem);
    	createOCSPResponseSavers(aValidationPolicy, oValidationSystem);
    }

    /**
     * DogrulamaParam objesinin icine SM Sertifika Kontrolculeri Ekler
     */
    static void createIssuerCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem checkSystem = aValidationSystem.getCheckSystem();
        List<PolicyClassInfo> ustSMKontrolcular = aValidationPolicy.getCertificateIssuerCheckers();
        for (PolicyClassInfo anUstSMKontrolcular : ustSMKontrolcular) {
            try {
                IssuerChecker kontrolcuClass = (IssuerChecker)
                        Class.forName(anUstSMKontrolcular.getClassName()).newInstance();
                kontrolcuClass.setCheckParameters(anUstSMKontrolcular.getParameters());
                kontrolcuClass.setParentSystem(aValidationSystem);
                checkSystem.addIssuerChecker(kontrolcuClass);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }

    /**
     * DogrulamaParam objesinin içine Güvenilir Sertifika Kontrolculeri Ekler
     */
    static void createTrustedCertificateCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem ks = aValidationSystem.getCheckSystem();
        List<PolicyClassInfo>
                gsKontrolcular = aValidationPolicy.getTrustedCertificateCheckers();
        for (PolicyClassInfo aGsKontrolcular : gsKontrolcular) {
            try {
                CertificateSelfChecker kontrolcuClass = (CertificateSelfChecker) Class.forName(aGsKontrolcular.getClassName()).newInstance();
                kontrolcuClass.setCheckParameters(aGsKontrolcular.getParameters());
                kontrolcuClass.setParentSystem(aValidationSystem);
                ks.addTrustedCertificateChecker(kontrolcuClass);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }


    /**
     * DogrulamaParam objesinin icine Tek Sertifika Kontrolculeri Ekler
     */
    static void createCertificateSelfCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem ks = aValidationSystem.getCheckSystem();
        List<PolicyClassInfo>
                tekSerKontrolcular = aValidationPolicy.getCertificateSelfCheckers();
        for (PolicyClassInfo aTekSerKontrolcular : tekSerKontrolcular) {
            try {
                CertificateSelfChecker kontrolcuClass = (CertificateSelfChecker) Class.forName(aTekSerKontrolcular.getClassName()).newInstance();
                kontrolcuClass.setCheckParameters(aTekSerKontrolcular.getParameters());
                kontrolcuClass.setParentSystem(aValidationSystem);
                ks.addCertificateSelfChecker(kontrolcuClass);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }

    /**
     * DogrulamaParam objesinin içine iptal Kontrolcuları Ekler
     */
    static void createRevocationCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem ks = aValidationSystem.getCheckSystem();
        List<RevocationPolicyClassInfo> iptalKontrolcular = aValidationPolicy.getRevocationCheckers();
        
        for (int i=0; i<iptalKontrolcular.size();  i++)
        {
             PolicyClassInfo iptalKontrolcu = iptalKontrolcular.get(i);
            try {
                RevocationChecker kontrolcuClass = (RevocationChecker) Class.forName(iptalKontrolcu.getClassName()).newInstance();
                kontrolcuClass.setCheckParameters(iptalKontrolcu.getParameters());
                kontrolcuClass.setParentSystem(aValidationSystem);

                List<PolicyClassInfo> finders = iptalKontrolcular.get(i).getFinders();
                for (int ifk =0; ifk< finders.size();ifk++)
                {
                    Finder finder= (Finder) Class.forName(finders.get(ifk).getClassName()).newInstance();
                    finder.setParameters(finders.get(ifk).getParameters());
                    finder.setParentSystem(aValidationSystem);
                    kontrolcuClass.addFinder(finder);
                }

                ks.addRevocationChecker(kontrolcuClass);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }

    /**
     * DogrulamaParam objesinin içine Tek SİL Kontrolcuları Ekler
     */
    static void createCRLSelfCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem ks = aValidationSystem.getCheckSystem();
        List<PolicyClassInfo>
                silKontrolcular = aValidationPolicy.getCRLSelfCheckers();
        for (PolicyClassInfo aSilKontrolcular : silKontrolcular) {
            try {
                CRLSelfChecker kontrolcuClass = (CRLSelfChecker) Class.forName(aSilKontrolcular.getClassName()).newInstance();
                kontrolcuClass.setCheckParameters(aSilKontrolcular.getParameters());
                kontrolcuClass.setParentSystem(aValidationSystem);
                ks.addCRLSelfChecker(kontrolcuClass);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }


    /**
     * DogrulamaParam objesinin içine SİL-SM  Kontrolculeri Ekler
     */
    static void createCRLCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem ks = aValidationSystem.getCheckSystem();
        List<PolicyClassInfo>
                silSMKontrolcular = aValidationPolicy.getCRLIssuerCheckers();
        for (PolicyClassInfo aSilSMKontrolcular : silSMKontrolcular) {
            try {
                CRLIssuerChecker kontrolcuClass = (CRLIssuerChecker) Class.forName(aSilSMKontrolcular.getClassName()).newInstance();
                kontrolcuClass.setCheckParameters(aSilSMKontrolcular.getParameters());
                kontrolcuClass.setParentSystem(aValidationSystem);
                ks.addCRLIssuerChecker(kontrolcuClass);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }
    }


    /**
     * DogrulamaParam objesinin içine delta-SİL Kontrolcüleri Ekler
     */
    static void createDeltaCRLCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem ks = aValidationSystem.getCheckSystem();
        List<PolicyClassInfo>
                deltaCRLCheckers = aValidationPolicy.getDeltaCRLCheckers();
        for (PolicyClassInfo aDeltaCRLcheckerInfo : deltaCRLCheckers) {
            try {
                DeltaCRLChecker deltaCRLChecker = (DeltaCRLChecker) Class.forName(aDeltaCRLcheckerInfo.getClassName()).newInstance();
                deltaCRLChecker.setCheckParameters(aDeltaCRLcheckerInfo.getParameters());
                deltaCRLChecker.setParentSystem(aValidationSystem);
                ks.addDeltaCRLChecker(deltaCRLChecker);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }

    /**
     * DogrulamaParam objesinin icine OCSP Kontrolculeri Ekler
     */
    static void createOCSPResponseCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        CheckSystem ks = aValidationSystem.getCheckSystem();
        List<PolicyClassInfo>
                ocspKontrolcular = aValidationPolicy.getOCSPResponseCheckers();
        for (PolicyClassInfo anOcspKontrolcular : ocspKontrolcular) {
            try {
                OCSPResponseChecker kontrolcuClass = (OCSPResponseChecker)
                        Class.forName(anOcspKontrolcular.getClassName()).newInstance();
                kontrolcuClass.setCheckParameters(anOcspKontrolcular.getParameters());
                kontrolcuClass.setParentSystem(aValidationSystem);
                ks.addOCSPResponseChecker(kontrolcuClass);
            }
            catch (Exception x) {
                throw new ESYAException(x);
            }
        }


    }

    /**
     * DogrulamaParam objesinin içine Kontrolcuları Ekler
     */
    static void createCheckers(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
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
     * ValidationSystem objesinin içindeki parametreleri belirler
     */
    static void initParameters(ValidationPolicy aValidationPolicy, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        aValidationSystem.setUserInitialPolicySet(aValidationPolicy.getUserInitialPolicySet());
        aValidationSystem.setInitialExplicitPolicy(aValidationPolicy.isInitialExplicitPolicy());
        aValidationSystem.setInitialPolicyMappingInhibit(aValidationPolicy.isInitialPolicyMappingInhibit());
        aValidationSystem.setInitialAnyPolicyInhibit(aValidationPolicy.isInitialAnyPolicyInhibit());
        aValidationSystem.setDefaultStorePath(aValidationPolicy.getDefaultStorePath());
    }

    /**
     * Create validation system from policy
     *
     * @param aValidationPolicy policy for
     * @return a validation system
     * @throws ESYAException if anything goes wrong
     */
    public static ValidationSystem createValidationSystem(ValidationPolicy aValidationPolicy)
            throws ESYAException
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
        // dogrulama politikamızdan kontrolculeri alıp kaydetme parametresini oluşturalım
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
    public static Map<ECertificate, CertificateStatusInfo> validateCertificates(
                                String aPolicyFile,
                                List<ECertificate> aCertificates,
                                List<ECertificate> aValidCertificates,
                                List<ECertificate> aUserInitialCertList)
            throws ESYAException
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
    public static Map<ECertificate, CertificateStatusInfo> validateCertificates(
                ValidationPolicy aValidationPolicy,
                List<ECertificate> aCertificates,
                List<ECertificate> aValidCertificates,
                List<ECertificate> aUserInitialCertList) throws ESYAException
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
    public static Map<ECertificate, CertificateStatusInfo> validateCertificates(
                            ValidationSystem aValidationSystem,
                            List<ECertificate> aCertificates,
                            boolean aDoNotUsePastRevocationInfo)
            throws ESYAException
    {
    	
        Map<ECertificate, CertificateStatusInfo> sdbList = new HashMap<ECertificate, CertificateStatusInfo>();

        for (ECertificate certificate : aCertificates) {
            CertificateStatusInfo csi = validateCertificate(aValidationSystem, certificate, aDoNotUsePastRevocationInfo);
            if (!sdbList.containsKey(certificate))
                sdbList.put(certificate, csi);
        }

        return sdbList;
    }

    private static void checkLicense(ECertificate aCer)
    {
    	try
    	{
    		boolean isTest = LV.getInstance().isTL(Urunler.SERTIFIKADOGRULAMA);
    		if(isTest && !aCer.isOCSPSigningCertificate() && !aCer.isTimeStampingCertificate() 
    				&& !aCer.isCACertificate())
    		{
    			if(!aCer.getSubject().getCommonNameAttribute().toLowerCase().contains("test"))
    			{
    				throw new ESYARuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
    			}
    		}
    	}
    	catch(LE e)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
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
            throws ESYAException
    {
        ValidationPolicy policy = PolicyReader.readValidationPolicy(aPolicyFile);
        return validateCertificate(policy, aCertificate, aValidCertificates, aUserInitialCertList);
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
//    public static CertificateStatusInfo validateCertificateInPast(
//                                            String aPolicyFile,
//                                            ECertificate aCertificate,
//                                            List<ECertificate> aValidCertificates,
//                                            List<ECertificate> aUserInitialCertList,
//                                            Calendar aBaseValidationTime,
//                                            Calendar aLastRevocationTime,
//                                            boolean aDoNotUsePastRevocationInfo)
//            throws ESYAException
//    {
//        ValidationPolicy policy = PolicyReader.readValidationPolicy(aPolicyFile);
//        return validateCertificateInPast(policy, aCertificate, aValidCertificates, aUserInitialCertList, aBaseValidationTime, aLastRevocationTime, aDoNotUsePastRevocationInfo);
//    }



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
            throws ESYAException
    {
        ValidationSystem validationSystem = createValidationSystem(aValidationPolicy);
        validationSystem.setValidCertificateSet(aValidCertificates);
        validationSystem.setUserInitialCertificateSet(aUserInitialCertList);

        return validateCertificate(validationSystem, aCertificate, false);
    }

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
//    public static CertificateStatusInfo validateCertificateInPast(
//                                            ValidationPolicy aValidationPolicy,
//                                            ECertificate aCertificate,
//                                            List<ECertificate> aValidCertificates,
//                                            List<ECertificate> aUserInitialCertList,
//                                            Calendar aBaseValidationTime,
//                                            Calendar aLastRevocationTime,
//                                            boolean aDoNotUsePastRevocationInfo)
//            throws ESYAException
//    {
//        ValidationSystem validationSystem = createValidationSystem(aValidationPolicy);
//        validationSystem.setValidCertificateSet(aValidCertificates);
//        validationSystem.setUserInitialCertificateSet(aUserInitialCertList);
//        validationSystem.setBaseValidationTime(aBaseValidationTime);
//        validationSystem.setLastRevocationTime(aLastRevocationTime);
//        return validateCertificate(validationSystem, aCertificate, aDoNotUsePastRevocationInfo);
//    }


    /**
     * Validate certificate
     *
     * @param aValidationSystem that will be used for validation
     * @param aCertificate      for validation
     * @return validation result for certificate
     * @throws ESYAException if anything goes wrong
     */
    public static CertificateStatusInfo validateCertificate(ValidationSystem aValidationSystem, ECertificate aCertificate)
            throws ESYAException
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
    		boolean aDoNotUsePastRevocationInfo)
            throws ESYAException
    {
        Chronometer c = new Chronometer("Validate certificate");
        c.start();
    	checkLicense(aCertificate);
        CertificateController controller = new CertificateController();
        controller.setDoNotUsePastRevocationInfo(aDoNotUsePastRevocationInfo);
        CertificateStatusInfo csi = controller.check(aValidationSystem, aCertificate);

        logger.info(c.stopSingleRun());
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
            throws ESYAException
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
            throws ESYAException
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
            throws ESYAException
    {
        CRLController kontrolcu = new CRLController();
        return kontrolcu.check(aValidationSystem, aCRL);
    }

    /*
    void testFonksiyonu()
    {
     CertificateFinderFromFile pDSB = new CertificateFinderFromFile();
     Bulucu pBulucu = pDSB;
     Bulucu.freeBulucu(pDSB);
    }       */
}

