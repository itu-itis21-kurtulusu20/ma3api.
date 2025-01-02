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
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
	using SignedInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
	using XMLObject = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.XMLObject;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
	using TimeStampResp = tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

	/// <summary>
	/// <p>Archive validation data consists of the complete validation data and the
	/// complete certificate and revocation data, time-stamped together with the
	/// electronic signature. The Archive validation data is necessary if the hash
	/// function and the crypto algorithms that were used to create the signature are
	/// no longer secure. Also, if it cannot be assumed that the hash function used
	/// by the Time-Stamping Authority is secure, then nested time-stamps of archived
	/// electronic signature are required.   
	/// 
	/// <p>Nested time-stamps will also protect the verifier against key compromise
	/// or cracking the algorithm on the old electronic signatures.
	/// 
	/// <p>The process will need to be performed and iterated before the
	/// cryptographic algorithms used for generating the previous time-stamp are no
	/// longer secure. Archive validation data MAY thus bear multiple embedded
	/// time-stamps.
	/// 
	/// <p>The xadesv141:ArchiveTimeStamp element is an unsigned property qualifying
	/// the signature. Below follows the schema definition for this element.
	/// 
	/// <p><xsd:element name="ArchiveTimeStamp" type="XAdESTimeStampType"/>
	/// 
	/// <p>Should a CounterSignature unsigned property be time-stamped by the
	/// xadesv141:ArchiveTimeStamp, any ulterior change of their contents (by
	/// addition of unsigned properties if the counter-signature is a XAdES
	/// signature, for instance) would make the validation of the
	/// xadesv141:ArchiveTimeStamp, and in consequence of the countersigned XAdES
	/// signature, fail. Implementers SHOULD, in consequence, not change the contents
	/// of the CounterSignature property once it has been time-stamped by the
	/// xadesv141:ArchiveTimeStamp. Implementors MAY, in these circumstances, to make
	/// use of the detached counter-signature mechanism specified  (not supported!)
	/// 
	/// <p> In addition it has to be noted that the present document allows to
	/// counter-sign a previously time-stamped countersignature with another
	/// CounterSignature property added to the embedding XAdES signature after the
	/// time-stamp container.
	/// 
	/// <p>Depending whether all the unsigned properties covered by the time-stamp
	/// token and the xadesv141:ArchiveTimeStamp property itself have the same parent
	/// or not, its contents may be different. Details are given in clauses below.
	/// 
	///  <p>NOTE: Readers are warned that once an xadesv141:ArchiveTimeStamp property
	/// is added to the signature, any ulterior addition of a ds:Object to the
	/// signature, would make the verification of such time-stamp fail.
	/// 
	/// <p>When xadesv141:ArchiveTimeStamp and all the unsigned properties covered by
	/// its time-stamp token have the same parent, this property uses the Implicit
	/// mechanism for all the time-stamped data objects. The input to the computation
	/// of the digest value MUST be built as follows:
	/// <ol>
	/// <li>1) Initialize the final octet stream as an empty octet stream.
	/// <li>2) Take all the ds:Reference elements in their order of appearance within
	/// ds:SignedInfo referencing whatever the signer wants to sign including the
	/// SignedProperties element. Process each one as indicated below:
	/// <ul>
	/// <li>- Process the retrieved ds:Reference element according to the reference
	/// processing model of XMLDSIG.
	/// <li>- If the result is a XML node set, canonicalize it. If
	/// ds:Canonicalization is present, the algorithm indicated by this element is
	/// used. If not, the standard canonicalization method specified by XMLDSIG is
	/// used.
	/// <li>- Concatenate the resulting octets to the final octet stream.
	/// </ul>
	/// <li>3) Take the following XMLDSIG elements in the order they are listed below,
	/// canonicalize each one and concatenate each resulting octet stream to the
	/// final octet stream:
	/// <ul>
	/// <li>- The ds:SignedInfo element.
	/// <li>- The ds:SignatureValue element.
	/// <li>- The ds:KeyInfo element, if present.
	/// </ul>
	/// <li>4) Take the unsigned signature properties that appear before the current
	/// xadesv141:ArchiveTimeStamp in the order they appear within the
	/// xades:UnsignedSignatureProperties, canonicalize each one and concatenate each
	/// resulting octet stream to the final octet stream. While concatenating the
	/// following rules apply:
	/// <ul>
	/// <li>- The xades:CertificateValues property MUST be added if it is not already
	/// present and the ds:KeyInfo element does not contain the full set of
	/// certificates used to validate the electronic signature.
	/// <li>- The xades:RevocationValues property MUST be added if it is not already
	/// present and the ds:KeyInfo element does not contain the revocation
	/// information that has to be shipped with the electronic signature.
	/// <li>- The xades:AttrAuthoritiesCertValues property MUST be added if not already
	/// present and the following conditions are true: there exist an attribute
	/// certificate in the signature AND a number of certificates that have been used
	/// in its validation do not appear in CertificateValues. Its content will
	/// satisfy with the rules specified in clause 7.6.3.
	/// <li>- The xades:AttributeRevocationValues property MUST be added if not already
	/// present and there the following conditions are true: there exist an attribute
	/// certificate AND some revocation data that have been used in its validation do
	/// not appear in RevocationValues. Its content will satisfy with the rules
	/// specified in clause 7.6.4.
	/// </ul>
	/// <li>5) Take all the ds:Object elements except the one containing
	/// xades:QualifyingProperties element.
	/// Canonicalize each one and concatenate each resulting octet stream to the
	/// final octet stream. If ds:Canonicalization is present, the algorithm
	/// indicated by this element is used. If not, the standard canonicalization
	/// method specified by XMLDSIG is used.
	/// </ol>
	/// NOTE: XAdESv1.3.2 [12] specified a different strategy for concatenating
	/// ds:Object elements present within the signature. Following that strategy,
	/// when the last transformation of a certain ds:Reference is not a
	/// canonicalization transformation and its output is an octet stream, it is, in
	/// the general case, unfeasible to ascertain that this reference actually makes
	/// the signature to sign a certain ds:Object. The present document overcomes
	/// this situation by forcing that all the ds:Object elements other than the one
	/// containing the qualifying properties, are canonicalized and concatenated,
	/// which is simple although in enveloping signatures may lead to certain degree
	/// of redundancy in the digest computation input. fail.
	/// 
	/// 
	/// @author ahmety
	/// date: Mar 28, 2011
	/// </summary>
	public class ArchiveTimeStamp : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{
		public ArchiveTimeStamp(Context aContext, XMLSignature aSignature, DigestMethod aDMForTimestamp, TSSettings aAyar) : base(aContext)
		{

			string xmlnsXAdESPrefix = mContext.Config.NsPrefixMap.getPrefix(Namespace);
			mElement.SetAttribute("xmlns:" + String.Intern(xmlnsXAdESPrefix),Namespace);

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
				throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", Constants.TAGX_ARCHIVETIMESTAMP);
			}

		}

		public ArchiveTimeStamp(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override byte[] getContentForTimeStamp(XMLSignature aSignature)
		{
			try
			{
                MemoryStream ms = new MemoryStream();
                BinaryWriter bs = new BinaryWriter(ms);
				// init
				
				SignedInfo signedInfo = aSignature.SignedInfo;
				// todo
				//C14nMethod c14nMethod = signedInfo.getCanonicalizationMethod();
				C14nMethod c14nMethod = CanonicalizationMethod;

				// add all references resolved
				for (int i = 0; i < signedInfo.ReferenceCount; i++)
				{
					Reference @ref = signedInfo.getReference(i);
					Document doc = @ref.getTransformedDocument();
					bs.Write(doc.Bytes);
				}

				// signedInfo, signatureValue, keyInfo
				bs.Write(signedInfo.CanonicalizedBytes);
				bs.Write(XmlUtil.outputDOM(aSignature.SignatureValueElement, c14nMethod));
				bs.Write(XmlUtil.outputDOM(aSignature.KeyInfo.Element, c14nMethod));


				// Take the unsigned signature properties that appear before the
				// current xadesv141:ArchiveTimeStamp in the order they appear,
				// canonicalize each one and concatenate each
				UnsignedSignatureProperties usp = aSignature.QualifyingProperties.UnsignedSignatureProperties;
				IList<UnsignedSignaturePropertyElement> unsignedProperties = usp.Properties;
				foreach (UnsignedSignaturePropertyElement unsignedProperty in unsignedProperties)
				{
					if (unsignedProperty.Equals(this))
					{
						break;
					}
                    bs.Write(XmlUtil.outputDOM(((BaseElement)unsignedProperty).Element, c14nMethod));
				}

				// Take all the ds:Object elements except the one containing
				// xades:QualifyingProperties element
				XMLObject qpObject = aSignature.QualifyingPropertiesObject;
				for (int j = 0; j < aSignature.ObjectCount; j++)
				{
					XMLObject @object = aSignature.getObject(j);
					if (!@object.Equals(qpObject))
					{
                        bs.Write(XmlUtil.outputDOM(@object.Element, c14nMethod));
					}
				}

				//System.out.println("<< Content for archive time stamp >>\b"+new String(baos.toByteArray()));

			    return ms.ToArray();
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantDigest", LocalName);
			}
		}

        public override TimestampType getType()
        {
            return TimestampType.ARCHIVE_TIMESTAMP_V2;
        }

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ARCHIVETIMESTAMP;
			}
		}

		public override string Namespace
		{
			get
			{
				return Constants.NS_XADES_1_4_1;
			}
		}
	}


}