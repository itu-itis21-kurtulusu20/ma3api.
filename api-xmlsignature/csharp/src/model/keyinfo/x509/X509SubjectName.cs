using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using LDAPDNUtil = tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <code>X509Data</code> child element, which contains an X.509 subject
	/// distinguished name that SHOULD be represented as a string that complies with
	/// section 3 of RFC4514 [LDAP-DN], to be generated according to the
	/// "Distinguished Name Encoding Rules" section</p>
	/// </summary>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data </seealso>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
	/// @author ahmety
	/// date: Jun 16, 2009 </seealso>
	public class X509SubjectName : X509DataElement
	{
		private readonly string mSubjectName;

		public X509SubjectName(Context aContext, string aSubjectName) : base(aContext)
		{
			mSubjectName = aSubjectName;
			mElement.InnerText = LDAPDNUtil.normalize(aSubjectName);
		}

		/// <summary>
		///  Construct KeyInfo from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public X509SubjectName(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public X509SubjectName(Element aElement, Context aContext) : base(aElement, aContext)
		{
			mSubjectName = XmlCommonUtil.getText(aElement);
		}

		public virtual string SubjectName
		{
			get
			{
				return mSubjectName;
			}
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_X509SUBJECTNAME;
			}
		}

	}

}