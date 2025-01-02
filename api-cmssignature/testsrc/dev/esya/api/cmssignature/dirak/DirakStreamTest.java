package dev.esya.api.cmssignature.dirak;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class DirakStreamTest {

    static final String PIN = "123456";
    static final String testDataBasePath = "C:/ma3api/test_files/different_size_files";

    SmartCard smartCard = null;
    long sessionID = 0;
    long slotID = 0;

    String keyLabel;
    long keyID;

    String inputFilePath;
    long fileSize;

    @Parameterized.Parameters(name = "{0}, {1}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
            { "1KB.bin",                      1024L}, //  1 KB
            {"20KB.bin",                20L * 1024L}, // 20 KB
            { "1MB.bin",              1024L * 1024L}, //  1 MB
            {"50MB.bin",        50L * 1024L * 1024L}, // 50 MB
            { "4GB.bin", 4L * 1024L * 1024L * 1024L}  //  4 GB
        });
    }

    static final String encryptedFilePath = testDataBasePath + "/output_file_encrypted.bin";
    static final String decryptedFilePath = testDataBasePath + "/output_file_decrypted.bin";

    public DirakStreamTest(String inputFileName, long inputFileSize) throws IOException {
        this.inputFilePath = testDataBasePath + "/" + inputFileName;
        this.fileSize = inputFileSize;
        FileUtil.createDummyFileIfNotExist(inputFilePath, inputFileSize);
    }

    @Before
    public void setUp() throws PKCS11Exception, IOException, SmartCardException {
        smartCard = new SmartCard(CardType.DIRAKHSM);

        slotID = getSlot();
        System.out.println("Using slot " + slotID);

        sessionID = smartCard.openSession(slotID);

        smartCard.login(sessionID, PIN);

        // ---

        keyLabel = "testKeyAES_" + System.currentTimeMillis();

        SecretKeyTemplate keyTemplate = new AESKeyTemplate(keyLabel, 16);
        keyID = smartCard.createSecretKey(sessionID, keyTemplate);
        System.out.println("Created key " + keyLabel);
    }

    @After
    public void cleanUp() throws PKCS11Exception {
        if (keyID > 0) {
            smartCard.getPKCS11().C_DestroyObject(sessionID, keyID);
            keyID = 0;
        }

        // ---

        smartCard.logout(sessionID);
        smartCard.closeSession(sessionID);

        // ---

        // delete output files finally
        try {
            Files.deleteIfExists(Paths.get(encryptedFilePath));
        } catch (IOException e) {
            System.err.println(MessageFormat.format("Could not delete output file (\"{0}\"); reason: {1}", encryptedFilePath, e.getMessage()));
        }
        try {
            Files.deleteIfExists(Paths.get(decryptedFilePath));
        } catch (IOException e) {
            System.err.println(MessageFormat.format("Could not delete output file (\"{0}\"); reason: {1}", decryptedFilePath, e.getMessage()));
        }
    }

    private long getSlot() throws PKCS11Exception {
        long[] slots = smartCard.getTokenPresentSlotList();
        return slots[0];
    }

    @Test
    public void testFileEncryption() throws PKCS11Exception, SmartCardException, IOException, NoSuchAlgorithmException {
        testFileEncryption(inputFilePath);
    }

    void testFileEncryption(String inputFilePath) throws IOException, PKCS11Exception, SmartCardException, NoSuchAlgorithmException {
        testEncryption(
            inputFilePath,
            encryptedFilePath,
            decryptedFilePath
        );
    }

    /**
     * Tests encryption and decryption functionalities of a given {@link SmartCard} depending solely on data from the file system (see {@link FileInputStream}, {@link FileOutputStream}).
     * <p/>
     * Note that the input data for the decryption is read from the output data file resulting from the encryption.
     *
     * @param encryptionInputFilePath  Path that poins to the data input (to be encrypted) file.
     * @param encryptionOutputFilePath Path that points to the encrypted data output file.
     * @param decryptionOutputFilePath Path that points to the decrypted data output file.
     */
    void testEncryption(String encryptionInputFilePath, String encryptionOutputFilePath, String decryptionOutputFilePath) throws PKCS11Exception, SmartCardException, IOException, NoSuchAlgorithmException {
        FileInputStream plainFileInputStream;
        FileOutputStream encryptedFileOutputStream;
        FileInputStream encryptedFileInputStream;
        FileOutputStream decryptedFileOutputStream;

        CK_MECHANISM mechanism;
        {
            byte[] iv = RandomUtil.generateRandom(16);
            mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv);
        }

        long startTime;
        long elapsedTime;

        // encryption
        plainFileInputStream = new FileInputStream(encryptionInputFilePath);
        encryptedFileOutputStream = new FileOutputStream(encryptionOutputFilePath);
        try {
            startTime = System.currentTimeMillis();

            smartCard.encryptData(sessionID, keyID, mechanism, plainFileInputStream, encryptedFileOutputStream);

            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(MessageFormat.format("Encrypt end - File size: {0} bytes - Elapsed time: {1} ms", fileSize, elapsedTime));
        } finally {
            plainFileInputStream.close();
            encryptedFileOutputStream.close();
        }

        // decryption
        encryptedFileInputStream = new FileInputStream(encryptionOutputFilePath);
        decryptedFileOutputStream = new FileOutputStream(decryptionOutputFilePath);
        try {
            startTime = System.currentTimeMillis();

            smartCard.decryptData(sessionID, keyID, mechanism, encryptedFileInputStream, decryptedFileOutputStream);

            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(MessageFormat.format("Decrypt end - File size: {0} bytes - Elapsed time: {1} ms", fileSize, elapsedTime));
        } finally {
            encryptedFileInputStream.close();
            decryptedFileOutputStream.close();
        }

        // verification: data comparison

        byte[] checksumOriginal;
        byte[] checksumDecrypted;

        // create input streams to read and compare the decrypted data
        try {
            plainFileInputStream = new FileInputStream(encryptionInputFilePath);
            checksumOriginal = checksum(plainFileInputStream);
        } finally {
            plainFileInputStream.close();
        }
        try {
            encryptedFileInputStream = new FileInputStream(decryptionOutputFilePath);
            checksumDecrypted = checksum(encryptedFileInputStream);
        } finally {
            encryptedFileInputStream.close();
        }

        // comparison: using checksum values
        Assert.assertArrayEquals("Checksums for input data and output data do not match", checksumOriginal, checksumDecrypted);
    }

    static byte[] checksum(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        byte[] buffer = new byte[1024 * 1024];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-512");

        while ((count = inputStream.read(buffer)) > 0) {
            digest.update(buffer, 0, count);
        }

        return digest.digest();
    }
}
