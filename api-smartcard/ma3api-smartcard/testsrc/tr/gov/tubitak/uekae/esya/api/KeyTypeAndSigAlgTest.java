package tr.gov.tubitak.uekae.esya.api;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

public class KeyTypeAndSigAlgTest {

    String rsaKeyLabel = "QCA1_1";
    String ecKeyLabel = "qc d1 2";

    String cardPIN = "12345";

    @Test
    public void rsaSignWithECKeyTest() throws Exception {
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        long sid = sc.openSession(slot);
        sc.login(sid, cardPIN);

        byte[] tbsData = RandomUtil.generateRandom(16);

        boolean success = false;
        try {
            SmartOp.sign(sc, sid, slot, ecKeyLabel, tbsData, SignatureAlg.RSA_SHA256.getName());
        } catch (SmartCardException ex) {
            success = ex.getMessage().equals(GenelDil.mesaj(GenelDil.IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU));
        } finally {
            sc.logout(sid);
            sc.closeSession(sid);
        }

        assert success;
    }

    @Test
    public void ecSignWithRSAKeyTest() throws Exception {
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        long sid = sc.openSession(slot);
        sc.login(sid, cardPIN);

        byte[] tbsData = RandomUtil.generateRandom(16);

        boolean success = false;
        try {
            SmartOp.sign(sc, sid, slot, rsaKeyLabel, tbsData, SignatureAlg.ECDSA_SHA256.getName());
        } catch (SmartCardException ex) {
            success = ex.getMessage().equals(GenelDil.mesaj(GenelDil.IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU));
        } finally {
            sc.logout(sid);
            sc.closeSession(sid);
        }

        assert success;
    }

    @Test
    public void rsaSignWithRSAKeyTest() throws Exception {
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        long sid = sc.openSession(slot);
        sc.login(sid, cardPIN);

        String keyLabel = rsaKeyLabel;
        byte[] tbsData = RandomUtil.generateRandom(16);

        try {
            SmartOp.sign(sc, sid, slot, keyLabel, tbsData, SignatureAlg.RSA_SHA256.getName());
        } finally {
            sc.logout(sid);
            sc.closeSession(sid);
        }
    }

    @Test
    public void ecSignWithECKeyTest() throws Exception {
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        long sid = sc.openSession(slot);
        sc.login(sid, cardPIN);

        String keyLabel = ecKeyLabel;
        byte[] tbsData = RandomUtil.generateRandom(16);

        try {
            SmartOp.sign(sc, sid, slot, keyLabel, tbsData, SignatureAlg.ECDSA_SHA256.getName());
        } finally {
            sc.logout(sid);
            sc.closeSession(sid);
        }
    }
}
