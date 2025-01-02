package tr.gov.tubitak.uekae.esya.api.smartcard.apdu;

import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals.State;
import javax.smartcardio.TerminalFactory;

import tubitak.akis.cif.akisExceptions.AkisCardException;
import tubitak.akis.cif.functions.CommandTransmitterPCSC;
import tubitak.akis.cif.functions.ICommandTransmitter;

public class PCTerminalHandler extends TerminalHandler
{
	@Override
	public List<CardTerminal> listCardTerminals(State aState) throws CardException
	{
		return TerminalFactory.getDefault().terminals().list(aState);
	}

	@Override
	public ICommandTransmitter getTransmitter(CardTerminal aCardTerminal) throws CardException
	{
		try {
			return new CommandTransmitterPCSC(aCardTerminal, false);
		} catch (AkisCardException e) {
			throw new CardException(e );
		}
	}
}
