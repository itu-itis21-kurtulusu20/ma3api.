package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_CLASS;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_PRIVATE;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_SERIAL_NUMBER;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_TOKEN;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKO_CERTIFICATE;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.TKartOps;


public class TKartTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
	static
	{
	    //ATR_HASHES.add("3BF2180002C10A31FE58C80874"); //Historical Byte içermediğinden kaldırıldı.
	}
	
	public TKartTemplate()
	{
		super(CardType.TKART);
	}
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new TKartOps();
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
