using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography.Xml;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.core;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.ms;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n
{
    using CanonicalXmlNodeList = tr.gov.tubitak.uekae.esya.api.xmlsignature.ms.CanonicalXmlNodeList;
    public static class C14NUtil
    {
        public static byte[] canonicalizeXmlDocument(XmlDocument xmlDocument, C14nMethod aC14nMethod)
        {
            C14nMethod c14n = (aC14nMethod == null) ? Constants.DEFAULT_REFERENCE_C14N : aC14nMethod;
            try
            {
                Transform transform = TransformFactory.Instance.CreateTransform(c14n.URL);
                transform.LoadInput(xmlDocument);
                MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));
                return outputStream.ToArray();
            }
            catch (Exception ex)
            {
                throw new XMLSignatureException(ex, "errors.cantCanonicalize", "Node");
            }
        }

        public static byte[] canonicalizeSubtreeValidation(XmlNode aNode,C14nMethod aC14nMethod)
        {
            C14nMethod c14n = (aC14nMethod == null) ? Constants.DEFAULT_REFERENCE_C14N : aC14nMethod;
            try
            {
                Transform transform = TransformFactory.Instance.CreateTransform(c14n.URL);

                MemoryStream memStream = new MemoryStream();
                XmlDocument testDoc = new XmlDocument();
                testDoc.Normalize();
                testDoc.LoadXml(aNode.OuterXml);
                testDoc.Save(memStream);
                byte[] array = memStream.ToArray();
                string readeble = Encoding.UTF8.GetString(array);
                //readeble = readeble.Replace("\r", "");
                
                string[] strings = readeble.Split('\r');              
                char [] trims = new char[3];
                trims[0] = '\r';
                trims[1] = ' ';
                trims[2] = '\n';
                StringBuilder builder = new StringBuilder();
                
                foreach (string s in strings)
                {
                    string writeStr = s;
                    writeStr = writeStr.TrimStart(trims);
                    writeStr = writeStr.TrimEnd(trims);
                    builder.AppendLine(writeStr);
                }
                readeble = builder.ToString().TrimEnd(new char[] { '\n' });
                testDoc.LoadXml(readeble);
                transform.LoadInput(testDoc);
                MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));
                return outputStream.ToArray();
            }
            catch (Exception ex)
            {
                throw new XMLSignatureException(ex, "errors.cantCanonicalize", "Node");
            }
        }

        public static byte[] canonicalizeSubtreeReference(XmlNode aNode, C14nMethod aC14nMethod)
        {
            C14nMethod c14n = (aC14nMethod == null) ? Constants.DEFAULT_REFERENCE_C14N : aC14nMethod;
            try
            {
                Transform transform = TransformFactory.Instance.CreateTransform(c14n.URL);
               
                XmlDocument doc = null;
                doc = new XmlDocument();
                doc.PreserveWhitespace = true;
                doc.LoadXml(aNode.OuterXml);
                // add namespace nodes
                foreach (XmlAttribute attr in aNode.SelectNodes("namespace::*"))
                {
                    if (attr.LocalName == "xml")
                        continue;
                    if (attr.OwnerElement == aNode)
                        continue;
                    doc.DocumentElement.SetAttributeNode(doc.DocumentElement.OwnerDocument.ImportNode(attr, true) as XmlAttribute);
                }
                transform.LoadInput(doc);
                /// ////////////
                MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));
                return outputStream.ToArray();
            }
            catch (Exception ex)
            {
                throw new XMLSignatureException(ex, "errors.cantCanonicalize", "Node");
            }
        }

        // bu fonksiyonun eski hali asagida, ben toparlayip duzelttim, artik document'i de
        // canonicalize edebiliyor, eskiden sadece element ile sinirliydi
        // benzeri icin bkz. xmlsignature.document.DOMDocument getTransformedDocument
        public static byte[] canonicalizeSubtree(XmlNode aNode, C14nMethod aC14nMethod)
        {
            // ortak kod
            // eger aC14NMethod null degilse o olsun, null ise DEFAULT olsun
            C14nMethod c14NMethod = aC14nMethod ?? Constants.DEFAULT_REFERENCE_C14N;
            Transform transform = TransformFactory.Instance.CreateTransform(c14NMethod.URL);

            // eger bir XmlElement ise
            if(aNode.NodeType.CompareTo(XmlNodeType.Element)==0)
            {   
                String baseURI = aNode.OwnerDocument.BaseURI;   
                XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseURI);
                transform.Resolver = resolver;

                XmlElement element = aNode as XmlElement;
                XmlDocument doc = MsUtils.PreProcessElementInput(element, resolver, baseURI);
                CanonicalXmlNodeList namespaces = MsUtils.GetPropagatedAttributes(element);
                MsUtils.AddNamespaces(doc.DocumentElement, namespaces);
                
                transform.LoadInput(doc);
            }

            // eger XmlDocument'in kendisi ise
            else if(aNode.NodeType.CompareTo(XmlNodeType.Document)==0)
            {
                // empty string as base uri, meaning the whole document, i guess... :)
                XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), "");
                transform.Resolver = resolver;
                transform.LoadInput(aNode);
            }

            // ortak kod devam
            MemoryStream outputStream = (MemoryStream) transform.GetOutput(typeof (Stream));
            return outputStream.ToArray();

        }

        public static byte[] canonicalizeXPathNodeSet(XmlNodeList aNodeList, C14nMethod aC14nMethod)
        {            
            /*HashSet<XmlNode> setNodeList = XmlUtil.convertNodelistToSet(aNodeList);
            XmlDocument ownerDocument = XmlUtil.getOwnerDocument(setNodeList);*/
                     
            C14nMethod c14n = (aC14nMethod == null) ? Constants.DEFAULT_REFERENCE_C14N : aC14nMethod;
            try
            {
                Transform transform = TransformFactory.Instance.CreateTransform(c14n.URL);
                transform.LoadInput(aNodeList);
                MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));
               
                byte[] retData =  outputStream.ToArray();
                return retData;
            }
            catch (Exception ex)
            {
                throw new XMLSignatureException(ex, "errors.cantCanonicalize", "Node");
            }
        }
    }
}
