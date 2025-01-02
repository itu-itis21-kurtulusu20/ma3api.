using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.cms;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * The content-reference attribute is a link from one SignedData to another. It may be used to link a reply to the
 * original message to which it refers, or to incorporate by reference one SignedData into another. The contentreference
 * attribute shall be a signed attribute.
 * (etsi 101733v010801 5.10.1)
 * @author aslihan.kubilay
 *
 */
    public class ContentRefAttr : AttributeValue
    {
        //	The contentReference attribute is a link from one SignedData to
        //	another. It may be used to link a reply to the original message to
        //	which it refers, or to incorporate by reference one SignedData into
        //	another. The first SignedData MUST include a contentIdentifier signed
        //   attribute, which SHOULD be constructed as specified in section 2.7.
        //   The second SignedData links to the first by including a
        //   ContentReference signed attribute containing the content type,
        //   content identifier, and signature value from the first SignedData
        //   (rfc 2634 2.11)


        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_contentReference;

        protected Asn1ObjectIdentifier mContentType;
        protected byte[] mSignedContentIdentifier;
        protected byte[] mOriginatorSignatureValue;

        public ContentRefAttr(Asn1ObjectIdentifier aContentType, byte[] aSignedContentIdentifier, byte[] aOriginatorSignatureValue)
            : base()
        {
            //super();
            mContentType = aContentType;
            mSignedContentIdentifier = aSignedContentIdentifier;
            mOriginatorSignatureValue = aOriginatorSignatureValue;
            if (aContentType == null || aSignedContentIdentifier == null || aOriginatorSignatureValue == null)
                throw new NullParameterException("One of parameters is not set");
        }


        public override void setValue()
        {
            Asn1OctetString identifier = new Asn1OctetString(mSignedContentIdentifier);

            Asn1OctetString signature = new Asn1OctetString(mOriginatorSignatureValue);

            ContentReference cr = new ContentReference(mContentType, identifier, signature);

            _setValue(cr);
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
         * Returns AttributeOID of ContentRefAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  content reference of ArchiveTimeStampAttr attribute
         * @param aAttribute EAttribute
         * @return EContentReference
         * @throws ESYAException
         */
        public static EContentReference toContentReference(EAttribute aAttribute)
        {
            return new EContentReference(aAttribute.getValue(0));
        }

    }
}
