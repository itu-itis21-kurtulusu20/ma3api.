package test.esya.api.asic;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.*;

import java.io.File;
import java.io.FileOutputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * @uathor ayetgin
 */
public class ExtendedTest extends ASiCBaseTest {

    @Test
    public void appendContainer_XAdES() throws Exception {
        SignaturePackage sp = read(PackageType.ASiC_E, SignatureFormat.XAdES, SignatureType.ES_BES);
        Signature existing = sp.getContainers().get(0).getSignatures().get(0);
        SignatureContainer sc = sp.createContainer();
        Signature s = sc.createSignature(CERTIFICATE);
        // get signable from signature
        s.addContent(existing.getContents().get(0), false);
        s.sign(SIGNER);
        String fileName = fileName(PackageType.ASiC_E, SignatureFormat.XAdES, SignatureType.ES_BES) + "extended.asice";
        sp.write(new FileOutputStream(fileName));

        SignaturePackage sp2 = SignaturePackageFactory.readPackage(new Context(), new File(fileName));
        PackageValidationResult pvr = sp2.verifyAll();

        assertEquals(pvr.toString(), PackageValidationResultType.ALL_VALID, pvr.getResultType());
    }

    @Test
    public void appendContainer_CAdES() throws Exception {
        SignaturePackage sp = read(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES);
        SignatureContainer sc = sp.createContainer();
        Signature s = sc.createSignature(CERTIFICATE);
        // get signable from package
        s.addContent(sp.getDatas().get(0), false);
        s.sign(SIGNER);
        String fileName = fileName(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES) + "extended.asice";
        sp.write(new FileOutputStream(fileName));

        SignaturePackage sp2 = SignaturePackageFactory.readPackage(new Context(), new File(fileName));
        PackageValidationResult pvr = sp2.verifyAll();

        assertEquals(pvr.toString(), PackageValidationResultType.ALL_VALID, pvr.getResultType());
    }

}
