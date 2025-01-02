using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * Iteration for certificate finding 
     */
    class CRLFinderIteration : ItemFinderIteration<ECRL>
    {
        //private List<ItemSource<ECRL>> mSources = new List<ItemSource<ECRL>>();
        private int mSourceIndex;
        private readonly ECertificate mSubject;

        public CRLFinderIteration(ECertificate aSubject, ValidationSystem aValidationSystem)
        {
            mSubject = aSubject;
            mSourceIndex = -1;
            _initSources(aValidationSystem);
        }

        protected override void _initSources(ValidationSystem iDS)
        {
            // PREDEFINED
            mSources.Add(new ListItemSource<ECRL>(iDS.getUserInitialCRLSet()));
            // HISTORY
            mSources.Add(new ListItemSource<ECRL>(iDS.getCRLValidationCache().getCheckedCRLs()));
            // FOUNDLIST
            mSources.Add(new FinderCRLSource<CRLFinder>(mSubject, iDS.getFindSystem().getCRLFinders()));

        }

        protected override bool _nextSource(ValidationSystem iDS)
        {
            /*if (mSourceIndex < 0)
                _initSources(iDS);*/

            mSourceIndex++;
            if (mSourceIndex < mSources.Count)
                mCurrentSource = mSources[mSourceIndex];

            return (mSourceIndex < mSources.Count);
        }

        public bool nextIteration(ValidationSystem iDS)
        {
            MatchSystem es = iDS.getMatchSystem();

            while (base.nextIteration(iDS))
            {
                if (Find.isMatcingCRL(es, mSubject, mCurrentItem))
                    return true;
            }

            return false;
        }
        // public enum CRLSource
        // {

        //     PREDEFINED, HISTORY, TRUSTLIST, FOUNDLIST
        // }


        // protected CRLSource mSource = CRLSource.PREDEFINED;
        // protected int mIndex = -1;
        // protected int mFinderIndex = -1;

        // protected List<ECRL> mLastFound;

        // protected List<ECRL> _getCurrentList(ValidationSystem aValidationSystem)
        // {
        //     switch (mSource)
        //     {
        //         case CRLSource.PREDEFINED:
        //             return aValidationSystem.getUserInitialCRLSet();
        //         case CRLSource.HISTORY:
        //             return aValidationSystem.getCRLValidationCache().getCheckedCRLs(CRLStatus.VALID);
        //         case CRLSource.FOUNDLIST:
        //             return mLastFound;
        //         default:
        //             throw new /*Runtime*/Exception("Undefined List");
        //     }


        // }

        //override protected bool _nextSource(ValidationSystem aValidationSystem, ECertificate aCertificate)
        // {
        //     switch (mSource)
        //     {
        //         case CRLSource.PREDEFINED:
        //             {
        //                 mSource = CRLSource.HISTORY;
        //                 return true;
        //             }
        //         case CRLSource.HISTORY:
        //             {
        //                 mSource = CRLSource.FOUNDLIST;
        //                 return true;
        //             }
        //         case CRLSource.FOUNDLIST:
        //             {
        //                 mFinderIndex++;
        //                 while (mFinderIndex < aValidationSystem.getFindSystem().getCRLFinders().Count)
        //                 {
        //                     CRLFinder crlFinder = aValidationSystem.getFindSystem().getCRLFinders()[mFinderIndex];                            
        //                     if (crlFinder == null)
        //                         mFinderIndex++;
        //                     else
        //                     {
        //                         mLastFound = crlFinder.findCRL(aCertificate);
        //                         return true;
        //                     }
        //                 }
        //                 return false;
        //             }
        //     }
        //     return false;
        // }

        // override public bool nextIteration(ValidationSystem aValidationSystem, ECertificate aCertificate)
        // {
        //     MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        //     while (true)
        //     {
        //         mIndex++;
        //         List<ECRL> crlList = _getCurrentList(aValidationSystem);
        //         while (crlList != null && mIndex < crlList.Count)
        //         {
        //             if (Find.isMatcingCRL(matchSystem, aCertificate, crlList[mIndex]))
        //                 return true;
        //             mIndex++;
        //         }
        //         mIndex = -1;
        //         if (!_nextSource(aValidationSystem, aCertificate))
        //             return false;
        //     }
        // }

        // public ECRL getCRL(ValidationSystem aValidationSystem)
        // {
        //     List<ECRL> crlList = _getCurrentList(aValidationSystem);

        //     if (mIndex < crlList.Count)
        //         return crlList[mIndex];
        //     else throw new /*Runtime*/Exception("Iteration out of bounds");
        // }
    }
}
