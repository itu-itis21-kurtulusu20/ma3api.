package test.esya.api.asn.x509;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EDocumentTypeListExtension;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Created by ramazan.girgin on 26.12.2016.
 */
public class EDocumentTypeListExtensionTest {
    @Test
    public void canBeEmptyObject(){
        EDocumentTypeListExtension eDocumentTypeListExtension = new EDocumentTypeListExtension();
        Assert.assertNull(eDocumentTypeListExtension.getValueList());
    }

    @Test
    public void isEmptyAfterLoadedWithNullList() {
        EDocumentTypeListExtension eDocumentTypeListExtension = new EDocumentTypeListExtension();
        eDocumentTypeListExtension.loadFromDocValueList(null);
        Assert.assertNull(eDocumentTypeListExtension.getValueList());

        EDocumentTypeListExtension eDocumentTypeListExtension2 = new EDocumentTypeListExtension((String[]) null);
        Assert.assertNull(eDocumentTypeListExtension2.getValueList());
    }

    @Test
    public void canContainOneElement() throws ESYAException {
        EDocumentTypeListExtension eDocumentTypeListExtension = new EDocumentTypeListExtension();
        String documentTypeValueStr = "P";
        eDocumentTypeListExtension.loadFromDocValueList(new String[]{documentTypeValueStr});

        String[] valueList = eDocumentTypeListExtension.getValueList();
        Assert.assertNotNull(valueList);
        Assert.assertTrue(valueList.length == 1);

        String documentTypeValue = valueList[0];
        Assert.assertEquals(documentTypeValue,documentTypeValueStr);
    }

    @Test
    public void canContainMultipleElement() throws ESYAException {
        EDocumentTypeListExtension eDocumentTypeListExtension = new EDocumentTypeListExtension();
        String documentTypeValueStr = "P";
        String documentTypeValue2Str = "ID";
        eDocumentTypeListExtension.loadFromDocValueList(new String[]{documentTypeValueStr, documentTypeValue2Str});

        String[] valueList = eDocumentTypeListExtension.getValueList();
        Assert.assertNotNull(valueList);
        Assert.assertTrue(valueList.length == 2);

        String documentTypeValue = valueList[0];
        Assert.assertEquals(documentTypeValue,documentTypeValueStr);

        String documentTypeValue2 = valueList[1];
        Assert.assertEquals(documentTypeValue2,documentTypeValue2Str);
    }


    @Test
    public void canEncodableAndDecodable() throws ESYAException {
        EDocumentTypeListExtension eDocumentTypeListExtension = new EDocumentTypeListExtension();
        String documentTypeValueStr = "P";
        String documentTypeValue2Str = "ID";
        eDocumentTypeListExtension.loadFromDocValueList(new String[]{documentTypeValueStr, documentTypeValue2Str});
        byte[] encoded = eDocumentTypeListExtension.getEncoded();

        EDocumentTypeListExtension decodedObj = new EDocumentTypeListExtension(encoded);

        String[] valueList = decodedObj.getValueList();
        Assert.assertNotNull(valueList);
        Assert.assertTrue(valueList.length == 2);

        String documentTypeValue = valueList[0];
        Assert.assertEquals(documentTypeValue,documentTypeValueStr);
    }

    @Test
    public void canBeLoadedWithSplittedString() throws ESYAException {
        String documentTypeValueStr = "P";
        String documentTypeValue2Str = "ID";
        String multiValueStr = documentTypeValueStr+EDocumentTypeListExtension.MULTI_VALUE_SPLITTER+documentTypeValue2Str;
        EDocumentTypeListExtension eDocumentTypeListExtension = new EDocumentTypeListExtension(multiValueStr);

        byte[] encoded = eDocumentTypeListExtension.getEncoded();

        EDocumentTypeListExtension decodedObj = new EDocumentTypeListExtension(encoded);

        String[] valueList = decodedObj.getValueList();
        Assert.assertNotNull(valueList);
        Assert.assertTrue(valueList.length == 2);

        String documentTypeValue = valueList[0];
        Assert.assertEquals(documentTypeValue,documentTypeValueStr);
    }
}
