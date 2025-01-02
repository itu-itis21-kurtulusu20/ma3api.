package tr.gov.tubitak.uekae.esya.api.cades.example.validation;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.cades.example.CadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;

import static junit.framework.TestCase.assertEquals;

public class GetSignatureType extends CadesSampleBase {

    /**
     * Getting signature type of a ESA type signature
     *
     * @throws Exception
     */
    @Test
    public void testIsItESA() throws Exception {

        byte[] content = FileUtil.readBytes(getTestDataFolder() + "ESA-Converted-1.p7s");
        BaseSignedData baseSignedData = new BaseSignedData(content);

        ESignatureType type = baseSignedData.getSignerList().get(0).getType();

        assertEquals(type, ESignatureType.TYPE_ESA);
    }
}
