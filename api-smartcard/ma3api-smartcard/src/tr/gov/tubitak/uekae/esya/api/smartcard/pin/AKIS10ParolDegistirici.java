package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

public class AKIS10ParolDegistirici extends AKISParolaDegistirici {
	
	public void init(IParolaParametre aParametre){
		super.init(aParametre);
		mSPIN = _parolaDuzelt(mParametre.getMSPIN());
	}
	
	public void pinDegistir(byte[] aYeniPin) throws ESYAException {
		byte[] duzeltilmisPIN = _parolaDuzelt(aYeniPin);
		byte[] veri = _gonderilecekVeriOlustur(duzeltilmisPIN,duzeltilmisPIN);

		_verifyPUK();
		_phaseControl();
		_resetPINCounter();
		_changePIN(veri);
	}

	public void pukDegistir(byte[] aYeniPuk) throws ESYAException {
		byte[] duzeltilmisPUK = _parolaDuzelt(aYeniPuk);
		byte[] veri = _gonderilecekVeriOlustur(mSPIN,duzeltilmisPUK);

		_verifyPUK();
		_phaseControl();
		
		byte[] akisSistemBilgi =_getIsoCardDataAKIS(04, 10); // Bellek Bilgi

        if (akisSistemBilgi[6] == (byte) 0xAA) // olum evresi        
              throw new ESYAException("Kart Olu");
         else if (akisSistemBilgi[6] == (byte) 0xA0) // Yasam Evresi
        	_changePUK(veri);
	}

	private void _verifyPUK() throws ESYAException {
		// verify puk
		try {
			mResponse = mChannel.transmit(new CommandAPDU(0, 0x20, 0x00, 0x00,mSPIN));
			_checkResult(mResponse);
		} catch (CardException aEx) {
			throw new ESYAException("Puk verify edilemedi", aEx);
		}
	}

	private void _resetPINCounter() throws ESYAException {
		// pin resetleme
		try
		{
			mResponse = mChannel.transmit(new CommandAPDU(0x80, 0x19, 0x05, 0x3D, HexConverter.hexStringToByteArray(mParametre.getMDizinAdi())));
			_checkResult(mResponse);
		}
		catch (Exception aEx)
		{
			throw new ESYAException("Pin resetleme hatası", aEx);
		}
	}
	
	private void _changePIN(byte[] aPIN) throws ESYAException {
		// PIN degistirme
		try
		{
			mResponse = mChannel.transmit(new CommandAPDU(0x00, 0x24, 0x02,HexConverter.hexStringToByteArray(mParametre.getMDizinAdi())[1], aPIN));
			_checkResult(mResponse);
		}
		catch (Exception aEx)
		{
			throw new ESYAException("Change reference data hatası", aEx);
		}
	}
	
	private void _changePUK(byte[] aPUK) throws ESYAException {
		// PIN degistirme
		try
		{
			mResponse = mChannel.transmit(new CommandAPDU(0x00, 0x24, 0x01,HexConverter.hexStringToByteArray(mParametre.getMDizinAdi())[1], aPUK));
			_checkResult(mResponse);
		}
		catch (Exception aEx)
		{
			throw new ESYAException("Change reference data hatası", aEx);
		}
	}	
	private byte[] _gonderilecekVeriOlustur(byte[] aEskiPIN,byte[] aYeniPIN){
		
		int index = 0;
		byte[] veri = new byte[2 + aEskiPIN.length + aYeniPIN.length];
		veri[index++] = (byte) aEskiPIN.length;
		System.arraycopy(aEskiPIN, 0, veri, index, aEskiPIN.length);
		index += aEskiPIN.length;
		veri[index++] = (byte) aYeniPIN.length;
		System.arraycopy(aYeniPIN, 0, veri, index, aYeniPIN.length); 
		
		return veri;
	}

	private byte[] _getIsoCardDataAKIS(int type, int le) throws ESYAException {
		try {
			mResponse = mChannel.transmit(new CommandAPDU(0, 0xCA, 1, type, le));
			_checkResult(mResponse);
			byte data[] = mResponse.getData();
			System.arraycopy(data, 0, data, 0, data.length - 2);
			return data;
		} catch (Exception e) {
			throw new ESYAException("getIsoCardDataAKIS yapılırken hata oluştu", e);
		}
	}

	private byte[] _parolaDuzelt(byte[] aParola)
	{
		byte[] puk = new byte[aParola.length];
		System.arraycopy(aParola, 0, puk, 0, aParola.length);
		for (int i = 0; i < puk.length; i++)
		{
			puk[i] -= '0';
		}
		return puk;
	}

}
