namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms
{

	using SignatureAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

	/// <summary>
	/// <p>The expression "RSA algorithm" as used in this specification refers to
	/// the RSASSA-PKCS1-v1_5 algorithm described in RFC 3447  [PKCS1]. The RSA
	/// algorithm takes no explicit parameters.
	/// 
	/// <p>The <code>SignatureValue</code> content for an RSA signature is the base64
	/// [MIME] encoding of the octet string  computed as per
	/// <a href="http://www.ietf.org/rfc/rfc3447.txt">RFC 3447</a> [PKCS1, section
	/// 8.2.1: Signature generation for the RSASSA-PKCS1-v1_5 signature scheme].
	///  Computation of the signature will require concatenation of the hash value
	/// and a constant string determined by RFC 3447. Signature computation and 
	/// validation does not require implementation of an ASN.1 parser.</p>
	/// 
	/// <p>The resulting base64 [MIME] string is the value of the child text node of
	/// the SignatureValue element
	/// 
	/// @author ahmety
	/// date: Aug 26, 2009
	/// </summary>
	public class RSAXmlSignature : BaseXmlSignatureAlgorithm
	{
		public RSAXmlSignature(SignatureAlg aSignatureAlg) : base(aSignatureAlg)
		{
		}

		public class RSAwithMD5 : RSAXmlSignature
		{
			public RSAwithMD5() : base(SignatureAlg.RSA_MD5)
			{
			}
		}
		public class RSAwithSHA1 : RSAXmlSignature
		{
			public RSAwithSHA1() : base(SignatureAlg.RSA_SHA1)
			{
			}
		}
		public class RSAwithSHA256 : RSAXmlSignature
		{
			public RSAwithSHA256() : base(SignatureAlg.RSA_SHA256)
			{
			}
		}
		public class RSAwithSHA384 : RSAXmlSignature
		{
			public RSAwithSHA384() : base(SignatureAlg.RSA_SHA384)
			{
			}
		}

	    public class RSAwithSHA512 : RSAXmlSignature
	    {
	        public RSAwithSHA512() : base(SignatureAlg.RSA_SHA512)
	        {
	        }
	    }

	    public class RSAwithPSS : RSAXmlSignature
	    {
	        public RSAwithPSS() : base(SignatureAlg.RSA_PSS)
	        {
	        }
	    }

}
}