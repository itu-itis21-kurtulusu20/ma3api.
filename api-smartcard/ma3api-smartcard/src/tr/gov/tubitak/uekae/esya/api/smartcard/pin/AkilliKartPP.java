package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;


public class AkilliKartPP implements IParolaParametre {
	private ISmartCard mAkilliKart;
	private long mSessionID;	
	private String mVarsayilanSifre;
	private String mVarsayilanPuk;

	public AkilliKartPP(ISmartCard aAkilliKart, long aSessionID,String aVarsayilanSifre, String aVarsayilanPuk) {
		mAkilliKart = aAkilliKart;
		mSessionID = aSessionID;
		mVarsayilanSifre = aVarsayilanSifre;
		mVarsayilanPuk = aVarsayilanPuk;
	}

	public ISmartCard getMAkilliKart() {
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
}
