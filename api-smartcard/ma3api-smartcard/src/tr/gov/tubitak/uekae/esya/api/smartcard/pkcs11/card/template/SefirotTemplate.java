package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.SefirotOps;

public class SefirotTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
	public SefirotTemplate()
	{
		super(CardType.SEFIROT);
	}
	
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new SefirotOps();
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

	public List<CK_ATTRIBUTE> getRSAPublicKeyCreateTemplate(String aKeyLabel,int aModulusBits,BigInteger aPublicExponent,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aPublicExponent,aIsSign, aIsEncrypt);
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
