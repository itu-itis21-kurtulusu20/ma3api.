package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import java.util.ArrayList;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.AladdinOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;

public class AladdinTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
	static
	{
	    ATR_HASHES.add("3BD5180081313A7D8073C8211030");
	    ATR_HASHES.add("3BD518008131FE7D8073C82110F4");
	    ATR_HASHES.add("3B7F96000080318065B0846160FB120FFD829000");
	}
	
	public AladdinTemplate()
	{
		super(CardType.ALADDIN);
	}

	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new AladdinOps();
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
