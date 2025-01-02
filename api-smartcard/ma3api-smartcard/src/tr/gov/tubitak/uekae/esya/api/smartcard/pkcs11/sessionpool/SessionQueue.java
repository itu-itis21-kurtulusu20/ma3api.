package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionQueue extends  ArrayBlockingQueue<ObjectSessionInfo> {

    protected AtomicInteger activeSessionCount = new AtomicInteger(0);

    public SessionQueue(int maxCapacity) {
        super(maxCapacity);
    }

    public int getActiveSessionCount() {
        return activeSessionCount.get();
    }

    public synchronized void increaseActiveSessionCount() {
        activeSessionCount.incrementAndGet();
    }

    public synchronized void decreaseActiveSessionCount() {
        activeSessionCount.decrementAndGet();
    }
}
