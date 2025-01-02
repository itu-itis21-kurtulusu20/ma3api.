using iaik.pkcs.pkcs11.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme
{
    /**
     * Used to specify the mechanism and signature input value for different algorithms
     */
    public interface ISignatureScheme
    {
        CK_MECHANISM getMechanism();
        byte[] getSignatureInput(byte[] aTobeSigned);
    }
}