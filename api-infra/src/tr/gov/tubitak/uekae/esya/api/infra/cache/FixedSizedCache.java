package tr.gov.tubitak.uekae.esya.api.infra.cache;

import java.time.Duration;
import java.util.*;

public class FixedSizedCache<K,V> {

    private final int maxSize;
    private final Duration staleTime;
    private final Object lock;

    private HashMap<K,CacheItem<V>> cache = new LinkedHashMap() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxSize;
        }
    };

    public FixedSizedCache(int maxSize, Duration staleTime) {
        this.maxSize = maxSize;
        this.staleTime = staleTime;
        this.lock = new Object();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getEntryCount() {
        return cache.size();
    }

    public V getItem(K key) {
        synchronized (lock) {
            CacheItem<V> item = cache.get(key);
            if( item == null )
                return null;
            Calendar time = item.getTime();
            Duration duration = Duration.between(time.toInstant(), Calendar.getInstance().toInstant());
            if(staleTime.compareTo(duration) > 0) {
                return item.getItem();
            } else {
                cache.remove(key);
                return null;
            }
        }
    }

    /**
     * Cache'de veri bulunuyorsa ilgili veri dönüyor, bulunmuyor ise retriever ile veri çekiliyor ve cache'e konuluyor.
     * @param key
     * @param retriever
     * @return
     * @throws Exception
     */
    public V getItem(K key, CacheItemRetriever<V> retriever) throws Exception {
        V item = getItem(key);
        if(item == null){
            item = retriever.getItem();
            if(item != null) {
                put(key, item);
            }
        }
        return item;
    }

    public void put(K key, V item) {
        synchronized (lock) {
            CacheItem<V> cacheItem = new CacheItem<>(item);
            cache.put(key, cacheItem);
        }
    }

    @FunctionalInterface
    public interface CacheItemRetriever<V>{
        V getItem() throws Exception;
    }
}
