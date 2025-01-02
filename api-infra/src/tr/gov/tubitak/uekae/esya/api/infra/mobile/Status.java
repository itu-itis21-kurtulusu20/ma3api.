package tr.gov.tubitak.uekae.esya.api.infra.mobile;

/**
 * Represents MSS status
 */
public class Status {

    public static final Status REQUEST_OK = new Status("100", "REQUEST_OK");
    public static final Status REGISTRATION_OK = new Status("408", "REGISTRATION_OK");
    public static final Status USER_CANCEL = new Status("401", "USER_CANCEL");
    public static final Status EXPIRED_TRANSACTION = new Status("208", "EXPIRED_TRANSACTION");

    private String _statusCode;
    private String _statusMessage;

    public Status(String aStatusCode, String aStatusMessage/*, Object[] aStatusDetail*/) {
        _statusCode = aStatusCode;
        _statusMessage = aStatusMessage;
        //_statusDetail = aStatusDetail;
    }

    public String get_statusCode() {
        return _statusCode;
    }

    public int get_StatusCodeInt() {
        return Integer.parseInt(get_statusCode());
    }

    public void set_statusCode(String _statusCode) {
        this._statusCode = _statusCode;
    }

    public String get_statusMessage() {
        return _statusMessage;
    }

    public void set_statusMessage(String _statusMessage) {
        this._statusMessage = _statusMessage;
    }

    @Override
    public String toString() {
        return "Status{" +
                "_statusCode='" + _statusCode + '\'' +
                ", _statusMessage='" + _statusMessage + '\'' +
                '}';
    }

    public boolean isStatusCodeEquals(Status obj) {
        return this._statusCode.equals(obj._statusCode);
    }
}
