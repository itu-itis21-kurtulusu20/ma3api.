using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    public class EMSSPException : ESYAException
    {
        private Status status;


        public EMSSPException(String aMessage)
            : base(aMessage)
        {
        }

        public EMSSPException(Status aResultStatus)
        {
            this.status = aResultStatus;
        }

        public EMSSPException(String aMessage,Status aResultStatus):base(aMessage)
        {
            this.status = aResultStatus;
        }

        public EMSSPException(String aMessage,Status aResultStatus,Exception aCause):base(aMessage, aCause)
        {
            this.status = aResultStatus;
        }

        public EMSSPException(String aMessage, Exception aCause)
            : base(aMessage, aCause)
        {
            
        }
        public Status getStatus()
        {
            return status;
        }

        public void setStatus(Status status)
        {
            this.status = status;
        }
        public EMSSPException(Status aResultStatus,Exception aCause)
            : base(aCause.Message, aCause)
        {
            this.status = aResultStatus;
        }
    }
}
