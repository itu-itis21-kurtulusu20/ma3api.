package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public abstract class AKISParolaDegistirici implements KartParolaDegistirici {

	protected static Logger logger = LoggerFactory.getLogger(AKISParolaDegistirici.class);

	//protected Card mCard;
	protected CardChannel mChannel;
	protected Card mCard;
	protected ResponseAPDU mResponse;
	
	//protected String mDizinAdi;
	protected byte[] mSPIN;
	protected APDUPP mParametre;
	
	public AKISParolaDegistirici()
	{
		super();
	}
	
	public void init(IParolaParametre aParametre){
		if (!(aParametre instanceof APDUPP))
			throw new IllegalArgumentException("Gelen parametre "+ APDUPP.class.toString()+" tipinde degil.Gelen tip :"+aParametre.getClass().toString()); 
		mParametre = (APDUPP) aParametre;
		mChannel = mParametre.getMCard().getBasicChannel();
		mSPIN = mParametre.getMSPIN();		
	}
	
	// isletim--> yonetim veya yonetim--> yonetim
	protected void _phaseControl() throws ESYAException {
		try {
			mResponse = mChannel.transmit(new CommandAPDU(0x80, 0x09,0x00, 0x02));
			_checkResult(mResponse);
		} catch (CardException e) {
			throw new ESYAException("phaseControl yaparken hata oluştu", e);
		}
	}
	
	protected void _checkResult(ResponseAPDU aAPDU) throws ESYAException {
		if (aAPDU.getSW() != 0x9000) {
			throw new ESYAException("Sonuc dogrulanamadi:"+ Integer.toHexString(aAPDU.getSW()));
		}
	}

	@Override
	public void terminate(boolean reset) {
		try {
			mCard.disconnect(reset);
		} catch (Exception e) {
			// kart baglantisi koparilamazsa isleme devam edebiliriz sorun yok
			logger.warn("Warning in AKISParolaDegistirici", e);
		}

		try {
			mParametre.getMCard().disconnect(reset);
		} catch (Exception e) {
			// kart baglantisi koparilamazsa isleme devam edebiliriz sorun yok
			logger.warn("Warning in AKISParolaDegistirici", e);
		}

	}
}
