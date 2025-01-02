package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import java.util.ArrayList;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;

public class DefaultCardTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();

	public DefaultCardTemplate(CardType aCardType) 
	{
		super(aCardType);
	}
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new PKCS11Ops(cardType);
		return mIslem;
	}
	
	public String[] getATRHashes() 
	{
		return ATR_HASHES.toArray(new String[0]);
	}
	
	
	public static void addATR(String aATR)
	{
		ATR_HASHES.add(aATR);
	}
	

}
