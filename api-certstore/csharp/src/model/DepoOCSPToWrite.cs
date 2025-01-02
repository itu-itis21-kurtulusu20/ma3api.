
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoOCSPToWrite : DepoOCSP
    {
        private byte[] mOCSPResponse;

        public byte[] getOCSPResponse()
        {
            return mOCSPResponse;
        }

        public void setOCSPResponse(byte[] aOCSPResponse)
        {
            mOCSPResponse = aOCSPResponse;
        }
    }
}
