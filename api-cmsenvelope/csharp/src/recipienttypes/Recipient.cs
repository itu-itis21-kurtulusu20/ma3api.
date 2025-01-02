
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes
{
    public interface Recipient
    {
        void calculateAndSetEncyptedKey(byte[] symmetricKey);
    }
}
