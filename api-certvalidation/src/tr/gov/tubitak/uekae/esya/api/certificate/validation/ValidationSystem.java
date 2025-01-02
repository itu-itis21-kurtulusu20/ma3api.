package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.MultiMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * <p>Validation System contains all of the validation parameters. In addition
 * to these parameters it also includes various structures such as
 * ValidationHistory lists and CACache in order to provide performance
 * optimizations especially druing batch validaton operations.
 *
 * <h3>Validation Parameters</h3>
 *
 * <p>CheckSystem: The list of Checker objects specifies which controls will be
 * performed during validation.
 *
 * <p>FindSystem: The list of Finder objects specifies from where the certificates,
 * crls and OCSP Responses required for validation will be found.
 *
 * <p>MatchSystem: The list of Matcher objects specifies how it will be decided
 * that the found items are the correct matches
 */
public class ValidationSystem implements Cloneable
{
	private static final Logger logger = LoggerFactory.getLogger(ValidationSystem.class);

    private CheckSystem mCheckSystem = new CheckSystem();

    /// Bulma işlemlerinde kullanılacak bulucular
    private FindSystem mFindSystem = new FindSystem();

    /// Bulma işlemlerinde kullanılacak eşleştiriciler
    private MatchSystem mMatchSystem = new MatchSystem();


    /// Bulma işlemlerinden sonra kaydetme işini yapacak kaydediciler
    private SaveSystem mSaveSystem = new SaveSystem();

    private CertificateValidationCache mCertificateValidationCache = new CertificateValidationCache();
    private CRLValidationCache mCRLValidationCache = new CRLValidationCache();

    private List<ECertificate> mCyclicCheckList = new ArrayList<ECertificate>(); // Döngüsel kontrolleri engellemek için
    /// kontrol edilmekte olan sertifikaları saklıyor.

    private List<ECertificate> mValidCertificateSet = new ArrayList<ECertificate>(); // Dogrulama işleminde öntanımlı olarak geçerliliği
    /// bilinen sertifikaları saklıyor

    private List<ECertificate> mUserInitialCertificateSet = new ArrayList<ECertificate>(); // Dogrulama işleminde dışarıdan verilmiş
    /// sertifikaları saklıyor

    private List<ECRL> mUserInitialCRLSet = new ArrayList<ECRL>(); // Dogrulama işleminde dışarıdan verilmiş
    /// silleri saklıyor

    private List<EOCSPResponse> mUserInitialOCSPResponseSet; // Dogrulama işleminde dışarıdan verilmiş
    /// OCSP Cevaplarını saklıyor

    private MultiMap<String,ECertificate> mCACache = new MultiMap<String, ECertificate>();

    private List<String> mUserInitialPolicySet;
    private boolean mInitialPolicyMappingInhibit;
    private boolean mInitialExplicitPolicy;
    private boolean mInitialAnyPolicyInhibit;
    private String mDefaultStorePath;

    //private List<PathValidationRecord> mValidationHistory = new ArrayList<PathValidationRecord>();
    private Calendar mBaseValidationTime;
    private Calendar mLastRevocationTime;

    private TimeProvider mTimeProvider;

    private boolean mDoNotUsePastRevocationInfo = false;

    public ValidationSystem()
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
    
   
    /**
	 * Returns check system of validation system
	 * @return
	 */
    public CheckSystem getCheckSystem()
    {
        return mCheckSystem;
    }

    /**
	 * Set check system of validation system
	 */
    public void setCheckSytem(CheckSystem aCheckSystem)
    {
        mCheckSystem = aCheckSystem;
    }

    /**
	 * Returns find system of validation system
	 * @return
	 */
    public FindSystem getFindSystem()
    {
        return mFindSystem;
    }

    /**
	 * Set find system of validation system
	 */
    public void setFindSystem(FindSystem aFindSystem)
    {
        mFindSystem = aFindSystem;
    }

    /**
	 * Returns match system of validation system
	 * @return
	 */
    public MatchSystem getMatchSystem()
    {
        return mMatchSystem;
    }

    /**
	 * Set match system of validation system
	 */
    public void setMatchSystem(MatchSystem aMatchSystem)
    {
        mMatchSystem = aMatchSystem;
    }

    /**
	 * Returns save system of validation system
	 * @return
	 */
    public SaveSystem getSaveSystem()
    {
        return mSaveSystem;
    }

    /**
	 * Set save system of validation system
	 */
    public void setSaveSystem(SaveSystem aSaveSystem)
    {
        mSaveSystem = aSaveSystem;
    }

    /**
     * Sertifika Doğrulama Hafızası döner
     */
    public CertificateValidationCache getCertificateValidationCache()
    {
        return mCertificateValidationCache;
    }

    /**
     * Sertifika Doğrulama Hafızası belirler
     */
    public void setCertificateValidationCache(CertificateValidationCache aCVC)
    {
        mCertificateValidationCache = aCVC;
    }

    /**
     * SİL Doğrulama Hafızası döner
     */
    public CRLValidationCache getCRLValidationCache()
    {
        return mCRLValidationCache;
    }

    /**
     * SİL Doğrulama Hafızası belirler
     */
    public void setCRLValidationCache(CRLValidationCache aCVC)
    {
        mCRLValidationCache = aCVC;
    }

    /**
	 * Checks whether aCertificate is trustedCertificate or not
	 * @return boolean
	 */
    public boolean isTrustedCertificate(ECertificate aCertificate)
    {
        return mFindSystem.isTrustedCertificate(aCertificate);
    }

    /**
     * \brief
     * Döngü Koruma Listesini döner
     *
     * \return List{@literal <ECertificate>}
     * Döngü Koruma Listesi
     */
    public List<ECertificate> getCyclicCheckList()
    {
        return mCyclicCheckList;
    }

    /**
     * Döngü Koruma Listesini belirler
     */
    public void setCyclicCheckList(List<ECertificate> aCyclicCheckList)
    {
        mCyclicCheckList = aCyclicCheckList;
    }

    /**
     * Döngü Koruma Listesine sertifika ekler
     */
    public void addToCyclicCheckList(ECertificate aCertificate)
    {
        mCyclicCheckList.add(aCertificate);
    }

    /**
     * Döngü Koruma Listesinden sertifika cikarir
     */
    public void removeFromCyclicCheckList(ECertificate aCertificate)
    {
        mCyclicCheckList.remove(aCertificate);
    }

    /**
     * Gecerli Sertifika Listesini döner
     */
    public List<ECertificate> getValidCertificateSet()
    {
        return mValidCertificateSet;
    }

    /**
     * Geçerli Sertifika Listesini belirler
     */
    public void setValidCertificateSet(List<ECertificate> aValidCertificateSet)
    {
        mValidCertificateSet = aValidCertificateSet;
    }

    /**
     * Kullanıcı Tanımlı Sertifika Listesini döner
     */
    public List<ECertificate> getUserInitialCertificateSet()
    {
        return mUserInitialCertificateSet;
    }

    /**
     * Kullanıcı Tanımlı Sertifika Listesini belirler
     */
    public void setUserInitialCertificateSet(List<ECertificate> aCertificateList)
    {
        mUserInitialCertificateSet = aCertificateList;
    }

    /**
     * Kullanıcı Tanımlı SIL Listesini döner
     */
    public List<ECRL> getUserInitialCRLSet()
    {
        return mUserInitialCRLSet;
    }

    /**
     * Kullanıcı Tanımlı SIL Listesini belirler
     */
    public void setUserInitialCRLSet(List<ECRL> aUserInitialCRLList)
    {
        mUserInitialCRLSet = aUserInitialCRLList;
    }

    /**
     * Kullanıcı Tanımlı OCSP Cevabı  Listesini belirler
     */
    public List<EOCSPResponse> getUserInitialOCSPResponseSet()
    {
        return mUserInitialOCSPResponseSet;
    }


    /**
     * Kullanıcı Tanımlı OCSP Cevabı  Listesini belirler
     */
    public void setUserInitialOCSPResponseSet(List<EOCSPResponse> aResponseList)
    {
        mUserInitialOCSPResponseSet = aResponseList;
    }

    /**
	 * Returns user initial policy of validation system
	 * @return
	 */
    public List<String> getUserInitialPolicySet()
    {
        return mUserInitialPolicySet;
    }

    public boolean isInitialPolicyMappingInhibit()
    {
        return mInitialPolicyMappingInhibit;
    }

    public boolean isInitialExplicitPolicy()
    {
        return mInitialExplicitPolicy;
    }

    public boolean isInitialAnyPolicyInhibit()
    {
        return mInitialAnyPolicyInhibit;
    }

    public void setUserInitialPolicySet(List<String> aPolicyList)
    {
        mUserInitialPolicySet = aPolicyList;
    }

    public void addToUserInitialPolicySet(String aPolicy)
    {
        mUserInitialPolicySet.add(aPolicy);
    }

    public void setInitialPolicyMappingInhibit(boolean aPMI)
    {
        mInitialPolicyMappingInhibit = aPMI;
    }

    public void setInitialExplicitPolicy(boolean aEP)
    {
        mInitialExplicitPolicy = aEP;
    }

    public void setInitialAnyPolicyInhibit(boolean aIAPI)
    {
        mInitialAnyPolicyInhibit = aIAPI;
    }

    /**
   	 * Set default path of store
   	 */  
    public void setDefaultStorePath(String aDefaultStorePath){
    	mDefaultStorePath = aDefaultStorePath;
    }

    /**
   	 * Returns default path of store
   	 * @return
   	 */
    public String getDefaultStorePath(){
    	return mDefaultStorePath;
    }

    /*
    public List<PathValidationRecord> getValidationHistory()
    {
        return mValidationHistory;
    }

    public void setValidationHistory(List<PathValidationRecord> iVH)
    {
        mValidationHistory = iVH;
    }

    public void addRecordToValidationHistory(PathValidationRecord iPVR)
    {
        mValidationHistory.add(iPVR);
    }  */

    public TimeProvider getTimeProvider()
    {
        return mTimeProvider;
    }

    public void setTimeProvider(TimeProvider aTimeProvider)
    {
        mTimeProvider = aTimeProvider;
    }

    /**
	 * Returns base validation time which signature(s) will be validated  in this time
	 * @return
	 */
    public Calendar getBaseValidationTime()
    {
        if (mBaseValidationTime == null){
            if (getTimeProvider()!=null){
                mBaseValidationTime = getTimeProvider().getCurrentTime();
            }
            else {
                mBaseValidationTime = Calendar.getInstance();
            }
        }
        return mBaseValidationTime;
    }

    /**
   	 * Set base validation time which signature(s) will be validated  in this time
   	 */
    public void setBaseValidationTime(Calendar aBaseValidationTime)
    {
        mBaseValidationTime = aBaseValidationTime;
    }

    public Calendar getLastRevocationTime()
    {
        return mLastRevocationTime;
    }

    public void setLastRevocationTime(Calendar aLRT)
    {
        mLastRevocationTime = aLRT;
    }

    /**
 	 * Checks whether old revocation info is used or not
 	 * @return boolean
 	 */
    public boolean isDoNotUsePastRevocationInfo()
    {
        return mDoNotUsePastRevocationInfo;
    }

    /**
   	 * Set DoNotUsePastRevocationInfo flag which old revocation info is used if it is false,old revocation info does not used otherwise
   	 */
    public void setDoNotUsePastRevocationInfo(boolean aDoNotUsePastRevocationInfo)
    {
    	mDoNotUsePastRevocationInfo = aDoNotUsePastRevocationInfo;
    }

    public Map<String, List<ECertificate>>  getCACache()
    {
        return mCACache;
    }

    public void setCACache(MultiMap<String, ECertificate> aCACache)
    {
        mCACache = aCACache;
    }

    public void addToCACache(ECertificate aCACert)
    {
        if (!aCACert.isCACertificate())
            return;

        String key = aCACert.getSubject().stringValue();
        mCACache.insert(key, aCACert);
    }

    public List<ECertificate> getCachedCACertificates(String aKey)
    {
        return mCACache.get(aKey);
    }

    // not a proper clone!
    //@Override
    public Object clone() {
        try {
            ValidationSystem vs = (ValidationSystem)super.clone();

            CheckSystem csClone = (CheckSystem)mCheckSystem.clone();

            fixParentSystem(csClone.getTrustedCertificateCheckers(), vs);
            fixParentSystem(csClone.getCertificateSelfCheckers(), vs);
            fixParentSystem(csClone.getIssuerCheckers(), vs);
            fixParentSystem(csClone.getRevocationCheckers(), vs);
            fixParentSystem(csClone.getCRLSelfCheckers(), vs);
            fixParentSystem(csClone.getCRLIssuerCheckers(), vs);
            fixParentSystem(csClone.getOCSPResponseCheckers(), vs);
            fixParentSystem(csClone.getDeltaCRLCheckers(), vs);

            vs.setCheckSytem(csClone);

            return vs;
        } catch (Exception x){
            throw new ESYARuntimeException(x);
        }
    }

    private void fixParentSystem(List<? extends Checker> checkers, ValidationSystem newSystem){
        for (Checker checker : checkers){
            checker.setParentSystem(newSystem);
        }
    }
}
