using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * @author ayetgin
     */
    public class  FinderCertificateSource : ItemSource<ECertificate>
    {
        readonly ECertificate mSubject;
        readonly List<CertificateFinder> mFinders;
        ItemSource<ECertificate> mCurrentSource;
        int mCurrentIndex;

        public FinderCertificateSource(ECertificate aSubject, List<CertificateFinder> aFinders)
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

        public ECertificate nextItem()
        {
            ECertificate pItem = null;
            if ((mCurrentSource != null) && ((pItem = mCurrentSource.nextItem()) != null))
                return pItem;

            while (true)
            {
                if ((++mCurrentIndex) >= mFinders.Count) return null;

                mCurrentSource = null;

                mCurrentSource = mFinders[mCurrentIndex].findCertificateSource(mSubject);

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
