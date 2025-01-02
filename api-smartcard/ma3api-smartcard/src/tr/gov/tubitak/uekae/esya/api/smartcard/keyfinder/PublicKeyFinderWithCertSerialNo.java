/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.spec.KeySpec;

/**
 * @author aslihan.kubilay
 *
 */
public class PublicKeyFinderWithCertSerialNo implements KeyFinder {

	ISmartCard mSmartCard;
	long mSessionId;
	byte[] mCertSerialNo;
	
	public PublicKeyFinderWithCertSerialNo(ISmartCard aSC,long aSessionId,byte[] aCertSerialNo)
	{
		mSmartCard = aSC;
		mSessionId = aSessionId;
		mCertSerialNo = aCertSerialNo;
	}
	
	
	
	public KeySpec getKeySpec() throws SmartCardException, PKCS11Exception 
	{
		return mSmartCard.readPublicKeySpec(mSessionId, mCertSerialNo);
	}



	public int getKeyLength() throws SmartCardException, PKCS11Exception
	{
		int keyLength = -1;
		KeySpec publicKeySpec = mSmartCard.readPublicKeySpec(mSessionId, mCertSerialNo);
		try {
			keyLength = KeyUtil.getKeyLength(publicKeySpec);
		} catch (Exception e) {
			throw new SmartCardException("Problem in getting key length..!");
		}
		return keyLength;
	}
}
