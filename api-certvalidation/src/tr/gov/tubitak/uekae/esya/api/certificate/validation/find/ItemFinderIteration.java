package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public abstract class ItemFinderIteration<T>
{
    protected List<ItemSource<T>> mSources = new ArrayList<ItemSource<T>>();

    protected ItemSource<T> mCurrentSource;
    protected T mCurrentItem;

    public boolean nextIteration(ValidationSystem aValidationSystem) throws ESYAException
    {
        T pItem = null;

        if (mCurrentSource != null) {
            while ((pItem = mCurrentSource.nextItem()) != null) {
                mCurrentItem = pItem;
                return true;
            }
        }

        while (_nextSource(aValidationSystem)) {
            while ((pItem = mCurrentSource.nextItem())!=null) {
                mCurrentItem = pItem;
                return true;
            }
        }
        return false;
    }

    public T getCurrentItem()
    {
        if (mCurrentItem != null)
            return mCurrentItem;
        else throw new ESYARuntimeException("Iteration out of bounds");
    }

    public void addItemSource(ItemSource<T> aItemSource){
        mSources.add(aItemSource);
    }

    protected abstract void _initSources(ValidationSystem aValidationSystem);

    protected abstract boolean _nextSource(ValidationSystem aValidationSystem);

}
