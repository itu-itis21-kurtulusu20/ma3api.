using System;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	using SignatureRuntimeException = tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
    //using SignatureRuntimeException = SystemException;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class XMLSignatureRuntimeException : SignatureRuntimeException
	{

		public XMLSignatureRuntimeException(string message)
            : base(I18n.translate(message))
		{
		}

		public XMLSignatureRuntimeException(Exception aCause, string aMessage)
            : base(aCause, I18n.translate(aMessage))
		{
		}


		public XMLSignatureRuntimeException(string aMessage, params object[] aMessageArgs)
            : base(I18n.translate(aMessage, aMessageArgs))
		{
		}

		public XMLSignatureRuntimeException(Exception aCause, string aMessage, params object[] aMessageArgs)
            : base(aCause, I18n.translate(aMessage, aMessageArgs))
		{
		}

	}

}