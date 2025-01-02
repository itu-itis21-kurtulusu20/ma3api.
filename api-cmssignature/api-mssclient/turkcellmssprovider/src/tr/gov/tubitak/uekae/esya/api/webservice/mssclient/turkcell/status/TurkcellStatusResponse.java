package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.status;

import org.etsi.uri.TS102204.v1_1_2.MSS_StatusRespType;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusResponse;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;

/**
 * Turkcell mobile signature status response implementation
 * @see IStatusResponse
 */
public class TurkcellStatusResponse implements IStatusResponse {
    MSS_StatusRespType _trcellStatusResp = null;

    public TurkcellStatusResponse(MSS_StatusRespType aStatusResponse) {
        _trcellStatusResp = aStatusResponse;
    }

    public String getMSISDN() {
        return _trcellStatusResp.getMobileUser().getMSISDN();
    }

    public Status getStatus() {
        return new Status(_trcellStatusResp.getStatus().getStatusCode().getValue().toString(), _trcellStatusResp.getStatus().getStatusMessage());
    }

    public byte[] getSignature() {
        return _trcellStatusResp.getMSS_Signature().getBase64Signature();
    }
}
