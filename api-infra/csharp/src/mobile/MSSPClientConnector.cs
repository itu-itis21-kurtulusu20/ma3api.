using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
   public interface MSSPClientConnector
    {
        void setCertificateInitials(UserIdentifier aUserID);
        byte[] sign(byte[] dataToBeSigned, SigningMode aMode,
                UserIdentifier aUserID, ECertificate aSigningCert,
                String informativeText, String aSigningAlg, IAlgorithmParameterSpec aParams);

        List<MultiSignResult> sign(List<byte[]> dataToBeSigned, SigningMode aMode,
               UserIdentifier aUserID, ECertificate aSigningCert,
               List<String> informativeText, String aSigningAlg, IAlgorithmParameterSpec aParams);

        ECertificate getSigningCert();
        SigningCertificate getSigningCertAttr();
        ESigningCertificateV2 getSigningCertAttrv2();
        ESignerIdentifier getSignerIdentifier();
        DigestAlg getDigestAlg();
    }
}
