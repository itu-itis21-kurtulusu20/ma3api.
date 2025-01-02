package tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.spec.KeySpec;

public class ModulusFinderFromObjectID implements KeyFinder
{

    ISmartCard mSmartCard;
	long mSessionId;
	long objID;
	
	
	public ModulusFinderFromObjectID(ISmartCard aSC,long aSessionId,long aObjID)
	{
		mSmartCard = aSC;
		mSessionId = aSessionId;
		objID = aObjID;
	}


	public KeySpec getKeySpec() throws SmartCardException, PKCS11Exception 
	{
		throw new SmartCardException("This function does not supported");
	}


	public int getKeyLength() throws SmartCardException, PKCS11Exception
	{
		return mSmartCard.getModulusOfKey(mSessionId, objID).length * 8;
	}
}
