package gnu.crypto.util;

import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Padding;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUEncryptor;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

import java.math.BigInteger;

/**
 * @author ahmety
 */
class TRSUYardimci
{

    //private SymmetricCrypto mSimetrikKripto = null;
    private static final int BLOCK_SIZE = 32;
    private static final int AES_BLOCK_SIZE = 16;
    static CipherAlg AES = CipherAlg.AES128_CBC;

    private static TRSUYardimci mInstance;

    private DurumVektoru mDurumVektoru = new DurumVektoru();

    private TRSUYardimci(){}

    public static TRSUYardimci getInstance(){
        if (mInstance==null){
            mInstance = new TRSUYardimci();
        }
        return mInstance;
    }

    /**
     * Gonderilen kaynak 32 bytelik parcalar halinde islemden gecirilir.
     * Kynak mod 32 den artan deger degistirilmeden remainder olarak doner.
     * Bu parca bir sonraki kaynak uretimiyle birlikte kullanilmalidir.
     */
    public synchronized RandomSonuc ticarilestir(byte[] aKaynak) throws CryptoException
    {
        if (aKaynak==null || aKaynak.length<BLOCK_SIZE)
            throw new CryptoException("Rastgele Sayı üretimi için yetersiz kaynak.");

        int kalanMiktar = aKaynak.length % BLOCK_SIZE;
        byte[] rastgeleSonuc = new byte[aKaynak.length - kalanMiktar];

        for (int i =0; (i+1)*BLOCK_SIZE <= aKaynak.length; i++){
            byte[] partial = new byte[BLOCK_SIZE];
            System.arraycopy(aKaynak, i*BLOCK_SIZE, partial, 0, BLOCK_SIZE);
            byte[] partialSonuc = rastgeleVeriUret(partial);
            System.arraycopy(partialSonuc, 0, rastgeleSonuc, i*BLOCK_SIZE, BLOCK_SIZE);
        }

        // remainder
        byte[] remainder = new byte[kalanMiktar];
        System.arraycopy(aKaynak, aKaynak.length - remainder.length, remainder, 0, remainder.length);

        return new RandomSonuc(rastgeleSonuc, remainder);
    }

    private byte[] rastgeleVeriUret(byte[] aKaynak) throws CryptoException
    {
        byte[] sonuc = null;

        durumVektorunuGuncelle(aKaynak);

        byte[] a0 = aes(mDurumVektoru.k, mDurumVektoru.getBytesOfC());
        mDurumVektoru.incrementC();
        byte[] a1 = aes(mDurumVektoru.k, mDurumVektoru.getBytesOfC());
        mDurumVektoru.incrementC();

        sonuc = concat(a0, a1);

        byte[] a2 = aes(mDurumVektoru.k, mDurumVektoru.getBytesOfC());
        mDurumVektoru.incrementC();
        byte[] a3 = aes(mDurumVektoru.k, mDurumVektoru.getBytesOfC());
        mDurumVektoru.incrementC();
        mDurumVektoru.k = concat(a2, a3);

        return sonuc;
    }

    private byte[] aes(byte[] aKey, byte[] aData) throws CryptoException
    {
	    BufferedCipher encryptor = new BufferedCipher(new GNUEncryptor(AES));
	    encryptor.init(aKey, null);
	    return encryptor.doFinal(aData);
        //return CipherUtil.encrypt(AES, null, aData, aKey);
        /*
        if (mSimetrikKripto ==null){
            SymmetricAlgorithm aesAlg = new SymmetricAlgorithm(Ozellikler.SIM_ALGO_AES, AES_BLOCK_SIZE, Ozellikler.SIM_MOD_ECB, Ozellikler.PAD_PKCS7, new byte[]{});
            mSimetrikKripto = new SymmetricCrypto(aesAlg);
        }
        mSimetrikKripto.anahtarYerlestir(aKey);
        byte[] aes = mSimetrikKripto.sifrele(aData);
        byte[] result = new byte[AES_BLOCK_SIZE];
        System.arraycopy(aes, 0, result, 0, AES_BLOCK_SIZE);
        return result;*/
    }

    private void durumVektorunuGuncelle(byte[] aKaynak) throws CryptoException
    {
        //durumVektoru.k = sha( sha(durumVektoru.k +| a32ByteKaynak) +| (durumVektoru.k +| a32ByteKaynak)
        byte[] s1 = DigestUtil.digest(DigestAlg.SHA256, concat(mDurumVektoru.k, aKaynak));
        mDurumVektoru.k = DigestUtil.digest(DigestAlg.SHA256, concat(s1, concat(mDurumVektoru.k, aKaynak)));
        // c
        mDurumVektoru.incrementC();
    }

    public static byte[] concat(byte[] aArr1, byte[] aArr2){
        byte[] result = new byte[aArr1.length + aArr2.length];
        System.arraycopy(aArr1, 0, result, 0, aArr1.length);
        System.arraycopy(aArr2, 0, result, aArr1.length, aArr2.length);
        return result;
    }

    static void yazdir(String aMesaj, byte[] aSrc){
        System.out.print(aMesaj+"("+aSrc.length+")");
        for (int i = 0; i < aSrc.length; i++) {
            String current = Integer.toString(0xff & aSrc[i], AES_BLOCK_SIZE);
            System.out.print(" "+(current.length()==2 ? current : '0'+current));
        }
        System.out.println();
    }

    class DurumVektoru {
        final BigInteger BIR = new BigInteger("1");
        BigInteger c = new BigInteger("0", 10);
        byte[] k = new byte[]{
                0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77,
                (byte)0x88, (byte)0x99, (byte)0xAA, (byte)0xBB,
                (byte)0XCC, (byte)0xDD, (byte)0xEE, (byte)0xFF,
                (byte)0xFF, (byte)0xEE, (byte)0xDD, (byte)0xCC,
                (byte)0xBB, (byte)0xAA, (byte)0x99, (byte)0x88,
                0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00  };

        byte[] getBytesOfC(){
            byte[] bytes = c.toString(10).getBytes();
            for(int i=0;i<bytes.length;i++){
                if (bytes[i]>0)
                    bytes[i]-= '0';
            }
            byte[] sonuc = new byte[AES_BLOCK_SIZE];
            System.arraycopy(bytes, 0, sonuc, AES_BLOCK_SIZE -bytes.length, bytes.length);
            return sonuc;
        }

        void incrementC(){
            c = c.add(BIR);
        }
    }

    public static class RandomSonuc {
        private byte[] mSonuc;
        private byte[] mKalan;

        public RandomSonuc(byte[] aSonuc, byte[] aKalan)
        {
            mSonuc = aSonuc;
            mKalan = aKalan;
        }

        public byte[] getmSonuc()
        {
            return mSonuc;
        }

        public byte[] getmKalan()
        {
            return mKalan;
        }

    }

    public static void main(String[] aArgs) throws Exception
    {
        byte[] bytes = new byte[1093];

        TRSUYardimci yardimci = new TRSUYardimci();

        yazdir("initial k: ", yardimci.mDurumVektoru.k);

        yazdir("initial c: ", yardimci.mDurumVektoru.getBytesOfC());

        yardimci.durumVektorunuGuncelle((Math.random()+"").getBytes());

        yazdir(" guncel c: ", yardimci.mDurumVektoru.getBytesOfC());

        yazdir("aes ",yardimci.aes(yardimci.mDurumVektoru.k, yardimci.mDurumVektoru.getBytesOfC() ));

        RandomSonuc sonuc = yardimci.ticarilestir(bytes);
        yazdir("ticari: ",sonuc.getmSonuc());
        yazdir("remain: ",sonuc.getmKalan());
    }
}
