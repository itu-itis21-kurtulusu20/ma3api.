package test.esya.api.asic;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackage;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackageFactory;
import tr.gov.tubitak.uekae.esya.api.signature.util.PfxSigner;

import java.io.File;

/**
 * @uathor ayetgin
 */
public class ASiCBaseTest {

    static String root = "T:\\api-asic\\testresources";

    static String outDir = root + "/created/";
    static File dataFile = new File(outDir + "sample.txt");
    static ECertificate CERTIFICATE;
    static BaseSigner SIGNER;

    static {
        String pfxPath = "T:\\api-parent\\resources\\pfx\\test1@test.com_745418.pfx";
        String pfxPass = "745418";

        PfxSigner signer = new PfxSigner(SignatureAlg.RSA_SHA256, pfxPath, pfxPass.toCharArray());
        CERTIFICATE = signer.getSignersCertificate();
        SIGNER = signer;

    }

    protected String fileName(PackageType packageType, SignatureFormat format, SignatureType type){
        String fileName = outDir + packageType + "-"+format.name() + "-" +type;
        switch (packageType){
            case ASiC_S : return fileName + ".asics";
            case ASiC_E : return fileName + ".asice";
        }
        return null;
    }

    protected SignaturePackage read(PackageType packageType, SignatureFormat format, SignatureType type) throws Exception
    {
        //Context c = new Context();
        Context c = createContext();
        File f = new File(fileName(packageType, format, type));
        return SignaturePackageFactory.readPackage(c, f);
    }

    public static Context createContext(){
        Context c = new Context(new File(outDir).toURI());
        c.setConfig(new Config(root + "/esya-signature-config.xml"));
        //c.setData(getContent()); //for detached CAdES signatures validation
        return c;
    }

}
