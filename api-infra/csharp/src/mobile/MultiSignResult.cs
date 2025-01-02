using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
    public class MultiSignResult
    {
        private byte[] signature;
        private Status status;
        private String informativeText;


        public MultiSignResult(byte[] signature, Status status, String informativeText)
        {

            this.signature = signature;
            this.status = status;
            this.informativeText = informativeText;
        }


        public byte[] getSignature()
        {
            return signature;
        }


        public void setSignature(byte[] signature)
        {
            this.signature = signature;
        }


        public Status getStatus()
        {
            return status;
        }


        public void setStatus(Status status)
        {
            this.status = status;
        }


        public String getInformativeText()
        {
            return informativeText;
        }


        public void setInformativeText(String informativeText)
        {
            this.informativeText = informativeText;
        }
    }
}
