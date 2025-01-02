using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find
{
    /**
     * @author ayetgin
     */
    public abstract class ItemFinderIteration<T> where T : class
    {
        protected List<ItemSource<T>> mSources = new List<ItemSource<T>>();

        protected ItemSource<T> mCurrentSource;
        protected T mCurrentItem;

        public virtual bool nextIteration(ValidationSystem aValidationSystem)
        {
            T pItem = null;
            //T pItem = default(T);

            if (mCurrentSource != null)
            {
                while ((pItem = mCurrentSource.nextItem()) != null)
                {
                    mCurrentItem = pItem;
                    return true;
                }
            }

            while (_nextSource(aValidationSystem))
            {
                while ((pItem = mCurrentSource.nextItem()) != null)
                {
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

        public void addItemSource(ItemSource<T> aItemSource)
        {
            mSources.Add(aItemSource);
        }

        protected abstract void _initSources(ValidationSystem aValidationSystem);

        protected abstract bool _nextSource(ValidationSystem aValidationSystem);
    }
}