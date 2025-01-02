package tr.gov.tubitak.uekae.esya.api.crypto.alg;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.util.Arrays;

/**
 * Definition for Message Digest algorithm, such as MD5 or SHA. Message digests are secure one-way hash functions that
 * take arbitrary-sized data and output a fixed-length hash value. 
 *
 * @author ayetgin
 */

public enum DigestAlg implements Algorithm
{
    @Deprecated
    SHA1    (_algorithmsValues.sha_1,       "SHA-1",    20),
    SHA224  (_algorithmsValues.id_sha224,   "SHA-224",  28),
    SHA256  (_algorithmsValues.id_sha256,   "SHA-256",  32),
    SHA384  (_algorithmsValues.id_sha384,   "SHA-384",  48),
    SHA512  (_algorithmsValues.id_sha512,   "SHA-512",  64),
    @Deprecated
    MD5     (_algorithmsValues.md5,         "MD5",      16),
    @Deprecated
    RIPEMD  (_algorithmsValues.ripemd_160,  "RIPEMD",   20);

    private int[] mOID;
    private String mName;
    private int mDigestLength;
    /**
     * Creates digest algorithm from parameters
     * 
     */
    DigestAlg(int[] aOid, String aName, int aDigestLength)
    {
        mOID = aOid;
        mName = aName;
        mDigestLength = aDigestLength;
    }
    /**
     * Returns OID of digest algorithm
     * @return 
     */
    public int[] getOID()
    {
        return mOID;
    }
    /**
     * Returns name of digest algorithm
     * @return 
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Returns the length of the digest in bytes
     * @return the length of the digest in bytes
     */
    public int getDigestLength()
    {
        return mDigestLength;
    }
    /**
     * Find  digest algorithm from AlgorithmIdentifier by comparing it with known ones and return
     * @return 
     */
    public static DigestAlg fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgorithmIdentifier){
        for (DigestAlg alg : DigestAlg.values()) {
              if (Arrays.equals(aAlgorithmIdentifier.getAlgorithm().value, alg.getOID()))
                  return alg;
        }
        return null;
    }
    /**
     * Find  digest algorithm from OID by comparing it with known ones and return
     * @return 
     */
    public static DigestAlg fromOID(int[] aOID){
        for (DigestAlg alg : DigestAlg.values()) {
              if (Arrays.equals(aOID, alg.getOID()))
                  return alg;
        }
        return null;
    }
    /**
     * Find  digest algorithm from name by comparing it with known ones and return
     * @return 
     */
    public static DigestAlg fromName(String algName){
        for (DigestAlg alg : values()) {
            if (alg.getName().equalsIgnoreCase(algName))
                return alg;
        }

        return null;
    }
    
   /* RFC4055 pg.15  Bölüm 6. ASN1 Modules
    Özet algoritmalarında MUST değil.
    When the following OIDs are used in an AlgorithmIdentifier the parameters SHOULD be absent, but if the parameters are present,
    they MUST be NULL.
	*/
    /**
     * Create an EAlgorithmIdentifier from digest algorithm and return
     * @return 
     */
    public EAlgorithmIdentifier toAlgorithmIdentifier()
    {
    	return new EAlgorithmIdentifier(new AlgorithmIdentifier(mOID));
    }

}
