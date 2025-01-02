package util;

import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.ObjectIdentifier;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.*;

/**
 <b>Author</b>    : zeldal.ozdemir <br/>
 <b>Project</b>   : MA3   <br/>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br/>
 <b>Date</b>: 9/12/11 - 4:50 PM <p/>
 <b>Description</b>: <br/>
 */
public class CustomCurve extends ECParameterSpec {

    private String name;
    private ObjectIdentifier oid;

    public ObjectIdentifier getOid() {
        return oid;
    }

    /**
     Creates elliptic curve domain parameters based on the
     specified values.

     @param curve the elliptic curve which this parameter
     defines.
     @param g     the generator which is also known as the base point.
     @param n     the order of the generator <code>g</code>.
     @param h     the cofactor.
     @throws NullPointerException     if <code>curve</code>,
     <code>g</code>, or <code>n</code> is null.
     @throws IllegalArgumentException if <code>n</code>
     or <code>h</code> is not positive.
     */
    public CustomCurve(String name, ObjectIdentifier oid, EllipticCurve curve,
                       ECPoint g, BigInteger n, int h) {
        super(curve, g, n, h);
        this.name = name;
        this.oid = oid;
    }

    public static CustomCurve createCustomCurve(String name, String soid, int type, String sfield,
                                                String a, String b, String x, String y, String n, int h) {
        BigInteger p = bi(sfield);
        ECField field;
        if ( type == P ) {
            field = new ECFieldFp(p);
        } else if ( type == B ) {
            field = new ECFieldF2m(p.bitLength() - 1, p);
        } else {
            throw new RuntimeException("Invalid type: " + type);
        }

        EllipticCurve curve = new EllipticCurve(field, bi(a), bi(b));
        ECPoint g = new ECPoint(bi(x), bi(y));

        try {
            ObjectIdentifier oid = new ObjectIdentifier(soid);
            return new CustomCurve(name, oid, curve, g, bi(n), h);

        } catch (IOException e) {
            throw new RuntimeException("Internal error", e);
        }

    }

    public final static int P = 1; // prime curve
    public final static int B = 2; // binary curve

    private static BigInteger bi(String s) {
        return new BigInteger(s, 16);
    }

    public String getName() {
        return name;
    }
}
