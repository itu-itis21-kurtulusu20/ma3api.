package tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

/**
 * @author ahmety
 * date: Aug 27, 2009
 */
public class ECDSAXmlSignature extends BaseXmlSignatureAlgorithm
{
    public ECDSAXmlSignature(SignatureAlg aSignatureAlg)
    {
        super(aSignatureAlg);
    }

    public byte[] sign() throws XMLSignatureException
    {
        try {
            byte[] asn1Bytes = mSigner.sign(null);
            return asn1Bytes;
        } catch (CryptoException x){
            throw new XMLSignatureException(x, "errors.sign");
        }
    }

    public boolean verify(byte[] aSignatureValue) throws XMLSignatureException
    {
        try {
            return mVerifier.verifySignature(aSignatureValue);
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.verify");
        }
    }


    /**
     * Converts an ASN.1 ECDSA value to a XML Signature ECDSA Value.
     *
     * The ECDSA Signature algorithm creates ASN.1 encoded (r,s) value
     * pairs; the XML Signature requires the core BigInteger values.
     *
     * @param asn1Bytes to convert
     * @return the decode bytes
     *
     * @throws XMLSignatureException if content is invalid
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     * @see <A HREF="ftp://ftp.rfc-editor.org/in-notes/rfc4050.txt">3.3. ECDSA Signatures</A>
     */
    private byte[] convertASN1toXMLDSIG(byte asn1Bytes[])
            throws XMLSignatureException
    {

       byte rLength = asn1Bytes[3];
       int i;

       for (i = rLength; (i>0) && (asn1Bytes[(4+rLength)-i] == 0); i--);

       byte sLength = asn1Bytes[5 + rLength];
       int j;

       for (j = sLength; (j > 0) && (asn1Bytes[(6+rLength+sLength)-j] == 0); j--);

       int rawLen = ((i+7)/8)*8;

       int tmp = ((j+7)/8)*8;

       if (tmp > rawLen)
           rawLen = tmp;

       if ((asn1Bytes[0] != 48) || (asn1Bytes[1] != asn1Bytes.length-2)
                                || (asn1Bytes[2] != 2)
                                || (rawLen < 24)
                                || (asn1Bytes[4+rLength] != 2)  )
       {
          throw new XMLSignatureException("core.invalid.formatOf", "ASN.1", "ECDSA "+ I18n.translate("signature"));
       }
       byte xmldsigBytes[] = new byte[2*rawLen];

       System.arraycopy(asn1Bytes, (4+rLength)-i, xmldsigBytes, rawLen-i, i);
       System.arraycopy(asn1Bytes, (6+rLength+sLength)-j, xmldsigBytes, 2*rawLen-j, j);

        return xmldsigBytes;
    }

    /**
     * Converts a XML Signature ECDSA Value to an ASN.1 DSA value.
     *
     * The ECDSA Signature algorithm creates ASN.1 encoded (r,s) value
     * pairs; the XML Signature requires the core BigInteger values.
     *
     * @param xmldsigBytes to convert
     * @return the encoded ASN.1 bytes
     *
     * @throws XMLSignatureException if content is invalid
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     * @see <A HREF="ftp://ftp.rfc-editor.org/in-notes/rfc4050.txt">3.3. ECDSA Signatures</A>
     */
    private byte[] convertXMLDSIGtoASN1(byte xmldsigBytes[])
            throws XMLSignatureException
     {

       if (xmldsigBytes.length < 48) {
          throw new XMLSignatureException("core.invalid.formatOf", "XMLDSIG", "ECDSA "+I18n.translate("signature"));
       }

       int rawLen = xmldsigBytes.length/2;

       int i;

       for (i=rawLen; (i>0) && (xmldsigBytes[rawLen-i] == 0); i--);

       int j = i;

       if (xmldsigBytes[rawLen-i] < 0) {
          j += 1;
       }

       int k;

       for (k = rawLen; (k>0) && (xmldsigBytes[2*rawLen-k] == 0); k--);

       int l = k;

       if (xmldsigBytes[2*rawLen-k] < 0) {
          l += 1;
       }

       byte asn1Bytes[] = new byte[6+j+l];

       asn1Bytes[0] = 48;
       asn1Bytes[1] = (byte) (4+j+l);
       asn1Bytes[2] = 2;
       asn1Bytes[3] = (byte) j;

       System.arraycopy(xmldsigBytes, rawLen-i, asn1Bytes, (4+j)-i, i);

       asn1Bytes[4 + j] = 2;
       asn1Bytes[5 + j] = (byte) l;

       System.arraycopy(xmldsigBytes, 2*rawLen-k, asn1Bytes, (6+j+l)-k, k);

       return asn1Bytes;
    }


    public static class ECDSAwithSHA1 extends ECDSAXmlSignature {
        public ECDSAwithSHA1() { super(SignatureAlg.ECDSA_SHA1); }
    }
    public static class ECDSAwithSHA256 extends ECDSAXmlSignature {
        public ECDSAwithSHA256() { super(SignatureAlg.ECDSA_SHA256); }
    }
    public static class ECDSAwithSHA384 extends ECDSAXmlSignature {
        public ECDSAwithSHA384() { super(SignatureAlg.ECDSA_SHA384); }
    }
    public static class ECDSAwithSHA512 extends ECDSAXmlSignature {
        public ECDSAwithSHA512() { super(SignatureAlg.ECDSA_SHA512); }
    }

}
