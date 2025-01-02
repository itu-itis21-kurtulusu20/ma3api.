package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;


/**
 * User: zeldal.ozdemir
 * Date: 07.Eki.2010
 * Time: 13:53:20
 * 4.4.6. errorMsgRep
 * This TCP-Message is sent when a TCP-Message level protocol error is
 * detected. It is imperative that PKIError messages MUST NOT be sent
 * using this message type. Examples of TCP-Message level errors are:
 */

public class PKIMessageTCPException extends Exception {
    private TCPExceptionInfo tcpExceptionInfo;

    public PKIMessageTCPException(TCPExceptionInfo tcpExceptionInfo, String message, Throwable cause) {
        super(message, cause);
        this.tcpExceptionInfo = tcpExceptionInfo;
    }

    public PKIMessageTCPException(TCPExceptionInfo tcpExceptionInfo, String message) {
        this(tcpExceptionInfo,message,null);
    }

    public TCPExceptionInfo getTcpException() {
        return tcpExceptionInfo;
    }

    @Override
    public String toString() {
        return "PKIMessageTCPException{" +
                "tcpExceptionInfo=" + tcpExceptionInfo +
                '}';
    }

}
