package dev.esya.api.cmssignature.smartcard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ESYASmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.List;

/**
 * Created by sura.emanet on 22.02.2018.
 */

public class PfxToSmartCard {

    static final String PASSWORD = "12345";

    static boolean fipsMode = true;
    static SmartCard sc = null;
    static long sid = 0;
    static long slotNo = 0;

    static final String PFX_PIN = "506436";
    static final String PFX_PATH = "T:\\api-parent\\resources\\unit-test-resources\\pfx\\sifreleme_RSA_sura_506436.pfx";

    @Before
    public void setUp() throws Exception
    {
        sc = new ESYASmartCard(CardType.NCIPHER);
        sc.setFipsMode(fipsMode);
        slotNo = getSlot();
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp()  throws Exception
    {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    @Test
    public void importCertificateAndKeys() throws Exception {

        String privKeyLabel = "RSA_Private_Import_" + System.currentTimeMillis();
        String pubKeyLabel  = "RSA_Public_Import_" + System.currentTimeMillis();
        String certLabel = "Test Sertifikasi";

        PfxParser parser = new PfxParser(new FileInputStream(PFX_PATH),PFX_PIN.toCharArray());
        List<Pair<ECertificate, PrivateKey>> entries = parser.getCertificatesAndKeys();

        Pair<ECertificate, PrivateKey> entry = entries.get(0);
        ECertificate cert = entry.first();

        RSAPrivateKeyTemplate privateKeyTemplate = new RSAPrivateKeyTemplate(privKeyLabel, (RSAPrivateCrtKey) entry.second(), null);
        privateKeyTemplate.getAsTokenTemplate(false, true, false);

        RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(pubKeyLabel, (java.security.interfaces.RSAPublicKey) cert.asX509Certificate().getPublicKey());
        publicKeyTemplate.getAsTokenTemplate(false, true, false);

        RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate, privateKeyTemplate);

        sc.importKeyPair(sid, keyPairTemplate);
        sc.importCertificate(sid,certLabel,cert.asX509Certificate());

    }

    private static long getSlot() throws PKCS11Exception
    {
        long[] slots = sc.getTokenPresentSlotList();
        return slots[1];
    }
}

