package tr.gov.tubitak.uekae.esya.api.crypto.params;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;

/**
 * <p>Title: PBEKeySpec </p>
 * <p>Description: PKCS#5 'de tanimlanmis PBKDF2 icin gerekli parametreleri
 * tutan siniftir.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Tubitak UEKAE</p>
 *
 * @author Murat Yasin Kubilay
 * @version 1.0
 */

public class PBEKeySpec implements KeySpec
{
    private char[] mPassword;
    private byte[] mSalt;
    private int mIterationCount;
    private int mKeyLength;
    private DigestAlg mDigestAlg;
    private static final int DEFAULT_PASS_LENGTH = 8;
    private static final int DEFAULT_SALT_LENGTH = 8;
    private static final int DEFAULT_ITERATION_COUNT = 100;

    /**
     * PBEKey objesini olusturur.
     * Salt DEFAULT_SALT_LENGTH(8) boyutunda random bir byte array olarak olusturulur.
     * Iteration sayisi DEFAULT_ITERATION_COUNT(1000) olarak set edilir.
     * Anahtar uzunlugu Simetrik algoritmanin block size'ina  ve key size'na bagli
     * oldugu icin daha sonra set edilecektir.
     *
     * @param aPassword Password
     */
    public PBEKeySpec(char[] aPassword) throws ArgErrorException
    {
        this(aPassword, null);
    }


    /**
     * PBEKey objesini olusturur.
     * Iteration sayisi DEFAULT_ITERATION_COUNT(1000) olarak set edilir.
     * Anahtar uzunlugu Simetrik algoritmanin block size'ina  ve key size'na bagli
     * oldugu icin daha sonra set edilecektir.
     *
     * @param aPassword Password
     * @param aSalt     Salt
     */
    public PBEKeySpec(char[] aPassword, byte[] aSalt) throws ArgErrorException
    {
        this(aPassword, aSalt, DEFAULT_ITERATION_COUNT);
    }

    /**
     * PBEKey objesini olusturur.
     * Iteration sayisi set edilir.
     * Anahtar uzunlugu Simetrik algoritmanin block size'ina  ve key size'na bagli
     * oldugu icin daha sonra set edilecektir.
     *
     * @param aPassword       Password
     * @param aIterationCount IterationCount
     */
    public PBEKeySpec(char[] aPassword, int aIterationCount) throws ArgErrorException
    {
        this(aPassword, null, aIterationCount);
    }

    /**
     * PBEKey objesini olusturur. Anahtar uzunlugu Simetrik algoritmanin block size'ina
     * ve key size'na bagli oldugu icin daha sonra set edilecektir.
     *
     * @param aPassword       Password
     * @param aSalt           Salt
     * @param aIterationCount Iteration sayisi
     */
    public PBEKeySpec(char[] aPassword, byte[] aSalt, int aIterationCount) throws
            ArgErrorException
    {
        this(aPassword, aSalt, aIterationCount, 0);
    }

    /**
     * PBEKeySpec objesini olusturur. Eger salt null olarak tanimlandiysa
     * DEFAULT_SALT_LENGTH boyunda random bir byte array olusturur ve salt'i olusturur.
     *
     * @param aPassword       Password
     * @param aSalt           Salt
     * @param aIterationCount Iteration sayisi
     * @param aKeyLength      Anahtarin uzunlugu
     */
    public PBEKeySpec(char[] aPassword, byte[] aSalt, int aIterationCount,
                      int aKeyLength) throws ArgErrorException
    {
        this(aPassword, aSalt, aIterationCount, aKeyLength, null);
    }


    /**
     * PBEKeySpec objesini olusturur. Eger salt null olarak tanimlandiysa
     * DEFAULT_SALT_LENGTH boyunda random bir byte array olusturur ve salt'i olusturur.
     *
     * @param aPassword       Password
     * @param aSalt           Salt
     * @param aIterationCount Iteration sayisi
     * @param aKeyLength      Anahtarin uzunlugu
     */
    public PBEKeySpec(char[] aPassword, byte[] aSalt, int aIterationCount,
                      int aKeyLength, DigestAlg aDigestAlg) throws ArgErrorException
    {
        if (aPassword == null || aPassword.length < DEFAULT_PASS_LENGTH) {
            throw new ArgErrorException("Password uzunlugu en az " + DEFAULT_PASS_LENGTH + " karakter olmali");
        }

        this.mPassword = aPassword;

        if (aSalt == null) {
            mSalt = RandomUtil.generateRandom(DEFAULT_SALT_LENGTH);
        } else if (aSalt.length < DEFAULT_SALT_LENGTH) {
            throw new ArgErrorException("Salt uzunlugu en az " + DEFAULT_SALT_LENGTH + " byte olmali");
        } else {
            this.mSalt = aSalt;
        }

        if (aIterationCount < DEFAULT_ITERATION_COUNT) {
            throw new ArgErrorException("Iteration count en az " + DEFAULT_ITERATION_COUNT + " olmali");
        }
        this.mIterationCount = aIterationCount;
        this.mKeyLength = aKeyLength;

        mDigestAlg = aDigestAlg;
    }


    /**
     * Passwordu donduren metodtur.
     *
     * @return password
     */
    public char[] getPassword()
    {
        return mPassword;
    }


    /**
     * Salt'i donduren metodtur.
     *
     * @return salt
     */
    public byte[] getSalt()
    {
        return mSalt;
    }


    /**
     * IterationCount'u donduren metodtor.
     *
     * @return iteration count
     */
    public int getIterationCount()
    {
        return mIterationCount;
    }

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    /**
     * Anahtarin uzunlugunu donduren metodtur.
     *
     * @return Anahtarin uzunlugu
     */
    public int getKeyLength()
    {
        return mKeyLength;
    }


    /**
     * Anahtarin uzunlugunu belirleyen metodtur.
     *
     * @param aKeyLength Anahtarin uzunlugu
     */
    public void setKeyLength(int aKeyLength)
    {
        mKeyLength = aKeyLength;
    }

}
