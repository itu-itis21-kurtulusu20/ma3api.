package gnu.crypto.agreement;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import gnu.crypto.util.Util;

import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class ECCofactorDHAgreement implements BaseAgreement {

    ECDSAPrivateKey privKey;


    public byte[] calculateAgreement(Key pubKey) {
        ECDSAPublicKey pub = ECDSAPublicKey.fromECPublicKey((ECPublicKey) pubKey);
        ECDomainParameter params = pub.getMParameters();

        BigInteger cofactor = BigInteger.valueOf(params.getCofactor());

        ECGNUPoint P = pub.getMQ().multiply(cofactor.multiply(privKey.getMD()));
        BigInteger affineX = P.getAffineX();

        int byteLen = (pub.getMParameters().getCurve().getField().getFieldSize() + 7) / 8;
        byte[] agreement = Util.toBytes(affineX, byteLen);

        return agreement;
    }


    public void init(Key privKey) {
        this.privKey = ECDSAPrivateKey.fromECPrivateKey((ECPrivateKey) privKey);
    }

}
