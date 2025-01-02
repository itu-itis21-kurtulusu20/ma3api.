package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common.IResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.ProfileInfo;

/**
 *Generic Mobile Signature Profile response
 */
public interface IProfileResponse extends IResponse {
    /**
     * Retrieves supported mobil signature profile URI
     * @return URI of Mobil Signature profile
     */
    String getMSSUri();
    ProfileInfo getProfileInfo();
    ECertificate getCertificate();
}
