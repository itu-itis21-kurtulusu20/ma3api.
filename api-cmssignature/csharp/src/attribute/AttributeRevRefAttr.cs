using Com.Objsys.Asn1.Runtime;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * <p>The attribute-revocation-references attribute is an unsigned attribute.
 * Only a single instance of this attribute shall occur with an electronic
 * signature. It references the full set of the ACRL or OCSP responses that
 * have been used in the validation of the attribute certificate. This attribute
 * can be used to illustrate that the verifier has taken due diligence of the
 * available revocation information
 *
 * <p>This attribute is only used when a user attribute certificate is present
 * in the electronic signature and when that attribute certificate can be
 * revoked.
 */
    public class AttributeRevRefAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_attrRevocationRefs;

        public AttributeRevRefAttr()
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
         * Returns AttributeOID of AttributeRevRefAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
    }
}
