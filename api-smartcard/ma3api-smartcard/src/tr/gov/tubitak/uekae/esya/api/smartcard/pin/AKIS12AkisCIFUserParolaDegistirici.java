package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tubitak.akis.cif.commands.AbstractAkisCommands;
import tubitak.akis.cif.commands.CIFFactory;
import tubitak.akis.cif.dataStructures.FCI;
import tubitak.akis.cif.functions.CommandTransmitterPCSC;
import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import javax.smartcardio.CardTerminal;
import java.io.UnsupportedEncodingException;

public class AKIS12AkisCIFUserParolaDegistirici extends AKISParolaDegistirici {
    AbstractAkisCommands akisCommands;
    ICommandTransmitter commandTransmitter;
	FCI pkcs15FCI;
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

	protected void _selectDizin(String aDizin) throws ESYAException
	{
		try
		{
            akisCommands.selectMF();
			pkcs15FCI = akisCommands.selectDFByName(aDizin.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new ESYAException(aDizin +" secilirken hata olustu", e);

		} catch (Exception e)
		{
			throw new ESYAException(aDizin +" secilirken hata olustu", e);
		}
    }

	protected void _init() throws ESYAException {
        _selectDizin("PKCS-15");
	}
	
	private void _changePIN(byte[] aYeniPin) throws ESYAException
	{
		try
		{
			akisCommands.unlockDFPIN(pkcs15FCI.getFID(),mSPIN,aYeniPin);
		} catch (Exception e)
		{
			throw new ESYAException("PIN degistirilirken hata olustu", e);
		}
	}

	private void _changePUK(byte[] aYeniPuk) throws ESYAException
	{
		try
		{
			akisCommands.changeDFPUK(pkcs15FCI.getFID(),mSPIN,aYeniPuk);

		} catch (Exception e)
		{
			throw new ESYAException("PIN degistirilirken hata olustu", e);
		}
	}
}
