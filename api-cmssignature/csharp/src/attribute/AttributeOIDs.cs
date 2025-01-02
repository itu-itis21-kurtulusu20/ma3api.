using Com.Objsys.Asn1.Runtime;
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * OID constants for signature attributes
 */
    public static class AttributeOIDs
    {
        public static readonly Asn1ObjectIdentifier id_aa_ets_certificateRefs = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 21 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_revocationRefs = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 22 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_attrCertificateRefs = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 44 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_attrRevocationRefs = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 45 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_certValues = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 23 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_revocationValues = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 24 });
        public static readonly Asn1ObjectIdentifier id_aa_signatureTimeStampToken = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 14 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_escTimeStamp = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 25 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_certCRLTimestamp = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 26 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_archiveTimestamp = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 27 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_archiveTimestampV2 = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 48 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_archiveTimestampV3 = new Asn1ObjectIdentifier(new int[]{ 0, 4, 0, 1733, 2, 4 });
        public static readonly Asn1ObjectIdentifier id_aa_ATSHashIndex = new Asn1ObjectIdentifier(new int[] { 0, 4, 0, 1733, 2, 5 });
        public static readonly Asn1ObjectIdentifier id_aa_signingCertificate = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 12 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_otherSigCert = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 19 });
        public static readonly Asn1ObjectIdentifier id_aa_signingCertificateV2 = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 47 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_sigPolicyId = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 15 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_commitmentType = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 16 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_signerLocation = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 17 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_signerAttr = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 18 });
        public static readonly Asn1ObjectIdentifier id_aa_ets_contentTimestamp = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 20 });
        public static readonly Asn1ObjectIdentifier id_contentType = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 3 });
        public static readonly Asn1ObjectIdentifier id_messageDigest = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 4 });
        public static readonly Asn1ObjectIdentifier id_signingTime = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 5 });
        public static readonly Asn1ObjectIdentifier id_countersignature = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 6 });
        public static readonly Asn1ObjectIdentifier id_aa_contentHint = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 4 });
        public static readonly Asn1ObjectIdentifier id_aa_mimeType = new Asn1ObjectIdentifier(new int[]{0, 4, 0, 1733, 2, 1});
        public static readonly Asn1ObjectIdentifier id_aa_contentReference = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 10 });
        public static readonly Asn1ObjectIdentifier id_aa_contentIdentifier = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 2, 7 });

        public static readonly Asn1ObjectIdentifier id_cti_ets_proofOfOrigin = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 6, 1 });
        public static readonly Asn1ObjectIdentifier id_cti_ets_proofOfReceipt = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 6, 2 });
        public static readonly Asn1ObjectIdentifier id_cti_ets_proofOfDelivery = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 6, 3 });
        public static readonly Asn1ObjectIdentifier id_cti_ets_proofOfSender = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 6, 4 });
        public static readonly Asn1ObjectIdentifier id_cti_ets_proofOfApproval = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 6, 5 });
        public static readonly Asn1ObjectIdentifier id_cti_ets_proofOfCreation = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 6, 6 });

        public static readonly Asn1ObjectIdentifier id_data = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 7, 1 });
        public static readonly Asn1ObjectIdentifier id_ct_TSTInfo = new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 9, 16, 1, 4 });

    }
}
