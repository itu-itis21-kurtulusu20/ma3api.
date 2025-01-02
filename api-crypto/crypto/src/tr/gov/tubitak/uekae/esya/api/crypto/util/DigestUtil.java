package tr.gov.tubitak.uekae.esya.api.crypto.util;

import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Digester;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class DigestUtil {

    private static final int BLOCK_SIZE = 10000;

    public static byte[] digest(DigestAlg aDigestAlg, byte[] aInput, int aOffset, int aLength) throws CryptoException
    {
        Digester digester  = Crypto.getDigester(aDigestAlg);
        digester.update(aInput, aOffset, aLength);
        return digester.digest();
    }

    public static byte[] digest(DigestAlg aDigestAlg, byte[] aInput) throws CryptoException
    {
        return digest(aDigestAlg, aInput, 0, aInput.length);
    }

    public static byte[] digestFile(DigestAlg aDigestAlg, String aFileName) throws CryptoException, IOException {
        return digestFile(aDigestAlg, aFileName, BLOCK_SIZE);
    }

    public static byte[] digestFile(DigestAlg aDigestAlg, String aFileName, int aBlockSize) throws CryptoException, IOException {
        FileInputStream is = new FileInputStream(aFileName);
        byte[] ozet = digestStream(aDigestAlg, is, aBlockSize);
        is.close();
        return ozet;
    }

    public static byte[] digestStream(DigestAlg aDigestAlg, InputStream aInputStream) throws CryptoException, IOException {
        return digestStream(aDigestAlg, aInputStream, BLOCK_SIZE);
    }

    public static byte[] digestStream(DigestAlg aDigestAlg, InputStream aInputStream, int aBlockSize) throws CryptoException, IOException{
        Digester digester = Crypto.getDigester(aDigestAlg);
    	byte[] block = new byte[aBlockSize];
        int fRead;
        //Java standardında stream sonunda -1 dönülüyor.
        while((fRead = aInputStream.read(block)) != -1)
        {
            digester.update(block, 0, fRead);
        }
        aInputStream.close();
        return digester.digest();
    }

}
