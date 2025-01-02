

using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Threading;
using iaik.pkcs.pkcs11.wrapper;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class HSMPool
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        

        public static int DEFAULT_INITIAL_CAPACITY = 5;
        public static int DEFAULT_MAX_CAPACITY = 64;
        public static int DEFAULT_TIMEOUT_INSECONDS = 20;

        private Object lockObject = new Object();
        private Queue<P11SmartCard> hsmPool;
        private List<P11SmartCard> allCreatedObjects;

        CardType cardType;
        long slotNo;
        String pin;

        int initialCapacity;
        int maxCapacity;
        int timeoutInSeconds;

        int totalCount = 0;

        private HSMPool()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.AKILLIKART);
            }
            catch (LE ex)
            {
                throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public HSMPool(CardType cardType, long slotNo, String pin) : this(cardType, slotNo, pin, DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_CAPACITY, DEFAULT_TIMEOUT_INSECONDS)
        {
            
        }


        public HSMPool(CardType cardType, long slotNo, String pin, int initialCapacity, int maxCapacity, int timeoutInSeconds) : this()
        {
            this.cardType = cardType;
            this.slotNo = slotNo;
            this.pin = pin;
            this.initialCapacity = initialCapacity;
            this.maxCapacity = maxCapacity;
            this.timeoutInSeconds = timeoutInSeconds;
            initialize();
        }

        private void initialize()
        {
            hsmPool = new Queue<P11SmartCard>(maxCapacity);
            allCreatedObjects = new List<P11SmartCard>();
            for (int i = 0; i<initialCapacity; i++)
            {
                P11SmartCard sc = createPoolItem();
                hsmPool.Enqueue(sc);
            }
        }

        private P11SmartCard createPoolItem() 
        {
            P11SmartCard sc = new P11SmartCard(cardType);
            sc.openSession(slotNo);
            sc.login(pin);
            allCreatedObjects.Add(sc);
            totalCount++;
            return sc;
        }

        public P11SmartCard checkOutItem() 
        {
            lock (lockObject)
            {

                /*
                * Queue'dan bir tane HSM bağlantısı alıyoruz. Eğer session bilgisi alınamazsa HSM ile bağlantı yok diyebiliriz. O nesneyi kullanmıyoruz.
                * Yeni bir SmartCard nesnesi yaratıyoruz, doğrudan onu dışarı veriyoruz. Queue'deki diğer HSM nesnelerinin bağlantısı olmayabilir. Risk almıyoruz.
                * */
                P11SmartCard p11SmartCard = checkOutOneItem();
                try
                {
                    CK_SESSION_INFO sessionInfo =
                        p11SmartCard.getSmartCard().getSessionInfo(p11SmartCard.getSessionNo());
                    return p11SmartCard;
                }
                catch (PKCS11Exception e)
                {
                    try
                    {
                        allCreatedObjects.Remove(p11SmartCard);
                        totalCount--;
                        logger.Info("Can not get session info from pool object!" + e.Message);
                        return createPoolItem();
                    }
                    catch (Exception ex)
                    {
                        throw new ESYAException("Can not create pool item!", ex);
                    }
                }
            }
        }

        private P11SmartCard checkOutOneItem()
        {
            if (hsmPool.Count > 0)
                return hsmPool.Dequeue();

            if (totalCount < maxCapacity)
            {
                try
                {
                    P11SmartCard poolItem = createPoolItem();
                    return poolItem;
                }
                catch (Exception e)
                {
                    throw new ESYAException("Hsm item can not be created!", e);
                }
            }
            else
            {
                for(int i=0; i < timeoutInSeconds; i++)
                {
                    Thread.Sleep(1000);
                    if (hsmPool.Count > 0)
                        break;
                }

                if (hsmPool.Count > 0)
                    return hsmPool.Dequeue();
                else 
                    throw new ESYAException("Hsm item pool returns no HSM after timeout!");

            }
        }

        public void offer(P11SmartCard sc)
        {
            lock (lockObject)
            {
                hsmPool.Enqueue(sc);
            }
        }

        public long getSlotNo()
        {
            return slotNo;
        }

        ~HSMPool()
        {
            for (int i = 0; i < allCreatedObjects.Count; i++)
            {
                allCreatedObjects[i].closeSession();
            }
        }

    }
}
