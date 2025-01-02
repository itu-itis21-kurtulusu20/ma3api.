using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.util;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
     * The content-identifier attribute provides an identifier for the signed content, for use when a reference may be
     * later required to that content; for example, in the content-reference attribute in other signed data sent later. The
     * content-identifier shall be a signed attribute.
     * 
     * The minimal content-identifier attribute should contain a concatenation of user-specific identification
     * information (such as a user name or public keying material identification information), a GeneralizedTime string,
     * and a random number.
     * 
     * (etsi 101733v010801 5.10.2)
     * @author aslihan.kubilay
     *
     */
    public class ContentIdentifierAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_contentIdentifier;

        protected byte[] mIdentifier;

        public ContentIdentifierAttr(byte[] aIdentifier)
        {
            //super();
            mIdentifier = aIdentifier;
            if (mIdentifier == null)
                throw new NullParameterException("Identifier must be set");
        }
        /**
         * Set identifier
         */
        public override void setValue()
        {
            Asn1OctetString cidentifier = new Asn1OctetString(mIdentifier);

            _setValue(cidentifier);
        }
        /**
        * Checks whether attribute is signed or not.
        * @return True 
        */ 
        public override bool isSigned()
        {
            return true;
        }
        /**
         * Returns Attribute OID of Content Identifier attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  identifier
         * @param aAttribute EAttribute
         * @return byte []
         * @throws CMSSignatureException
         */
        public static byte[] toIdentifier(EAttribute aAttribute)
        {
            try
            {
                Asn1OctetString identifier = new Asn1OctetString();
                AsnIO.derOku(identifier, aAttribute.getValue(0));
                return identifier.mValue;
            }
            catch (Exception ex)
            {
                throw new CMSSignatureException(ex);
            }
        }
    }
}