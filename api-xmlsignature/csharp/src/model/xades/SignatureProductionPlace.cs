using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{


	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Element = XmlElement;
	using Logger = log4net.ILog;

	/// <summary>
	/// <p>In some transactions the purported place where the signer was at the
	/// time of signature creation MAY need to be indicated. In order to provide
	/// this information a new property MAY be included in the signature. This
	/// property specifies an address associated with the signer at a particular
	/// geographical (e.g. city) location.
	/// 
	/// <p>This is a signed property that qualifies the signer.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature. 
	/// 
	/// <p>Below follows the schema definition for this element.
	/// 
	/// 
	/// <pre>
	/// &lt;xsd:element name="SignatureProductionPlace" type="SignatureProductionPlaceType"/>
	///   &lt;xsd:complexType name="SignatureProductionPlaceType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="City" type="xsd:string" minOccurs="0"/>
	///     &lt;xsd:element name="StateOrProvince" type="xsd:string" minOccurs="0"/>
	///     &lt;xsd:element name="PostalCode" type="xsd:string" minOccurs="0"/>
	///     &lt;xsd:element name="CountryName" type="xsd:string" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 3, 2009
	/// </summary>
	public class SignatureProductionPlace : XAdESBaseElement
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(SignatureProductionPlace));

		private readonly string mCity;
		private readonly string mStateOrProvince;
		private readonly string mPostalCode;
		private readonly string mCountryName;

		public SignatureProductionPlace(Context aBaglam, string aCity, string aStateOrProvince, string aPostalCode, string aCountryName) : base(aBaglam)
		{

			addLineBreak();
			if (aCity != null)
			{
				mCity = aCity;
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CITY, aCity);
			}
			if (aStateOrProvince != null)
			{
				mStateOrProvince = aStateOrProvince;
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_STATEORPROVINCE, aStateOrProvince);
			}
			if (aPostalCode != null)
			{
				mPostalCode = aPostalCode;
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_POSTALCODE, aPostalCode);
			}
			if (aCountryName != null)
			{
				mCountryName = aCountryName;
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_COUNTRYNAME, aCountryName);
			}

		}


		/// <summary>
		/// Construct SignatureProductionPlace from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureProductionPlace(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignatureProductionPlace(Element aElement, Context aContext) : base(aElement, aContext)
		{

            mCity = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_CITY);
            mStateOrProvince = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_STATEORPROVINCE);
            mPostalCode = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_POSTALCODE);
            mCountryName = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_COUNTRYNAME);

			logger.Info("Signature ProductionPlace is: " + "[ city: " + mCity + ", state/province: " + mStateOrProvince + ", postal code: " + mPostalCode + ", country: " + mCountryName + "]");

		}

		public virtual string City
		{
			get
			{
				return mCity;
			}
		}

		public virtual string StateOrProvince
		{
			get
			{
				return mStateOrProvince;
			}
		}

		public virtual string PostalCode
		{
			get
			{
				return mPostalCode;
			}
		}

		public virtual string CountryName
		{
			get
			{
				return mCountryName;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_SIGNATUREPRODUCTIONPLACE;
			}
		}
	}

}