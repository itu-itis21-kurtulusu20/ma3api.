using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <dl><dt>Identifier</dt>
	/// 
	/// <dd><code>Type="<a name="PGPData" id="PGPData" href="http://www.w3.org/2000/09/xmldsig#PGPData">http://www.w3.org/2000/09/xmldsig#PGPData</a></code>"
	/// 
	/// (this can be used within a <code>RetrievalMethod</code> or
	/// <code>Reference</code> element to identify the referent's type)</dd>
	/// </dl>
	/// 
	/// <p>The <code>PGPData</code> element within <code>KeyInfo</code> is used to
	/// convey information related to PGP public key pairs and signatures on such
	/// keys. The <code>PGPKeyID</code>'s value is a base64Binary sequence
	/// containing a standard PGP public key identifier as defined in
	/// [<a href="http://www.ietf.org/rfc/rfc2440.txt">PGP</a>, section 11.2]. The
	/// <code>PGPKeyPacket</code> contains a base64-encoded Key Material Packet as 
	/// defined in [<a href="http://www.ietf.org/rfc/rfc2440.txt">PGP</a>, section
	/// 5.5]. These children element types can be complemented/extended by siblings
	/// from an * external namespace within <code>PGPData</code>, or
	/// <code>PGPData</code> can be replaced all together with an alternative PGP XML
	/// structure as a child of <code>KeyInfo</code>. <code>PGPData</code> must
	/// contain one <code>PGPKeyID</code> and/or one <code>PGPKeyPacket</code> and 0
	/// or more elements from an external namespace.</p>
	/// 
	/// <p>The following schema fragment specifies the expected content contained within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="PGPDataType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;choice>
	///         &lt;sequence>
	///           &lt;element name="PGPKeyID" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
	///           &lt;element name="PGPKeyPacket" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
	///           &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
	///         &lt;/sequence>
	///         &lt;sequence>
	///           &lt;element name="PGPKeyPacket" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
	///           &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
	///         &lt;/sequence>
	///       &lt;/choice>
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
	public class PGPData : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyInfoElement
	{

		/// <summary>
		///  Construct PGPData from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public PGPData(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public PGPData(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_PGPDATA;
			}
		}
	}

}