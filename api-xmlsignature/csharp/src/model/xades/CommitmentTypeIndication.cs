using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;


	/// <summary>
	/// The commitment type can be indicated in the electronic signature either:
	/// <ul>
	/// <li>explicitly using a commitment type indication in the electronic signature;
	/// <li>implicitly or explicitly from the semantics of the signed data object.
	/// </ul>
	/// 
	/// <p>If the indicated commitment type is explicit by means of a commitment
	/// type indication in the electronic signature, acceptance of a verified
	/// signature implies acceptance of the semantics of that commitment type.
	/// The semantics of explicit commitment types indications shall be specified
	/// either as part of the signature policy or MAY be registered for generic
	/// use across multiple policies.
	/// 
	/// <p>If a signature includes a commitment type indication other than one
	/// of those recognized under the signature policy the signature shall be
	/// treated as invalid.
	/// 
	/// <p>How commitment is indicated using the semantics of the data object
	/// being signed is outside the scope of the present document.
	/// 
	/// <p>The commitment type MAY be:
	/// <ul>
	/// <li>defined as part of the signature policy, in which case the commitment
	/// type has precise semantics that is defined as part of the signature policy;
	/// <li>a registered type, in which case the commitment type has precise
	/// semantics defined by registration, under the rules of the registration
	/// authority. Such a registration authority may be a trading association or
	/// a legislative authority.
	/// </ul>
	/// 
	/// <p>The definition of a commitment type includes:
	/// <ul>
	/// <li>the object identifier for the commitment;
	/// <li>a sequence of qualifiers.
	/// </ul>
	/// 
	/// <p> The qualifiers can provide more information about the commitment, it
	/// could provide, for example, information about the context be it
	/// contractual/legal/application specific.
	/// 
	/// <p>If an electronic signature does not contain a recognized commitment
	/// type then the semantics of the electronic signature is dependent on the
	/// data object being signed and the context in which it is being used.
	/// 
	/// <p>This is a signed property that qualifies signed data object(s). In
	/// consequence, an XML electronic signature aligned with the present document
	/// MAY contain more than one CommitmentTypeIndication elements.
	/// 
	/// <p>Below follows the schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="CommitmentTypeIndication" type="CommitmentTypeIndicationType"/>
	/// 
	/// &lt;xsd:complexType name="CommitmentTypeIndicationType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="CommitmentTypeId" type="ObjectIdentifierType"/>
	///     &lt;xsd:choice>
	///       &lt;xsd:element name="ObjectReference" type="xsd:anyURI" maxOccurs="unbounded"/>
	///       &lt;xsd:element name="AllSignedDataObjects"/>
	///     &lt;/xsd:choice>
	///     &lt;xsd:element name="CommitmentTypeQualifiers" type="CommitmentTypeQualifiersListType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="CommitmentTypeQualifiersListType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="CommitmentTypeQualifier" type="AnyType" minOccurs="0" maxOccurs="unbounded"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The CommitmentTypeId element univocally identifies the type of commitment
	/// made by the signer. A number of commitments have been already identified
	/// in TS 101 733 , namely:
	/// 
	/// <ul>
	/// <li>Proof of origin indicates that the signer recognizes to have created,
	/// approved and sent the signed data object. The URI for this commitment is
	/// <a href="">http://uri.etsi.org/01903/v1.2.2#ProofOfOrigin</a>.
	/// <li>Proof of receipt indicates that signer recognizes to have received the
	/// content of the signed data object. The URI for this commitment is
	/// <a href="">http://uri.etsi.org/01903/v1.2.2#ProofOfReceipt</a>.
	/// <li>Proof of delivery indicates that the TSP providing that indication has
	/// delivered a signed data object in a local store accessible to the recipient
	/// of the signed data object. The URI for this commitment is
	/// <a href="">http://uri.etsi.org/01903/v1.2.2#ProofOfDelivery</a>.
	/// <li>Proof of sender indicates that the entity providing that indication has
	/// sent the signed data object (but not necessarily created it). The URI for
	/// this commitment is
	/// <a href="">http://uri.etsi.org/01903/v1.2.2#ProofOfSender</a>.
	/// <li>Proof of approval indicates that the signer has approved the content of
	/// the signed data object. The URI for this commitment is
	/// <a href="">http://uri.etsi.org/01903/v1.2.2#ProofOfApproval</a>.
	/// <li>Proof of creation indicates that the signer has created the signed data
	/// object (but not necessarily approved, nor sent it). The URI for this
	/// commitment is
	/// <a href="">http://uri.etsi.org/01903/v1.2.2#ProofOfCreation</a.>
	/// </ul>
	/// 
	/// <p>One <code>ObjectReference</code> element refers to one
	/// <code>ds:Reference</code> element of the <code>ds:SignedInfo</code>
	/// corresponding with one data object qualified by this property. If some but
	/// not all the signed data objects share the same commitment, one
	/// <code>ObjectReference</code> element MUST appear for each one of them.
	/// However, if all the signed data objects share the same commitment, the
	/// <code>AllSignedDataObjects</code> empty element MUST be present.
	/// 
	/// <p>The <code>CommitmentTypeQualifiers</code> element provides means to
	/// include additional qualifying information on the commitment made by the
	/// signer.
	/// 
	/// 
	/// @author ahmety
	/// date: Sep 23, 2009
	/// </summary>
	public class CommitmentTypeIndication : XAdESBaseElement
	{
		private readonly CommitmentTypeId mCommitmentTypeId;

		private readonly IList<string> mObjectReferences = new List<string>();

		/*
		    If all the signed data objects share the same commitment,
		    the AllSignedDataObjects empty element MUST be present.
		*/
		private readonly bool mAllSignedDataObjects;

		private readonly IList<CommitmentTypeQualifier> mCommitmentTypeQualifiers = new List<CommitmentTypeQualifier>(0);

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CommitmentTypeIndication(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aBaglam, CommitmentTypeId aCommitmentTypeId, java.util.List<String> aObjectReferences, boolean aAllSignedDataObjects, java.util.List<CommitmentTypeQualifier> aCommitmentTypeQualifiers) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CommitmentTypeIndication(Context aBaglam, CommitmentTypeId aCommitmentTypeId, IList<string> aObjectReferences, bool aAllSignedDataObjects, IList<CommitmentTypeQualifier> aCommitmentTypeQualifiers) : base(aBaglam)
		{
			mCommitmentTypeId = aCommitmentTypeId;
			mObjectReferences = aObjectReferences;
			mAllSignedDataObjects = aAllSignedDataObjects;
			mCommitmentTypeQualifiers = aCommitmentTypeQualifiers;

			if (aCommitmentTypeId == null)
			{
				throw new XMLSignatureException("errors.null", "CommitmentTypeId");
			}

			if (((aObjectReferences == null) || (aObjectReferences.Count == 0)) && (!aAllSignedDataObjects))
			{
				throw new XMLSignatureException("core.model.commitmentNeedObjectReferences");
			}


			addLineBreak();
			mElement.AppendChild(mCommitmentTypeId.Element);
			addLineBreak();

			if ((mObjectReferences != null) && (mObjectReferences.Count > 0))
			{
				for (int i = 0; i < mObjectReferences.Count; i++)
				{
					string objectRef = mObjectReferences[i];
                    insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OBJECTREFERENCE, objectRef);
				}
			}

			if (mAllSignedDataObjects)
			{
                insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ALLSIGNEDDATAOBJECTS);
			}

			if (mCommitmentTypeQualifiers != null)
			{
				for (int i = 0; i < mCommitmentTypeQualifiers.Count; i++)
				{
					CommitmentTypeQualifier ctq = mCommitmentTypeQualifiers[i];
					mElement.AppendChild(ctq.Element);
					addLineBreak();
				}
			}

		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="XMLSignatureException">
		///          when structure is invalid or can not be resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CommitmentTypeIndication(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CommitmentTypeIndication(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element typeIdElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_COMMITMENTTYPEID);
			if (typeIdElm != null)
			{
				mCommitmentTypeId = new CommitmentTypeId(typeIdElm, mContext);
			}

            Element[] objectIdElm = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_OBJECTREFERENCE);
			if (objectIdElm != null && objectIdElm.Length > 0)
			{
				for (int i = 0; i < objectIdElm.Length; i++)
				{
					string objId = XmlCommonUtil.getText(objectIdElm[i]);
					mObjectReferences.Add(objId);
				}
			}

            mAllSignedDataObjects = (null != selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ALLSIGNEDDATAOBJECTS));

            Element qualifiersElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_COMMITMENTTYPEQUALIFIERS);
			if (qualifiersElm != null)
			{
                Element[] qualifierArr = XmlCommonUtil.selectNodes(qualifiersElm.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_COMMITMENTTYPEQUALIFIER);
				for (int i = 0; i < qualifierArr.Length; i++)
				{
					CommitmentTypeQualifier ctq = new CommitmentTypeQualifier(qualifierArr[i], mContext);
					mCommitmentTypeQualifiers.Add(ctq);
				}
			}
		}

		public virtual ObjectIdentifier CommitmentTypeId
		{
			get
			{
				return mCommitmentTypeId;
			}
		}

		public virtual IList<string> ObjectReferences
		{
			get
			{
				return mObjectReferences;
			}
		}

		public virtual bool AllSignedDataObjects
		{
			get
			{
				return mAllSignedDataObjects;
			}
		}

		public virtual IList<CommitmentTypeQualifier> CommitmentTypeQualifiers
		{
			get
			{
				return mCommitmentTypeQualifiers;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_COMMITMENTTYPEINDICATION;
			}
		}
	}

}