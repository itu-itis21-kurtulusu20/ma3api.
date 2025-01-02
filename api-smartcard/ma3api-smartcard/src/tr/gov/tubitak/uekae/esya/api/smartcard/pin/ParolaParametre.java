package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import javax.smartcardio.Card;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;


public class ParolaParametre {
	private SmartOp mKartIslem;
	private SmartCard mAkilliKart;
	private long mSessionID;	
	private String mVarsayilanSifre;
	private String mVarsayilanPuk;
	
	private String mDizinAdi;
	private byte[] mSPIN;
	private Card mCard;
	
	public ParolaParametre(SmartCard aAkilliKart, long aSessionID,String aVarsayilanSifre, String aVarsayilanPuk) {
		mAkilliKart = aAkilliKart;
		mSessionID = aSessionID;
		mVarsayilanSifre = aVarsayilanSifre;
		mVarsayilanPuk = aVarsayilanPuk;
	}
	public ParolaParametre(SmartOp aKartIslem, String aVarsayilanSifre, String aVarsayilanPuk)
	{
		mKartIslem = aKartIslem;
		mVarsayilanSifre = aVarsayilanSifre;
		mVarsayilanPuk = aVarsayilanPuk;
	}

	public ParolaParametre(Card aCard, String aDizinAdi, byte[] aPuk) {
		mCard = aCard;
		mSPIN = aPuk;
		mDizinAdi = aDizinAdi;
	}
	
	public SmartOp getMKartIslem() {
		return mKartIslem;
	}
	
	public SmartCard getMAkilliKart() {
		return mAkilliKart;
	}

	public long getMSessionID() {
		return mSessionID;
	}

	public String getMVarsayilanSifre() {
		return mVarsayilanSifre;
	}

	public String getMVarsayilanPuk() {
		return mVarsayilanPuk;
	}

	public String getMDizinAdi() {
		return mDizinAdi;
	}

	public byte[] getMSPIN() {
		return mSPIN;
	}

	public Card getMCard() {
		return mCard;
	}
}
