package tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;

import java.io.File;

/**
 * Interface for signature package provider APIs
 * @author ayetgin
 */
public interface SignaturePackageProvider {


    SignaturePackage createPackage(Context context, PackageType packageType, SignatureFormat format);

    SignaturePackage readPackage(Context context, File file) throws SignatureException;

    boolean supportsPackageType(PackageType packageType);

}
