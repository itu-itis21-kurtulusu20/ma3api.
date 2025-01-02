package tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.io.IOException;

/**
 * <p>The DSA family of algorithms is defined in FIPS 186-3 [DSS].  FIPS 186-3
 * defines DSA in terms of two security parameters L and N where
 * L = |p|, N = |q|, p is the prime modulus, q is a prime divisor of (p-1).
 * FIPS 186-3 defines four valid pairs of (L, N); they are: (1024, 160),
 * (2048, 224), (2048, 256) and (3072, 256).  The pair (1024, 160) corresponds
 * to the algorithm DSAwithSHA1, which is identified in this specification by
 * the URI http://www.w3.org/2000/09/xmldsig#dsa-sha1.  The pairs (2048, 256)
 * and (3072, 256) correspond to the algorithm DSAwithSHA256, which is
 * identified in this specification by the URI
 * http://www.w3.org/2009/xmldsig11#dsa-sha256.  This specification does not use
 * the (2048, 224) instance of DSA (which corresponds to DSAwithSHA224).
 *
 * <p>The output of the DSA algorithm consists of a pair of integers usually
 * referred by the pair (r, s). The signature value consists of the base64
 * encoding of the concatenation of two octet-streams that respectively result
 * from the octet-encoding of the values r and s in that order. Integer to
 * octet-stream conversion must be done according to the I2OSP operation defined
 * in the RFC 3447  [PKCS1] specification with a l parameter equal to 20.
 *
 * <p> DSA takes no explicit parameters.
 *
 * <h2>Security considerations regarding DSA key sizes</h2>
 *
 * <p>Per FIPS 186-3 [DSS], the DSA security parameter L is defined to be 1024,
 * 2048 or 3072 bits and the corresponding DSA q value is defined to be 160,
 * 224/256 and 256 bits respectively. Special Publication SP 800-57 Part 1
 * [SP800-57], NIST recommends using at least at 2048-bit public keys for
 * securing information beyond 2010 (and 3072-bit keys for securing information
 * beyond 2030).
 *
 * <p>Since XML Signature 1.0 requires implementations to support DSA-based
 * digital signatures, this XML Signature 1.1 revision REQUIRES signature
 * verifiers to implement DSA only for keys of 1024 bits in order to guarantee
 * interoperability with XML Signature 1.0 generators. XML Signature 1.1
 * implementations MAY but are NOT REQUIRED to support DSA-based signature
 * generation, and given the short key size and the SP800-57 guidelines,
 * DSA with 1024-bit prime moduli SHOULD NOT be used for signatures that will be
 * verified beyond 2010.
 * 
 * @author ahmety
 * date: Aug 26, 2009
 */
public class DSAXmlSignature extends BaseXmlSignatureAlgorithm
{
    public DSAXmlSignature(SignatureAlg aSignatureAlg)
    {
        super(aSignatureAlg);
    }

    public byte[] sign() throws XMLSignatureException
    {
        byte[] asn1Bytes = super.sign();
        return convertASN1toXMLDSIG(asn1Bytes);
    }

    public boolean verify(byte[] aSignatureValue) throws XMLSignatureException
    {
        return super.verify(convertXMLDSIGtoASN1(aSignatureValue));
    }

    /**
     * Converts an ASN.1 DSA value to a XML Signature DSA Value.
     * <p/>
     * XML Signature requires the core BigInteger values.
     * @param asn1Bytes
     * @return the decode bytes
     * @throws java.io.IOException
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     *
    private byte[] convertASN1toXMLDSIG(byte[] asn1Bytes)
            throws XMLSignatureException
    {
        if (asn1Bytes[0] == (byte) 0x30)//SEQUENCE
            throw new XMLSignatureException("Invalid DSA ASN format");
        //icerigi alalim
        int[] sinir = TLV.getIcerik(asn1Bytes, 0);
        if (sinir[1] + 1 != asn1Bytes.length)
            return null;
        //icerikte ardarda iki int olmali
        if (asn1Bytes[sinir[0]] != 0x02) //INTEGER
            return null;
        int[] sinir_r = TLV.getIcerik(asn1Bytes, sinir[0]);
        if (asn1Bytes[sinir_r[1] + 1] != 0x02) //INTEGER
            return null;
        int[] sinir_s = TLV.getIcerik(asn1Bytes, sinir_r[1] + 1);

        byte[] r_array = new byte[sinir_r[1]-sinir_r[0]+1];
             byte[] s_array = new byte[sinir_s[1]-sinir_s[0]+1];

             System.arraycopy(asn1Bytes,sinir_r[0],r_array,0,r_array.length);
             System.arraycopy(asn1Bytes,sinir_s[0],s_array,0,s_array.length);

             BigInteger r = new BigInteger(r_array);
             BigInteger s = new BigInteger(s_array);



        int len_r = (sinir_r[1] - sinir_r[0] + 1);
        int len_s = (sinir_s[1] - sinir_s[0] + 1);

        byte[] result = new byte[len_r + len_s];
        System.arraycopy(asn1Bytes,sinir_r[0],result,0,len_r);
        System.arraycopy(asn1Bytes,sinir_s[0],result,len_r,len_s);

        return result;
    }  */

    /**
     * Converts a XML Signature DSA Value to an ASN.1 DSA value.
     * <p/>
     * the XML Signature requires the core BigInteger values.
     * @param xmldsigBytes
     * @return the encoded ASN.1 bytes
     * @throws IOException
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     *
    private byte[] convertXMLDSIGtoASN1(byte[] xmldsigBytes)
    {
        int qLen = mPublicKey.getParams().getQ().bitLength() / 8;
        ByteBuffer buffer = ByteBuffer.wrap(xmldsigBytes);
        byte[] r = new byte[qLen];
        buffer.get(r);
        byte[] s = new byte[qLen];
        buffer.get(s);

        //return new BigInteger[]{new BigInteger(1, r), new BigInteger(1, s)}

        byte[] a = TLV.makeTLV((byte) 0x02, r);
        byte[] b = TLV.makeTLV((byte) 0x02, s);
        byte[] x = new byte[a.length + b.length];
        System.arraycopy(a, 0, x, 0, a.length);
        System.arraycopy(b, 0, x, a.length, b.length);
        return TLV.makeTLV((byte) 0x30, x);
    }  */


    /**
     * Converts an ASN.1 DSA value to a XML Signature DSA Value.
     *
     * DSA Signature algorithm creates ASN.1 encoded (r,s) value
     * pairs; the XML Signature requires the core BigInteger values.
     *
     * @param asn1Bytes to convert XMLDSİg version
     * @return the decode bytes
     *
     * @throws XMLSignatureException if invalid format is found
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     */
    private byte[] convertASN1toXMLDSIG(byte[] asn1Bytes)
           throws XMLSignatureException
    {
        byte rLength = asn1Bytes[3];
        int i;

        for (i=rLength; (i>0) && (asn1Bytes[(4+rLength)-i]==0); i--);

        byte sLength = asn1Bytes[5 + rLength];
        int j;

        for (j=sLength; (j>0) && (asn1Bytes[(6+rLength+sLength)-j]==0); j--);

        if ((asn1Bytes[0]!=48) || (asn1Bytes[1]!=asn1Bytes.length-2)
                               || (asn1Bytes[2]!=2)
                               || (i>20)
                               || (asn1Bytes[4+rLength]!=2)
                               || (j>20) )
        {
            throw new XMLSignatureException("core.invalid.formatOf", "ASN.1", "DSA "+ I18n.translate("signature"));
        }
        byte xmldsigBytes[] = new byte[40];

        System.arraycopy(asn1Bytes, (4+rLength)-i, xmldsigBytes, 20-i, i);
        System.arraycopy(asn1Bytes, (6+rLength+sLength)-j, xmldsigBytes, 40-j, j);

        return xmldsigBytes;
    }

    /**
     * Converts a XML Signature DSA Value to an ASN.1 DSA value.
     *
     * The JAVA JCE DSA Signature algorithm creates ASN.1 encoded (r,s) value
     * pairs; the XML Signature requires the core BigInteger values.
     *
     * @param xmldsigBytes
     * @return the encoded ASN.1 bytes
     *
     * @throws IOException
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     */
    private static byte[] convertXMLDSIGtoASN1(byte xmldsigBytes[])
           throws XMLSignatureException
    {

        if (xmldsigBytes.length != 40) {
            throw new XMLSignatureException("core.invalid.formatOf","XMLDSIG", "DSA signature");
        }

        int i;

        for (i=20; (i>0) && (xmldsigBytes[20-i]==0); i--);

        int j = i;

        if (xmldsigBytes[20 - i] < 0) {
         j += 1;
        }

        int k;

        for (k=20; (k>0) && (xmldsigBytes[40-k]==0); k--);

        int l = k;

        if (xmldsigBytes[40-k]<0) {
            l += 1;
        }

        byte asn1Bytes[] = new byte[6+j+l];

        asn1Bytes[0] = 48;
        asn1Bytes[1] = (byte) (4+j+l);
        asn1Bytes[2] = 2;
        asn1Bytes[3] = (byte) j;

        System.arraycopy(xmldsigBytes, 20-i, asn1Bytes, (4+j)-i, i);

        asn1Bytes[4 + j] = 2;
        asn1Bytes[5 + j] = (byte) l;

        System.arraycopy(xmldsigBytes, 40-k, asn1Bytes, (6+j+l)-k, k);

        return asn1Bytes;
    }    

    public static class DSAWithSHA1 extends DSAXmlSignature {
        public DSAWithSHA1() { super(SignatureAlg.DSA_SHA1); }
    }

    // todo add SHA-256 version

}
