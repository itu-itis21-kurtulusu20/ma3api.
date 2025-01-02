using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <dl>
	///   <dt>Identifier</dt>
	///   <dd><code>Type="<a name="SPKIData" id="SPKIData" href="http://www.w3.org/2000/09/xmldsig#SPKIData">http://www.w3.org/2000/09/xmldsig#SPKIData</a>
	///   </code>"
	/// (this can be used within a <code>RetrievalMethod</code> or
	/// <code>Reference</code> element to identify the referent's type)</dd>
	/// </dl>
	/// 
	/// <p>The <code>SPKIData</code> element within <code>KeyInfo</code> is used to
	/// convey information related to SPKI public key pairs, certificates and other
	/// SPKI data. <code>SPKISexp</code> is the base64 encoding of a SPKI canonical
	/// S-expression. <code>SPKIData</code> must have at least one
	/// <code>SPKISexp</code>; <code>SPKISexp</code> can be complemented/extended by
	/// siblings from an external namespace within <code>SPKIData</code>, or
	/// <code>SPKIData</code> can be entirely replaced with an alternative SPKI XML
	/// structure as a child of <code>KeyInfo</code>.</p>
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="SPKIDataType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence maxOccurs="unbounded">
	///         &lt;element name="SPKISexp" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
	///         &lt;any processContents='lax' namespace='##other' minOccurs="0"/>
	///       &lt;/sequence>
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// 
	/// @author ahmety
	/// date: Jun 16, 2009
	/// 
	/// todo implement this class
	/// </summary>
	public class SPKIData : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyInfoElement
	{

		/// <summary>
		///  Construct SPKIData from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SPKIData(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SPKIData(Element aElement, Context aContext) : base(aElement, aContext)
		{
			throw new Exception("Not yet implemented! " + LocalName);
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_SPKIDATA;
			}
		}
	}

}