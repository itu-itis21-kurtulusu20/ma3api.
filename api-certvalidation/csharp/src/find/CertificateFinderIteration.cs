using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
    * Iteration for certificate finding 
    */
    public class CertificateFinderIteration : ItemFinderIteration<ECertificate>
    {
        //protected  List<ItemSource<ECertificate>> mSources=new List<ItemSource<ECertificate>>();
        protected  int mSourceIndex;
        protected ECertificate mSubject;

        public CertificateFinderIteration(ECertificate aSubject, ValidationSystem aValidationSystem)
        {
            mSourceIndex = -1;
            mSubject = aSubject;
            _initSources(aValidationSystem);
        }

        protected override void _initSources(ValidationSystem iDS)
        {
            if (mSubject == null)
                return;

            // CA-CACHE
            mSources.Add(new ListItemSource<ECertificate>(iDS.getCachedCACertificates(mSubject.getIssuer().stringValue())));
            // USER-DEFINED
            mSources.Add(new ListItemSource<ECertificate>(iDS.getUserInitialCertificateSet()));
            // TRUSTED;
            mSources.Add(new ListItemSource<ECertificate>(iDS.getFindSystem().getTrustedCertificates()));
            // FINDERS
            mSources.Add(new FinderCertificateSource(mSubject, iDS.getFindSystem().getCertificateFinders()));

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

        public override bool nextIteration(ValidationSystem iDS)
        {
            MatchSystem es = iDS.getMatchSystem();

            while (base.nextIteration(iDS))
            {
                if (Find.isMatchingIssuer(es, mSubject, mCurrentItem))
                    return true;
            }

            return false;
        }

        //enum CertificateSource
        //{
        //    PREDEFINED, HISTORY, TRUSTLIST, FOUNDLIST
        //}

        //private CertificateSource mSource = CertificateSource.PREDEFINED;
        //private int mIndex = -1;
        //private int mFinderIndex = -1;

        //private List<ECertificate> mLastFound = new List<ECertificate>();


        //public CertificateFinderIteration()
        //{
        //}

        //protected List<ECertificate> _getCurrentList(ValidationSystem aValidationSystem)
        //{
        //    switch (mSource)
        //    {
        //        case CertificateSource.PREDEFINED:
        //            return aValidationSystem.getUserInitialCertificateSet();
        //        case CertificateSource.HISTORY:
        //            return aValidationSystem.getCertificateValidationCache().getCheckedCertificates();
        //        case CertificateSource.TRUSTLIST:
        //            return aValidationSystem.getFindSystem().getTrustedCertificates();
        //        case CertificateSource.FOUNDLIST:
        //            return mLastFound;
        //        default:
        //            throw new /*Runtime*/Exception("Undefined List");
        //    }
        //}

        //protected bool _nextSource(ValidationSystem aValidationSystem, ECertificate aCertificate)
        //{
        //    switch (mSource)
        //    {
        //        case CertificateSource.PREDEFINED:
        //            {
        //                mSource = CertificateSource.HISTORY;
        //                return true;
        //            }
        //        case CertificateSource.HISTORY:
        //            {
        //                mSource = CertificateSource.TRUSTLIST;
        //                return true;
        //            }
        //        case CertificateSource.TRUSTLIST:
        //            {
        //                mSource = CertificateSource.FOUNDLIST;
        //                return true;
        //            }
        //        case CertificateSource.FOUNDLIST:
        //            {
        //                mFinderIndex++;
        //                while (mFinderIndex < aValidationSystem.getFindSystem().getCertificateFinders().Count)
        //                {
        //                    CertificateFinder pSB = aValidationSystem.getFindSystem().getCertificateFinders()[mFinderIndex];
        //                    if (pSB == null)
        //                        mFinderIndex++;
        //                    else
        //                    {
        //                        mLastFound = pSB.findCertificate(aCertificate);
        //                        return true;
        //                    }
        //                }
        //                return false;
        //            }
        //    }
        //    return false;
        //}

        //public bool nextIteration(ValidationSystem aValidationSystem, ECertificate aCertificate)
        //{
        //    MatchSystem matchSystem = aValidationSystem.getMatchSystem();

        //    while (true)
        //    {
        //        mIndex++;
        //        List<ECertificate> certList = _getCurrentList(aValidationSystem);
        //        while (mIndex < certList.Count)
        //        {
        //            if (Find.isMatchingIssuer(matchSystem, aCertificate, certList[mIndex]))
        //                return true;
        //            mIndex++;
        //        }
        //        mIndex = -1;
        //        if (!_nextSource(aValidationSystem, aCertificate))
        //            return false;
        //    }
        //}

        //public ECertificate getCertificate(ValidationSystem aValidationSystem)
        //{
        //    List<ECertificate> certList = _getCurrentList(aValidationSystem);

        //    if (mIndex < certList.Count)
        //        return certList[mIndex];
        //    else throw new /*Runtime*/Exception("Iteration out of bounds");
        //}

    }
}
