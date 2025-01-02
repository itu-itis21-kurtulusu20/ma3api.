package dev.esya.api.smartcard.dirak;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;

import java.security.KeyPair;
import java.security.spec.ECParameterSpec;

public class DirakKeyGenerateTest {

    static final String PASSWORD = "12345678";
    SmartCard sc;
    long slotNo;
    long sid;

    @Before
    public void setUpClass() throws Exception {
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
    public void testRSAKeyGeneration() throws ESYAException {
        byte[] bytes = sc.generateRSAPrivateKey(sid, 2048);
    }

    @Test
    public void testECKeyGeneration() throws ESYAException {
        String curveName = "secp384r1";
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);

        KeyPair keyPair = sc.generateECKeyPair(sid, ecParameterSpec);
    }
}
