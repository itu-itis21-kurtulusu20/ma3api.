package dev.esya.api.smartcard.dirak;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

public class DirakPKCS11DataTest {

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
    public void test_01_WritePrivateData()
        throws Exception {
        byte[] data = new byte[]{1, 2, 3, 4};
        sc.writePrivateData(sid, "PRIDATA", data);

        boolean r1 = sc.isObjectExist(sid, "PRIDATA");
        Assert.assertTrue(r1);

        byte[] result = sc.readPrivateData(sid, "PRIDATA").get(0);
        Assert.assertArrayEquals(data, result);

        sc.deletePrivateData(sid, "PRIDATA");
        r1 = sc.isObjectExist(sid, "PRIDATA");

        Assert.assertFalse(r1);
    }

    @Test
    public void test_02_WritePublicData()
        throws Exception {
        byte[] data = new byte[]{5, 6, 7, 8};
        sc.writePublicData(sid, "PUBDATA", data);

        boolean r1 = sc.isObjectExist(sid, "PUBDATA");
        Assert.assertTrue(r1);

        byte[] result = sc.readPublicData(sid, "PUBDATA").get(0);
        Assert.assertArrayEquals(data, result);

        sc.deletePublicData(sid, "PUBDATA");
        r1 = sc.isObjectExist(sid, "PUBDATA");

        Assert.assertFalse(r1);
    }
}
