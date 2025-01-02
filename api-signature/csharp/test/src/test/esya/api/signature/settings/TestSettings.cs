

using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace test.esya.api.signature.settings
{
    public interface TestSettings
    {
        BaseSigner getSigner();

        ECertificate getSignersCertificate();

        Signable getContent();
    }
}
