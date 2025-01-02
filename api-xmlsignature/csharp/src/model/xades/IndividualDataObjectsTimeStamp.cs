using System;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{


	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	using TSClient = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
	using TSSettings = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using TimestampConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using DOMDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using Include = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.Include;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using SignedInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
	using TimeStampResp = tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;
	using Element = XmlElement;


	/// <summary>
	/// The <code>IndividualDataObjectsTimeStamp</code> element contains the
	/// time-stamp computed before the signature production, over a sequence formed
	/// by SOME <code>ds:Reference</code> elements within the
	/// <code>ds:SignedInfo</code>. Note that this sequence cannot contain a
	/// <code>ds:Reference</code> computed on the <code>SignedProperties</code>
	/// element.
	/// 
	/// <p>The <code>IndividualDataObjectsTimeStamp</code> element is a signed
	/// property that qualifies the signed data object(s).
	/// 
	/// <p>Several instances of this property can occur within the same XAdES.
	/// 
	/// <p>Below follows the schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="IndividualDataObjectsTimeStamp" type="XAdESTimeStampType"/>
	/// </pre>
	/// <p>This property uses the explicit (Include) mechanism. Generating
	/// applications MUST compose the <code>Include</code> elements to refer to
	/// those <code>ds:Reference</code> elements that are to be time-stamped. Their
	/// corresponding referencedData attribute MUST be present and set to "true".
	/// 
	/// <p>The digest computation input MUST be the result of processing the
	/// selected <code>ds:Reference</code> within <code>ds:SignedInfo</code> as
	/// follows:
	/// <ol>
	/// <li>Process the retrieved ds:Reference element according to the reference
	/// processing model of XMLDSIG.
	/// <li>If the result is a XML node set, canonicalize it. If
	/// <code>ds:Canonicalization</code> is present, the algorithm indicated by this
	/// element is used. If not, the standard canonicalization method specified by
	/// XMLDSIG is used
	/// <li>Concatenate the resulting octets to those resulting from previously
	/// processed <code>ds:Reference</code> elements in ds:SignedInfo.
	/// </ol>
	/// 
	/// @author ahmety
	/// date: Sep 29, 2009
	/// </summary>
	public class IndividualDataObjectsTimeStamp : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp
	{

		public IndividualDataObjectsTimeStamp(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		/// Construct GenericTimeStamp from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>

		public IndividualDataObjectsTimeStamp(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}


		public virtual void addEncapsulatedTimeStamp(XMLSignature aSignature, DigestMethod aDMForTimeStamp, TSSettings aAyar)
		{
			try
			{
				DigestMethod dm = (aDMForTimeStamp != null) ? aDMForTimeStamp : mContext.Config.AlgorithmsConfig.DigestMethod;
				byte[] bytes2TimeStamp = getContentForTimeStamp(aSignature);
				byte[] digested = KriptoUtil.digest(bytes2TimeStamp, dm);
				TSClient istemci = new TSClient();
			    TimeStampResp response = istemci.timestamp(digested, aAyar).getObject();

				EncapsulatedTimeStamp ets = new EncapsulatedTimeStamp(mContext, aSignature, this, response);
				addEncapsulatedTimeStamp(ets);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", "IndivualDataObjects");
			}
		}


		public virtual void addEncapsulatedTimeStamp(XMLSignature aSignature)
		{
			TimestampConfig tsConfig = mContext.Config.TimestampConfig;
			DigestMethod digestMethod = tsConfig.DigestMethod;
			TSSettings tsSettings = tsConfig.Settings;
			addEncapsulatedTimeStamp(aSignature, digestMethod, tsSettings);
		}


		public override byte[] getContentForTimeStamp(XMLSignature aSignature)
		{
            MemoryStream ms = new MemoryStream();
            BinaryWriter bw = new BinaryWriter(ms);
			SignedInfo signedInfo = aSignature.SignedInfo;

			/*
			Repeat steps for all the subsequent Include elements (in their order of
			appearance) within the time-stamp token container.
			*/
			for (int j = 0; j < IncludeCount; j++)
			{

				Include include = getInclude(j);

				string id2seek = include.URI.Substring(1);
				Document doc = null;

				/*
				Check that the retrieved element is actually a ds:Reference element
				of the ds:SignedInfo of the qualified signature(we do that by iterating
				references, instead of using resolvers!)
				*/
				for (int i = 0; i < signedInfo.ReferenceCount;i++)
				{
					Reference reference = signedInfo.getReference(i);
					if (id2seek.Equals(reference.Id))
					{

						/*
						Check that that its Type attribute (if present) does not have
						the value "http://uri.etsi.org/01903#SignedProperties".
						*/
						string type = reference.Type;
						if (type != null && type.Equals(Constants.REFERENCE_TYPE_SIGNED_PROPS))
						{
							throw new XMLSignatureException("core.model.individualDataObjectsCantReferenceQP");
						}

						/*
						If the retrieved data is a ds:Reference element and the
						referencedData attribute is set to the value "true", take
						the result of processing the retrieved ds:Reference element
						according to the reference processing model of XMLdSIG;
						otherwise take the ds:Reference element itself.
	
						If the resulting data is an XML node set, canonicalize it.
						If ds:Canonicalization is present, the algorithm indicated
						by this element is used. If not, the standard
						canonicalization method specified by XMLDSIG is used.
						*/
						bool ?referenced = include.ReferencedData;
					    bool isTrue = false;
						if (referenced != null && referenced == true)
						{
                                isTrue = true;					
						}
                        if (isTrue)
                        {
                            doc = reference.getTransformedDocument();
                        }else
						{
							doc = new DOMDocument(reference.Element, reference.URI);
						}
					}
				}

				if (doc == null)
				{
					throw new XMLSignatureException("errors.cantFind", id2seek);
				}

				//Concatenate the resulting bytes in an octet stream.
				try
				{
					bw.Write(doc.Bytes);
				}
				catch (Exception x)
				{
					throw new XMLSignatureException(x, "errors.cantDigest", id2seek);
				}
			}

		    return ms.ToArray();

		}

        public override TimestampType getType()
        {
            return TimestampType.CONTENT_TIMESTAMP;
        }

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_INDIVIDUALDATAOBJECTSTIMESTAMP;
			}
		}
	}

}