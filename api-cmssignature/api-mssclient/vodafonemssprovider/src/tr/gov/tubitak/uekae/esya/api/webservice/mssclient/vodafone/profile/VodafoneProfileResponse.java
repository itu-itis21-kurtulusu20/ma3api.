package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.profile;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.DigestAlgorithm;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.ProfileInfo;

public class VodafoneProfileResponse implements IProfileResponse {

    public static DigestAlgorithm SIGNING_CERT_DIGEST_ALG = DigestAlgorithm.SHA_256;

    protected ProfileInfo profileInfo;

    protected ECertificate cert;

    MSSParams params;

    public VodafoneProfileResponse(ECertificate cert, MSSParams params) {
        this.cert = cert;
        this.params = params;
        this.profileInfo = generateProfileInfo(cert, params);
    }

    private ProfileInfo generateProfileInfo(ECertificate cert, MSSParams params) {
        try {
            ProfileInfo profileInfo = new ProfileInfo();

            profileInfo.setSerialNumber(cert.getSerialNumberHex());
            profileInfo.setIssuerName(cert.getIssuer().stringValue());
            profileInfo.setCertIssuerDN(Base64.encode(cert.getIssuer().getEncoded()));
            profileInfo.setMssProfileURI(params.getMsspProfileQueryUrl());
            profileInfo.setCertHash(Base64.encode(DigestUtil.digest(SIGNING_CERT_DIGEST_ALG.getAlgorithm(), cert.getEncoded())));
            profileInfo.setDigestAlg(SIGNING_CERT_DIGEST_ALG.getUrl());

            return profileInfo;
        } catch (CryptoException e) {
            throw new ESYARuntimeException("Can get generate profile info from certificate.", e);
        }
    }

    public String getMSSUri() {
        return params.getMsspSignatureQueryUrl();
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public ECertificate getCertificate() {
        return cert;
    }

    public Status getStatus() {
        return Status.REQUEST_OK;
    }
}
