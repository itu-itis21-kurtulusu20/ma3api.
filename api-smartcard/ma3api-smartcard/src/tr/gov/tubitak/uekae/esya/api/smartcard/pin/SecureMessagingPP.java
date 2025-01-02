package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tubitak.akis.cif.commands.AbstractAkisCommands;

/**
 * Created by selami.ozbey on 11.02.2016.
 */

public class SecureMessagingPP implements IParolaParametre {
	private AbstractAkisCommands commander;

    public SecureMessagingPP(AbstractAkisCommands commander) {
        this.commander = commander;
	}

	public AbstractAkisCommands getCommander() {
		return commander;
	}
}
