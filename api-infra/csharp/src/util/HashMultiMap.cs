using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.infra.util
{
    public class HashMultiMap<K, V>
    {
        readonly Dictionary<K, List<V>> map;

        public HashMultiMap()
        {
            map = new Dictionary<K, List<V>>();
        }

        public void put(K aKey, V aValue)
        {
            List<V> list = null;
            map.TryGetValue(aKey, out list);
            if (list == null)
            {
                list = new List<V>();
                map[aKey] = list;
            }
            list.Add(aValue);
        }

        public List<V> get(K aKey)
        {
            List<V> list = null;
            map.TryGetValue(aKey, out list);
            return list;

        }

        public bool remove(K key)
        {
            return map.Remove(key);
        }
    }
}
