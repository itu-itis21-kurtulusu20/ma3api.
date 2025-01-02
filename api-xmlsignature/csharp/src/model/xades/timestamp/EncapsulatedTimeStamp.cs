using System;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp
{

	using Element = XmlElement;
	using EContentInfo = tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
	using ESignedData = tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
	using ETSTInfo = tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
	using EAlgorithmIdentifier = tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
	using DigestAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using EncapsulatedPKIData = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
	using AsnUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.AsnUtil;
	using ContentInfo = tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
	using SignedData = tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
	using TimeStampResp = tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;
	using TSTInfo = tr.gov.tubitak.uekae.esya.asn.pkixtsp.TSTInfo;
	using _pkixtspValues = tr.gov.tubitak.uekae.esya.asn.pkixtsp._pkixtspValues;
	using Asn1DerDecodeBuffer = Com.Objsys.Asn1.Runtime.Asn1DerDecodeBuffer;
    using Asn1ObjectIdentifier = Com.Objsys.Asn1.Runtime.Asn1ObjectIdentifier;


	/// <summary>
	/// Container for the time-stamp token in form of ASN.1 data object generated
	/// by TSA. 
	/// 
	/// @author ahmety
	/// date: Sep 29, 2009
	/// 
	/// todo ETSTInfo vs asn wrapper classlari kullan...
	/// </summary>
	public class EncapsulatedTimeStamp : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData
	{
	    private readonly XMLSignature signature;
	    private readonly GenericTimeStamp parent;
		private readonly EContentInfo mContentInfo;
		private ESignedData mSignedData;
		private ETSTInfo mTSTInfo;
		private DateTime ? mCalendar;

		public EncapsulatedTimeStamp(Context aContext, XMLSignature signature, XAdESTimeStamp parent, TimeStampResp aResponse) : base(aContext)
		{
		    this.signature = signature;
		    this.parent = parent;

			mContentInfo = new EContentInfo(aResponse.timeStampToken);
			try
			{
				Value = AsnUtil.encode(aResponse.timeStampToken, mEncoding);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.timestamp.cantEncode");
			}

            if (mContext.ValidateTimeStamps)
            {
                EncapsulatedTimeStampValidator validator = new EncapsulatedTimeStampValidator(signature, parent);
                ValidationResult vr = validator.verify(this, null);
                if ( !vr.getType().Equals(ValidationResultType.VALID) )
                {
                    //throw new XMLSignatureException("Cant validate timestamp " + LocalName + ", reason : " + vr.getMessage());
                    throw new XMLSignatureException(I18n.translate("validation.timestamp.failed", parent.LocalName, vr.getMessage()));
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
		public EncapsulatedTimeStamp(Element aElement, GenericTimeStamp parent, Context aContext) : base(aElement, aContext)
		{
		    this.parent = parent;
			ContentInfo contentInfo = new ContentInfo();
			//TimeStampResp resp = new TimeStampResp();
			AsnUtil.decode(contentInfo, mValue, mEncoding);
			mContentInfo = new EContentInfo(contentInfo);
			//mContentInfo = resp.timeStampToken;
		}

		public virtual EContentInfo ContentInfo
		{
			get
			{
				return mContentInfo;
			}
		}

		/*
		from RFC 3160 :
		    TimeStampToken ::= ContentInfo
		    -- contentType is id-signedData as defined in [CMS]
		    -- content is SignedData as defined in([CMS])
		    -- eContentType within SignedData is id-ct-TSTInfo
		    -- eContent within SignedData is TSTInfo
		*/
        public virtual ESignedData SignedData
		{
			get
			{
				if (mSignedData == null)
				{
					try
					{
						Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(mContentInfo.getContent());
						SignedData signedData = new SignedData();
						signedData.Decode(decBuf);
						mSignedData = new ESignedData(signedData);
    
					}
					catch (Exception x)
					{
						throw new XMLSignatureException(x, "core.timestamp.cantExtractSignedData");
					}
				}
				return mSignedData;
			}
		}

		public virtual bool TimeStamp
		{
			get
			{
				try
				{
					return SignedData.getEncapsulatedContentInfo().getContentType().Equals(new Asn1ObjectIdentifier(_pkixtspValues.id_ct_TSTInfo));
				}
				catch (Exception exc)
				{
                    Console.WriteLine(exc.StackTrace);
					return false;
				}
			}
		}

		public virtual ETSTInfo TSTInfo
		{
			get
			{
				if (mTSTInfo == null)
				{
					try
					{
						TSTInfo tstInfo = new TSTInfo();
						Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(SignedData.getEncapsulatedContentInfo().getContent());
						tstInfo.Decode(decBuf);
						mTSTInfo = new ETSTInfo(tstInfo);
					}
					catch (Exception x)
					{
						throw new XMLSignatureException(x, "core.timestamp.cantExtractTSTInfo");
					}
				}
				return mTSTInfo;
			}
		}

		public virtual byte[] DigestValue
		{
			get
			{
				return TSTInfo.getHashedMessage();
			}
		}

		public virtual DigestAlg DigestAlgorithm
		{
			get
			{
				try
				{
					EAlgorithmIdentifier tsHashAlg = TSTInfo.getHashAlgorithm();
					return DigestAlg.fromOID(tsHashAlg.getAlgorithm().mValue);
				}
				catch (Exception x)
				{
					throw new XMLSignatureException(x, "core.timestamp.cantExtractDigestAlg");
				}
			}
		}


		public virtual DateTime Time
		{
			get
			{
				if (mCalendar == null)
				{
					try
					{
						mCalendar = TSTInfo.getTime();
					}
					catch (Exception x)
					{
						throw new XMLSignatureException(x, "core.timestamp.cantExtractTime");
					}
				}
				return (DateTime) mCalendar;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ENCAPSULATEDTIMESTAMP;
			}
		}
	}

}