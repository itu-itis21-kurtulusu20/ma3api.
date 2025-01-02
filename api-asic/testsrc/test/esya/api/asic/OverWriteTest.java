package test.esya.api.asic;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignableFile;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.*;

import java.io.File;
import java.io.FileOutputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * @author ayetgin
 */
public class OverWriteTest extends ASiCBaseTest {

    @Test
    public void update_CAdES() throws Exception {
        SignaturePackage sp = read(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES);
        String fileName = fileName(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES)+"-upgraded.asice";
        File toUpgrade = new File(fileName);

        // create a copy tu update package
        sp.write(new FileOutputStream(fileName));

        // add signature
        SignaturePackage sp2 = SignaturePackageFactory.readPackage(new Context(), toUpgrade);

        Signable data = new SignableFile(new File(outDir+"/sample.bin"), "app/bin");
        Signable sdata = sp2.addData(data, "sample.png");

        SignatureContainer sc = sp2.createContainer();

        Signature s = sc.createSignature(CERTIFICATE);

        s.addContent(sdata /*sp.getDatas().get(0)*/, false);
        s.sign(SIGNER);

        // write onto read file!
        sp2.write();

        // read again to verify
        SignaturePackage sp3 = SignaturePackageFactory.readPackage(new Context(), new File(fileName));
        PackageValidationResult pvr = sp3.verifyAll();

        assertEquals(pvr.toString(), PackageValidationResultType.ALL_VALID, pvr.getResultType()) ;
    }

}
