package tr.gov.tubitak.uekae.esya.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ConstantsExtended;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetPKCS11Info {

    static final String PASSWORD = "123456";

    static SmartCard sc = null;
    static long sid = 0;
    static long slotNo = 0;


    @Before
    public void setUpClass() throws Exception
    {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = getSlot();
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp()  throws Exception
    {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    private static long getSlot()
            throws PKCS11Exception
    {
        long[] slots = sc.getTokenPresentSlotList();
        return slots[0];
    }

    @Test
    public void listSupportedMechanisms() throws Exception
    {
        Field[] fieldsPKCS11Constants = PKCS11Constants.class.getFields();
        Field[] fieldsPKCS11ConstantsExtended = PKCS11ConstantsExtended.class.getFields();

        List<Field> fieldsPKCS11 =  new ArrayList(Arrays.asList(fieldsPKCS11Constants));
        fieldsPKCS11.addAll(Arrays.asList(fieldsPKCS11ConstantsExtended));

        long[] mechanismList = sc.getMechanismList(slotNo);

        for(int i =0; i< mechanismList.length; i++){
            long mechanism = mechanismList[i];
            for(int j=0; j < fieldsPKCS11.size(); j++){
                Field field = fieldsPKCS11.get(j);
                if(field.getName().startsWith("CKM")){
                    if(field.getLong(null) == mechanism)
                        System.out.println(field.getName());
                }

            }
        }
    }
}
