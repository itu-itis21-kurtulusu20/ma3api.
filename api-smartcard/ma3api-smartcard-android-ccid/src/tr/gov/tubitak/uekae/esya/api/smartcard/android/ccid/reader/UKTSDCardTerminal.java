package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

/**
 * Created by omer.dural on 06.06.2017.
 */

public class UKTSDCardTerminal extends CardTerminal {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Card connect(String s) throws CardException {
        return null;
    }

    @Override
    public boolean isCardPresent() throws CardException {
        return false;
    }

    @Override
    public boolean waitForCardPresent(long l) throws CardException {
        return false;
    }

    @Override
    public boolean waitForCardAbsent(long l) throws CardException {
        return false;
    }
}
