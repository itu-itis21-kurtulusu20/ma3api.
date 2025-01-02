package dev.esya.api.cmssignature.ec.compressed;

import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;

import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;

/**
 * Created by ramazan.girgin on 12/1/2014.
 */
public class CustomECDSAPublicKey implements ECPublicKey {
    NamedCurve namedCurve;
    ECDSAPublicKey ecdsaPublicKey;


    public CustomECDSAPublicKey(ECDSAPublicKey ecdsaPublicKey, NamedCurve namedCurve) {
        this.ecdsaPublicKey = ecdsaPublicKey;
        this.namedCurve = namedCurve;
    }

    @Override
    public ECPoint getW() {
        return ecdsaPublicKey.getW();
    }

    @Override
    public ECParameterSpec getParams() {
        return namedCurve;
    }

    @Override
    public String getAlgorithm() {
        return ecdsaPublicKey.getAlgorithm();
    }

    @Override
    public String getFormat() {
        return ecdsaPublicKey.getFormat();
    }

    @Override
    public byte[] getEncoded() {
        return ecdsaPublicKey.getEncoded();
    }
}
