using System;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public interface IAlgorithm
    {
        String getName();
        int[] getOID();
    }
}
