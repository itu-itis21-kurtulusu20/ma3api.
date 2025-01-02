package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import java.math.BigInteger;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

public class AKIS12UserParolaDegistirici extends AKISParolaDegistirici {

	public void pinDegistir(byte[] aYeniPin) throws ESYAException {
		_init();
		_selectDizin(mParametre.getMDizinAdi());
        _changePIN(aYeniPin);
	}

	public void pukDegistir(byte[] aYeniPuk) throws ESYAException {
		_init();
		_selectDizin(mParametre.getMDizinAdi());
        _changePUK(aYeniPuk);
	}

	protected void _verifySPIN() throws ESYAException
	{
		try
		{
			mResponse = mChannel.transmit(new CommandAPDU(0, 0x20, 0x00, 0x01,mSPIN));
			_checkResult(mResponse);
		} catch (CardException e)
		{
			throw new ESYAException("sistem verifyPUK yapılırken hata oluştu", e);
		}
	}

	protected void _selectDizin(String aDizin) throws ESYAException
	{
		try
		{
	        //mResponse = mChannel.transmit(new CommandAPDU(HexConverter.hexStringToByteArray("00A4010002"+aDizin)));
			mResponse = mChannel.transmit(new CommandAPDU(new BigInteger("00A4010002"+aDizin,16).toByteArray()));
	        _checkResult(mResponse);
		} catch (CardException e)
		{
			throw new ESYAException(aDizin +" secilirken hata olustu", e);
		}
	}

	protected void _init() throws ESYAException {
		_verifySPIN();

		_phaseControl();

		_selectDizin("3F00");
	}
	
	private void _changePIN(byte[] aYeniPin) throws ESYAException
	{
		try
		{
			  mResponse = mChannel.transmit(new CommandAPDU(00, 0x24, 0x01, 0x81, aYeniPin));
			  //Uygulama kitlenmis, reset / retry kullanılacak.
			  if(mResponse.getSW1() == 0x69 && mResponse.getSW2() == 0x84)
			  {
				  mResponse = mChannel.transmit(new CommandAPDU(00, 0x2C, 0x02, 0x81, aYeniPin));
			  }
	        _checkResult(mResponse);
		} catch (CardException e)
		{
			throw new ESYAException("PIN degistirilirken hata olustu", e);
		}
	}

	private void _changePUK(byte[] aYeniPuk) throws ESYAException
	{
		try
		{
			  mResponse = mChannel.transmit(new CommandAPDU(00, 0x24, 0x01, 0x82, aYeniPuk));
	        _checkResult(mResponse);
		} catch (CardException e)
		{
			throw new ESYAException("PUK degistirilirken hata olustu", e);
		}
	}
}
