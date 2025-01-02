using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{


	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using Element = XmlElement;
	using Logger = log4net.ILog;



	/// <summary>
	/// <p>While the name of the signer is important, the position of the signer
	/// within a company or an organization can be even more important. Some
	/// contracts may only be valid if signed by a user in a particular role, e.g. a
	/// Sales Director. In many cases who the sales Director really is, is not that
	/// important but being sure that the signer is empowered by his company to be
	/// the Sales Director is fundamental.
	/// 
	/// <p>The present document defines two different ways for providing this
	/// feature:
	/// <ul><li> using a claimed role name;
	///     <li> using an attribute certificate containing a certified role.
	/// </ul>
	/// <p>The signer MAY state his own role without any certificate to corroborate
	/// this claim, in which case the claimed role can be added to the signature
	/// as a signed qualifying property.
	/// 
	/// <p>Unlike public key certificates that bind an identifier to a public key,
	/// Attribute Certificates bind the identifier of a certificate to some
	/// attributes of its owner, like a role. The Attribute Authority will be most
	/// of the time under the control of an organization or a company that is best
	/// placed to know which attributes are relevant for which individual. The
	/// Attribute Authority MAY use or point to public key certificates issued by
	/// any CA, provided that the appropriate trust may be placed in that CA.
	/// Attribute Certificates MAY have various periods of validity. That period may
	/// be quite short, e.g. one day. While this requires that a new Attribute
	/// Certificate is obtained every day, valid for that day, this can be
	/// advantageous since revocation of such certificates may not be needed. When
	/// signing, the signer will have to specify which Attribute Certificate it
	/// selects.
	/// 
	/// <p>This is an optional signed property that qualifies the signer. There SHALL
	/// be at most one occurence of this property in the signature.
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;xsd:element name="SignerRole" type="SignerRoleType"/>
	/// 
	/// &lt;xsd:complexType name="SignerRoleType">
	///     &lt;xsd:sequence>
	///       &lt;xsd:element name="ClaimedRoles" type="ClaimedRolesListType" minOccurs="0"/>
	///       &lt;xsd:element name="CertifiedRoles" type="CertifiedRolesListType" minOccurs="0"/>
	///     &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="ClaimedRolesListType">
	///     &lt;xsd:sequence>
	///       &lt;xsd:element name="ClaimedRole" type="AnyType" maxOccurs="unbounded"/>
	///     &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="CertifiedRolesListType">
	///     &lt;xsd:sequence>
	///       &lt;xsd:element name="CertifiedRole" type="EncapsulatedPKIDataType" maxOccurs="unbounded"/>
	///     &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 3, 2009
	/// </summary>
	public class SignerRole : XAdESBaseElement
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(SignerRole));

		private readonly IList<ClaimedRole> mClaimedRoles = new List<ClaimedRole>(0);
		private readonly IList<CertifiedRole> mCertifiedRoles = new List<CertifiedRole>(0);

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignerRole(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, ClaimedRole[] aClaimedRoles) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignerRole(Context aContext, ClaimedRole[] aClaimedRoles) : this(aContext, aClaimedRoles, null)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignerRole(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, CertifiedRole[] aCertifiedRoles) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignerRole(Context aContext, CertifiedRole[] aCertifiedRoles) : this(aContext, null, aCertifiedRoles)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignerRole(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, ClaimedRole[] aClaimedRoles, CertifiedRole[] aCertifiedRoles) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignerRole(Context aContext, ClaimedRole[] aClaimedRoles, CertifiedRole[] aCertifiedRoles) : base(aContext)
		{
			addLineBreak();

			if (aClaimedRoles != null)
			{
                Element claimedRolesElement = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CLAIMEDROLES);
				addLineBreak(claimedRolesElement);

				foreach (ClaimedRole aClaimedRole in aClaimedRoles)
				{
					claimedRolesElement.AppendChild(aClaimedRole.Element);
					addLineBreak(claimedRolesElement);
					mClaimedRoles.Add(aClaimedRole);
				}
			}


			if (aCertifiedRoles != null)
			{
                Element certifiedRolesElement = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CERTIFIEDROLES);
				addLineBreak(certifiedRolesElement);

				foreach (CertifiedRole aCertifiedRole in aCertifiedRoles)
				{
					certifiedRolesElement.AppendChild(aCertifiedRole.Element);
					addLineBreak(certifiedRolesElement);
					mCertifiedRoles.Add(aCertifiedRole);
				}
			}
		}

		/// <summary>
		/// Construct SignerRole from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignerRole(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignerRole(Element aElement, Context aContext) : base(aElement, aContext)
		{

			// claimed roles..
            Element claimedElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CLAIMEDROLES);
			if (claimedElm != null)
			{
                Element[] roleArr = XmlCommonUtil.selectNodes(claimedElm.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_CLAIMEDROLE);
				foreach (Element roleElm in roleArr)
				{
					ClaimedRole role = new ClaimedRole(roleElm, mContext);
					mClaimedRoles.Add(role);
					logger.Info("Found claimed role: " + role);
				}
			}

			// certified roles with attribute certificate
            Element certifiedElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CERTIFIEDROLES);
			if (certifiedElm != null)
			{
                Element[] roleArr = XmlCommonUtil.selectNodes(certifiedElm.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_CERTIFIEDROLE);
				foreach (Element roleElm in roleArr)
				{
					CertifiedRole cr = new CertifiedRole(roleElm, mContext);
					mCertifiedRoles.Add(cr);
                    logger.Info("Found certified role: " + cr);
				}
			}


		}

		public virtual IList<ClaimedRole> ClaimedRoles
		{
			get
			{
				return mClaimedRoles;
			}
		}

		public virtual IList<CertifiedRole> CertifiedRoles
		{
			get
			{
				return mCertifiedRoles;
			}
		}

		//base element
		public override string LocalName
		{
			get
			{
				return Constants.TAGX_SIGNERROLE;
			}
		}
	}

}