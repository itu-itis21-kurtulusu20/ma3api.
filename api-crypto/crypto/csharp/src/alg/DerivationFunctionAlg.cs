using System;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class DerivationFunctionAlg : IAlgorithm
    {
        private readonly DerivationFunctionType mFunctionType;
        private readonly DigestAlg mDigestAlg;

        public DerivationFunctionAlg(DerivationFunctionType aFunctionType, DigestAlg aDigestAlg)
        {
            this.mFunctionType = aFunctionType;
            this.mDigestAlg = aDigestAlg;
        }

        public String getName()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public int[] getOID()
        {
            // TODO Auto-generated method stub
            return null;
        }

        public DerivationFunctionType getFunctionType()
        {
            return mFunctionType;
        }

        public DigestAlg getDigestAlg()
        {
            return mDigestAlg;
        }

    }
}
