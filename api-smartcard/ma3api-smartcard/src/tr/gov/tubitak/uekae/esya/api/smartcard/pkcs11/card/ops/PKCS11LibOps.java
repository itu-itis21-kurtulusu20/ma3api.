package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import com.sun.jna.*;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ExceptionFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.util.OpsUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_ATTRIBUTE_STRUCTURE;

import java.util.ArrayList;
import java.util.List;

public class PKCS11LibOps {

    private static CardType cardType;

    public PKCS11LibOps(CardType cardType) {
        PKCS11LibOps.cardType = cardType;
    }

    /*
    * Herhangi bir pkcs11 objesi için C_GetAttributeValue'da, objenin sahip olmadığı bir attribute sorgularsak Java pkcs11 arayüzü exception fırlatıyor.
    * Fakat bizim istediğimiz objenin sahip olmadığı bir özelliği sorguladığımızda exception'ı esgeçmek. Çünkü HSM return olarak CKR_ATTRIBUTE_TYPE_INVALID dönse bile verilen attribute array'ini dolduruyor.
    * Bu sayede;
    * Başlangıçta herhangi bir pkcs11 objesinin hangi attribute'lere sahip olduğunu bilmediğimiz halde, ilk başta bütün pkcs11 attribute'lerini herhangi bir obje için sorgulayıp
    * attribute'lerin len değişkeni 0'dan büyük olanları filtreleyip ardından bir daha çağrı daha yapıyoruz ve objenin attribute'lerini elde etmiş oluyoruz.
    */
    public CK_ATTRIBUTE[] getAttributeValueInTwoSteps(long sessionID, long objectID, long [] types) throws PKCS11Exception {
        CK_ATTRIBUTE_STRUCTURE[] attributeStructures = CK_ATTRIBUTE_STRUCTURE.newArrayFilled(types);

        long result1 = PKCS11LibOps.PKCS11LibJNAConnector.pkcs11Lib.C_GetAttributeValue(sessionID, objectID, attributeStructures, types.length);

        if(!(result1 == PKCS11Constants.CKR_OK || result1 == PKCS11Constants.CKR_ATTRIBUTE_TYPE_INVALID)){
            throw PKCS11ExceptionFactory.getPKCS11Exception(result1);
        }

        int count = 0;
        for (CK_ATTRIBUTE_STRUCTURE attributeStructure : attributeStructures) {
            long length = attributeStructure.ulValueLen.longValue();
            if (length > 0) {
                count++;
            }
        }

        CK_ATTRIBUTE_STRUCTURE[] filledAttributeStructures = CK_ATTRIBUTE_STRUCTURE.newArrayEmpty(count);
        int index = 0;
        for (CK_ATTRIBUTE_STRUCTURE attributeStructure : attributeStructures) {
            long length = attributeStructure.ulValueLen.longValue();
            if (length > 0) {
                filledAttributeStructures[index].type = attributeStructure.type;
                filledAttributeStructures[index].ulValueLen = attributeStructure.ulValueLen;
                filledAttributeStructures[index].pValue = new Memory(length);
                index++;
            }
        }

        long result2 = PKCS11LibOps.PKCS11LibJNAConnector.pkcs11Lib.C_GetAttributeValue(sessionID, objectID, filledAttributeStructures, filledAttributeStructures.length);

        if(!(result2 == PKCS11Constants.CKR_OK)){
            throw PKCS11ExceptionFactory.getPKCS11Exception(result2);
        }

        List<CK_ATTRIBUTE> filledCkAttributes = new ArrayList<>();
        for (int i = 0; i < filledAttributeStructures.length; i++) {
            CK_ATTRIBUTE ck_attribute = new CK_ATTRIBUTE();
            ck_attribute.type = filledAttributeStructures[i].type.longValue();
            ck_attribute.pValue = filledAttributeStructures[i].pValue.getByteArray(0, filledAttributeStructures[i].ulValueLen.intValue());
            filledCkAttributes.add(ck_attribute);
        }

        return filledCkAttributes.toArray(new CK_ATTRIBUTE[0]);
    }

    public interface PKCS11LibJNAConnector extends Library {

        PKCS11LibOps.PKCS11LibJNAConnector pkcs11Lib = (PKCS11LibOps.PKCS11LibJNAConnector) Native.loadLibrary(OpsUtil.getLibPath(cardType.getLibName()), PKCS11LibOps.PKCS11LibJNAConnector.class);

        long C_GetAttributeValue(long sessionID, long objectID, CK_ATTRIBUTE_STRUCTURE[] ckAttributeStructures, long count);
    }
}
