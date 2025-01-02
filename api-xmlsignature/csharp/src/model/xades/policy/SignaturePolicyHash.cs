using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
{

	using DigestAlgAndValue = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DigestAlgAndValue;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Element = XmlElement;

	/// <summary>
	/// @author ahmety
	/// date: Nov 11, 2009
	/// </summary>
	public class SignaturePolicyHash : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DigestAlgAndValue
	{

		public SignaturePolicyHash(Context aContext) : base(aContext)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignaturePolicyHash(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignaturePolicyHash(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_SIGPOLICYHASH;
			}
		}
	}

}