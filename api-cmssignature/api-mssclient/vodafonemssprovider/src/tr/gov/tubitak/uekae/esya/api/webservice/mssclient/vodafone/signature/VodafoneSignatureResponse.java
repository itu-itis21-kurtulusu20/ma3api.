package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.signature;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;

public class VodafoneSignatureResponse implements ISignatureResponse {
    String transId;
    String msisdn;
    byte[] rawSignature;
    Status status;

    public VodafoneSignatureResponse(String transId, String msisdn, byte [] rawSignature, Status status) {
        this.transId = transId;
        this.msisdn = msisdn;
        this.rawSignature = rawSignature;
        this.status = status;
    }

    public String getTransId() {
        return transId;
    }

    public String getMSISDN() {
        return msisdn;
    }

    public byte[] getSignature() {
        throw new ESYARuntimeException("Vodafone do not support CAdES signature");
    }

    public Status getStatus() {
        return status;
    }

    public byte[] getRawSignature() throws ESYAException {
        return rawSignature;
    }

    public byte[] getCertBytes() {
        return null;
    }
}
