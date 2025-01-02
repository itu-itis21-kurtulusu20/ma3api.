package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.signature;

import org.etsi.uri.TS102204.v1_1_2.MSS_SignatureRespType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;

/**
 * Turkcell mobile signature response implementation
 * @see ISignatureResponse
 */
public class TurkcellSignatureResponse implements ISignatureResponse {
    MSS_SignatureRespType _trcellResp = null;

    public TurkcellSignatureResponse(MSS_SignatureRespType aResponse) {
        _trcellResp = aResponse;
    }

    public Status getStatus() {
        return new Status(_trcellResp.getStatus().getStatusCode().getValue().toString(), _trcellResp.getStatus().getStatusMessage());
    }

    public byte[] getSignature() {
        return (byte[]) _trcellResp.getMSS_Signature().getBase64Signature();
    }

    public String getTransId() {
        return _trcellResp.getMSSP_TransID().toString();
    }

    public String getMSISDN() {
        return _trcellResp.getMobileUser().getMSISDN();
    }

    public byte[] getRawSignature() throws ESYAException {
        return null;
    }
}
