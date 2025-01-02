using System;
using System.Net;

namespace tr.gov.tubitak.uekae.esya.api.common.tools
{
    public class EWebClient : WebClient
    {
        private int _timeOut = 100000; //100 sn default timeout value

        public void setTimeOut(int aTimeOut)
        {
            _timeOut = aTimeOut;
        }

        protected override WebRequest GetWebRequest(Uri aAddress)
        {
            HttpWebRequest webRequest = (HttpWebRequest)base.GetWebRequest(aAddress);
            if (webRequest == null) return null;
            webRequest.KeepAlive = false;
            webRequest.Timeout = _timeOut;
            return webRequest;
        }

        


    }
}