package dev.esya.api.smartcard;

import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;

import java.io.IOException;
import java.util.List;

public class SmartCardUtilityTest {
    @Test
    public void deleteAllObjects() throws Exception{
        SmartCard card = new SmartCard(CardType.DIRAKHSM);

        for(int i=1; i<=12;i++)
        {
            long session = card.openSession(i);
            card.login(session, "123456");

            CK_ATTRIBUTE [] attrs = new CK_ATTRIBUTE[]{
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true)
            };

            long[] objects = card.objeAra(session, attrs);
            PKCS11 pkcs11 = ((PKCS11Ops)(card.getCardType().getCardTemplate().getPKCS11Ops())).getmPKCS11();
            for (long objectId: objects) {
                pkcs11.C_DestroyObject(session,objectId);
            }
        }
    }


    @Test
    public void TestGettingAllCertificates() throws PKCS11Exception, IOException, ESYAException {
        long slotID = 1;
        SmartCard sc = new SmartCard(CardType.AKIS);

        long session = sc.openSession(slotID);

        List<byte[]> certificates = sc.getCertificates(session);


        for (byte [] cert: certificates) {
            ECertificate eCertificate = new ECertificate(cert);
            System.out.println(eCertificate);
        }
    }

    @Test
    public void TestGettingSignatureCertificates() throws PKCS11Exception, IOException, ESYAException {
        long slotID = 1;
        SmartCard sc = new SmartCard(CardType.AKIS);

        long session = sc.openSession(slotID);

        List<byte[]> certificates = sc.getSignatureCertificates(session);


        for (byte [] cert: certificates) {
            ECertificate eCertificate = new ECertificate(cert);
            System.out.println(eCertificate);
        }
    }

    @Test
    public void TestGettingEncryptionCertificates() throws PKCS11Exception, IOException, ESYAException {
        long slotID = 1;
        SmartCard sc = new SmartCard(CardType.AKIS);

        long session = sc.openSession(slotID);

        List<byte[]> certificates = sc.getEncryptionCertificates(session);


        for (byte [] cert: certificates) {
            ECertificate eCertificate = new ECertificate(cert);
            System.out.println(eCertificate);
        }
    }
}
