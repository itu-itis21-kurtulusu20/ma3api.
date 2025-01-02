using System.Collections.Generic;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

	using Element = XmlElement;
	using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


    /// <summary>
    /// <dt>Identifier</dt>
    /// <dd><code>Type="<a name="X509Data" id="X509Data" href="http://www.w3.org/2000/09/xmldsig#SPKIData" shape="rect">http://www.w3.org/2000/09/xmldsig#X509Data</a></code>
    /// " (this can be used within a <code>RetrievalMethod</code> or
    /// <code>Reference</code> element to identify the referent's type)</dd>
    /// </dl>
    /// 
    /// <p>An <code>X509Data</code> element within <code>KeyInfo</code> contains one
    /// or more identifiers of keys or X509 certificates (or certificates'
    /// identifiers or a revocation list). The content of <code>X509Data</code> is:</p>
    /// 
    /// <ol>
    /// <li>At least one element, from the following set of element types; any of
    /// these may appear together or more than once iff (if and only if) each
    /// instance describes or is related to the same certificate:</li>
    /// 
    /// <li style="list-style-type: none; list-style-image: none; list-style-position: outside;">
    ///   <ul>
    ///      <li>The <code>X509IssuerSerial</code> element, which
    ///      contains an X.509 issuer distinguished name/serial number
    ///      pair. The distinguished name SHOULD be represented as a
    ///      string that complies with section 3 of RFC4514 [<a href="#ref-LDAP-DN" shape="rect">LDAP-DN</a>], to be generated
    ///      according to the <a href="#dname-encrules" shape="rect">Distinguished Name Encoding Rules</a> section
    ///      below,</li>
    /// 
    ///      <li>The <code>X509SubjectName</code> element, which
    ///      contains an X.509 subject distinguished name that SHOULD be
    ///      represented as a string that complies with section 3 of
    ///      RFC4514 [<a href="#ref-LDAP-DN" shape="rect">LDAP-DN</a>],
    ///      to be generated according to the <a href="#dname-encrules" shape="rect">Distinguished Name Encoding Rules</a> section
    ///      below,</li>
    /// 
    ///      <li>The <code>X509SKI</code> element, which contains the
    ///      base64 encoded plain (i.e. non-DER-encoded) value of a X509
    ///      V.3 SubjectKeyIdentifier extension.</li>
    /// 
    ///      <li>The <code>X509Certificate</code> element, which
    ///      contains a base64-encoded [<a href="#ref-X509v3" shape="rect">X509v3</a>] certificate, and</li>
    /// 
    ///      <li>Elements from an external namespace which
    ///      accompanies/complements any of the elements above.</li>
    /// 
    ///      <li>The <code>X509CRL</code> element, which contains a
    ///      base64-encoded certificate revocation list (CRL) [<a href="#ref-X509v3" shape="rect">X509v3</a>].</li>
    ///     </ul>
    ///  </li>
    /// </ol>
    /// 
    /// <p>Any <code>X509IssuerSerial</code>, <code>X509SKI</code>, and
    /// <code>X509SubjectName</code> elements that appear MUST refer to the
    /// certificate or certificates containing the validation key. All such elements
    /// that refer to a particular individual certificate MUST be grouped inside a
    /// single <code>X509Data</code> element and if the certificate to which they
    /// refer appears, it MUST also be in that <code>X509Data</code> element.</p>
    /// 
    /// <p>Any <code>X509IssuerSerial</code>, <code>X509SKI</code>, and
    /// <code>X509SubjectName</code> elements that relate to the same key but
    /// different certificates MUST be grouped within a single <code>KeyInfo</code>
    /// but MAY occur in multiple <code>X509Data</code> elements.</p>
    /// 
    /// <p>All certificates appearing in an <code>X509Data</code> element MUST relate
    /// to the validation key by either containing it or being part of a
    /// certification chain that terminates in a certificate containing the
    /// validation key.</p>
    /// 
    /// <p>No ordering is implied by the above constraints. The comments in the
    /// following instance demonstrate these constraints:</p>
    /// <pre class="xml-example" xml:space="preserve">   &lt;KeyInfo&gt;
    ///     &lt;X509Data&gt; &lt;!-- two pointers to certificate-A --&gt;
    /// 
    ///       &lt;X509IssuerSerial&gt;
    ///         &lt;X509IssuerName&gt;<span class="tx">CN=TAMURA Kent, OU=TRL, O=IBM,
    ///           L=Yamato-shi, ST=Kanagawa, C=JP</span>&lt;/X509IssuerName&gt;
    ///         &lt;X509SerialNumber&gt;12345678&lt;/X509SerialNumber&gt;
    ///       &lt;/X509IssuerSerial&gt;
    ///       &lt;X509SKI&gt;31d97bd7&lt;/X509SKI&gt;
    ///     &lt;/X509Data&gt;
    /// 
    ///     &lt;X509Data&gt;&lt;!-- single pointer to certificate-B --&gt;
    ///       &lt;X509SubjectName&gt;Subject of Certificate B&lt;/X509SubjectName&gt;
    ///     &lt;/X509Data&gt;
    /// 
    ///     &lt;X509Data&gt; &lt;!-- certificate chain --&gt;
    ///       &lt;!--Signer cert, issuer CN=arbolCA,OU=FVT,O=IBM,C=US, serial 4--&gt;
    ///       &lt;X509Certificate&gt;MIICXTCCA..&lt;/X509Certificate&gt;
    ///       &lt;!-- Intermediate cert subject CN=arbolCA,OU=FVT,O=IBM,C=US
    ///            issuer CN=tootiseCA,OU=FVT,O=Bridgepoint,C=US --&gt;
    ///       &lt;X509Certificate&gt;MIICPzCCA...&lt;/X509Certificate&gt;
    ///       &lt;!-- Root cert subject CN=tootiseCA,OU=FVT,O=Bridgepoint,C=US --&gt;
    ///       &lt;X509Certificate&gt;MIICSTCCA...&lt;/X509Certificate&gt;
    ///     &lt;/X509Data&gt;
    ///   &lt;/KeyInfo&gt;
    /// </pre>
    /// 
    /// <p>Note, there is no direct provision for a PKCS#7 encoded "bag" of
    /// certificates or CRLs. However, a set of certificates and CRLs can occur
    /// within an <code>X509Data</code> element and multiple <code>X509Data</code>
    /// elements can occur in a <code>KeyInfo</code>. Whenever multiple certificates
    /// occur in an <code>X509Data</code> element, at least one such certificate must
    /// contain the public key which verifies the signature.</p>
    /// 
    /// <h4><a name="dname-encrules" id="dname-encrules" shape="rect">4.4.4.1 Distinguished Name Encoding Rules</a></h4>
    /// 
    /// <p>To encode a distinguished name
    /// (<code>X509IssuerSerial</code>,<code>X509SubjectName</code>, and
    /// <code>KeyName</code> if appropriate), the encoding rules in
    /// section 2 of RFC 4514 [<a href="#ref-LDAP-DN" shape="rect">LDAP-DN</a>] SHOULD be applied, except that the character
    /// escaping rules in section 2.4 of RFC 4514 [<a href="#ref-LDAP-DN" shape="rect">LDAP-DN</a>] MAY be augmented as follows:</p>
    /// 
    /// <ul>
    ///  <li>Escape all occurrences of ASCII control characters (Unicode
    ///  range \x00 - \x1f) by replacing them with "\" followed by a two
    ///  digit hex number showing its Unicode number.</li>
    /// 
    ///  <li>Escape any trailing space characters (Unicode \x20) by
    ///  replacing them with "\20", instead of using the escape sequence "\ ".</li>
    ///  </ul>
    /// 
    /// <p>Since a XML document logically consists of characters, not octets, the
    /// resulting Unicode string is finally encoded according to the character
    /// encoding used for producing the physical representation of the XML document.
    /// </p>
    /// 
    /// <p>The following schema fragment specifies the expected content contained within this class.
    /// 
    /// <pre>
    /// &lt;complexType name="X509DataType">
    ///   &lt;complexContent>
    ///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
    ///       &lt;sequence maxOccurs="unbounded">
    ///         &lt;choice>
    ///           &lt;element name="X509IssuerSerial" type="{http://www.w3.org/2000/09/xmldsig#}X509IssuerSerialType"/>
    ///           &lt;element name="X509SKI" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
    ///           &lt;element name="X509SubjectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
    ///           &lt;element name="X509Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
    ///           &lt;element name="X509CRL" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
    ///           &lt;any processContents='lax' namespace='##other'/>
    ///         &lt;/choice>
    ///       &lt;/sequence>
    ///     &lt;/restriction>
    ///   &lt;/complexContent>
    /// &lt;/complexType>
    /// </pre>
    /// 
    /// 
    /// @author ahmety
    /// date: Jun 10, 2009
    /// </summary>
    public class X509Data : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyInfoElement
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(X509Data));

		private readonly IList<X509DataElement> mContent = new List<X509DataElement>(1);

		public X509Data(Context aBaglam) : base(aBaglam)
		{
			addLineBreak();
		}

		/// <summary>
		///  Construct X509Data from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public X509Data(Element aElement, Context aContext) : base(aElement, aContext)
		{
			IList<Element> children = XmlCommonUtil.selectChildElements(aElement);
			for (int i = 0; i < children.Count; i++)
			{
				Element element = children[i];
				resolve(element, aContext);
			}
		}

		/**
		 * Convert xml element to appropriate x509Data content and add to list of
		 * contents.
		 */
		private void resolve(Element aElement, Context aBaglam)
		{
			string name = aElement.LocalName;

			if (name.Equals(Constants.TAG_X509ISSUERSERIAL))
			{
				mContent.Add(new X509IssuerSerial(aElement, aBaglam));
			}
			else if (name.Equals(Constants.TAG_X509SKI))
			{
				mContent.Add(new X509SKI(aElement, aBaglam));
			}
			else if (name.Equals(Constants.TAG_X509SUBJECTNAME))
			{
				mContent.Add(new X509SubjectName(aElement, aBaglam));
			}
			else if (name.Equals(Constants.TAG_X509CERTIFICATE))
			{
				mContent.Add(new X509Certificate(aElement, aBaglam));
			}
			else if (name.Equals(Constants.TAG_X509CRL))
			{
				mContent.Add(new X509CRL(aElement, aBaglam));
			}
			else
			{
				logger.Warn("Unknown X509Data element: " + aElement);
//JAVA TO C# CONVERTER TODO TASK: Anonymous inner classes are not converted to C# if the base type is not defined in the code being converted:
//				mContent.add(new X509DataElement(aElement, aBaglam)
	//			{
	//				public String getLocalName()
	//				{
	//					return aElement.getLocalName();
	//				}
	//			});
			}
		}

		public virtual int ElementCount
		{
			get
			{
				return mContent.Count;
			}
		}

		public virtual X509DataElement get(int aIndex)
		{
			return mContent[aIndex];
		}

		public virtual void add(X509DataElement aElement)
		{
			mContent.Add(aElement);
			mElement.AppendChild(aElement.Element);
			addLineBreak();
		}

		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
			foreach (X509DataElement element in mContent)
			{
				builder.Append("X509dataElement: ").Append(element);
			}
			return builder.ToString();
		}

		// base element methods
		public override string LocalName
		{
			get
			{
				return Constants.TAG_X509DATA;
			}
		}
	}

}