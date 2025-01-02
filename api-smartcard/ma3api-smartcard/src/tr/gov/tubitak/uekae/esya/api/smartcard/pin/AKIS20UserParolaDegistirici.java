package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tubitak.akis.cif.commands.AbstractAkisCommands;
import tubitak.akis.cif.commands.CIFFactory;
import tubitak.akis.cif.functions.CommandTransmitterPCSC;
import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import javax.smartcardio.CardTerminal;
import java.io.UnsupportedEncodingException;

public class AKIS20UserParolaDegistirici extends AKISParolaDegistirici {
    AbstractAkisCommands akisCommands;
    ICommandTransmitter commandTransmitter;
    @Override
    public void init(IParolaParametre aParametre){
        super.init(aParametre);
        CardTerminal terminal = mParametre.getTerminal();
        try {
            commandTransmitter = new CommandTransmitterPCSC(terminal, false);
            akisCommands= CIFFactory.getAkisCIFInstance(commandTransmitter);
        } catch (Exception e) {
            throw new RuntimeException("Akis için transmitter oluşturulurken hata oluştu.",e);
        }
    }

    public void pinDegistir(byte[] aYeniPin) throws ESYAException {
		_init();
        _changePIN(aYeniPin);
	}

	public void pukDegistir(byte[] aYeniPuk) throws ESYAException {
		_init();
        _changePUK(aYeniPuk);
	}

	protected void _verifySPIN() throws ESYAException
	{
		try
		{
            akisCommands.verify((byte)2,mSPIN,false);
			//mResponse = mChannel.transmit(new CommandAPDU(0, 0x20, 0x00, 0x01,mSPIN));
            //_checkResult(mResponse);
        } catch (Exception e)
		{
			throw new ESYAException("sistem verifyPUK yapılırken hata oluştu", e);
		}
	}

	protected void _selectDizin(String aDizin) throws ESYAException
	{
		try
		{
            akisCommands.selectMF();
            akisCommands.selectDFByName(aDizin.getBytes("ASCII"));
            //mResponse = mChannel.transmit(new CommandAPDU(HexConverter.hexStringToByteArray("00A4010002"+aDizin)));
			//mResponse = mChannel.transmit(new CommandAPDU(new BigInteger("00A4010002"+aDizin,16).toByteArray()));
	        // _checkResult(mResponse);
		} catch (UnsupportedEncodingException e) {
			throw new ESYAException(aDizin +" secilirken hata olustu", e);
		}
		catch (Exception e) {
			throw new ESYAException(aDizin +" secilirken hata olustu", e);
		}
    }

	protected void _init() throws ESYAException {
        _selectDizin("PKCS-15");
		_verifySPIN();
		//_phaseControl();

	}
	
	private void _changePIN(byte[] aYeniPin) throws ESYAException
	{
		try
		{
            //akisCommands.unlockDFPIN((byte) 0, null, (byte) 2, puk);
            //akisCommands.verify((byte) 2, mSPIN, false);
            akisCommands.unlockDFPIN((byte) 2, mSPIN, (byte) 1, aYeniPin);
            /*
            mResponse = mChannel.transmit(new CommandAPDU(00, 0x24, 0x01, 0x81, aYeniPin));
			  //Uygulama kitlenmis, reset / retry kullanılacak.
			  if(mResponse.getSW1() == 0x69 && mResponse.getSW2() == 0x84)
			  {
				  mResponse = mChannel.transmit(new CommandAPDU(00, 0x2C, 0x02, 0x81, aYeniPin));
			  }
	        _checkResult(mResponse);*/
		} catch (Exception e)
		{
			throw new ESYAException("PIN degistirilirken hata olustu", e);
		}
	}

	private void _changePUK(byte[] aYeniPuk) throws ESYAException
	{
		try
		{
            //akisCommands.unlockDFPIN((byte) 0, null, (byte) 2, puk);
            //akisCommands.verify((byte) 2, mSPIN, false);
            akisCommands.changeDFPIN((byte) 2, mSPIN,  aYeniPuk);
            //akisCommands.unlockDFPIN((byte) 2, mSPIN, (byte) 2, aYeniPuk);
			 //mResponse = mChannel.transmit(new CommandAPDU(00, 0x24, 0x01, 0x82, aYeniPuk));
	       // _checkResult(mResponse);
		} catch (Exception e)
		{
			throw new ESYAException("PUK degistirilirken hata olustu", e);
		}
	}
}
