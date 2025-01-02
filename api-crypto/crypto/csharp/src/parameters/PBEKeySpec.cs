using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
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
    public class PBEKeySpec : IKeySpec
    {
        private readonly char[] _mPassword;
        private readonly byte[] _mSalt;
        private readonly int _mIterationCount;
        private int _mKeyLength;
        private readonly DigestAlg _mDigestAlg;
        private static readonly int DEFAULT_PASS_LENGTH = 8;
        private static readonly int DEFAULT_SALT_LENGTH = 8;
        private static readonly int DEFAULT_ITERATION_COUNT = 100;

        /**
         * PBEKey objesini olusturur.
         * Salt DEFAULT_SALT_LENGTH(8) boyutunda random bir byte array olarak olusturulur.
         * Iteration sayisi DEFAULT_ITERATION_COUNT(1000) olarak set edilir.
         * Anahtar uzunlugu Simetrik algoritmanin block size'ina  ve key size'na bagli
         * oldugu icin daha sonra set edilecektir.
         *
         * @param aPassword Password
         */
        public PBEKeySpec(char[] aPassword)
            : this(aPassword, null) { }


        /**
         * PBEKey objesini olusturur.
         * Iteration sayisi DEFAULT_ITERATION_COUNT(1000) olarak set edilir.
         * Anahtar uzunlugu Simetrik algoritmanin block size'ina  ve key size'na bagli
         * oldugu icin daha sonra set edilecektir.
         *
         * @param aPassword Password
         * @param aSalt     Salt
         */
        public PBEKeySpec(char[] aPassword, byte[] aSalt)
            : this(aPassword, aSalt, DEFAULT_ITERATION_COUNT) { }

        /**
         * PBEKey objesini olusturur.
         * Iteration sayisi set edilir.
         * Anahtar uzunlugu Simetrik algoritmanin block size'ina  ve key size'na bagli
         * oldugu icin daha sonra set edilecektir.
         *
         * @param aPassword       Password
         * @param aIterationCount IterationCount
         */
        public PBEKeySpec(char[] aPassword, int aIterationCount) : this(aPassword, null, aIterationCount) { }


        /**
         * PBEKey objesini olusturur. Anahtar uzunlugu Simetrik algoritmanin block size'ina
         * ve key size'na bagli oldugu icin daha sonra set edilecektir.
         *
         * @param aPassword       Password
         * @param aSalt           Salt
         * @param aIterationCount Iteration sayisi
         */
        public PBEKeySpec(char[] aPassword, byte[] aSalt, int aIterationCount)
            : this(aPassword, aSalt, aIterationCount, 0) { }


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
                          int aKeyLength)
            : this(aPassword, aSalt, aIterationCount, aKeyLength, null) { }


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
                          int aKeyLength, DigestAlg aDigestAlg)
        {
            if (aPassword == null || aPassword.Length < DEFAULT_PASS_LENGTH)
            {
                throw new ArgErrorException("Password uzunluğu en az " + DEFAULT_PASS_LENGTH + " karakter olmalı");
            }

            this._mPassword = aPassword;

            if (aSalt == null)
            {
                //mSalt = new byte[DEFAULT_SALT_LENGTH];
                //SecureRandom rng = new SecureRandom();
                //rng.NextBytes(mSalt);   
                _mSalt = RandomUtil.generateRandom(DEFAULT_SALT_LENGTH);
            }
            else if (aSalt.Length < DEFAULT_SALT_LENGTH)
            {
                throw new ArgErrorException("Salt uzunluğu en az " + DEFAULT_SALT_LENGTH + " byte olmalı");
            }
            else
            {
                this._mSalt = aSalt;
            }

            if (aIterationCount < DEFAULT_ITERATION_COUNT)
            {
                throw new ArgErrorException("Iteration count en az " + DEFAULT_ITERATION_COUNT + " olmalı");
            }
            this._mIterationCount = aIterationCount;
            this._mKeyLength = aKeyLength;

            _mDigestAlg = aDigestAlg;
        }


        /**
         * Passwordu donduren metodtur.
         *
         * @return password
         */
        public char[] getPassword()
        {
            return _mPassword;
        }


        /**
         * Salt'i donduren metodtur.
         *
         * @return salt
         */
        public byte[] getSalt()
        {
            return _mSalt;
        }


        /**
         * IterationCount'u donduren metodtor.
         *
         * @return iteration count
         */
        public int getIterationCount()
        {
            return _mIterationCount;
        }

        public DigestAlg getDigestAlg()
        {
            return _mDigestAlg;
        }

        /**
         * Anahtarin uzunlugunu donduren metodtur.
         *
         * @return Anahtarin uzunlugu
         */
        public int getKeyLength()
        {
            return _mKeyLength;
        }


        /**
         * Anahtarin uzunlugunu belirleyen metodtur.
         *
         * @param aKeyLength Anahtarin uzunlugu
         */
        public void setKeyLength(int aKeyLength)
        {
            _mKeyLength = aKeyLength;
        }
    }
}
