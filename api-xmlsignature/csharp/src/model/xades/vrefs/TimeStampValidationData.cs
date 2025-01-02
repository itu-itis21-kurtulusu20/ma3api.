using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
	using XAdESBaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;


	/// <summary>
	/// This element is specified to serve as an optional  container for validation data 
	/// required for carrying a full verification of time-stamp tokens embedded within any 
	/// of the different time-stamp containers defined in the present document.
	/// 
	///  <p>Below follows the schema definition for this element.
	///  
	/// <pre>
	/// <xsd:element name="TimeStampValidationData" type="ValidationDataType"/> 
	/// 
	/// <xsd:complexType name="ValidationDataType"> 
	/// <xsd:sequence> 
	///  <xsd:element ref="xades:CertificateValues" minOccurs="0" /> 
	///  <xsd:element ref="xades:RevocationValues" minOccurs="0" /> 
	/// </xsd:sequence> 
	/// <xsd:attribute name="Id" type="xsd:ID" use="optional"/> 
	/// <xsd:attribute name="URI" type="xsd:anyURI" use="optional"/> 
	/// </xsd:complexType> 
	/// </pre> 
	/// 
	/// 
	/// @author ayetgin
	/// </summary>
	public class TimeStampValidationData : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{
		private readonly CertificateValues mCertificateValues;
		private readonly RevocationValues mRevocationValues;

		private string mURI;

		public TimeStampValidationData(Context aContext, CertificateValues aCertificateValues, RevocationValues aRevocationValues) : base(aContext)
		{

			string xmlnsXAdESPrefix = mContext.Config.NsPrefixMap.getPrefix(Namespace);
            mElement.SetAttribute("xmlns:" + string.Intern(xmlnsXAdESPrefix), Namespace);

			mCertificateValues = aCertificateValues;
			mRevocationValues = aRevocationValues;
			addLineBreak();
			mElement.AppendChild(aCertificateValues.Element);
			addLineBreak();
			mElement.AppendChild(aRevocationValues.Element);
			addLineBreak();
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public TimeStampValidationData(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public TimeStampValidationData(Element aElement, Context aContext) : base(aElement, aContext)
		{

			mURI = getAttribute(aElement, Constants.ATTR_URI);

			Element certElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CERTIFICATEVALUES);
			Element revElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_REVOCATIONVALUES);

			if (certElement != null)
			{
				mCertificateValues = new CertificateValues(certElement, aContext);
			}
			if (revElement != null)
			{
				mRevocationValues = new RevocationValues(revElement, aContext);
			}

		}

		public virtual CertificateValues CertificateValues
		{
			get
			{
				return mCertificateValues;
			}
		}

		public virtual RevocationValues RevocationValues
		{
			get
			{
				return mRevocationValues;
			}
		}

		public virtual string URI
		{
			set
			{
				mURI = value;
				mElement.SetAttribute(Constants.ATTR_URI,null, value);
			}
			get
			{
				return mURI;
			}
		}


		public override string LocalName
		{
			get
			{
				return Constants.TAGX_TIMESTAMPVALIDATIONDATA;
			}
		}

		public override string Namespace
		{
			get
			{
				return Constants.NS_XADES_1_4_1;
			}
		}
	}

}