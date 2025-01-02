package tr.gov.tubitak.uekae.esya.api.certificate.validation.find;

import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;

import java.util.List;

/**
 * @author ayetgin
 */
public class ListItemSource<T> implements ItemSource<T>
{
    List<T> mItems;
    int mIndex = 0;

    public ListItemSource(List<T> aItems)
    {
        mItems = aItems;
    }

    public boolean open()
    {
        reset();
        return true;
    }

    public boolean close()
    {
        return true;
    }

    public boolean reset()
    {
        mIndex=0;
        return true;
    }

    public T nextItem()
    {
        if (atEnd())
            return null;
        
        T t =  mItems.get(mIndex);
        mIndex++;
        return t;
    }

    public boolean atEnd()
    {
        return (mItems==null) || (mIndex>=mItems.size());
    }
}
