package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import sun.security.pkcs11.wrapper.CK_MECHANISM;

public class RSAPKCS_ES implements IEncryptionScheme{

	
	public RSAPKCS_ES()
	{}
	
	public void init(boolean aEncryption) 
	{}

	public CK_MECHANISM getMechanism() 
	{
		CK_MECHANISM mech = new CK_MECHANISM(0L);
		mech.mechanism = PKCS11Constants.CKM_RSA_PKCS;
		return mech;
	}

	public byte[] getResult(byte[] aData) 
	throws SmartCardException 
	{
		return aData;
	}

	public byte[] finalize(byte[] aData) 
	{
		return aData;
	}

}
