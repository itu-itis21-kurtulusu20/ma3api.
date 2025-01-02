using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MobileSignatureClient
{
    class SignatureResult
    {
        private Exception exception;
        private Boolean exceptionOccured;
        private String informativeText;


        public SignatureResult(Exception exception, Boolean isExceptionOccured, String informativeText)
        {
            this.exception = exception;
            this.exceptionOccured = isExceptionOccured;
            this.informativeText = informativeText;
        }


        public Exception getException()
        {
            return exception;
        }


        public Boolean isExceptionOccured()
        {
            return exceptionOccured;
        }


        public String getInformativeText()
        {
            return informativeText;
        }


        public void setException(Exception exception)
        {
            this.exception = exception;
        }


        public void setExceptionOccured(Boolean isExceptionOccured)
        {
            this.exceptionOccured = isExceptionOccured;
        }


        public void setInformativeText(String informativeText)
        {
            this.informativeText = informativeText;
        }
    }
}
