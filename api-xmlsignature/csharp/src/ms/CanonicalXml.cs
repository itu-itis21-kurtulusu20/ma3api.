
using System;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.ms
{
    using System.Xml;
    using System.IO;
    using System.Text; 
    using System.Collections;
 
    public class ECanonicalXml { 
      //  private CanonicalXmlDocument m_c14nDoc;
      //  private C14NAncestralNamespaceContextManager m_ancMgr; 

        // private static String defaultXPathWithoutComments = "(//. | //@* | //namespace::*)[not(self::comment())]";
        // private static String defaultXPathWithoutComments = "(//. | //@* | //namespace::*)";
        // private static String defaultXPathWithComments = "(//. | //@* | //namespace::*)"; 
        // private static String defaultXPathWithComments = "(//. | //@* | //namespace::*)";

        public static XmlDocument GetOwnerDocument(XmlNodeList nodeList)
        {
            foreach (XmlNode node in nodeList)
            {
                if (node.OwnerDocument != null)
                    return node.OwnerDocument;
            }
            return null;
        }

        public static bool NodeInList(XmlNode node, XmlNodeList nodeList)
        {
            foreach (XmlNode nodeElem in nodeList)
            {
                if (nodeElem == node) return true;
            }
            return false;
        } 

       public ECanonicalXml(XmlNodeList nodeList, XmlResolver resolver, bool includeComments) {
            if (nodeList == null)
                throw new ArgumentNullException("nodeList");
 
            XmlDocument doc = GetOwnerDocument(nodeList);
            if (doc == null) 
                throw new ArgumentException("nodeList"); 

       //     m_c14nDoc = new CanonicalXmlDocument(false, includeComments); 
         //   m_c14nDoc.XmlResolver = resolver;
           // m_c14nDoc.Load(new XmlNodeReader(doc));
           // m_ancMgr = new C14NAncestralNamespaceContextManager();
 
          // MarkInclusionStateForNodes(nodeList, doc,doc m_c14nDoc);
            MarkInclusionStateForNodes(nodeList, doc,doc);
        } 
 
       static void MarkNodeAsIncluded(XmlNode node) {
         //   if (node is ICanonicalizableNode) 
             //  ((ICanonicalizableNode) node).IsInNodeSet = true;
        }

        private static void MarkInclusionStateForNodes(XmlNodeList nodeList, XmlDocument inputRoot, XmlDocument root) { 
            CanonicalXmlNodeList elementList = new CanonicalXmlNodeList();
            CanonicalXmlNodeList elementListCanonical = new CanonicalXmlNodeList(); 
            elementList.Add(inputRoot); 
            elementListCanonical.Add(root);
            int index = 0;
            Console.WriteLine("BAS" + DateTime.UtcNow.ToLocalTime().ToString());
            do {
                XmlNode currentNode = (XmlNode) elementList[index];
                XmlNode currentNodeCanonical = (XmlNode) elementListCanonical[index]; 
                XmlNodeList childNodes = currentNode.ChildNodes;
                XmlNodeList childNodesCanonical = currentNodeCanonical.ChildNodes; 
                for (int i = 0; i < childNodes.Count; i++)
                {
                    Console.WriteLine(i);
                    elementList.Add(childNodes[i]);
                    elementListCanonical.Add(childNodesCanonical[i]); 

                    if (NodeInList(childNodes[i], nodeList)) {
                        MarkNodeAsIncluded(childNodesCanonical[i]);
                    } 

                    XmlAttributeCollection attribNodes = childNodes[i].Attributes; 
                    if (attribNodes != null) { 
                        for (int j = 0; j < attribNodes.Count; j++) {
                            if (NodeInList(attribNodes[j], nodeList)) { 
                                MarkNodeAsIncluded(childNodesCanonical[i].Attributes.Item(j));
                            }
                        }
                    }
                }
                index++; 
            } while (index < elementList.Count);
            Console.WriteLine("SON" + DateTime.UtcNow.ToLocalTime().ToString());
        }
 
        /*internal byte[] GetBytes() {
            StringBuilder sb = new StringBuilder();
            m_c14nDoc.Write(sb, DocPosition.BeforeRootElement, m_ancMgr);
            UTF8Encoding utf8 = new UTF8Encoding(false); 
            return utf8.GetBytes(sb.ToString());
        } */
    } 
} 
