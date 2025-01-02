package gnu.crypto.prng;

/**
 * Donanimsal Gurultu Kaynagi Genisletme Fonksiyonu
 *
 * 1- G0 = D[0......127]
 * 2- G1 = D[128....255]
 * 3- G2 = D[256....383]
 * 4- G3 = D[384....511]
 * 5- N = ceiling[L/128]
 * 6- i=0 to N-4
 * 	Gi4 = Gi xor AES(Gi1 || Gi2 ,Gi3)
 *
 * 7- G= G0 || G1 || G2 || G3 ||.... GN-2 || GN-1
 *
 * Belirli bir D gizinden en fazla uretilebilecek bit miktari 2^20
 *
 */

import gnu.crypto.Registry;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BasePRNG;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUEncryptor;

import java.util.Arrays;
import java.util.Map;


public class DGKGF extends BasePRNG {

    public static final String SEED_GENERATOR = "seed_generator";
    private int DEFAULT_BUFFER_SIZE = 2048;
    private ISeed SEED_GENERATOR_I = null;
    private int L = DEFAULT_BUFFER_SIZE * 8;//length in bits for the number that will be generated from the given random
    private byte[] G = null;
    private static final int INITIAL_SEED_LENGTH = 64;

    protected DGKGF() {
        super(Registry.DGKGF_PRNG);
    }

    private DGKGF(DGKGF aDGKGF) {
        this();
        this.SEED_GENERATOR_I = aDGKGF.SEED_GENERATOR_I;
        this.ndx = aDGKGF.ndx;
        this.buffer = aDGKGF.buffer.clone();
        this.initialised = aDGKGF.initialised;
    }

    @Override
    public void setup(Map attributes) {
        if (attributes != null) {
            if (attributes.get(SEED_GENERATOR) != null) {
                SEED_GENERATOR_I = (ISeed) attributes.get(SEED_GENERATOR);
                return;
            }
        }

        SEED_GENERATOR_I = new MemorySeed();
    }

    @Override
    public void fillBlock()
            throws LimitReachedException {
        try {

            byte[] seed = SEED_GENERATOR_I.getSeed(INITIAL_SEED_LENGTH);


            int N = (L + 127) / 128;
            G = new byte[N * 16];

            byte[] G0 = new byte[16];
            byte[] G1 = new byte[16];
            byte[] G2 = new byte[16];
            byte[] G3 = new byte[16];

            System.arraycopy(seed, 0, G0, 0, 16);
            System.arraycopy(seed, 16, G1, 0, 16);
            System.arraycopy(seed, 32, G2, 0, 16);
            System.arraycopy(seed, 48, G3, 0, 16);

            for (int i = 0; i < 64; i++) {
                System.arraycopy(seed, 0, G, 0, 64);
            }

            int Gindex = 64;
            for (int i = 0; i < N - 4; i++) {
                byte[] result = _AES(G1, G2, G3);

                for (int j = 0; j < result.length; j++) {
                    result[j] = (byte) (result[j] ^ G0[j]);
                }

                for (int j = 0; j < 16; j++) {
                    System.arraycopy(result, 0, G, Gindex, 16);
                }

                Gindex = Gindex + 16;
                G0 = G1;
                G1 = G2;
                G2 = G3;
                G3 = result;
            }

        } catch (Exception aEx) {
            throw new ESYARuntimeException("Seed Error:" + aEx.getMessage(), aEx);
        }

        buffer = G;
    }

    private byte[] _AES(byte[] aG1, byte[] aG2, byte[] aG3)
            throws Exception {

        byte[] K = new byte[32];
        System.arraycopy(aG1, 0, K, 0, 16);
        System.arraycopy(aG2, 0, K, 16, 16);
	    BufferedCipher cipher = new BufferedCipher(new GNUEncryptor(CipherAlg.AES256_ECB));
	    cipher.init(K, null);
	    //byte[] result256bit = CipherUtil.encrypt(CipherAlg.AES256_ECB, null, aG3, K)
	    byte[] result256bit = cipher.doFinal(aG3);
        byte[] result128bit = new byte[16];
        System.arraycopy(result256bit, 0, result128bit, 0, 16);

        //it was done due to meet the requirements of crypto analysis
        Arrays.fill(K, (byte)0xCC);

        return result128bit;
    }

    @Override
    public Object clone() {
        return new DGKGF(this);
    }

}
