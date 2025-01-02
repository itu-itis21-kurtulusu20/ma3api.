package tr.gov.tubitak.uekae.esya.api.common.crypto.ec;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.math.BigInteger;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.Arrays;

public class ECPointUtil {

    public static final byte UNCOMPRESSED_POINT_TAG = 0x04;

    public static byte[] encodePoint(ECPoint point, EllipticCurve curve) {
        return encodePoint(point, curve, false);
    }

    public static byte[] encodePoint(ECPoint point, EllipticCurve curve, boolean compress) {
        if (compress) {

            // X9.62 Page 21 Section 4.3.6
            // 1) Convert the field element x_p to an octet string X_1

            byte[] X_1 = point.getAffineX().toByteArray();

            // 2.1) Compute the bit y_pTilda
            // 2.2) Assign the value 02 to the single octet PC if y_pTilda is
            // 0,
            // or the value 03 if y_pTilda is 1
            // 2.3) The result is the octet string PO = PC X_1
            byte[] result = new byte[X_1.length + 1];
            result[0] = point.getAffineY().testBit(0) ? (byte) 0x03 : (byte) 0x02;
            System.arraycopy(X_1,
                    0,
                    result,
                    1,
                    X_1.length
            );
            return result;

        } else {
            // get field size in bytes (rounding up)
            int n = (curve.getField().getFieldSize() + 7) >> 3;
            byte[] xb = trimZeroes(point.getAffineX().toByteArray());
            byte[] yb = trimZeroes(point.getAffineY().toByteArray());
            if ((xb.length > n) || (yb.length > n)) {
                throw new RuntimeException("Point coordinates do not match field size");
            }
            byte[] r = new byte[1 + (n << 1)];
            r[0] = 4; // uncompressed
            System.arraycopy(xb, 0, r, n - xb.length + 1, xb.length);
            System.arraycopy(yb, 0, r, r.length - yb.length, yb.length);

            return r;
        }
    }

    public static byte[] trimZeroes(byte[] b)
    {
        int i = 0;
        while ((i < b.length - 1) && (b[i] == 0))
        {
            i++;
        }
        if (i == 0)
        {
            return b;
        }
        byte[] t = new byte[b.length - i];
        System.arraycopy(b, i, t, 0, t.length);
        return t;
    }

    public static ECPoint getECPoint(byte[] point) throws ESYAException {
        if (point.length == 1 && point[0] == (byte) 0) {
            return ECPoint.POINT_INFINITY;
        }

        if (point[0] != UNCOMPRESSED_POINT_TAG) {
            throw new ESYAException("Cannot parse non-uncompressed EC point");
        }

        int axisLen = (point.length - 1) / 2;

        byte[] xBytes = Arrays.copyOfRange(point, 1, 1 + axisLen);
        byte[] yBytes = Arrays.copyOfRange(point, 1 + axisLen, 1 + axisLen + axisLen);

        return new ECPoint(new BigInteger(1, xBytes), new BigInteger(1, yBytes));
    }

    public static ECPoint getECPointRaw(byte[] point) throws ESYAException {
        if (point[0] != UNCOMPRESSED_POINT_TAG) {
            throw new ESYAException("Cannot parse non-uncompressed EC point");
        }

        int axisLen = (point.length - 1) / 2;

        byte[] xBytes = Arrays.copyOfRange(point, 1, 1 + axisLen);
        byte[] yBytes = Arrays.copyOfRange(point, 1 + axisLen, 1 + axisLen + axisLen);

        return new ECPoint(new BigInteger(xBytes), new BigInteger(yBytes));
    }

    public static byte[] encodePointUnsafely(ECPoint point) {
        final byte[] xb, yb;

        if (point.equals(ECPoint.POINT_INFINITY)) {
            xb = new byte[0];
            yb = new byte[0];
        } else {
            xb = trimZeroes(point.getAffineX().toByteArray());
            yb = trimZeroes(point.getAffineY().toByteArray());
        }

        int len = Math.max(xb.length, yb.length);

        byte[] r = new byte[1 + len * 2];
        r[0] = UNCOMPRESSED_POINT_TAG;
        //Koordinatların başında 00 olacak şekilde kopyalama yapıldı.
        System.arraycopy(xb, 0, r, 1 + len - xb.length, xb.length);
        System.arraycopy(yb, 0, r, 1 + len + (len - yb.length), yb.length);

        return r;
    }

    public static byte[] getXPoint(byte[] dataBytes) {
        if (dataBytes[0] == 04) {
            int len = (dataBytes.length - 1) / 2;
            return Arrays.copyOfRange(dataBytes, 1, 1 + len);
        } else {
            return Arrays.copyOfRange(dataBytes, 1, dataBytes.length);
        }
    }
}
