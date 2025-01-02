package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.signature;

import org.etsi.uri.ts102204.v1_1.turktelekom.MSSSignatureRespType;
import org.etsi.uri.ts102204.v1_1.turktelekom.SignatureType;
import org.etsi.uri.ts102204.v1_1.turktelekom.StatusType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;

/**
 * Turkcell mobile signature response implementation
 * @see tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse
 */
public class TurkTelekomSignatureResponse implements ISignatureResponse {
    MSSSignatureRespType mssSignatureResp;

    public TurkTelekomSignatureResponse(MSSSignatureRespType mssSignatureResp) {
        this.mssSignatureResp = mssSignatureResp;
    }

    public Status getStatus() {
        final StatusType status = mssSignatureResp.getStatus();
        if (status == null) {
            return Status.REQUEST_OK;
        }

        return new Status(status.getStatusCode().getValue().toString(), status.getStatusMessage());
    }

    public byte[] getSignature() {
        SignatureType mssSignature = mssSignatureResp.getMSSSignature();
        if(mssSignature==null)
        {
            return null;
        }
        return (byte[])mssSignature.getBase64Signature();
    }

    public byte[] getRawSignature() throws ESYAException {
        return null;
    }

    public String getTransId() {
        return mssSignatureResp.getMSSPTransID();
    }

    public String getMSISDN() {
        return mssSignatureResp.getMobileUser().getMSISDN();
    }
}
