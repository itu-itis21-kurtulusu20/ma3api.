using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using DataType = tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
    using EConstants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using KeyInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo;
	using Transforms = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
	using Resolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


    /// <summary>
    /// <p>A RetrievalMethod element within KeyInfo is used to convey a reference to
    /// KeyInfo information that is stored at another location.</p>
    /// 
    /// <p>RetrievalMethod uses the same syntax and dereferencing behavior as
    /// Reference's URI, and "The Reference Processing Model" except that there is
    /// no DigestMethod or DigestValue child elements and presence of the URI is
    /// mandatory.
    /// 
    /// <p>Type is an optional identifier for the type of data retrieved after all
    /// transforms have been applied. The result of dereferencing a RetrievalMethod
    /// Reference for all KeyInfo types defined by this specification with a
    /// corresponding XML structure is an XML element or document with that element
    /// as the root. The rawX509Certificate KeyInfo (for which there is no XML
    /// structure) returns a binary X509 certificate.
    /// 
    /// <p>The following schema fragment specifies the expected content contained
    /// within this class.
    /// <p/>
    /// <pre>
    /// &lt;complexType name="RetrievalMethodType">
    ///   &lt;complexContent>
    ///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
    ///       &lt;sequence>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Transforms" minOccurs="0"/>
    ///       &lt;/sequence>
    ///       &lt;attribute name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
    ///       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
    ///     &lt;/restriction>
    ///   &lt;/complexContent>
    /// &lt;/complexType>
    /// </pre>
    /// @author ahmety
    /// date: Jun 10, 2009
    /// </summary>
    public class RetrievalMethod : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyInfoElement
	{
		public static readonly string TYPE_RAWX509 = EConstants.NS_XMLDSIG + "rawX509Certificate";


		private readonly string mURI;
		private readonly string mType;

		private readonly Transforms mTransforms;
		private byte[] mRawX509;

		/// <summary>
		/// Construct RetrievalMethod from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///                               resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public RetrievalMethod(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public RetrievalMethod(Element aElement, Context aContext) : base(aElement, aContext)
		{

            mURI = getAttribute(aElement, EConstants.ATTR_URI);
            mType = getAttribute(aElement, EConstants.ATTR_TYPE);

			Element element = XmlCommonUtil.getNextElement(aElement.FirstChild);
            if (element != null && EConstants.TAG_TRANSFORMS.Equals(element.LocalName))
			{
				mTransforms = new Transforms(element, mContext);
			}
		}

		public virtual string URI
		{
			get
			{
				return mURI;
			}
		}

		public virtual string Type
		{
			get
			{
				return mType;
			}
		}

		public virtual Transforms Transforms
		{
			get
			{
				return mTransforms;
			}
		}

		/// <summary>
		/// Dereference RetrievalMethod URI, appy transforms and </summary>
		/// <returns> final KeyInfo (rmation) Element, returns itself if Retrieval
		///         Method is of type #rawX509Certificate </returns>
		/// <exception cref="XMLSignatureException"> if any error occurred over
		///                               derefencing, transformation and final construction phase </exception>
		public virtual KeyInfoElement resolve()
		{
			Element found;
			Document doc;
			try
			{
				doc = Resolver.resolve(mURI, mContext);
				if (mTransforms != null)
				{
					doc = mTransforms.apply(doc);
				}
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.cantResolveRetrievalMethod", mURI);
			}
			if (doc == null)
			{
				throw new XMLSignatureException("core.cantResolveRetrievalMethod", mURI);
			}


			if (TYPE_RAWX509.Equals(mType))
			{
				mRawX509 = doc.Bytes;
				return this;
			}

			/*
			The result of dereferencing a RetrievalMethod Reference for all KeyInfo
			types defined by this specification (section 4.4) with a corresponding
			XML structure is an XML element or document with that element as the
			root.
			 */
			if (doc.Type == DataType.NODESET)
			{
				found = (Element) doc.NodeList.Item(0);
				return KeyInfo.resolve(found, mContext);
			}

			throw new XMLSignatureException("core.invalidRetrievalMethod", doc);

		}

		public virtual byte[] RawX509
		{
			get
			{
				return mRawX509;
			}
		}

		//base element
		public override string LocalName
		{
			get
			{
                return EConstants.TAG_RETRIEVALMETHOD;
			}
		}
	}

}