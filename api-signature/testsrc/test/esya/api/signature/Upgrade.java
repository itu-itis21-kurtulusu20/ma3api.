package test.esya.api.signature;

import junit.framework.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.*;

/**
 * @author ayetgin
 */
public class Upgrade extends BaseTest
{

    public void upgrade(String fileNameRead, String fileNameWrite,
                        SignatureType convertToType)
            throws Exception
    {
        SignatureContainer sc = readSignatureContainer(fileNameRead);
        Signature signature = sc.getSignatures().get(0);
        signature.upgrade(convertToType);
        write(sc, fileNameWrite);

        SignatureContainer read = readSignatureContainer(fileNameWrite);
        Signature readSignature = read.getSignatures().get(0);
        Assert.assertEquals(convertToType, readSignature.getSignatureType());
        ContainerValidationResult cvr = read.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }


    @Test
    public void upgrade() throws Exception {
        upgrade(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_T, SignatureType.ES_T);
    }

    @Test
    public void upgradeBesToC() throws Exception {
        upgrade(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_C, SignatureType.ES_C);
    }

    @Test
    public void upgradeBesToX1() throws Exception {
        upgrade(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_X1, SignatureType.ES_X_Type1);
    }

    @Test
    public void upgradeBesToXL() throws Exception {
        upgrade(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_XL, SignatureType.ES_XL_Type1);
    }

    @Test
    public void upgradeTToC() throws Exception {
        upgrade(FileNames.UPGRADED_BES_T, FileNames.UPGRADED_T_C, SignatureType.ES_C);
    }

    @Test
    public void upgradeTToX1() throws Exception {
        upgrade(FileNames.UPGRADED_BES_T, FileNames.UPGRADED_T_X1, SignatureType.ES_X_Type1);
    }

    @Test
    public void upgradeTToXL() throws Exception {
        upgrade(FileNames.UPGRADED_BES_T, FileNames.UPGRADED_T_XL, SignatureType.ES_XL_Type1);
    }

    @Test
    public void upgradeCToXL() throws Exception {
        upgrade(FileNames.UPGRADED_T_C, FileNames.UPGRADED_C_XL, SignatureType.ES_XL_Type1);
    }


}
