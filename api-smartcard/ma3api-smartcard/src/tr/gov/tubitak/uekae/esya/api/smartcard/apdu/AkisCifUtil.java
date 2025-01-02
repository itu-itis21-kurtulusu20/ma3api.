package tr.gov.tubitak.uekae.esya.api.smartcard.apdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tubitak.akis.cif.commands.AbstractAkisCommands;
import tubitak.akis.cif.commands.CIFFactory;
import tubitak.akis.cif.functions.CommandTransmitterPCSC;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 * Created by sura.emanet on 26.06.2019.
 */
public class AkisCifUtil {

    private static final Logger logger = LoggerFactory.getLogger(AkisCifUtil.class);

    public static AbstractAkisCommands getAkisCommandsFromUSBName(String deviceUSBName) throws ESYAException {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminal terminal = factory.terminals().getTerminal(deviceUSBName);
            CommandTransmitterPCSC transmitter = new CommandTransmitterPCSC(terminal, false);
            logger.debug("getAkisCommandsFromUSBName ATR: " + StringUtil.toHexString(transmitter.atr().getBytes()));
            AbstractAkisCommands commands = CIFFactory.getAkisCIFInstance(transmitter);

            return commands;
        }
        catch (Exception ex){
            throw new ESYAException(ex);
        }
    }

    public static AbstractAkisCommands getFirstAkisCommands() throws ESYAException
    {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            CardTerminal terminal = factory.terminals().list().get(0);
            CommandTransmitterPCSC transmitter = new CommandTransmitterPCSC(terminal, false);
            AbstractAkisCommands commands = CIFFactory.getAkisCIFInstance(transmitter);

            return commands;
        }
        catch (Exception ex){
            throw new ESYAException(ex);
        }
    }
}
