using System;
using System.Collections.Generic;
using System.Xml;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Identifier = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.Identifier;
	using XAdESBaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;



	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// The signature policy is a set of rules for the creation and validation of an
	/// electronic signature, under which the signature can be determined to be
	/// valid. A given legal/contractual context MAY recognize a particular
	/// signature policy as meeting its requirements.
	/// 
	/// <p>The signature policy needs to be available in human readable form so that
	/// it can be assessed to meet the requirements of the legal and contractual
	/// context in which it is being applied. To facilitate the automatic processing
	/// of an electronic signature the parts of the signature policy which specify
	/// the electronic rules for the creation and validation of the electronic
	/// signature also need to be in a computer processable form.
	/// 
	/// <p>If no signature policy is identified then the signature may be assumed to
	/// have been generated/verified without any policy constraints, and hence may
	/// be given no specific legal or contractual significance through the context
	/// of a signature policy.
	/// 
	/// <p>The present document specifies two unambiguous ways for identifying the
	/// signature policy that a signature follows:
	/// <ul>
	/// <li>The electronic signature can contain an explicit and unambiguous
	/// identifier of a signature policy together with a hash value of the signature
	/// policy, so it can be verified that the policy selected by the signer is the
	/// one being used by the verifier. An explicit signature policy has a globally
	/// unique reference, which, in this way, is bound to an electronic signature by
	/// the signer as part of the signature calculation. In these cases, for a given
	/// explicit signature policy there shall be one definitive form that has a
	/// unique binary encoded value. Finally, a signature policy identified in this
	/// way MAY be qualified by additional information.
	/// 
	/// <li>Alternatively, the electronic signature can avoid the inclusion of the
	/// aforementioned identifier and hash value. This will be possible when the
	/// signature policy can be unambiguously derived from the semantics of the type
	/// of data object(s) being signed, and some other information, e.g. national
	/// laws or private contractual agreements, that mention that a given signature
	/// policy MUST be used for this type of data content. In such cases, the
	/// signature will contain a specific empty element indicating that this implied
	/// way to identify the signature policy is used instead the identifier and hash
	/// value.
	/// </ul>
	/// 
	/// <p> The signature policy identifier is a signed property qualifying the
	/// signature.
	/// 
	/// <p> At most one <code>SignaturePolicyIdentifier</code> element MAY be
	/// present in the signature.
	/// 
	/// <p> Below follows the Schema definition for this type.
	/// <pre>
	/// &lt;xsd:element name="SignaturePolicyIdentifier" type="SignaturePolicyIdentifierType"/>
	/// 
	/// &lt;xsd:complexType name="SignaturePolicyIdentifierType">
	///   &lt;xsd:choice>
	///     &lt;xsd:element name="SignaturePolicyId" type="SignaturePolicyIdType"/>
	///     &lt;xsd:element name="SignaturePolicyImplied"/>
	///   &lt;/xsd:choice>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="SignaturePolicyIdType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="SigPolicyId" type="ObjectIdentifierType"/>
	///     &lt;xsd:element ref="ds:Transforms" minOccurs="0"/>
	///     &lt;xsd:element name="SigPolicyHash" type="DigestAlgAndValueType"/>
	///     &lt;xsd:element name="SigPolicyQualifiers" type="SigPolicyQualifiersListType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="SigPolicyQualifiersListType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="SigPolicyQualifier" type="AnyType" maxOccurs="unbounded"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The <code>SignaturePolicyId</code> element will appear when the signature
	/// policy is identified using the first alternative. The
	/// <code>SigPolicyId</code> element contains an identifier that uniquely
	/// identifies a specific version of the signature policy.
	/// 
	/// <p>The <code>SigPolicyHash</code> element contains the identifier of the
	/// hash algorithm and the hash value of the signature policy.
	/// 
	/// <p>The <code>SigPolicyQualifier</code> element can contain additional
	/// information qualifying the signature policy identifier. The optional
	/// <code>ds:Transforms</code> element can contain the transformations performed
	/// on the signature policy document before computing its hash. The processing
	/// model for these transformations is described in [3].
	/// 
	/// <p>Alternatively, the <code>SignaturePolicyImplied</code> element will
	/// appear when the second alternative is used. This empty element indicates
	/// that the data object(s) being signed and other external data imply the
	/// signature policy.
	/// 
	/// 
	/// @author ahmety
	/// date: Oct 15, 2009
	/// </summary>
	public class SignaturePolicyIdentifier : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private readonly SignaturePolicyId mSignaturePolicyId;
		private readonly bool mSignaturePolicyImplied;

		public SignaturePolicyIdentifier(Context aContext, int[] aOID, string aDescription, string aPolicyURI): this(aContext,aOID,aDescription,aPolicyURI,null)
		{
		    
		}

	    public SignaturePolicyIdentifier(Context aContext, int[] aOID, string aDescription, string aPolicyURI, String userNotice) : base(aContext)
	    {
	        PolicyId policyId = new PolicyId(aContext, new Identifier(aContext, aOID), aDescription, null);
	        IList<SignaturePolicyQualifier> qualifiers = null;
	        IList<String> explicitTestList = null;
	        if (aPolicyURI != null)
	        {
	            explicitTestList = new List<String>();
                explicitTestList.Add(userNotice);
                SPUserNotice spUserNotice = new SPUserNotice(aContext, null, explicitTestList);
            
                qualifiers = new List<SignaturePolicyQualifier>();
	            qualifiers.Add(new SignaturePolicyQualifier(mContext, aPolicyURI, spUserNotice));
	      
            }

	        mSignaturePolicyId = new SignaturePolicyId(aContext, policyId, null, null, qualifiers);
	        mElement.AppendChild(mSignaturePolicyId.Element);
	        addLineBreak();
	    }

        public SignaturePolicyIdentifier(Context context, tr.gov.tubitak.uekae.esya.api.signature.attribute.SignaturePolicyIdentifier spi) : base(context)
        {
            Identifier id = new Identifier(context, spi.getPolicyId().getValue());
            PolicyId pid = new PolicyId(context, id, null, null);

            List<SignaturePolicyQualifier> qualifiers = new List<SignaturePolicyQualifier>();
            if (spi.getPolicyURI() != null)
            {
                qualifiers.Add(new SignaturePolicyQualifier(mContext, spi.getPolicyURI(), null));
            }
            if (spi.getUserNotice() != null)
            {
                SPUserNotice notice = new SPUserNotice(context, null, new List<String>(new[]{spi.getUserNotice()}));
                qualifiers.Add(new SignaturePolicyQualifier(mContext, null, notice));
            }

            mSignaturePolicyId = new SignaturePolicyId(context, pid, null, DigestMethod.resolveFromName(spi.getDigestAlg()), spi.getDigestValue(), qualifiers);

            mElement.AppendChild(mSignaturePolicyId.Element);
            addLineBreak();
        }

		/// <summary>
		/// Policy identifier constructor for both implicit or explicit signature
		/// policies. </summary>
		/// <param name="aContext"> where this signature belongs </param>
		/// <param name="aPolicyId"> Either identifier o null for implied signature policy! </param>
		public SignaturePolicyIdentifier(Context aContext, SignaturePolicyId aPolicyId) : base(aContext)
		{
			addLineBreak();

			mSignaturePolicyId = aPolicyId;
			mSignaturePolicyImplied = (aPolicyId == null);

			// add signature policy id
			if (mSignaturePolicyId != null)
			{
				mElement.AppendChild(mSignaturePolicyId.Element);
			}

			// if implicit
			if (mSignaturePolicyImplied)
			{
                insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNATUREPOLICYIMPLIED);
			}

			addLineBreak();
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>

		public SignaturePolicyIdentifier(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element spiElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNATUREPOLICYID);
			if (spiElm != null)
			{
				mSignaturePolicyId = new SignaturePolicyId(spiElm, mContext);
			}

            mSignaturePolicyImplied = (selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNATUREPOLICYIMPLIED) != null);
		}

		public virtual SignaturePolicyId SignaturePolicyId
		{
			get
			{
				return mSignaturePolicyId;
			}
		}

		public virtual bool SignaturePolicyImplied
		{
			get
			{
				return mSignaturePolicyImplied;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_SIGNATUREPOLICYIDENTIFIER;
			}
		}

        public TurkishESigProfile getTurkishESigProfile()
        {
            String oidString = SignaturePolicyId.PolicyId.Identifier.Value;
            int[] OID = OIDUtil.fromURN(oidString);
            return TurkishESigProfile.getSignatureProfileFromOid(OID);
        }
    }

}