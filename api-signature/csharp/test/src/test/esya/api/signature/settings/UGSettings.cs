

using System;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.signature.util;

namespace test.esya.api.signature.settings
{
    public class UGSettings : TestSettings
    {
        private String pfxPath = "E:\\ahmet\\prj\\MA3API\\MA3\\api-signature\\testresources\\ahmet.yetgin_890111@ug.net.pfx";
        private String pfxPass = "890111";

        private PfxSigner signer;

        public UGSettings()
        {
            signer = new PfxSigner(Algorithms.SIGNATURE_RSA_SHA256, pfxPath, pfxPass);
        }

        public String getBaseDir()
        {
            return null;
        }

        public BaseSigner getSigner()
        {
            return signer;
        }

        public ECertificate getSignersCertificate()
        {
            return signer.getSignersCertificate();
        }

        public Signable getContent()
        {
            return new SignableBytes(Encoding.ASCII.GetBytes("test data"), "data.txt", "text/plain");
        }
    }
}
