using System;
using System.Linq;
using System.Reflection;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    /**
     * 
     * Mask Generation Function.
     *
     * @author ayetgin
     */
    public class MGF
    {
        public static readonly MGF MGF1 = new MGF(_algorithmsValues.id_mgf1, "MGF1");

        private readonly int[] mOID;
        private readonly String mName;

        private MGF(int[] aOID, String aName)
        {
            mName = aName;
            mOID = aOID;
        }

        public int[] getOID()
        {
            return mOID;
        }

        public String getName()
        {
            return mName;
        }

        public static MGF fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgorithmIdentifier)
        {
            foreach (FieldInfo fieldInfo in typeof (MGF).GetFields())
            {
                MGF alg = (MGF) fieldInfo.GetValue(null);
                if (alg.getOID().SequenceEqual(aAlgorithmIdentifier.getObject().algorithm.mValue))
                    return alg;
            }
            return null;
        }

        public static MGF fromOID(int[] aOID)
        {
            foreach (FieldInfo fieldInfo in typeof (MGF).GetFields())
            {
                MGF alg = (MGF) fieldInfo.GetValue(null);
                if (aOID.SequenceEqual(alg.getOID()))
                    return alg;
            }
            return null;
        }

        public static MGF fromName(String algName)
        {
            foreach (FieldInfo fieldInfo in typeof (MGF).GetFields())
            {
                MGF alg = (MGF) fieldInfo.GetValue(null);
                if (alg.getName().Equals(algName, StringComparison.OrdinalIgnoreCase))
                    return alg;
            }

            return null;
        }
    }
}