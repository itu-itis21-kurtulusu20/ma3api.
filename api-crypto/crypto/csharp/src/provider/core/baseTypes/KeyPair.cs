/**
 * This class is a simple holder for a key pair (a public key and a
 * private key). It does not enforce any security, and, when initialized,
 * should be treated like a PrivateKey.
 *
 * @see PublicKey
 * @see PrivateKey
 *
 * @version 1.16 05/11/17
 * @author Benjamin Renaud
 */
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public class KeyPair
    {
        private readonly IPrivateKey _privateKey;
        private readonly IPublicKey _publicKey;

        /**
         * Constructs a key pair from the given public key and private key.
         *
         * <p>Note that this constructor only stores references to the public
         * and private key components in the generated key pair. This is safe,
         * because <code>Key</code> objects are immutable.
         *
         * @param publicKey the public key.
         *
         * @param privateKey the private key.
         */
        public KeyPair(IPublicKey publicKey, IPrivateKey privateKey)
        {
            this._publicKey = publicKey;
            this._privateKey = privateKey;
        }

        /**
         * Returns a reference to the public key component of this key pair.
         *
         * @return a reference to the public key.
         */
        public IPublicKey getPublic()
        {
            return _publicKey;
        }

        /**
        * Returns a reference to the private key component of this key pair.
        *
        * @return a reference to the private key.
        */
        public IPrivateKey getPrivate()
        {
            return _privateKey;
        }
    }
}
