using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{
    //todo Annotation!
    //@ApiClass
    public class CertStoreException:Exception
    {
        public CertStoreException()
        { }

        public CertStoreException(String aMessage):base(aMessage)
        {
            //super(aMessage);
        }

        public CertStoreException(String aMessage, Exception aCause):base(aMessage, aCause)
        {
            //super(aMessage, aCause);
        }        
    }
}
