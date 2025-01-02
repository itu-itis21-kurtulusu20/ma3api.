using System;
using System.IO;
using System.Security.Cryptography.Xml;
using System.Xml;
using System.Xml.Serialization;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.ms;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.ms;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.document
{
	using Logger = log4net.ILog;
	using Element = XmlElement;
	using Node = XmlNode;
	using NodeList = XmlNodeList;
	using DataType = tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using C14nMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    /// <summary>
    /// Document containing DOM Node.
    /// </summary>
    /// <seealso cref= org.w3c.dom.Node
    /// 
    /// @author ahmety
    /// date: Jun 17, 2009 </seealso>
    public class DOMDocument : Document
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(DOMDocument));

		private readonly NodeList mNodeList;
		private readonly bool mIncludeComments;
        protected DataType mDataType;

        private readonly Node mNode;

	    public XmlNode MNode
	    {
	        get { return mNode; }
	    }

	    /// <summary>
		/// Constructs DOMDocument which holds its data as NodeList. Note that this
		/// constructor strips comments from data. If you dont wish so, use other
		/// constructor
		/// </summary>
		/// <param name="aNode"> that holds xml data </param>
		/// <param name="aURI"> where data is obtained. </param>
		public DOMDocument(Node aNode, string aURI) : this(aNode, aURI, false)
		{
            
        }

		public DOMDocument(Node aNode, string aURI, bool aIncludeComments) : base(aURI, "text/xml", null)
		{
            mDataType = DataType.NODE;
            mIncludeComments = aIncludeComments;

			if (aNode == null)
			{
				throw new Exception("invalid.argument");
			}
			if (logger.IsDebugEnabled)
			{
				logger.Debug("Constructing DomDocument from node : " + aNode.Name);
			}

			if (aNode is Element)
			{
				string mime = ((Element)aNode).GetAttribute(Constants.ATTR_MIMETYPE);
				if (mime != null && mime.Length > 0)
				{
					mMIMEType = mime;
				}

                string encoding = ((Element)aNode).GetAttribute(Constants.ATTR_ENCODING);
				if (encoding != null && encoding.Length > 0)
				{
					mEncoding = encoding;
				}
			}


			try
			{
                mNode = aNode;
                //XmlUtil.circumventBug2650(XMLUtils.getOwnerDocument(aNode));
                mNodeList = XmlCommonUtil.getNodeSet(aNode, aIncludeComments);

			}
			catch (Exception x)
			{
				Console.WriteLine(x.ToString());
				Console.Write(x.StackTrace);
			}
		}

		public DOMDocument(NodeList aNodeList, string aURI, string aMIMEType, string aEncoding) : base(aURI, aMIMEType, aEncoding)
		{
		    mDataType = DataType.NODESET;
            mNodeList = aNodeList;
		}

		/// <summary>
		/// Get document content as bytes, which requires datatype conversion from
		/// InputStream or NodeList. </summary>
		/// <returns> document content as byte[] </returns>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          if any errors occur while datatype
		///          conversion </exception>
		public override byte[] Bytes
		{
			get
			{
				if (mBytesCached == null)
				{
					switch (Type)
					{
                        case DataType.NODESET:
                            if (mNode != null)
                            {
                                // nodelist ben cevirdim simdi denemek icin
                                mBytesCached = XmlUtil.outputDOM(mNode, mIncludeComments ? C14nMethod.INCLUSIVE_WITH_COMMENTS : C14nMethod.INCLUSIVE);
                                //mBytesCached = XmlUtil.outputDOM(mNodeList, mIncludeComments ? C14nMethod.INCLUSIVE_WITH_COMMENTS : C14nMethod.INCLUSIVE);
                                //byte[] data = DigestUtil.digest(DigestAlg.SHA1, mBytesCached);
                                //Console.WriteLine("mBytesCache: " + Convert.ToBase64String(data, 0, data.Length));
                                //Console.WriteLine("mBytesCache: " + System.Text.Encoding.UTF8.GetString(mBytesCached));
                            }
                            else
                            {
                                mBytesCached = XmlUtil.outputDOM(mNodeList, mIncludeComments ? C14nMethod.INCLUSIVE_WITH_COMMENTS : C14nMethod.INCLUSIVE);
                              // mBytesCached = XmlUtil.outputDOM(NodeList, mIncludeComments ? C14nMethod.INCLUSIVE_WITH_COMMENTS : C14nMethod.INCLUSIVE);
                            }
							break;
                        case DataType.OCTETSTREAM:
							mBytesCached = toByteArray(Stream);
							break;
					}
				}
				// dummy return, we shouldnt be here!
				return mBytesCached;
			}
		}

	    public Document getTransformedDocumentSignedProps(C14nMethod aC14nMethod)
	    {
	        XmlNodeList xmlNodeList = XmlCommonUtil.getNodeSet(mNode, mIncludeComments);
	        byte[] bytes = C14NUtil.canonicalizeXPathNodeSet(xmlNodeList, aC14nMethod);
	        Document mTransformedDocument = new InMemoryDocument(bytes, URI, MIMEType, Encoding);
	        return mTransformedDocument;
	    }

        // bkz. xmlsignature.c14n.C14NUtil canonicalizeSubtree
	    public Document getTransformedDocument(C14nMethod aC14nMethod)
        {
            ///////yeni kod
            //*
            // ortak kod
            // eger aC14NMethod null degilse o olsun, null ise DEFAULT olsun
            C14nMethod c14NMethod = aC14nMethod ?? Constants.DEFAULT_REFERENCE_C14N;
            Transform transform = TransformFactory.Instance.CreateTransform(c14NMethod.URL);

            // eger bir XmlElement ise
            if (MNode.NodeType.CompareTo(XmlNodeType.Element) == 0)
            {
                //burasi orjinal kopya idi
                //String baseURI = MNode.OwnerDocument.BaseURI;
                //XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseURI);
                //transform.Resolver = resolver;

                //XmlElement element = MNode as XmlElement;
                //XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseURI);
                //CanonicalXmlNodeList namespaces = MsUtils.GetPropagatedAttributes(element);
                //MsUtils.AddNamespaces(doc.DocumentElement, namespaces);

                //transform.LoadInput(doc);

                // preprocesselement icinde string'e donusum yapilinca &#13; karakteri \n'ye
                // donuyordu sonra da canonicalization ayagina atiliyordu, ben de stream olarak gonderdim

                String baseURI = MNode.OwnerDocument.BaseURI;
                XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseURI);
                transform.Resolver = resolver;

                XmlElement element = MNode as XmlElement;
                //XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseURI);
                Stream stim = new MemoryStream(this.Bytes);
                XmlDocument doc = MsUtils.PreProcessElementInput(stim, resolver, baseURI);
                CanonicalXmlNodeList namespaces = MsUtils.GetPropagatedAttributes(element);
                MsUtils.AddNamespaces(doc.DocumentElement, namespaces);

                transform.LoadInput(doc);
            }

            // eger XmlDocument'in kendisi ise
            else if (MNode.NodeType.CompareTo(XmlNodeType.Document) == 0)
            {
                // empty string as base uri, meaning the whole document, i guess... :)
                XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), "");
                transform.Resolver = resolver;
                transform.LoadInput(MNode);
            }

            // ortak kod devam
            MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));
	        byte[] bytes = outputStream.ToArray();
            Document mTransformedDocument = new InMemoryDocument(bytes, URI, MIMEType, Encoding);
	        return mTransformedDocument;
            //*/


	        // todo burasi c14n
	        ///////////////// eski kod burasi kapatilip duzenlenerek yazildi, icerik ayni tabi
	        /*
            XmlDocument ownerDocument = null;
            XmlElement element = null;
            if (MNode.GetType().IsAssignableFrom(typeof(XmlDocument)))
            {
                C14nMethod c14N2 = aC14nMethod ?? Constants.DEFAULT_REFERENCE_C14N;
                System.Security.Cryptography.Xml.Transform transform2 = TransformFactory.Instance.CreateTransform(c14N2.URL);

                string baseUri2 = "";
                if (ownerDocument != null)
                    baseUri2 = ownerDocument.BaseURI;
                XmlResolver resolver2 = new XmlSecureResolver(new XmlUrlResolver(), baseUri2);

                //XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseUri);
                // Add non default namespaces in scope
                // suleyman, element null ise ki burda null alttaki satira gerek yok
                CanonicalXmlNodeList namespaces2 = MsUtils.GetPropagatedAttributes(element);
                MsUtils.AddNamespaces(element, namespaces2);
                transform2.Resolver = resolver2;
                transform2.LoadInput(MNode);
                MemoryStream outputStream2 = (MemoryStream)transform2.GetOutput(typeof(Stream));
                byte[] bytes2 = outputStream2.ToArray();
                return new InMemoryDocument(bytes2, URI, MIMEType, Encoding);      
            }
            else if (MNode.GetType().IsAssignableFrom(typeof(XmlElement)))
            {
                element = MNode as XmlElement;
                ownerDocument = MNode.OwnerDocument;
            }

            string baseUri = "";
            if (ownerDocument != null)
                baseUri = ownerDocument.BaseURI;
            XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseUri);

            XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseUri);
            // Add non default namespaces in scope
            CanonicalXmlNodeList namespaces = MsUtils.GetPropagatedAttributes(element);
            MsUtils.AddNamespaces(doc.DocumentElement, namespaces);
            C14nMethod c14N = aC14nMethod ?? Constants.DEFAULT_REFERENCE_C14N;
            System.Security.Cryptography.Xml.Transform transform = TransformFactory.Instance.CreateTransform(c14N.URL);
            transform.Resolver = resolver;
            transform.LoadInput(doc);
            MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));
            byte[] bytes = outputStream.ToArray();
            Document mTransformedDocument = new InMemoryDocument(bytes, URI, MIMEType, Encoding);
            return mTransformedDocument;
            //*/
        }

		public override DataType Type
		{
			get
			{
                return mDataType;
            }
		}

		public override NodeList NodeList
		{
			get
			{
				return mNodeList;
			}
		}

        public override object C14NObject
        {
            get
            {
                if (mDataType == DataType.NODE)
                    return mNode;
                else
                    return mNodeList;

            }
        }
    }

}