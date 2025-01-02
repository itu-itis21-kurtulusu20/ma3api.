using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile
{
    /**
 *Generic Mobile Signature Profile response
 */

    public interface IProfileResponse : IResponse
    {
        /**
     * Retrieves supported mobil signature profile URI
     * @return URI of Mobil Signature profile
     */
        String getMSSUri();

        /**
     * Retrieves mobil user profile info
     * @return mobil user profile info
     */
        ProfileInfo getProfileInfo();

        ECertificate getCertificate();
    }
}