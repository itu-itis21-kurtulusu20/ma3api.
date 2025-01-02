using System;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find
{
    public interface ICertificateFinder
    {
        byte[] find(String certSerial);
    }
}