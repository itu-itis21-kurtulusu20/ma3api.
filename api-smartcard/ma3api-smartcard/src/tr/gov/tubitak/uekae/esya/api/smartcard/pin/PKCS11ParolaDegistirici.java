package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * SUN PKCS11 ile pin/puk değiştirir.
 * @author MYK
 *
 */

public class PKCS11ParolaDegistirici implements KartParolaDegistirici
{
	private IParolaParametre mParametre;
	
	public PKCS11ParolaDegistirici()
	{
		super();
	}
	
	public void pinDegistir(byte[] aYeniPin) throws ESYAException
	{
		if (mParametre instanceof KartIslemPP){
			KartIslemPP pp = (KartIslemPP) mParametre;			
			try {
				pp.getMKartIslem().changePassword(pp.getMVarsayilanSifre(), new String(aYeniPin));
			} catch (Exception e) {
				throw new ESYAException(e);
			}
		}
		else if (mParametre instanceof AkilliKartPP){
			AkilliKartPP pp = (AkilliKartPP) mParametre;
			try {
				pp.getMAkilliKart().login(pp.getMSessionID(),pp.getMVarsayilanSifre());
			} catch (PKCS11Exception e) {
				// Karta zaten login olunmus. O yuzden devam edelim.
				if (e.getErrorCode() != PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN) {
					throw new ESYAException(e);
				}
			}
			try {
				pp.getMAkilliKart().changePassword(pp.getMVarsayilanSifre(), new String(aYeniPin),pp.getMSessionID());
				pp.getMAkilliKart().logout(pp.getMSessionID());
			} catch (PKCS11Exception aEx) {
				throw new ESYAException(aEx);
			}
		}
		else{
			throw new ESYAException("Desteklenmeyen parola parametre" + mParametre.getClass().getName());
		}
	}

	public void pukDegistir(byte[] aYeniPuk) throws ESYAException
	{
		if (mParametre instanceof KartIslemPP){
			KartIslemPP pp = (KartIslemPP) mParametre;	
			try {
				pp.getMKartIslem().changePuk(pp.getMVarsayilanPuk().getBytes(), aYeniPuk);
			} catch (Exception e) {
				throw new ESYAException(e);
			}
		}	
		else if (mParametre instanceof AkilliKartPP){
			AkilliKartPP pp = (AkilliKartPP) mParametre;
			try
			{
				pp.getMAkilliKart().setSOPin(pp.getMVarsayilanPuk().getBytes(), aYeniPuk, pp.getMSessionID());
			}
			catch (PKCS11Exception aEx)
			{
				throw new ESYAException(aEx);
			}
		}
		else{
			throw new ESYAException("Desteklenmeyen parola parametre" + mParametre.getClass().getName());
		}
	}

	public void init(IParolaParametre aParametre) {
		mParametre = aParametre;		
	}

	@Override
	public void terminate(boolean reset) {
		return; // card.disconnect elimizde yok o yuzden islem yapmiyoruz
	}
}
