package tr.gov.tubitak.uekae.esya.api.smartcard.pin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tubitak.akis.cif.commands.AbstractAkisCommands;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.io.UnsupportedEncodingException;

/**
 * Created by selami.ozbey on 11.02.2016.
 */
public class AKIS2AkisCIFGMUserParolaDegistirici implements KartParolaDegistirici {

    protected static Logger logger = LoggerFactory.getLogger(AKIS2AkisCIFGMUserParolaDegistirici.class);
    private AbstractAkisCommands akisCommands;
    private String pkcs15Dizin = "PKCS-15";

    @Override
    public void init(IParolaParametre aParametre) {
        try {
            akisCommands = ((SecureMessagingPP) aParametre).getCommander();
        } catch (Exception e) {
            throw new ESYARuntimeException("Akis için abstract akis command alınamadı.", e);
        }
    }

    public void pinDegistir(byte[] aYeniPin) throws ESYAException {
        _init();
        _changePIN(aYeniPin);
    }

    public void pukDegistir(byte[] aYeniPuk) throws ESYAException {
        throw new ESYAException("function not supported");
    }

    protected void _selectDizin() throws ESYAException {
        try {
            akisCommands.selectMF();
            akisCommands.selectDFByName(pkcs15Dizin.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            throw new ESYAException(pkcs15Dizin + " secilirken hata olustu", e);
        } catch (Exception e) {
            throw new ESYAException(pkcs15Dizin + " secilirken hata olustu", e);
        }
    }

    protected void _init() throws ESYAException {
        _selectDizin();
    }

    private void _changePIN(byte[] aYeniPin) throws ESYAException {
        try {
            akisCommands.unlockDFPIN((byte) -1, null, (byte) 0x1, aYeniPin);
        } catch (Exception e) {
            throw new ESYAException("PIN degistirilirken hata olustu", e);
        }
    }


    @Override
    public void terminate(boolean reset) {
        try {
            akisCommands.closeEF();
        } catch (Exception e) {
            // kart baglantisi koparilamazsa isleme devam edebiliriz sorun yok
            logger.warn("Warning in AKIS2AkisCIFGMUserParolaDegistirici", e);
        }
    }

}
