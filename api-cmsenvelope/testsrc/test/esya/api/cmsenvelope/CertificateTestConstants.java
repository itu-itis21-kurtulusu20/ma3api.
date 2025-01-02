package test.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.util.List;

/**
 * Created by sura.emanet on 12.02.2018.
 */
public class CertificateTestConstants {

    private static final String PIN = "506436";
    private static final String PIN_EC = "607983";
    private static final String PFX_PATH = "T:\\api-parent\\resources\\unit-test-resources\\pfx\\sifreleme_RSA_sura_506436.pfx";
    private static final String PFX_PATH_EC = "T:\\api-parent\\resources\\unit-test-resources\\pfx\\sifreleme_EC_sura_607983.pfx";

    public static Pair<ECertificate, PrivateKey> getRSAEncCertificateAndKey() throws Exception {
        FileInputStream fis = new FileInputStream(PFX_PATH);
        PfxParser pfxParser = new PfxParser(fis, PIN.toCharArray());

        List<Pair<ECertificate, PrivateKey>> entries = pfxParser.getCertificatesAndKeys();
        if (entries != null)
            return entries.get(0);
        else
            return null;
    }


    public static Pair<ECertificate, PrivateKey> getECEncCertificateAndKey() throws Exception {
        FileInputStream fis = new FileInputStream(PFX_PATH_EC);
        PfxParser pfxParser = new PfxParser(fis, PIN_EC.toCharArray());

        List<Pair<ECertificate, PrivateKey>> entries = pfxParser.getCertificatesAndKeys();

        if (entries != null) {
            entries.get(0).setObject2(new GNUCryptoProvider().getKeyFactory().decodePrivateKey(AsymmetricAlg.ECDSA, entries.get(0).second().getEncoded()));
            return entries.get(0);
        } else
            return null;
      }
    }
