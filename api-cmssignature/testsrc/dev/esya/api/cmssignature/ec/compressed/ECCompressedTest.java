package dev.esya.api.cmssignature.ec.compressed;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;

/**
 * Created by orcun.ertugrul on 14-Mar-18.
 */
public class ECCompressedTest {
    @Test
    public void importingECCompressedCertToSmartCard() throws Exception {
        byte[] certBytes = AsnIO.dosyadanOKU("T:\\tmp\\TestCSCA.cer");

        ECertificate cert = new ECertificate(certBytes);

        X509Certificate x509Certificate = new X509CompressedECCertificate(cert);

        byte[] issuer = x509Certificate.getIssuerX500Principal().getEncoded();
        byte[] subject = x509Certificate.getSubjectX500Principal().getEncoded();

        ECPublicKey ecPublicKey = (ECPublicKey) x509Certificate.getPublicKey();

        SmartCard sc = new SmartCard(CardType.AKIS);
        long slotNo = sc.getSlotList()[0];
        long sessionNo = sc.openSession(slotNo);
        sc.login(sessionNo, "123456");

        sc.importCertificate(sessionNo, "MyCert3", x509Certificate);
    }

}
