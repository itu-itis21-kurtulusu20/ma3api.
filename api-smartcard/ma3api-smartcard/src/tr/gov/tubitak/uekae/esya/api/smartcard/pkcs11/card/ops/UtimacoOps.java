package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ConstantsExtended;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECSignatureTLVUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.TLV;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

public class UtimacoOps extends PKCS11Ops{

private static final int BLOCK_SIZE = 64*1024;
public static final long UTIMACO_DEFINED_CKM_AES_CMAC = 0x80000136L;


	public UtimacoOps()
	{
		super(CardType.UTIMACO);
	}

    @Override
    CK_C_INITIALIZE_ARGS get_CK_C_INITIALIZE_ARGS() {
        CK_C_INITIALIZE_ARGS args = new CK_C_INITIALIZE_ARGS();
        args.flags = CKF_OS_LOCKING_OK;
        return args;
    }

	public byte[] signData(long aSessionID,String aKeyLabel,byte[] aImzalanacak,CK_MECHANISM aMechanism)
	throws PKCS11Exception,SmartCardException
	{
		CK_ATTRIBUTE[] template = 
		{
				new CK_ATTRIBUTE(CKA_LABEL,aKeyLabel),
				new CK_ATTRIBUTE(CKA_TOKEN,true),
				new CK_ATTRIBUTE(CKA_CLASS,CKO_PRIVATE_KEY)
		};
		
		long[] objectList = objeAra(aSessionID, template);
		
		if(objectList.length == 0)
		{
			template[2] = new CK_ATTRIBUTE(CKA_CLASS,CKO_SECRET_KEY);
			objectList = objeAra(aSessionID, template);
			if(objectList.length == 0)
				throw new SmartCardException(GenelDil.mesaj(GenelDil.KARTTA_0_ANAHTARI_YOK, new String[]{ aKeyLabel }));
		}

		if(aMechanism.mechanism == PKCS11ConstantsExtended.CKM_AES_CMAC)
			aMechanism.mechanism = UTIMACO_DEFINED_CKM_AES_CMAC;

		return sign(aSessionID,aImzalanacak,objectList[0],aMechanism);
	}
	
	
	public byte[] signDataWithCertSerialNo(long aSessionID,byte[] aSerialNumber,CK_MECHANISM aMechanism,byte[] aImzalanacak)
	throws PKCS11Exception,SmartCardException
	{
		List<List<CK_ATTRIBUTE>> list = CardType.UTIMACO.getCardTemplate().getCertSerialNumberTemplates(aSerialNumber);
		
		long[] objectList = null;
		
		for(List<CK_ATTRIBUTE> tList:list)
		{
			objectList = objeAra(aSessionID, tList.toArray(new CK_ATTRIBUTE[0]));
			if(objectList.length > 0 ) break;
		}
		
		if(objectList == null || objectList.length == 0)
		{
			throw new SmartCardException("Verilen seri numarali sertifika kartta bulunamadi.");
		}
		
		CK_ATTRIBUTE[] idTemplate = {new CK_ATTRIBUTE(CKA_ID)};

		mPKCS11.C_GetAttributeValue(aSessionID, objectList[0],idTemplate);
		byte[] id = (byte[])idTemplate[0].pValue;
		
		CK_ATTRIBUTE[] privateKeyTemplate = 
		{
				new CK_ATTRIBUTE(CKA_CLASS,CKO_PRIVATE_KEY),
				new CK_ATTRIBUTE(CKA_ID,id),
				new CK_ATTRIBUTE(CKA_PRIVATE,true),
				new CK_ATTRIBUTE(CKA_TOKEN,true)
		};
		
		long[] keyList = objeAra(aSessionID, privateKeyTemplate);
		
		if(keyList.length == 0)
		{
			throw new SmartCardException("Verilen seri numarasina sahip sertifikayla ayni ID ye sahip ozel anahtar kartta bulunamadi.");
		}
		
		
		return sign(aSessionID, aImzalanacak, keyList[0], aMechanism);
	}

    public byte[] signDataWithKeyID(long aSessionID,long aKeyID,CK_MECHANISM aMechanism,byte[] aImzalanacak) throws PKCS11Exception
    {
        mPKCS11.C_SignInit(aSessionID,aMechanism,aKeyID);

        byte[] imzali = mPKCS11.C_Sign(aSessionID, aImzalanacak);

        if(aMechanism.mechanism==PKCS11Constants.CKM_ECDSA || aMechanism.mechanism==PKCS11Constants.CKM_ECDSA_SHA1)
            return ECSignatureTLVUtil.addTLVToSignature(imzali);

        return imzali;
    }
	
	private byte[] sign(long aSessionID,byte[] aImzalanacak,long aKeyId,CK_MECHANISM aMechanism)
	throws PKCS11Exception,SmartCardException
	{
		
		mPKCS11.C_SignInit(aSessionID, aMechanism, aKeyId);
		
		int length = aImzalanacak.length;
		byte[] imzali = null;
		
		if(length<BLOCK_SIZE)
		{
			imzali = mPKCS11.C_Sign(aSessionID, aImzalanacak);
		}
		else
		{
			int start = 0;
			while(length>BLOCK_SIZE)
			{
				mPKCS11.C_SignUpdate(aSessionID,0, aImzalanacak, start, BLOCK_SIZE);
				length = length-BLOCK_SIZE;
				start = start+BLOCK_SIZE;
			}
			
			mPKCS11.C_SignUpdate(aSessionID, 0, aImzalanacak, start, length);
			
			try
			{
				mPKCS11.C_SignFinal(aSessionID, 1000);
			}
			catch(Exception aEx)
			{
				if(!(aEx instanceof PKCS11Exception))
						throw new SmartCardException("C_SignFinal methodu cagrilamadi",aEx);
			}
			
		}
		
		if(aMechanism.mechanism==PKCS11Constants.CKM_ECDSA || aMechanism.mechanism==PKCS11Constants.CKM_ECDSA_SHA1)
			return ECSignatureTLVUtil.addTLVToSignature(imzali);
		
		return imzali;
	}


	public void verifyData(long aSessionID,String aKeyLabel,byte[] aData,byte[] aImza,CK_MECHANISM aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		CK_ATTRIBUTE[] template =
		{
				new CK_ATTRIBUTE(CKA_LABEL,aKeyLabel),
				new CK_ATTRIBUTE(CKA_TOKEN,true),
				new CK_ATTRIBUTE(CKA_CLASS,CKO_PUBLIC_KEY)
		};

		long[] objectList = objeAra(aSessionID,template);

		if(objectList.length == 0)
		{
			template[2] = new CK_ATTRIBUTE(CKA_CLASS,CKO_SECRET_KEY);
			objectList = objeAra(aSessionID,template);
			if(objectList.length == 0)
				throw new SmartCardException(GenelDil.mesaj(GenelDil.KARTTA_0_ANAHTARI_YOK, new String[]
				                                                                                       { aKeyLabel }));
		}

		mPKCS11.C_VerifyInit(aSessionID, aMechanism, objectList[0]);

		int length = aData.length;
		if(length < BLOCK_SIZE)
		{
			mPKCS11.C_Verify(aSessionID, aData, aImza);
		}
		else
		{
			int start = 0;
			while(length>BLOCK_SIZE)
			{
				mPKCS11.C_VerifyUpdate(aSessionID, 0, aData, start, BLOCK_SIZE);
				length = length-BLOCK_SIZE;
				start = start+BLOCK_SIZE;
			}

			mPKCS11.C_VerifyUpdate(aSessionID, 0, aData, start, length);
			mPKCS11.C_VerifyFinal(aSessionID, aImza);
		}
	}
	

	public void verifyData(long aSessionID,String aKeyLabel,byte[] aData,byte[] aImza,long aMechanism)
	throws PKCS11Exception,SmartCardException
	{
		CK_MECHANISM mech = new CK_MECHANISM(0L);
		mech.mechanism = aMechanism;

		verifyData(aSessionID, aKeyLabel, aData, aImza, mech);
	}

	private static byte[] _sequenceBoz(byte[] aSeq)
	{
		if(aSeq[0] != (byte)0x30)//SEQUENCE
            return null;
		
		int[] a = TLV.getIcerik(aSeq, 0);
		
		int[] rindex = TLV.getIcerik(aSeq, a[0]);
		int[] sindex = TLV.getIcerik(aSeq, rindex[1]+1);
		
		if(aSeq[rindex[0]] == (byte) 0x00)
		    rindex[0] = rindex[0]+1;
		
		if(aSeq[sindex[0]] == (byte) 0x00)
		    sindex[0] = sindex[0]+1;
		
		int rl = rindex[1]-rindex[0]+1;
		int sl = sindex[1]-sindex[0]+1;
		byte[] rs = new byte[rl+sl];
		
		System.arraycopy(aSeq, rindex[0], rs, 0, rl);
		System.arraycopy(aSeq, sindex[0], rs, rl, sl);
		
		return rs;
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
