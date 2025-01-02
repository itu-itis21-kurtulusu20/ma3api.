using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
    public class Status
    {
        private String _statusCode;
        private String _statusMessage;

        public Status(String aStatusCode, String aStatusMessage)
        {
            _statusCode = aStatusCode;
            _statusMessage = aStatusMessage;
        }

        public String get_statusCode()
        {
            return _statusCode;
        }

        public void set_statusCode(String _statusCode)
        {
            this._statusCode = _statusCode;
        }

        public String get_statusMessage()
        {
            return _statusMessage;
        }

        public void set_statusMessage(String _statusMessage)
        {
            this._statusMessage = _statusMessage;
        }
    }
}
