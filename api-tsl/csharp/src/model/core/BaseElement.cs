using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using Org.BouncyCastle.Math;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.core
{
    public abstract class BaseElement
    {
        protected internal XmlElement mElement;
        protected internal XmlDocument mDocument;
        //protected internal Context mContext;

        protected internal String mId = null;

        private readonly NamespacePrefixMap nsPrefixMap = new NamespacePrefixMap();
        private readonly IdGenerator idGenerator = new IdGenerator();

        /// <summary>
        /// Construct new BaseElement according to context </summary>
        /// <param name="aContext"> where some signature spesific properties reside. </param>
        public BaseElement(XmlDocument document)
        {
            mDocument = document;

            mElement = createElement(Namespace, LocalName);
        }

        /// <summary>
        ///  Construct BaseElement from existing </summary>
        ///  <param name="aElement"> xml element </param>
        ///  <param name="aContext"> according to context </param>
        ///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
        ///      resolved appropriately </exception>
        public BaseElement(XmlElement aElement)
        {
            if (aElement == null)
            {
                throw new TSLException("errors.nullElement");
            }

            this.mDocument = aElement.OwnerDocument;
            this.mElement = aElement;

            mId = getAttribute(aElement, Constants.ATTR_ID);

            checkNamespace();
        }

        /// <summary>
        /// Get requested attribute value from given element as String </summary>
        /// <param name="aElement"> element containing the attribute </param>
        /// <param name="aAttributeName"> name of the attribute </param>
        /// <returns>  null or attribute value </returns>
        protected internal virtual String getAttribute(XmlElement aElement, string aAttributeName)
        {
            String retValue = null;
            if (aElement.HasAttribute(aAttributeName))
            {
                return aElement.GetAttribute(aAttributeName);
            }
            return null;
        }

        protected internal virtual XmlElement createElement(string aNamespace, string aName)
        {
            string prefix = nsPrefixMap.getPrefix(aNamespace);
            XmlElement e = null;
            if (prefix == null)
            {
                e = mDocument.CreateElement(aName);
            }
            else
            {
                e = mDocument.CreateElement(aName, aNamespace);
                e.Prefix = prefix;
            }

            // string attrName = prefix == null ? "xmlns" : "xmlns:" + string.Intern(prefix);
            // e.SetAttribute(attrName,aNamespace);
            return e;
        }

        protected internal virtual XmlElement insertElement(string aNamespace, string aName)
        {
            XmlElement e = createElement(aNamespace, aName);
            mElement.AppendChild(e);
            addLineBreak();
            return e;
        }



        protected internal virtual XmlElement insertTextElement(string aNamespace, string aTagname, string aText)
        {
            XmlElement element = insertElement(aNamespace, aTagname);
            element.InnerText = aText;
            return element;
        }

        protected internal virtual string getChildText(string aNamespace, string aTagname)
        {
            XmlElement e = selectChildElement(aNamespace, aTagname);
            if (e != null)
            {
                return XmlUtil.getText(e);
            }
            return null;
        }

        protected internal virtual XmlElement insertBase64EncodedElement(string aNamespace, string aTagname, byte[] aValue)
        {
            return insertTextElement(aNamespace, aTagname, System.Convert.ToBase64String(aValue));
        }

        protected internal virtual XmlElement selectChildElementXML(string aNSPrefix, string aTagname)
        {
            XmlNameTable nsTable = mElement.OwnerDocument.NameTable;
            XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
            nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);
            nsManager.AddNamespace("xades", Constants.NS_XADES);
            nsManager.AddNamespace("tsl", Constants.NS_TSL);
            nsManager.AddNamespace("tslx", Constants.NS_TSLX);
            nsManager.AddNamespace("ecc", Constants.NS_ECC);
            XmlNodeList xmlNodeList = mElement.SelectNodes(".//" + aNSPrefix + ":" + aTagname, nsManager);
            return (XmlElement)mElement.SelectSingleNode(".//" + aNSPrefix + ":" + aTagname, nsManager);
        }

        protected internal virtual XmlElement selectChildElement(string aNamespace, string aTagname)
        {
            return XmlUtil.selectFirstElement(mElement.FirstChild, aNamespace, aTagname);
        }

        protected internal virtual XmlElement[] selectChildren(string aNamespace, string aTagname)
        {
            return XmlUtil.selectNodes(mElement.FirstChild, aNamespace, aTagname);
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
                //bu değişim ancak ve ancak optional tag ler için yapılacaktır. Bunu niçin bir kontrol mekanizması düşünülebilir!!!
                //Ex: list of required and list of optional tags gibi
                //mElement = (XmlElement)mElement.NextSibling;
                //checkNamespace();
                throw new TSLException("xml.WrongElement" + namespaceIS + ":" + localnameIS + namespaceSHOULDBE + ":" + localnameSHOULDBE);
            }
        }


        public virtual XmlElement Element
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

        public virtual XmlDocument Document
        {
            get
            {
                return mDocument;
            }
            set
            {
                mDocument = value;
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
                    mElement.RemoveAttribute(Constants.ATTR_ID, null);
                }
                else
                {
                    if (mElement.HasAttribute(Constants.ATTR_ID))
                    {
                        mElement.RemoveAttribute(Constants.ATTR_ID);
                    }
                    mElement.SetAttribute(Constants.ATTR_ID, null, value);
                    mId = value;
                }
            }
        }


        // utility methods

        //Add line break text node to xml structure for pretty print
        protected internal virtual void addLineBreak()
        {
            mElement.AppendChild(mDocument.CreateTextNode("\n"));
        }

        //Add line break text node to given xml element for pretty print
        protected internal virtual void addLineBreak(XmlElement aElement)
        {
            aElement.AppendChild(mDocument.CreateTextNode("\n"));
        }

        protected internal virtual void generateAndSetId(string aType)
        {
            string id = idGenerator.uret(aType);
            Id = id;
        }

        protected internal virtual void addBigIntegerElement(BigInteger aValue, string aNamespace, string aTagName)
        {
            if (aValue != null)
            {
                XmlElement e = mDocument.CreateElement(aTagName, aNamespace);
                e.Prefix = nsPrefixMap.getPrefix(aNamespace);

                string val = System.Convert.ToBase64String(aValue.ToByteArray());
                XmlText text = mDocument.CreateTextNode(val);
                e.AppendChild(text);
                mElement.AppendChild(e);
            }
        }

        // get base64 encoded content from child element as BigInteger
        protected internal virtual BigInteger getBigIntegerFromElement(string aNamespace, string aLocalname)
        {
            XmlElement text = selectChildElement(aNamespace, aLocalname);
            byte[] bytes = Convert.FromBase64String(XmlUtil.getText(text));
            return new BigInteger(bytes);
        }

        public virtual string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public abstract string LocalName { get; }
    }
}
