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

public class ElGamalEncryptedData {

    public static final int MAX_POINT_COUNT = 16;

    private byte[] encoded = null;
    private ECPoint[] pointsA = null;
    private ECPoint[] pointsB = null;

    public ElGamalEncryptedData(byte[] encoded) throws ESYAException, IOException {
        this.encoded = encoded;
        decode();
    }

    public ElGamalEncryptedData(ECPoint pointA, ECPoint pointB) {
        this.pointsA = new ECPoint[]{pointA};
        this.pointsB = new ECPoint[]{pointB};
    }

    public ElGamalEncryptedData(ECPoint[] pointsA, ECPoint[] pointsB) {
        assert pointsA.length == pointsB.length;

        this.pointsA = pointsA;
        this.pointsB = pointsB;
    }

    public byte[] getEncoded() throws IOException {
        if (encoded == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            DerOutputStream derOutputStream = new DerOutputStream();

            for (int i = 0; i < pointsA.length; i++) {
                byte[] encodedPointA = ECPointUtil.encodePointUnsafely(pointsA[i]);
                byte[] encodedPointB = ECPointUtil.encodePointUnsafely(pointsB[i]);

                derOutputStream.putOctetString(encodedPointA);
                derOutputStream.putOctetString(encodedPointB);
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

        if (values.length > MAX_POINT_COUNT * 2) {
            throw new IllegalArgumentException("Too many points");
        }

        // then get the other values

        pointsA = new ECPoint[values.length / 2];
        pointsB = new ECPoint[values.length / 2];

        for (int i = 0; i < values.length; i += 2) {
            byte[] encodedPointA = values[i].getOctetString();
            pointsA[i / 2] = ECPointUtil.getECPoint(encodedPointA);

            byte[] encodedPointB = values[i + 1].getOctetString();
            pointsB[i / 2] = ECPointUtil.getECPoint(encodedPointB);
        }
    }

    public ECPoint[] getPointsA() throws ESYAException, IOException {
        if (pointsA == null) {
            decode();
        }

        return pointsA;
    }

    public ECPoint[] getPointsB() throws ESYAException, IOException {
        if (pointsB == null) {
            decode();
        }

        return pointsB;
    }

    public ECPoint getPointA(int index) throws ESYAException, IOException {
        if (pointsA == null) {
            decode();
        }

        if (index > pointsA.length) {
            throw new ESYAException(MessageFormat.format("Given index ({0}) exceeds sub-message count ({1})", index, pointsA.length));
        }

        return pointsA[index];
    }

    public ECPoint getPointB(int index) throws ESYAException, IOException {
        if (pointsB == null) {
            decode();
        }

        if (index > pointsB.length) {
            throw new ESYAException(MessageFormat.format("Given index ({0}) exceeds sub-message count ({1})", index, pointsB.length));
        }

        return pointsB[index];
    }
}
