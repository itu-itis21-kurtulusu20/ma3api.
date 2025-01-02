using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.common;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
     * The content-type attribute indicates the type of the signed content.(ts_101733v010801 5.7.1)
     * 
     * The content-type attribute MUST be a signed attribute (RFC 3852 11.1)
     * 
     * Even though the syntax is defined as a SET OF AttributeValue, a content-type attribute
     * MUST have a single attribute value; zero or multiple instances of AttributeValue are 
     * not permitted.(RFC 3852 11.1)
     * 
     * The SignedAttributes in a signerInfo MUST NOT include multiple instances of the 
     * content-type attribute.
     * 
     * @author aslihanu
     *
     */
    public class ContentTypeAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_contentType;

        public ContentTypeAttr()
            : base()
        {
        }


        public ContentTypeAttr(Asn1ObjectIdentifier aContentType)
            : base()
        {
            _setValue(aContentType);
        }


        public override void setValue()
        {
            Object contentType = null;
            mAttParams.TryGetValue(AllEParameters.P_CONTENT_TYPE, out contentType);

            if (contentType == null)
            {
                throw new NullParameterException("P_CONTENT_TYPE parameter is not set");
            }

            Asn1ObjectIdentifier contentTypeOID = null;
            try
            {
                contentTypeOID = (Asn1ObjectIdentifier)contentType;
            }
            catch (InvalidCastException ex)
            {
                throw new CMSSignatureException("P_CONTENT_TYPE parameter is not of type Asn1ObjectIdentifier", ex);
            }

            _setValue(contentTypeOID);
        }

        /**
        * Checks whether attribute is signed or not.
        * @return true 
        */ 
        public override bool isSigned()
        {
            return true;
        }

        /**
         * Returns AttributeOID of ContentTypeAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

        public static Asn1ObjectIdentifier toContentType(EAttribute aAttribute)
        {
            try
            {
                Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(aAttribute.getValue(0));
                Asn1ObjectIdentifier objIden = new Asn1ObjectIdentifier();
                objIden.Decode(buff);
                return objIden;
            }
            catch (Asn1Exception e)
            {
                throw new ESYAException("Asn1 decode error", e);
            }
            catch (IOException e)
            {
                throw new ESYAException("IOException", e);
            }
        }

    }
}
