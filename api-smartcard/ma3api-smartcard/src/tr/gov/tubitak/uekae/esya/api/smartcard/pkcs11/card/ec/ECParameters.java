/*
 * @(#)ECParameters.java	1.1 06/04/06
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec;

import gnu.crypto.sig.ecdsa.ecmath.field.FieldFp;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerInputStream;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerOutputStream;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ec.ECPointUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParametersSpi;
import java.security.spec.*;

/**
 * This class implements encoding and decoding of Elliptic Curve parameters
 * as specified in RFC 3279.
 *
 * However, only named curves are currently supported.
 *
 * ASN.1 from RFC 3279 follows. Note that X9.62 (2005) has added some additional
 * options.
 *
 * <pre>
 *    EcpkParameters ::= CHOICE {
 *      ecParameters  ECParameters,
 *      namedCurve    OBJECT IDENTIFIER,
 *      implicitlyCA  NULL }
 *
 *    ECParameters ::= SEQUENCE {
 *       version   ECPVer,          -- version is always 1
 *       fieldID   FieldID,         -- identifies the finite field over
 *                                  -- which the curve is defined
 *       curve     Curve,           -- coefficients a and b of the
 *                                  -- elliptic curve
 *       base      ECPoint,         -- specifies the base point P
 *                                  -- on the elliptic curve
 *       order     INTEGER,         -- the order n of the base point
 *       cofactor  INTEGER OPTIONAL -- The integer h = #E(Fq)/n
 *       }
 *
 *    ECPVer ::= INTEGER {ecpVer1(1)}
 *
 *    Curve ::= SEQUENCE {
 *       a         FieldElement,
 *       b         FieldElement,
 *       seed      BIT STRING OPTIONAL }
 *
 *    FieldElement ::= OCTET STRING
 *
 *    ECPoint ::= OCTET STRING
 * </pre>
 *
 * @since   1.6
 * @version 1.1, 04/06/06
 * @author  Andreas Sterbenz
 */
public final class ECParameters extends AlgorithmParametersSpi {
	
	private static final ObjectIdentifier prime_field = 
		ObjectIdentifier.newInternal(new int[] {1, 2, 840, 10045, 1, 1});

	private static final ObjectIdentifier characteristic_two_field =
		ObjectIdentifier.newInternal(new int[] {1, 2, 840, 10045, 1, 2});
	
	private static final ObjectIdentifier tpBasis =
		ObjectIdentifier.newInternal(new int[] {1, 2, 840, 10045, 1, 2, 3, 2});
	
	private static final ObjectIdentifier ppBasis =
		ObjectIdentifier.newInternal(new int[] {1, 2, 840, 10045, 1, 2, 3, 1});

    public ECParameters() 
    {}

    //Used by SunPKCS11 and SunJSSE.    
    public static ECPoint decodePoint(byte[] data, EllipticCurve curve)
	throws IOException 
	{
    	if ((data.length == 0) || (data[0] != 4)) 
    	{
    		throw new IOException("Only uncompressed point format supported");
    	}
    	int n = (curve.getField().getFieldSize() + 7 ) >> 3;
    	if (data.length != (n * 2) + 1) 
    	{
    		throw new IOException("Point does not match field size");
    	}
    	byte[] xb = new byte[n];
    	byte[] yb = new byte[n];
    	System.arraycopy(data, 1, xb, 0, n);
    	System.arraycopy(data, n + 1, yb, 0, n);
    	return new ECPoint(new BigInteger(1, xb), new BigInteger(1, yb));
    }
    
	public static byte[] encodePoint(ECPoint point, EllipticCurve curve, boolean compress) {
		return ECPointUtil.encodePoint(point, curve, compress);
	}

	// Used by SunPKCS11 and SunJSSE.
	/**
	 * Encode {@link ECPoint} in <i>uncompressed</i> mode.
	 *
	 * @param point The point to encode.
	 * @param curve The curve to use in encoding.
	 * @return Encoded point data.
	 */
	public static byte[] encodePoint(ECPoint point, EllipticCurve curve) {
		return encodePoint(point, curve, false);
	}

    // Copied from the SunPKCS11 code - should be moved to a common location.
    // trim leading (most significant) zeroes from the result
    static byte[] trimZeroes(byte[] b) 
    {
		return ECPointUtil.trimZeroes(b);
    }
    
    // Convert the given ECParameterSpec object to a NamedCurve object.
    // If params does not represent a known named curve, return null.
    // Used by SunPKCS11.
    public static NamedCurve getNamedCurve(ECParameterSpec params) 
    {
    	if ((params instanceof NamedCurve) || (params == null)) 
    	{
    		return (NamedCurve)params;
    	}
	
    // This is a hack to allow SunJSSE to work with 3rd party crypto
	// providers for ECC and not just SunPKCS11.
	// This can go away once we decide how to expose curve names in the
	// public API.
	// Note that it assumes that the 3rd party provider encodes named
	// curves using the short form, not explicitly. If it did that, then
	// the SunJSSE TLS ECC extensions are wrong, which could lead to
	// interoperability problems.
    	int fieldSize = params.getCurve().getField().getFieldSize();
    	for (ECParameterSpec namedCurve : NamedCurve.knownECParameterSpecs()) 
    	{
	    // ECParameterSpec does not define equals, so check all the
	    // components ourselves.
	    // Quick field size check first
    		if (namedCurve.getCurve().getField().getFieldSize() != fieldSize) 
    		{
    			continue;
    		}
    		if (namedCurve.getCurve().equals(params.getCurve()) == false) 
    		{
    			continue;
    		}
    		if (namedCurve.getGenerator().equals(params.getGenerator()) == false) 
    		{
    			continue;
    		}
    		if (namedCurve.getOrder().equals(params.getOrder()) == false) 
	   	 	{
    			continue;
	   	 	}
    		if (namedCurve.getCofactor() != params.getCofactor()) 
    		{
    			continue;
    		}
    		// everything matches our named curve, return it
    		return (NamedCurve)namedCurve;
    	}
	// no match found
    	return null;
    }
    
    // Used by SunJSSE.
    public static String getCurveName(ECParameterSpec params) 
    {
    	NamedCurve curve = getNamedCurve(params);
    	return (curve == null) ? null : curve.getObjectIdentifier().toString();
    }
    
    // Used by SunPKCS11.
    public static byte[] encodeParameters(ECParameterSpec params) 
    {
    	NamedCurve curve = getNamedCurve(params);
    	if (curve != null) 
    	{
    		return curve.getEncoded();
    	}
    	
    	return encodeECParameterSpec(params);
	}

    public static byte[] encodeECParameterSpec(ECParameterSpec params)
	{
		try {
			DerOutputStream paramSeq = new DerOutputStream();


			DerOutputStream seq = new DerOutputStream();

			//version
			seq.putInteger(1);


			//fieldID sequence
			DerOutputStream fieldIdSeq = new DerOutputStream();


			//fp field
			ECField field = params.getCurve().getField();
			if( field instanceof ECFieldFp)
			{
				ECFieldFp fieldfp = (ECFieldFp) field;
				fieldIdSeq.putOID(prime_field);
				fieldIdSeq.putInteger(fieldfp.getP());

			}
			else if(field instanceof gnu.crypto.sig.ecdsa.ecmath.field.FieldFp){
				FieldFp fieldFp = (gnu.crypto.sig.ecdsa.ecmath.field.FieldFp)field;
				fieldIdSeq.putOID(prime_field);
				fieldIdSeq.putInteger(fieldFp.getMP());
			}
			else{

				fieldIdSeq.putOID(characteristic_two_field);
				ECFieldF2m field2m = (ECFieldF2m) params.getCurve().getField();

				DerOutputStream twofieldSeq = new DerOutputStream();
				twofieldSeq.putInteger(field2m.getFieldSize());

				int[] terms = field2m.getMidTermsOfReductionPolynomial();
				if(terms.length ==1)
				{
					//trinomial
					twofieldSeq.putOID(tpBasis);
					twofieldSeq.putInteger(terms[0]);
				}
				else if(terms.length==3)
				{
					//pentanomial
					twofieldSeq.putOID(ppBasis);
					DerOutputStream pentaSeq = new DerOutputStream();
					pentaSeq.putInteger(terms[2]);
					pentaSeq.putInteger(terms[1]);
					pentaSeq.putInteger(terms[0]);

					twofieldSeq.write(DerValue.tag_Sequence, pentaSeq);
				}

				fieldIdSeq.write(DerValue.tag_Sequence,twofieldSeq);

			}

			seq.write(DerValue.tag_Sequence,fieldIdSeq);

			EllipticCurve curve = params.getCurve();
			BigInteger a = curve.getA();
			BigInteger b = curve.getB();
			int fieldSize = curve.getField().getFieldSize();

			//curve sequence
			DerOutputStream curveSeq = new DerOutputStream();
			curveSeq.putOctetString(_convert(a, fieldSize));
			curveSeq.putOctetString(_convert(b, fieldSize));

			if(curve.getSeed() !=null )
				curveSeq.putBitString(curve.getSeed());

			seq.write(DerValue.tag_Sequence,curveSeq);

			//base
			byte[] base = encodePoint(params.getGenerator(), curve);
			seq.putOctetString(base);

			//order
			seq.putInteger(params.getOrder());

			//cofactor
			seq.putInteger(params.getCofactor());

			paramSeq.write(DerValue.tag_Sequence,seq);

			byte[] encoded = paramSeq.toByteArray();

			return encoded;
		} catch (IOException ex) {
			throw new ESYARuntimeException("Can not encode ECParams", ex);
		}
    }
    
    private static byte[] _convert(BigInteger aBI,int aFieldSize)
    {
    	int byteL = (aFieldSize+7)/8;
    	
    	byte[] bytes = aBI.toByteArray();
        
        if (byteL < bytes.length)
        {
            byte[] tmp = new byte[byteL];
        
            System.arraycopy(bytes, bytes.length - tmp.length, tmp, 0, tmp.length);
            
            return tmp;
        }
        else if (byteL > bytes.length)
        {
            byte[] tmp = new byte[byteL];
        
            System.arraycopy(bytes, 0, tmp, tmp.length - bytes.length, bytes.length);
            
            return tmp; 
        }
    
        return bytes;
    	
    }
    
    // Used by SunPKCS11.
    public static ECParameterSpec decodeParameters(byte[] params) 
    throws IOException 
    {
    	DerValue encodedParams = new DerValue(params);
    	if (encodedParams.tag == DerValue.tag_ObjectId) 
    	{
    		ObjectIdentifier oid = encodedParams.getOID();
    		ECParameterSpec spec = NamedCurve.getECParameterSpec(oid);
    		if (spec == null) 
    		{
    			throw new IOException("Unknown named curve: " + oid);
    		}
    		return spec;
    	}
    	encodedParams.data.reset();
    	if(encodedParams.tag == DerValue.tag_Sequence)
    	{
    		
		
    		DerInputStream in = encodedParams.data;
		
    		int version = in.getInteger();
    		if (version != 1) 
    		{
    			throw new IOException("Unsupported EC parameters version: " + version);
    		}
		
    		ECField field = parseField(in);
    		EllipticCurve curve = parseCurve(in, field);
    		ECPoint point = parsePoint(in, curve);
		
    		BigInteger order = in.getBigInteger();
    		int cofactor = 0;
		
    		if (in.available() != 0) 
    		{
    			cofactor = in.getInteger();
    		}
		
    		if(encodedParams.data.available() != 0) 
    		{
    			throw new IOException("encoded params have " +encodedParams.data.available() +" extra bytes");
    		}
		
		
    		return new ECParameterSpec(curve, point, order, cofactor);
		
    	}
	
    	throw new IOException("Only named ECParameters supported");
	
    }

    
    private static ECField parseField(DerInputStream in) 
    throws IOException 
    {
    	DerValue v = in.getDerValue();
    	ObjectIdentifier oid = v.data.getOID();
    	if (oid.equals(prime_field)) 
    	{
    		BigInteger fieldSize = v.data.getBigInteger();
    		return new ECFieldFp(fieldSize);
    	}
    	else if(oid.equals(characteristic_two_field))
    	{
    	        DerValue twofield = v.getData().getDerValue();
    	    	int m = twofield.data.getInteger();
    		ObjectIdentifier basis = twofield.data.getOID();
    		if(basis.equals(tpBasis))//trinomial
    		{
    			int k = twofield.data.getInteger();
    			return new ECFieldF2m(m,new int[]{k});
    		}
    		else if(basis.equals(ppBasis))//pentanomial
    		{
    			DerValue pentoField = twofield.getData().getDerValue();
    		    	int k1 = pentoField.data.getInteger();
    			int k2 = pentoField.data.getInteger();
    			int k3 = pentoField.data.getInteger();
    			return new ECFieldF2m(m,new int[]{k1,k2,k3});
    		}
    		else
    			throw new IOException("Not a trinomial or pentanomial type");
    	}
    	else 
    		throw new IOException("Not a prime or characteristic-two field");
	
    }
    
    private static EllipticCurve parseCurve(DerInputStream in, ECField field)
	throws IOException 
	{
    	DerValue v = in.getDerValue();
    	byte[] ab = v.data.getOctetString();
    	byte[] bb = v.data.getOctetString();
    	return new EllipticCurve(field, new BigInteger(1, ab), new BigInteger(1, bb));
    }
    
    private static ECPoint parsePoint(DerInputStream in, EllipticCurve curve)
	throws IOException 
	{
    	byte[] data = in.getOctetString();
    	return decodePoint(data, curve);
    }

    
    // used by ECPublicKeyImpl and ECPrivateKeyImpl
   /* static AlgorithmParameters getAlgorithmParameters(ECParameterSpec spec)
	    throws InvalidKeyException {
	try {
	    AlgorithmParameters params = AlgorithmParameters.getInstance
					("EC", ECKeyFactory.ecInternalProvider);
	    params.init(spec);
	    return params;
	} catch (GeneralSecurityException e) {
	    throw new InvalidKeyException("EC parameters error", e);
	}
    }*/

    // AlgorithmParameterSpi methods

    // The parameters these AlgorithmParameters object represents.
    // Currently, it is always an instance of NamedCurve.
    private ECParameterSpec paramSpec;
    
    protected void engineInit(AlgorithmParameterSpec paramSpec)
	    throws InvalidParameterSpecException {
	if (paramSpec instanceof ECParameterSpec) {
	    this.paramSpec = getNamedCurve((ECParameterSpec)paramSpec);
	    if (this.paramSpec == null) {
		throw new InvalidParameterSpecException
		    ("Not a supported named curve: " + paramSpec);
	    }
	} else if (paramSpec instanceof ECGenParameterSpec) {
	    String name = ((ECGenParameterSpec)paramSpec).getName();
	    ECParameterSpec spec = NamedCurve.getECParameterSpec(name);
	    if (spec == null) {
		throw new InvalidParameterSpecException("Unknown curve: " + name);
	    }
	    this.paramSpec = spec;
	} else if (paramSpec == null) {
	    throw new InvalidParameterSpecException
		("paramSpec must not be null");
	} else {
	    throw new InvalidParameterSpecException
		("Only ECParameterSpec and ECGenParameterSpec supported");
	}
    }

    protected void engineInit(byte[] params) throws IOException {
	paramSpec = decodeParameters(params);
    }

    protected void engineInit(byte[] params, String decodingMethod) throws IOException {
	engineInit(params);
    }

    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> spec)
	    throws InvalidParameterSpecException {
	if (spec.isAssignableFrom(ECParameterSpec.class)) {
	    return (T)paramSpec;
	} else if (spec.isAssignableFrom(ECGenParameterSpec.class)) {
	    return (T)new ECGenParameterSpec(getCurveName(paramSpec));
	} else {
	    throw new InvalidParameterSpecException
		("Only ECParameterSpec and ECGenParameterSpec supported");
	}
    }

    protected byte[] engineGetEncoded() throws IOException {
	return encodeParameters(paramSpec);
    }

    protected byte[] engineGetEncoded(String encodingMethod) throws IOException {
	return engineGetEncoded();
    }

    protected String engineToString() {
	return paramSpec.toString();
    }
}
