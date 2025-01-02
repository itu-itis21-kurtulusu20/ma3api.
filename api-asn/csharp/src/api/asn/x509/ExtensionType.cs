using tr.gov.tubitak.uekae.esya.api.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public interface ExtensionType
    {
        EExtension toExtension(bool aCritic);
    }
}
