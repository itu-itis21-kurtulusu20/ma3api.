using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.infra.cache
{
    public class FixedSizedCache<TKey, TValue>
    {
        private Dictionary<TKey, CacheItem<TValue>> dictionary;
        private List<TKey> keys; // Queue
        private int capacity;
        private TimeSpan staleTime;
        private readonly object dictionaryLock = new object();


        public FixedSizedCache(int capacity, TimeSpan staleTime)
        {
            this.keys = new List<TKey>(capacity);
            this.capacity = capacity;
            this.dictionary = new Dictionary<TKey, CacheItem<TValue>>(capacity);
            this.staleTime = staleTime;
        }

        public void Add(TKey key, TValue value)
        {
            lock (dictionaryLock)
            {
                if (dictionary.Count == capacity)
                {
                    var oldestKey = keys[0];
                    keys.RemoveAt(0);
                    dictionary.Remove(oldestKey);
                }

                if (dictionary.ContainsKey(key))
                {
                    dictionary.Remove(key);
                    keys.Remove(key);
                }

                CacheItem<TValue> item = new CacheItem<TValue>(value);
                dictionary.Add(key, item);
                keys.Add(key);
            }
        }

        public TValue Get(TKey key)
        {
            lock (dictionaryLock)
            {
                if (!dictionary.ContainsKey(key))
                {
                    return default(TValue);
                }

                CacheItem<TValue> item = dictionary[key];
                if (DateTime.UtcNow.Subtract(item.getTime()) > staleTime)
                {
                    dictionary.Remove(key);
                    return default(TValue);
                }

                return item.getItem();
            }
        }

        public TValue Get(TKey key, Func<TKey, TValue> retriever)
        { 
            TValue value = Get(key);
            if (value == null)
            {
                value = retriever(key);
                if (value != null)
                    Add(key, value);
            }

            return value;
        }
    }
}
