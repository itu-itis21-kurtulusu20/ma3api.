using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    public class FinderCRLSource<T> : ItemSource<ECRL> where T : Finder
    {
        private readonly ECertificate mSubject;
        private readonly List<T> mFinders;

        ItemSource<ECRL> mCurrentSource;
        int mCurrentIndex;

        public FinderCRLSource(ECertificate aSubject, List<T> aFinders)
        {
            mSubject = aSubject;
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

        public ECRL nextItem()
        {
            ECRL pItem = null;
            if ((mCurrentSource != null) && ((pItem = mCurrentSource.nextItem()) != null))
                return pItem;

            while (true)
            {
                if ((++mCurrentIndex) >= mFinders.Count) return null;

                mCurrentSource = null;

                //mCurrentSource = mFinders[mCurrentIndex].findCRLSource(mSubject);
                try
                {
                    mCurrentSource = (mFinders[mCurrentIndex] as CRLFinder).findCRLSource(mSubject);
                }
                catch(Exception x)
                {
                    Console.WriteLine(x.StackTrace);
                }

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
