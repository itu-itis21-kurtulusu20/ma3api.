package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.KeyCorpOps;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.smartcard.util.Constants.*;

public class KeyCorpTemplate extends CardTemplate
{
	
	private static List<String> ATR_HASHES = new ArrayList<String>();
    	static
    	{
//    	    ATR_HASHES.add("3BB79400C03E31FE6553504B32339000AE");
    	}
    
    
	public KeyCorpTemplate()
	{
		super(CardType.KEYCORP);
	}
	
	public synchronized IPKCS11Ops getPKCS11Ops()
	{
		if(mIslem == null)
			mIslem = new KeyCorpOps();
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
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits,aPublicExponent, aIsSign, aIsEncrypt);
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP,true));
		list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY_RECOVER,aIsSign));
		
		return list;
	}
	
	public List<CK_ATTRIBUTE> getRSAPrivateKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aSertifika,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPrivateKeyImportTemplate(aLabel, aPrivKey,aSertifika,aIsSign,aIsEncrypt);
		
		String label = aLabel;
		if(aSertifika != null)
		{
			boolean[] ku = aSertifika.getKeyUsage();
			if(ku!=null)
			{
				if(ku[KEYUSAGE_DIGITALSIGNATURE])
				{
					label = label+"-S";
					list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,aLabel+"-S"));
				}
				else if(ku[KEYUSAGE_KEYENCIPHER] || ku[KEYUSAGE_DATAENCIPHER])
				{
					label = label+"-X";
					list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,aLabel+"-X"));
				}
				
				for(CK_ATTRIBUTE attr:list)
				{
					if(attr.type == PKCS11Constants.CKA_LABEL)
						attr.pValue = label;
				}
			}
		}
		
		
		
		return list;
	}
	
	
	public List<CK_ATTRIBUTE> getRSAPublicKeyImportTemplate(String aLabel,RSAPrivateCrtKey aPrivKey,X509Certificate aSertifika,boolean aIsSign,boolean aIsEncrypt)
	{
		List<CK_ATTRIBUTE> list = super.getRSAPublicKeyImportTemplate(aLabel, aPrivKey,aSertifika,aIsSign,aIsEncrypt);

		String label = aLabel;
		if(aSertifika != null)
		{
			boolean[] ku = aSertifika.getKeyUsage();
			if(ku!=null)
			{
				if(ku[KEYUSAGE_DIGITALSIGNATURE])
				{
					label = label+"-S";
					list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,aLabel+"-S"));
				}
				else if(ku[KEYUSAGE_KEYENCIPHER] || ku[KEYUSAGE_DATAENCIPHER])
				{
					label = label+"-X";
					list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,aLabel+"-X"));
				}
				
				for(CK_ATTRIBUTE attr:list)
				{
					if(attr.type == PKCS11Constants.CKA_LABEL)
						attr.pValue = label;
				}
			}
		}
		
		
		
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
