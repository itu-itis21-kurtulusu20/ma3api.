package test.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeStreamParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.MemoryDecryptor;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.Arrays;

/**
 * Created by sura.emanet on 16.01.2018.
 */
public class CmsEnvelopeTestUtil {

    public static void decryptWithStream(PrivateKey privKey, ECertificate cert, String encryptedFileName, String decryptedFileName) throws Exception {

        FileInputStream encryptedInputStream = new FileInputStream(encryptedFileName);
        FileOutputStream decryptedOutputStream = new FileOutputStream(decryptedFileName);

        Pair<ECertificate,PrivateKey> recipient = new Pair<ECertificate,PrivateKey>(cert ,privKey);
        IDecryptorStore decryptor = new MemoryDecryptor(recipient);
        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
        cmsParser.open(decryptedOutputStream, decryptor);

        decryptedOutputStream.close();
        encryptedInputStream.close();
    }

    public static byte[] decryptInMemory(ECertificate cert, PrivateKey privKey, byte[] encryptedCMS) throws Exception {
        Pair<ECertificate, PrivateKey> recipient = new Pair<ECertificate, PrivateKey>(cert, privKey);
        IDecryptorStore decryptor = new MemoryDecryptor(recipient);
        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedCMS);
        byte[] plainData = cmsParser.open(decryptor);

        return plainData;
    }

    public static byte [] decryptWithStream(PrivateKey privKey, ECertificate cert, ByteArrayInputStream encryptedInputStream) throws Exception {
        ByteArrayOutputStream decryptedOutputStream = new ByteArrayOutputStream();

        Pair<ECertificate,PrivateKey> recipient = new Pair<ECertificate,PrivateKey>(cert ,privKey);
        IDecryptorStore decryptor = new MemoryDecryptor(recipient);
        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
        cmsParser.open(decryptedOutputStream, decryptor);

        decryptedOutputStream.close();
        encryptedInputStream.close();

        return decryptedOutputStream.toByteArray();
    }

    public static byte[] getDigest(String expected) throws Exception {
        byte[] buffer = new byte[128 * 10000];
        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream expectedInputStream = new FileInputStream(expected);

        int readLen = expectedInputStream.read(buffer);
        while (readLen > 0) {
            md.update(buffer, 0, readLen);
            readLen = expectedInputStream.read(buffer);
        }
        return md.digest();
    }

    public static boolean compareFiles(String expected, String actual) throws Exception {
        byte [] expectedDigest = getDigest(expected);
        byte [] actualDigest = getDigest(actual);

        if(Arrays.equals(expectedDigest,actualDigest))
            return true;
        else
            return false;
    }
}
