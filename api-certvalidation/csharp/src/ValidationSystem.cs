using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
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
    public class ValidationSystem : ICloneable
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private CheckSystem mCheckSystem = new CheckSystem();

        /// Bulma işlemlerinde kullanılacak bulucular
        private FindSystem mFindSystem = new FindSystem();

        /// Bulma işlemlerinde kullanılacak eşleştiriciler
        private MatchSystem mMatchSystem = new MatchSystem();


        /// Bulma işlemlerinden sonra kaydetme işini yapacak kaydediciler
        private SaveSystem mSaveSystem = new SaveSystem();

        private CertificateValidationCache mCertificateValidationCache = new CertificateValidationCache();
        private CRLValidationCache mCRLValidationCache = new CRLValidationCache();

        private List<ECertificate> mCyclicCheckList = new List<ECertificate>(); // Döngüsel kontrolleri engellemek için
        /// kontrol edilmekte olan sertifikaları saklıyor.

        private List<ECertificate> mValidCertificateSet = new List<ECertificate>(); // Dogrulama işleminde öntanımlı olarak geçerliliği
        /// bilinen sertifikaları saklıyor

        private List<ECertificate> mUserInitialCertificateSet = new List<ECertificate>(); // Dogrulama işleminde dışarıdan verilmiş
        /// sertifikaları saklıyor

        private List<ECRL> mUserInitialCRLSet = new List<ECRL>(); // Dogrulama işleminde dışarıdan verilmiş
        /// silleri saklıyor

        private List<EOCSPResponse> mUserInitialOCSPResponseSet; // Dogrulama işleminde dışarıdan verilmiş
        /// OCSP Cevaplarını saklıyor

        Dictionary<String, List<ECertificate>> mCACache = new Dictionary<String, List<ECertificate>>();

        private List<String> mUserInitialPolicySet;
        private bool mInitialPolicyMappingInhibit;
        private bool mInitialExplicitPolicy;
        private bool mInitialAnyPolicyInhibit;
        private String mDefaultStorePath;

        //private List<PathValidationRecord> mValidationHistory = new ArrayList<PathValidationRecord>();
        private DateTime? mBaseValidationTime;
        private DateTime? mLastRevocationTime;

        private ITimeProvider mTimeProvider;

        private bool mDoNotUsePastRevocationInfo = false;

        public ValidationSystem()
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
        public bool isTrustedCertificate(ECertificate aCertificate)
        {
            return mFindSystem.isTrustedCertificate(aCertificate);
        }

        /**
         * \brief
         * Döngü Koruma Listesini döner
         *
         * \return List<ECertificate>
         * Döngü Koruma Listesi
         */
        public List<ECertificate> getCyclicCheckList()
        {
            return mCyclicCheckList;
        }

        /**
         * Döngü Koruma Listesini belirler
         */
        public void setCyclicCheckList(List<ECertificate> iDKL)
        {
            mCyclicCheckList = iDKL;
        }

        /**
         * Döngü Koruma Listesine sertifika ekler
         */
        public void addToCyclicCheckList(ECertificate iCertificate)
        {
            mCyclicCheckList.Add(iCertificate);
        }

        /**
         * Döngü Koruma Listesinden sertifika cikarir
         */
        public void removeFromCyclicCheckList(ECertificate iCertificate)
        {
            mCyclicCheckList.Remove(iCertificate);
        }

        /**
         * Geçerli Sertifika Listesini döner
         */
        public List<ECertificate> getValidCertificateSet()
        {
            return mValidCertificateSet;
        }

        /**
         * Geçerli Sertifika Listesini belirler
         */
        public void setValidCertificateSet(List<ECertificate> iVCList)
        {
            mValidCertificateSet = iVCList;
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
        public void setUserInitialCertificateSet(List<ECertificate> iUICertList)
        {
            mUserInitialCertificateSet = iUICertList;
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
        public void setUserInitialOCSPResponseSet(List<EOCSPResponse> iUIOCSPList)
        {
            mUserInitialOCSPResponseSet = iUIOCSPList;
        }

        /**
         * Returns user initial policy of validation system
         * @return
         */
        public List<String> getUserInitialPolicySet()
        {
            return mUserInitialPolicySet;
        }

        public bool isInitialPolicyMappingInhibit()
        {
            return mInitialPolicyMappingInhibit;
        }

        public bool isInitialExplicitPolicy()
        {
            return mInitialExplicitPolicy;
        }

        public bool isInitialAnyPolicyInhibit()
        {
            return mInitialAnyPolicyInhibit;
        }

        public void setUserInitialPolicySet(List<String> iList)
        {
            mUserInitialPolicySet = iList;
        }

        public void addToUserInitialPolicySet(String iPolicy)
        {
            mUserInitialPolicySet.Add(iPolicy);
        }

        public void setInitialPolicyMappingInhibit(bool iPMI)
        {
            mInitialPolicyMappingInhibit = iPMI;
        }

        public void setInitialExplicitPolicy(bool iEP)
        {
            mInitialExplicitPolicy = iEP;
        }

        public void setInitialAnyPolicyInhibit(bool iAPI)
        {
            mInitialAnyPolicyInhibit = iAPI;
        }

        /**
         * Set default path of store
         */ 
        public void setDefaultStorePath(String aDefaultStorePath)
        {
            mDefaultStorePath = aDefaultStorePath;
        }

        /**
         * Returns default path of store
         * @return
         */
        public String getDefaultStorePath()
        {
            return mDefaultStorePath;
        }

        public ITimeProvider getTimeProvider()
        {
            return mTimeProvider;
        }

        public void setTimeProvider(ITimeProvider aTimeProvider)
        {
            mTimeProvider = aTimeProvider;
        }

        /**
         * Returns base validation time which signature(s) will be validated  in this time
         * @return
         */
        public DateTime? getBaseValidationTime()
        {
            if (mBaseValidationTime == null)
            {
                if (getTimeProvider() != null)
                {
                    mBaseValidationTime = getTimeProvider().getCurrentTime();
                }
                else
                {
                    mBaseValidationTime = DateTime.UtcNow;
                }
            }
            return mBaseValidationTime;           
        }

        /**
         * Set base validation time which signature(s) will be validated  in this time
         */
        public void setBaseValidationTime(DateTime? aBaseValidationTime)
        {
            mBaseValidationTime = aBaseValidationTime;
        }

        public DateTime? getLastRevocationTime()
        {
            return mLastRevocationTime;
        }

        public void setLastRevocationTime(DateTime? iLRT)
        {
            mLastRevocationTime = iLRT;
        }

        /**
         * Checks whether old revocation info is used or not
         * @return boolean
         */
        public bool isDoNotUsePastRevocationInfo()
        {
            return mDoNotUsePastRevocationInfo;
        }

        /**
         * Set DoNotUsePastRevocationInfo flag which old revocation info is used if it is false,old revocation info does not used otherwise
         */
        public void setDoNotUsePastRevocationInfo(bool aDoNotUsePastRevocationInfo)
        {
            mDoNotUsePastRevocationInfo = aDoNotUsePastRevocationInfo;
        }

        public Dictionary<String, List<ECertificate>> getCACache()
        {
            return mCACache;
        }

        public void setCACache(Dictionary<String, List<ECertificate>> aCACache)
        {
            mCACache = aCACache;
        }

        public void addToCACache(ECertificate aCACert)
        {
            if (!aCACert.isCACertificate())
                return;

            String key = aCACert.getSubject().stringValue();
            if (!mCACache.ContainsKey(key))
            {
                List<ECertificate> list = new List<ECertificate>();
                list.Add(aCACert);
                mCACache[key] = list;
            }
            else
            {
                mCACache[key].Add(aCACert);
            }
        }

        public List<ECertificate> getCachedCACertificates(String aKey)
        {
            List<ECertificate> value = null;
            mCACache.TryGetValue(aKey, out value);
            return value;
        }

        // not a proper clone!
        public Object Clone()
        {
            try
            {
                ValidationSystem vs = (ValidationSystem) base.MemberwiseClone();

                CheckSystem csClone = (CheckSystem) mCheckSystem.Clone();

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
            }
            catch (Exception x)
            {
                throw new SystemException("", x);
            }
        }

        private void fixParentSystem<T>(IList<T> checkers, ValidationSystem newSystem) where T : Checker
        {
            foreach (Checker checker in checkers)
            {
                checker.setParentSystem(newSystem);
            }
        }

    }
}
