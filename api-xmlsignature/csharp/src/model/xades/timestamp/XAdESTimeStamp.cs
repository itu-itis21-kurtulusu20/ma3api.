using System;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp
{

	using Logger = log4net.ILog;
	using LE = tr.gov.tubitak.uekae.esya.api.common.license.LE;
    using LV = tr.gov.tubitak.uekae.esya.api.common.license.LV;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Element = XmlElement;


	/// <summary>
	/// <p>This concrete derived type is provided for containing time-stamp tokens
	/// computed on data objects of XAdES signatures.
	/// 
	/// <p>This type provides two mechanisms for identifying data objects that are
	/// covered by the time-stamp token present in the container, and for specifying
	/// how to use them for computing the digest value that is sent to the TSA:
	/// <ul>
	/// <li>Explicit. This mechanism uses the Include element for referencing
	/// specific data objects and for indicating their contribution to the input of
	/// the digest computation.
	/// <li>Implicit. For certain time-stamp container properties under certain
	/// circumstances, applications do not require any additional indication for
	/// knowing that certain data objects are covered by the time-stamp tokens and
	/// how they contribute to the input of the digest computation. The present
	/// document specifies, in the clauses defining such properties (clauses 7.2.9,
	/// 7.2.10, 7.3, 7.5 and 7.7), how applications MUST act in these cases without
	/// explicit indications.
	/// </ul>
	/// 
	/// <pre>
	/// &lt;xsd:complexType name="XAdESTimeStampType">
	///   &lt;xsd:complexContent>
	///     &lt;xsd:restriction base="GenericTimeStampType">
	///       &lt;xsd:sequence>
	///         &lt;xsd:element ref="Include" minOccurs="0" maxOccurs="unbounded"/>
	///         &lt;xsd:element ref="ds:CanonicalizationMethod" minOccurs="0"/>
	///         &lt;xsd:choice maxOccurs="unbounded">
	///           &lt;xsd:element name="EncapsulatedTimeStamp" type="EncapsulatedPKIDataType"/>
	///           &lt;xsd:element name="XMLTimeStamp" type="AnyType"/>
	///         &lt;/xsd:choice>
	///       &lt;/xsd:sequence>
	///       &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	///     &lt;/xsd:restriction>
	///   &lt;/xsd:complexContent>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 28, 2009
	/// </summary>
	public abstract class XAdESTimeStamp : GenericTimeStamp
	{

        protected internal Logger logger = log4net.LogManager.GetLogger(typeof(XAdESTimeStamp));

		protected internal XAdESTimeStamp(Context aContext) : base(aContext)
		{
			checkBeforeUpgrade();
		}

		/// <summary>
		/// Construct GenericTimeStamp from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
		protected internal XAdESTimeStamp(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		private void checkBeforeUpgrade()
		{

			try
			{                
				LV.getInstance().checkLicenceDates(LV.Products.XMLIMZAGELISMIS);
			}
			catch (LE ex)
			{
				logger.Fatal("Lisans kontrolu basarisiz.");
				throw new Exception("Lisans kontrolu basarisiz. " + ex.Message);
			}
		}

	    public abstract TimestampType getType();

		public abstract byte[] getContentForTimeStamp(XMLSignature aSignature);
	}

}