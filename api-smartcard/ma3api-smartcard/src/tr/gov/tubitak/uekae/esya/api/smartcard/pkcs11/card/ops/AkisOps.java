package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template.ICardTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;


public class AkisOps extends PKCS11Ops
{

	public AkisOps(CardType aCardType)
    {
	    super(aCardType);
	}

    public void createKeyPair(long aSessionID,String aKeyLabel, int aModulusBits,BigInteger aPublicExponent, boolean aIsSign, boolean aIsEncrypt)
	throws PKCS11Exception,IOException,SmartCardException
	{
		CK_MECHANISM mech = new CK_MECHANISM(0L);
		mech.mechanism = CKM_RSA_PKCS_KEY_PAIR_GEN;
		mech.pParameter = null;
		
		ICardTemplate kartBilgi = CardType.AKIS.getCardTemplate();
		
		CK_ATTRIBUTE[] pubKeyTemplate = kartBilgi.getRSAPublicKeyCreateTemplate(aKeyLabel,aModulusBits,aPublicExponent,aIsSign,aIsEncrypt).toArray(new CK_ATTRIBUTE[0]);
		CK_ATTRIBUTE[] priKeyTemplate = kartBilgi.getRSAPrivateKeyCreateTemplate(aKeyLabel,aIsSign,aIsEncrypt).toArray(new CK_ATTRIBUTE[0]);
		
		mPKCS11.C_GenerateKeyPair(aSessionID, mech, pubKeyTemplate, priKeyTemplate);
		
	}
	
	public void updatePrivateData(long aSessionID,String aLabel,byte[] aValue)
	throws PKCS11Exception,SmartCardException
	{
		_updateData( aSessionID, aLabel, aValue,true);
	}
	
	public void updatePublicData(long aSessionID,String aLabel,byte[] aValue)
	throws PKCS11Exception,SmartCardException
	{
		_updateData(aSessionID,aLabel,aValue,false);
	}
	
	public void changeUserPin (byte[] aSOPin, byte[] aUserPin, long aSessionID)
	throws PKCS11Exception
	{
		unBlockPIN(aSOPin, aUserPin, aSessionID);
	}
	
	public void setSOPin (byte[] aSOPin, byte[] aNewSOPin, long aSessionID)
	throws PKCS11Exception
	{
		changePUK(aSOPin, aNewSOPin, aSessionID);
	}
	
	private void _updateData(long aSessionID,String aLabel,byte[] aValue,boolean aIsPrivate)
	throws PKCS11Exception,SmartCardException
	{
		List<CK_ATTRIBUTE> aramaSablon = new ArrayList<CK_ATTRIBUTE>();
		aramaSablon.add(new CK_ATTRIBUTE(CKA_CLASS,CKO_DATA));
		aramaSablon.add(new CK_ATTRIBUTE(CKA_TOKEN,true));
		aramaSablon.add(new CK_ATTRIBUTE(CKA_PRIVATE,aIsPrivate));
		aramaSablon.add(new CK_ATTRIBUTE(CKA_LABEL,aLabel));
		
		long[] objectList = objeAra(aSessionID, aramaSablon.toArray(new CK_ATTRIBUTE[0]));
		
		if(objectList.length == 0)
		{
			throw new SmartCardException(aLabel+ " isimli nesne kartta bulunamadi.");
		}
		
		for(long objectID:objectList)
		{
			mPKCS11.C_DestroyObject(aSessionID, objectID);
		}
		
		aramaSablon.add(new CK_ATTRIBUTE(CKA_VALUE,aValue));
		mPKCS11.C_CreateObject(aSessionID, aramaSablon.toArray(new CK_ATTRIBUTE[0]));
		
	}
	
	

}
