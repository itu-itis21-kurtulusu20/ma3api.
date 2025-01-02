using System;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	/// <summary>
	/// @author ahmety
	/// date: May 29, 2009
	/// </summary>
	public class UnknownAlgorithmException : XMLSignatureRuntimeException
	{

		public UnknownAlgorithmException(Exception aCause, string aMessageId, params object[] aParams) : base(aCause, aMessageId, aParams)
		{
		}
	}

}