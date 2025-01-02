using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.profile
{
    public class VodafoneProfileResponse: IProfileResponse
    {
        public static DigestAlgorithm SIGNING_CERT_DIGEST_ALG = DigestAlgorithm.SHA_256;

        protected ProfileInfo profileInfo;

        protected ECertificate cert;

        MSSParams aParams;

        public VodafoneProfileResponse(ECertificate cert, MSSParams aParams) {
            this.cert = cert;
            this.aParams = aParams;
            this.profileInfo = generateProfileInfo(cert, aParams);
        }
        
        private ProfileInfo generateProfileInfo(ECertificate cert, MSSParams aParams) {
            ProfileInfo profileInfo = new ProfileInfo();

            profileInfo.SetSerialNumber(cert.getSerialNumberHex());
            profileInfo.SetIssuerName(cert.getIssuer().getCommonNameAttribute());
            byte [] issuerBytes =  cert.getIssuer().getEncoded();
            string issuerBase64 = Convert.ToBase64String(issuerBytes, 0, issuerBytes.Length);
            profileInfo.SetCertIssuerDN(issuerBase64);
            profileInfo.setMssProfileURI(aParams.GetMsspProfileQueryUrl());
            profileInfo.SetCertHash(Convert.ToBase64String(DigestUtil.digest(DigestAlg.SHA256, cert.getBytes())));
            profileInfo.setDigestAlg(SIGNING_CERT_DIGEST_ALG.Url);

            return profileInfo;
        }

        public Status getStatus()
        {
            return Status.REQUEST_OK;
        }

        public string getMSSUri()
        {
            return aParams.GetMsspSignatureQueryUrl();
        }

        public ProfileInfo getProfileInfo()
        {
            return profileInfo;
        }

        public ECertificate getCertificate()
        {
            return cert;
        }
    }
}