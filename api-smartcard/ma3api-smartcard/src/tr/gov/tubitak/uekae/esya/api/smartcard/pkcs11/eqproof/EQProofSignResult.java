package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.eqproof;

import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ec.ECPointUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.ECPoint;

/*
 * Takasbank Biga Projesi için eklendi.
 **/

public class EQProofSignResult {

    private byte[] encodedProof = null;
    private ECPoint a1 = null;
    private ECPoint a2 = null;
    private BigInteger w = null;

    public EQProofSignResult(byte[] encodedProof) {
        this.encodedProof = encodedProof;
    }

    public EQProofSignResult(ECPoint a1, ECPoint a2, BigInteger w) {
        this.a1 = a1;
        this.a2 = a2;
        this.w = w;
    }

    public synchronized ECPoint getA1() throws IOException, ESYAException {
        if (a1 == null) {
            decode();
        }

        return a1;
    }

    public synchronized ECPoint getA2() throws IOException, ESYAException {
        if (a2 == null) {
            decode();
        }

        return a2;
    }

    public synchronized BigInteger getW() throws IOException, ESYAException {
        if (w == null) {
            decode();
        }

        return w;
    }

    public synchronized byte[] getEncoded() throws IOException {
        if (encodedProof == null) {
            byte[] encodedPointA1 = ECPointUtil.encodePointUnsafely(a1);
            byte[] encodedPointA2 = ECPointUtil.encodePointUnsafely(a2);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            DerOutputStream derOutputStream = new DerOutputStream();

            derOutputStream.putOctetString(encodedPointA1);
            derOutputStream.putOctetString(encodedPointA2);
            derOutputStream.putInteger(w);

            derOutputStream.derEncode(baos);
            derOutputStream.reset();
            derOutputStream.putDerValue(new DerValue(DerValue.tag_Sequence, baos.toByteArray()));
            baos.reset();
            derOutputStream.derEncode(baos);

            encodedProof = baos.toByteArray();
        }

        return encodedProof;
    }

    private synchronized void decode() throws IOException, ESYAException {
        DerInputStream derInputStream = new DerInputStream(encodedProof);

        // extract DerSequence first
        DerValue[] values = derInputStream.getSequence(0);

        // then get the other values
        byte[] encodedPointA1 = values[0].getOctetString();
        a1 = ECPointUtil.getECPoint(encodedPointA1);

        byte[] encodedPointA2 = values[1].getOctetString();
        a2 = ECPointUtil.getECPoint(encodedPointA2);

        w = values[2].getBigInteger();
    }
}
