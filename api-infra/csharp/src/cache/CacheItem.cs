using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.cache
{
    public class CacheItem<T>
    {
        T item;
        DateTime time;

        public CacheItem(T item)
        {
            this.item = item;
            this.time = DateTime.UtcNow;
        }

        public T getItem()
        {
            return item;
        }

        public DateTime getTime()
        {
            return time;
        }
    }
}
