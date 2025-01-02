package dev.esya.api.xmlsignature.legacy.bes.smartcard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithCertSerialNo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by sura.emanet on 8.01.2020.
 */

@RunWith(Parameterized.class)
public class SmartCardBESECTest extends XMLSignatureTestBase {

    static final String PASSWORD = "12345";

    static boolean fipsMode = true;
    static SmartCard sc = null;
    static long sid = 0;
    static long slotNo = 0;

    SignatureAlg signatureAlg;

    /*ECDSA_SHA224 algoritması ile ilgili CKR_DATA_LEN_RANGE hatası alınıyor.
      AKIS ECDSA ve RSA için SHA1, SHA256, SHA384 ve SHA512 desteklendiğini belirtti.*/

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {SignatureAlg.ECDSA_SHA1},
                {SignatureAlg.ECDSA_SHA256},
                {SignatureAlg.ECDSA_SHA384},
                {SignatureAlg.ECDSA_SHA512}
        });
    }

    public SmartCardBESECTest(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Before
    public void setUp() throws Exception
    {
        sc = new SmartCard(CardType.AKIS);
        sc.setFipsMode(fipsMode);
        slotNo = sc.getTokenPresentSlotList()[0];
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
    public void testCreateEnveloping() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);

        byte [] certBytes = sc.getSignatureCertificates(sid).get(0);
        ECertificate cert = new ECertificate(certBytes);
        BaseSigner signer = new SCSignerWithCertSerialNo(sc, sid, slotNo, cert.getSerialNumber().toByteArray(), signatureAlg.getName());

        signature.addKeyInfo(cert);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid( BASEDIR, signatureBytes.toByteArray());
    }
}
