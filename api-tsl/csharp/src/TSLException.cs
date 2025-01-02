using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.tsl
{
    public class TSLException : Exception
    {
        public TSLException()
        {
        }

        public TSLException(String aMessage):base(aMessage)
        {            
        }

        public TSLException(String aMessage, Exception aCause):base(aMessage, aCause)
        {            
        }

        public TSLException(Exception aCause, String aMessage)
            : base(aMessage, aCause)
        {
        }       


        public TSLException(Exception aCause)
            : base(aCause.Message, aCause) //:))
        {            
        }
    }
}
