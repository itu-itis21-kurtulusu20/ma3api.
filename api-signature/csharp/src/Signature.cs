using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
  using Algorithm = tr.gov.tubitak.uekae.esya.api.crypto.alg.IAlgorithm;
    /**
 * Signature represents one entity's signature and constructed by related
 * signature container. A container can hold multiple signatures.
 *
 * Counter(serial) signatures are constructed by the related signature.
 *
 * @see SignatureContainer#createSignature(tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate)
 * @see #createCounterSignature(tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate)
 * @author ayetgin
 */
    public interface Signature
    {
        /**
    * Set unsigned signature property signing time
    */
        void setSigningTime(DateTime? aTime);


        /**
         * Unsigned signature property signing time
         * @return signing time or null if not exist
         */
        DateTime? getSigningTime();

        /**
         * Set signature policy information
         * @param policyId policy identifier
         */
        void setSignaturePolicy(SignaturePolicyIdentifier policyId);

        /**
         * Get signature policy information if exists
         * @return policy identifier
         */
        SignaturePolicyIdentifier getSignaturePolicy();

        /*/**
         * Get signature timestamp trusted time if signature
         * is in form ES_T or advanced
         * @return signature timestamp time or null
         *
        DateTime? getSignatureTimestampTime();*/
        
        /**
          * @param type of timestamps to be returned
          * @return all timestamps of given type
          */
        List<TimestampInfo> getTimestampInfo(TimestampType type);

        /**
         * @return all timestamps in signagture (currently unsigned ones)
         */
        List<TimestampInfo> getAllTimestampInfos();

        /**
         * @return references to certificate validation data contained within ES_C
         */
        CertValidationReferences getCertValidationReferences();

        /**
         * @return certificate validation data contained within ES_XL
         */
        CertValidationValues getCertValidationValues();

        /**
         * Create signature that is signing this signature
         * @param certificate signers certificate
         * @return newly created counter signature
         * @throws SignatureException if anything goes wrong
         */
        Signature createCounterSignature(ECertificate certificate);

        /**
         * @return first level counter signatures
         */
        List<Signature> getCounterSignatures();

        /**
         * Detach/remove signature from parent. Counter signature's parent is
         *      another signature. Root level signature's parent
         *      is Signature Container.
         * @throws SignatureException if counter signature in an archived signature
         *  is tried to detach
         */
        void detachFromParent();

        /**
         * Add content to sign. Some signature formats signs single data
         * (like CAdES), and some permits signing of multiple data (lie XAdES). If
         * container permits only one data, secondary signature add data calls will
         * be ignored..
         *
         * @param aData to sign
         * @param includeContent include content in signature, false means detached
         * @throws SignatureException possibly if IO error occurs, or if called
         *      multiple times against single data signable formats
         */
        void addContent(Signable aData, bool includeContent);

        /**
         * @return contents signed by this signature
         * @throws SignatureException if content cant be found or detached CAdES
         *      content is asked for
         */
        List<Signable> getContents();

        /**
         * @return algorithm used in this signature's generation
         */
        Algorithm getSignatureAlg();

        /**
         * Create Basic signature
         * @throws SignatureException if anything goes wrong!
         */
        void sign(BaseSigner cryptoSigner);

        /**
         * Convert signature to more advanced formats like ES_BES->ES_T
         *  or ES_XL->ES_A
         * @param type signature type the conversion is to be made
         * @throws SignatureException if anything goes wrong, like timestamp service
         *      failure etc.
         */
        void upgrade(SignatureType type);

        /**
         * Verify signature and signing certificate according to policies
         *  and standards
         * @return verification result
         * @throws SignatureException if anything goes wrong
         */
        SignatureValidationResult verify();

        /**
         * Add archive timestamp to signature. From time to time (before last
         *      timestamp certificate expires, this should be called as a signature
         *      lifecycle operation.)
         * @throws SignatureException if timestamp service fails, or timestamped
         *  data could not be calculated according to IO or discovery failures.
         */
        void addArchiveTimestamp();

        /**
         * @return signature type like ES_BES, ES_XL
         */
        SignatureType? getSignatureType();

        /**
         * @return signature type like CAdES, XAdES, ASiC_XAdES etc...
         */
        SignatureFormat getSignatureFormat();

        /**
         * @return signers certificate
         */
        ECertificate getSignerCertificate();

        /**
         * @return which container this signature belongs to
         */
        SignatureContainer getContainer();
        /**
         * Used for reaching adapted old API objects if necessary.
         * @return XMLSignature for XAdES, Signer for CAdES
         */
        Object getUnderlyingObject();

    }
}
