using System;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms
{

	using SignatureAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
	using CryptoException = tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;


	/// <summary>
	/// @author ahmety
	/// date: Aug 27, 2009
	/// </summary>
	public class ECDSAXmlSignature : BaseXmlSignatureAlgorithm
	{
		public ECDSAXmlSignature(SignatureAlg aSignatureAlg) : base(aSignatureAlg)
		{
		}

		public override byte[] sign()
		{
			try
			{
				byte[] asn1Bytes = mSigner.sign(null);
				return asn1Bytes;
			}
			catch (CryptoException x)
			{
				throw new XMLSignatureException(x, "errors.sign");
			}
		}

		public override bool verify(byte[] aSignatureValue)
		{
			try
			{
				return mVerifier.verifySignature(aSignatureValue);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.verify");
			}
		}


		/// <summary>
		/// Converts an ASN.1 ECDSA value to a XML Signature ECDSA Value.
		/// 
		/// The ECDSA Signature algorithm creates ASN.1 encoded (r,s) value
		/// pairs; the XML Signature requires the core BigInteger values.
		/// </summary>
		/// <param name="asn1Bytes"> to convert </param>
		/// <returns> the decode bytes
		/// </returns>
		/// <exception cref="XMLSignatureException"> if content is invalid </exception>
		/// <seealso cref= <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A> </seealso>
		/// <seealso cref= <A HREF="ftp://ftp.rfc-editor.org/in-notes/rfc4050.txt">3.3. ECDSA Signatures</A> </seealso>

		private byte[] convertASN1toXMLDSIG(byte[] asn1Bytes)
		{

		   byte rLength = asn1Bytes[3];
		   int i;

		   for (i = rLength; (i > 0) && (asn1Bytes[(4 + rLength) - i] == 0); i--)
		   {
			   ;
		   }

		   byte sLength = asn1Bytes[5 + rLength];
		   int j;

		   for (j = sLength; (j > 0) && (asn1Bytes[(6 + rLength + sLength) - j] == 0); j--)
		   {
			   ;
		   }

		   int rawLen = ((i + 7) / 8) * 8;

		   int tmp = ((j + 7) / 8) * 8;

		   if (tmp > rawLen)
		   {
			   rawLen = tmp;
		   }

		   if ((asn1Bytes[0] != 48) || (asn1Bytes[1] != asn1Bytes.Length - 2) || (asn1Bytes[2] != 2) || (rawLen < 24) || (asn1Bytes[4 + rLength] != 2))
		   {
			  throw new XMLSignatureException("core.invalid.formatOf", "ASN.1", "ECDSA " + I18n.translate("signature"));
		   }
		   byte[] xmldsigBytes = new byte[2 * rawLen];

		   Array.Copy(asn1Bytes, (4 + rLength) - i, xmldsigBytes, rawLen - i, i);
		   Array.Copy(asn1Bytes, (6 + rLength + sLength) - j, xmldsigBytes, 2 * rawLen - j, j);

			return xmldsigBytes;
		}

		/// <summary>
		/// Converts a XML Signature ECDSA Value to an ASN.1 DSA value.
		/// 
		/// The ECDSA Signature algorithm creates ASN.1 encoded (r,s) value
		/// pairs; the XML Signature requires the core BigInteger values.
		/// </summary>
		/// <param name="xmldsigBytes"> to convert </param>
		/// <returns> the encoded ASN.1 bytes
		/// </returns>
		/// <exception cref="XMLSignatureException"> if content is invalid </exception>
		/// <seealso cref= <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A> </seealso>
		/// <seealso cref= <A HREF="ftp://ftp.rfc-editor.org/in-notes/rfc4050.txt">3.3. ECDSA Signatures</A> </seealso>

		private byte[] convertXMLDSIGtoASN1(byte[] xmldsigBytes)
		{

		   if (xmldsigBytes.Length < 48)
		   {
			  throw new XMLSignatureException("core.invalid.formatOf", "XMLDSIG", "ECDSA " + I18n.translate("signature"));
		   }

		   int rawLen = xmldsigBytes.Length / 2;

		   int i;

		   for (i = rawLen; (i > 0) && (xmldsigBytes[rawLen - i] == 0); i--)
		   {
			   ;
		   }

		   int j = i;

		   if (xmldsigBytes[rawLen - i] < 0)
		   {
			  j += 1;
		   }

		   int k;

		   for (k = rawLen; (k > 0) && (xmldsigBytes[2 * rawLen - k] == 0); k--)
		   {
			   ;
		   }

		   int l = k;

		   if (xmldsigBytes[2 * rawLen - k] < 0)
		   {
			  l += 1;
		   }

		   byte[] asn1Bytes = new byte[6 + j + l];

		   asn1Bytes[0] = 48;
		   asn1Bytes[1] = (byte)(4 + j + l);
		   asn1Bytes[2] = 2;
		   asn1Bytes[3] = (byte) j;

		   Array.Copy(xmldsigBytes, rawLen - i, asn1Bytes, (4 + j) - i, i);

		   asn1Bytes[4 + j] = 2;
		   asn1Bytes[5 + j] = (byte) l;

		   Array.Copy(xmldsigBytes, 2 * rawLen - k, asn1Bytes, (6 + j + l) - k, k);

		   return asn1Bytes;
		}


		public class ECDSAwithSHA1 : ECDSAXmlSignature
		{
			public ECDSAwithSHA1() : base(SignatureAlg.ECDSA_SHA1)
			{
			}
		}
		public class ECDSAwithSHA256 : ECDSAXmlSignature
		{
			public ECDSAwithSHA256() : base(SignatureAlg.ECDSA_SHA256)
			{
			}
		}
		public class ECDSAwithSHA384 : ECDSAXmlSignature
		{
			public ECDSAwithSHA384() : base(SignatureAlg.ECDSA_SHA384)
			{
			}
		}
		public class ECDSAwithSHA512 : ECDSAXmlSignature
		{
			public ECDSAwithSHA512() : base(SignatureAlg.ECDSA_SHA512)
			{
			}
		}

	}

}