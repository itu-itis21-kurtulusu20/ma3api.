//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.parameters
{
    public class ECDHDecryptorParams : IDecryptorParams
    {
        private readonly byte[] mWrappedKey;
        private readonly int[] mKeyEncAlgOid;
        private readonly int[] mKeyWrapAlgOid;
        private readonly byte[] mSenderPublicKey;
        private readonly byte[] mukm;

        public ECDHDecryptorParams(byte[] wrappedKey, int[] keyEncAlgOid, int[] keyWrapAlgOid, byte[] senderPublicKey, byte[] ukm)
        {
            this.mWrappedKey = wrappedKey;
            this.mKeyEncAlgOid = keyEncAlgOid;
            this.mKeyWrapAlgOid = keyWrapAlgOid;
            this.mSenderPublicKey = senderPublicKey;
            this.mukm = ukm;
        }

        public byte[] getWrappedKey()
        {
            return mWrappedKey;
        }

        public int[] getKeyEncAlgOid()
        {
            return mKeyEncAlgOid;
        }

        public int[] getKeyWrapAlgOid()
        {
            return mKeyWrapAlgOid;
        }

        public byte[] getSenderPublicKey()
        {
            return mSenderPublicKey;
        }

        public byte[] getukm()
        {
            return mukm;
        }
    }
}
