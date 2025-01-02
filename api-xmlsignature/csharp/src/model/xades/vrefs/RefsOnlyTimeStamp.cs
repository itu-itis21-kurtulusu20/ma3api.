using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using TSClient = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
	using TSSettings = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
	using TimeStampResp = tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;


	/// 
	/// <summary>
	/// <h3>Not distributed case</h3>
	/// 
	/// <p>When <code>RefsOnlyTimeStamp</code> and all the unsigned properties covered
	/// by its time-stamp token have the same parent, this property uses the
	/// Implicit mechanism. The input to the computation of the digest value MUST be
	/// the result of taking those of the unsigned properties listed below that
	/// appear before the <code>RefsOnlyTimeStamp</code> in their order of appearance
	/// within the <code>UnsignedSignatureProperties</code> element, canonicalize
	/// each one and concatenate the resulting octet streams:
	/// <ul>
	/// <li>The CompleteCertificateRefs element.
	/// <li>The CompleteRevocationRefs element.
	/// <li>The AttributeCertificateRefs element if this property is present.
	/// <li>The AttributeRevocationRefs element if this property is present.
	/// </ul>
	/// <p>Below follows the list of data objects that contribute to the digest
	/// computation:
	/// ([CompleteCertificateRefs, CompleteRevocationRefs,
	///   AttributeCertificateRefs?, AttributeRevocationRefs?]).
	/// 
	/// <h3>Distributed case</h3>
	/// 
	/// <p>When RefsOnlyTimeStamp and some of the unsigned properties covered by its
	/// time-stamp token DO NOT have the same parent, applications MUST build this
	/// property generating one <code>Include</code> element per each unsigned
	/// property that must be covered by the time-stamp token in the order they
	/// appear listed below:
	/// <ul>
	/// <li>The CompleteCertificateRefs element.
	/// <li>The CompleteRevocationRefs element.
	/// <li>The AttributeCertificateRefs element if this property is present.
	/// <li>The AttributeRevocationRefs element if this property is present.
	/// </ul>
	/// 
	/// <p>Applications MUST build URI attributes following the rules stated in
	/// clause 7.1.4.3.1.
	/// 
	/// <p>Generating applications MUST build digest computation input as indicated
	/// below:
	/// <ol>
	/// <li>Initialize the final octet stream as an empty octet stream.
	/// <li>Take each unsigned property listed above in the order they have been
	/// listed above (this order MUST be the same as the order the
	/// <code>Include</code> elements appear in the property). For each one extract
	/// comment nodes, canonicalize and concatenate the resulting octet stream to
	/// the final octet stream.
	/// </ol>
	/// 
	/// <p>Validating applications MUST build digest computation input as indicated
	/// below:
	/// <ol>
	/// <li>Initialize the final octet stream as an empty octet stream.
	/// <li>Process in order each <code>Include</code> element present as indicated
	/// in clause 7.1.4.3.1. Concatenate the resulting octet stream to the final
	/// octet stream.
	/// </ol>
	/// 
	/// <p>Below follows the list -in order- of the data objects that contribute to
	/// the digest computation. Superindex e means that this property is referenced
	/// using explicit mechanism, i.e. that the property contains a
	/// <code>Include</code> element that references it:
	/// 
	/// ( CompleteCertificateRefs e, CompleteRevocationRefs e,
	///   AttributeCertificateRefs e?, AttributeRevocationRefs e?).
	/// 
	/// @author ahmety
	/// date: Dec 17, 2009
	/// </summary>
	public class RefsOnlyTimeStamp : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public RefsOnlyTimeStamp(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature, tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod aDMForTimestamp, tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings aAyar) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public RefsOnlyTimeStamp(Context aContext, XMLSignature aSignature, DigestMethod aDMForTimestamp, TSSettings aAyar) : base(aContext)
		{
			addLineBreak();

			try
			{
				DigestMethod dm = aDMForTimestamp != null ? aDMForTimestamp : mContext.Config.AlgorithmsConfig.DigestMethod;
				byte[] bytes2TimeStamp = getContentForTimeStamp(aSignature);
				byte[] digested = KriptoUtil.digest(bytes2TimeStamp, dm);
				TSClient istemci = new TSClient();
				TimeStampResp response = istemci.timestamp(digested, aAyar).getObject();

				EncapsulatedTimeStamp ets = new EncapsulatedTimeStamp(mContext, aSignature, this, response);
				addEncapsulatedTimeStamp(ets);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", LocalName);
			}

		}

		/// <summary>
		/// Construct GenericTimeStamp from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public RefsOnlyTimeStamp(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public RefsOnlyTimeStamp(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public byte[] getContentForTimeStamp(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override byte[] getContentForTimeStamp(XMLSignature aSignature)
		{
            MemoryStream ms = new MemoryStream();
            BinaryWriter bw = new BinaryWriter(ms);
			/*
			elow follows the list of data objects that contribute to the digest
			computation:
			( [CompleteCertificateRefs, CompleteRevocationRefs,
			   AttributeCertificateRefs?, AttributeRevocationRefs?] ) .
			 */
			try
			{

				UnsignedSignatureProperties usp = aSignature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties;
				byte[] bytes;

				IList<UnsignedSignaturePropertyElement> elements = usp.Properties;
				foreach (UnsignedSignaturePropertyElement element in elements)
				{
					/*if (element instanceof SignatureTimeStamp){
					    SignatureTimeStamp sts = (SignatureTimeStamp)element;
					    C14nMethod c14n = sts.getCanonicalizationMethod();
					    bytes = XmlUtil.outputDOM(sts.getElement(), c14n);
					    baos.write(bytes);
					}
					else*/	 if (element is CompleteCertificateRefs)
	 {
						CompleteCertificateRefs ccr = (CompleteCertificateRefs)element;
						bytes = XmlUtil.outputDOM(ccr.Element, mCanonicalizationMethod);
						bw.Write(bytes);
	 }
					else if (element is CompleteRevocationRefs)
					{
						CompleteRevocationRefs crf = (CompleteRevocationRefs)element;
						bytes = XmlUtil.outputDOM(crf.Element, mCanonicalizationMethod);
                        bw.Write(bytes);

					}
					else if (element is AttributeCertificateRefs)
					{
						AttributeCertificateRefs acr = ((AttributeCertificateRefs) element);
						bytes = XmlUtil.outputDOM(acr.Element, mCanonicalizationMethod);
                        bw.Write(bytes);
					}
					else if (element is AttributeRevocationRefs)
					{
						AttributeRevocationRefs arr = (AttributeRevocationRefs) element;
						bytes = XmlUtil.outputDOM(arr.Element, mCanonicalizationMethod);
                        bw.Write(bytes);

					}
				}
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", LocalName);
			}
			return ms.ToArray();
		}

        public override TimestampType getType()
        {
            return TimestampType.REFERENCES_TIMESTAMP;
        }

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_REFSONLYTIMESTAMP;
			}
		}
	}

}