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
	using SignatureTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
	using TimeStampResp = tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;



    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// This property contains a time-stamp token that covers the following data
	/// objects: <code>ds:SignatureValue</code> element, all present
	/// <code>SignatureTimeStamp</code> elements,
	/// </code>CompleteCertificateRefs</code>, <code>CompleteRevocationRefs</code>,
	/// and when present, <code>AttributeCertificateRefs</code> and
	/// <code>AttributeRevocationRefs</code>.
	/// 
	/// <p>Depending whether all the aforementioned time-stamped unsigned properties
	/// and the <code>SigAndRefsTimeStamp</code> property itself have the same parent
	/// or not, its contents may be different. Details are given in clauses below.
	/// 
	/// <h3>Not distributed case</h3>
	/// 
	/// When <code>SigAndRefsTimeStamp</code> and all the unsigned properties covered
	/// by its time-stamp token have the same parent, this property uses the Implicit
	/// mechanism. The input to the computation of the digest value MUST be the
	/// result of taking in order each of the data objects listed below, canonicalize
	/// each one and concatenate the resulting octet streams:
	/// 
	/// <ol>
	/// <li>The <code>ds:SignatureValue</code> element.
	/// <li>Those among the following unsigned properties that appear before
	///  <code>SigAndRefsTimeStamp</code>, in their order of appearance within the
	///  <code>UnsignedSignatureProperties</code> element:
	///   <ul>
	///   <li><code>SignatureTimeStamp</code> elements.
	///   <li>The <code>CompleteCertificateRefs</code> element.
	///   <li>The <code>CompleteRevocationRefs</code> element.
	///   <li>The <code>AttributeCertificateRefs</code> element if this property is
	///      present.
	///   <li>The <code>AttributeRevocationRefs</code> element if this property is
	///      present.
	///   </ul>
	/// </ol>
	/// 
	/// <p>Below follows the list -in order- of data objects that contribute to the
	/// digest computation. Elements within [] contribute in their order of
	/// appearance within the <code>UnsignedSignatureProperties</code> element, not
	/// in the order they are enumerated below:
	/// 
	/// <p>(ds:SignatureValue, [SignatureTimeStamp+, CompleteCertificateRefs, 
	/// CompleteRevocationRefs, AttributeCertificateRefs?,
	/// AttributeRevocationRefs?]).
	/// 
	/// 
	/// <h3>Distributed case</h3>
	/// 
	/// When SigAndRefsTimeStamp and some of the unsigned properties covered by its
	/// time-stamp token DO NOT have the same parent, applications MUST build this
	/// property as indicated below:
	/// 
	/// <ol>
	/// <li>No <code>Include</code> element will be added for
	///  <code>ds:SignatureValue</code>. All applications MUST implicitly assume its
	/// contribution to the digest input (see below in this clause).
	/// <li>Generate one <code>Include</code> element per each unsigned property that
	/// MUST be covered by the time-stamp token in the order they appear listed
	/// below:
	///   <ul>
	///   <li>The SignatureTimeStamp elements.
	///   <li>The CompleteCertificateRefs element.
	///   <li>The CompleteRevocationRefs element.
	///   <li>The AttributeCertificateRefs element if this property is present.
	///   <li>The AttributeRevocationRefs element if this property is present.
	///   </ul>
	/// </ol>
	/// 
	/// <p>Applications MUST build URI attributes following the rules stated in
	/// clause 7.1.4.3.1.
	/// 
	/// <p>Generating applications MUST build digest computation input as indicated
	/// below:
	/// <ol>
	/// <li>Initialize the final octet stream as an empty octet stream.
	/// <li>Take the <code>ds:SignatureValue</code> element and its content.
	/// Canonicalize it and put the result in the final octet stream.
	/// <li>Take each unsigned property listed above in the order they have been
	/// listed above(this order MUST be the same as the order the
	/// <code>Include</code> elements appear in the property). For each one extract
	/// comment nodes, canonicalize and concatenate the resulting octet string to
	/// the final octet stream.
	/// </ol>
	/// 
	/// <p>Validating applications MUST build digest computation input as indicated
	/// below:
	/// <ol>
	/// <li>Initialize the final octet stream to empty.
	/// <li>Take the <code>ds:SignatureValue</code>. Canonicalize it and put the
	/// result in the final octet stream.
	/// <li>Process in order each <code>Include</code> element present as indicated
	/// in clause 7.1.4.3.1. Concatenate the resulting octet strings to the final
	/// octet stream.
	/// </ol>
	/// 
	/// <p>Below follows the list of the data objects that contribute to the digest
	/// computation. Super index e means that this property is referenced using
	/// explicit mechanism, i.e. that the property contains an <code>Include</code>
	/// element that references it:
	/// 
	/// <p>(ds:SignatureValue, SignatureTimeStamp e+, CompleteCertificateRefs e,
	///  CompleteRevocationRefs e, AttributeCertificateRefs e?,
	/// AttributeRevocationRefs e?).
	/// 
	/// @author ahmety
	/// date: Dec 17, 2009
	/// </summary>
	public class SigAndRefsTimeStamp : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SigAndRefsTimeStamp(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature, tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod aDMForTimestamp, tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings aAyar) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SigAndRefsTimeStamp(Context aContext, XMLSignature aSignature, DigestMethod aDMForTimestamp, TSSettings aAyar) : base(aContext)
		{
			addLineBreak();

			try
			{
				DigestMethod dm = aDMForTimestamp != null ? aDMForTimestamp : mContext.Config.TimestampConfig.DigestMethod;
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
		/// Construct SigAndRefsTimeStamp from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SigAndRefsTimeStamp(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SigAndRefsTimeStamp(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override byte[] getContentForTimeStamp(XMLSignature aSignature)
		{
            MemoryStream ms = new MemoryStream();
            BinaryWriter bw = new BinaryWriter(ms);
		/*
			Below follows the list -in order- of data objects that contribute to the
			digest computation. Elements within [] contribute in their order of
			appearance within the <code>UnsignedSignatureProperties</code> element,
			not in the order they are enumerated below:
	
			(ds:SignatureValue, [SignatureTimeStamp+, CompleteCertificateRefs,
			 CompleteRevocationRefs, AttributeCertificateRefs?,
			 AttributeRevocationRefs?]).
			 */
			try
			{
				Element signatureValueElement = aSignature.SignatureValueElement;
				byte[] bytes = XmlUtil.outputDOM(signatureValueElement, mCanonicalizationMethod);
				bw.Write(bytes);

				UnsignedSignatureProperties usp = aSignature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties;

				IList<UnsignedSignaturePropertyElement> elements = usp.Properties;
				foreach (UnsignedSignaturePropertyElement element in elements)
				{
					if (element is SignatureTimeStamp)
					{
						SignatureTimeStamp sts = (SignatureTimeStamp)element;
						bytes = XmlUtil.outputDOM(sts.Element, mCanonicalizationMethod);
                        bw.Write(bytes);
					}
					else if (element is CompleteCertificateRefs)
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
			//System.out.println("SigAndRefs: "+new String(baos.toByteArray()));
		    return ms.ToArray();
		}

        public override TimestampType getType()
        {
            return TimestampType.SIG_AND_REFERENCES_TIMESTAMP;
        }

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_SIGANDREFSTIMESTAMP;
			}
		}
	}

}