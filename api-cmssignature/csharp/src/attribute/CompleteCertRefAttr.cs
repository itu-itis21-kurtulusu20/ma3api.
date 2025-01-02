using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.cms;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * The complete-certificate-references attribute is an unsigned attribute. It references the full set of CA
 * certificates that have been used to validate an ES with Complete validation data up to (but not including) the signer's
 * certificate.
 * (etsi 101733v010801 6.2.1)
 * @author aslihan.kubilay
 *
 */
    public class CompleteCertRefAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_certificateRefs;
        private DigestAlg mDigestAlg = DigestAlg.SHA256; //If not set by user,default sha256 is used

        public CompleteCertRefAttr()
            : base()
        {
        }

        //@SuppressWarnings("unchecked")
        public override void setValue()
        {
            //P_CERTIFICATE_REVOCATION_LIST parameter is set internally and includes certificates and the corresponding revocation information
            List<CertRevocationInfoFinder.CertRevocationInfo> list = (List<CertRevocationInfoFinder.CertRevocationInfo>)mAttParams[AllEParameters.P_CERTIFICATE_REVOCATION_LIST];

            Object digestAlgO = null;
            mAttParams.TryGetValue(AllEParameters.P_REFERENCE_DIGEST_ALG, out digestAlgO);
            if (digestAlgO != null)
            {
                try
                {
                    mDigestAlg = (DigestAlg)digestAlgO;
                }
                catch (InvalidCastException aEx)
                {
                    throw new CMSSignatureException("P_REFERENCE_DIGEST_ALG parameter is not of type DigestAlg", aEx);
                }
            }

            //Creates the complete-certificate-references attribute
            CompleteCertificateRefs refs = AttributeUtil.createCertificateReferences(list, mDigestAlg);

            _setValue(refs);

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
         * Returns AttributeOID of CompleteCertRefAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

    }
}
