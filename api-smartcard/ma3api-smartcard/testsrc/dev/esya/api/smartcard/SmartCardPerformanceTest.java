package dev.esya.api.smartcard;

import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

public class SmartCardPerformanceTest {

    @Test
    public void RSA_PKCS_Performance_Test() throws Exception
    {
        long signCount = 100;
        //long slotNo = 1;
        //String keyLabel = "QCA1_2";

        long slotNo = 2;
        String keyLabel = "QCA1_1";

        byte [] toBeSigned = RandomUtil.generateRandom(50);

        CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

        SmartCard sc = new SmartCard(CardType.AKIS);
        long session = sc.openSession(slotNo);
        sc.login(session, "12345");

        long startTime = System.currentTimeMillis();
        for(int i=0; i < signCount; i++)
        {
            sc.signData(session, keyLabel, toBeSigned, mechanism);
        }
        long endTime = System.currentTimeMillis();

        double interval = ((double) (endTime - startTime)) / 1000;
        System.out.println(signCount + " RSA_PKCS sign: " + interval);
    }


}
