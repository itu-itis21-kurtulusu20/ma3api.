using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
/**
 * <p>A public key. This interface contains no methods or constants.
 * It merely serves to group (and provide type safety for) all public key
 * interfaces.
 *
 * Note: The specialized public key interfaces extend this interface.
 * See, for example, the DSAPublicKey interface in
 * <code>java.security.interfaces</code>.
 *
 * @see Key
 * @see PrivateKey
 * @see Certificate
 * @see Signature#initVerify
 * @see java.security.interfaces.DSAPublicKey
 * @see java.security.interfaces.RSAPublicKey
 *
 * @version 1.33 05/11/17
 */
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public interface IPublicKey : IKey
    {
    }
}
