package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Kart pin/puk değiştirir.
 * @author IH
 *
 */

public interface KartParolaDegistirici
{
    public void pinDegistir(byte[] yeniPin) throws ESYAException;
	public void pukDegistir(byte[] yeniPuk) throws ESYAException;
	public void init(IParolaParametre parametre);
	public void terminate(boolean reset);
}
