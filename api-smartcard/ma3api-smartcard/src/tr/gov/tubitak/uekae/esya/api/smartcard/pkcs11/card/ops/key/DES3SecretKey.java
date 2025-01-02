/**
 * This class is used for generating/importing DES3 keys.
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.util.ArrayList;
import java.util.List;

/**
 *  @deprecated do no use or implement, use AESKeyTemplate
 *  @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.DES3KeyTemplate
 * @author aslihan.kubilay
 *
 */
@Deprecated
public class DES3SecretKey extends SecretKey {

	private static final int KEY_SIZE = 24;
	
	/**
	 * Used for generating DES3 key in token. Key length for DES3 is 24 bytes.
	 *
	 * @param aLabel label to be given to the DES3 key generated.
	 */
	public DES3SecretKey(String aLabel)
	{
		mKeySize = KEY_SIZE;
		mLabel = aLabel;
	}
	
	
	/**
	 * Used for importing DES3 key to the token.
	 * @param aLabel label to be given to the imported key.
	 * @param aValue key value.Must be 24 bytes long.
	 * @throws SmartCardException If length of given key value is not 24.
	 */
	public DES3SecretKey(String aLabel,byte[] aValue)
	throws SmartCardException
	{
		if(aValue.length != 24)
			throw new SmartCardException("DES3 anahtar uzunlugu 24 byte olmalidir");
		
		mKeySize = KEY_SIZE;
		mValue = aValue;
		mLabel = aLabel;
	}
	
	
	/**
	 * getGenerationMechanism returns pkcs11 constant value for DES3 key generation.
	 */
	public long getGenerationMechanism() 
	{
		return PKCS11Constants.CKM_DES3_KEY_GEN;
	}

	/**
	 * getKeyType returns pkcs11 constant value for DES3 key type.
	 */
	public long getKeyType() 
	{
		return PKCS11Constants.CKK_DES3;
	}

	/**
	 * getCreationTemplate returns template for creating DES3 key in token.
	 */
	@Override
	public List<CK_ATTRIBUTE> getCreationTemplate()
	{
		
		List<CK_ATTRIBUTE> list = new ArrayList<CK_ATTRIBUTE>();
		
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS,PKCS11Constants.CKO_SECRET_KEY));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,getLabel()));
		
		return list;
		
	}


}
