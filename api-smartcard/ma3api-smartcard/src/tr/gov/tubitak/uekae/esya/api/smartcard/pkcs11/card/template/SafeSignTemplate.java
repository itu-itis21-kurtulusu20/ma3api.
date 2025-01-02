package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.util.BigIntegerUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.SafeSignOps;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.ArrayList;
import java.util.List;

public class SafeSignTemplate extends CardTemplate
{
	
	private static List<String> ATR_HASHES = new ArrayList<String>();
	
	
	static
	{
	    ATR_HASHES.add("3BBB1800C01031FE4580670412B00303000081053C");
	    ATR_HASHES.add("3BFA1800FF8131FE454A434F5032315632333165");
	    ATR_HASHES.add("3BB79400C03E31FE6553504B32339000AE");
        ATR_HASHES.add("3BF81800FF8131FE454A434F507632343143");
	}
	

	public SafeSignTemplate()
	{
		super(CardType.SAFESIGN);
	}
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new SafeSignOps();
		return mIslem;
	}

	public List<CK_ATTRIBUTE> getRSAPrivateKeyCreateTemplate(String aKeyLabel,boolean aIsSign,boolean aIsEncrypt)
    {
		List<CK_ATTRIBUTE> list = super.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN_RECOVER,aIsSign));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LOCAL,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_NEVER_EXTRACTABLE,true));
		return list;
    }
	
	public List<CK_ATTRIBUTE> getRSAPublicKeyCreateTemplate(String aKeyLabel,int aModulusBits,BigInteger aPublicExponent,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits,aPublicExponent, aIsSign, aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY_RECOVER,aIsSign));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LOCAL,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_NEVER_EXTRACTABLE,true));
		return list;
	}
	
	
	public List<CK_ATTRIBUTE> getRSAPublicKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aCert,boolean aIsSign,boolean aIsEncrypt)
	{
		byte[] modBytes = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getModulus());
		
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyImportTemplate(aLabel, aPrivKey,aCert,aIsSign,aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LOCAL,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_NEVER_EXTRACTABLE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_MODULUS_BITS,modBytes.length*8));
		return list;
	}
	
	public List<CK_ATTRIBUTE> getRSAPrivateKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aCert,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPrivateKeyImportTemplate(aLabel, aPrivKey,aCert,aIsSign,aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LOCAL,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_NEVER_EXTRACTABLE,true));
		
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
