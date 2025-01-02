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
	/// <p>This element contains properties that qualify some of the signed
	/// data objects.
	/// 
	/// <p>The optional <code>Id</code> attribute can be used to make a reference
	/// to the <code>SignedDataObjectProperties</code> element.
	/// 
	/// <p>These properties qualify the signed data object after all the required
	/// transforms have been made.
	/// 
	/// <p><pre>
	/// &lt;xsd:element name="SignedDataObjectProperties" type="SignedDataObjectPropertiesType"/>
	/// 
	/// &lt;xsd:complexType name="SignedDataObjectPropertiesType">
	///     &lt;xsd:sequence>
	///         &lt;xsd:element name="DataObjectFormat"
	///             type="DataObjectFormatType" minOccurs="0" maxOccurs="unbounded"/>
	///         &lt;xsd:element name="CommitmentTypeIndication"
	///             type="CommitmentTypeIndicationType" minOccurs="0" maxOccurs="unbounded"/>
	///         &lt;xsd:element name="AllDataObjectsTimeStamp"
	///             type="XAdESTimeStampType" minOccurs="0" maxOccurs="unbounded"/>
	///         &lt;xsd:element name="IndividualDataObjectsTimeStamp"
	///             type="XAdESTimeStampType" minOccurs="0" maxOccurs="unbounded"/>
	///     &lt;/xsd:sequence>
	///     &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 1, 2009
	/// </summary>
	public class SignedDataObjectProperties : XAdESBaseElement
	{

		private readonly IList<DataObjectFormat> mDataObjectFormats = new List<DataObjectFormat>(0);
		private readonly IList<CommitmentTypeIndication> mCommitmentTypeIndications = new List<CommitmentTypeIndication>(0);
		private readonly IList<AllDataObjectsTimeStamp> mAllDataObjectsTimeStamps = new List<AllDataObjectsTimeStamp>(0);
		private readonly IList<IndividualDataObjectsTimeStamp> mIndividualDataObjectsTimeStamps = new List<IndividualDataObjectsTimeStamp>(0);


		public SignedDataObjectProperties(Context aBaglam) : base(aBaglam)
		{
		}


		/// <summary>
		///  Construct SignedDataObjectProperties from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignedDataObjectProperties(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignedDataObjectProperties(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element[] dofElms = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_DATAOBJECTFORMAT);
			if (dofElms != null)
			{
				for (int i = 0; i < dofElms.Length; i++)
				{
					mDataObjectFormats.Add(new DataObjectFormat(dofElms[i], mContext));
				}
			}

            Element[] commitmentTypeElms = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_COMMITMENTTYPEINDICATION);
			if (commitmentTypeElms != null)
			{
				for (int i = 0; i < commitmentTypeElms.Length; i++)
				{
					mCommitmentTypeIndications.Add(new CommitmentTypeIndication(commitmentTypeElms[i], mContext));
				}
			}

            Element[] allDataObjsElms = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_ALLDATAOBJECTSTIMESTAMP);
			if (allDataObjsElms != null)
			{
				for (int i = 0; i < allDataObjsElms.Length; i++)
				{
					mAllDataObjectsTimeStamps.Add(new AllDataObjectsTimeStamp(allDataObjsElms[i], mContext));
				}
			}

            Element[] individualDataObjectsElms = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_INDIVIDUALDATAOBJECTSTIMESTAMP);
			if (individualDataObjectsElms != null)
			{
				for (int i = 0; i < individualDataObjectsElms.Length; i++)
				{
					mIndividualDataObjectsTimeStamps.Add(new IndividualDataObjectsTimeStamp(individualDataObjectsElms[i], mContext));
				}
			}
		}

		private void setupChildren()
		{
            XmlCommonUtil.removeChildren(mElement);

			addLineBreak();

			foreach (DataObjectFormat dof in mDataObjectFormats)
			{
				mElement.AppendChild(dof.Element);
				addLineBreak();
			}
			foreach (CommitmentTypeIndication cti in mCommitmentTypeIndications)
			{
				mElement.AppendChild(cti.Element);
				addLineBreak();
			}
			foreach (AllDataObjectsTimeStamp adots in mAllDataObjectsTimeStamps)
			{
				mElement.AppendChild(adots.Element);
				addLineBreak();
			}
			foreach (IndividualDataObjectsTimeStamp idots in mIndividualDataObjectsTimeStamps)
			{
				mElement.AppendChild(idots.Element);
				addLineBreak();
			}

			if (mId != null)
			{
                if (mElement.HasAttribute(Constants.ATTR_ID))
                {
                    mElement.RemoveAttribute(Constants.ATTR_ID);
                }
                mElement.SetAttribute(Constants.ATTR_ID, null, mId);
			}
		}

		public virtual int DataObjectFormatCount
		{
			get
			{
				return mDataObjectFormats.Count;
			}
		}

		public virtual DataObjectFormat getDataObjectFormat(int aIndex)
		{
			return mDataObjectFormats[aIndex];
		}

		public virtual void addDataObjectFormat(DataObjectFormat aDataObjectFormat)
		{
			mDataObjectFormats.Add(aDataObjectFormat);
			setupChildren();
		}

		public virtual int CommitmentTypeIndicationCount
		{
			get
			{
				return mCommitmentTypeIndications.Count;
			}
		}

		public virtual CommitmentTypeIndication getCommitmentTypeIndication(int aIndex)
		{
			return mCommitmentTypeIndications[aIndex];
		}

		public virtual void addCommitmentTypeIndication(CommitmentTypeIndication aCommitmentTypeIndication)
		{
			mCommitmentTypeIndications.Add(aCommitmentTypeIndication);
			setupChildren();
		}

		public virtual int AllDataObjectsTimeStampCount
		{
			get
			{
				return mAllDataObjectsTimeStamps.Count;
			}
		}

		public virtual AllDataObjectsTimeStamp getAllDataObjectsTimeStamp(int aIndex)
		{
			return mAllDataObjectsTimeStamps[aIndex];
		}

        public virtual IList<AllDataObjectsTimeStamp> getAllDataObjectsTimeStamps()
        {
            return mAllDataObjectsTimeStamps;
        }

		/// <summary>
		/// Add TimestampValidationData manually </summary>
		/// <param name="aAllDataObjectsTimeStamp"> </param>
		/// <seealso cref= TimeStampValidationData </seealso>
		public virtual void addAllDataObjectsTimeStamp(AllDataObjectsTimeStamp aAllDataObjectsTimeStamp)
		{
			// todo         addTimestampValidationData ;
			mAllDataObjectsTimeStamps.Add(aAllDataObjectsTimeStamp);
			setupChildren();
		}

		public virtual int IndividualDataObjectsTimeStampCount
		{
			get
			{
				return mIndividualDataObjectsTimeStamps.Count;
			}
		}

		public virtual IndividualDataObjectsTimeStamp getIndividualDataObjectsTimeStamp(int aIndex)
		{
			return mIndividualDataObjectsTimeStamps[aIndex];
		}

        public virtual IList<IndividualDataObjectsTimeStamp> getIndividualDataObjectsTimeStamps()
        {
            return mIndividualDataObjectsTimeStamps;
        }

		/// <summary>
		/// Add TimestampValidationData manually </summary>
        /// <param name="aIndividualDataObjectsTimeStamp"> </param>
		/// <seealso cref= TimeStampValidationData </seealso>
		public virtual void addIndividualDataObjectsTimeStamp(IndividualDataObjectsTimeStamp aIndividualDataObjectsTimeStamp)
		{
			// todo         addTimestampValidationData ;
			mIndividualDataObjectsTimeStamps.Add(aIndividualDataObjectsTimeStamp);
			setupChildren();
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_SIGNEDATAOBJECTPROPERIES;
			}
		}
	}

}