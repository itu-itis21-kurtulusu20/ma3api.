using System;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    /**
 * Contains basic parameters used to connect to related MSSP
 */

    public class MSSParams
    {
        public MSSParams()
        {
            
        }

        public MSSParams(string apId, string pwd, string dnsName)
        {
            AP_ID = apId;
            PWD = pwd;
            DnsName = dnsName;
        }

        private String _msspProfileQueryUrl;
        private String _msspSignatureQueryUrl;
        private int _connectionTimeOutMs = -1;
        private int _queryTimeOutInSeconds=180;

        public String GetMsspProfileQueryUrl()
        {
            return _msspProfileQueryUrl;
        }

        public void SetMsspProfileQueryUrl(String msspProfileQueryUrl)
        {
            this._msspProfileQueryUrl = msspProfileQueryUrl;
        }

        public String GetMsspSignatureQueryUrl()
        {
            return _msspSignatureQueryUrl;
        }

        public void SetMsspSignatureQueryUrl(String msspSignatureQueryUrl)
        {
            this._msspSignatureQueryUrl = msspSignatureQueryUrl;
        }

        private readonly String _majorVersion = "1";
        private readonly String _minorVersion = "1";

        public String MajorVersion
        {
            get { return _majorVersion; }
        }

        public String MinorVersion
        {
            get { return _minorVersion; }
        }

        public String AP_ID { get; set; }

        public String PWD { get; set; }


        public String DnsName { get; set; }

        public int ConnectionTimeOutMs
        {
            get { return _connectionTimeOutMs; }
            set { _connectionTimeOutMs = value; }
        }

        public int QueryTimeOutInSeconds
        {
            get { return _queryTimeOutInSeconds; }
            set { _queryTimeOutInSeconds = value; }
        }
    }
}