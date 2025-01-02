using System;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.cms;


namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
    public class MultiMobileSignerForOne : MobileSigner
    {
        MultiMobileSigner multiMobileSigner;
        String informativeText;

        int index;

        public MultiMobileSignerForOne(MultiMobileSigner multiMobileSigner, String informativeText, int index) : base(informativeText)
        {
            this.multiMobileSigner = multiMobileSigner;
            this.informativeText = informativeText;
            this.index = index;
        }

        public override IAlgorithmParameterSpec getAlgorithmParameterSpec()
        {
            return multiMobileSigner.getMobileSigner().getAlgorithmParameterSpec();
        }

        public override String getSignatureAlgorithmStr()
        {
            MobileSigner signer = multiMobileSigner.getMobileSigner();
            String str = signer.getSignatureAlgorithmStr();
            return multiMobileSigner.getMobileSigner().getSignatureAlgorithmStr();
        }

        public override byte[] sign(byte[] bytes)
        {
            DigestAlg digestAlg = SignatureAlg.fromName(getSignatureAlgorithmStr()).getDigestAlg();
            calculateFingerPrint(digestAlg, bytes);
            return multiMobileSigner.sign(bytes, informativeText, index);
        }

        public override ESignerIdentifier getSignerIdentifier()
        {
            return multiMobileSigner.getMobileSigner().getSignerIdentifier();
        }

        public override DigestAlg getDigestAlg()
        {
            return multiMobileSigner.getMobileSigner().getDigestAlg();
        }

        public override SigningCertificate getSigningCertAttr()
        {
            return multiMobileSigner.getMobileSigner().getSigningCertAttr();
        }

        public override ESigningCertificateV2 getSigningCertAttrv2()
        {
            return multiMobileSigner.getMobileSigner().getSigningCertAttrv2();
        }

        public override ECertificate getSigningCert()
        {
            return multiMobileSigner.getMobileSigner().getSigningCert();
        }
    }
}