package tr.gov.tubitak.uekae.esya.api.common;

public class EUserException extends ESYAException {

    public static final long USER_CANCELLED = 1;
    public static final long TIMEOUT = 2;

    long errorCode;

    public EUserException(long errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

}
