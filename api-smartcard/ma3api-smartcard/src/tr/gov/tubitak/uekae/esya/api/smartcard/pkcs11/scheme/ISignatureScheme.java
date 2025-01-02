package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/17/11
 * Time: 8:42 AM
 */

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

/**
 * Used to specify the mechanism and signature input value for different algorithms
 */
public interface ISignatureScheme
{
    void init(boolean aIsSigning);
    P11SignParameters getSignParameters(byte[] aTobeSigned) throws SmartCardException, PKCS11Exception;
}
