using System;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * @author ayetgin
     */
    public interface ITimeProvider
    {
        DateTime? getCurrentTime();
    }
}
