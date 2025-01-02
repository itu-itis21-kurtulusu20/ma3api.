package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;


public class APDUPP implements IParolaParametre {
    private CardTerminal mTerminal;
    private String mDizinAdi;
	private byte[] mSPIN;
	private Card mCard;


    public CardTerminal getTerminal() {
        return mTerminal;
    }

    public APDUPP(CardTerminal terminal,Card aCard, String aDizinAdi, byte[] aPuk) {
        this.mTerminal = terminal;
        mCard = aCard;
        mSPIN = aPuk;
        mDizinAdi = aDizinAdi;

    }

	public APDUPP(Card aCard, String aDizinAdi, byte[] aPuk) {
		mCard = aCard;
		mSPIN = aPuk;
		mDizinAdi = aDizinAdi;
	}

	public String getMDizinAdi() {
		return mDizinAdi;
	}

	public byte[] getMSPIN() {
		return mSPIN;
	}

	public Card getMCard() {
		return mCard;
	}
}
