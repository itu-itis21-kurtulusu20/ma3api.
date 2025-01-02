using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * Specifies the finder classes that will be used during validation. * <p>
     * 
     * @author IH
     */
    public class FindingPolicy
    {
        private MatchingPolicy mMatchingPolicy = new MatchingPolicy();

        private SavePolicy mSavePolicy = new SavePolicy();

        private List<PolicyClassInfo> mOCSPResponseFinders = new List<PolicyClassInfo>(0);

        private List<PolicyClassInfo> mCertificateFinders = new List<PolicyClassInfo>(0);

        private List<PolicyClassInfo> mTrustedCertificateFinders = new List<PolicyClassInfo>(0);

        private List<PolicyClassInfo> mCrossCertificateFinders = new List<PolicyClassInfo>(0);

        private List<PolicyClassInfo> mCRLFinders = new List<PolicyClassInfo>(0);

        private List<PolicyClassInfo> mDeltaCRLFinders = new List<PolicyClassInfo>(0);

        /**
	     * Returns save policy
	     * @return
	     */
        public SavePolicy getSavePolicy()
        {
            return mSavePolicy;
        }

        /**
   	     * Set save policy
   	     * @param aSavePolicy
   	     */
        public void setSavePolicy(SavePolicy aSavePolicy)
        {
            mSavePolicy = aSavePolicy;
        }

        /**
	     * Returns matching policy
	     * @return
	     */
        public MatchingPolicy getMatchingPolicy()
        {
            return mMatchingPolicy;
        }

        /**
   	     * Set matching policy
   	     * @param aMatchingPolicy
   	     */
        public void setMatchingPolicy(MatchingPolicy aMatchingPolicy)
        {
            mMatchingPolicy = aMatchingPolicy;
        }

        /**
   	     * Add OCSP response finder to the OCSP response finders
   	     * @param aOCSPResponseFinder
   	     * @param aParametre
   	     */
        public void addOCSPResponseFinder(String aOCSPResponseFinder, Dictionary<String, Object> aParametre)
        {
            mOCSPResponseFinders.Add(new PolicyClassInfo(aOCSPResponseFinder, new ParameterList(aParametre)));
        }

        /**
   	     * Set OCSP response finders
   	     * @param aOCSPResponseFinders
   	     */
        public void setOCSPResponseFinders(List<PolicyClassInfo> aOCSPResponseFinders)
        {
            mOCSPResponseFinders = aOCSPResponseFinders;
        }

        /**
   	     * Add trusted certificate finder to the trusted certificate finders
   	     * @param aCertificateFinder
   	     * @param aParametre
   	     */
        public void addTrustedCertificateFinder(String aCertificateFinder, Dictionary<String, Object> aParametre)
        {
            mTrustedCertificateFinders.Add(new PolicyClassInfo(aCertificateFinder, new ParameterList(aParametre)));
        }

        /**
   	     * Set  trusted certificate finders
   	     * @param aTrustedCertFinders
   	     */
        public void setTrustedCertificateFinders(List<PolicyClassInfo> aTrustedCertFinders)
        {
            mTrustedCertificateFinders = aTrustedCertFinders;
        }

        /**
   	     * Add cross certificate finder to the cross certificate finders
   	     * @param aCrossCertificateFinder
   	     * @param aParametre
   	     */
        public void addCrossCertificateFinder(String aCrossCertificateFinder, Dictionary<String, Object> aParametre)
        {
            mCrossCertificateFinders.Add(new PolicyClassInfo(aCrossCertificateFinder, new ParameterList(aParametre)));
        }

        /**
   	     * Set  cross certificate finders
   	     * @param aCrossCertificateFinders
   	     */
        public void setCrossCertificateFinders(List<PolicyClassInfo> aCrossCertificateFinders)
        {
            mCrossCertificateFinders = aCrossCertificateFinders;
        }

        /**
   	     * Add certificate finder to the certificate finders
   	     * @param aCertificateFinders
   	     * @param aParametre
   	     */
        public void addCertificateFinder(String aCertificateFinders, Dictionary<String, Object> aParametre)
        {
            mCertificateFinders.Add(new PolicyClassInfo(aCertificateFinders, new ParameterList(aParametre)));
        }

        /**
   	     * Set  certificate finders
   	     * @param mCertificateFinders
   	     */
        public void setCertificateFinders(List<PolicyClassInfo> mCertificateFinders)
        {
            this.mCertificateFinders = mCertificateFinders;
        }

        /**
   	     * Add CRL finder to the CRL finders
   	     * @param aCRLFinder
   	     * @param aParametre
   	     */
        public void addCRLFinder(String aCRLFinder, Dictionary<String, Object> aParametre)
        {
            mCRLFinders.Add(new PolicyClassInfo(aCRLFinder, new ParameterList(aParametre)));
        }

        /**
   	     * Set  CRL finders
   	     * @param aCRLFinders
   	     */
        public void setCRLFinders(List<PolicyClassInfo> aCRLFinders)
        {
            mCRLFinders = aCRLFinders;
        }

        /**
   	     * Add delta CRL finder to the delta CRL finders
   	     * @param aDeltaCRLFinders
   	     * @param aParametre
   	     */
        public void addDeltaCRLFinder(String aDeltaCRLFinders, Dictionary<String, Object> aParametre)
        {
            mDeltaCRLFinders.Add(new PolicyClassInfo(aDeltaCRLFinders, new ParameterList(aParametre)));
        }

        /**
   	     * Set  delta CRL finders
   	     * @param aDeltaCRLFinders
   	     */
        public void setDeltaCRLFinders(List<PolicyClassInfo> aDeltaCRLFinders)
        {
            mDeltaCRLFinders = aDeltaCRLFinders;
        }

        /**
	     * Returns OCSP response finders
	     * @return
	     */
        public List<PolicyClassInfo> getOCSPResponseFinders()
        {
            return mOCSPResponseFinders;
        }

        /**
	     * Returns Certificate finders
	     * @return
	     */
        public List<PolicyClassInfo> getCertificateFinders()
        {
            return mCertificateFinders;
        }

        /**
	     * Returns CRL finders
	     * @return
	     */
        public List<PolicyClassInfo> getCRLFinders()
        {
            return mCRLFinders;
        }

        /**
	     * Returns delta CRL finders
	     * @return
	     */
        public List<PolicyClassInfo> getDeltaCRLFinders()
        {
            return mDeltaCRLFinders;
        }

        /**
	     * Returns trusted certificate finders
	     * @return
	     */
        public List<PolicyClassInfo> getTrustedCertificateFinders()
        {
            return mTrustedCertificateFinders;
        }

        /**
	     * Returns cross certificate finders
	     * @return
	     */
        public List<PolicyClassInfo> getCrossCertificateFinders()
        {
            return mCrossCertificateFinders;
        }

    }
}
