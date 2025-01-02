package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;

import javax.smartcardio.ATR;

import com.scdroid.smartcard.Card;
import com.scdroid.smartcard.SCException;


public class AkisCard extends Card {

	ATR atr;
	@Override
	public boolean IdentifyCard(byte[] cardAtrByte) {
		atr = new ATR(cardAtrByte);
		return false;
	}
	public ATR getAtr() {
		return atr;
	}
	@Override
	public boolean isPresent() throws SCException {
		// TODO Auto-generated method stub
		boolean isPresent = super.isPresent();
		return isPresent;
	}
	
}
