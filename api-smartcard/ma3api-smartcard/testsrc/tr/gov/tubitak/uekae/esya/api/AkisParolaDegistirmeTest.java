package tr.gov.tubitak.uekae.esya.api;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.smartcard.pin.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import java.util.Random;

/**
 * Created by ramazan.girgin on 7/22/2014.
 */
public class AkisParolaDegistirmeTest extends TestCase {

    public void testChangePUKAkis12pinpukWithAkisCIF() throws Exception {
        String puk = "12345";
        String newPuk = "123456";

        long slotNumber = SmartOp.findSlotNumber(CardType.AKIS);
        SmartCard smartCard = new SmartCard(CardType.AKIS);
        char[] slotDescriptionChar = smartCard
                .getSlotInfo(slotNumber).slotDescription;
        String mTerminalAdi = new String(slotDescriptionChar).trim();
        CardTerminal initializationTerminal = TerminalFactory.getDefault()
                .terminals().getTerminal(mTerminalAdi);


        Card card = initializationTerminal.connect("*");

        String KART_DIZIN = "3D00";
        APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));
        KartParolaDegistirici parolaDegistirici = new AKIS12AkisCIFUserParolaDegistirici();
        parolaDegistirici.init(parametre);


        String newPin = "1234567";
        System.out.println("Pin= " + newPin + " olarak degistirilecek.");
        parolaDegistirici.pinDegistir(newPin.getBytes());
        System.out.println("Pin= " + newPin + " olarak degistirildi.");

        System.out.println("Puk = " + newPuk + " olarak degistirilecek.");
        parolaDegistirici.pukDegistir(newPuk.getBytes());
        System.out.println("Puk = " + newPuk + " olarak degistirildi.");
    }

    private static String PUK_BEGIN="1234567812";
    public void testChangeAkis20Pin() throws Exception {

        String puk = "12345678127634";
        String newPuk = null;
        while(true){

            long slotNumber = SmartOp.findSlotNumber(CardType.AKIS);
            SmartCard smartCard = new SmartCard(CardType.AKIS);
            char[] slotDescriptionChar = smartCard
                    .getSlotInfo(slotNumber).slotDescription;
            String mTerminalAdi = new String(slotDescriptionChar).trim();
            CardTerminal initializationTerminal = TerminalFactory.getDefault()
                    .terminals().getTerminal(mTerminalAdi);


            Card card = initializationTerminal.connect("*");

            String KART_DIZIN = "3D00";


            Random rnd = new Random();
            String newPin = String.valueOf(rnd.nextInt(900000)+100000);
            newPuk=PUK_BEGIN+newPin;
            {
                APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));
                KartParolaDegistirici parolaDegistirici = ParolaFabrika
                        .parolaciAl(ParolaDegistirmeYontemi.APDU20USR);
                parolaDegistirici.init(parametre);
                System.out.println("Puk = "+newPuk+" olarak degistirilecek.");
                parolaDegistirici.pukDegistir(newPuk.getBytes());
                System.out.println("Puk = "+newPuk+" olarak degistirildi.");
            }
            {
                APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, newPuk.getBytes("ASCII"));
                KartParolaDegistirici parolaDegistirici = ParolaFabrika
                        .parolaciAl(ParolaDegistirmeYontemi.APDU20USR);
                parolaDegistirici.init(parametre);
                System.out.println("Pin = "+newPin+" olarak degistirilecek.");
                parolaDegistirici.pinDegistir(newPin.getBytes());
                System.out.println("Pin = "+newPin+" olarak degistirildi.");
            }
            puk=newPuk;
            card.disconnect(true);
        }
    }
}
