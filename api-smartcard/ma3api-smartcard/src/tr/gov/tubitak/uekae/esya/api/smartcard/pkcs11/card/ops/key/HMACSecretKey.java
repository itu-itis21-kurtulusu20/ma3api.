/**
 * This class is used for generating/importing HMAC keys.
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated do no use or implement, use AESKeyTemplate
 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate
 * @author aslihan.kubilay
 *
 */
@Deprecated
public class HMACSecretKey extends SecretKey
{	
	String mDigestAlg = Algorithms.DIGEST_SHA1;
	long mKeyType = PKCS11Constants.CKK_GENERIC_SECRET;
	long mGenerationMechanism = PKCS11Constants.CKM_GENERIC_SECRET_KEY_GEN;
	
	
    	/**
	 * Used for generating hmac keys in token.
	 * 
	 * @param aLabel  label to be given to the generated key
	 * @param aKeySize key length
	 */
	public HMACSecretKey(String aLabel,int aKeySize)
	{
		mLabel = aLabel;
		mKeySize = aKeySize;
	}
	
	
	/**
	 * 
	 * @param aLabel label to be given to the generated key
	 * @param aKeySize key length
	 * @param aDigestAlg digest algorithm for macing 
	 */
	public HMACSecretKey(String aLabel,int aKeySize,String aDigestAlg)
	{
		mLabel = aLabel;
		mKeySize = aKeySize;
		mDigestAlg = aDigestAlg;
	}
	
	/**
	 * Used for importing hmac key to token.
	 * 
	 * @param aLabel label to be given to the imported key.
	 * @param aValue key value 
	 */
	public HMACSecretKey(String aLabel,byte[] aValue)
	{
		mKeySize = aValue.length;
		mValue = aValue;
		mLabel = aLabel;
	}
	
	
	/**
	 * 
	 * @param aLabel label to be given to the generated key
	 * @param aValue key value 
	 * @param aDigestAlg digest algorithm for macing 
	 */
	public HMACSecretKey(String aLabel,byte[] aValue,String aDigestAlg)
	{
		mKeySize = aValue.length;
		mValue = aValue;
		mLabel = aLabel;
		mDigestAlg = aDigestAlg;
	}
	
	/**
	 * getGenerationMechanism returns pkcs11 constant value for generic secret key generation.
	 */
	public long getGenerationMechanism() 
	{
		return mGenerationMechanism;
	}

	/**
	 * getKeyType returns pkcs11 constant value for generic secret key type.
	 */
	public long getKeyType() 
	{
		return mKeyType;
	}
	
	
	
	
	/**
	 * getCreationTemplate returns template for key generation in token
	 */
	public List<CK_ATTRIBUTE> getCreationTemplate()
	{
		List<CK_ATTRIBUTE> list = new ArrayList<CK_ATTRIBUTE>();
		
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS,PKCS11Constants.CKO_SECRET_KEY));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE,PKCS11Constants.CKK_GENERIC_SECRET));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,getLabel()));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN,getKeySize()));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,true));
		return list;
		                      
	}
	
	public List<CK_ATTRIBUTE> getImportTemplate()
	{
		List<CK_ATTRIBUTE> list = new ArrayList<CK_ATTRIBUTE>();
			         		 
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS,PKCS11Constants.CKO_SECRET_KEY));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,getLabel()));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE,getKeyType()));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE,getValue()));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,true));
		return list;
			         		      
	}
	
	public String getDigestAlgorithm()
	{
	    return mDigestAlg;
	}
	
	public void setGenerationMechanism(long aMechanism)
	{
	    mGenerationMechanism = aMechanism;
	}
	
	public void setKeyType(long aKeyType)
	{
	    mKeyType = aKeyType;
	}
}
