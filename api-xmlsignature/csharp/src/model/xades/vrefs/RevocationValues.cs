using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Logger = log4net.ILog;
	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Any = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;


	/// <summary>
	/// The <code>RevocationValues</code> property element is used to hold the values 
	/// of the revocation information which are to be shipped with the electronic 
	/// signature. If <code>CompleteRevocationRefs</code> and 
	/// <code>RevocationValues</code> are present, all the revocation data referenced 
	/// in <code>RevocationRefs</code> MUST be present either in the 
	/// <code>ds:KeyInfo</code> element of the signature or in the 
	/// <code>RevocationValues</code> property element.
	/// 
	/// <p>This is an optional unsigned property that qualifies the signature.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature.
	/// 
	/// <p>Below follows the Schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="RevocationValues" type="RevocationValuesType"/>
	/// 
	/// &lt;xsd:complexType name="RevocationValuesType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="CRLValues" type="CRLValuesType" minOccurs="0"/>
	///     &lt;xsd:element name="OCSPValues" type="OCSPValuesType" minOccurs="0"/>
	///     &lt;xsd:element name="OtherValues" type="OtherCertStatusValuesType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>Revocation information can include Certificate Revocation Lists 
	/// (CRLValues) or responses from an online certificate status server 
	/// (OCSPValues). Additionally a placeholder for other revocation information 
	/// (OtherValues) is provided for future use.
	/// 
	/// <p><font color="red">Note: Curretly other values is not supported..</p>
	/// 
	/// @author ahmety
	/// date: Dec 17, 2009
	/// </summary>
	public class RevocationValues : RevocationValuesType, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(RevocationValues));

		public RevocationValues(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public RevocationValues(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public RevocationValues(Element aElement, Context aContext) : base(aElement, aContext)
		{

			if (logger.IsDebugEnabled)
			{
				logger.Debug("REVOCATION VALUES");
				for (int i = 0; i < CRLValueCount; i++)
				{
					logger.Debug("" + getCRL(i));
				}
				for (int i = 0; i < OCSPValueCount; i++)
				{
					logger.Debug("" + getOCSPResponse(i));
				}
			}
		}


		public override string LocalName
		{
			get
			{
				return Constants.TAGX_REVOCATIONVALUES;
			}
		}
	}

}