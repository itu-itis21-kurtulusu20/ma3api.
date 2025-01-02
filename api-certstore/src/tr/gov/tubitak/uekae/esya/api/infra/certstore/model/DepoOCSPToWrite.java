package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

/**
 * @author ayetgin
 */
public class DepoOCSPToWrite extends DepoOCSP
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
