package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.MatchSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Iteration for certificate finding 
 */
public class CertificateFinderIteration extends ItemFinderIteration<ECertificate>
{
    protected int mSourceIndex;
    protected ECertificate mSubject;

    public CertificateFinderIteration(ECertificate aSubject, ValidationSystem aValidationSystem)
    {
        mSourceIndex = -1;
        mSubject = aSubject;
        _initSources(aValidationSystem);
    }

    protected void _initSources(ValidationSystem iDS)
    {
        if (mSubject ==null)
            return;
        // CA-CACHE
        mSources.add(new ListItemSource<ECertificate>(iDS.getCachedCACertificates(mSubject.getIssuer().stringValue())));
        // USER-DEFINED
        mSources.add(new ListItemSource<ECertificate>(iDS.getUserInitialCertificateSet()));
        // TRUSTED;
        mSources.add(new ListItemSource<ECertificate>(iDS.getFindSystem().getTrustedCertificates())) ;
        // FINDERS
        mSources.add(new FinderCertificateSource(mSubject,iDS.getFindSystem().getCertificateFinders()));

    }

    public boolean _nextSource(ValidationSystem iDS)
    {
        /*if (mSourceIndex<0)
            _initSources(iDS);*/

        mSourceIndex++;
        if ( mSourceIndex<mSources.size() )
            mCurrentSource = mSources.get(mSourceIndex);

        return (mSourceIndex<mSources.size());

    }

    public boolean nextIteration(ValidationSystem iDS  ) throws ESYAException
    {
        MatchSystem es = iDS.getMatchSystem();

        while(super.nextIteration(iDS))
        {
            if (Find.isMatchingIssuer(es,mSubject,mCurrentItem))
                return true;
        }

        return false;
    }

    /*
    enum CertificateSource
    {
        PREDEFINED, HISTORY, TRUSTLIST, FOUNDLIST
    }


    private CertificateSource mSource = CertificateSource.PREDEFINED;
    private int mIndex = -1;
    private int mFinderIndex = -1;

    private List<ECertificate> mLastFound = new ArrayList<ECertificate>();


    public CertificateFinderIteration()
    {
    }

    protected List<ECertificate> _getCurrentList(ValidationSystem aValidationSystem)
    {
        switch (mSource) {
            case PREDEFINED:
                return aValidationSystem.getUserInitialCertificateSet();
            case HISTORY:
                return aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
            case TRUSTLIST:
                return aValidationSystem.getFindSystem().getTrustedCertificates();
            case FOUNDLIST:
                return mLastFound;
            default:
                throw new RuntimeException("Undefined List");
        }
    }

    protected boolean _nextSource(ValidationSystem aValidationSystem, ECertificate aCertificate)
    {
        switch (mSource) {
            case PREDEFINED: {
                mSource = CertificateSource.HISTORY;
                return true;
            }
            case HISTORY: {
                mSource = CertificateSource.TRUSTLIST;
                return true;
            }
            case TRUSTLIST: {
                mSource = CertificateSource.FOUNDLIST;
                return true;
            }
            case FOUNDLIST: {
                mFinderIndex++;
                while (mFinderIndex < aValidationSystem.getFindSystem().getCertificateFinders().size()) {
                    CertificateFinder pSB = aValidationSystem.getFindSystem().getCertificateFinders().get(mFinderIndex);
                    if (pSB==null)
                        mFinderIndex++;
                    else {
                        mLastFound = pSB.findCertificate(aCertificate);
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public boolean nextIteration(ValidationSystem aValidationSystem, ECertificate aCertificate)
    {
        MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        while (true) {
            mIndex++;
            List<ECertificate> certList = _getCurrentList(aValidationSystem);
            if (certList!=null){
                while (mIndex < certList.size()) {
                    if (Find.isMatchingIssuer(matchSystem, aCertificate, certList.get(mIndex)))
                        return true;
                    mIndex++;
                }
            }
            mIndex = -1;
            if (!_nextSource(aValidationSystem, aCertificate))
                return false;
        }
    }

    public ECertificate getCertificate(ValidationSystem aValidationSystem)
    {
        List<ECertificate> certList = _getCurrentList(aValidationSystem);

        if (mIndex < certList.size())
            return certList.get(mIndex);
        else throw new RuntimeException("Iteration out of bounds");
    }
    */
}
