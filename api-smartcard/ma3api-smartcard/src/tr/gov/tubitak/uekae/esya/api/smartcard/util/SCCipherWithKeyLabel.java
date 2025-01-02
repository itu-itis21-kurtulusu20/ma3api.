package tr.gov.tubitak.uekae.esya.api.smartcard.util;


import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

import java.security.spec.AlgorithmParameterSpec;


public class SCCipherWithKeyLabel implements BaseCipher
{
	private CardType mKartTipi = null;
	private String mParola = "";
	private String mAnahtarAdi = "";
	private long mSlot = -1;
	private long mSessionId = -1;
	private ISmartCard mAkilliKart;
	private String mAlgorithm = "";
	private AlgorithmParameterSpec mParams = null;
	

	/**
	 * 
	 * @param aSmartCard
	 * @param aSessionId
	 * @param aSlotID
	 * @param aKeyLabel
	 */
	public SCCipherWithKeyLabel(ISmartCard aSmartCard, long aSessionId,long aSlotID, String aKeyLabel)
	{
		mAkilliKart = aSmartCard;
		mSessionId = aSessionId;
		mSlot = aSlotID;
		mAnahtarAdi = aKeyLabel;
		mAlgorithm = Algorithms.CIPHER_RSA_PKCS1;
	}

	public SCCipherWithKeyLabel(CardType aKartTipi, String aParola, String aAnahtarAdi, long aSlot)
	{
		mKartTipi = aKartTipi;
		mParola = aParola;
		mAnahtarAdi = aAnahtarAdi;
		mSlot = aSlot;
		mAlgorithm = Algorithms.CIPHER_RSA_PKCS1;
	}
	
	
	public SCCipherWithKeyLabel(ISmartCard aSmartCard, long aSessionId,long aSlotID, String aKeyLabel,String aAlgorithm,AlgorithmParameterSpec aParams)
	{
		mAkilliKart = aSmartCard;
		mSessionId = aSessionId;
		mSlot = aSlotID;
		mAnahtarAdi = aKeyLabel;
		mAlgorithm = aAlgorithm;
		mParams = aParams;
	}

	public SCCipherWithKeyLabel(CardType aKartTipi, String aParola, String aAnahtarAdi, long aSlot,String aAlgorithm,AlgorithmParameterSpec aParams)
	{
		mKartTipi = aKartTipi;
		mParola = aParola;
		mAnahtarAdi = aAnahtarAdi;
		mSlot = aSlot;
		mAlgorithm = aAlgorithm;
		mParams = aParams;
	}

	public byte[] doFinal(byte[] aData) 
	throws ESYAException 
	{
		try
		{
			if(mAkilliKart != null)
			{
				return SmartOp.decrypt(mAkilliKart, mSessionId, mSlot, mAnahtarAdi, aData,mAlgorithm,mParams);
			}
			else
			{
				SmartOp so = new SmartOp(mSlot, mKartTipi, mParola);
				return so.decrypt(mAnahtarAdi, aData,mAlgorithm,mParams);
			}
		}
		catch(Exception aEx)
		{
			throw new ESYAException("Kartta sifre cozulurken hata olustu", aEx);
		}
	}

	public String getCipherAlgorithmStr() 
	{
		return mAlgorithm;
	}



}
