using System;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    // todo bu sinif JAVA'nin eslenigi olsun diye yazilmis galiba
    // todo ama ITimeProvider olarak bunun aynisi var ve bu hic kullanilmiyor

    public interface TimeProvider
    {
        DateTime? getCurrentTime();
    }
}
