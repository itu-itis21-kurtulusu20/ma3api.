package tr.gov.tubitak.uekae.esya.api.smartcard;

import gnu.crypto.key.ecdsa.ECDSAKeyPairX509Codec;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.text.MessageFormat;

public class Akis25SecretECTest {
    @Test
    public void test_11_C1() throws Exception {
        final String curveName = "secp224r1";
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);
        byte[] pointBytes = StringUtil.toByteArray("02" + "06F19046A33D3FCFD2BBA22C6974408CA1034B9D4FF02DD6ACC3078D");

        ECDomainParameter domainParameter = ECDomainParameter.getInstance(ecParameterSpec);

        ECGNUPoint ecPoint = new ECDSAKeyPairX509Codec().getECPoint(domainParameter, pointBytes);
        ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecParameterSpec);

        KeyFactory keyFactory = KeyFactory.getInstance("EC", "SunEC");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        // System.out.println(pubKey.getAlgorithm());
        System.out.println(MessageFormat.format("X: {0} ({1})", ecPoint.getAffineX().toString(10), StringUtil.toHexString(ecPoint.getAffineX().toByteArray())));
        System.out.println(MessageFormat.format("Y: {0} ({1})", ecPoint.getAffineY().toString(10), StringUtil.toHexString(ecPoint.getAffineY().toByteArray())));

        // _signAndVerifyECDSA(sc, signatureAlg, keyLabel, pubKey);
    }
}
