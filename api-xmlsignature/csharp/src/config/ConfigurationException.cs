using System;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{

	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	/// <summary>
	/// @author ahmety
	/// date: Dec 11, 2009
	/// </summary>
	public class ConfigurationException : tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException
	{

		public ConfigurationException(string aMessageId) : base(aMessageId)
		{
		}

		public ConfigurationException(Exception aCause, string aMessageId) : base(I18n.translate(aMessageId), aCause)
		{
		}

		public ConfigurationException(string aMessageId, params object[] aMessageArgs)
            : base(I18n.translate(aMessageId, aMessageArgs))
		{
		}

		public ConfigurationException(Exception aCause, string aMessageId, params object[] aMessageArgs) 
            : base(I18n.translate(aMessageId, aMessageArgs), aCause)
		{
		}
	}

}