package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.eqproof;

import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ec.ECPointUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.ECPoint;

/*
 * Takasbank Biga Projesi için eklendi.
 **/

public class EQProofData {

    ECPoint pointA;
    ECPoint pointB;

    public EQProofData(ECPoint pointA, ECPoint pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public byte[] getEncoded() throws IOException {
        byte[] encodedPointA = ECPointUtil.encodePointUnsafely(pointA);
        byte[] encodedPointB = ECPointUtil.encodePointUnsafely(pointB);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DerOutputStream derOutputStream = new DerOutputStream();

        derOutputStream.putOctetString(encodedPointA);
        derOutputStream.putOctetString(encodedPointB);

        derOutputStream.derEncode(baos);
        derOutputStream.reset();
        derOutputStream.putDerValue(new DerValue(DerValue.tag_Sequence, baos.toByteArray()));
        baos.reset();
        derOutputStream.derEncode(baos);

        return baos.toByteArray();
    }
}
