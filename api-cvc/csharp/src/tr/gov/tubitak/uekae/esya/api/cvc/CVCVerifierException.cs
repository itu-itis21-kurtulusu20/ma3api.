using System;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    public class CVCVerifierException : ESYAException
    {
        public CVCVerifierException(String mesaj)
            : base(mesaj)
        {
        }

        public CVCVerifierException(String mesaj, Exception cause)
            : base(mesaj, cause)
        {
        }
    }
}
