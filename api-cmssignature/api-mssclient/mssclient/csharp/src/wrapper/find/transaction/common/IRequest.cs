using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common
{
    /**
    * Basic request methods
    */

    public interface IRequest : IMessageAbstract
    {
        /**
     * Sets new requests parameters
     * @param aParams   Request parameter
     */
        void setMSSParams(MSSParams aParams);
        /**
     * Sets service endpoint
     * @param aServiceUrl   Service endpoint Url
     */
        void setServiceUrl(String aServiceUrl);
    }
}