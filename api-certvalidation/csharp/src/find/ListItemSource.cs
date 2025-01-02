using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * @author ayetgin
     */
    public class ListItemSource<T> : ItemSource<T> where T: class 
    {
        readonly List<T> mItems;
        int mIndex = 0;

        public ListItemSource(List<T> aItems)
        {
            mItems = aItems;
        }

        public bool open()
        {
            reset();
            return true;
        }

        public bool close()
        {
            return true;
        }

        public bool reset()
        {
            mIndex = 0;
            return true;
        }

        public T nextItem()
        {
            if (atEnd())
                return default(T);

            T t = mItems[mIndex];
            mIndex++;
            return t;
        }

        public bool atEnd()
        {
            return (mItems == null) || (mIndex >= mItems.Count);
        }
    }
}
