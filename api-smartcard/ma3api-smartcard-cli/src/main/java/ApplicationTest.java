import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.util.Arrays;
import java.util.Random;

public class ApplicationTest {

    long slotID1 = 0L;
    long slotID2 = 80L;

    String keyLabelSymmetric = "aes5";
    String keyLabelAsymmetric = "rsa5";

    CardType cardType1 = CardType.UTIMACO;
    CardType cardType2 = CardType.DIRAKHSM;

    String pin1 = "123456";
    String pin2 = "123456";

    @Test
    public void TestEncryption() throws Exception {
        byte[] inputData;
        {
            int dataToBeSignedLen = 10 + new Random().nextInt(30);
            inputData = RandomUtil.generateRandom(dataToBeSignedLen);
        }

        byte[] iv = RandomUtil.generateRandom(16);

        CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD);
        mechanism.pParameter = iv;

        // ---

        SmartCard sc1 = new SmartCard(cardType1);
        SmartCard sc2 = new SmartCard(cardType2);
        long sid1 = sc1.openSession(slotID1);
        long sid2 = sc2.openSession(slotID2);
        boolean success = false;
        try {
            sc1.login(sid1, pin1);
            sc2.login(sid2, pin2);

            byte[] decryptedDataFromExportedKey;
            {
                byte[] encryptedDataFromOriginalKey = sc1.encryptData(sid1, keyLabelSymmetric, inputData, mechanism);
                assert encryptedDataFromOriginalKey != null;

                decryptedDataFromExportedKey = sc2.decryptData(sid2, keyLabelSymmetric, encryptedDataFromOriginalKey, mechanism);
                assert decryptedDataFromExportedKey != null;
            }

            byte[] decryptedDataFromOriginalKey;
            {
                byte[] encryptedDataFromExportedKey = sc2.encryptData(sid2, keyLabelSymmetric, inputData, mechanism);
                assert encryptedDataFromExportedKey != null;

                decryptedDataFromOriginalKey = sc1.decryptData(sid1, keyLabelSymmetric, encryptedDataFromExportedKey, mechanism);
                assert decryptedDataFromOriginalKey != null;
            }

            assert Arrays.equals(inputData, decryptedDataFromExportedKey);
            assert Arrays.equals(inputData, decryptedDataFromOriginalKey);

            success = true;
        } finally {
            sc1.logout(sid1);
            sc2.logout(sid2);

            sc1.closeSession(sid1);
            sc2.closeSession(sid2);
        }

        assert success;
    }

    @Test
    public void TestSign() throws Exception {
        byte[] inputData;
        {
            int dataToBeSignedLen = 10 + new Random().nextInt(30);
            inputData = RandomUtil.generateRandom(dataToBeSignedLen);
        }

        CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

        // ---

        SmartCard sc1 = new SmartCard(cardType1);
        SmartCard sc2 = new SmartCard(cardType2);
        long sid1 = sc1.openSession(slotID1);
        long sid2 = sc2.openSession(slotID2);
        boolean success = false;
        try {
            sc1.login(sid1, pin1);
            sc2.login(sid2, pin2);

            byte[] signedDataFromOriginalKey = sc1.signData(sid1, keyLabelAsymmetric, inputData, mechanism);
            byte[] signedDataFromExportedKey = sc2.signData(sid2, keyLabelAsymmetric, inputData, mechanism);

            sc1.verifyData(sid1, keyLabelAsymmetric, inputData, signedDataFromExportedKey, mechanism);
            sc2.verifyData(sid2, keyLabelAsymmetric, inputData, signedDataFromOriginalKey, mechanism);

            success = true;
        } finally {
            sc1.logout(sid1);
            sc2.logout(sid2);

            sc1.closeSession(sid1);
            sc2.closeSession(sid2);
        }

        assert success;
    }
}
