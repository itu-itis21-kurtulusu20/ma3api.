package dev.esya.api.smartcard;

import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.HSMSessionPool;

import java.io.IOException;


public class HSMSessionPoolTest {
    CardType cardType;
    SmartCard sc;

    @Before
    public void initialize() throws PKCS11Exception, IOException {
        cardType = CardType.DIRAKHSM;
        sc = new SmartCard(cardType);
    }

    @Test
    public void testOneLambdaOperation() throws Exception {
        byte[] data = RandomUtil.generateRandom(10);

        HSMSessionPool sessionPool = new HSMSessionPool(cardType, 22, "123456");
        byte[] signature = sessionPool.executeCryptoOp("QCA1_1-sign", data, (session, objectid, d) ->
                sc.signDataWithKeyID(session, objectid, new CK_MECHANISM(PKCS11Constants.CKM_SHA256_RSA_PKCS), d)
        );
    }

    @Test
    public void testOneSignOperation() throws Exception {
        byte[] data = RandomUtil.generateRandom(10);

        CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_SHA256_RSA_PKCS);

        HSMSessionPool sessionPool = new HSMSessionPool(cardType, 22, "123456");
        HSMSessionPool.SignOperation signOperation = new HSMSessionPool.SignOperation(sc, mechanism);

        byte[] signature = sessionPool.executeCryptoOp("QCA1_1-sign", data, signOperation);
    }

}
