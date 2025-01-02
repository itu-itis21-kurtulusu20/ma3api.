using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace test.esya.api.cmsenvelope
{
    class CertificateTestConstants
    {
        private static readonly string PIN = "506436";
        private static readonly string PIN_EC = "607983";
        private static readonly string PFX_PATH = "T:\\api-parent\\resources\\unit-test-resources\\pfx\\sifreleme_RSA_sura_506436.pfx";
        private static readonly string PFX_PATH_EC = "T:\\api-parent\\resources\\unit-test-resources\\pfx\\sifreleme_EC_sura_607983.pfx";

        public static Pair<ECertificate, IPrivateKey> GetRSAEncCertificateAndKey()
        {
            IPfxParser pp = Crypto.getPfxParser();
            pp.loadPfx(PFX_PATH, PIN);
            return pp.getCertificatesAndKeys()[0];
        }

        public static Pair<ECertificate, IPrivateKey> GetECEncCertificateandKey()
        {
            IPfxParser pp = Crypto.getPfxParser();
            pp.loadPfx(PFX_PATH_EC, PIN_EC);
            return pp.getCertificatesAndKeys()[0];
        }
    }
}
