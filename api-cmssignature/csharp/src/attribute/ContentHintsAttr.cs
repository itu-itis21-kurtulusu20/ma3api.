using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
     * The content-hints attribute provides information on the innermost signed content 
     * of a multi-layer message where one content is encapsulated in another.
     * (etsi 101733v010801 5.10.3)
     * @author aslihanu
     *
     */
    public class ContentHintsAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_contentHint;

        protected EContentHints mContentHints;

        /**
        * Create ContentHintsAttr with content hints
         * @param aContentHints EContentHints
         * @throws NullParameterException
         */
        public ContentHintsAttr(EContentHints aContentHints)
        {
            //super();
            mContentHints = aContentHints;
            if (mContentHints == null)
            {
                throw new NullParameterException("Content hints must be set");
            }
        }

        /**
            * Set content hints
            */
        public override void setValue()
        {
            _setValue(mContentHints.getObject());
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
         * Returns AttributeOID of Content Hints attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  content hints
         * @param aAttribute EAttribute
         * @return
         * @throws ESYAException
         */
        public static EContentHints toContentHints(EAttribute aAttribute)
        {
            return new EContentHints(aAttribute.getValue(0));
        }
    }
}