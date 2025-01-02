/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import sun.security.pkcs11.wrapper.CK_MECHANISM;

/**
 * @author aslihan.kubilay
 *
 */
public interface IEncryptionScheme {
	
	public void init(boolean aEncryption);
	public CK_MECHANISM getMechanism() throws SmartCardException;
	public byte[] getResult(byte[] aData) throws SmartCardException;
	public byte[] finalize(byte[] aData);
}
