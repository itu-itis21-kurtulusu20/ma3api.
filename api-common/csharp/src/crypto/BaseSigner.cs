using System;

//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.common.crypto
{
  /** Interface for signature implementations. */
    public interface BaseSigner
    {
        /**
         * Returns the signature bytes computed from the given data. The format of the signature depends on the underlying signature scheme.
         * @param aData Data to be signed
         * @return Signature bytes. The format of the signature depends on the underlying signature scheme.
         * @throws ESYAException
         */
        byte[] sign(byte[] aData);
        /**
         * Returns the name of signature algorithm. To avoid ambiguity, names must be the same with the ones in {@link Algorithms} class.  
         * @return the name of signature algorithm used
         */
        String getSignatureAlgorithmStr();

        IAlgorithmParameterSpec getAlgorithmParameterSpec();  //bunun olabilmesi için crypto modulundeki baseTypes içindeki Interface tanimlarinin common modulune tasinmasi gerekiyor
    }
}