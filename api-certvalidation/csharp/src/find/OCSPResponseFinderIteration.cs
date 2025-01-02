using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * @author ayetgin
     */
    public class OCSPResponseFinderIteration : ItemFinderIteration<EOCSPResponse>
    {
        protected int mSourceIndex;

        private readonly ECertificate mSubject;
        private readonly ECertificate mIssuer;


        public OCSPResponseFinderIteration(ECertificate aCertificate, ECertificate aIssuer, ValidationSystem aValidationSystem)
        {
            mSubject = aCertificate;
            mIssuer = aIssuer;
            mSourceIndex = -1;
            _initSources(aValidationSystem);
        }

        protected override void _initSources(ValidationSystem aDS)
        {
            // PREDEFINED
            mSources.Insert(0, new ListItemSource<EOCSPResponse>(aDS.getUserInitialOCSPResponseSet()));
            // HISTORY
            // todo
            // FOUNDLIST
            mSources.Add(new FinderOCSPResponseSource<OCSPResponseFinder>(mSubject, mIssuer, aDS.getFindSystem().getOCSPResponseFinders()));
        }

        protected override bool _nextSource(ValidationSystem aDS)
        {
            /*if (mSourceIndex<0)
                _initSources(aDS);*/

            mSourceIndex++;
            if (mSourceIndex < mSources.Count)
                mCurrentSource = mSources[mSourceIndex];

            return (mSourceIndex < mSources.Count);
        }

        public bool nextIteration(ValidationSystem aDS)
        {
            MatchSystem es = aDS.getMatchSystem();

            while (base.nextIteration(aDS))
            {
                if (Find.isMatchingOCSPResponse(es, mSubject, mIssuer, mCurrentItem))
                    return true;
            }

            return false;
        }
    }
}
