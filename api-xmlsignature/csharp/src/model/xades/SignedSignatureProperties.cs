using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using SignaturePolicyIdentifier = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

//	using XMLGregorianCalendar = javax.xml.datatype.XMLGregorianCalendar;

	/// <summary>
	/// <p>This element contains properties that qualify the XML signature that has
	/// been specified with the Target attribute of the
	/// <code>QualifyingProperties</code> container element.
	/// 
	/// <p>
	/// <pre>
	/// &lt;xsd:complexType name="SignedSignaturePropertiesType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="SigningTime" type="xsd:dateTime" minOccurs="0"/>
	///     &lt;xsd:element name="SigningCertificate" type="CertIDListType" minOccurs="0"/>
	///     &lt;xsd:element name="SignaturePolicyIdentifer" type="SignaturePolicyIdentifierType" minOccurs="0"/>
	///     &lt;xsd:element name="SignatureProductionPlace" type="SignatureProductionPlaceType" minOccurs="0"/>
	///     &lt;xsd:element name="SignerRole" type="SignerRoleType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 22, 2009
	/// </summary>
	public class SignedSignatureProperties : XAdESBaseElement
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(SignedSignatureProperties));

		protected internal DateTime ? mSigningTime=null;
		protected internal SigningCertificate mSigningCertificate;
		protected internal SignaturePolicyIdentifier mSignaturePolicyIdentifier;
		protected internal SignatureProductionPlace mSignatureProductionPlace;
		protected internal SignerRole mSignerRole;

		public SignedSignatureProperties(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		///  Construct SignedSignatureProperties from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public SignedSignatureProperties(Element aElement, Context aContext) : base(aElement, aContext)
		{
            Element timeElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNINGTIME);
			if (timeElement != null)
			{
				mSigningTime = XmlCommonUtil.getDate(timeElement);
				logger.Info("Signing time is: " + mSigningTime);
			}
            Element certElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNINGCERTIFICATE);
			if (certElement != null)
			{
				mSigningCertificate = new SigningCertificate(certElement, mContext);
				logger.Debug("Signed signature certificate property exists.");
			}

            Element policyElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNATUREPOLICYIDENTIFIER);
			if (policyElement != null)
			{
				mSignaturePolicyIdentifier = new SignaturePolicyIdentifier(policyElement, mContext);
				logger.Debug("Signature policiy identifier property exists.");
			}

            Element placeElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNATUREPRODUCTIONPLACE);
			if (placeElement != null)
			{
				mSignatureProductionPlace = new SignatureProductionPlace(placeElement, mContext);
			}

            Element roleElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNERROLE);
			if (roleElement != null)
			{
				mSignerRole = new SignerRole(roleElement, mContext);
			}

		}


		public virtual DateTime ? SigningTime
		{
			get
			{
				return mSigningTime;
			}
			set
			{
				mSigningTime = value;
				setupChildren();
			}
		}


		public virtual SigningCertificate SigningCertificate
		{
			get
			{
				return mSigningCertificate;
			}
			set
			{
				mSigningCertificate = value;
				setupChildren();
			}
		}


		public virtual SignaturePolicyIdentifier SignaturePolicyIdentifier
		{
			get
			{
				return mSignaturePolicyIdentifier;
			}
			set
			{
				mSignaturePolicyIdentifier = value;
				setupChildren();
			}
		}


		public virtual SignerRole SignerRole
		{
			get
			{
				return mSignerRole;
			}
			set
			{
				mSignerRole = value;
				setupChildren();
			}
		}


		public virtual SignatureProductionPlace SignatureProductionPlace
		{
			get
			{
				return mSignatureProductionPlace;
			}
			set
			{
				mSignatureProductionPlace = value;
				setupChildren();
			}
		}



		private void setupChildren()
		{
            XmlCommonUtil.removeChildren(mElement);
			addLineBreak();

            if (mSigningTime != null)
			{
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNINGTIME, XmlCommonUtil.datetime2xmlgregstr((DateTime) mSigningTime));
			}

			if (mSigningCertificate != null)
			{                 
				mElement.AppendChild(mSigningCertificate.Element);
				addLineBreak();
			}

			if (mSignaturePolicyIdentifier != null)
			{
				mElement.AppendChild(mSignaturePolicyIdentifier.Element);
				addLineBreak();
			}

			if (mSignatureProductionPlace != null)
			{
				mElement.AppendChild(mSignatureProductionPlace.Element);
				addLineBreak();
			}

			if (mSignerRole != null)
			{
				mElement.AppendChild(mSignerRole.Element);
				addLineBreak();
			}

			if (mId != null)
			{
                if (mElement.HasAttribute(Constants.ATTR_ID))
                {
                    mElement.RemoveAttribute(Constants.ATTR_ID);
                }
				mElement.SetAttribute(Constants.ATTR_ID,null, mId);
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_SIGNEDSIGNATUREPROPERTIES;
			}
		}
	}

}