package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.CRLFinder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FinderCRLSource implements ItemSource<ECRL>
{
    protected static Logger logger = LoggerFactory.getLogger(FinderCRLSource.class);
    private ECertificate mSubject;
    private List<? extends Finder> mFinders;
    ItemSource<ECRL> mCurrentSource;
    int mCurrentIndex;

    public FinderCRLSource(ECertificate aSubject, List<? extends Finder> aFinders)
    {
        mSubject= aSubject;
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

    public ECRL nextItem() throws ESYAException
    {
        ECRL pItem = null;
        if ((mCurrentSource!=null) && ((pItem = mCurrentSource.nextItem())!=null))
            return pItem;

        while(true)
        {
            if ((++mCurrentIndex)>=mFinders.size()) return null;

            mCurrentSource = null;

            try {
                mCurrentSource = ((CRLFinder)mFinders.get(mCurrentIndex)).findCRLSource(mSubject);
            } catch (Exception x){
                logger.error("Error in FinderCRLSource", x);
            }

            if ((mCurrentSource!=null) && ((pItem = mCurrentSource.nextItem())!=null))
                return pItem;
        }

    }

    public boolean atEnd()
    {
        return (mCurrentIndex>=mFinders.size());
    }

}
