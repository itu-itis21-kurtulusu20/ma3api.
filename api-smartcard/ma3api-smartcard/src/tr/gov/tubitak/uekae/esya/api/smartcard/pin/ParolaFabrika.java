package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;


public class ParolaFabrika {
	
	public static KartParolaDegistirici parolaciAl(int aYontem){
		
		if (aYontem == ParolaDegistirmeYontemi.PKCS11){
			return new PKCS11ParolaDegistirici();
		}
		else if (aYontem == ParolaDegistirmeYontemi.APDU10){
			return new AKIS10ParolDegistirici();
		}
		else if (aYontem == ParolaDegistirmeYontemi.APDU12SYS){
			return new AKIS12SystemParolaDegistirici();
		}
		else if (aYontem == ParolaDegistirmeYontemi.APDU12USR){
			return new AKIS12UserParolaDegistirici();
		}
        else if (aYontem == ParolaDegistirmeYontemi.APDU20USR){
            return new AKIS20UserParolaDegistirici();
        }
        else if (aYontem == ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF){
            return new AKIS12AkisCIFUserParolaDegistirici();
        }
		else if (aYontem == ParolaDegistirmeYontemi.GMAKIS20WITHAKISCIF){
			return new AKIS2AkisCIFGMUserParolaDegistirici();
		}
		
		throw new ESYARuntimeException(aYontem + " nolu parola degistirme yontemi bulunamadi");
	}  

}
