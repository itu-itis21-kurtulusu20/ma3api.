package tr.gov.tubitak.uekae.esya.api.smartcard;

import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.HMACSecretKey;

public class NCipherTest {

    @Test
    public void testNCipher() throws Exception {
        SmartCard sc = new SmartCard(CardType.NCIPHER);
        long[] slotList = sc.getSlotList();

        for (long slot : slotList) {
            CK_SLOT_INFO slotInfo = sc.getSlotInfo(slot);

            System.out.println("Slot description: " + new String(slotInfo.slotDescription));
        }

        long session = sc.openSession(slotList[1]);

        byte[] generatedKey = RandomUtil.generateRandom(128);
        sc.login(session, "123456");


        sc.createSecretKey(session, new HMACSecretKey("HMAC_CREATE", generatedKey, Algorithms.DIGEST_SHA256));

        //sc.createSecretKey(session, );

        //sc.import

        //sc.importSecretKey(session, new HMACSecretKey("TEST1", generatedKey));

        //sc.importKeyPair();
    }
}
