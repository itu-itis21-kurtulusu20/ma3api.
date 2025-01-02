
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IPfxParser
    {
        void loadPfx(string filePath, string password);
        ECertificate getFirstCertificate();
        IPrivateKey getFirstPrivateKey();
        List<Pair<ECertificate, IPrivateKey>> getCertificatesAndKeys();
        Pair<ECertificate, IPrivateKey> getFirstSigningKeyCertPair();
    }
}
