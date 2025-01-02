package tr.gov.tubitak.uekae.esya.api.infra.cache;

import java.util.Calendar;

public class CacheItem<T> {

    T item;
    Calendar time;

    public CacheItem(T item) {
        this.item = item;
        this.time = Calendar.getInstance();
    }

    public T getItem() {
        return item;
    }

    public Calendar getTime() {
        return time;
    }

}
