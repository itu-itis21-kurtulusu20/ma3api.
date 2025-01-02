package util;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECParameterSpec;

/**
 * Created by ramazan.girgin on 12/1/2014.
 */
public class CustomECDSAPrivateKey implements ECPrivateKey {

    NamedCurve namedCurve=null;
    ECDSAPrivateKey ecdsaPrivateKey;
    public CustomECDSAPrivateKey(ECDSAPrivateKey ecdsaPrivateKey,CustomCurve customCurve) throws IOException {
        this.namedCurve =new NamedCurve(customCurve.getName(),customCurve.getOid(),customCurve.getCurve(),customCurve.getGenerator(),customCurve.getOrder(),customCurve.getCofactor());
        this.ecdsaPrivateKey = ecdsaPrivateKey;
    }

    public CustomECDSAPrivateKey(ECDSAPrivateKey ecdsaPrivateKey,NamedCurve namedCurve) throws IOException {
        this.namedCurve =namedCurve;
        this.ecdsaPrivateKey = ecdsaPrivateKey;
    }


    public ECDSAPrivateKey getEcdsaPrivateKey() {
        return ecdsaPrivateKey;
    }

    @Override
    public ECParameterSpec getParams() {
        return namedCurve;
    }

    @Override
    public BigInteger getS() {
        return ecdsaPrivateKey.getS();
    }

    @Override
    public String getAlgorithm() {
        return ecdsaPrivateKey.getAlgorithm();
    }

    @Override
    public String getFormat() {
        return ecdsaPrivateKey.getAlgorithm();
    }

    @Override
    public byte[] getEncoded() {
        return ecdsaPrivateKey.getEncoded();
    }
}
