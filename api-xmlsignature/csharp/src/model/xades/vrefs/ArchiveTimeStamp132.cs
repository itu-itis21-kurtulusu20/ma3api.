using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
	using SignedInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
	using XMLObject = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.XMLObject;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;


	/// <summary>
	/// Deprecated in 1.4.1.
	/// 
	/// Only for backwards compatibility. Newly created signatures, should not use this one.
	/// 
	/// @author ahmety
	/// date: Mar 28, 2011
	/// </summary>
	public class ArchiveTimeStamp132 : ArchiveTimeStamp, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public ArchiveTimeStamp132(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public ArchiveTimeStamp132(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public byte[] getContentForTimeStamp(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override byte[] getContentForTimeStamp(XMLSignature aSignature)
		{
			try
			{
				// init
                MemoryStream ms = new MemoryStream();
                BinaryWriter bs = new BinaryWriter(ms);

				SignedInfo signedInfo = aSignature.SignedInfo;

				// add all references resolved
				for (int i = 0; i < signedInfo.ReferenceCount; i++)
				{
					Reference @ref = signedInfo.getReference(i);
					Document doc = @ref.getTransformedDocument(mCanonicalizationMethod);
					bs.Write(doc.Bytes);
				}

				// signedInfo, signatureValue, keyInfo
				//bs.Write(signedInfo.getCanonicalizedBytes());
				bs.Write(XmlUtil.outputDOM(signedInfo.Element, mCanonicalizationMethod));
				bs.Write(XmlUtil.outputDOM(aSignature.SignatureValueElement, mCanonicalizationMethod));
				if (aSignature.KeyInfo != null)
				{
				bs.Write(XmlUtil.outputDOM(aSignature.KeyInfo.Element, mCanonicalizationMethod));
				}

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
					bs.Write(XmlUtil.outputDOM(((BaseElement)unsignedProperty).Element, mCanonicalizationMethod));
				}

				// Take any ds:Object element in the signature that is not referenced
				// by any ds:Reference within ds:SignedInfo, except that one containing
				// the QualifyingProperties element
				XMLObject qpObject = aSignature.QualifyingPropertiesObject;
				for (int j = 0; j < aSignature.ObjectCount; j++)
				{
					XMLObject @object = aSignature.getObject(j);
					if (!@object.Equals(qpObject) && !isObjectReferenced(@object, signedInfo))
					{
						bs.Write(XmlUtil.outputDOM(@object.Element, mCanonicalizationMethod));
					}
				}

			    return ms.ToArray();
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantDigest", LocalName);
			}
		}

		private bool isObjectReferenced(XMLObject aObject, SignedInfo aSignedinfo)
		{
			string objectId = aObject.Id;
			if (objectId == null || objectId.Length == 0)
			{
				return false;
			}

			for (int i = 0; i < aSignedinfo.ReferenceCount; i++)
			{
				Reference @ref = aSignedinfo.getReference(i);
				string uri = @ref.URI;
				if (uri[0] == '#' && uri.Length > 1 && uri.Substring(1).Equals(objectId))
				{
						return true;					
				}
			}
			return false;
		}

        public override TimestampType getType()
        {
            return TimestampType.ARCHIVE_TIMESTAMP;
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
				return Constants.NS_XADES_1_3_2;
			}
		}
	}


}