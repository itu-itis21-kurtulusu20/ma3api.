package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.GemPlusOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;


public class GemPlusTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
	static
	{
	    ATR_HASHES.add("3B7D94000080318065B08301029083009000");
	    ATR_HASHES.add("3B6D000080318065B08301029083009000");
	    ATR_HASHES.add("3B6D00008065B08301019083009000"); //Historical Byte içermiyor.
	}
	
	public GemPlusTemplate()
	{
		super(CardType.GEMPLUS);
	}
		
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new GemPlusOps();
		return mIslem;
	}

	
	public List<CK_ATTRIBUTE> getRSAPrivateKeyCreateTemplate(String aKeyLabel,boolean aIsSign,boolean aIsEncrypt)
    {
		
		List<CK_ATTRIBUTE> list = super.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN_RECOVER,aIsSign));
		
		return list;
    }

	public List<CK_ATTRIBUTE> getRSAPublicKeyCreateTemplate(String aKeyLabel,int aModulusBits,BigInteger aPublicExponent, boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aPublicExponent, aIsSign, aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY_RECOVER,aIsSign));
		
		return list;
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
