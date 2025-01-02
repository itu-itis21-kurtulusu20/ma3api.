using System;
using System.Xml;
using Org.BouncyCastle.Math;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

	using Document = XmlDocument;
	using Element = XmlElement;
	using Node = XmlNode ;
	using Text = XmlText;
	//using Base64 = tr.gov.tubitak.uekae.esya.api.common.Base64;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    //import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;

    /// <summary>
    /// Base class for java classes that mapps to XML digital signature spec.
    /// 
    /// @author ahmety
    /// date: Jun 10, 2009
    /// </summary>
    public abstract class BaseElement
	{

		protected internal Element mElement;
		//protected internal Document mDocument;
		protected internal Context mContext;

		protected internal String mId=null;



		/// <summary>
		/// Construct new BaseElement according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		public BaseElement(Context aContext)
		{
			//mDocument = aContext.Document;
			mContext = aContext;

			mElement = createElement(Namespace, LocalName);
		}

		/// <summary>
		///  Construct BaseElement from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public BaseElement(Element aElement, Context aContext)
		{
			if (aElement == null)
			{
				throw new XMLSignatureException("errors.nullElement", LocalName);
			}

			//this.mDocument = aElement.OwnerDocument;
			this.mElement = aElement;
			this.mContext = aContext;

            
			mId = getAttribute(aElement, Constants.ATTR_ID);
			if (mId != null && !mId.Equals(""))
			{
				mContext.IdRegistry.put(mId, aElement);
				mContext.IdGenerator.update(mId);
			}

			checkNamespace();
		}

		/// <summary>
		/// Get requested attribute value from given element as String </summary>
		/// <param name="aElement"> element containing the attribute </param>
		/// <param name="aAttributeName"> name of the attribute </param>
		/// <returns>  null or attribute value </returns>
        protected internal virtual String getAttribute(Element aElement, string aAttributeName)
		{
		    if (aElement.HasAttribute(aAttributeName))
		    {
		        return aElement.GetAttribute(aAttributeName);
		    }
		    return null;
		}

		protected internal virtual Element createElement(string aNamespace, string aName)
		{
			string prefix = mContext.Config.NsPrefixMap.getPrefix(aNamespace);
			Element e = null;
			if (prefix == null)
			{
				e = Document.CreateElement(aName);
			}
			else
			{
				e = Document.CreateElement(aName,aNamespace);
				e.Prefix = prefix;
			}

           // string attrName = prefix == null ? "xmlns" : "xmlns:" + string.Intern(prefix);
           // e.SetAttribute(attrName,aNamespace);
			return e;
		}

		protected internal virtual Element insertElement(string aNamespace, string aName)
		{
			Element e = createElement(aNamespace, aName);
			mElement.AppendChild(e);
			addLineBreak();
			return e;
		}



		protected internal virtual Element insertTextElement(string aNamespace, string aTagname, string aText)
		{
			Element element = insertElement(aNamespace, aTagname);
			element.InnerText = aText;
			return element;
		}

		protected internal virtual string getChildText(string aNamespace, string aTagname)
		{
			Element e = selectChildElement(aNamespace, aTagname);
			if (e != null)
			{
				return XmlCommonUtil.getText(e);
			}
			return null;
		}

		protected internal virtual Element insertBase64EncodedElement(string aNamespace, string aTagname, byte[] aValue)
		{
            return insertTextElement(aNamespace, aTagname, System.Convert.ToBase64String(aValue));
		}

		protected internal virtual Element selectChildElementXML(string aNSPrefix, string aTagname)
		{
		    XmlNameTable nsTable = mElement.OwnerDocument.NameTable;
            XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
            nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);
            nsManager.AddNamespace("xades", Constants.NS_XADES_1_3_2);
		    XmlNodeList xmlNodeList = mElement.SelectNodes(".//" + aNSPrefix + ":" + aTagname, nsManager);
		    return (XmlElement)mElement.SelectSingleNode(".//" + aNSPrefix + ":" + aTagname, nsManager);
		}

        protected internal virtual Element selectChildElement(string aNamespace, string aTagname)
        {
            return XmlCommonUtil.selectFirstElement(mElement.FirstChild, aNamespace, aTagname);
        }

		protected internal virtual Element[] selectChildren(string aNamespace, string aTagname)
		{
            return XmlCommonUtil.selectNodes(mElement.FirstChild, aNamespace, aTagname);
		}

		/// <summary>
		/// Check if the namespace and localname is valid. </summary>
		/// <exception cref="XMLSignatureException"> if wrong xml element given to constructor </exception>
		protected internal virtual void checkNamespace()
		{
			string localnameSHOULDBE = LocalName;
			string namespaceSHOULDBE = Namespace;

			string localnameIS = mElement.LocalName;
			string namespaceIS = mElement.NamespaceURI;
			if ((!namespaceIS.StartsWith(namespaceSHOULDBE)) || !localnameSHOULDBE.Equals(localnameIS))
			{
				throw new XMLSignatureException("xml.WrongElement", namespaceIS + ":" + localnameIS, namespaceSHOULDBE + ":" + localnameSHOULDBE);
			}
		}


		public virtual Element Element
		{
			get
			{
				return mElement;
			}
            set
            {
                mElement = value;
            }
		}
		//public void setElement(Element aElement) { mElement = aElement; }

		public virtual Document Document
		{
			get
			{
                return mElement == null ? mContext.Document : mElement.OwnerDocument;
			}
            /*set
            {
                mDocument = value;
            }*/
		}
		//public void setDocument(Document aDocument) { mDocument = aDocument; }

		public virtual Context Context
		{
			get
			{
				return mContext;
			}
		}

		public virtual string Id
		{
			get
			{
				return mId;
			}
			set
			{
				if (value == null)
				{
					mElement.RemoveAttribute(Constants.ATTR_ID,null);
				}
				else
				{
                    if (mElement.HasAttribute(Constants.ATTR_ID))
                    {
                        mElement.RemoveAttribute(Constants.ATTR_ID);
                    }
					mElement.SetAttribute(Constants.ATTR_ID,null,value);
					mId = value;
				}
				mContext.IdRegistry.put(value, mElement);
			}
		}


		// utility methods

		//Add line break text node to xml structure for pretty print
		protected internal virtual void addLineBreak()
		{
//			mElement.AppendChild(Document.CreateTextNode("\n"));
            mElement.AppendChild(Document.CreateWhitespace("\n"));
		}

		//Add line break text node to given xml element for pretty print
		protected internal virtual void addLineBreak(Element aElement)
		{
			//aElement.AppendChild(Document.CreateTextNode("\n"));
            aElement.AppendChild(Document.CreateWhitespace("\n"));
		}

		protected internal virtual void generateAndSetId(string aType)
		{
			string id = mContext.IdGenerator.uret(aType);
			Id = id;
			mContext.IdRegistry.put(id, mElement);
		}

		protected internal virtual void addBigIntegerElement(BigInteger aValue, string aNamespace, string aTagName)
		{
			if (aValue != null)
			{
				Element e = Document.CreateElement(aTagName,aNamespace);
				e.Prefix = mContext.Config.NsPrefixMap.getPrefix(aNamespace);

                string val = System.Convert.ToBase64String(aValue.ToByteArray());
				Text text = Document.CreateTextNode(val);
				e.AppendChild(text);
				mElement.AppendChild(e);
			}
		}

		// get base64 encoded content from child element as BigInteger
		protected internal virtual BigInteger getBigIntegerFromElement(string aNamespace, string aLocalname)
		{
			Element text = selectChildElement(aNamespace, aLocalname);
		    byte[] bytes = Convert.FromBase64String(XmlCommonUtil.getText(text));
		    return new BigInteger(bytes);
		}

		public virtual string Namespace
		{
			get
			{
				return Constants.NS_XMLDSIG;
			}
		}

		public abstract string LocalName {get;}


	}

}