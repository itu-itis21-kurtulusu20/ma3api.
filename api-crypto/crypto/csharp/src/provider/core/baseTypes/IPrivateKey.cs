using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
/**
 * <p>A private key. This interface contains no methods or constants.
 * It merely serves to group (and provide type safety for) all private key
 * interfaces.
 *
 * Note: The specialized private key interfaces extend this interface.
 * See, for example, the DSAPrivateKey interface in
 * <code>java.security.interfaces</code>.
 *
 * @see Key
 * @see PublicKey
 * @see Certificate
 * @see Signature#initVerify
 * @see java.security.interfaces.DSAPrivateKey
 * @see java.security.interfaces.RSAPrivateKey
 * @see java.security.interfaces.RSAPrivateCrtKey
 *
 * @version 1.30 05/11/17
 * @author Benjamin Renaud
 * @author Josh Bloch
 */
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public interface IPrivateKey : IKey
    {
    }
}
