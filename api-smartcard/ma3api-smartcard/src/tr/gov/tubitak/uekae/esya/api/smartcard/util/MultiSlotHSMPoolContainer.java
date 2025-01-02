package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;
import java.util.concurrent.ConcurrentHashMap;

public class MultiSlotHSMPoolContainer
{
    public ConcurrentHashMap<Long, HSMPool> multiSlotHSMPool;

    public MultiSlotHSMPoolContainer()
    {
        multiSlotHSMPool = new ConcurrentHashMap<>();
    }

    public void addHSMPool(HSMPool pool)
    {
        multiSlotHSMPool.put(pool.getSlotNo(), pool);
    }

    public P11SmartCard checkOutItem(long slot) throws ESYAException
    {
        return multiSlotHSMPool.get(slot).checkOutItem();
    }

    public void offer(P11SmartCard sc)
    {
        multiSlotHSMPool.get(sc.getSlotNo()).offer(sc);
    }
}
