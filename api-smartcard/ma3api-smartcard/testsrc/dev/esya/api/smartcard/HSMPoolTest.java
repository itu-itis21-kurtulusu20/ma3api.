package dev.esya.api.smartcard;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.HSMPool;

import java.util.concurrent.ArrayBlockingQueue;

public class HSMPoolTest
{
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testPool() throws Exception
    {
        HSMPool.DEFAULT_INITIAL_CAPACITY = 5;
        HSMPool.DEFAULT_MAX_CAPACITY = 10;
        HSMPool hsmPool = new HSMPool(CardType.DIRAKHSM, 22, "123456");

        for(int i=0; i <10; i++)
        {
            P11SmartCard p11SmartCard = hsmPool.checkOutItem();
            System.out.println("Session: " + p11SmartCard.getSessionNo());
        }
        System.out.println("Done");
    }

    @Test
    public void testPoolAtOverRequest() throws Exception
    {
        HSMPool.DEFAULT_INITIAL_CAPACITY = 1;
        HSMPool.DEFAULT_MAX_CAPACITY = 2;
        HSMPool hsmPool = new HSMPool(CardType.DIRAKHSM, 22, "123456");

        P11SmartCard p11SmartCard = null;
        p11SmartCard = hsmPool.checkOutItem();
        p11SmartCard = hsmPool.checkOutItem();

        exceptionRule.expect(ESYAException.class);
        exceptionRule.expectMessage("Hsm item pool returns no HSM after timeout!");

        p11SmartCard = hsmPool.checkOutItem();
    }

    @Test
    public void testQueueOverAdd()
    {
        ArrayBlockingQueue<Integer> numPool = new ArrayBlockingQueue<Integer>(2);
        numPool.offer(1);
        numPool.offer(2);
        numPool.offer(3);
    }
}
