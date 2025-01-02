package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;

public class EMSSPException extends ESYAException
{
    private Status status;

    public EMSSPException(String aMessage)
    {
        super(aMessage);
    }

    public EMSSPException(Status aResultStatus)
    {
        this.status = aResultStatus;
    }

    public EMSSPException(String aMessage,Status aResultStatus)
    {
        super(aMessage);
        this.status = aResultStatus;
    }

    public EMSSPException(String aMessage,Status aResultStatus,Exception aCause)
    {
        super(aMessage);
        this.status = aResultStatus;
    }

    public EMSSPException(String aMessage, Exception aCause)
    {
        super(aMessage,aCause);

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public EMSSPException(Status aResultStatus,Exception aCause)
    {
        super(aCause);
        this.status = aResultStatus;
    }

    @Override
    public String toString() {
        return "EMSSPException{" +
                "cause=" + super.toString() +
                ", status=" + status +
                '}';
    }
}
