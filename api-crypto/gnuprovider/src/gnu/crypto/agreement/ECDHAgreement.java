package gnu.crypto.agreement;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import gnu.crypto.util.Util;

import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class ECDHAgreement implements BaseAgreement {
    ECDSAPrivateKey privKey;

    public byte[] calculateAgreement(Key pubKey) {
        ECDSAPublicKey pub = ECDSAPublicKey.fromECPublicKey((ECPublicKey) pubKey);
        ECGNUPoint P = pub.getMQ().multiply(privKey.getMD());

        BigInteger affineX = P.getAffineX();

        int byteLen = (pub.getMParameters().getCurve().getField().getFieldSize() + 7) / 8;
        byte[] agreement = Util.toBytes(affineX, byteLen);

        return agreement;
    }

    public void init(Key key) {
        this.privKey = ECDSAPrivateKey.fromECPrivateKey((ECPrivateKey) key);
    }

}
