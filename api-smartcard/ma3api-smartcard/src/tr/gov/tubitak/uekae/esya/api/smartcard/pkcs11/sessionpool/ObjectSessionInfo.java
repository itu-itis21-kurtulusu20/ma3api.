package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool;

public class ObjectSessionInfo {

    private long session;
    private long objectId;
    private String objectLabel;

    public ObjectSessionInfo(long session, long objectId, String objectLabel) {
        this.session = session;
        this.objectId = objectId;
        this.objectLabel = objectLabel;
    }

    public long getSession() {
        return session;
    }

    public long getObjectId() {
        return objectId;
    }

    public String getObjectLabel() {
        return objectLabel;
    }
}
