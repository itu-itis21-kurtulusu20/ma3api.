package dev.esya.api.cmssignature.orcun;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8.ERSAPrivateKey;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self.CertificateDateChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECSignatureTLVUtil;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.spec.ECParameterSpec;
import java.util.Calendar;

/**
 * Created by orcun.ertugrul on 11-Oct-17.
 */
public class SupportTest extends TestCase
{
    //dummy comment
    public void testSmartCardType() throws Exception{
        String[] cardTerminals = SmartOp.getCardTerminals();
        Pair<Long, CardType> slotAndCardType = SmartOp.getSlotAndCardType(cardTerminals[0]);

        System.out.println(slotAndCardType.second());
    }


    public void testRSA() throws Exception{
        SmartCard sc = new SmartCard(CardType.DIRAKHSM);

        long session = sc.openSession(31);
        sc.login(session, "123456");
        byte [] keyBytes = sc.generateRSAPrivateKey(session, 2048);
        System.out.println(StringUtil.toHexString(keyBytes));

        ERSAPrivateKey rsaPrivateKey = new ERSAPrivateKey(keyBytes);
        BigInteger modulus = rsaPrivateKey.getModulus();

        System.out.println(modulus.toString(16));
    }

    public void testEC() throws Exception{
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp384r1");

        SmartCard sc = new SmartCard(CardType.DIRAKHSM);

        long session = sc.openSession(31);
        sc.login(session, "123456");
        KeyPair keyPair = sc.generateECKeyPair(session, ecParameterSpec);


        byte[] encodedPublic = keyPair.getPrivate().getEncoded();
        byte[] encodedPrivate = keyPair.getPublic().getEncoded();


        System.out.println(StringUtil.toHexString(encodedPublic));
        System.out.println(StringUtil.toHexString(encodedPrivate));
    }


    
    
    public void testCertificate() throws Exception
    {
        byte [] x = StringUtil.hexToByte("021900010203040506070809100102030405060708091001020304");
        byte [] y = StringUtil.hexToByte("02170203040506070809100102030405060708091001020304");

        byte [] header = StringUtil.hexToByte("3034");

        byte [] sig = ByteUtil.concatAll(header ,x, y);

        boolean result = ECSignatureTLVUtil.isSignatureInTLVFormat(sig);

        System.out.println("Result: " + result);
    }

    public void testCertificateDate() throws Exception{
        ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-parent\\resources\\ug\\config\\certval-policy-test.xml");
        ValidationSystem validationSystem = CertificateValidation.createValidationSystem(policy);

        ECertificate cert = ECertificate.readFromFile("C:\\a\\a1.cer");
        CertificateStatusInfo csi = new CertificateStatusInfo(cert, Calendar.getInstance());

        CertificateDateChecker dateChecker = new CertificateDateChecker();
        dateChecker.setParentSystem(validationSystem);
        PathValidationResult result = dateChecker.check(csi);

        System.out.println(result);


    }


    public void testPfxParse() throws Exception {


        for(Integer i=0; i < 1000000;i++)
        {
            String password = String.format("%06d", i);
            try
            {
                PfxParser parser = new PfxParser(new FileInputStream("C:\\Users\\orcun.ertugrul\\Desktop\\981585gnkmertc.pfx"), password);
            }
            catch (Exception ex)
            {
                continue;
            }
            System.out.println("Password: " + password);
        }



    }
}
