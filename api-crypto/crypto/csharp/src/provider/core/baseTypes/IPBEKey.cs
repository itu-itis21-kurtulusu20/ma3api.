using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
/**
 * The interface to a PBE key. 
 */
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public interface IPBEKey : ISecretKey
    {
        char[] getPassword();
        byte[] getSalt();
        int getIterationCount();
    }
}
