package tr.gov.tubitak.uekae.esya.api.crypto.alg;

import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import java.util.Arrays;

/**
 * @author ayetgin
 */

public enum AsymmetricAlg implements Algorithm
{
    RSA     (_algorithmsValues.rsaEncryption,   "RSA"       ),
    //RSA_PSS (_algorithmsValues.id_RSASSA_PSS,   "RSA_PSS"   ),
    //RSA_ISO9796d2 (_algorithmsValues.id_RSASS_ISO9796d2, "RSA_ISO9796d2"),
    DSA     (_algorithmsValues.id_dsa,          "DSA"       ),
    ECDSA   (_algorithmsValues.id_ecPublicKey,  "ECDSA"     );

    private String mName;
    private int[] mOID;

    AsymmetricAlg(int[] aOID, String aName)
    {
        mName = aName;
        mOID = aOID;
    }

    public String getName()
    {
        return mName;
    }

    public int[] getOID()
    {
        return mOID;
    }

    public static AsymmetricAlg fromOID(int[] aOID){
        for (AsymmetricAlg alg : AsymmetricAlg.values()) {
              if (Arrays.equals(aOID, alg.getOID()))
                  return alg;
        }
        return null;
    }

    public static AsymmetricAlg fromName(String algName){
        for (AsymmetricAlg alg : values()) {
            if (alg.getName().equalsIgnoreCase(algName))
                return alg;
        }

        return null;
    }
}
