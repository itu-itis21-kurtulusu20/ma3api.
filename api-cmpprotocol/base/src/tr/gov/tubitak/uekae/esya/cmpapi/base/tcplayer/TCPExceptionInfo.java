package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;


/**
 * 4.4.6.  errorMsgRep
 * o  Invalid protocol version
 * o  Invalid TCP message-type
 * o  Invalid polling reference number
 * <p>
 * The Value field of the errorMsgRep TCP-Message MUST contain:
 * <p>
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |          Error-Type           |         Data-Length           |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * \                                                               \
 * /                     Data  (variable length)                   /
 * \                                                               \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * <p>
 * Error-Type: 16 bits  A value (format described below) indicating the
 * type of the error.
 * <p>
 * Data-Length: 16 bits (unsigned integer)  Contains the length of the
 * Data field in number of octets.  Error messages not conveying
 * additional information MUST set Data-Length to 0.
 * <p>
 * Data: &lt;data-length&gt; octets
 * An UTF8 text string for user readable error messages, containing
 * additional information about the error.  Note that it does not
 * contain a terminating NULL character at the end.  It SHOULD
 * include an [RFC5646] language tag, as described in [RFC2482]
 * <p>
 * The Error-Type is in the format MMNN where M and N are hex digits
 * (0-F) and MM represents the major category and NN the minor.  The
 * major categories defined by this specification are:
 * <p>
 * ID Value   Major Categories
 * --------   ----------------
 * '01'H      TCP-Message version negotiation
 * '02'H      client errors
 * '03'H      server errors
 * <p>
 * The major categories '80'H-'FF'H are reserved for application use.
 * <p>
 */

public enum TCPExceptionInfo {
    VersionNotSupported(new byte[]{01, 01}),
    GeneralClientError(new byte[]{02, 00}),
    InvalidMessageType(new byte[]{02, 01}),
    InvalidPollID(new byte[]{02, 02}),
    GeneralServerError(new byte[]{02, 03});
    private byte[] errorBytes;

    TCPExceptionInfo(int i) {

    }

    TCPExceptionInfo(byte[] errorBytes) {

        this.errorBytes = errorBytes;
    }

    public byte[] getErrorBytes() {
        return errorBytes;
    }
}
