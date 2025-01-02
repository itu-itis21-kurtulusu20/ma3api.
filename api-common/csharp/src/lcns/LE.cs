using System;

namespace tr.gov.tubitak.uekae.esya.api.common.license
{
    public class LE : Exception
    {
        public LE()
        { }

        public LE(String aMessage)
            : base(aMessage)
        {
            //super(aMessage);
        }

        public LE(String aMessage, Exception aCause)
            : base(aMessage, aCause)
        {
            //super(aMessage, aCause);
        }

        public LE(Exception aCause)
            : base(aCause.Message, aCause)
        {
            //super(aCause);
        }
    }
}
