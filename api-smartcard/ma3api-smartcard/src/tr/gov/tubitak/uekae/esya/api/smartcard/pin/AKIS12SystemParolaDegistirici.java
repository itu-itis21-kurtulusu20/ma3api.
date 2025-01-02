package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;

public class AKIS12SystemParolaDegistirici extends AKIS12UserParolaDegistirici {

	@Override
	public void pinDegistir(byte[] aYeniPin) throws ESYAException {
		try
		{
			_init();
			mResponse = mChannel.transmit(new CommandAPDU(00, 0x24, 0x01, 0x01, aYeniPin));
	        _checkResult(mResponse);
		} catch (CardException e)
		{
			throw new ESYAException("PIN degistirilirken hata olustu", e);
		}
	}

	@Override
	public void pukDegistir(byte[] aYeniPuk) throws ESYAException {
		try
		{
			_init();
			mResponse = mChannel.transmit(new CommandAPDU(00, 0x24, 0x01, 0x02, aYeniPuk));
	        _checkResult(mResponse);
		} catch (CardException e)
		{
			throw new ESYAException("PUK degistirilirken hata olustu", e);
		}
	}
}
