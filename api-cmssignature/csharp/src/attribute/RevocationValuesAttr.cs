using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * The revocation-values attribute is an unsigned attribute. Only a single instance of this attribute shall occur with
 * an electronic signature. It holds the values of CRLs and OCSP referenced in the
 * complete-revocation-references attribute.
 * (etsi 101733v010801 6.3.4)
 * 
 * @author aslihan.kubilay
 *
 */
    public class RevocationValuesAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_revocationValues;


        public RevocationValuesAttr()
            : base()
        {
        }

        //@SuppressWarnings("unchecked")
        public override void setValue()
        {
            RevocationValues values = null;

            //P_CERTIFICATE_REVOCATION_LIST parameter refers to a list which contains
            //certificates and corresponding revocation information for each certificate
            //it is an internal parameter,is not set by user
            Object list = null;
            mAttParams.TryGetValue(AllEParameters.P_CERTIFICATE_REVOCATION_LIST, out list);

            if (list != null)
            {
                List<CertRevocationInfoFinder.CertRevocationInfo> certRevListMap = null;
                try
                {
                    certRevListMap = (List<CertRevocationInfoFinder.CertRevocationInfo>)list;
                }
                catch (InvalidCastException aEx)
                {
                    throw new CMSSignatureException("P_CERTIFICATE_REVOCATION_LIST parameter is not of type List<CertRevocationInfo>", aEx);
                }

                values = AttributeUtil.createRevocationValues(certRevListMap);

            }
            else
            {
                //If the P_CERTIFICATE_REVOCATION_LIST parameter is not set, the complete-revocation-references 
                //attribute in signerinfo is used. The references are searched in certificate store to find values  
                ESignerInfo si = (ESignerInfo)mAttParams[AllEParameters.P_SIGNER_INFO];
                List<EAttribute> attrs = si.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs);
                if (attrs.Count == 0)
                    throw new CMSSignatureException("CompleteRevocationReferences attribute could not be found");

                ECompleteRevocationReferences revRefs = null;
                try
                {
                    revRefs = new ECompleteRevocationReferences(attrs[0].getValue(0));
                }
                catch (Exception aEx)
                {
                    throw new CMSSignatureException("Error while decoding attribute as CompleteRevocationReferences", aEx);
                }

                ValidationInfoResolver vir = (ValidationInfoResolver)mAttParams[EParameters.P_VALIDATION_INFO_RESOLVER];
                ValueFinderFromElsewhere vfCS = new ValueFinderFromElsewhere(vir);
                values = vfCS.findRevocationValues(revRefs).getObject();
            }

            _setValue(values);
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
         * Returns AttributeOID of RevocationValuesAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

    }
}
