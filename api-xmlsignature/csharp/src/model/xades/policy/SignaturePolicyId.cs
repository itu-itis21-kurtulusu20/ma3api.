using System;
using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
{
    using Transforms = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
    using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using Resolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	using Element = XmlElement;

	/// <summary>
	/// The <code>SignaturePolicyId</code> element will appear when the signature
	/// policy is identified using explicit mechanism. The <code>SigPolicyId</code>
	/// element contains an identifier that uniquely identifies a specific version
	/// of the signature policy.
	/// 
	/// <p>The <code>SigPolicyHash</code> element contains the identifier of the
	/// hash algorithm and the hash value of the signature policy.
	/// 
	/// <p>The <code>SigPolicyQualifier</code> element can contain additional
	/// information qualifying the signature policy identifier.
	/// 
	/// <p>The optional <code>ds:Transforms</code> element can contain the
	/// transformations performed on the signature policy document before computing
	/// its hash.
	/// 
	/// <p>Below follows the schema definition :
	/// <pre>
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
	/// @author ahmety
	/// date: Oct 15, 2009
	/// </summary>
	public class SignaturePolicyId : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private PolicyId mPolicyId;
		private Transforms mTransforms;

		private SignaturePolicyHash mSignaturePolicyHash;
        private readonly List<SignaturePolicyQualifier> mPolicyQualifiers = new List<SignaturePolicyQualifier>(0);

		public SignaturePolicyId(Context aContext, PolicyId aPolicyId, Transforms aTransforms, DigestMethod aDigestMethod, IList<SignaturePolicyQualifier> aQualifiers) : base(aContext)
		{
		    /*
			addLineBreak();

			mPolicyId = aPolicyId;
			mTransforms = aTransforms;
			//mDigestMethod = aDigestMethod;
			if (aQualifiers != null)
			{
				mPolicyQualifiers.AddRange(aQualifiers);
			}

			if (mPolicyId == null)
			{
				throw new XMLSignatureRuntimeException("errors.nullElement", Constants.TAGX_SIGPOLICYID);
			}

			mElement.AppendChild(mPolicyId.Element);
			addLineBreak();

			if (mTransforms != null)
			{
				mElement.AppendChild(mTransforms.Element);
				addLineBreak();
			}
            */

		    // Retrieve the electronic document containing the details of the policy
		    string policyUrl = aPolicyId.Identifier.Value;
		    Document doc = Resolver.resolve(policyUrl, aContext);
		    if (doc == null)
		    {
		        throw new XMLSignatureException("validation.policy.cantFind");
		    }

		    // apply transforms, check hash
		    if (aTransforms != null)
		    {
		        doc = aTransforms.apply(doc);
		    }

		    DigestMethod digestMethod = aDigestMethod != null ? aDigestMethod : aContext.Config.AlgorithmsConfig.DigestMethod;
		    byte[] digestValue = KriptoUtil.digest(doc.Bytes, digestMethod);

		    constructElement(aPolicyId, aTransforms, digestMethod, digestValue, aQualifiers);
		}

        public SignaturePolicyId(Context aContext,
                                 PolicyId aPolicyId,
                                 Transforms aTransforms,
                                 DigestMethod aDigestMethod,
                                 byte[] digestValue,
                                 List<SignaturePolicyQualifier> aQualifiers)
            : base(aContext)
        {
            constructElement(aPolicyId, aTransforms, aDigestMethod, digestValue, aQualifiers);
        }

        private void constructElement(PolicyId aPolicyId, Transforms aTransforms,
                                      DigestMethod aDigestMethod, byte[] aDigestValue,
                                      IList<SignaturePolicyQualifier> aQualifiers)
        {
            addLineBreak();

            mPolicyId = aPolicyId;
            mTransforms = aTransforms;
            //mDigestMethod = aDigestMethod;
            if (aQualifiers != null)
            {
                mPolicyQualifiers.AddRange(aQualifiers);
            }

            if (mPolicyId == null)
            {
                throw new XMLSignatureRuntimeException("errors.nullElement", Constants.TAGX_SIGPOLICYID);
            }

            mElement.AppendChild(mPolicyId.Element);
            addLineBreak();

            if (mTransforms != null)
            {
                mElement.AppendChild(mTransforms.Element);
                addLineBreak();
            }

	        // policy hash
			mSignaturePolicyHash = new SignaturePolicyHash(mContext);

			mSignaturePolicyHash.DigestMethod = aDigestMethod;
			mSignaturePolicyHash.DigestValue = aDigestValue;

			mElement.AppendChild(mSignaturePolicyHash.Element);
			addLineBreak();

			// policy qualifiers
			if (aQualifiers != null && aQualifiers.Count > 0)
			{
                Element qualifiersElm = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGPOLICYQUALIFIERS);
				addLineBreak(qualifiersElm);
				foreach (SignaturePolicyQualifier qualifier in aQualifiers)
				{
					qualifiersElm.AppendChild(qualifier.Element);
					addLineBreak(qualifiersElm);
				}
			}
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>

		public SignaturePolicyId(Element aElement, Context aContext) : base(aElement, aContext)
		{

			// policy id
            Element policyIdElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGPOLICYID);
			if (policyIdElm == null)
			{
                throw new XMLSignatureException("xml.WrongContent", Constants.TAGX_SIGPOLICYID, LocalName);
			}
			mPolicyId = new PolicyId(policyIdElm, mContext);

			// transforms
            Element transformsElm = selectChildElement(Constants.NS_XMLDSIG, Constants.TAG_TRANSFORMS);
			if (transformsElm != null)
			{
				mTransforms = new Transforms(transformsElm, mContext);
			}

			// policy hash : digest method, digest value
            Element sigPolichHashElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGPOLICYHASH);
			if (sigPolichHashElm == null)
			{
                throw new XMLSignatureException("xml.WrongContent", Constants.TAGX_SIGPOLICYHASH, LocalName);
			}

			mSignaturePolicyHash = new SignaturePolicyHash(sigPolichHashElm, mContext);

			// qualifiers
            Element qualifiersElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGPOLICYQUALIFIERS);
			if (qualifiersElm != null)
			{
                Element[] qualifierElmArr = XmlCommonUtil.selectNodes(qualifiersElm.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_SIGPOLICYQUALIFIER);
				foreach (Element qualifierElm in qualifierElmArr)
				{
					mPolicyQualifiers.Add(new SignaturePolicyQualifier(qualifierElm, mContext));
				}
			}
		}


		public virtual PolicyId PolicyId
		{
			get
			{
				return mPolicyId;
			}
		}

		public virtual Transforms Transforms
		{
			get
			{
				return mTransforms;
			}
		}

		public virtual byte[] DigestValue
		{
			get
			{
				return mSignaturePolicyHash.DigestValue;
			}
		}

		public virtual DigestMethod DigestMethod
		{
			get
			{
				return mSignaturePolicyHash.DigestMethod;
			}
		}

		public virtual IList<SignaturePolicyQualifier> PolicyQualifiers
		{
			get
			{
				return mPolicyQualifiers;
			}
		}


	    public String getFirstUserNoticeExplicitText()
	    {
	        List<SignaturePolicyQualifier> policyQualifiers = mPolicyQualifiers;
	        if (policyQualifiers == null || policyQualifiers.Count == 0)
	            return null;

	        return policyQualifiers[0].UserNotice.getExplicitTexts()[0];
	    }


        public override string LocalName
		{
			get
			{
                return Constants.TAGX_SIGNATUREPOLICYID;
			}
		}
	}

}