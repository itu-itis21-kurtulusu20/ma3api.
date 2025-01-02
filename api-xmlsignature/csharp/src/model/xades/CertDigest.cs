using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;

	/// <summary>
	/// @author ahmety
	/// date: Nov 11, 2009
	/// </summary>
	public class CertDigest : DigestAlgAndValue
	{

		public CertDigest(Context aContext) : base(aContext)
		{
		}

		public CertDigest(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_CERTDIGEST;
			}
		}
	}

}