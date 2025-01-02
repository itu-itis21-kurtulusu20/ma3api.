package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;


public class KartIslemPP implements IParolaParametre {
	private SmartOp mKartIslem;
	private String mVarsayilanSifre;
	private String mVarsayilanPuk;


	public KartIslemPP(SmartOp aKartIslem, String aVarsayilanSifre, String aVarsayilanPuk)
	{
		mKartIslem = aKartIslem;
		mVarsayilanSifre = aVarsayilanSifre;
		mVarsayilanPuk = aVarsayilanPuk;
	}
	
	public SmartOp getMKartIslem() {
		return mKartIslem;
	}

	public String getMVarsayilanSifre() {
		return mVarsayilanSifre;
	}

	public String getMVarsayilanPuk() {
		return mVarsayilanPuk;
	}
}
