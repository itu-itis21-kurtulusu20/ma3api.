
//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IDigester
    {
        void update(byte[] aData);
        void update(byte[] aData, int aOffset, int aLength);
        byte[] digest();
    }
}
