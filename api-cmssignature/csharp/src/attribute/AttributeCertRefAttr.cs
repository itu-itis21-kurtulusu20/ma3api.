using Com.Objsys.Asn1.Runtime;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * <p>The attribute-certificate-references attribute is an unsigned attribute.
 * It references the full set of AA certificates that have been used to validate
 * the attribute certificate. Only a single instance of this attribute shall
 * occur with an electronic signature.
 *
 * <p>This attribute is only used when a user attribute certificate is present 
 * in the electronic signature.
 */
    public class AttributeCertRefAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_attrCertificateRefs;

        public AttributeCertRefAttr()
            : base()
        {
        }

        public override void setValue()
        {
            //TODO
        }
        /**
        * Checks whether attribute is signed or not.
        * @return false 
        */ 
        public override bool isSigned()
        {
            return false;
        }
        /**
         * Returns AttributeOID of AttributeCertRefAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

    }
}
