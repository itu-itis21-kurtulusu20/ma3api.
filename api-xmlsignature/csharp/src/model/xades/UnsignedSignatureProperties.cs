using System.Collections.Generic;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Logger = log4net.ILog;
	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;



    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <p>This element contains properties that qualify the XML signature that has
	/// been specified with the <code>Target</code> attribute of the
	/// <code>QualifyingProperties</code> container element. The content of this
	/// element is not covered by the XML signature.
	/// 
	/// <p><pre>
	/// &lt;xsd:element name="UnsignedSignatureProperties" type="UnsignedSignaturePropertiesType"/>
	/// 
	/// &lt;xsd:complexType name="UnsignedSignaturePropertiesType">
	///  &lt;xsd:choice maxOccurs="unbounded">
	///    &lt;xsd:element name="CounterSignature" type="CounterSignatureType" />
	///    &lt;xsd:element name="SignatureTimeStamp" type="XAdESTimeStampType/>
	///    &lt;xsd:element name="CompleteCertificateRefs" type="CompleteCertificateRefsType"/>
	///    &lt;xsd:element name="CompleteRevocationRefs" type="CompleteRevocationRefsType"/>
	///    &lt;xsd:element name="AttributeCertificateRefs" type="CompleteCertificateRefsType"/>
	///    &lt;xsd:element name="AttributeRevocationRefs" type="CompleteRevocationRefsType"/>
	///    &lt;xsd:element name="SigAndRefsTimeStamp" type="XAdESTimeStampType"/>
	///    &lt;xsd:element name="RefsOnlyTimeStamp" type="XAdESTimeStampType"/>
	///    &lt;xsd:element name="CertificateValues" type="CertificateValuesType"/>
	///    &lt;xsd:element name="RevocationValues" type="RevocationValuesType"/>
	///    &lt;xsd:element name="AttrAuthoritiesCertValues" type="CertificateValuesType"/>
	///    &lt;xsd:element name="AttributeRevocationValues" type="RevocationValuesType"/>
	///    &lt;xsd:element name="ArchiveTimeStamp" type="XAdESTimeStampType"/>
	///    &lt;xsd:any namespace="##other" />
	///  &lt;/xsd:choice>
	///  &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The optional <code>Id</code> attribute can be used to make a reference
	/// to the <code>UnsignedSignatureProperties</code> element.
	/// 
	/// @author ahmety
	/// date: Sep 1, 2009
	/// </summary>
	public class UnsignedSignatureProperties : XAdESBaseElement
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(UnsignedSignatureProperties));

		private readonly IList<CounterSignature> mCounterSignatures = new List<CounterSignature>(0);
		private readonly IList<XMLSignature> mCounterXMLSignatures = new List<XMLSignature>(0);

		private readonly IList<TimeStampValidationData> mTimeStampValidationDatas = new List<TimeStampValidationData>(0);

		private readonly IDictionary<XAdESTimeStamp, TimeStampValidationData> mTS2ValidationData = new Dictionary<XAdESTimeStamp, TimeStampValidationData>();
		/// <summary>
		/// A XAdES-T form MAY contain several <code>SignatureTimeSamp</code> elements,
		/// obtained from different TSAs
		/// </summary>
		private readonly IList<SignatureTimeStamp> mSignatureTimeStamps = new List<SignatureTimeStamp>(0);

		// C form
		private CompleteCertificateRefs mCompleteCertificateRefs;
		private CompleteRevocationRefs mCompleteRevocationRefs;
		private AttributeCertificateRefs mAttributeCertificateRefs;
		private AttributeRevocationRefs mAttributeRevocationRefs;

		// X 
		private readonly IList<SigAndRefsTimeStamp> mSigAndRefsTimeStamps = new List<SigAndRefsTimeStamp>(0);
		private readonly IList<RefsOnlyTimeStamp> mRefsOnlyTimeStamps = new List<RefsOnlyTimeStamp>(0);

		// X-L
		private CertificateValues mCertificateValues;
		private RevocationValues mRevocationValues;
		private AttrAuthoritiesCertValues mAttrAuthoritiesCertValues;
		private AttributeRevocationValues mAttributeRevocationValues;

		// A
		private readonly IList<ArchiveTimeStamp> mArchiveTimeStamps = new List<ArchiveTimeStamp>(0);

		// properties in order
		private readonly IList<UnsignedSignaturePropertyElement> mProperties = new List<UnsignedSignaturePropertyElement>(0);

		private readonly XMLSignature mSignature;

		public UnsignedSignatureProperties(Context aBaglam, XMLSignature aSignature) : base(aBaglam)
		{
			addLineBreak();
			mSignature = aSignature;
		}

		/// <summary>
		///  Construct UnsignedSignatureProperties from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <param name="aSignature"> which UnsignedSignatureProperties belongs </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public UnsignedSignatureProperties(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public UnsignedSignatureProperties(Element aElement, Context aContext, XMLSignature aSignature) : base(aElement, aContext)
		{
			mSignature = aSignature;
			init();
		}

		// delay construction of conntent because some children may reference a not
		// yet constructed conted via RetriecalMethod!
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: private void init() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		private void init()
		{

			IList<Element> children = XmlCommonUtil.selectChildElements(mElement);
			foreach (Element element in children)
			{
				resolve(element);
			}
		}

		private XAdESTimeStamp mLastTimeStampProcessed = null;
		private int mElementCountAfterTimestamp = 0;

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public void resolve(XmlElement aElement) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual void resolve(Element aElement)
		{
			string name = aElement.LocalName;

            if (name.Equals(Constants.TAGX_COUNTERSIGNATURE))
			{
				CounterSignature cs = new CounterSignature(aElement, mContext);
				mCounterSignatures.Add(cs);
				mCounterXMLSignatures.Add(cs.Signature);
				mProperties.Add(cs);
			}
            else if (name.Equals(Constants.TAGX_SIGNATURETIMESTAMP))
			{
				SignatureTimeStamp sts = new SignatureTimeStamp(aElement, mContext);
				mSignatureTimeStamps.Add(sts);
				mProperties.Add(sts);
				mLastTimeStampProcessed = sts;
				mElementCountAfterTimestamp = 0;
			}
            else if (name.Equals(Constants.TAGX_COMPLETECERTREFS))
			{
				mCompleteCertificateRefs = new CompleteCertificateRefs(aElement, mContext);
				mProperties.Add(mCompleteCertificateRefs);
			}
            else if (name.Equals(Constants.TAGX_COMPLETEREVOCATIONREFS))
			{
				mCompleteRevocationRefs = new CompleteRevocationRefs(aElement, mContext);
				mProperties.Add(mCompleteRevocationRefs);
			}
            else if (name.Equals(Constants.TAGX_ATTRIBUTECERTIFICATEREFS))
			{
				mAttributeCertificateRefs = new AttributeCertificateRefs(aElement, mContext);
				mProperties.Add(mAttributeCertificateRefs);
			}
            else if (name.Equals(Constants.TAGX_ATTRIBUTEREVOCATIONREFS))
			{
				mAttributeRevocationRefs = new AttributeRevocationRefs(aElement, mContext);
				mProperties.Add(mAttributeRevocationRefs);
			}
            else if (name.Equals(Constants.TAGX_SIGANDREFSTIMESTAMP))
			{
				SigAndRefsTimeStamp srt = new SigAndRefsTimeStamp(aElement, mContext);
				mSigAndRefsTimeStamps.Add(srt);
				mProperties.Add(srt);
				mLastTimeStampProcessed = srt;
				mElementCountAfterTimestamp = 0;
			}
            else if (name.Equals(Constants.TAGX_REFSONLYTIMESTAMP))
			{
				RefsOnlyTimeStamp rts = new RefsOnlyTimeStamp(aElement, mContext);
				mRefsOnlyTimeStamps.Add(rts);
				mProperties.Add(rts);
				mLastTimeStampProcessed = rts;
				mElementCountAfterTimestamp = 0;
			}
            else if (name.Equals(Constants.TAGX_CERTIFICATEVALUES))
			{
				mCertificateValues = new CertificateValues(aElement, mContext);
				mProperties.Add(mCertificateValues);
			}
            else if (name.Equals(Constants.TAGX_REVOCATIONVALUES))
			{
				mRevocationValues = new RevocationValues(aElement, mContext);
				mProperties.Add(mRevocationValues);
			}
            else if (name.Equals(Constants.TAGX_TIMESTAMPVALIDATIONDATA))
			{
				TimeStampValidationData tsvd = new TimeStampValidationData(aElement, mContext);
				mTimeStampValidationDatas.Add(tsvd);
				string uri = tsvd.URI;
				if (uri != null && uri.Length > 0)
				{
					uri = uri.Substring(1);
					XAdESTimeStamp found = null;
					SignedDataObjectProperties sdop = mSignature.QualifyingProperties.SignedDataObjectProperties;
					if (sdop != null)
					{
						for (int j = 0; j < sdop.IndividualDataObjectsTimeStampCount; j++)
						{
							IndividualDataObjectsTimeStamp idots = sdop.getIndividualDataObjectsTimeStamp(j);
							string id = idots.Id;
							if (id != null && id.Equals(uri))
							{
								found = idots;
								break;
							}
						}
						if (found == null)
						{
							for (int j = 0; j < sdop.AllDataObjectsTimeStampCount; j++)
							{
								AllDataObjectsTimeStamp adots = sdop.getAllDataObjectsTimeStamp(j);
								string id = adots.Id;
								if (id != null && id.Equals(uri))
								{
									found = adots;
									break;
								}
							}
						}
					}

					if (found == null)
					{
						foreach (UnsignedSignaturePropertyElement element in mProperties)
						{
							if (element is XAdESTimeStamp)
							{
								XAdESTimeStamp timeStamp = (XAdESTimeStamp)element;
								string id = timeStamp.Id;
								if (id != null && id.Equals(uri))
								{
									found = timeStamp;
									break;
								}
							}
						}
					}
					if (found == null)
					{
						throw new XMLSignatureException("validation.timestamp.vdata.cantResolveByURI", uri);
					}
					mTS2ValidationData[found] = tsvd;

				}
				else if (mLastTimeStampProcessed != null && mElementCountAfterTimestamp == 1)
				{
					mTS2ValidationData[mLastTimeStampProcessed] = tsvd;
				}
				else
				{
					// todo: improve message  
					throw new XMLSignatureException("validation.timestamp.vdata.cantResolve", uri);
				}

				// todo resolve where this tsvd belongs
				// mTS2ValidationData.put(timestamp, tsvd);
				mProperties.Add(tsvd);
			}
            else if (name.Equals(Constants.TAGX_ARCHIVETIMESTAMP))
			{
				ArchiveTimeStamp ats;
				string @namespace = aElement.NamespaceURI;

                if (@namespace.Equals(Constants.NS_XADES_1_4_1))
				{
					ats = new ArchiveTimeStamp(aElement, mContext);
				}
				else
				{
					ats = new ArchiveTimeStamp132(aElement, mContext);
				}
				mArchiveTimeStamps.Add(ats);
				mProperties.Add(ats);
				mLastTimeStampProcessed = ats;
				mElementCountAfterTimestamp = 0;
			}
			else
			{
				// todo resolve other whenever implemented
				//mOtherProperties.add(aElement);
				logger.Warn("Not yet identified unsigned property type: " + name);
			}
			mElementCountAfterTimestamp++;
		}

		public virtual IList<UnsignedSignaturePropertyElement> Properties
		{
			get
			{
				return mProperties;
			}
		}

		public virtual void addCounterSignature(XMLSignature aSignature)
		{
			CounterSignature cs = new CounterSignature(mContext, aSignature);
			mCounterSignatures.Add(cs);
			mCounterXMLSignatures.Add(aSignature);
			addElement(cs);
		}

        public void removeCounterSignature(XMLSignature aSignature){
            foreach (CounterSignature cs in mCounterSignatures){
                if (cs.Signature.Equals(aSignature)){
                    mElement.RemoveChild(cs.Element);
                    mCounterSignatures.Remove(cs);
                    mCounterXMLSignatures.Remove(aSignature);
                    return;
                }
            }
            throw new SignatureRuntimeException("Cant find signature in counter signatures!");
        }

		public virtual int CounterSignatureCount
		{
			get
			{
				return mCounterSignatures.Count;
			}
		}

		public virtual CounterSignature getCounterSignature(int aIndex)
		{
			return mCounterSignatures[aIndex];
		}

		public virtual IList<XMLSignature> AllCounterSignatures
		{
			get
			{
				return mCounterXMLSignatures;
			}
		}

		public virtual int SignatureTimeStampCount
		{
			get
			{
				return mSignatureTimeStamps.Count;
			}
		}

		public virtual SignatureTimeStamp getSignatureTimeStamp(int aIndex)
		{
			return mSignatureTimeStamps[aIndex];
		}

        public virtual IList<SignatureTimeStamp> getSignatureTimeStamps()
        {
            return mSignatureTimeStamps;
        }

		public virtual void addSignatureTimeStamp(SignatureTimeStamp aSignatureTimeStamp)
		{
			mSignatureTimeStamps.Add(aSignatureTimeStamp);
			addElement(aSignatureTimeStamp);
		}

		public virtual CompleteCertificateRefs CompleteCertificateRefs
		{
			get
			{
				return mCompleteCertificateRefs;
			}
			set
			{
				mCompleteCertificateRefs = value;
				addElement(value);
			}
		}


		public virtual CompleteRevocationRefs CompleteRevocationRefs
		{
			get
			{
				return mCompleteRevocationRefs;
			}
			set
			{
				mCompleteRevocationRefs = value;
				addElement(value);
			}
		}


		public virtual AttributeCertificateRefs AttributeCertificateRefs
		{
			get
			{
				return mAttributeCertificateRefs;
			}
			set
			{
				mAttributeCertificateRefs = value;
				addElement(value);
			}
		}


		public virtual AttributeRevocationRefs AttributeRevocationRefs
		{
			get
			{
				return mAttributeRevocationRefs;
			}
			set
			{
				mAttributeRevocationRefs = value;
				addElement(mAttributeRevocationRefs);
			}
		}


		public virtual int SigAndRefsTimeStampCount
		{
			get
			{
				return mSigAndRefsTimeStamps.Count;
			}
		}

		public virtual SigAndRefsTimeStamp getSigAndRefsTimeStamp(int aIndex)
		{
			return mSigAndRefsTimeStamps[aIndex];
		}

        public virtual IList<SigAndRefsTimeStamp> getSigAndRefsTimeStamps()
        {
            return mSigAndRefsTimeStamps;
        }

		public virtual void addSigAndRefsTimeStamp(SigAndRefsTimeStamp aSrts)
		{
			mSigAndRefsTimeStamps.Add(aSrts);
			addElement(aSrts);
		}

		public virtual int RefsOnlyTimeStampCount
		{
			get
			{
				return mRefsOnlyTimeStamps.Count;
			}
		}

		public virtual RefsOnlyTimeStamp getRefsOnlyTimeStamp(int aIndex)
		{
			return mRefsOnlyTimeStamps[aIndex];
		}

        public virtual IList<RefsOnlyTimeStamp> getRefsOnlyTimeStamps()
        {
            return mRefsOnlyTimeStamps;
        }

		public virtual void addRefsOnlyTimeStamp(RefsOnlyTimeStamp aRots)
		{
			mRefsOnlyTimeStamps.Add(aRots);
			addElement(aRots);
		}

		public virtual AttrAuthoritiesCertValues AttrAuthoritiesCertValues
		{
			get
			{
				return mAttrAuthoritiesCertValues;
			}
			set
			{
				mAttrAuthoritiesCertValues = value;
				addElement(mAttrAuthoritiesCertValues);
			}
		}


		public virtual CertificateValues CertificateValues
		{
			get
			{
				return mCertificateValues;
			}
			set
			{
				mCertificateValues = value;
				addElement(value);
			}
		}


		public virtual RevocationValues RevocationValues
		{
			get
			{
				return mRevocationValues;
			}
			set
			{
				mRevocationValues = value;
				addElement(value);
			}
		}


		public virtual AttributeRevocationValues AttributeRevocationValues
		{
			get
			{
				return mAttributeRevocationValues;
			}
			set
			{
				mAttributeRevocationValues = value;
				addElement(value);
			}
		}


		public virtual int ArchiveTimeStampCount
		{
			get
			{
				return mArchiveTimeStamps.Count;
			}
		}

		public virtual void addArchiveTimeStamp(ArchiveTimeStamp aArchiveTimeStamp)
		{
			mArchiveTimeStamps.Add(aArchiveTimeStamp);
			addElement(aArchiveTimeStamp);
		}

		public virtual ArchiveTimeStamp getArchiveTimeStamp(int aIndex)
		{
			return mArchiveTimeStamps[aIndex];
		}

        public virtual IList<ArchiveTimeStamp> getArchiveTimeStamps()
        {
            return mArchiveTimeStamps;
        }

		/// <summary>
		/// Add timestamp validation data of timestamp existing in signature.
		/// 
		/// When a XAdES signature requires to include all the validation data
		/// required for a full verification of a time-stamp token embedded in any of
		/// the following containers: SignatureTimeStamp, RefsOnlyTimeStamp,
		/// SigAndRefsTimeStamp, or ArchiveTimeStamp, and that validation data is not
		/// present in other parts of the signature, a new
		/// xadesv141:TimeStampValidationData element SHALL be created containing the
		/// missing validation data information and it SHALL be added as a child of
		/// UnsignedSignatureProperties elements immediately after the respective
		/// time-stamp token container element.
		/// </summary>
		/// <param name="aTSValidationData"> to add </param>
		/// <param name="aTimeStamp"> which validation data belongs </param>
		/// <exception cref="XMLSignatureException"> if the timestamp is not in the signature </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public void addTimeStampValidationData(TimeStampValidationData aTSValidationData, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp aTimeStamp) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual void addTimeStampValidationData(TimeStampValidationData aTSValidationData, XAdESTimeStamp aTimeStamp)
		{
			string tsId = aTimeStamp.Id;
			if (tsId != null && tsId.Length > 0)
			{
				aTSValidationData.URI = "#" + tsId;
			}

			if ((aTimeStamp is IndividualDataObjectsTimeStamp) || (aTimeStamp is AllDataObjectsTimeStamp))
			{
				mElement.AppendChild(aTSValidationData.Element);
				mProperties.Add(aTSValidationData);
				addLineBreak();
			}
			else
			{
				int indexOfTS = mProperties.IndexOf((UnsignedSignaturePropertyElement)aTimeStamp);
				if (indexOfTS == -1)
				{
					throw new XMLSignatureException("validation.timestamp.vdata.orderError");
				}
				mProperties.Insert(indexOfTS + 1, aTSValidationData);
				Element nextElement = XmlCommonUtil.getNextElement(aTimeStamp.Element.NextSibling);
				mElement.InsertBefore(aTSValidationData.Element, nextElement);
				mElement.InsertBefore(Document.CreateTextNode("\n"), nextElement);
			}

			mTimeStampValidationDatas.Add(aTSValidationData);
			mTS2ValidationData[aTimeStamp] = aTSValidationData;
		}


		public virtual TimeStampValidationData getValidationDataForTimestamp(XAdESTimeStamp aTimestamp)
		{
			return mTS2ValidationData[aTimestamp];
		}

		public virtual TimeStampValidationData getTimeStampValidationData(int aIndex)
		{
			return mTimeStampValidationDatas[aIndex];
		}

		public virtual IDictionary<XAdESTimeStamp, TimeStampValidationData> AllTimeStampValidationData
		{
			get
			{
				return mTS2ValidationData;
			}
		}

		public virtual int TimestampValidationDataCount
		{
			get
			{
				return mTimeStampValidationDatas.Count;
			}
		}

		protected internal virtual void addElement(UnsignedSignaturePropertyElement aElement)
		{
			mProperties.Add(aElement);
			addLineBreak();
			mElement.AppendChild(((BaseElement)aElement).Element);
		}


		public override string LocalName
		{
			get
			{
                return Constants.TAGX_UNSIGNEDSIGNATUREPROPERTIES;
			}
		}
	}

}