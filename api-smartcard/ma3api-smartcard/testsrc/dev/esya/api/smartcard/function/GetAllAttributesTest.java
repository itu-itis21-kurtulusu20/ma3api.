package dev.esya.api.smartcard.function;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import util.CardTestUtil;

public class GetAllAttributesTest {

    static final String PASSWORD = "12345678";
    SmartCard sc = null;
    long sid = 0;
    long slotNo;

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
    public void getAllAttributesTest() throws PKCS11Exception, SmartCardException {
        // Create AES key
        AESKeyTemplate aesKeyTemplate = new AESKeyTemplate("AESKey", 32);
        aesKeyTemplate.getAsTokenTemplate(false, true, false);
        aesKeyTemplate.getAsExportableTemplate();

        long AESKeyID = sc.createSecretKey(sid, aesKeyTemplate);

        CK_ATTRIBUTE[] allAttributes = sc.getAllAttributes(sid, AESKeyID);

        Assert.assertNotNull(allAttributes);
    }
}
