package dev.esya.api.dist.signatureApi.upgrades;

import org.junit.Assert;
import org.junit.Test;
import dev.esya.api.dist.signatureApi.SampleBase;
import tr.gov.tubitak.uekae.esya.api.signature.*;

public class Upgrade extends SampleBase {

    public void upgrade(String fileNameRead, String fileNameWrite, SignatureType convertToType) throws Exception {

        SignatureContainer sc = readSignatureContainer(fileNameRead);
        Signature signature = sc.getSignatures().get(0);
        signature.upgrade(convertToType);
        dosyaYaz(sc, fileNameWrite);

        SignatureContainer read = readSignatureContainer(fileNameWrite);
        Signature readSignature = read.getSignatures().get(0);
        Assert.assertEquals(convertToType, readSignature.getSignatureType());
        ContainerValidationResult cvr = read.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void upgradeBesToT() throws Exception {
        upgrade("bes_detached", "upgrade_BES_T", SignatureType.ES_T);
    }

    @Test
    public void upgradeBesToC() throws Exception {
        upgrade("bes_detached", "upgrade_BES_C", SignatureType.ES_C);
    }

    @Test
    public void upgradeBesToX1() throws Exception {
        upgrade("bes_detached", "upgrade_BES_X1", SignatureType.ES_X_Type1);
    }

    @Test
    public void upgradeBesToXL() throws Exception {
        upgrade("bes_detached", "upgrade_BES_xL", SignatureType.ES_XL_Type1);
    }

    @Test
    public void upgradeTToC() throws Exception {
        upgrade("upgrade_BES_T", "upgrade_T_C", SignatureType.ES_C);
    }

    @Test
    public void upgradeTToX1() throws Exception {
        upgrade("upgrade_BES_T", "upgrade_T_X1", SignatureType.ES_X_Type1);
    }

    @Test
    public void upgradeTToXL() throws Exception {
        upgrade("upgrade_BES_T", "upgrade_T_XL", SignatureType.ES_XL_Type1);
    }

    @Test
    public void upgradeCToXL() throws Exception {
        upgrade("upgrade_T_C", "upgrade_C_XL", SignatureType.ES_XL_Type1);
    }

}
