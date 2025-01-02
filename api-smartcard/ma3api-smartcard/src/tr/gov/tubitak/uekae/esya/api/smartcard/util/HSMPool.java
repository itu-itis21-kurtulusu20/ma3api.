package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_SESSION_INFO;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.LoginException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class HSMPool
{
    private static Logger logger = LoggerFactory.getLogger(HSMPool.class);

    public static int DEFAULT_INITIAL_CAPACITY = 5;
    public static int DEFAULT_MAX_CAPACITY = 64;
    public static int DEFAULT_TIMEOUT_INSECONDS = 20;

    private ArrayBlockingQueue<P11SmartCard> hsmPool;
    private ArrayList<P11SmartCard> allCreatedObjects;

    CardType cardType;
    long slotNo;
    String pin;

    int initialCapacity;
    int maxCapacity;
    int timeoutInSeconds;

    int totalCount = 0;

    private HSMPool() {
        try {
            LV.getInstance().checkLD(LV.Urunler.AKILLIKART);
        } catch (LE e) {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
    }

    public HSMPool(CardType cardType, long slotNo, String pin) throws PKCS11Exception, IOException, LoginException, SmartCardException {
        this(cardType, slotNo, pin, DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_CAPACITY, DEFAULT_TIMEOUT_INSECONDS);
    }

    public HSMPool(CardType cardType, long slotNo, String pin, int initialCapacity, int maxCapacity, int timeoutInSeconds) throws LoginException, PKCS11Exception, SmartCardException, IOException {
        this();
        this.cardType = cardType;
        this.slotNo = slotNo;
        this.pin = pin;
        this.initialCapacity = initialCapacity;
        this.maxCapacity = maxCapacity;
        this.timeoutInSeconds = timeoutInSeconds;
        initialize();
    }

    private void initialize() throws PKCS11Exception, IOException, SmartCardException, LoginException
    {
        allCreatedObjects = new ArrayList<>();
        hsmPool = new ArrayBlockingQueue<P11SmartCard>(maxCapacity);
        for(int i=0; i < initialCapacity; i++)
        {
            P11SmartCard sc = createPoolItem();
            hsmPool.add(sc);
        }
    }

    private P11SmartCard createPoolItem() throws PKCS11Exception, IOException, SmartCardException, LoginException
    {
        P11SmartCard sc = new P11SmartCard(cardType);
        sc.openSession(slotNo);
        sc.login(pin);
        allCreatedObjects.add(sc);
        totalCount++;
        return sc;
    }

    public synchronized P11SmartCard checkOutItem() throws ESYAException
    {
        /*
        * Queue'dan bir tane HSM bağlantısı alıyoruz. Eğer session bilgisi alınamazsa HSM ile bağlantı yok diyebiliriz. O nesneyi kullanmıyoruz.
        * Yeni bir SmartCard nesnesi yaratıyoruz, doğrudan onu dışarı veriyoruz. Queue'deki diğer HSM nesnelerinin bağlantısı olmayabilir. Risk almıyoruz.
        * */
        P11SmartCard p11SmartCard = checkOutOneItem();
        try
        {
            CK_SESSION_INFO sessionInfo = p11SmartCard.getSmartCard().getSessionInfo(p11SmartCard.getSessionNo());
            return p11SmartCard;
        }
        catch (Exception e)
        {
            try
            {
                totalCount--;
                allCreatedObjects.remove(p11SmartCard);
                logger.info("Can not get session info from pool object!", e.getMessage());
                return createPoolItem();
            }
            catch (Exception ex)
            {
                throw new ESYAException("Can not create pool item!", ex);
            }
        }
    }

    private synchronized P11SmartCard checkOutOneItem() throws ESYAException
    {
        try
        {
            P11SmartCard sc = hsmPool.poll();
            if(sc != null)
                return sc;
            else
            {
                if(totalCount < maxCapacity)
                {
                    try
                    {
                        P11SmartCard poolItem = createPoolItem();
                        return poolItem;
                    }
                    catch (PKCS11Exception | IOException | SmartCardException | LoginException e)
                    {
                        throw new ESYAException("Hsm item can not be created!", e);
                    }
                }
                else
                {
                    sc = hsmPool.poll(timeoutInSeconds, TimeUnit.SECONDS);
                    if (sc == null)
                    {
                        throw new ESYAException("Hsm item pool returns no HSM after timeout!");
                    }
                    return sc;
                }
            }
        }
        catch (InterruptedException e)
        {
            throw new ESYAException("Hsm item pool returns no Hsm");
        }
    }

    public boolean offer(P11SmartCard sc)
    {
        return hsmPool.offer(sc);
    }

    @Override
    protected void finalize() throws Throwable
    {
        for(int i=0; i < allCreatedObjects.size(); i++)
        {
            try
            {
                allCreatedObjects.get(i).closeSession();
            }
            catch (Exception ex)
            {
                logger.info("Can not close session!" + ex.getMessage());
            }
        }
        super.finalize();
    }

    public long getSlotNo()
    {
        return slotNo;
    }
}
