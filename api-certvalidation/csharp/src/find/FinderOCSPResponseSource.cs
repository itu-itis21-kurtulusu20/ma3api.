using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * @author ayetgin
     */
    public class FinderOCSPResponseSource<T> : ItemSource<EOCSPResponse> where T : Finder
    {
        private readonly ECertificate mSubject;
        private readonly ECertificate mIssuer;
        private readonly List<T> mFinders;
        private ItemSource<EOCSPResponse> mCurrentSource;
        private int mCurrentIndex;

        public FinderOCSPResponseSource(ECertificate aSubject, ECertificate aIssuer, List<T> aFinders)
        {
            mSubject = aSubject;
            mIssuer = aIssuer;
            mFinders = aFinders;
            mCurrentIndex = -1;
            mCurrentSource = null;
        }

        public bool open()
        {
            return reset();
        }

        public bool close()
        {
            return true;
        }

        public bool reset()
        {
            mCurrentIndex = -1;
            return true;
        }

        public EOCSPResponse nextItem()
        {
            EOCSPResponse pItem = null;
            if ((mCurrentSource != null) && ((pItem = mCurrentSource.nextItem()) != null))
                return pItem;

            while (true)
            {
                if ((++mCurrentIndex) >= mFinders.Count) return null;

                mCurrentSource = null;

                //mCurrentSource = ((OCSPResponseFinder)mFinders[mCurrentIndex]).findOCSPSource(mSubject, mIssuer);

                mCurrentSource = (mFinders[mCurrentIndex] as OCSPResponseFinder).findOCSPSource(mSubject, mIssuer);

                if ((mCurrentSource != null) && ((pItem = mCurrentSource.nextItem()) != null))
                    return pItem;
            }
        }

        public bool atEnd()
        {
            return (mCurrentIndex >= mFinders.Count);
        }
    }
}