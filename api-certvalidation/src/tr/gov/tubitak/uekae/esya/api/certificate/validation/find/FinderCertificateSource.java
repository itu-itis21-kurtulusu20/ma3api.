package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.CertificateFinder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;

import java.util.List;

/**
 * @author ayetgin
 */
public class FinderCertificateSource implements ItemSource<ECertificate>
{
    ECertificate mSubject;
	List<CertificateFinder> mFinders;
	ItemSource<ECertificate> mCurrentSource;
    int mCurrentIndex;

    public FinderCertificateSource(ECertificate aSubject, List<CertificateFinder> aFinders)
    {     
        mSubject = aSubject;
        mFinders = aFinders;
        mCurrentIndex = -1;
        mCurrentSource = null;
    }

    public boolean open()
    {
        return reset();
    }

    public boolean close()
    {
        return true;
    }

    public boolean reset()
    {
        mCurrentIndex = -1;
        return true;
    }

    public ECertificate nextItem() throws ESYAException
    {
        ECertificate pItem = null;
        if ((mCurrentSource!=null) && ((pItem = mCurrentSource.nextItem())!=null))
            return pItem;

        while(true)
        {
            if ((++mCurrentIndex)>=mFinders.size()) return null;

            mCurrentSource = null;

            mCurrentSource = mFinders.get(mCurrentIndex).findCertificateSource(mSubject);

            if ((mCurrentSource!=null) && ((pItem = mCurrentSource.nextItem())!=null))
                return pItem;
        }

    }

    public boolean atEnd()
    {
        return (mCurrentIndex>=mFinders.size());
    }

}
