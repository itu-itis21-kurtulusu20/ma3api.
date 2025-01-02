using System;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	/// <summary>
	/// @author ahmety
	/// date: Jul 8, 2009
	/// </summary>
	public class UnsupportedOperationException : XMLSignatureException
	{

		public UnsupportedOperationException(string aMessageId) : base(aMessageId)
		{
		}

		public UnsupportedOperationException(Exception aCause, string aMessageId) : base(aCause, aMessageId)
		{
		}

		public UnsupportedOperationException(string aMessageId, params object[] aMessageArgs) : base(aMessageId, aMessageArgs)
		{
		}

		public UnsupportedOperationException(Exception aCause, string aMessageId, params object[] aMessageArgs) : base(aCause, aMessageId, aMessageArgs)
		{
		}
	}

}