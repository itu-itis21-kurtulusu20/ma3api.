using System;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n;
using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
    /*
	using org.w3c.dom;
	using Base64 = tr.gov.tubitak.uekae.esya.api.common.util.Base64;
	using C14nMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Canonicalizer = tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.Canonicalizer;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using NodeListImpl = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NodeListImpl;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	using DatatypeConfigurationException = javax.xml.datatype.DatatypeConfigurationException;
	using DatatypeFactory = javax.xml.datatype.DatatypeFactory;
	using XMLGregorianCalendar = javax.xml.datatype.XMLGregorianCalendar;
	using XPathFactory = javax.xml.xpath.XPathFactory;
	using XPath = javax.xml.xpath.XPath;
	using XPathExpression = javax.xml.xpath.XPathExpression;
	using XPathConstants = javax.xml.xpath.XPathConstants;*/


    /// <summary>
    /// @author ahmety
    /// date: Apr 2, 2009
    /// </summary>
    public static class XmlUtil
    {
        public static byte[] outputDOM(XmlNodeList aNodeList, C14nMethod aC14nMethod)
        {
            return C14NUtil.canonicalizeXPathNodeSet(aNodeList, aC14nMethod);
        }

        public static byte[] outputDOM(XmlNode aNode, C14nMethod aC14nMethod)
        {
            MemoryStream baos = new MemoryStream();
            BinaryWriter bw = new BinaryWriter(baos);
            C14nMethod c14n = aC14nMethod == null ? Constants.DEFAULT_REFERENCE_C14N : aC14nMethod;

            outputDOM(aNode, c14n, bw, false);
            return baos.ToArray();
        }

        public static void outputDOM(XmlNode aNode, C14nMethod aC14nMethod, BinaryWriter os, bool addPreamble)
        {
            C14nMethod c14n = aC14nMethod;
            if (c14n == null)
            {
                c14n = Constants.DEFAULT_REFERENCE_C14N;
            }
            try
            {
                if (addPreamble)
                {
                    os.Write(XmlCommonUtil.XML_PREAMBLE);
                }

                byte[] canonicalizeSubtree = C14NUtil.canonicalizeSubtree(aNode, c14n);
                os.Write(canonicalizeSubtree);
            }
            catch (Exception ex)
            {
                throw new XMLSignatureException(ex, "errors.cantCanonicalize", aNode);
            }
        }
    }
}