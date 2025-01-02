package test.esya.api.cmsenvelope;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyAgreement;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyPairGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithSharedInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.spec.ECParameterSpec;

public class ECDHWithAPI {

    @Test
    public void ECDHTest() throws CryptoException {
        // Key generation
        ECParameterSpec ecParameterSpecOfSECP384R1 = NamedCurve.getECParameterSpec("secp384r1");
        ParamsWithECParameterSpec paramsWithECParameterSpec = new ParamsWithECParameterSpec(ecParameterSpecOfSECP384R1);

        KeyPairGenerator kpg = Crypto.getProvider().getKeyPairGenerator(AsymmetricAlg.ECDSA);
        KeyPair ephemeralKey1 = kpg.generateKeyPair(paramsWithECParameterSpec);
        KeyPair ephemeralKey2 = kpg.generateKeyPair(paramsWithECParameterSpec);
        // Key generation

        // Generate shared info bytes that will be used in key derivation
        WrapAlg wrapAlg = WrapAlg.AES256;
        int keyLength = KeyUtil.getKeyLength(wrapAlg);
        byte[] sharedInfoBytes = ECUtil.generateKeyAgreementSharedInfoBytes(wrapAlg.getOID(), keyLength, null);
        AlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);
        // Generate shared info bytes that will be used in key derivation

        // ECDH
        KeyAgreement keyAgreement = Crypto.getProvider().getKeyAgreement(KeyAgreementAlg.ECDH_SHA256KDF);

        keyAgreement.init(ephemeralKey1.getPrivate(), sharedInfoParam);
        SecretKey secretKeyBytes1 = keyAgreement.generateKey(ephemeralKey2.getPublic(), wrapAlg);

        keyAgreement.init(ephemeralKey2.getPrivate(), sharedInfoParam);
        SecretKey secretKeyBytes2 = keyAgreement.generateKey(ephemeralKey1.getPublic(), wrapAlg);
        // ECDH

        // Comparison of derived keys
        Assert.assertArrayEquals(secretKeyBytes1.getEncoded(), secretKeyBytes2.getEncoded());
    }
}
