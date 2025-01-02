package dev.esya.api.smartcard.dirak;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import org.junit.*;
import org.junit.runners.MethodSorters;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_MECHANISM_STRUCTURE;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_MILENAGE_SIGN_PARAMS;

import java.security.spec.RSAKeyGenParameterSpec;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DirakMilenageTest {

    static final String PASSWORD = "12345678";

    SmartCard sc = null;
    long sid = 0;
    long slotNo;

    public static final long CKM_MILENAGE = PKCS11Constants.CKM_VENDOR_DEFINED + 1;

    @Before
    public void setUpClass() throws Exception {
        System.out.println("SettingUP starts");
        sc = new SmartCard(CardType.DIRAKHSM);

        slotNo = CardTestUtil.getSlot(sc);

        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
        System.out.println("SettingUP finishes");
    }

    @After
    public void cleanUp() throws Exception {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    @Test
    public void milenageTest() {
        final String keyLabel = "milenageTestRSAKey";

        long[] keyIDs;
        try {
            // create test key pair
            {
                RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
                RSAKeyPairTemplate template = new RSAKeyPairTemplate(keyLabel, spec);
                template.getAsTokenTemplate(true, false, false);

                // create
                sc.createKeyPair(sid, template);
                keyIDs = new long[2];
                keyIDs[0] = template.getPrivateKeyTemplate().getKeyId();
                keyIDs[1] = template.getPublicKeyTemplate().getKeyId();
            }

            System.out.println("----------------------------------------------------------");
            System.out.println("Milenage imzalama testi basladi.");

            try {
                byte[] EncKi = {(byte) 0x46, (byte) 0x5b, (byte) 0x5c, (byte) 0xe8, (byte) 0xb1, (byte) 0x99, (byte) 0xb4, (byte) 0x9f, (byte) 0xaa, (byte) 0x5f, (byte) 0x0a, (byte) 0x2e, (byte) 0xe2, (byte) 0x38, (byte) 0xa6, (byte) 0xbc};
                byte[] OPc = {(byte) 0xCD, (byte) 0x63, (byte) 0xcb, (byte) 0x71, (byte) 0x95, (byte) 0x4a, (byte) 0x9f, (byte) 0x4e, (byte) 0x48, (byte) 0xa5, (byte) 0x99, (byte) 0x4e, (byte) 0x37, (byte) 0xa0, (byte) 0x2b, (byte) 0xaf};
                byte[] Amf = {(byte) 0xb9, (byte) 0xb9};
                byte[] Sqn = {(byte) 0xff, (byte) 0x9b, (byte) 0xb4, (byte) 0xd0, (byte) 0xb6, (byte) 0x07};

                CK_MILENAGE_SIGN_PARAMS milenageParams = new CK_MILENAGE_SIGN_PARAMS();
                {
                    milenageParams.ulMilenageFlags = new NativeLong(1L, true);

                    milenageParams.ulEncKiLen = new NativeLong(EncKi.length, true);

                    milenageParams.pEncKi = new Memory(EncKi.length);
                    milenageParams.pEncKi.write(0L, EncKi, 0, EncKi.length);

                    milenageParams.ulEncOPcLen = new NativeLong(OPc.length, true);

                    milenageParams.pEncOPc = new Memory(OPc.length);
                    milenageParams.pEncOPc.write(0L, OPc, 0, OPc.length);

                    milenageParams.hSecondaryKey = new NativeLong(1L, true);
                    milenageParams.hRCKey = new NativeLong(2L, true);

                    System.arraycopy(Sqn, 0, milenageParams.sqn, 0, Sqn.length);
                    System.arraycopy(Amf, 0, milenageParams.amf, 0, Amf.length);

                    milenageParams.count = new NativeLong(1L, true);
                }
                milenageParams.write();

                CK_MECHANISM_STRUCTURE mech = new CK_MECHANISM_STRUCTURE();
                {
                    mech.mechanism = new NativeLong(CKM_MILENAGE, true);
                    mech.pParameter = milenageParams.getPointer();
                    mech.ulParameterLen = new NativeLong(milenageParams.size(), true);
                }
                mech.write();

                // imza atma

                long privateKeyID = keyIDs[0];

                mech.read();

                byte[] signed = sc.signDataWithDIRAK(sid, privateKeyID, mech, null);

                System.out.println("Milenage Result: " + StringUtil.toHexString(signed));
                
            } finally {
                sc.getPKCS11().C_DestroyObject(sid, keyIDs[0]);
                sc.getPKCS11().C_DestroyObject(sid, keyIDs[1]);
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
            Assert.fail();
        }

        System.out.println();
        System.out.println("TEST BASARILI");
        System.out.println("----------------------------------------------------------");
        System.out.println();
    }
}
