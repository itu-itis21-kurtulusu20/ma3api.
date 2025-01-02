package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.MatchSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * @author ayetgin
 */
public class OCSPResponseFinderIteration extends ItemFinderIteration<EOCSPResponse>
{
    protected int mSourceIndex;

    private ECertificate mSubject;
    private ECertificate mIssuer;


    public OCSPResponseFinderIteration(ECertificate aCertificate, ECertificate aIssuer, ValidationSystem aValidationSystem)
    {
        mSubject = aCertificate;
        mIssuer = aIssuer;
        mSourceIndex = -1;
        _initSources(aValidationSystem);
    }

    protected void _initSources(ValidationSystem aDS)
    {
        // PREDEFINED
        mSources.add(0, new ListItemSource<EOCSPResponse>(aDS.getUserInitialOCSPResponseSet()));
        // HISTORY
        // todo
        // FOUNDLIST
        mSources.add(new FinderOCSPResponseSource(mSubject, mIssuer, aDS.getFindSystem().getOCSPResponseFinders()));
    }

    @Override
    protected boolean _nextSource(ValidationSystem aDS)
    {
        /*if (mSourceIndex<0)
            _initSources(aDS);*/

        mSourceIndex++;
        if ( mSourceIndex<mSources.size() )
            mCurrentSource = mSources.get(mSourceIndex);

        return (mSourceIndex<mSources.size());
    }

    public boolean nextIteration(ValidationSystem aDS  ) throws ESYAException
    {
        MatchSystem es = aDS.getMatchSystem();

        while(super.nextIteration(aDS))
        {
            if (Find.isMatchingOCSPResponse(es,mSubject,mIssuer,mCurrentItem))
                return true;
        }

        return false;
    }

}
