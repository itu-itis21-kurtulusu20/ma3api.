package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tubitak.akis.cif.functions.CommandTransmitterPCSC;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import java.util.List;
import java.util.stream.Collectors;

public class PCSCUtil {

    public static CardChannel getCardChannelFromUSBName(String deviceUSBName) throws ESYAException {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminal terminal = factory.terminals().getTerminal(deviceUSBName);
            CommandTransmitterPCSC transmitter = new CommandTransmitterPCSC(terminal, false);

            CardChannel cardChannel = transmitter.card.getBasicChannel();

            return cardChannel;
        }
        catch (Exception ex){
            throw new ESYAException(ex);
        }
    }


    public static String [] getCardReaderNames() throws ESYAException {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> cardTerminals = factory.terminals().list();
            return cardTerminals.stream().map(p -> p.getName()).collect(Collectors.toList()).toArray(new String[0]);
        }
        catch (Exception ex){
            throw new ESYAException(ex);
        }
    }

}
