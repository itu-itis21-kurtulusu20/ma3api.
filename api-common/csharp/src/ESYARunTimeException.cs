using System;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common//tr.gov.tubitak.uekae.esya.genel.exceptions
{
    public class ESYARuntimeException : SystemException //Javadaki RunTimeException'in .NET'teki karşılığı
    {
        public ESYARuntimeException()
        {
        }

        public ESYARuntimeException(String aMessage):base(aMessage)
        {            
        }

        public ESYARuntimeException(String aMessage, Exception aCause):base(aMessage, aCause)
        {           
        }

        public ESYARuntimeException(Exception aCause):base("", aCause)
        {            
        }
    }
}
