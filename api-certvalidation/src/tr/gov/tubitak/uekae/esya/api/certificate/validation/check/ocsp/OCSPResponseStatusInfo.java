package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StatusInfo;

import java.io.Serializable;

/**
 * Stores the result of the OCSP Response validation 
 *
 * @author IH
 */
public class OCSPResponseStatusInfo extends StatusInfo implements Serializable
{
    public enum OCSPResponseStatus {

        INVALID_RESPONSE, VALID, PATH_VALIDATION_FAILURE

    }

    private EOCSPResponse mOCSPResponse;

    private OCSPResponseStatus mOCSPResponseStatus;

    public OCSPResponseStatusInfo()
    {

    }

    public OCSPResponseStatusInfo(EOCSPResponse aOCSPResponse)
    {
        mOCSPResponse = aOCSPResponse;
    }

    public OCSPResponseStatus getOCSPResponseStatus()
    {
        return mOCSPResponseStatus;
    }

    public void setOCSPResponseStatus(OCSPResponseStatus aOCSPResponseStatus)
    {
        mOCSPResponseStatus = aOCSPResponseStatus;
    }

    public EOCSPResponse getOCSPResponse()
    {
        return mOCSPResponse;
    }

    public void setOCSPResponse(EOCSPResponse aOCSPCevabi)
    {
        mOCSPResponse = aOCSPCevabi;
    }
}
