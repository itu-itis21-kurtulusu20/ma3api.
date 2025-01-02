package dev.esya.api.cmssignature.dirak;

import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.HSMSessionPool;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.ObjectSessionInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DirakStreamExample {

    SmartCard sc;

    String keyLabel = "fileEncKey";

    CK_MECHANISM mechanism = null;

    HSMSessionPool hsmPool = null;

    public DirakStreamExample() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);

        mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC);
        mechanism.pParameter = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}; //IV

        long slotNo = 79L;
        String PIN = "123456";
        hsmPool = new HSMSessionPool(CardType.DIRAKHSM, slotNo, PIN);
    }


    @Test
    public void encryptTest() throws Exception {
        encryptFile("C:\\ma3api\\test_files\\different_size_files\\10MB.bin", "C:\\ma3api\\test_files\\different_size_files\\Enc_10MB.bin");
    }


    @Test
    public void decryptTest() throws Exception {
        decryptFile("C:\\ma3api\\test_files\\different_size_files\\Enc_10MB.bin", "C:\\ma3api\\test_files\\different_size_files\\Decrypted_10MB.bin");
    }


    public void encryptFile(String fileName, String encryptedFileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        FileOutputStream fos = new FileOutputStream(encryptedFileName);

        ObjectSessionInfo objectSessionInfo = hsmPool.checkOutItem(keyLabel);

        sc.encryptData(objectSessionInfo.getSession(), objectSessionInfo.getObjectId(), mechanism, fis, fos);

        hsmPool.offer(objectSessionInfo);
    }

    public void decryptFile(String encryptedFileName, String decryptedFileName) throws Exception {
        FileInputStream fis = new FileInputStream(encryptedFileName);
        FileOutputStream fos = new FileOutputStream(decryptedFileName);

        ObjectSessionInfo objectSessionInfo = hsmPool.checkOutItem(keyLabel);

        sc.decryptData(objectSessionInfo.getSession(), objectSessionInfo.getObjectId(), mechanism, fis, fos);

        hsmPool.offer(objectSessionInfo);
    }
}
