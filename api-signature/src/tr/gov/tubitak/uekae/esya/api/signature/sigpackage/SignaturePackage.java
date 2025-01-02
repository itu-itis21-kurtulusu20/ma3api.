package tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;

import java.io.OutputStream;
import java.util.List;

/**
 * @author ayetgin
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
    Signable addData(Signable signable, String pathInZip);

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
     * @throws SignatureException if IO error occurs
     */
    void write(OutputStream stream) throws SignatureException;

    /**
     * Update package content on filesystem
     * @throws SignatureException if IO error occurs
     */
    void write() throws SignatureException;

}
