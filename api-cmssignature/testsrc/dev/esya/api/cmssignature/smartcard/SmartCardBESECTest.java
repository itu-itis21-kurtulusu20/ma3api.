package dev.esya.api.cmssignature.smartcard;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithCertSerialNo;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by sura.emanet on 7.01.2020.
 */

@RunWith(Parameterized.class)
public class SmartCardBESECTest extends CMSSignatureTest{

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
    public void createSign() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        byte [] certBytes = sc.getSignatureCertificates(sid).get(0);
        ECertificate cert = new ECertificate(certBytes);
        BaseSigner signer = new SCSignerWithCertSerialNo(sc, sid, slotNo, cert.getSerialNumber().toByteArray(), signatureAlg.getName());

        bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }
}
