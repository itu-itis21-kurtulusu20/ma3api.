using System;
using System.Collections.Generic;
using System.Security;
using System.Text;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class XmlUtil
    {
        public static readonly String NS_NAMESPACESPEC     = "http://www.w3.org/2000/xmlns/";
        public static readonly String ATTR_ID                  = "Id";

        public static XmlElement getNextElement(XmlNode el)
        {
            while ((el != null) && (el.NodeType != XmlNodeType.Element))
            {
                el = el.NextSibling;
            }
            return (XmlElement)el;

        }

        public static void removeChildren(XmlElement aElement)
        {
            XmlNode child;
            while ((child = aElement.FirstChild) != null)
            {
                aElement.RemoveChild(child);
            }
        }

        public static void addLineBreak(XmlElement aElement)
        {
            if (aElement != null && aElement.OwnerDocument != null) aElement.AppendChild(aElement.OwnerDocument.CreateTextNode("\n"));
        }

        public static XmlElement createDSctx(XmlDocument doc, string prefix, string @namespace)
        {
            if ((prefix == null) || (prefix.Trim().Length == 0))
            {
                throw new ArgumentException("You must supply a prefix");
            }

            XmlElement ctx = doc.CreateElement("namespaceContext", null);

            //TODO Bu Constants.NS_NAMESPACESPEC olunca zaten namespace var hatası veriyor. .net
            //java tarafında nasıl oluyor anlamadım.
            //Ahmetle konuşmak lazım.
            //ctx.SetAttribute("xmlns:" + prefix.Trim(),Constants.NS_NAMESPACESPEC, @namespace);
            ctx.SetAttribute("xmlns:" + String.Intern(prefix).Trim(), @namespace);

            return ctx;
        }

        public static void addNSAttribute(XmlElement aElement, string aPrefix, string aNamespace)
        {
            aElement.SetAttribute("xmlns:" + aPrefix.Trim(), NS_NAMESPACESPEC, aNamespace);
        }

        // XML Dökümanı içindeki geçersiz XML karakterlerini kaldırır.
        //Geçerli olanlarla yer değiştirir.
        public static string escapeXMLData(string aText)
        {
            return SecurityElement.Escape(aText);
        }

        public static XmlElement findByIdAttr(XmlElement aRoot, string aAttributeValue)
        {
            XmlElement result = findByIdAttrRec(aRoot, aAttributeValue) ?? findLikeIdAttr(aRoot, aAttributeValue);
            return result;
        }


        private static XmlElement findByIdAttrRec(XmlElement aRoot, string aAttributeValue)
        {
            string id = aRoot != null ? aRoot.GetAttribute(ATTR_ID) : null;
            if (id != null && id == aAttributeValue)
            {
                return aRoot;
            }
            if (aRoot == null) return null;
            XmlNodeList elements = aRoot.GetElementsByTagName("*");
            for (int i = 0; i < elements.Count; i++)
            {
                XmlElement next = (XmlElement)elements.Item(i);
                XmlElement found = findByIdAttr(next, aAttributeValue);
                if (found != null)
                {
                    return found;
                }
            }
            return null;
        }

        private static readonly string[] IDs = { "id", "ID", "iD" };

        private static XmlElement findLikeIdAttr(XmlElement aRoot, string aAttributeValue)
        {
            foreach (string str in IDs)
            {
                if (aRoot == null) continue;
                string id = aRoot.GetAttribute(str);
                if (!string.IsNullOrEmpty(id) && id == aAttributeValue)
                {
                    return aRoot;
                }
            }
            if (aRoot == null) return null;
            XmlNodeList elements = aRoot.GetElementsByTagName("*");
            for (int i = 0; i < elements.Count; i++)
            {
                XmlElement next = (XmlElement)elements.Item(i);
                XmlElement found = findLikeIdAttr(next, aAttributeValue);
                if (found != null)
                {
                    return found;
                }
            }
            return null;
        }


        public static string getText(XmlNodeList aList)
        {
            StringBuilder buffer = new StringBuilder();
            if (aList == null) return buffer.ToString();

            for (int i = 0; i < aList.Count; i++)
            {
                XmlNode aNode = aList.Item(i);
                if (aNode == null || aNode.NodeType != XmlNodeType.Text) continue;
                XmlText t = (XmlText) aNode;
                buffer.Append(t.Data);
            }
            return buffer.ToString();
        }

        //*/

        public static string getText(XmlElement element)
        {
            if (element == null) return string.Empty;
            XmlNode sibling = element.FirstChild;
            StringBuilder sb = new StringBuilder();

            while (sibling != null)
            {
                if (sibling.NodeType == XmlNodeType.Text)
                {
                    XmlText t = (XmlText) sibling;
                    sb.Append(t.Data);
                }
                sibling = sibling.NextSibling;
            }
            return sb.ToString();
        }


        public static readonly string XML_PREAMBLE_STR= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        public static readonly byte[] XML_PREAMBLE = Encoding.UTF8.GetBytes(XML_PREAMBLE_STR);


        public static byte[] getBase64DecodedText(XmlElement element)
        {
            string text = getText(element);
            byte[] bytes;
            try
            {
                //bytes = Convert.FromBase64String(text); artik Base64 class'i var
                 bytes = Base64.Decode(text);
            }
            catch (Exception x)
            {
                throw new ESYARuntimeException("errors.base64", x);
            }
            return bytes;
        }

        public static void setBase64EncodedText(XmlElement aElement, byte[] aValue)
        {
            if (aElement != null)
            {
                XmlNode n = aElement.FirstChild;
                while (n != null)
                {
                    aElement.RemoveChild(n);
                    n = n.NextSibling;
                }
            }

            if (aValue == null)
            {
                return;
            }

            //string base64Encoded = Base64.Encode(aValue).ToString(); //Convert.ToBase64String(aValue);
            string base64Encoded = Convert.ToBase64String(aValue);
            if (aElement == null) return;
            if (aElement.OwnerDocument == null) return;
            XmlText t = aElement.OwnerDocument.CreateTextNode(base64Encoded);

            aElement.AppendChild(t);
        }


        public static XmlNodeList getNodeSet(XmlNode rootNode, bool comments)
        {
            IList<XmlNode> list = new List<XmlNode>();
            GetSetRec(rootNode, list, comments);
            return new NodeListImpl(list);
        }

        private static void GetSetRec(XmlNode rootNode, IList<XmlNode> result, bool comments)
        {
            if (rootNode == null) return;
            switch (rootNode.NodeType)
            {
                case XmlNodeType.Element:
                    result.Add(rootNode);
                    XmlElement el = (XmlElement)rootNode;
                    if (el.HasAttributes)
                    {
                        XmlAttributeCollection nl = el.Attributes;
                        for (int i = 0; i < nl.Count; i++)
                        {
                            result.Add(nl.Item(i));
                        }
                    }
                    GetSetRecForXmlNodeTypeDocument(rootNode, result, comments);
                    return;
                case XmlNodeType.Document:
                    GetSetRecForXmlNodeTypeDocument(rootNode, result, comments);
                    return;
                case XmlNodeType.Comment:
                    if (comments)
                    {
                        result.Add(rootNode);
                    }
                    return;
                case XmlNodeType.DocumentType:
                    return;
                default:
                    result.Add(rootNode);
                    break;
            }
        }

        private static void GetSetRecForXmlNodeTypeDocument(XmlNode rootNode, IList<XmlNode> result, bool comments)
        {
            for (XmlNode r = rootNode.FirstChild; r != null; r = r.NextSibling)
            {
                if (r.NodeType == XmlNodeType.Text)
                {
                    result.Add(r);
                    while ((r != null) && (r.NodeType == XmlNodeType.Text))
                    {
                        r = r.NextSibling;
                    }
                    if (r == null)
                    {
                        return;
                    }
                }
                GetSetRec(r, result, comments);
            }
        }

        public static IList<XmlElement> selectChildElements(XmlElement aElement)
        {
            IList<XmlElement> selected = new List<XmlElement>();
            if (aElement != null)
            {
                XmlNodeList list = aElement.ChildNodes;

                for (int i = 0; i < list.Count; i++)
                {
                    XmlNode node = list.Item(i);
                    if (node != null && node.NodeType == XmlNodeType.Element)
                    {
                        selected.Add((XmlElement)node);
                    }
                }
            }
            return selected;
        }

        public static XmlElement[] selectNodes(XmlNode aSibling, string aNamespace, string aNodeName)
        {
            int size = 20;
            XmlElement[] a = new XmlElement[size];
            int curr = 0;
            //List list=new ArrayList();
            while (aSibling != null)
            {
                if (isNamespaceElement(aSibling, aNodeName, aNamespace))
                {
                    a[curr++] = (XmlElement)aSibling;
                    if (size <= curr)
                    {
                        int cursize = size << 2;
                        XmlElement[] cp = new XmlElement[cursize];
                        Array.Copy(a, 0, cp, 0, size);
                        a = cp;
                        size = cursize;
                    }
                }
                aSibling = aSibling.NextSibling;
            }
            XmlElement[] af = new XmlElement[curr];
            Array.Copy(a, 0, af, 0, curr);
            return af;
        }

        public static XmlElement selectFirstElement(XmlNode aSibling, string aNamespace, string aNodeName)
        {
            while (aSibling != null)
            {
                if (isNamespaceElement(aSibling, aNodeName, aNamespace))
                {
                    return (XmlElement)aSibling;
                }
                aSibling = aSibling.NextSibling;
            }
            return null;
        }

        public static XmlElement selectFirstElementNoNS(XmlNode aSibling, string aNodeName)
        {
            while (aSibling != null)
            {
                if (aSibling.LocalName == aNodeName)
                {
                    return (XmlElement)aSibling;
                }
                aSibling = aSibling.NextSibling;
            }
            return null;
        }

        public static bool isNamespaceElement(XmlNode el, string type, string ns)
        {
            return !((el == null) || string.IsNullOrEmpty(el.NamespaceURI) || !el.NamespaceURI.StartsWith(ns) || (!string.IsNullOrEmpty(el.LocalName) && el.LocalName != type));
        }

        public static XmlDocument getOwnerDocument(HashSet<XmlNode> xpathNodeSet)
        {
            if (xpathNodeSet == null) return null;
            using (IEnumerator<XmlNode> enumerator = xpathNodeSet.GetEnumerator())
            {
                while (enumerator.MoveNext())
                {
                    XmlNode node = enumerator.Current;
                    if (node != null && node.NodeType == XmlNodeType.Document)
                    {
                        return (XmlDocument)node;
                    }
                    XmlDocument doc = null;
                    if (node != null && node.NodeType == XmlNodeType.Attribute)
                    {
                        var ownerDocument = ((XmlAttribute)node).OwnerDocument;
                        if (ownerDocument != null)
                            doc = ownerDocument.OwnerDocument;
                    }
                    else
                    {
                        if (node != null) doc = node.OwnerDocument;
                    }
                    if (doc != null)
                        return doc;
                }
            }
            return null;
        }

        public static HashSet<XmlNode> convertNodelistToSet(XmlNodeList xpathNodeSet)
        {

            if (xpathNodeSet == null)
            {
                return new HashSet<XmlNode>();
            }

            int length = xpathNodeSet.Count;
            HashSet<XmlNode> set = new HashSet<XmlNode>();
            for (int i = 0; i < length; i++)
            {
                set.Add(xpathNodeSet.Item(i));
            }

            return set;
        }

        public static string datetime2xmlgregstr(DateTime dt)
        {
            return XmlConvert.ToString(dt, XmlDateTimeSerializationMode.Local);
        }

        public static DateTime getDate(XmlElement aElement)
        {
            string text = getText(aElement);
            try
            {
                //TODO Bu kısmın test edilmesi gerekiyor.
                //ISO 8601 format
                return XmlConvert.ToDateTime(text, XmlDateTimeSerializationMode.Local);
                //as return DateTime.Parse(text,  null, DateTimeStyles.RoundtripKind);
            }
            catch (Exception e)
            {
                throw new ESYARuntimeException("Incorrect date format: " + text, e);
            }
        }
        public static string FindXPath(XmlNode node)
        {
            StringBuilder builder = new StringBuilder();
            while (node != null)
            {
                switch (node.NodeType)
                {
                    case XmlNodeType.Attribute:
                        builder.Insert(0, "/@" + node.Name);
                        node = ((XmlAttribute) node).OwnerElement;
                        break;
                    case XmlNodeType.Element:
                        int index = FindElementIndex((XmlElement) node);
                        builder.Insert(0, "/" + node.Name + "[" + index + "]");
                        node = node.ParentNode;
                        break;
                    case XmlNodeType.Document:
                        return builder.ToString();
                    default:
                        throw new ArgumentException("Only elements and attributes are supported");
                }
            }
            throw new ArgumentException("Node was not in a document");
        }

        static int FindElementIndex(XmlElement element)
        {
            if (element == null) throw new ArgumentException("Couldn't find element within parent");
            XmlNode parentNode = element.ParentNode;
            if (parentNode is XmlDocument)
            {
                return 1;
            }
            XmlElement parent = (XmlElement) parentNode;
            int index = 1;
            if (parent == null) throw new ArgumentException("Couldn't find element within parent");
            foreach (XmlNode candidate in parent.ChildNodes)
            {
                if (!(candidate is XmlElement) || candidate.Name != element.Name) continue;
                if (candidate == element)
                {
                    return index;
                }
                index++;
            }
            throw new ArgumentException("Couldn't find element within parent");
        }


        /* @orcun.ertugrul; XmlDsigEnvelopedSignatureTransform.GetOutputFromNode fonksiyonunda Path aracılığı ile node seçmek gerekiyordu. 
         * Framework içindeki XmlNode.SelectSingleNode fonksiyonu XmlNamespaceManager nesnesi istiyor. Bu nesnenin yaratılması için yeterli bilgi 
         * erişilmek istenen fonksiyonun içinde olmadığı için aşağıdaki SelectNodeFromPath fonksiyonu yazıldı.
         * Eğer kullanabiliyorsanız Framework içindeki XmlNode.SelectSingleNode fonksiyonunu kullanınız.
         */

        public static XmlNode SelectNodeFromPath(XmlNode retNode, string path)
        {
            String[] nodeNames = path.Split('/');
            XmlNode travelerNode = retNode;

            foreach (string nodePath in nodeNames)
            {
                if (string.IsNullOrEmpty(nodePath))
                    continue;

                string nodeName = nodePath.Substring(0, nodePath.LastIndexOf("[", StringComparison.Ordinal));
                string indexStr = nodePath.Substring(nodePath.LastIndexOf("[", StringComparison.Ordinal) + 1,
                    nodePath.LastIndexOf("]", StringComparison.Ordinal) - nodePath.LastIndexOf("[", StringComparison.Ordinal) - 1);

                int nodeIndex;
                int.TryParse(indexStr, out nodeIndex);

                int index = 0;
                if (travelerNode == null) continue;
                XmlNodeList childNodes = travelerNode.ChildNodes;

                foreach (XmlNode childNode in childNodes)
                {
                    if (childNode.Name == nodeName)
                        index ++;
                    if (nodeIndex != index) continue;
                    travelerNode = childNode;
                    break;
                }
            }

            return travelerNode;

        }
    }
}
