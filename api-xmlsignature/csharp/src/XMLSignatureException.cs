using System;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

	/// <summary>
	/// @author ahmety
	/// date: May 8, 2009
	/// </summary>
    public class XMLSignatureException : SignatureException
	{

		public XMLSignatureException(string aMessageId)
            : base(I18n.translate(aMessageId))
		{
		}

		public XMLSignatureException(Exception aCause, string aMessageId)
            : base(I18n.translate(aMessageId), aCause)
		{
		}

		public XMLSignatureException(string aMessageId, params object[] aMessageArgs)
            : base(I18n.translate(aMessageId, aMessageArgs))
		{
		}

        public XMLSignatureException(Exception aCause, string aMessageId, params object[] aMessageArgs)
            : base(I18n.translate(aMessageId, aMessageArgs), aCause)
		{
		}
	}

}