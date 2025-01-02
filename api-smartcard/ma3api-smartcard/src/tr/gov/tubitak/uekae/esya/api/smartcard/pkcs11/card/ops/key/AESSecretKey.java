/**
 * This class is used for generating/importing AES keys.
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key;

import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

/**
 *  @deprecated do no use or implement, use AESKeyTemplate
 *  @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate
 * @author aslihan.kubilay
 *
 */
@Deprecated
public class AESSecretKey extends SecretKey {

	/**
	 * Used for creating AES key in token. For AES,key length must be 16,24 or 32.
	 * @param aLabel label to be given to the generated key
	 * @param aKeySize length of the key in bytes
	 * @throws SmartCardException If key length is not 16,24 or 32.
	 */
	public AESSecretKey(String aLabel,int aKeySize)
	throws SmartCardException
	{
		if(aKeySize != 16 && aKeySize != 24 && aKeySize != 32)
			throw new SmartCardException("AES anahtar boyu 16, 24 veya 32 byte olabilir");
		
		mKeySize = aKeySize;
		mLabel = aLabel;
	}
	
	
	/**
	 * Used for importing AES key to the token.
	 * 
	 * @param aLabel label to be given to the imported key
	 * @param aValue key value
	 * @throws SmartCardException If given value's length is not 16,24 or 32.
	 */
	public AESSecretKey(String aLabel,byte[] aValue)
	throws SmartCardException
	{
		int keySize = aValue.length;
		if(keySize != 16 && keySize != 24 && keySize != 32)
			throw new SmartCardException("AES anahtar boyu 16, 24 veya 32 byte olabilir");
		
		mKeySize = keySize;
		mValue = aValue;
		mLabel = aLabel;
	}
	
	
	/**
	 * getGenerationMechanism returns pkcs11 constant value for AES key generation.
	 */
	public long getGenerationMechanism() 
	{
		return PKCS11Constants.CKM_AES_KEY_GEN;
	}

	/**
	 * getKeyType returns pkcs11 constant value for AES key type.
	 */
	public long getKeyType() 
	{
		return PKCS11Constants.CKK_AES;
	}	
}
