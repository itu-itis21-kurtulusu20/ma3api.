package tr.gov.tubitak.uekae.esya.api.crypto.params;


import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.security.spec.AlgorithmParameterSpec;

/**
 * @author ayetgin
 */

public interface AlgorithmParams  extends AlgorithmParameterSpec{

    byte[] getEncoded() throws CryptoException;

}
