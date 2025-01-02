using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;

	/// <summary>
	/// <p>The <code>UnsignedProperties</code> element MAY contain properties that
	/// qualify XML signature itself or the signer. They are included as content of
	/// the <code>UnsignedSignatureProperties</code> element.
	/// 
	/// <p>The <code>UnsignedProperties</code> element MAY also contain properties
	/// that qualify some of the signed data objects. These properties appear as
	/// content of the <code>UnsignedDataObjectProperties</code> element.
	/// 
	/// <p>The optional <code>Id</code> attribute can be used to make a reference to the
	/// <code>UnsignedProperties</code> element.
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <p><pre>
	/// &lt;complexType name="UnsignedPropertiesType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence>
	///         &lt;element name="UnsignedSignatureProperties" type="{http://uri.etsi.org/01903/v1.3.2#}UnsignedSignaturePropertiesType" minOccurs="0"/>
	///         &lt;element name="UnsignedDataObjectProperties" type="{http://uri.etsi.org/01903/v1.3.2#}UnsignedDataObjectPropertiesType" minOccurs="0"/>
	///       &lt;/sequence>
	///       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 22, 2009
	/// </summary>
	public class UnsignedProperties : XAdESBaseElement
	{

		private readonly UnsignedSignatureProperties mUnsignedSignatureProperties;
		private UnsignedDataObjectProperties mUnsignedDataObjectProperties;


		public UnsignedProperties(Context aContext, XMLSignature aSignature) : base(aContext)
		{

			mUnsignedSignatureProperties = new UnsignedSignatureProperties(aContext, aSignature);

			addLineBreak();
			mElement.AppendChild(mUnsignedSignatureProperties.Element);
			addLineBreak();
		}

		/// <summary>
		/// Construct UnsignedProperties from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
		public UnsignedProperties(Element aElement, Context aContext, XMLSignature aSignature) : base(aElement, aContext)
		{
            Element usp = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_UNSIGNEDSIGNATUREPROPERTIES);
			if (usp != null)
			{
				mUnsignedSignatureProperties = new UnsignedSignatureProperties(usp, mContext, aSignature);
			}
		}

		public virtual UnsignedSignatureProperties UnsignedSignatureProperties
		{
			get
			{
				return mUnsignedSignatureProperties;
			}
		}

		public virtual UnsignedDataObjectProperties UnsignedDataObjectProperties
		{
			get
			{
				return mUnsignedDataObjectProperties;
			}
		}

		public virtual UnsignedDataObjectProperties createOrGetUnsignedDataObjectProperties()
		{
			if (mUnsignedDataObjectProperties == null)
			{
				mUnsignedDataObjectProperties = new UnsignedDataObjectProperties(mContext);
				mElement.AppendChild(mUnsignedDataObjectProperties.Element);
				addLineBreak();
			}
			return mUnsignedDataObjectProperties;
		}


		public override string LocalName
		{
			get
			{
                return Constants.TAGX_UNSIGNEDPROPERTIES;
			}
		}
	}

}