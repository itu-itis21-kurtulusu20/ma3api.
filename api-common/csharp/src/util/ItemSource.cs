
namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public interface ItemSource<T> where T : class
    {
        bool open();
        bool close();
        bool reset();

        T nextItem();
        bool atEnd();
    }
}
