package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.elgamal;

import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ec.ECPointUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.ECPoint;
import java.text.MessageFormat;

/*
 * Takasbank Biga Projesi için eklendi.
 **/

public class ElGamalPlainData {

    public static final int MAX_POINT_COUNT = 16;

    private byte[] encoded = null;
    ECPoint[] points = null;

    public ElGamalPlainData(byte[] encoded) throws ESYAException, IOException {
        this.encoded = encoded;
        decode();
    }

    public ElGamalPlainData(ECPoint[] points) throws IllegalArgumentException {
        if (points.length > MAX_POINT_COUNT) {
            throw new IllegalArgumentException("Too many points");
        }

        this.points = points;
    }

    public ElGamalPlainData(ECPoint point) {
        this.points = new ECPoint[]{point};
    }

    public byte[] getEncoded() throws IOException {
        if (encoded == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            DerOutputStream derOutputStream = new DerOutputStream();

            for (ECPoint point : points) {
                byte[] encodedPoint = ECPointUtil.encodePointUnsafely(point);
                derOutputStream.putOctetString(encodedPoint);
            }

            derOutputStream.derEncode(baos);
            derOutputStream.reset();
            derOutputStream.putDerValue(new DerValue(DerValue.tag_Sequence, baos.toByteArray()));
            baos.reset();
            derOutputStream.derEncode(baos);

            encoded = baos.toByteArray();
        }

        return encoded;
    }

    private void decode() throws IOException, ESYAException {
        DerInputStream derInputStream = new DerInputStream(encoded);

        // extract DerSequence first
        DerValue[] values = derInputStream.getSequence(0);

        if (values.length > MAX_POINT_COUNT) {
            throw new IllegalArgumentException("Too many points");
        }

        // then get the other values

        points = new ECPoint[values.length];

        for (int i = 0; i < values.length; i++) {
            byte[] encodedPoint = values[i].getOctetString();
            points[i] = ECPointUtil.getECPoint(encodedPoint);
        }
    }

    public ECPoint[] getPoints() throws ESYAException, IOException {
        if (points == null) {
            decode();
        }

        return points;
    }

    public ECPoint getPoint(int index) throws ESYAException, IOException {
        if (points == null) {
            decode();
        }

        if (index > points.length) {
            throw new ESYAException(MessageFormat.format("Given index ({0}) exceeds sub-message count ({1})", index, points.length));
        }

        return points[index];
    }
}
