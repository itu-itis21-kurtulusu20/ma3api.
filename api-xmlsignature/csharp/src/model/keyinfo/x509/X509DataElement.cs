using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

	/// <summary>
	/// <p>Base Element for X509Data children elements.</p>
	/// 
	/// <p>An <code>X509Data</code> element within <code>KeyInfo</code> contains one
	/// or more identifiers of keys or X509 certificates (or certificates'
	/// identifiers or a revocation list). The content of <code>X509Data</code> is:
	/// </p>
	/// 
	/// <ol><li>At least one element, from the following set of element
	/// types; any of these may appear together or more than once iff
	/// (if and only if) each instance describes or is related to the
	/// same certificate:</li>
	/// 
	/// <li >
	///   <ul>
	///      <li>The <code>X509IssuerSerial</code> element, which
	///      contains an X.509 issuer distinguished name/serial number
	///      pair. The distinguished name SHOULD be represented as a
	///      string that complies with section 3 of RFC4514
	///      , to be generated
	///      according to the "Distinguished Name Encoding Rules" section
	///      below,</li>
	/// 
	///      <li>The <code>X509SubjectName</code> element, which
	///      contains an X.509 subject distinguished name that SHOULD be
	///      represented as a string that complies with section 3 of
	///      RFC4514 [LDAP-DN],
	///      to be generated according to the
	///      "Distinguished Name Encoding Rules" section,</li>
	/// 
	///      <li>The <code>X509SKI</code> element, which contains the
	///      base64 encoded plain (i.e. non-DER-encoded) value of a X509
	///      V.3 SubjectKeyIdentifier extension.</li>
	/// 
	///      <li>The <code>X509Certificate</code> element, which
	///      contains a base64-encoded [X509v3] certificate, and</li>
	/// 
	///      <li>Elements from an external namespace which
	///      accompanies/complements any of the elements above.</li>
	/// 
	///      <li>The <code>X509CRL</code> element, which contains a
	///      base64-encoded certificate revocation list (CRL) [X509v3].</li>
	///    </ul>
	///  </li>
	/// </ol>
	/// </summary>
	/// <seealso cref= X509Certificate </seealso>
	/// <seealso cref= X509CRL </seealso>
	/// <seealso cref= X509IssuerSerial </seealso>
	/// <seealso cref= X509SKI </seealso>
	/// <seealso cref= X509SubjectName </seealso>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data </seealso>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
	/// 
	/// @author ahmety
	/// date: Jun 16, 2009 </seealso>
	public abstract class X509DataElement : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement
	{

		protected internal X509DataElement(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		///  Construct X509DataElement from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: protected X509DataElement(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		protected internal X509DataElement(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		// base element
		public override string Namespace
		{
			get
			{
				return Constants.NS_XMLDSIG;
			}
		}


	}

}