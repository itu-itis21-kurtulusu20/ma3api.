package tr.gov.tubitak.uekae.esya.api.smartcard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.cardobject.P11Object;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.AttributeUtil;

import java.io.IOException;
import java.text.MessageFormat;

public class ObjectLabelTest {

    private static SmartCard sc;
    private static long sid;

    @BeforeClass
    public static void setUp() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.UTIMACO);
        sid = sc.openSession(0L);
        sc.login(sid, "123456");
    }

    @AfterClass
    public static void cleanUp() throws PKCS11Exception {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    @Test
    public void objectSearchByStringTest() throws PKCS11Exception {
        P11Object[] objectInfos = sc.getAllObjectInfos(sid);

        CK_ATTRIBUTE[] template;
        {
            String searchLabel = "troy_rsa\0";

            template = new CK_ATTRIBUTE[]{
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, searchLabel)
            };
        }
        long[] objIDs = sc.findObjects(sid, template);
        System.out.println(MessageFormat.format("Number of objects: {0}", objIDs.length));
        System.out.println();

        for (long objID : objIDs) {
            template = new CK_ATTRIBUTE[]{
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL),
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS)
            };
            sc.getAttributeValue(sid, objID, template);

            String objLabel = AttributeUtil.getStringValue(template[0].pValue);
            String objClass = AttributeUtil.getStringValue(template[1].pValue);

            System.out.println(MessageFormat.format("- objID: {0}, label: {1}, class: {2}", objID, objLabel, objClass));
        }
    }

    @Test
    public void objectSearchBySubstringTest() throws PKCS11Exception {
        P11Object[] objectInfos = sc.getAllObjectInfos(sid);

        CK_ATTRIBUTE[] template;
        {
            String searchLabel = objectInfos[0].getLabel();
            String searchLabelSubstring = searchLabel.substring(0, searchLabel.length() - 1);

            template = new CK_ATTRIBUTE[]{
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, searchLabelSubstring)
            };
        }
        long[] objIDs = sc.findObjects(sid, template);
        System.out.println(MessageFormat.format("Number of objects: {0}", objIDs.length));
        System.out.println();

        for (long objID : objIDs) {
            template = new CK_ATTRIBUTE[]{
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL),
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS)
            };
            sc.getAttributeValue(sid, objID, template);

            String objLabel = AttributeUtil.getStringValue(template[0].pValue);
            String objClass = AttributeUtil.getStringValue(template[1].pValue);

            System.out.println(MessageFormat.format("- objID: {0}, label: {1}, class: {2}", objID, objLabel, objClass));
        }
    }

    @Test
    public void objectSearchByCharArrayTest() throws PKCS11Exception {
        P11Object[] objectInfos = sc.getAllObjectInfos(sid);

        CK_ATTRIBUTE[] template;
        {
            String searchLabel = "troy_rsa\0";
            char[] searchLabelCharArray = searchLabel.toCharArray();

            template = new CK_ATTRIBUTE[]{
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, searchLabelCharArray)
            };
        }
        long[] objIDs = sc.findObjects(sid, template);
        System.out.println(MessageFormat.format("Number of objects: {0}", objIDs.length));
        System.out.println();

        for (long objID : objIDs) {
            template = new CK_ATTRIBUTE[]{
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL),
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS)
            };
            sc.getAttributeValue(sid, objID, template);

            String objLabel = AttributeUtil.getStringValue(template[0].pValue);
            String objClass = AttributeUtil.getStringValue(template[1].pValue);

            System.out.println(MessageFormat.format("- objID: {0}, label: {1}, class: {2}", objID, objLabel, objClass));
        }
    }
}
