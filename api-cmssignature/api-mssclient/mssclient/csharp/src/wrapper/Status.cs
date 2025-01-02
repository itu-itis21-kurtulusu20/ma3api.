using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    /**
 * Represents MSS status
 */

    public class Status
    {
        public static readonly Status REQUEST_OK = new Status("100", "REQUEST_OK");
        public static readonly Status REGISTRATION_OK = new Status("408", "REGISTRATION_OK");

        public Status(String aStatusCode, String aStatusMessage)
        {
            StatusCode = aStatusCode;
            StatusMessage = aStatusMessage;
        }

        public String StatusCode { get; set; }

        public String StatusMessage { get; set; }
    }
}