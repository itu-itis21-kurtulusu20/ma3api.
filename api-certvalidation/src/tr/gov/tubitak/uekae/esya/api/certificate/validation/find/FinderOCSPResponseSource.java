package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp.OCSPResponseFinder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;

import java.util.List;

/**
 * @author ayetgin
 */
public class FinderOCSPResponseSource implements ItemSource<EOCSPResponse>
{
    private ECertificate mSubject, mIssuer;
    private List<? extends Finder> mFinders;
    ItemSource<EOCSPResponse> mCurrentSource;
    int mCurrentIndex;

    public FinderOCSPResponseSource(ECertificate aSubject, ECertificate aIssuer, List<? extends Finder> aFinders)
    {
        mSubject= aSubject;
        mIssuer = aIssuer;
        mFinders = aFinders;
        mCurrentIndex=-1;
        mCurrentSource=null;
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

    public EOCSPResponse nextItem() throws ESYAException
    {
        EOCSPResponse pItem = null;
        if ((mCurrentSource!=null) && ((pItem = mCurrentSource.nextItem())!=null))
            return pItem;

        while(true)
        {
            if ((++mCurrentIndex)>=mFinders.size()) return null;

            mCurrentSource = null;

            mCurrentSource = ((OCSPResponseFinder)mFinders.get(mCurrentIndex)).findOCSPSource(mSubject, mIssuer);

            if ((mCurrentSource!=null) && ((pItem = mCurrentSource.nextItem())!=null))
                return pItem;
        }

    }

    public boolean atEnd()
    {
        return (mCurrentIndex>=mFinders.size());
    }

}
