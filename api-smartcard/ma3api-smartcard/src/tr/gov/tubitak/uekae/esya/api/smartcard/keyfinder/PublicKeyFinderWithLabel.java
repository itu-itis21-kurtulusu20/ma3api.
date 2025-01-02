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
public class PublicKeyFinderWithLabel implements KeyFinder
{
    ISmartCard mSC;
	long mSessionId;
	String mLabel;

	public PublicKeyFinderWithLabel(ISmartCard aSC,long aSessionId,String aLabel)
	{
		mSC = aSC;
		mSessionId = aSessionId;
		mLabel = aLabel;
	}

	public KeySpec getKeySpec()
	throws SmartCardException,PKCS11Exception
	{
		return mSC.readPublicKeySpec(mSessionId, mLabel);
	}

	public int getKeyLength() throws SmartCardException, PKCS11Exception {

		int keyLength = -1;
		KeySpec publicKeySpec = mSC.readPublicKeySpec(mSessionId, mLabel);
		try {
			keyLength = KeyUtil.getKeyLength(publicKeySpec);
		} catch (Exception e) {
			throw new SmartCardException("Problem in getting key length..!");
		}
		return keyLength;
	}
}
