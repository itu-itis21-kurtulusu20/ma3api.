
using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class MultiSlotHSMPoolContainer
    {
        private Dictionary<long, HSMPool> multiSlotHSMPool;
        private Object dictionaryLock;

        public MultiSlotHSMPoolContainer()
        {
            multiSlotHSMPool = new Dictionary<long, HSMPool>();
            dictionaryLock = new object();
        }

        public void addHSMPool(HSMPool pool)
        {
            lock (dictionaryLock)
            {
                multiSlotHSMPool[pool.getSlotNo()] = pool;
            }
            
        }

        public P11SmartCard checkOutItem(long slot)
        {
            HSMPool hsmPool;
            lock (dictionaryLock)
            {
                hsmPool = multiSlotHSMPool[slot];
            }

            return hsmPool.checkOutItem();
        }

        public void offer(P11SmartCard sc)
        {
            HSMPool hsmPool;
            lock (dictionaryLock)
            {
                hsmPool = multiSlotHSMPool[sc.getSlotNo()];
            }
            hsmPool.offer(sc);
        }

    }
}
