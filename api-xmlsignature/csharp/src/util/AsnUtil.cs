using System;
using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using UnsupportedOperationException = tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;

	/// <summary>
	/// @author ahmety
	/// date: Oct 8, 2009
	/// </summary>
	public static class AsnUtil
	{
		public static byte[] encode(Asn1Type aObj, PKIEncodingType aEncoding)
		{
			try
			{
				PKIEncodingType encoding = aEncoding == null ? PKIEncodingType.DER : aEncoding;
				Asn1EncodeBuffer encodeBuffer = encoding.createEncodeBuffer();
				if (encodeBuffer is Asn1BerEncodeBuffer)
				{
					Asn1BerEncodeBuffer buffer = (Asn1BerEncodeBuffer)encodeBuffer;
					aObj.Encode(buffer);
					return buffer.MsgCopy;
				}
				else // Asn1XerEncodeBuffer, Asn1XMLDecodeBuffer etc..{
				{
					// todo
					throw new UnsupportedOperationException("Not yet implemented encoding type! " + aEncoding);
				}
			}
			catch (Exception aEx)
			{
				throw new XMLSignatureException(aEx, "errors.cantEncode", "ASN", aObj);
			}

		}

		public static void decode(Asn1Type aObj, byte[] aEncoded, PKIEncodingType aEncoding)
		{
			try
			{
				PKIEncodingType encoding = (aEncoding == null) ? PKIEncodingType.DER : aEncoding;
				Asn1DecodeBuffer decodeBuffer = encoding.createDecodeBuffer(aEncoded);
				if (decodeBuffer is Asn1BerDecodeBuffer)
				{
					Asn1BerDecodeBuffer buffer = (Asn1BerDecodeBuffer)decodeBuffer;
					aObj.Decode(buffer);
				}
				else // Asn1XMLDecodeBuffer etc..{
				{
					// todo
					throw new UnsupportedOperationException("Not yet implemented encoding type! " + aEncoding);
				}
			}
			catch (Exception aEx)
			{
				throw new XMLSignatureException(aEx, "errors.cantDecode", "ASN", aObj);
			}

		}

	}

}