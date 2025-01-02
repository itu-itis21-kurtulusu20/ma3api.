package dev.esya.api.cmssignature.smartcard;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.APDUSmartCard;

public class ApduSmartCardTest {

    @Test
    public void signByApdu() throws Exception{
        APDUSmartCard sc = new APDUSmartCard();
        sc.openSession(1);

        sc.login("12345");

        byte[] certBytes = sc.getSignatureCertificates().get(0);
        ECertificate cert = new ECertificate(certBytes);

        BaseSigner signer = sc.getSigner(cert.asX509Certificate(),
                Algorithms.SIGNATURE_RSA);

        byte []randBytes = RandomUtil.generateRandom(51);
        byte[] signature = signer.sign(randBytes);

        System.out.println(StringUtil.toHexString(signature));
    }

}
