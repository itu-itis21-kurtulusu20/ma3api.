package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.MatchingPolicy;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyClassInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.SavePolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Specifies the finder classes that will be used during validation. * <p>
 * 
 * @author IH
 */
public class FindingPolicy
{
    private MatchingPolicy mMatchingPolicy = new MatchingPolicy();

    private SavePolicy mSavePolicy = new SavePolicy();

    private List<PolicyClassInfo> mOCSPResponseFinders = new ArrayList<PolicyClassInfo>(0);

    private List<PolicyClassInfo> mCertificateFinders = new ArrayList<PolicyClassInfo>(0);

    private List<PolicyClassInfo> mTrustedCertificateFinders = new ArrayList<PolicyClassInfo>(0);

    private List<PolicyClassInfo> mCrossCertificateFinders = new ArrayList<PolicyClassInfo>(0);

    private List<PolicyClassInfo> mCRLFinders = new ArrayList<PolicyClassInfo>(0);

    private List<PolicyClassInfo> mDeltaCRLFinders = new ArrayList<PolicyClassInfo>(0);

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
    public void addOCSPResponseFinder(String aOCSPResponseFinder, Map<String, Object> aParametre)
    {
        mOCSPResponseFinders.add(new PolicyClassInfo(aOCSPResponseFinder, new ParameterList(aParametre)));
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
    public void addTrustedCertificateFinder(String aCertificateFinder, Map<String, Object> aParametre)
    {
        mTrustedCertificateFinders.add(new PolicyClassInfo(aCertificateFinder, new ParameterList(aParametre)));
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
    public void addCrossCertificateFinder(String aCrossCertificateFinder, Map<String, Object> aParametre)
    {
        mCrossCertificateFinders.add(new PolicyClassInfo(aCrossCertificateFinder, new ParameterList(aParametre)));
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
    public void addCertificateFinder(String aCertificateFinders, Map<String, Object> aParametre)
    {
        mCertificateFinders.add(new PolicyClassInfo(aCertificateFinders, new ParameterList(aParametre)));
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
    public void addCRLFinder(String aCRLFinder, Map<String, Object> aParametre)
    {
        mCRLFinders.add(new PolicyClassInfo(aCRLFinder, new ParameterList(aParametre)));
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
    public void addDeltaCRLFinder(String aDeltaCRLFinders, Map<String, Object> aParametre)
    {
        mDeltaCRLFinders.add(new PolicyClassInfo(aDeltaCRLFinders, new ParameterList(aParametre)));
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
