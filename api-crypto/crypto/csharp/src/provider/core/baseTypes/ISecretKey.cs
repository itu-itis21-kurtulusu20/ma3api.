using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
/**
 *A secret (symmetric) key. 
 *This interface contains no methods or constants.
 *Its only purpose is to group (and provide type safety for) secret keys. 
 *
 *Provider implementations of this interface must overwrite the equals 
 *and hashCode methods inherited from java.lang.Object, so that secret keys 
 *are compared based on their underlying key material and not based on reference. 
 *Keys that implement this interface return the string RAW as their encoding format 
 *(see getFormat), and return the raw key bytes as the result of a getEncoded method call. 
 *(The getFormat and getEncoded methods are inherited from the java.security.Key parent interface.) 
 * 
 */
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public interface ISecretKey : IKey
    {
    }
}
