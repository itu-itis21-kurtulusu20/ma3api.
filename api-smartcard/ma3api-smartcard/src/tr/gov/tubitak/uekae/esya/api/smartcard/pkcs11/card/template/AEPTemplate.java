package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import java.util.ArrayList;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.AEPOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;


public class AEPTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
    	public AEPTemplate()
	{
		super(CardType.AEPKEYPER);
	}


	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new AEPOps();
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
