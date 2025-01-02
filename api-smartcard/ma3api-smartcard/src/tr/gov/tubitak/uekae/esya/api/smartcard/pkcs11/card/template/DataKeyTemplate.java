package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.ArrayList;
import java.util.List;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.DataKeyOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;

public class DataKeyTemplate extends CardTemplate
{
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
	static
	{
	    ATR_HASHES.add("3BFF1100008131FE4D8025A00000005657444B3333300600D0");
	}
	
	public DataKeyTemplate()
	{
		super(CardType.DATAKEY);
	}
	

	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new DataKeyOps();
		return mIslem;
	}

	public List<CK_ATTRIBUTE> getRSAPrivateKeyCreateTemplate(String aKeyLabel,boolean aIsSign,boolean aIsEncrypt )
    {
		List<CK_ATTRIBUTE> list = super.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN_RECOVER,aIsSign));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE,true));
		return list;
    }

	public List<CK_ATTRIBUTE> getRSAPublicKeyCreateTemplate(String aKeyLabel,int aModulusBits,BigInteger aPublicExponent,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits,aPublicExponent, aIsSign, aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY_RECOVER,aIsSign));
		
		return list;
	}
	
	public List<CK_ATTRIBUTE> getRSAPublicKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aCert,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyImportTemplate(aLabel, aPrivKey,aCert,aIsSign,aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY_RECOVER,false));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DERIVE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP,false));
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
