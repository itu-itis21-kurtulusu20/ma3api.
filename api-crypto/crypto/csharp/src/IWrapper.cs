
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IWrapper
    {
        /**
     * @param aKey to be used for cipher process
     */
        void init(byte[] aKey);

        /**
         *
         * @param aKey to be wrapped.
         * @return wrapped key
         */
        byte[] process(byte[] aKey);

        /**
         * @return if this instance wraps or unwraps.
         */
        bool isWrapper();
    }
}
