package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;



public class GemPlusOps extends PKCS11Ops
{
	public GemPlusOps()
	{
		super(CardType.GEMPLUS);
	}
	
	/*public void changePassword(String aOldPass,String aNewPass,long aSessionID)
	throws PKCS11Exception
	{
		if(mESYAPKCS11 == null)
			mESYAPKCS11 = new ESYAPKCS11(msCardInfoMap.get(CardType.GEMPLUS).getLibName());
		
		login(aSessionID, aOldPass);
		int sonuc = mESYAPKCS11.changePassword(aOldPass, aNewPass,(int) aSessionID);
		if(sonuc != PKCS11Constants.CKR_OK)
			throw PKCS11ExceptionFactory.getPKCS11Exception(sonuc);
		logout(aSessionID);
	}*/
}
