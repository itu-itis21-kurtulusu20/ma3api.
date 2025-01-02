using System;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.core;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using TSClient = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
	using TSSettings = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using TimestampConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
	using SignedInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
	using TimeStampResp = tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;
    

	/// <summary>
	/// <p>The <code>AllDataObjectsTimeStamp</code> element contains the time-stamp
	/// computed before the signature production, over the sequence formed by ALL
	/// the <code>ds:Reference</code> elements within the <code>ds:SignedInfo</code>
	/// referencing whatever the signer wants to sign except the
	/// <code>SignedProperties</code> element.
	/// 
	/// <p>The <code>AllDataObjectsTimeStamp</code> element is a signed property.
	/// Several instances of this property from different TSAs can occur within the
	/// same XAdES.
	/// 
	/// <p>Below follows the schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="AllDataObjectsTimeStamp" type="XAdESTimeStampType"/>
	/// </pre>
	/// 
	/// <p>This property uses the Implicit mechanism. The input to the computation
	/// of the digest value MUST be the result of processing the aforementioned
	/// suitable <code>ds:Reference</code> elements in their order of appearance
	/// within <code>ds:SignedInfo</code> as follows:
	/// <ol>
	/// <li>Process the retrieved <code>ds:Reference<code> element according to
	/// the reference processing model of XMLDSIG.
	/// <li>If the result is a XML node set, canonicalize it. If
	/// <code>ds:Canonicalization</code> is present, the algorithm indicated by this
	/// element is used. If not, the standard canonicalization method specified by
	/// XMLDSIG is used.
	/// <li>Concatenate the resulting octets to those resulting from previously
	/// processed <code>ds:Reference</code> elements in <code>ds:SignedInfo</code>.
	/// </ol>
	/// 
	/// @author ahmety
	/// date: Sep 29, 2009
	/// </summary>
	public class AllDataObjectsTimeStamp : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp
	{
        public AllDataObjectsTimeStamp(Context aContext, XMLSignature aSignature) : this(aContext, aSignature, aContext.Config.TimestampConfig.DigestMethod, aContext.Config.TimestampConfig.Settings)
		{
		}

        public AllDataObjectsTimeStamp(Context aContext, XMLSignature aSignature, DigestMethod aDMForTimestamp, TSSettings aAyar) : base(aContext)
		{
			addLineBreak();

			try
			{
				TimestampConfig tsConfig = mContext.Config.TimestampConfig;
				DigestMethod dm = aDMForTimestamp != null ? aDMForTimestamp : tsConfig.DigestMethod;
				byte[] bytes2TimeStamp = getContentForTimeStamp(aSignature);
				byte[] digested = KriptoUtil.digest(bytes2TimeStamp, dm);

				TSSettings tsSettings = aAyar != null? aAyar : tsConfig.Settings;

				TSClient istemci = new TSClient();
				TimeStampResp response = istemci.timestamp(digested, tsSettings).getObject();

				EncapsulatedTimeStamp ets = new EncapsulatedTimeStamp(mContext, aSignature, this, response);
				addEncapsulatedTimeStamp(ets);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", "AllDataObjects");
			}

		}

		/// <summary>
		/// Construct GenericTimeStamp from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
		public AllDataObjectsTimeStamp(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override byte[] getContentForTimeStamp(XMLSignature aSignature)
		{
			SignedInfo signedInfo = aSignature.SignedInfo;
            MemoryStream ms= new MemoryStream();
            BinaryWriter bw = new BinaryWriter(ms);
			//ByteArrayOutputStream bis = new ByteArrayOutputStream();
			/*
			Repeat for all the subsequent ds:Reference elements (in their order of
			appearance) within ds:SignedInfo if and only if Type attribute has not
			the value "http://uri.etsi.org/01903#SignedProperties".
			*/
			for (int i = 0; i < signedInfo.ReferenceCount; i++)
			{
				Reference @ref = signedInfo.getReference(i);

				//dont process SignedProperties
				if ((@ref.Type == null) || (!@ref.Type.Equals(Constants.REFERENCE_TYPE_SIGNED_PROPS)))
				{
					/*
					Process it according to the reference processing model of XMLDSIG.
	
					If the result is a node-set, canonicalize it using the algorithm
					indicated in CanonicalizationMethod element of the property, if
					present. If not, the standard canonicalization method as specified
					by XMLDSIG will be used.
					*/
					Document doc = @ref.getTransformedDocument();

					//Concatenate the resulting bytes in an octet stream.
					try
					{
                        bw.Write(doc.Bytes);
					}
					catch (Exception x)
					{
						throw new XMLSignatureException(x, "errors.cantDigest", "AllDataObjectTimeStamp");
					}
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
				return Constants.TAGX_ALLDATAOBJECTSTIMESTAMP;
			}
		}
	}

}