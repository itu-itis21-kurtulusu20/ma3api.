package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_CLASS;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_ID;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_LABEL;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_MODULUS;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_PUBLIC_EXPONENT;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_TOKEN;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKO_PUBLIC_KEY;
import static tr.gov.tubitak.uekae.esya.api.smartcard.util.Constants.KEYUSAGE_DATAENCIPHER;
import static tr.gov.tubitak.uekae.esya.api.smartcard.util.Constants.KEYUSAGE_DIGITALSIGNATURE;
import static tr.gov.tubitak.uekae.esya.api.smartcard.util.Constants.KEYUSAGE_KEYENCIPHER;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.List;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.util.BigIntegerUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;


public class CardosOps extends PKCS11Ops
{
	public CardosOps()
    {
	    super(CardType.CARDOS);
    }

    public void importCertificateAndKey(long aSessionID,String aCertLabel,String aKeyLabel, RSAPrivateCrtKey aPrivKey, X509Certificate aCert)
	throws PKCS11Exception,SmartCardException
	{
		byte[] modBytes = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getModulus());
		OZET_ALICI.update(modBytes);
		byte[] id = OZET_ALICI.digest();
		
		boolean isSign = false;
		boolean isEncrypt = false;
		if(aCert!=null)
		{
			boolean[] ku = aCert.getKeyUsage();
			if(ku!=null)
			{
				isSign = ku[KEYUSAGE_DIGITALSIGNATURE];
				isEncrypt = (ku[KEYUSAGE_KEYENCIPHER] || ku[KEYUSAGE_DATAENCIPHER]);
			}
		}
		
		//Bu kart tipinde privatekey yazilinca,default olarak public keyi kendi yaratiyor.
		List<CK_ATTRIBUTE> priKeyTemplate =  CardType.CARDOS.getCardTemplate().getRSAPrivateKeyImportTemplate(aKeyLabel, aPrivKey,aCert,isSign,isEncrypt);
		mPKCS11.C_CreateObject(aSessionID, priKeyTemplate.toArray(new CK_ATTRIBUTE[0]));
		
		
		CK_ATTRIBUTE[] template = 
		{
			new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS,PKCS11Constants.CKO_PUBLIC_KEY),
			new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true),
			new CK_ATTRIBUTE(PKCS11Constants.CKA_ID,id),
			new CK_ATTRIBUTE(PKCS11Constants.CKA_MODULUS,modBytes)
		};
		
		mPKCS11.C_FindObjectsInit(aSessionID, template);
		long[] objectList = mPKCS11.C_FindObjects(aSessionID, 1);
		mPKCS11.C_FindObjectsFinal(aSessionID);
		if(objectList.length == 0)
		{
			throw new SmartCardException("Karta public key yazilmamis");
		}
		
		CK_ATTRIBUTE[] labelTemplate = 
		{
			new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,aKeyLabel)
		};
		
		
		mPKCS11.C_SetAttributeValue(aSessionID,objectList[0], labelTemplate);
		
		
		List<CK_ATTRIBUTE> certTemplate = CardType.CARDOS.getCardTemplate().getCertificateTemplate(aCertLabel, aCert);
		mPKCS11.C_CreateObject(aSessionID, certTemplate.toArray(new CK_ATTRIBUTE[0]));
	}
	
	
	public void importRSAKeyPair(long aSessionID,String aLabel, RSAPrivateCrtKey aPrivKey, byte[] aSubject,boolean aIsSign,boolean aIsEncrypt)
	throws PKCS11Exception,SmartCardException
	{
		List<CK_ATTRIBUTE> priKeyTemplate = CardType.CARDOS.getCardTemplate().getRSAPrivateKeyImportTemplate(aLabel, aPrivKey,null,aIsSign,aIsEncrypt);
		mPKCS11.C_CreateObject(aSessionID, priKeyTemplate.toArray(new CK_ATTRIBUTE[0]));
		
		byte[] modBytes = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getModulus());
		
		OZET_ALICI.update(modBytes);
		byte[] id = OZET_ALICI.digest();
		
		byte[] RSA_Public_Exponent = BigIntegerUtil.toByteArrayWithoutSignByte(aPrivKey.getPublicExponent());
		
		CK_ATTRIBUTE[] template = 
		{
				new CK_ATTRIBUTE(CKA_CLASS,CKO_PUBLIC_KEY),
				new CK_ATTRIBUTE(CKA_TOKEN,true),
				new CK_ATTRIBUTE(CKA_ID,id),
				new CK_ATTRIBUTE(CKA_MODULUS,modBytes),
				new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT,RSA_Public_Exponent)
		};
		
		long[] objectList = objeAra(aSessionID, template);
		if(objectList.length == 0)
		{
			throw new SmartCardException("Karta public key yazilmamis");
		}
		
		CK_ATTRIBUTE[] labelTemplate = 
		{
				new CK_ATTRIBUTE(CKA_LABEL,aLabel)
		};
		
		mPKCS11.C_SetAttributeValue(aSessionID, objectList[0],labelTemplate);
	}
}
