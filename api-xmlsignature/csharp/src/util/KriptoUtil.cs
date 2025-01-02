using System;
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    using DigestAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
	using DigestUtil = tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;


	/// <summary>
	/// @author ahmety
	/// date: May 8, 2009
	/// </summary>
	public static class KriptoUtil
	{
        public static byte[] digest(byte[] aBytes, DigestMethod aDigestMethod)
		{
			return digest(aBytes, aDigestMethod.Algorithm);
		}

		public static byte[] digest(byte[] aBytes, DigestAlg aAlgorithm)
		{
			try
			{
				return DigestUtil.digest(aAlgorithm, aBytes);
			}
			catch (Exception x)
			{
				Console.WriteLine(x.ToString());
				Console.Write(x.StackTrace);
				throw new XMLSignatureException(x, "errors.kripto.hash", aAlgorithm);
			}
		}

		public static byte[] digest(Stream aStream, DigestMethod aDigestMethod)
		{
			return digest(aStream, aDigestMethod.Algorithm);
		}

        public static byte[] digest(Stream aStream, DigestAlg aAlgorithm)
		{
			try
			{
				 return DigestUtil.digestStream(aAlgorithm, aStream);
			}
			catch (Exception x)
			{
				Console.WriteLine(x.ToString());
				Console.Write(x.StackTrace);
				throw new XMLSignatureException(x, "errors.kripto.hash", aAlgorithm);
			}
		}
	}
}