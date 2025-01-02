using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.signature.sigpackage
{
    /**
     * Interface for signature package provider APIs
     * @uathor suleyman.uslu
     */
    public interface SignaturePackageProvider
    {
        SignaturePackage createPackage(Context context, PackageType packageType, SignatureFormat format);

        SignaturePackage readPackage(Context context, FileInfo file);

        bool supportsPackageType(PackageType packageType);
    }
}
