package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ECertID;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponseData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl.DeltaCRLController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Find;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.CertificateFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross.CrossCertificateFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted.TrustedCertificateFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.CRLFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta.DeltaCRLFinder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp.OCSPResponseFinder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Container class for the Finder objects specified by FindingPolicy
 *
 * @author IH
 */
public class FindSystem
{
    private static Logger logger = LoggerFactory.getLogger(FindSystem.class);

    private static final List<ECertificate> EMPTY_CERTIFICATE_LIST = new ArrayList<ECertificate>(0);
    private static final List<ECRL> EMPTY_CRL_LIST = new ArrayList<ECRL>(0);

    private List<ECertificate> mTrustedCertificates = new ArrayList<ECertificate>(0);
    private boolean mTrustedCertificatesFound;

    // sertifika bulucu
    private List<CertificateFinder> mCertificateFinders = new ArrayList<CertificateFinder>(0);
    // sil bulucu
    private List<CRLFinder> mCRLFinders = new ArrayList<CRLFinder>(0);
    // OCSP cevabı bulucu
    private List<OCSPResponseFinder> mOCSPResponseFinders = new ArrayList<OCSPResponseFinder>(0);
    // guvenilir sertifika buluc
    private List<TrustedCertificateFinder> mTrustedCertificateFinders = new ArrayList<TrustedCertificateFinder>();
    // Çapraz sertifika bulucu
    private List<CrossCertificateFinder> mCrossCertificateFinders = new ArrayList<CrossCertificateFinder>(0);
    // delta sil bulucu
    private List<DeltaCRLFinder> mDeltaCRLFinders = new ArrayList<DeltaCRLFinder>(0);


    public FindSystem()
    {
    	try
    	{
    		LV.getInstance().checkLD(Urunler.SERTIFIKADOGRULAMA);
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
    }
    
    
    public List<ECertificate> getTrustedCertificates()
    {
        return mTrustedCertificates;
    }

    public void findTrustedCertificates()
    {
        if (!mTrustedCertificatesFound) {
            for (TrustedCertificateFinder gsBulucu : mTrustedCertificateFinders) {
                //if (gsBulucu)
                // Guvenilir Sertifika Bulucu Eklenmiş mi ?
                mTrustedCertificates.addAll(gsBulucu.findTrustedCertificate());
            }
            mTrustedCertificatesFound = true;
        }
    }

    public void addTrustedCertificate(ECertificate aCertificate)
    {
        if (mTrustedCertificates.indexOf(aCertificate) < 0)
            mTrustedCertificates.add(aCertificate);
    }

    public boolean isTrustedCertificate(ECertificate aCertificate)
    {
        for (ECertificate cert : mTrustedCertificates) {
            if (aCertificate.equals(cert)) {
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
        mCertificateFinders.add(aCertificateFinder);
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
        mTrustedCertificateFinders.add(aTrustedCertificateFinder);
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
        mCrossCertificateFinders.add(aCrossCertificateFinder);
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
        mCRLFinders.add(aCRLFinder);
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
        mOCSPResponseFinders.add(aOCSPResponseFinder);
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
        mDeltaCRLFinders.add(aDeltaCRLFinder);
    }

    public List<ECertificate> findTrustedCertificates(ValidationSystem aValidationSystem)
    {
        if (mTrustedCertificateFinders.isEmpty())
            return EMPTY_CERTIFICATE_LIST;

        List<ECertificate> gsList = new ArrayList<ECertificate>(0);

        for (TrustedCertificateFinder gsb : mTrustedCertificateFinders) {
            gsList.addAll(gsb.findTrustedCertificate());
        }

        return gsList;
    }

    public ECertificate findIssuerCertificate(ValidationSystem aValidationSystem,
                                              CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        ECertificate cert = aCertStatusInfo.getCertificate();
        MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        String subject = cert.getSubject().stringValue();

        if (logger.isDebugEnabled())
            logger.debug(subject + " sertifika SM sertifikası bulunacak");

        ECertificate certificate = null;

        // Kontrol Edilmiş Sertifikalar arasında mı kontrol edelim
        List<ECertificate> certList = aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
        certificate = Find.fromList(matchSystem, certList, cert);
        if (certificate != null) {
            CertificateStatusInfo csi = aValidationSystem.getCertificateValidationCache().getCheckResult(certificate);
            aCertStatusInfo.setSigningCertficateInfo(csi.clone());
            if (csi.getCertificateStatus() == CertificateStatus.VALID)
                return certificate;
            else return null;
        }

        // Güvenilir Sertifikalar arasında mı kontrol edelim
        List<ECertificate> trustedCertificates = getTrustedCertificates();
        certificate = Find.fromList(matchSystem, trustedCertificates, cert);
        if (certificate != null) {
            CertificateController certificateController = new CertificateController();
            CertificateStatusInfo csi = certificateController.check(aValidationSystem, certificate);
            aCertStatusInfo.setSigningCertficateInfo(csi);
            if (csi.getCertificateStatus() == CertificateStatus.VALID)
                return certificate;
            else return null;
        }


        for (CertificateFinder certificateFinder : mCertificateFinders) {
            certList = certificateFinder.findCertificate(aCertStatusInfo.getCertificate());
            certificate = Find.fromList(matchSystem, certList, cert);

            if ((certificate == null) ||        // Sertifika bulunmuş mu?
                    (certificateFinder.isToBeChecked()
                            && !_checkIssuer(aValidationSystem, aCertStatusInfo, certificate)))    // geçerli mi?
            {
                certificate = null;
            }
            else {
                if (certificateFinder.isToBeChecked())
                    Find.saveCertificate(aValidationSystem.getSaveSystem(), certificate);

                return certificate; // geçerli sertifika bulundu geri dönülecek.
            }
        }

        return certificate;

    }


    /*
    public List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria)
           throws ESYAException
    {
        List<ECertificate> certList = new ArrayList<ECertificate>();

        for (CertificateFinder certificateFinder : mCertificateFinders) {
            certList = certificateFinder.searchCertificates(aCriteria);

            if (certList != null && certList.size() > 0) // Sertifika bulunmuş mu?
            {
                return certList;
            }
        }

        for (CrossCertificateFinder certificateFinder : mCrossCertificateFinders) {
            List<ECertificate> crossCerts = certificateFinder.findCrossCertificate();
            for (ECertificate crossCert : crossCerts) {
                if (mMatcher.match(aCriteria, crossCert)) {
                    if (certList == null)
                        certList = new ArrayList<ECertificate>();
                    certList.add(crossCert);
                }
            }
            if (certList != null && certList.size() > 0) // Sertifika bulunmuş mu?
            {
                return certList;
            }

        }

        for (TrustedCertificateFinder certificateFinder : mTrustedCertificateFinders) {
            List<ECertificate> trustedCerts = certificateFinder.findTrustedCertificate();
            for (ECertificate trustedCert : trustedCerts) {
                if (mMatcher.match(aCriteria, trustedCert)) {
                    if (certList == null)
                        certList = new ArrayList<ECertificate>();
                    certList.add(trustedCert);
                }
            }
            if (certList != null && certList.size() > 0) // Sertifika bulunmuş mu?
            {
                return certList;
            }

        }
        return certList;
    }
    */

    public ECertificate findCRLIssuerCertificate(ValidationSystem aValidationSystem,
                                                 CRLStatusInfo aCRLStatusInfo)
            throws ESYAException
    {
        /*if (mCertificateFinders.isEmpty())
            return null;*/
        ECertificate certificate = null;
        MatchSystem matchSystem = aValidationSystem.getMatchSystem();
        FindSystem findSystem = aValidationSystem.getFindSystem();

        ECRL crl = aCRLStatusInfo.getCRL();

        // Kontrol Edilmiş Sertifikalar arasında mı kontrol edelim
        List<ECertificate> certList = aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
        certificate = Find.issuerCertificateFromList(matchSystem, certList, crl);
        if (certificate != null) {
            CertificateStatusInfo csi = aValidationSystem.getCertificateValidationCache().getCheckResult(certificate);
            aCRLStatusInfo.setSigningCertficateInfo(csi.clone());
            if (csi.getCertificateStatus() == CertificateStatus.VALID)
                return certificate;
            else return null;
        }

        // Güvenilir Sertifikalar arasında mı kontrol edelim
        List<ECertificate> trustedCertificates = findSystem.getTrustedCertificates();
        certificate = Find.issuerCertificateFromList(matchSystem, trustedCertificates, crl);
        if (certificate != null) {
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
            certificate = Find.issuerCertificateFromList(matchSystem, userInitialCertList, crl);

        if (certificate!=null && _checkIssuer(aValidationSystem,aCRLStatusInfo, certificate))
        {
            return certificate;
        }
        certificate = null;

        // CA Cache e bakalım.
        List<ECertificate> cachedCACerts = aValidationSystem.getCachedCACertificates(crl.getIssuer().stringValue());
        certificate  = Find.issuerCertificateFromList(matchSystem,cachedCACerts,crl);
        if ((certificate!=null) && _checkIssuer(aValidationSystem,aCRLStatusInfo, certificate))
        {
                return certificate;
        }

        certificate = null;

        // todo AIA varsa oradan arasın, yoksa bir kritere göre arasın 
        // Hala bulamadıysak bulucular arasın
        for(CertificateFinder pSB : mCertificateFinders)
        {
            certList	= pSB.findCertificate();
            certificate	= Find.issuerCertificateFromList(matchSystem,certList,crl);
            if(	( certificate == null)	||									// Sertifika bulunmuş mu?
                ( !_checkIssuer(aValidationSystem, aCRLStatusInfo, certificate))		)	// geçerli mi?
            {
                certificate = null;
            }
            else // Bulduk ve doğruladık
            {
                break;
            }
        }

        if (certificate != null) {
            Find.saveCertificate(aValidationSystem.getSaveSystem(), certificate);
        }

        return certificate;
    }

    public EOCSPResponse findOCSPResponseFromInitial(ValidationSystem aValidationSystem, ECertificate aIssuerCertificate, CertificateStatusInfo aCertStatusInfo)
    {
        EOCSPResponse ocspResponse;

        ECertificate certificate = aCertStatusInfo.getCertificate();

        List<EOCSPResponse> initial = aValidationSystem.getUserInitialOCSPResponseSet();
        if ((initial!=null) && (initial.size()>0)){
            logger.debug("Initial ocsp set size: "+initial.size());
            for (int i = 0; i < initial.size(); i++) {
                EOCSPResponse response = initial.get(i);

                EResponseData rd = response.getBasicOCSPResponse().getTbsResponseData();
                for (int j=0; j<rd.getSingleResponseCount(); j++){
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

        //System.out.println("Ocsp response finders: "+mOCSPResponseFinders.size());
        for (OCSPResponseFinder ocspResponseFinder : mOCSPResponseFinders) {
            logger.debug("Ocsp Response finder : "+ocspResponseFinder);
            ocspResponse = ocspResponseFinder.findOCSPResponse(aCertStatusInfo.getCertificate(), aIssuerCertificate);
            if (ocspResponse != null)
                return ocspResponse;
        }
        return null;
    }

    private boolean _matches(ValidationSystem aVS, ECertID aCertId, ECertificate aCertificate)
    {
        if (!aCertId.getSerialNumber().equals(aCertificate.getSerialNumber()))
            return false;

        try {
            DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(aCertId.getHashAlgorithm());
            byte[] issuerName = aCertificate.getIssuer().getEncoded();
            byte[] issuerNameHash = DigestUtil.digest(digestAlg, issuerName);
            
            if (!Arrays.equals(aCertId.getIssuerNameHash(), issuerNameHash))
                return false;

            List<ECertificate> issuers = findIssuerCertificate(aVS, aCertificate, true);
            ECertificate issuer = issuers.get(0);
            byte[] issuerKey = issuer.getSubjectPublicKeyInfo().getSubjectPublicKey();
            byte[] issuerKeyHash = DigestUtil.digest(digestAlg, issuerKey);

            if (!Arrays.equals(aCertId.getIssuerKeyHash(), issuerKeyHash))
                return false;

        } catch (Exception x){
            logger.error("Error in FindSystem", x);
            return false;
        }
        return true;
    }

    public List<ECRL> findCRL(ValidationSystem aValidationSystem, CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        List<ECRL> crlList = new ArrayList<ECRL>();

        /*if (mCRLFinders.isEmpty())
            return crlList;*/

        MatchSystem matchSystem = aValidationSystem.getMatchSystem();
        ECertificate certificate = aCertStatusInfo.getCertificate();
        ECRL pSIL = null;

        String subject = certificate.getSubject().stringValue();
        if (logger.isDebugEnabled())
            logger.debug(subject + " sertifika sili bulunacak");


        List<ECRL> matchingCRLList = new ArrayList<ECRL>();

        logger.debug("CRL Count: " + aValidationSystem.getUserInitialCRLSet().size());
        
        // Kullanıcı tanımlı sillerden eslesenleri alalım
        matchingCRLList = Find.crlsFromList(matchSystem, aValidationSystem.getUserInitialCRLSet(), matchingCRLList, certificate);
        
        logger.debug("Matching CRL Count:" + matchingCRLList.size());

        // Bulucularımız silleri bulsun
        for (CRLFinder pSB : mCRLFinders) {
            List<ECRL> altCRLList = pSB.findCRL(certificate);
            matchingCRLList = Find.crlsFromList(matchSystem, altCRLList, matchingCRLList, certificate);
        }

        // Bulduklarımızı kontrol edelim
        for (int i = 0; i < matchingCRLList.size(); i++) 
        {
            ECRL sil = matchingCRLList.get(i);
            if ((checkCRL(aValidationSystem, aCertStatusInfo, sil)))    // Geçerli Mi?
            	crlList.add(sil);
        }
        return crlList;
    }

    //public List<ECRL> silleriEslestirmesizBul(ECertificate iCert);

    public ECRL findDeltaCRL(ValidationSystem aValidationSystem, ECRL aBaseCRL, CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        /*if (mDeltaCRLFinders.isEmpty())
            return null;*/

        ECRL crl = null;
        MatchSystem es = aValidationSystem.getMatchSystem();

        // Dışarıdan verilmiş siller arasında mı bakalım
        List<ECRL> uiCRLList = aValidationSystem.getUserInitialCRLSet();
        crl = Find.deltaCRLFromList(es, uiCRLList, aBaseCRL);

        if (crl == null || !_isDeltaCRLValid(aValidationSystem, crl, aCertStatusInfo)) {
            crl = null;
            for (DeltaCRLFinder deltaCRLFinder : mDeltaCRLFinders) {
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

    public ECRL findDeltaCRL(ValidationSystem aValidationSystem, ECertificate aCertificate, CertificateStatusInfo aCertStatusInfo) throws ESYAException
    {
        /*if (mDeltaCRLFinders.isEmpty())
            return null;*/

        ECRL crl = null;
        MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        // Dışarıdan verilmiş siller arasında mı bakalım
        List<ECRL> uiCRLList = aValidationSystem.getUserInitialCRLSet();
        crl = Find.deltaCRLFromList(matchSystem, uiCRLList, aCertificate);

        if ((crl == null) || !_isDeltaCRLValid(aValidationSystem, crl, aCertStatusInfo)) {
            crl = null;
            for (DeltaCRLFinder pDSB : mDeltaCRLFinders) {
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
            throws ESYAException
    {
        /*if (!mCrossCertificateFinders.isEmpty())
            return null;  */

        ECertificate certificate = null;
        MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        // Geçerli Sertifikalar arasında var mı bakalım
        List<ECertificate> validCertList = aValidationSystem.getValidCertificateSet();
        certificate = Find.crossCertificateFromList(matchSystem, validCertList, aCertificate);
        if (certificate != null) {
            return certificate;
        }


        // Dışarıdan verilmiş sertifikalar arasında var mı bakalım
        List<ECertificate> uiCertList = aValidationSystem.getUserInitialCertificateSet();
        certificate = Find.crossCertificateFromList(matchSystem, uiCertList, aCertificate);


        if (certificate == null) {
            // Güvenilir Sertifikalar arasında var mı bakalım
            List<ECertificate> gsList = getTrustedCertificates();
            certificate = Find.crossCertificateFromList(matchSystem, gsList, aCertificate);
        }


        if ((certificate == null) || !isCrossCertValid(aValidationSystem, certificate)) {
            certificate = null; //DELETE_MEMORY(pSertifika)
            // Hala bulamadıysak bulucularımız arasın
            for (CrossCertificateFinder crossCertFinder : mCrossCertificateFinders) {
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


    boolean _checkIssuer(ValidationSystem aValidationSystem, StatusInfo aStatusInfo, ECertificate aIssuerCertificate)
            throws ESYAException
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

    public boolean checkCRL(ValidationSystem aValidationSystem,
                            CertificateStatusInfo aCertStatusInfo,
                            ECRL aCRL)
            throws ESYAException
    {
        // 		if (eslesenCRLList[i].getTBSCertList().getIssuer() == aSDB.sertifikaAl().getTBSCertificate().getSubject())
        // 			return true;
        // buraya bakılacak

        CRLController crlController = new CRLController();
        CRLStatusInfo csi = crlController.check(aValidationSystem, aCRL);


        if( csi.getCRLStatus() == CRLStatus.VALID )	// Geçerli Mi?
        {
            if ( ! aCertStatusInfo.getTrustCertificate().equals(csi.getTrustCertificate()))
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        aCertStatusInfo.addCRLInfo(csi);
	    return true;

    }


    boolean _isDeltaCRLValid(ValidationSystem aValidationSystem, ECRL aDeltaCRL, CertificateStatusInfo aCertStatusInfo)
            throws ESYAException
    {
        DeltaCRLController dsk = new DeltaCRLController();
        CRLStatusInfo crlStatusInfo = dsk.check(aValidationSystem, aDeltaCRL);
        aCertStatusInfo.addDeltaCRLInfo(crlStatusInfo);

        return (crlStatusInfo.getCRLStatus() == CRLStatus.VALID);
    }

    boolean isCrossCertValid(ValidationSystem aValidationSystem, ECertificate aCrossCertificate) throws ESYAException
    {
        CertificateController certificateController = new CertificateController();

        CertificateStatusInfo csi = certificateController.check(aValidationSystem, aCrossCertificate);

        return (csi.getCertificateStatus() == CertificateStatus.VALID);
    }


    // todo iRemoteSearch default true

    public List<ECertificate> findIssuerCertificate(ValidationSystem aValidationSystem, ECertificate aCertificate, boolean aRemoteSearch)
    {
        MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        String subject = aCertificate.getSubject().stringValue();
        if (logger.isDebugEnabled())
            logger.debug(subject + " sertifika SM sertifikalari bulunacak");

        List<ECertificate> foundList = new ArrayList<ECertificate>();

        // Kontrol Edilmiş Sertifikalar arasında mı kontrol edelim
        List<ECertificate> certList = aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
        foundList = Find.issuerCertificatesFromList(matchSystem, certList, aCertificate, foundList);

        // Guvenilir Sertifikalar arasında mı kontrol edelim
        List<ECertificate> gsList = getTrustedCertificates();
        foundList = Find.issuerCertificatesFromList(matchSystem, gsList, aCertificate, foundList);

        // Dışarıdan Verilmiş Sertifikalar arasında mı kontrol edelim
        List<ECertificate> uiCertList = aValidationSystem.getUserInitialCertificateSet();
        foundList = Find.issuerCertificatesFromList(matchSystem, uiCertList, aCertificate, foundList);

        for (CertificateFinder certificateFinder : mCertificateFinders) {
            if (!aRemoteSearch && certificateFinder.isRemote())
                continue; // Local modda arama yapıyorsak uzak bulucuları geçiyoruz.
            certList = certificateFinder.findCertificate(aCertificate);
            foundList = Find.issuerCertificatesFromList(matchSystem, certList, aCertificate, foundList);
        }

        return foundList;

    }


}
