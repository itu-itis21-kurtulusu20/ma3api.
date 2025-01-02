/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.spec.KeySpec;

/**
 * @author aslihan.kubilay
 *
 */
public interface KeyFinder 
{
	
	KeySpec getKeySpec() throws SmartCardException,PKCS11Exception;
		
	int getKeyLength() throws SmartCardException, PKCS11Exception;
	
}
