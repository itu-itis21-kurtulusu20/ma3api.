using System.Collections.Generic;
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.signature.sigpackage
{
    /**
     * @author yavuz.kahveci
     */
    public interface SignaturePackage
    {
        /**
         * Add signable data to the package(ZIP)
         * Note that this method oly adds data to the zip,
         * same data should be added top signature also.
         * @return signable to be added to signature(s)
         * @param signable
         */
        Signable addData(Signable signable, string pathInZip);

        List<Signable> getDatas();
        
        /**
         * Add newly generated signature container to the package
         * Note that most packages only permits only one signature container.
         * @return newly created container
         */
        SignatureContainer createContainer();
        List<SignatureContainer> getContainers();

        PackageType getPackageType();
        SignatureFormat getSignatureFormat();

        PackageValidationResult verifyAll();

        /**
         * Output signature(s) bytes to stream.
         * @param stream to write signature
         */
        void write(Stream stream);

        /**
         * Update package content on filesystem
         */
        void write();

    }
}