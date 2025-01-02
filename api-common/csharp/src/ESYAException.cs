using System;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common
{
    public class ESYAException:Exception
    {
        public ESYAException()
        {
        }

        public ESYAException(String aMessage):base(aMessage)
        {            
        }

        public ESYAException(String aMessage, Exception aCause):base(aMessage, aCause)
        {            
        }

        public ESYAException(Exception aCause)
            : base(aCause.Message, aCause) //:))
        {            
        }
    }
}
