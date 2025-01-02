package tr.gov.tubitak.uekae.esya.api.smartcard.apdu;

import java.util.List;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminals.State;

import tubitak.akis.cif.functions.ICommandTransmitter;

public abstract class TerminalHandler 
{
	public abstract List<CardTerminal> listCardTerminals(State aState) throws CardException; 
	
	public abstract ICommandTransmitter getTransmitter(CardTerminal cardTerminal)  throws CardException;
}
