package dev.esya.api.cmssignature.ec.compressed;

import gnu.crypto.key.ecdsa.ECDSAKeyPairX509Codec;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import org.junit.Assert;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ec.ECPointUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;

import java.security.KeyPair;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.List;

public class SecretECPointTest {

    @Test
    public void compareECPointOfCertAndSCPublicKey() throws Exception {
        //String label = "ectest1#mail.netNES0Test Özel Eğrisi38B17FA9D7796BB8";
        String label = "ephemeralKey2ForECDH";

        SmartCard smartCard = new SmartCard(CardType.DIRAKHSM);
        long session = smartCard.openSession(89);
        smartCard.login(session, "12345678");

        List<byte[]> bytes = smartCard.readCertificate(session, label);
        ECertificate eCertificate = new ECertificate(bytes.get(0));

        byte[] subjectPublicKey = eCertificate.getSubjectPublicKeyInfo().getSubjectPublicKey();
        System.out.println(StringUtil.toHexString(subjectPublicKey));

        byte[] certX = ECPointUtil.getXPoint(subjectPublicKey);
        certX = ECPointUtil.trimZeroes(certX);


        CK_ATTRIBUTE[] ecPublicKeyTemplate =
                {
                        new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_POINT)
                };

        long publicKeyObjIDFromPublicKeyLabel = smartCard.getPublicKeyObjIDFromPublicKeyLabel(session, label);
        smartCard.getAttributeValue(session, publicKeyObjIDFromPublicKeyLabel, ecPublicKeyTemplate);
        byte[] point = (byte[]) ecPublicKeyTemplate[0].pValue;

        DerValue derValue = new DerValue(point);
        byte[] dataBytes = derValue.getDataBytes();

        byte[] smartCardX = ECPointUtil.getXPoint(dataBytes);
        smartCardX = ECPointUtil.trimZeroes(smartCardX);

        Assert.assertArrayEquals(certX, smartCardX);
    }

    @Test
    public void xPointToYPoint() throws Exception {
        // get parameter spec
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp521r1");

        // create key pair template
        ParamsWithECParameterSpec keyGenParams = new ParamsWithECParameterSpec(ecParameterSpec);
        KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keyGenParams);

        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate("secretCurve", ecParameterSpec, keyPair.getPrivate(), keyPair.getPublic());
        ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
        ecKeyPairTemplate.getAsSecretECCurveTemplate(true);

        // get parameter spec
        ECDomainParameter domainParameter = ECDomainParameter.getInstance(ecParameterSpec);

        // pointX
        byte[] pointX = (byte[]) ecKeyPairTemplate.getPublicKeyTemplate().getAttribute(PKCS11Constants.CKA_EC_POINT);
        DerValue derValue = new DerValue(pointX);
        byte[] pointXDataBytes = derValue.getDataBytes();

        // pointX to pointY and generate public key
        ECGNUPoint ecPoint = new ECDSAKeyPairX509Codec().getECPoint(domainParameter, pointXDataBytes);
        ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecParameterSpec);
        ECPublicKey ecPublicKey = (ECPublicKey) KeyUtil.generatePublicKey(keySpec);

        // comparison
        Assert.assertEquals(((ECPublicKey) keyPair.getPublic()).getW(), ecPublicKey.getW());
    }

    // for non secret curve
    @Test
    public void compareECPoint() throws Exception {
        String label = "enc d1 3";

        SmartCard smartCard = new SmartCard(CardType.DIRAKHSM);
        long session = smartCard.openSession(23);
        smartCard.login(session, "123456");

        List<byte[]> bytes = smartCard.readCertificate(session, label);
        ECertificate eCertificate = new ECertificate(bytes.get(0));

        ECPublicKey publicKey = (ECPublicKey) KeyUtil.decodePublicKey(eCertificate.getSubjectPublicKeyInfo());
        ECPoint ecPoint = publicKey.getW();

        ECPublicKeySpec keySpec = (ECPublicKeySpec) smartCard.readPublicKeySpec(session, label);
        ECPoint ecPoint1 = keySpec.getW();

        Assert.assertTrue(ecPoint.getAffineX().equals(ecPoint1.getAffineX()) && ecPoint.getAffineY().equals(ecPoint1.getAffineY()));
    }
}
