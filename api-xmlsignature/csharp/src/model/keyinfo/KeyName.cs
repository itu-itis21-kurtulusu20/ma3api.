using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <p>The KeyName element contains a string value (in which white space is
	/// significant) which may be used by the signer to communicate a key identifier
	/// to the recipient. Typically, KeyName contains an identifier related to the
	/// key pair used to sign the message, but it may contain other protocol-related
	/// information that indirectly identifies a key pair. (Common uses of KeyName
	/// include simple string names for keys, a key index, a distinguished name (DN),
	/// an email address, etc.)
	/// 
	/// @author ahmety
	/// date: Jun 10, 2009
	/// </summary>
	public class KeyName : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyInfoElement
	{
		private string mName;


		/// <summary>
		///  Construct KeyName from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public KeyName(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public KeyName(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public virtual string Name
		{
			get
			{
				return mName;
			}
			set
			{
				mName = value;
			}
		}


		// baseelement
		public override string LocalName
		{
			get
			{
				return Constants.TAG_KEYNAME;
			}
		}
	}

}