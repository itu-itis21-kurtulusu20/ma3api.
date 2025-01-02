package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.status;

import org.etsi.uri.ts102204.v1_1.turktelekom.MSSStatusRespType;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusResponse;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;

/**
 * Turkcell mobile signature status response implementation
 * @see tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusResponse
 */
public class TurkTelekomStatusResponse implements IStatusResponse {
    MSSStatusRespType mssStatusResponse;

    public TurkTelekomStatusResponse(MSSStatusRespType mssStatusResponse) {
        this.mssStatusResponse = mssStatusResponse;
    }

    public String getMSISDN() {
        return mssStatusResponse.getMobileUser().getMSISDN();
    }

    public Status getStatus() {
        return new Status(mssStatusResponse.getStatus().getStatusCode().getValue().toString(), mssStatusResponse.getStatus().getStatusMessage());
    }

    public byte[] getSignature() {
        return mssStatusResponse.getMSSSignature().getBase64Signature();
    }
}
