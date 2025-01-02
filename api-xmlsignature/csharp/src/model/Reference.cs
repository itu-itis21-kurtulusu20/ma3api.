using System;
using System.IO;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using StringUtil = tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

	using Logger = log4net.ILog;
	using Element = XmlElement;	
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;	
	using Resolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;	
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	/// <summary>
	/// <p><code>Reference</code> is an element that may occur one or more times. It
	/// specifies a digest algorithm and digest value, and optionally an identifier
	/// of the object being signed, the type of the object, and/or a list of
	/// transforms to be applied prior to digesting. The identification (URI) and
	/// transforms describe how the digested content (i.e., the input to the digest
	/// method) was created. The <code>Type</code> attribute facilitates the
	/// processing of referenced data. For example, while this specification makes no
	/// requirements over external data, an application may wish to signal that the
	/// referent is a <code>Manifest</code>. An optional ID attribute permits a
	/// <code>Reference</code> to be referenced from elsewhere.</p>
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="ReferenceType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence>
	///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Transforms" minOccurs="0"/>
	///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestMethod"/>
	///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestValue"/>
	///       &lt;/sequence>
	///       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
	///       &lt;attribute name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
	///       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 10, 2009
	/// </summary>
	public class Reference : BaseElement
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(Reference));

		private Transforms mTransforms;
		private bool transformsAdded;

		private DigestMethod mDigestMethod;
	    private readonly Manifest mReferencedManifest;
		private byte[] mDigestValue;
		private string mURI;
		private string mType;

		public Document mReferencedDocument;
		public Document mTransformedDocument;

        public static readonly string MANIFEST_URI = Constants.NS_XMLDSIG + Constants.TAG_MANIFEST;

		// internal
		private readonly Element mDigestMethodElement, mDigestValueElement;

		public Reference(Context aBaglam) : base(aBaglam)
		{
			addLineBreak();

            mDigestMethodElement = insertElement(Constants.NS_XMLDSIG, Constants.TAG_DIGESTMETHOD);
            mDigestValueElement = insertElement(Constants.NS_XMLDSIG, Constants.TAG_DIGESTVALUE);
		}

		/// <summary>
		///  Construct Reference from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public Reference(Element aElement, Context aContext) : base(aElement, aContext)
		{

            mURI = getAttribute(aElement, Constants.ATTR_URI);
            mType = getAttribute(aElement, Constants.ATTR_TYPE);

			Element element = XmlCommonUtil.getNextElement(aElement.FirstChild);
            if (Constants.TAG_TRANSFORMS.Equals(element.LocalName))
			{
				mTransforms = new Transforms(element, mContext);
				element = XmlCommonUtil.getNextElement(element.NextSibling);
				transformsAdded = true;
			}

			mDigestMethodElement = element;
            string digestMethodAlg = getAttribute(mDigestMethodElement, Constants.ATTR_ALGORITHM);
			mDigestMethod = DigestMethod.resolve(digestMethodAlg);

			mDigestValueElement = XmlCommonUtil.getNextElement(mDigestMethodElement.NextSibling);
			mDigestValue = XmlCommonUtil.getBase64DecodedText(mDigestValueElement);



			if (MANIFEST_URI.Equals(mType))
			{
				Element manifestElement = XmlCommonUtil.findByIdAttr(Document.DocumentElement, mURI.Substring(1));
				mReferencedManifest = new Manifest(manifestElement, aContext);
			}
		}

        public Document getReferencedDocument()
		{
			if (mReferencedDocument == null)
			{
				try
				{
					mReferencedDocument = Resolver.resolve(this, mContext);
				}
				catch (XMLSignatureException e)
				{
					logger.Error("Error occurred while deferencing " + mURI, e);
					throw e;
				}
			}
			return mReferencedDocument;
		}


		/// <summary>
		/// apply transforms, if resulting document is NodeSet canonicalize it using
		/// default c14nMethod
		/// </summary>
		/// <returns> transforms applied document </returns>
		public Document getTransformedDocument()
		{
			return getTransformedDocument(C14nMethod.INCLUSIVE);
		}

		public virtual Document getTransformedDocument(C14nMethod aC14nMethod)
		{
            //TODO java tarafi ile ayni hale gelmeli.
            if (mTransformedDocument == null)
            {
                if (mTransforms != null)
                {
                    mTransformedDocument = mTransforms.apply(getReferencedDocument());
                }
                else
                {
                    mTransformedDocument = getReferencedDocument();
                }

                /*
                Users may specify alternative transforms that override these
                defaults in transitions between transforms that expect different
                inputs. !!!The final octet stream contains the data octets!!! being
                secured. The digest algorithm specified by DigestMethod is then
                applied to these data octets, resulting in the DigestValue.
                */
                //* burasi kapaliydi onceden
                /*if (mTransformedDocument.GetType().IsAssignableFrom(typeof (DOMDocument)))
                {
                    DOMDocument dom = (DOMDocument) mTransformedDocument;
                    InMemoryDocument soc = (InMemoryDocument)dom.getTransformedDocument(aC14nMethod);
                    return soc;
                }//*/

                if (mTransformedDocument.Type == DataType.NODESET)
                {
                    byte[] bytes = XmlUtil.outputDOM(mTransformedDocument.NodeList, aC14nMethod);
                    mTransformedDocument = new InMemoryDocument(bytes, mTransformedDocument.URI,
                                                                mTransformedDocument.MIMEType,
                                                                mTransformedDocument.Encoding);
                }
                else if (mTransformedDocument.Type == DataType.NODE)
                {
                    byte[] bytes = XmlUtil.outputDOM((XmlNode)mTransformedDocument.C14NObject, aC14nMethod);
                    mTransformedDocument = new InMemoryDocument(bytes, mTransformedDocument.URI,
                                                                mTransformedDocument.MIMEType,
                                                                mTransformedDocument.Encoding);
                }
            }
		    return mTransformedDocument;
		}

		public Transforms getTransforms()
		{
			return mTransforms;
		}

        public void setTransforms(Transforms aTransforms)
        {
			if (transformsAdded)
			{
				throw new XMLSignatureRuntimeException("Transforms already added!");
			}
			transformsAdded = true;
			mTransforms = aTransforms;
            
			mElement.InsertBefore(aTransforms.Element, mDigestMethodElement);
			mElement.InsertBefore(Document.CreateTextNode("\n"), mDigestMethodElement);
		}


		public virtual DigestMethod DigestMethod
		{
			get
			{
				return mDigestMethod;
			}
			set
			{
				mDigestMethodElement.SetAttribute(Constants.ATTR_ALGORITHM,null, value.Url);
				mDigestMethod = value;
			}
		}


		public virtual byte[] DigestValue
		{
			get
			{
				return mDigestValue;
			}
			set
			{
                XmlCommonUtil.setBase64EncodedText(mDigestValueElement, value);
				mDigestValue = value;
			}
		}

		public virtual void generateDigestValue()
		{
			try
			{
                /*
                System.Security.Cryptography.Xml.Reference netReference = new System.Security.Cryptography.Xml.Reference();
                netReference.LoadXml(mElement);
                byte[] digestValue = netReference.DigestValue;
                */
			    Document doc = getTransformedDocument();
				byte[] bytes;
				if (logger.IsDebugEnabled)
				{
					logger.Debug("Generate digest for reference id :" + mId);
					logger.Debug("Reference.uri :" + mURI);
					logger.Debug("Reference.digestMethod :" + mDigestMethod.Algorithm);
					logger.Debug("Reference.data (trimmed) :" + StringUtil.substring(doc.Bytes, 256));
				}

	/*if (doc.getType().equals(DataType.OCTETSTREAM))
	            {
	                bytes = KriptoUtil.digest(doc.getStream(), mDigestMethod);
	            }
	            else*/
				{
					bytes = KriptoUtil.digest(doc.Bytes, mDigestMethod);
				}
				if (logger.IsDebugEnabled)
				{
                    logger.Debug("Generated digest is :" + System.Text.Encoding.ASCII.GetString(Base64.Encode(bytes)));
				}
				DigestValue = bytes;
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantDigest", "Reference(id:'" + mId + "', uri:'" + mURI + ")");
			}
		}



        private void FixupNamespaceNodes(XmlElement src, XmlElement dst, bool ignoreDefault)
        {
            // add namespace nodes
            foreach (XmlAttribute attr in src.SelectNodes("namespace::*"))
            {
                if (attr.LocalName == "xml")
                    continue;
                if (ignoreDefault && attr.LocalName == "xmlns")
                    continue;
                dst.SetAttributeNode(dst.OwnerDocument.ImportNode(attr, true) as XmlAttribute);
            }
        }

		public virtual bool validateDigestValue()
		{
			try
			{
				Document doc = getTransformedDocument();
				if (logger.IsDebugEnabled)
				{
					logger.Debug("Validating reference id :" + mId);
                    logger.Debug("Reference.uri :" + mURI);
                    logger.Debug("Reference.digestMethod :" + mDigestMethod.Algorithm);
                    logger.Debug("Reference.data (trimmed) :" + StringUtil.substring(doc.Bytes, 256 ));
				}

				byte[] bytes;

			    //Console.WriteLine(ASCIIEncoding.ASCII.GetString(Base64.Encode(doc.Bytes)));
			    //Console.WriteLine("-------------------------------------------------------");
			    //Console.WriteLine(ASCIIEncoding.ASCII.GetString(doc.Bytes));

                //AsnIO.dosyayaz(doc.Bytes, @"D:\Projeler\API\Destek\Vakifbank Xades\CSharp_Transformed.xml");


				bytes = KriptoUtil.digest(doc.Bytes, mDigestMethod);

                if (logger.IsDebugEnabled)
				{
                    logger.Debug("Original digest: " + /*Base64.Encode(DigestValue)*/System.Text.Encoding.ASCII.GetString(Base64.Encode(DigestValue)));
                    logger.Debug("Calculated digest: " + /*Base64.Encode(bytes)*/System.Text.Encoding.ASCII.GetString(Base64.Encode(bytes)));
				}
                
				bool digestOk = ArrayUtil.Equals(bytes, DigestValue);
				if (!digestOk)
				{
					logger.Info("Reference( id :'" + mId + "' uri: '" + mURI + "' could not be validated.");
					return false;
				}
				else
				{
                    logger.Info("Reference( id :'" + mId + "' uri: '" + mURI + "' validated.");
				}
			}
			catch (Exception x)
			{
                logger.Info("Referans( id :'" + mId + "' uri: '" + mURI + "' could not be processed.", x);
				return false;
			}
			return true;
		}

		public virtual string URI
		{
			get
			{
				return mURI;
			}
			set
			{
				mElement.SetAttribute(Constants.ATTR_URI,null,value);
				mURI = value;
				// clear cached data
				mReferencedDocument = null;
				mTransformedDocument = null;
			}
		}


		public virtual string Type
		{
			get
			{
				return mType;
			}
			set
			{
				mElement.SetAttribute(Constants.ATTR_TYPE,null, value);
				mType = value;
			}
		}


		public virtual Manifest ReferencedManifest
		{
			get
			{
				return mReferencedManifest;
			}
		}

		// baseElement metodlari
		public override string LocalName
		{
			get
			{
				return Constants.TAG_REFERENCE;
			}
		}
	}

}