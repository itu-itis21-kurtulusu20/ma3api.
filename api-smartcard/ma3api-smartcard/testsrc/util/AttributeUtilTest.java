package util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.AttributeUtil;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AttributeUtilTest {

    private Object value;
    private Object expectedResult;

    public AttributeUtilTest(Object value, Object expectedResult) {
        this.value = value;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] {
                {"QCA1_1", "QCA1_1"},
                {new char[]{'Q','C','A','1','_','1'}, "QCA1_1"},
                {new byte[]{0x51,0x43,0x41,0x31,0x5f,0x31}, "QCA1_1"},
                {null, null}
        });
    }

    @Test
    public void getStringValue_ByStringParam(){
        Assert.assertEquals(expectedResult, AttributeUtil.getStringValue(value));
    }
}
