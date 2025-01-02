using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.attrcert;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.attrcert;
using tr.gov.tubitak.uekae.esya.asn.cms;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
     * The signer-attributes attribute specifies additional attributes of the signer (e.g. role).
     *  It may be either:
		    • claimed attributes of the signer; or
		    • certified attributes of the signer.
    		
     * (etsi 101733v010801 5.11.3)		
     * @author aslihan.kubilay
     *
     */
    public class SignerAttributesAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_signerAttr;
        protected EClaimedAttributes mClaimedAttributes;
        protected EAttributeCertificate mAttributeCertificate;

        public SignerAttributesAttr(EClaimedAttributes aClaimedAttributes)
            : base()
        {
            mClaimedAttributes = aClaimedAttributes;

        }
        public SignerAttributesAttr(EAttributeCertificate aAttributeCertificate)
            : base()
        {
            mAttributeCertificate = aAttributeCertificate;
        }

        private SignerAttribute _signerAttrsOlustur(EClaimedAttributes aClaimedAttributes)
        {
            SignerAttribute_element[] saElement = new SignerAttribute_element[1];
            saElement[0] = new SignerAttribute_element();
            saElement[0].Set_claimedAttributes(aClaimedAttributes.getObject());
            SignerAttribute signerAttribute = new SignerAttribute(saElement);
            return signerAttribute;
        }
        /*
        private SignerAttribute _signerAttrsOlustur(EAttribute[] aAttributes)
        {
            ClaimedAttributes claimeds = new ClaimedAttributes(aAttributes.Length);
            for (int i = 0; i < aAttributes.Length; i++)
            {
                claimeds.elements[i] = aAttributes[i].getObject();
            }

            SignerAttribute_element[] saElement = new SignerAttribute_element[1];
            saElement[0] = new SignerAttribute_element();
            saElement[0].Set_claimedAttributes(claimeds);
            SignerAttribute signerAttribute = new SignerAttribute(saElement);
            return signerAttribute;

        }
         * */

        private SignerAttribute _signerAttrsOlustur(AttributeCertificate aAttributeCert)
        {
            SignerAttribute_element[] saElement = new SignerAttribute_element[1];
            saElement[0] = new SignerAttribute_element();
            saElement[0].Set_certifiedAttributes(aAttributeCert);
            SignerAttribute signerAttribute = new SignerAttribute(saElement);
            return signerAttribute;

        }

        public override void setValue()
        {
            if (mClaimedAttributes == null && mAttributeCertificate == null)
            {
                throw new CMSSignatureException("Claimed attribute or attribute certificate must be set");
            }

            //If claimed signer attributes will be added
            if (mClaimedAttributes != null)
            {
                _setValue(_signerAttrsOlustur(mClaimedAttributes));
            }
            else
            {
                _setValue(_signerAttrsOlustur(mAttributeCertificate.getObject()));
            }
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
         * Returns AttributeOID of SignerAttributesAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

        public static ESignerAttribute toESignerAttribute(EAttribute aAttribute)
        {
            return new ESignerAttribute(aAttribute.getValue(0));
        }

    }
}
