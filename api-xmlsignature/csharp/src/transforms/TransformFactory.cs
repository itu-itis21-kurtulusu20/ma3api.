using System;
using System.Security.Cryptography;
using System.Security.Cryptography.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using XmlDsigEnvelopedSignatureTransform = tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.XmlDsigEnvelopedSignatureTransform;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.ms;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms
{
    public class TransformFactory
    {
        public static readonly string CANON_URL_c14n_OmitComments = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
        public static readonly string CANON_URL_c14n_WithComments = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
        public static readonly string CANON_URL_exc_c14n_OmitComments = "http://www.w3.org/2001/10/xml-exc-c14n#";
        public static readonly string CANON_URL_exc_c14n_WithComments = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";

        //TODO Sonraki sürümlerde bunların yazılması gerekiyor.
        public static readonly string CANON_URL_c14n11_OmitComments = "http://www.w3.org/2006/12/xml-c14n11";
        public static readonly string CANON_URL_c14n11_WithComments = "http://www.w3.org/2006/12/xml-c14n11#WithComments";

        static TransformFactory instance = new TransformFactory();

        protected TransformFactory() { }

        public static TransformFactory Instance
        {
            get { return instance; }
        }

        public Transform CreateTransform(string transformAlgorithmUri)
        {
            if (TransformType.XPATH.Url.Equals(transformAlgorithmUri))
            {                
                return new EOtherXPathTransform();
            }
            else if (TransformType.ENVELOPED.Url.Equals(transformAlgorithmUri))
            {
                return new XmlDsigEnvelopedSignatureTransform();
            }
            else if (TransformType.BASE64.Url.Equals(transformAlgorithmUri))
            {
                return new XmlDsigBase64Transform();
            }
            else if (TransformType.XSLT.Url.Equals(transformAlgorithmUri))
            {
                return new XmlDsigXsltTransform();
            }
            else if (CANON_URL_c14n_OmitComments.Equals(transformAlgorithmUri))
            {
                return new XmlDsigC14NTransform();
            }
            else if (CANON_URL_c14n_WithComments.Equals(transformAlgorithmUri))
            {
                return new XmlDsigC14NWithCommentsTransform();
            }
            else if (CANON_URL_exc_c14n_OmitComments.Equals(transformAlgorithmUri))
            {
                return new XmlDsigExcC14NTransform();
            }
            else if (CANON_URL_exc_c14n_WithComments.Equals(transformAlgorithmUri))
            {
                return new XmlDsigExcC14NWithCommentsTransform();
            }
            throw new UnsupportedOperationException("transform.unknown transform", transformAlgorithmUri);
        }
    }
}