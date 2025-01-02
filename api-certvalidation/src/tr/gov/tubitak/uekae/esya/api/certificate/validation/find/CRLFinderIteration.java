package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.MatchSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Iteration for certificate finding 
 */
public class CRLFinderIteration extends ItemFinderIteration<ECRL>
{
    private int mSourceIndex;
    private ECertificate mSubject;

    public CRLFinderIteration(ECertificate aSubject, ValidationSystem aValidationSystem)
    {
        super();
        mSubject = aSubject;
        mSourceIndex = -1;
        _initSources(aValidationSystem);
    }

    protected void _initSources(ValidationSystem iDS)
    {
        // PREDEFINED
        mSources.add(new ListItemSource<ECRL>(iDS.getUserInitialCRLSet()));
        // HISTORY
        mSources.add(new ListItemSource<ECRL>(iDS.getCRLValidationCache().getCheckedCRLs()));
        // FOUNDLIST
        mSources.add(new FinderCRLSource(mSubject,iDS.getFindSystem().getCRLFinders()));

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
            if (Find.isMatcingCRL(es, mSubject, mCurrentItem))
                return true;
        }

        return false;
    }

    /*
    public enum CRLSource
    {

        PREDEFINED, HISTORY, TRUSTLIST, FOUNDLIST
    }

    
    protected CRLSource mSource = CRLSource.PREDEFINED;
    protected int mIndex = -1;
    protected int mFinderIndex = -1;

    protected List<ECRL> mLastFound;

    protected List<ECRL> _getCurrentList(ValidationSystem aValidationSystem)
    {
        switch (mSource) {
            case PREDEFINED:
                return aValidationSystem.getUserInitialCRLSet();
            case HISTORY:
                return aValidationSystem.getCRLValidationCache().getCheckedCRLs(CRLStatus.VALID);
            case FOUNDLIST:
                return mLastFound;
            default:
                throw new RuntimeException("Undefined List");
        }
    }

    protected boolean _nextSource(ValidationSystem aValidationSystem, ECertificate aCertificate)
            throws ESYAException
    {
        switch (mSource) {
            case PREDEFINED: {
                mSource = CRLSource.HISTORY;
                return true;
            }
            case HISTORY: {
                mSource = CRLSource.FOUNDLIST;
                return true;
            }
            case FOUNDLIST: {
                mFinderIndex++;
                while (mFinderIndex < aValidationSystem.getFindSystem().getCRLFinders().size()) {
                    CRLFinder crlFinder = aValidationSystem.getFindSystem().getCRLFinders().get(mFinderIndex);
                    if (crlFinder == null)
                        mFinderIndex++;
                    else {
                        mLastFound = crlFinder.findCRL(aCertificate);
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public boolean nextIteration(ValidationSystem aValidationSystem, ECertificate aCertificate)
            throws ESYAException
    {
        MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        while (true) {
            mIndex++;
            List<ECRL> crlList = _getCurrentList(aValidationSystem);
            while (crlList!=null && mIndex < crlList.size()) {
                if (Find.isMatcingCRL(matchSystem, aCertificate, crlList.get(mIndex)))
                    return true;
                mIndex++;
            }
            mIndex = -1;
            if (!_nextSource(aValidationSystem, aCertificate))
                return false;
        }
    }

    public ECRL getCRL(ValidationSystem aValidationSystem)
    {
        List<ECRL> crlList = _getCurrentList(aValidationSystem);

        if (mIndex < crlList.size())
            return crlList.get(mIndex);
        else throw new RuntimeException("Iteration out of bounds");
    }
    */
}
