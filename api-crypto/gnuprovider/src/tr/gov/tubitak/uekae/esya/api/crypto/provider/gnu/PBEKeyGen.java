package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.Registry;
import gnu.crypto.prng.IPBE;
import gnu.crypto.prng.PRNGFactory;

import java.util.HashMap;

import javax.crypto.interfaces.PBEKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.common.crypto.IRandom;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.PBEKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.core.EPBEKey;


/**
 * <p>Title: PBEKeyGen </p>
 * <p>Description:PKCS #5 'de tanimlanmis PBKDF2 algoritmasini kullanarak anahtar olusturur.
 * Anahtarin olusturmasinda kullanilacak parametreler PBEKeySpec objesi ve Hash Algoritma ismi
 * olarak verilmelidir. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Tubitak UEKAE</p>
 *
 * @author Murat Yasin Kubilay
 * @version 1.0
 */

public class PBEKeyGen
{
    public static final String DEFAULT_HASH_ALGORITHM = Registry.SHA256_HASH;
    private HashMap mMap = new HashMap();
    private String mHashName;
    private PBEKeySpec mSpec ;
    private byte[] mDerivedKeyBytes;
    private static Logger logger = LoggerFactory.getLogger(PBEKeyGen.class);

    /**
     * PBEKeyGen objesi olusturur.
     *
     * @param aPBEKeySpec PBKDF2 kullanilarak olusturulacak anahtar'in parametrelerini icerir.
     */
    public PBEKeyGen(PBEKeySpec aPBEKeySpec/*, String aHashName*/)
    {
        mSpec = aPBEKeySpec;
        mMap.put(IPBE.PASSWORD, aPBEKeySpec.getPassword());
        mMap.put(IPBE.ITERATION_COUNT, new Integer(aPBEKeySpec.getIterationCount()));
        mMap.put(IPBE.SALT, aPBEKeySpec.getSalt());

        DigestAlg digestAlg = mSpec.getDigestAlg();
        mHashName = digestAlg!=null ? GNUProviderUtil.resolveDigestName(digestAlg) : DEFAULT_HASH_ALGORITHM;
        mDerivedKeyBytes = new byte[aPBEKeySpec.getKeyLength()];
    }

    /**
     * PKCS#5 tanimlanmis PBKDF2'yi kullanarak anahtar olusturur.
     *
     * @return PBKDF2 kullanilarak olusturulan anahtar
     * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException
     *          Key olusturulamazsa
     */
    public PBEKey generateKey() throws CryptoException
    {

        IRandom pbkdf2 = PRNGFactory.getInstance(Registry.PBKDF2_PRNG_PREFIX +
                Registry.HMAC_NAME_PREFIX +
                mHashName);

        pbkdf2.init(mMap);

        try {
            pbkdf2.nextBytes(mDerivedKeyBytes, 0, mDerivedKeyBytes.length);
        }
        catch (LimitReachedException ex) {
            logger.error("this instance has reached its theoretical limit for generating non-repetitive pseudo-random data", ex);
            throw new CryptoException("this instance has reached its theoretical limit for generating non-repetitive pseudo-random data");
        }
        return new EPBEKey(mDerivedKeyBytes, mSpec.getPassword(), mSpec.getSalt(), mSpec.getIterationCount());
    }

}