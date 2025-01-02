using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.policy
{
    /**
     * <p>Validation Policy specifies which controls will be performed and in which
     * order they take place during validation of certificates , CRLs or OCSP
     * Responses. The class contains list of PolicyClassInfo objects to define
     * Checker , Finder , Matcherand other validation parameters
     *
     * <p>Checkers Checkers performs the control steps of validation. To make it
     * configurable; each control step is defined in its own checker. For example
     * the condition which is specified in RFC 5280 "The serial number is a positive
     * integer." is checked by PositiveSerialNumberChecker class.
     *
     * <p>Finders During validation process, some items such as issuer certificates
     * or crls must be found from some places. These external items to be found are
     * searched and found according to the finders. Each Finder specifies a location
     * or means of finding an item. For example to locate an issuer certificate
     * according to the Authority Info Access extension of the certificate ,
     * CertificateFinderFromAIA is used.
     *
     * <p>Matchers When an associatiated item like CA certificate of a certificate
     * or CRL of a certificate is found, the matching between these two items must
     * be done according to several criteria specified in RFC5280. Those matching
     * criteria are defined in matcher classes.
     */
    public class ValidationPolicy : ICloneable
    {
        private FindingPolicy mFindingPolicy = new FindingPolicy();

        private List<PolicyClassInfo> mTrustedCertificateCheckers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mCertificateSelfCheckers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mCertificateIssuerCheckers = new List<PolicyClassInfo>();
        //private List<PolicyClassInfo> mRevocationCheckers = new List<PolicyClassInfo>();
        private List<RevocationPolicyClassInfo> mRevocationCheckers = new List<RevocationPolicyClassInfo>();


        private List<PolicyClassInfo> mCRLSelfCheckers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mCRLIssuerCheckers = new List<PolicyClassInfo>();

        private List<PolicyClassInfo> mDeltaCRLCheckers = new List<PolicyClassInfo>();

        private List<PolicyClassInfo> mOCSPResponseCheckers = new List<PolicyClassInfo>();

        private List<PolicyClassInfo> mSavers = new List<PolicyClassInfo>();


        private List<String> mUserInitialPolicySet = new List<String>();
        private bool mInitialPolicyMappingInhibit;
        private bool mInitialExplicitPolicy;
        private bool mInitialAnyPolicyInhibit;
        private String mDefaultStorePath;

        /**
         * ValidationPolicy constructoru
         *
         public ValidationPolicy(String iText)
         {
         mUserInitialPolicySet.add(Constants.IMP_ANY_POLICY.toString());
         PolicyReader.oku(iText, this);
         }                                                                */

        /**
         * ValidationPolicy constructoru
         */
        public ValidationPolicy()
        {
            mUserInitialPolicySet.Add(OIDUtil.concat(Constants.IMP_ANY_POLICY.mValue));
        }

        /**
         * Returns the FindingPolicy of ValidationPolicy
         * @return 
         */
        public FindingPolicy bulmaPolitikasiAl()
        {
            return mFindingPolicy;
        }

        /**
         * Bulma Politikası belirler
         */
        public void setFindingPolicy(FindingPolicy aFindingPolicy)
        {
            mFindingPolicy = aFindingPolicy;
        }

        /**
         * Güvenilir Sertifika Kontrolcu ekler
         */
        public void addTrustedCertificateChecker(String aTrustedCertificateChecker, Dictionary<String, Object> aParameters)
        {
            mTrustedCertificateCheckers.Add(new PolicyClassInfo(aTrustedCertificateChecker, new ParameterList(aParameters)));
        }

        /**
         * Güvenilir Sertifika Kontrolcüleri belirler
         */
        public void setTrustedCertificateCheckers(List<PolicyClassInfo> aTrustedCertificateCheckers)
        {
            mTrustedCertificateCheckers = aTrustedCertificateCheckers;
        }

        /**
         * Tek Sertifika Kontrolcu ekler
         */
        public void addCertificateSelfChecker(String aCertificateSelfCheckers, Dictionary<String, Object> aParameters)
        {
            mCertificateSelfCheckers.Add(new PolicyClassInfo(aCertificateSelfCheckers, new ParameterList(aParameters)));
        }

        /**
         * Tek Sertifika Kontrolcüleri belirler
         */
        public void setCertificateSelfCheckers(List<PolicyClassInfo> aCertificateSelfCheckers)
        {
            mCertificateSelfCheckers = aCertificateSelfCheckers;
        }

        /**
         * Sertifika SM  Kontrolcu ekler
         */
        public void addCertificateIssuerChecker(String aCertificateIssuerChecker, Dictionary<String, Object> aParameters)
        {
            mCertificateIssuerCheckers.Add(new PolicyClassInfo(aCertificateIssuerChecker, new ParameterList(aParameters)));
        }

        /**
         * Sertifika SM Kontrolcüleri belirler
         */
        public void setCertificateIssuerCheckers(List<PolicyClassInfo> aCertificateIssuerCheckers)
        {
            mCertificateIssuerCheckers = aCertificateIssuerCheckers;
        }

        /**
         * İptal Kontrolcu ekler
         */        
        public void addRevocationChecker(String aRevocationChecker, Dictionary<String, Object> aParameters, List<PolicyClassInfo> aFinders)
        {
            mRevocationCheckers.Add(new RevocationPolicyClassInfo(aRevocationChecker, new ParameterList(aParameters), aFinders));
        }

        /**
         * İptal Kontrolcüleri belirler
         */
        public void setRevocationCheckers(List<RevocationPolicyClassInfo> aRevocationCheckers)
        {
            mRevocationCheckers = aRevocationCheckers;
        }

        /**
         * Tek Sil Kontrolcu ekler
         */
        public void tekSilKontrolcuEkle(String aCRLSelfChecker, Dictionary<String, Object> aParameters)
        {
            mCRLSelfCheckers.Add(new PolicyClassInfo(aCRLSelfChecker, new ParameterList(aParameters)));
        }

        /**
         * Tek Sil Kontrolcüleri belirler
         */
        public void setCRLSelfCheckers(List<PolicyClassInfo> aCRLSelfCheckers)
        {
            mCRLSelfCheckers = aCRLSelfCheckers;
        }

        /**
         * SilSM Kontrolcu ekler
         */
        public void addCRLIssuerChecker(String aCRLIssuerChecker, Dictionary<String, Object> aParameters)
        {
            mCRLIssuerCheckers.Add(new PolicyClassInfo(aCRLIssuerChecker, new ParameterList(aParameters)));
        }

        /**
         * SilSM Kontrolcüleri belirler
         */
        public void setCRLIssuerCheckers(List<PolicyClassInfo> aCRLIssuerCheckers)
        {
            mCRLIssuerCheckers = aCRLIssuerCheckers;
        }

        /**
         * OCSP Cevabı Kontrolcu ekler
         */
        public void addOCSPResponseChecker(String mOCSPResponseChecker, Dictionary<String, Object> aParameters)
        {
            mOCSPResponseCheckers.Add(new PolicyClassInfo(mOCSPResponseChecker, new ParameterList(aParameters)));
        }

        /**
         * OCSP Cevabı Kontrolcüleri belirler
         */
        public void setOCSPResponseCheckers(List<PolicyClassInfo> aOCSPResponseCheckers)
        {
            mOCSPResponseCheckers = aOCSPResponseCheckers;
        }

        /**
         * delta-SİL Kontrolcu ekler
         */
        public void addDeltaCRLChecker(String aDeltaCRLChecker, Dictionary<String, Object> aParameters)
        {
            mDeltaCRLCheckers.Add(new PolicyClassInfo(aDeltaCRLChecker, new ParameterList(aParameters)));
        }

        /**
         * delta-SİL Kontrolcüleri belirler
         */
        public void setDeltaCRLCheckers(List<PolicyClassInfo> aDeltaCRLCheckers)
        {
            mDeltaCRLCheckers = aDeltaCRLCheckers;
        }

        /**
         * Saver ekler
         */
        public void addSaver(String aSaver, Dictionary<String, Object> aParameters)
        {
            mSavers.Add(new PolicyClassInfo(aSaver, new ParameterList(aParameters)));
        }

        /**
         * Kaydedicileri belirler
         */
        public void setSavers(List<PolicyClassInfo> aSavers)
        {
            mSavers = aSavers;
        }

        /**
         * Güvenilir Sertifika Kontrolcüleri döner
         */
        public List<PolicyClassInfo> getTrustedCertificateCheckers()
        {
            return mTrustedCertificateCheckers;
        }

        /**
         * Tek Sertifika Kontrolcüler
         */
        public List<PolicyClassInfo> getCertificateSelfCheckers()
        {
            return mCertificateSelfCheckers;
        }

        /**
         * ÜstSM Kontrolcüleri döner
         */
        public List<PolicyClassInfo> getCertificateIssuerCheckers()
        {
            return mCertificateIssuerCheckers;
        }

        /**
         * İptal Kontrolcüleri döner
         */
        public List<RevocationPolicyClassInfo> getRevocationCheckers()
        {
            return mRevocationCheckers;
        }

        /**
         * Tek SİL Kontrolcüleri döner
         */
        public List<PolicyClassInfo> getCRLSelfCheckers()
        {
            return mCRLSelfCheckers;
        }

        /**
         * SİL-SM Kontcolcüleri döner
         */
        public List<PolicyClassInfo> getCRLIssuerCheckers()
        {
            return mCRLIssuerCheckers;
        }

        /**
         * OCSP Cevabı Kontrolcüleri döner
         */
        public List<PolicyClassInfo> getOCSPResponseCheckers()
        {
            return mOCSPResponseCheckers;
        }

        /**
         * delta-SİL Kontrolcüleri döner
         */
        public List<PolicyClassInfo> getDeltaCRLCheckers()
        {
            return mDeltaCRLCheckers;
        }

        /**
         * Set default path of Store
         * @param aDefaultStorePath path of Store
         */
        public void setDefaultStorePath(String aDefaultStorePath)
        {
            mDefaultStorePath = aDefaultStorePath;
        }

        /**
         * Get default location of StorePath
         */
        public String getDefaultStorePath()
        {
            return mDefaultStorePath;
        }

        /**
         * Kaydedicileri döner
         */
        public List<PolicyClassInfo> getSavers()
        {
            return mSavers;
        }

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

        public void setUserInitialPolicySet(List<String> aList)
        {
            mUserInitialPolicySet = aList;
        }

        public void addUserInitialPolicy(String aUserInitialPolicy)
        {
            mUserInitialPolicySet.Add(aUserInitialPolicy);
        }

        public void setInitialPolicyMappingInhibit(bool aInitialPolicyMappingInhibit)
        {
            mInitialPolicyMappingInhibit = aInitialPolicyMappingInhibit;
        }

        public void setInitialExplicitPolicy(bool aInitialExplicitPolicy)
        {
            mInitialExplicitPolicy = aInitialExplicitPolicy;
        }

        public void setInitialAnyPolicyInhibit(bool aInitialAnyPolicyInhibit)
        {
            mInitialAnyPolicyInhibit = aInitialAnyPolicyInhibit;
        }

        public Object Clone()
        {
            return MemberwiseClone();
        }

        /**
         * Close all finder,Do not use external Resource
         */
        public ValidationPolicy withoutFinders()  
        {
    	    ValidationPolicy policy = null;
    	    try {
    		    policy = (ValidationPolicy)this.Clone();
    	    } catch (Exception e) {
			    throw new ESYARuntimeException(e);
		    }

    	    List<RevocationPolicyClassInfo> revocationCheckers = new List<RevocationPolicyClassInfo>();
    	    policy.setRevocationCheckers(new List<RevocationPolicyClassInfo>(getRevocationCheckers()));
    	    foreach (RevocationPolicyClassInfo rc in policy.getRevocationCheckers()){
    		    revocationCheckers.Add(new RevocationPolicyClassInfo(rc.getClassName(), rc.getParameters(), new List<PolicyClassInfo>()));
    	    }
    	    policy.setRevocationCheckers(revocationCheckers);
        	
    	    FindingPolicy fp = new FindingPolicy();
    	    fp.setTrustedCertificateFinders(mFindingPolicy.getTrustedCertificateFinders());
    	    fp.setCrossCertificateFinders(mFindingPolicy.getCrossCertificateFinders());
    	    fp.setMatchingPolicy(mFindingPolicy.getMatchingPolicy());
    	    fp.setSavePolicy(mFindingPolicy.getSavePolicy());
    	    policy.setFindingPolicy(fp);

    	    return policy;
        }

        public static void main(String[] args)
        {
            new ValidationPolicy().withoutFinders();
        }
    }
}
