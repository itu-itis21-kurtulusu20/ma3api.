using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    /**
     * Container for multiple related(parallel) signatures. Standard CAdES SignedData
     *      maps to this class, whereas single XAdES signature  maps to Signature.
     *
     * <p>XAdES multiple signatures are provided through &lt;signed-doc
     *      proprietary xml structure. Note that no standard tag exist for this
     *      structure and flexible nature of XML document doesn't require that
     *      either.</p>
     *
     * @author ayetgin
     * @see Signature
     */
    public interface SignatureContainer
    {
        /**
        * Create new signature that will exist in this container
        * @param certificate that signature belongs
        * @return newly created signature
        */
        Signature createSignature(ECertificate certificate);

        /**
         * Ad existing signature to this container. Due to nature of formats, XAdES
         *      signatures might be added to this container if its enveloping(data),
         *      and CAdES signature can be added if it is signing the same data
         * @param signature to be added
         * @throws SignatureException if parameter type is another signature format,
         *      if signed data cannot be found or if signed data is different for
         *      cms signature
         */
        void addExternalSignature(Signature signature);

        //void detachSignature(Signature signature) throws SignatureException;

        /**
         * @return existing signature within this container.
         */
        List<Signature> getSignatures();

        /**
         * @return signature format like XAdES, CAdES, ASiC_XAdES etc.
         */
        SignatureFormat getSignatureFormat();

        /**
         * @return signature package, if container is in a package!
         */
        SignaturePackage getPackage();

        /**
         * verify all signatures within this container
         * @return verification results of all signatures
         */
        ContainerValidationResult verifyAll();

        /**
         * Set context related parameters like configuration or working dir
         * @param context object
         */
        void setContext(Context context);

        /**
         * Get context related parameters like configuration or working dir
         * @return context object
         */
        Context getContext();

        /**
         * Check if given stream is in any known format
         * @param stream to check
         * @return check result
         * @throws SignatureException if IO error occurs
         */
        bool isSignatureContainer(Stream stream);

        /**
         * Construct signature from stream.
         * @param stream to write signature
         * @throws SignatureException if IO error occurs
         */
        void read(Stream stream);

        /**
         * Output signature(s) bytes to stream.
         * @param stream to write signature
         * @throws SignatureException if IO error occurs
         */
        void write(Stream stream);


        /**
         * Used for reaching adapted old API objects if necessary.
         * @return SignedDoc for XAdES, BaseSignedData for CAdES
         */
        Object getUnderlyingObject();

    }
}
