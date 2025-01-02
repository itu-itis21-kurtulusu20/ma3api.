package tr.gov.tubitak.uekae.esya.api.common.util;

import java.util.*;

/**
 * Hold pairs of Key (K) and List of Value (V) :
 * pair(K, List{@literal <V>})
 *
 * <p>Note that this class makes possible to keep multiple values for each key.
 *
 * <p>To assign a value for any key, use insert method.
 *
 * @author ayetgin
 */
public class MultiMap<K, V> extends HashMap<K, List<V>>
{
    /**
     * insert value into List that is paired with key K.
     * @param key which key this value belongs to
     * @param value one of values for key
     */
    public void insert(K key, V value){
       if (!containsKey(key)){
           put(key, new ArrayList<V>());
       }
        get(key).add(value);
    }

}
