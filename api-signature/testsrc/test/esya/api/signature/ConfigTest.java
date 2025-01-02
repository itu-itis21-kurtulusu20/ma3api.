package test.esya.api.signature;

import junit.framework.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.config.CertificateValidationConfig;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

/**
 * @author ayetgin
 */
public class ConfigTest
{

    @Test
    public void load(){
        Config c = new Config();
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getAlgorithmsConfig().getDigestAlg(), DigestAlg.SHA256);
        Assert.assertEquals(c.getAlgorithmsConfig().getSignatureAlg(), SignatureAlg.RSA_SHA256);
    }

    @Test
    public void testTimeConfig() {
        Config c = new Config();
        CertificateValidationConfig config = c.getCertificateValidationConfig();

        System.out.println("Grace period: " + config.getGracePeriodInSeconds());
        System.out.println("Last revocation period: " + config.getLastRevocationPeriodInSeconds());
        System.out.println("Signing time tolerance: " + config.getSigningTimeToleranceInSeconds());
    }
}
