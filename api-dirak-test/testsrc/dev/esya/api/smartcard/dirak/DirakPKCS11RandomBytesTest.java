package dev.esya.api.smartcard.dirak;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.util.Random;

public class DirakPKCS11RandomBytesTest {

    static final String PASSWORD = "12345678";

    SmartCard sc = null;
    long sid = 0;
    long slotNo = 0;

    @Before
    public void setUp() throws Exception {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp() throws Exception {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    @Test
    public void testRandomBytes() throws Exception {
        int dataLen = new Random().nextInt(63) + 1;
        byte[] randomData = sc.getRandomData(sid, dataLen);

        Assert.assertEquals(dataLen, randomData.length);
    }

    @Test
    public void testGet10MBRandom() throws PKCS11Exception {
        int len = 10 * 1024 * 1024;


        //ToDo: 2.3.20 sürümü:
        //int chunkSize = DirakHSMOps.MAX_CHUNK_SIZE;
        int chunkSize = 65536;
        int loopCount = len / chunkSize;
        int totalRandomLen = 0;
        long start = System.currentTimeMillis();
        for(int i = 0; i < loopCount; i++){
            byte[] randomData = sc.getRandomData(sid, chunkSize);
            totalRandomLen = totalRandomLen + randomData.length;
        }
        long end = System.currentTimeMillis();
        System.out.printf("Random data: %d bytes in %d ms", totalRandomLen, (end-start));
    }
}
