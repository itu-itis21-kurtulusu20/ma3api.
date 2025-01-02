package tr.gov.tubitak.uekae.esya.api.smartcard.pin;


/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Oct 19, 2010
 * Time: 2:40:15 PM
 * To change this template use File | Settings | File Templates.
 */

public enum ParolaDegistirmeYontemi {
    ;
    public static final int PKCS11 = 0;
    public static final int APDU10 = 1;
    public static final int APDU12SYS = 2;
    public static final int APDU12USR = 3;
    public static final int APDU20USR = 4;
    public static final int APDU12USRWITHAKISCIF = 5;
    public static final int GMAKIS20WITHAKISCIF = 6;

}
