using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.ms;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.ms;
using System.Security.Cryptography.Xml;
using System.Security.Cryptography;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

	using Element = XmlElement;
	using NodeList = XmlNodeList;
	using C14nMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using StreamingDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument;
	using DOMDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;


	/// <summary>
	/// <p>The optional <code>Transforms</code> element contains an ordered list of
	/// <code>Transform</code> elements; these describe how the signer obtained the
	/// data object that was digested. The output of each <code>Transform</code>
	/// serves as input to the next <code>Transform</code>. The input to the first
	/// <code>Transform</code> is the result of dereferencing the <code>URI</code>
	/// attribute of the <code>Reference</code> element. The output from the last
	/// <code>Transform</code> is the input for the <code>DigestMethod</code>
	/// algorithm. When transforms are applied the signer is not signing the native
	/// (original) document but the resulting (transformed) document.
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="TransformsType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence>
	///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Transform" maxOccurs="unbounded"/>
	///       &lt;/sequence>
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// 
	/// @author ahmety
	/// date: Jun 11, 2009
	/// </summary>
	public class Transforms : BaseElement
	{
		internal IList<Transform> mTransforms = new List<Transform>();

		public Transforms(Context aBaglam) : base(aBaglam)
		{
			addLineBreak();
		}

		/// <summary>
		///  Construct KeyInfo from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///  resolved appropriately </exception>
		public Transforms(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element[] transforms = selectChildren(Constants.NS_XMLDSIG, Constants.TAG_TRANSFORM);

			if (transforms == null || transforms.Length == 0)
			{
				// En az 1 transform olmali.
                throw new XMLSignatureException("xml.WrongContent", Constants.TAG_TRANSFORM, Constants.TAG_TRANSFORMS);
			}

			foreach (Element transform in transforms)
			{
				mTransforms.Add(new Transform(transform, aContext));
			}

		}

		/// <summary>
		/// Add user defined transform step.
		/// </summary>
		/// <param name="aTransform"> user defined transform </param>
		public virtual void addTransform(Transform aTransform)
		{
			mElement.AppendChild(aTransform.Element);
			addLineBreak();

			mTransforms.Add(aTransform);
		}

		/// <summary>
		/// Return the <it>i</it><sup>th</sup> <code><seealso cref="Transform"/></code>.
		/// Valid <code>i</code> values are 0 to 
		///  <code><seealso cref="#getTransformCount()"/>-1</code>.
		/// </summary>
		/// <param name="aIndex"> index of <seealso cref="Transform"/> to return </param>
		/// <returns> transform at the <it>aIndex</it> </returns>
		public virtual Transform getTransform(int aIndex)
		{
			if (aIndex >= 0 && aIndex < mTransforms.Count)
			{
				return mTransforms[aIndex];
			}
			return null;
		}

		/// <returns> total number of <code><seealso cref="Transform"/></code>s </returns>
		public virtual int TransformCount
		{
			get
			{
				return mTransforms.Count;
			}
		}

		/// <summary>
		/// Applies all included <code>Transform</code>s to input and returns the
		/// result of these transformations.
		/// </summary>
		/// <param name="aSource"> object to be transformed </param>
		/// <returns> Document containing transformed data,
		///      if aSource is null, returned data will be null. </returns>
		/// <exception cref="XMLSignatureException"> if anything goes wrong. </exception>
        public virtual Document apply(Document aSource)
        {
            if (aSource == null)
            {
                return null;
            }
            object result = aSource;
            for (int i = 0; i < TransformCount; i++)
            {
                Transform t = getTransform(i);
                string algUrl = t.Algorithm;
                System.Security.Cryptography.Xml.Transform transform = TransformFactory.Instance.CreateTransform(algUrl);
                DOMDocument domDoc = result as DOMDocument;
                object transformOutput = null;

                if (domDoc != null)
                {
                    XmlDocument ownerDocument = null;
                    XmlElement element = null;
                    if (domDoc.MNode.GetType().IsAssignableFrom(typeof(XmlDocument)))
                    {
                        string baseUri2 = "";
                        if (ownerDocument != null)
                            baseUri2 = ownerDocument.BaseURI;
                        XmlResolver resolver2 = new XmlSecureResolver(new XmlUrlResolver(), baseUri2);

                        //XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseUri);
                        // Add non default namespaces in scope
                        CanonicalXmlNodeList namespaces = MsUtils.GetPropagatedAttributes(element);
                        MsUtils.AddNamespaces(element, namespaces);
                        transform.Resolver = resolver2;
                        transform.LoadInput(domDoc.MNode);

                        if (transform is tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.XmlDsigEnvelopedSignatureTransform)
                        {
                            (transform as tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.XmlDsigEnvelopedSignatureTransform).SetElement(Element);
                        }

                        //object output = transform.GetOutput();
                        //if(output is XmlDocument)
                        //{
                        //    result = new DOMDocument(output as XmlDocument, domDoc.URI); // todo cast
                        //}
                        //else if(output is MemoryStream)
                        //{
                        //    byte[] data = (output as MemoryStream).ToArray();
                        //    result = new InMemoryDocument(data, aSource.URI, aSource.MIMEType, aSource.Encoding);
                        //}

                        //if (transform.GetType().IsAssignableFrom(typeof(XmlDsigXPathTransform)) || transform.GetType().IsAssignableFrom(typeof(EOtherXPathTransform)))
                        //{
                        //    XmlNodeList parameterNodes = t.Element.ChildNodes;
                        //    transform.LoadInnerXml(parameterNodes);
                        //}

                        //**
                        //*
                        //XmlDocument retDoc = null;
                        try
                        {
                            XmlDocument retDoc = (XmlDocument)transform.GetOutput();
                            result = new DOMDocument(retDoc, domDoc.URI);
                        } catch(Exception e)
                        {
                            //retDoc = new XmlDocument();
                            //XmlReader reader = XmlReader.Create((Stream)transform.GetOutput());
                            //retDoc.Load((Stream)transform.GetOutput());
                            //retDoc.Load(reader);


                            //Object o = transform.GetOutput(typeof(MemoryStream));
                            //Type tayp = o.GetType();

                            try
                            {

                                MemoryStream outputStream = (MemoryStream) transform.GetOutput();//typeof (MemoryStream));
                                byte[] data = outputStream.ToArray();
                                result = new InMemoryDocument(data, aSource.URI, aSource.MIMEType, aSource.Encoding);

                            }
                            catch(Exception x)
                            {
                                transform.LoadInput(result as XmlNodeList);
                                XmlNodeList xmlNodeList = (XmlNodeList) transform.GetOutput();//typeof (XmlNodeList));
                                result = new DOMDocument(xmlNodeList, domDoc.URI, domDoc.MIMEType, domDoc.Encoding);
                            }



                        }//*/
                        //*
                        // burasi transform olarak c14n verilince patliyordu
                        //XmlDocument retDoc = (XmlDocument)transform.GetOutput();
                        //result = new DOMDocument(retDoc, domDoc.URI);
                        //result = transform.GetOutput();

                        //if ( ! transform.GetType().IsAssignableFrom(typeof(EOtherXPathTransform)) )
                        //continue;
                        //*/
                    }
                    else if (domDoc.MNode.GetType().IsAssignableFrom(typeof(XmlElement)))
                    {
                        element = domDoc.MNode as XmlElement;
                        ownerDocument = domDoc.MNode.OwnerDocument;
                        //
                        string baseUri = "";
                        if (ownerDocument != null)
                            baseUri = ownerDocument.BaseURI;
                        XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseUri);

                        XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseUri);
                        // Add non default namespaces in scope
                        CanonicalXmlNodeList namespaces2 = MsUtils.GetPropagatedAttributes(element);
                        MsUtils.AddNamespaces(doc.DocumentElement, namespaces2);
                        transform.Resolver = resolver;
                        transform.LoadInput(doc);
                        if (transform is tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.XmlDsigEnvelopedSignatureTransform)
                        {
                            (transform as tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.XmlDsigEnvelopedSignatureTransform).SetElement(Element);
                        }
                    }
                    //string baseUri = "";
                    //if(ownerDocument!=null)
                    //    baseUri = ownerDocument.BaseURI;
                    //XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseUri);

                    //XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseUri);
                    //// Add non default namespaces in scope
                    //CanonicalXmlNodeList namespaces2 = MsUtils.GetPropagatedAttributes(element);
                    //MsUtils.AddNamespaces(doc.DocumentElement, namespaces2);
                    //transform.Resolver = resolver;
                    //transform.LoadInput(doc);
                    if (transform.GetType().IsAssignableFrom(typeof(XmlDsigXPathTransform)) ||
                        transform.GetType().IsAssignableFrom(typeof(EOtherXPathTransform)))
                    {
                        XmlNodeList parameterNodes = t.Element.ChildNodes;
                        transform.LoadInnerXml(parameterNodes);
                    }
                }
                else
                {
                    transform.LoadInput(aSource.Stream);
                }
                transformOutput = transform.GetOutput();
                if (transformOutput != null)
                {
                    if (transformOutput.GetType().IsAssignableFrom(typeof(XmlDocument)))
                    {
                        XmlDocument retDoc = (XmlDocument)transform.GetOutput();
                        result = new DOMDocument(retDoc, domDoc.URI);
                    }
                    else if (transformOutput.GetType().IsAssignableFrom(typeof(MemoryStream)))
                    {
                        MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(MemoryStream));
                        byte[] data = outputStream.ToArray();
                        result = new InMemoryDocument(data, aSource.URI, aSource.MIMEType, aSource.Encoding);
                    }
                    else if (transformOutput.GetType().IsAssignableFrom(typeof(CryptoStream)))
                    {
                        result = new StreamingDocument((Stream)transformOutput, aSource.URI, aSource.MIMEType, aSource.Encoding);
                    }
                    else
                    {
                        result = transformOutput;
                    }
                }
            }

            Document resultingDoc;
            if (result is Document)
            {
                resultingDoc = (Document)result;
            }
            else
            {
                resultingDoc = generateDocument(result, aSource.URI, aSource.MIMEType, aSource.Encoding);
            }
            return resultingDoc;
        }


        //****************************************************************************************************//
        //*************************  eski fonksiyonu geri getirdim  ******************************************//
        //****************************************************************************************************//

        /*

        public virtual Document apply(Document aSource)
        {
            if (aSource == null)
            {
                return null;
            }
            object result = aSource;
            for (int i = 0; i < TransformCount; i++)
            {
                Transform t = getTransform(i);
                string algUrl = t.Algorithm;
                System.Security.Cryptography.Xml.Transform transform = TransformFactory.Instance.CreateTransform(algUrl);
                DOMDocument domDoc = result as DOMDocument;
                object transformOutput = null;
                if (domDoc != null)
                {
                    XmlDocument ownerDocument = null;
                    XmlElement element = null;
                    if (domDoc.MNode.GetType().IsAssignableFrom(typeof(XmlDocument)))
                    {
                        string baseUri2 = "";
                        if (ownerDocument != null)
                            baseUri2 = ownerDocument.BaseURI;
                        XmlResolver resolver2 = new XmlSecureResolver(new XmlUrlResolver(), baseUri2);

                        //XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseUri);
                        // Add non default namespaces in scope
                        CanonicalXmlNodeList namespaces = MsUtils.GetPropagatedAttributes(element);
                        MsUtils.AddNamespaces(element, namespaces);
                        transform.Resolver = resolver2;
                        transform.LoadInput(domDoc.MNode);

                        XmlDocument retDoc = (XmlDocument)transform.GetOutput();
                        result = new DOMDocument(retDoc, domDoc.URI);
                        continue;

                    }
                    else if (domDoc.MNode.GetType().IsAssignableFrom(typeof(XmlElement)))
                    {
                        element = domDoc.MNode as XmlElement;
                        ownerDocument = domDoc.MNode.OwnerDocument;
                    }
                    string baseUri = "";
                    if (ownerDocument != null)
                        baseUri = ownerDocument.BaseURI;
                    XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseUri);

                    XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseUri);
                    // Add non default namespaces in scope
                    CanonicalXmlNodeList namespaces2 = MsUtils.GetPropagatedAttributes(element);
                    MsUtils.AddNamespaces(doc.DocumentElement, namespaces2);
                    transform.Resolver = resolver;
                    transform.LoadInput(doc);
                    if (transform.GetType().IsAssignableFrom(typeof(XmlDsigXPathTransform)))
                    {
                        XmlNodeList parameterNodes = t.Element.ChildNodes;
                        transform.LoadInnerXml(parameterNodes);
                    }
                }
                else
                {
                    transform.LoadInput(aSource.Stream);
                }
                transformOutput = transform.GetOutput();
                if (transformOutput != null)
                {
                    if (transformOutput.GetType().IsAssignableFrom(typeof(XmlDocument)))
                    {
                        XmlDocument retDoc = (XmlDocument)transform.GetOutput();
                        result = new DOMDocument(retDoc, domDoc.URI);
                    }
                    else if (transformOutput.GetType().IsAssignableFrom(typeof(MemoryStream)))
                    {
                        MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(MemoryStream));
                        byte[] data = outputStream.ToArray();
                        result = new InMemoryDocument(data, aSource.URI, aSource.MIMEType, aSource.Encoding);
                    }
                    else if (transformOutput.GetType().IsAssignableFrom(typeof(CryptoStream)))
                    {
                        result = new StreamingDocument((Stream)transformOutput, aSource.URI, aSource.MIMEType, aSource.Encoding);
                    }
                    else
                    {
                        result = transformOutput;
                    }
                }
            }

            Document resultingDoc;
            if (result is Document)
            {
                resultingDoc = (Document)result;
            }
            else
            {
                resultingDoc = generateDocument(result, aSource.URI, aSource.MIMEType, aSource.Encoding);
            }
            return resultingDoc;
        }

        //*/

        //*******************************************************************************************************//
        //*******************************************************************************************************//



		private Document generateDocument(object transformed, string aURI, string aMIMEType, string aEncoding)
		{
			if (transformed is NodeList)
			{
				return new DOMDocument((NodeList)transformed, aURI, aMIMEType, aEncoding);
			}
			else if (transformed is Stream)
			{
                return new StreamingDocument((Stream)transformed, aURI, aMIMEType, aEncoding);
			}

			throw new Exception("Unknown transformation result.");
		}

		/// <returns> if list of transforms contains any <code>C14nTransform</code> </returns>
		public virtual bool hasC14nTransform()
		{
			for (int i = 0; i < TransformCount; i++)
			{
				Transform t = getTransform(i);
				if (C14nMethod.isSupported(t.Algorithm))
				{
					return true;
				}
			}
			return false;
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_TRANSFORMS;
			}
		}
	}

}