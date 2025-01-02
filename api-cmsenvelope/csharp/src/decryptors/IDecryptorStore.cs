using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.parameters;
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors
{
    public interface IDecryptorStore
    {
        byte[] decrypt(ECertificate aCert, IDecryptorParams aParams);

        ECertificate[] getEncryptionCertificates();
    }
}
