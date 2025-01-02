package tr.gov.tubitak.uekae.esya.api.smartcard.object;

public class ObjectResult {

    private long objectID;
    private boolean isSuccess;
    private String reasonIfFail;

    public long getObjectID() {
        return objectID;
    }

    public void setObjectID(long objectID) {
        this.objectID = objectID;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String getReasonIfFail() {
        return reasonIfFail;
    }

    public void setReasonIfFail(String reasonIfFail) {
        this.reasonIfFail = reasonIfFail;
    }
}
