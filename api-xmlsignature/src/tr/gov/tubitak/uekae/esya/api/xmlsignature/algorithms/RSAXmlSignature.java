package tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

/**
 * <p>The expression "RSA algorithm" as used in this specification refers to
 * the RSASSA-PKCS1-v1_5 algorithm described in RFC 3447  [PKCS1]. The RSA
 * algorithm takes no explicit parameters.
 *
 * <p>The <code>SignatureValue</code> content for an RSA signature is the base64
 * [MIME] encoding of the octet string  computed as per
 * <a href="http://www.ietf.org/rfc/rfc3447.txt">RFC 3447</a> [PKCS1, section
 * 8.2.1: Signature generation for the RSASSA-PKCS1-v1_5 signature scheme].
 *  Computation of the signature will require concatenation of the hash value
 * and a constant string determined by RFC 3447. Signature computation and 
 * validation does not require implementation of an ASN.1 parser.</p>
 *
 * <p>The resulting base64 [MIME] string is the value of the child text node of
 * the SignatureValue element
 * 
 * @author ahmety
 * date: Aug 26, 2009
 */
public class RSAXmlSignature extends BaseXmlSignatureAlgorithm
{
    public RSAXmlSignature(SignatureAlg aSignatureAlg)
    {
        super(aSignatureAlg);
    }

    public static class RSAwithMD5 extends RSAXmlSignature {
        public RSAwithMD5() { super(SignatureAlg.RSA_MD5); }
    }
    public static class RSAwithSHA1 extends RSAXmlSignature {
        public RSAwithSHA1() { super(SignatureAlg.RSA_SHA1); }
    }
    public static class RSAwithSHA256 extends RSAXmlSignature {
        public RSAwithSHA256() { super(SignatureAlg.RSA_SHA256); }
    }
    public static class RSAwithSHA384 extends RSAXmlSignature {
        public RSAwithSHA384() { super(SignatureAlg.RSA_SHA384); }
    }
    public static class RSAwithSHA512 extends RSAXmlSignature {
        public RSAwithSHA512() { super(SignatureAlg.RSA_SHA512); }
    }

    public static class RSAwithPSS extends RSAXmlSignature {
        public RSAwithPSS() { super(SignatureAlg.RSA_PSS); }
    }
}
