package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import java.util.ArrayList;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.ATIKHSMOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;

public class ATIKHSMTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
    public ATIKHSMTemplate()
	{
		super(CardType.ATIKHSM);
	}
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new ATIKHSMOps();
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
