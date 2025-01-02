package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * User: zeldal.ozdemir
 * Date: 07.Eki.2010
 * [CMPtrans]   Kapoor, A., Tschalar, R. and T. Kause, "Internet X.509
 * Public Key Infrastructure -- Transport Protocols for
 * CMP", Work in Progress. 2004.
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                            Length                             |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |  Version = 10 |     Flags     |  Message-Type |               \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+               /
 * \                                                               \
 * /                     Value (variable length)                   /
 * \                                                               \
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * <p>
 * Length: 32 bits (unsigned integer)
 * This field contains the number of remaining octets of the TCP-
 * Message (i.e. number of octets of the Value field plus 3).  All
 * bit values in this protocol are specified to be in network byte
 * order.
 * <p>
 * Version: 8-bits (unsigned integer)
 * The version of the TCP-Message is 10 in for this document.  It
 * MUST be incremented in each future specification modification
 * e.g. changing the Flags field in a way that is not fully
 * backwards compatible.
 * <p>
 * Flags: 8 bits
 * TCP-Message specific flags as described in Section 4.3.
 * <p>
 * Message-Type: 8 bits
 * A value indicating the type of the TCP-Message.
 * <p>
 * Value: variable length
 * Message-type dependent data is stored here.  The usage of this
 * field is described along with the respective message-type
 *
 * Beware :  it might be used by all EE, RA, CA sides.
 * <p>
 */

public class CmpTcpLayer implements IConnection {
    private static final Logger logger = LoggerFactory.getLogger(CmpTcpLayer.class);
    protected BufferedInputStream bis;
    protected OutputStream os;
    private String hostIP;
    private int port;
    private int timeOutInSec = 60;
    protected byte[] defaultFixedErrorMessage = fixedClientErrorMessage;
    protected TCPExceptionInfo generalErrorType = TCPExceptionInfo.GeneralClientError;
    private Socket connection;


    /**
     * Baglanilacak server bilgisi ile olustur.
     *
     * @param hostIP server hostIP IPsi
     * @param port   server port numarası
     */
    public CmpTcpLayer(String hostIP, int port) {
        this.hostIP = hostIP;
        this.port = port;
    }

    /**
     * Baglanilacak server bilgisi ile olustur.
     *
     * @param hostIP server hostIP IPsi
     * @param port   server port numarası
     */
    public CmpTcpLayer(String hostIP, int port, int timeOutInSec) {
        this.hostIP = hostIP;
        this.port = port;
        this.timeOutInSec = timeOutInSec;
    }

    /**
     * create with existing connection, Server or RA side applications may use.
     *
     * @param connection server connection
     */
    public CmpTcpLayer(Socket connection) {
        this.connection = connection;
        try {
            this.bis = new BufferedInputStream(connection.getInputStream());
            this.os = connection.getOutputStream();
        } catch (Exception e) {
            logger.error("Error while trying to get streams: " , e);
            throw new ESYARuntimeException("Error while trying to get streams: " , e);
        }
    }

    /**
     * Connect with given configurations
     *
     */
    public void connect() {
        logger.info("Connecting to Host:" + hostIP + " Port:" + port);
        if (hostIP == null || port == -1) {
            logger.error("Invalid Connection Configurations. Host:" + hostIP + " Port:" + port);
            throw new ESYARuntimeException("Invalid Connection Configurations. Host:" + hostIP + " Port:" + port);
        }
        try {
            connection = new Socket(InetAddress.getByName(hostIP), port);
            connection.setSoTimeout(timeOutInSec * 1000);
            logger.debug("Connection is successful: " + hostIP + ":" + port);
            this.bis = new BufferedInputStream(connection.getInputStream());
            this.os = connection.getOutputStream();
        } catch (Exception e) {
            logger.error("Error while trying to connect: " + hostIP + ":" + port, e);
            throw new ESYARuntimeException("Error while trying to connect: " + hostIP + ":" + port, e);
        }
    }


    /**
     * 0                   1                   2                   3
     * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                            Length                             |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |  Version = 10 |     Flags     |  Message-Type |               \
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+               /
     * \                                                               \
     * /                     Value (variable length)                   /
     * \                                                               \
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *
     * @return
     * @throws PKIMessageTCPException
     */
    public PKIMessagePacket decodeTCPHeader() throws PKIMessageTCPException {
        try {
            PKIMessagePacket pkiMessagePacket = new PKIMessagePacket();
            DataInputStream dis = new DataInputStream(bis);
            pkiMessagePacket.setMessageLength(dis.readInt());
            byte versionByte = dis.readByte();
            byte flagByte = dis.readByte();
            byte messageTypeByte = dis.readByte();


            if (versionByte == Version.RFC4210_Compliant.getVersionByte())
                pkiMessagePacket.setVersion(Version.RFC4210_Compliant);
            else
                throw new PKIMessageTCPException(TCPExceptionInfo.VersionNotSupported,
                        "Invalid CMP Version in TCP AHeader:" + versionByte);
            if (flagByte == Flag.open.getFlagByte())
                pkiMessagePacket.setFlag(Flag.open);
            else if (flagByte == Flag.close.getFlagByte())
                pkiMessagePacket.setFlag(Flag.close);
            else {
                throw new PKIMessageTCPException(generalErrorType,
                        "Invalid CMP Flag in TCP AHeader:" + flagByte);
            }
            pkiMessagePacket.setMessageType(MessageType.getMessageType(messageTypeByte));
            if (pkiMessagePacket.getMessageType() == null)
                throw new PKIMessageTCPException(TCPExceptionInfo.InvalidMessageType,
                        "Invalid CMP MessageType in TCP AHeader:" + messageTypeByte);
            return pkiMessagePacket;

        } catch (IOException e) {
            throw new PKIMessageTCPException(generalErrorType,
                    "Invalid CMP Message AHeader: " + e.getMessage(), e);
        }

    }

    public PKIMessage decodeValueAsPKIMessage() throws IOException, Asn1Exception {
        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(bis);
        PKIMessage message = new PKIMessage();
        message.decode(decBuf);
        return message;
    }

    public PKIMessage decodeValueAsPKIMessage(byte[] value) throws IOException, Asn1Exception {
        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(value);
        PKIMessage message = new PKIMessage();
        message.decode(decBuf);
        return message;
    }

    /**
     * 0                   1                   2                   3
     * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                            Length                             |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |  Version = 10 |     Flags     |  Message-Type |               \
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+               /
     * \                                                               \
     * /                     Value (variable length)                   /
     * \                                                               \
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     *
     * @param pkiMessagePacket
     * @return
     */
    public byte[] encodeMessage(PKIMessagePacket pkiMessagePacket) {
        try {
            return pkiMessagePacket.encode();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return getFixedErrorMessage();
        }
    }

    /**
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
     *
     * @param tcpException
     * @return
     */
    public byte[] encodeErrorMessage(PKIMessageTCPException tcpException) {

        if (tcpException == null)
            return getFixedErrorMessage();
        PKIMessagePacket pkiMessagePacket = new PKIMessagePacket();
        try {
            pkiMessagePacket.setFlag(Flag.close);
            pkiMessagePacket.setVersion(Version.RFC4210_Compliant);
            pkiMessagePacket.setMessageType(MessageType.errorMsgRep);
            byte[] msgBytes = tcpException.getMessage() != null ? tcpException.getMessage().getBytes() : (new byte[0]);
            // ErrorType[2]+DataLength[2]+msgBytes[]
            ByteArrayOutputStream bous = new ByteArrayOutputStream(msgBytes.length + 4);
            DataOutputStream dos = new DataOutputStream(bous);
            dos.write(tcpException.getTcpException().getErrorBytes());
            dos.writeShort(msgBytes.length);
            dos.write(msgBytes);
            pkiMessagePacket.setValueInBytes(bous.toByteArray());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return getFixedErrorMessage();
        }
        return encodeMessage(pkiMessagePacket);

    }

    public static final byte[] fixedServerErrorMessage =
            new byte[]{00, 00, 00, 07, // Length 7
                    10,    // Version 0A
                    01,    // Flag Close
                    06,    // errorMsgRep
                    03, 00, // GeneralServerError
                    00, 00  // No Error Explanation, we sux
            };

    public static final byte[] fixedClientErrorMessage =
            new byte[]{00, 00, 00, 07, // Length 7
                    10,    // Version 0A
                    01,    // Flag Close
                    06,    // errorMsgRep
                    02, 00, // GeneralClientError
                    00, 00  // No Error Explanation, we sux
            };


    private byte[] getFixedErrorMessage() {
        return defaultFixedErrorMessage;
    }


    public void sendErrorMessage(PKIMessageTCPException e) {
        try {
            byte[] msg = encodeErrorMessage(e);
            os.write(msg);
            close();
        } catch (IOException e1) {
            throw new ESYARuntimeException("Error while trying to send Error Exception", e1);
        }
    }

/*    public void sendReponseMessage(PKIMessage message) throws CMPConnectionException {

        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

        logger.debug("Response PKIMessage is sending...");
        try {
            message.encode(encBuf);
        } catch (Asn1Exception ex) {
            logger.error("Response PKIMessage Encode Failure", ex);
            throw new CMPConnectionException("Response PKIMessage Encode Failure", ex);
        }

        try {
            PKIMessagePacket responsePacket = new PKIMessagePacket();
            responsePacket.setValueInBytes(encBuf.getMsgCopy());
            responsePacket.setFlag(Flag.open);
            responsePacket.setMessageType(MessageType.pkiRep);
            byte[] reponseBytes = encodeMessage(responsePacket);
            os.write(reponseBytes);
            os.flush();
        } catch (Exception ex) {
            logger.fatal("Stream Write Failed.", ex);
            throw new CMPConnectionException("Stream Write Failed!");
        }
        logger.debug("Response PKIMessage has sent successfully...");

    }*/

    public void sendMessage(PKIMessagePacket pkiMessagePacket) {
        logger.debug("PKIMessage is sending...");
        try {   // just in case, PKIMessage must match with value...
            pkiMessagePacket.setValueInBytes(pkiMessagePacket.getPkiMessage().getEncoded());
        } catch (Exception ex) {
            logger.error("PKIMessage Encode Failure", ex);
            throw new ESYARuntimeException("PKIMessage Encode Failure", ex);
        }

        try {
            byte[] reponseBytes = encodeMessage(pkiMessagePacket);
            os.write(reponseBytes);
            os.flush();
        } catch (Exception ex) {
            logger.error("Stream Write Failed.", ex);
            throw new ESYARuntimeException("Stream Write Failed!");
        }
        logger.info("PKIMessage has sent successfully...");
    }

    public PKIMessagePacket readPKIMessagePacket() throws PKIMessageTCPException {

        try {
            PKIMessagePacket pkiMessagePacket = new PKIMessagePacket();
            pkiMessagePacket.decode(bis);
            return pkiMessagePacket;
        } catch (Exception e) {
            throw new PKIMessageTCPException(generalErrorType,
                    "Invalid CMP Message Body: " + e.getMessage(), e);
        }
    }

    public void finish() {
        try {
            PKIMessagePacket responsePacket = new PKIMessagePacket();
            responsePacket.setFlag(Flag.close);
            responsePacket.setMessageType(MessageType.finRep);
            responsePacket.setValueInBytes(new byte[]{0});
            byte[] reponseBytes = encodeMessage(responsePacket);
            os.write(reponseBytes);
            close();
        } catch (Exception ex) {
            logger.warn("Stream Write Failed While Trying to Finish CMP Protocol, " +
                    "But we continue as normal, Cause: " + ex.getMessage(), ex);
        }
        logger.debug("CMP Protocol has finished");
    }

    public void close() {
        try {
            os.flush();
        } catch (IOException e) {
            logger.warn("Warning in CmpTcpLayer", e);
        }
        try {
            os.close();
        } catch (IOException e) {
            logger.warn("Warning in CmpTcpLayer", e);
        }
        try {
            bis.close();
        } catch (IOException e) {
            logger.warn("Warning in CmpTcpLayer", e);
        }
    }

    public void setTimeOutInSec(int timeOutInSec) {
        this.timeOutInSec = timeOutInSec;
    }

    public void waitFinish() {
        try {
            PKIMessagePacket messagePacket = decodeTCPHeader();
            if (messagePacket.getMessageType() == MessageType.finRep)
                logger.info("Finish Response is received, connection will close");
            else
                logger.warn("We couldnt get finsh response, Response is:" + messagePacket);
            close();
        } catch (Exception e) {
            logger.warn("Error while expecting finish response.", e);
        }
    }

    public Socket getConnection() {
        return connection;
    }

    public void setDefaultFixedErrorMessage(byte[] defaultFixedErrorMessage) {
        this.defaultFixedErrorMessage = defaultFixedErrorMessage;
    }

    public void setGeneralErrorType(TCPExceptionInfo generalErrorType) {
        this.generalErrorType = generalErrorType;
    }

    public void setBis(BufferedInputStream bis) {
        this.bis = bis;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }


}
