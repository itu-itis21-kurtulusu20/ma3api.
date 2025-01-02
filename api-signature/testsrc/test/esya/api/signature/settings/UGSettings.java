package test.esya.api.signature.settings;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignableBytes;
import tr.gov.tubitak.uekae.esya.api.signature.util.PfxSigner;

/**
 * @author ayetgin
 */
public class UGSettings implements TestSettings
{
    private String pfxPath = "./api-signature/testresources/ahmet.yetgin_890111@ug.net.pfx";
    private String pfxPass = "890111";

    private PfxSigner signer;

    public UGSettings()
    {
        signer = new PfxSigner(SignatureAlg.RSA_SHA256, pfxPath, pfxPass.toCharArray());
    }

    public String getBaseDir()
    {
        return null;
    }

    public BaseSigner getSigner()
    {
        return signer;
    }

    public ECertificate getSignersCertificate()
    {
        return signer.getSignersCertificate();
    }

    public Signable getContent()
    {
        return new SignableBytes("test data".getBytes(), "data.txt", "text/plain");
    }
}
