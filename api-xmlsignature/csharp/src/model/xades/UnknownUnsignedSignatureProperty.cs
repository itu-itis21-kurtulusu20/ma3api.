using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class UnknownUnsignedSignatureProperty : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, UnsignedSignaturePropertyElement
	{

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public UnknownUnsignedSignatureProperty(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public UnknownUnsignedSignatureProperty(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return mElement.LocalName;
			}
		}

		public override string Namespace
		{
			get
			{
				return mElement.NamespaceURI;
			}
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: protected void checkNamespace() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		protected internal override void checkNamespace()
		{
			// no op
		}
	}

}