package test.esya.api.asic;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignableFile;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.*;

import java.io.File;
import java.io.FileOutputStream;

import static junit.framework.TestCase.assertEquals;

/**
 * @author ayetgin
 */
public class GenerationTest extends ASiCBaseTest  {

    public void createBES(PackageType packageType, SignatureFormat format) throws Exception {

        //Context c = new Context();
        Context c = createContext();
        c.getConfig().getAlgorithmsConfig().setSignatureAlg(SignatureAlg.RSA_SHA256);
        SignaturePackage signaturePackage = SignaturePackageFactory.createPackage(c, packageType, format);

        // add into zip
        Signable inPackage = signaturePackage.addData(new SignableFile(dataFile, "cgi/bin"), dataFile.getName());

        SignatureContainer container = signaturePackage.createContainer();
        Signature signature = container.createSignature(CERTIFICATE);

        // pass document in ZIP to signature
        signature.addContent(inPackage, false);

        signature.sign(SIGNER);

        String fileName = fileName(packageType, format, SignatureType.ES_BES);
        signaturePackage.write(new FileOutputStream(fileName));

        // read it back
        signaturePackage = SignaturePackageFactory.readPackage(c, new File(fileName));

        // validate
        PackageValidationResult pvr = signaturePackage.verifyAll();

        assertEquals(pvr.toString(), PackageValidationResultType.ALL_VALID, pvr.getResultType()) ;
    }

    @Test
    public void createASiC_S_CAdES() throws Exception {
        createBES(PackageType.ASiC_S, SignatureFormat.CAdES);
    }

    @Test
    public void createASiC_E_CAdES() throws Exception {
        createBES(PackageType.ASiC_E, SignatureFormat.CAdES);
    }

    @Test
    public void createASiC_S_XAdES() throws Exception {
        createBES(PackageType.ASiC_S, SignatureFormat.XAdES);
    }

    @Test
    public void createASiC_E_XAdES() throws Exception {
        createBES(PackageType.ASiC_E, SignatureFormat.XAdES);
    }

}
