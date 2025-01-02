package tr.gov.tubitak.uekae.esya.api.crypto.alg;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import java.util.Arrays;

/**
 * Mask Generation Function.
 *
 * @author ayetgin
 */

public enum MGF
{
    MGF1 (_algorithmsValues.id_mgf1, "MGF1");

    private int[] mOID;
    private String mName;

    private MGF(int[] aOID, String aName)
    {
        mOID = aOID;
        mName = aName;
    }

    public int[] getOID()
    {
        return mOID;
    }

    public String getName()
    {
        return mName;
    }

    public static MGF fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgorithmIdentifier){
        for (MGF alg : MGF.values()) {
              if (Arrays.equals(aAlgorithmIdentifier.getAlgorithm().value, alg.getOID()))
                  return alg;
        }
        return null;
    }

    public static MGF fromOID(int[] aOID){
        for (MGF alg : MGF.values()) {
              if (Arrays.equals(aOID, alg.getOID()))
                  return alg;
        }
        return null;
    }

    public static MGF fromName(String algName){
        for (MGF alg : values()) {
            if (alg.getName().equalsIgnoreCase(algName))
                return alg;
        }

        return null;
    }

}
