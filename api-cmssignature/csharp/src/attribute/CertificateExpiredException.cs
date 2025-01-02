using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    class CertificateExpiredException : CMSSignatureException
    {
        readonly ECertificate mCert;

        public CertificateExpiredException(ECertificate cert, String msg)
            : base(msg)
        {
            mCert = cert;
        }

        public ECertificate getCertificate()
        {
            return mCert;
        }

    }
}
