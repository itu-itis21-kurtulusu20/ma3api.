package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage;

import java.io.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * see [CMPtrans]   Kapoor, A., Tschalar, R. and T. Kause, "Internet X.509
 * Public Key Infrastructure -- Transport Protocols for CMP", Work in Progress. 2004.
 */

public class PKIMessagePacket {
    /*  Length: 32 bits (unsigned integer)
            This field contains the number of remaining octets of the TCPMessage
            (i.e. number of octets of the Value field plus 3). All
            bit values in this protocol are specified to be in network byte
            order.
    */
    private int messageLength = 0;
    private Flag flag = Flag.open;
    private Version version = Version.RFC4210_Compliant;
    private MessageType messageType;
    private byte[] tcpMessageBytes;
    private byte[] valueInBytes;
    private EPKIMessage pkiMessage;

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public byte[] getTcpMessageBytes() {
        return tcpMessageBytes;
    }

    public void setTcpMessageBytes(byte[] tcpMessageBytes) {
        this.tcpMessageBytes = tcpMessageBytes;
    }

    public byte[] getValueInBytes() {
        return valueInBytes;
    }

    public void setValueInBytes(byte[] valueInBytes) {
        this.valueInBytes = valueInBytes;
    }

    public void setPKIMessage(EPKIMessage pkiMessage) {
        this.pkiMessage = pkiMessage;
        setValueInBytes(pkiMessage.getEncoded());
    }

    public EPKIMessage getPkiMessage() {
        return pkiMessage;
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
     */
    public byte[] encode() throws IOException {
        //  Version[1]+Flag[1]+MessageType[1]+Value[]
        setMessageLength(3 + getValueInBytes().length);
        // Length[4]+message[]
        ByteArrayOutputStream bous = new ByteArrayOutputStream(3 + getMessageLength());
        DataOutputStream dos = new DataOutputStream(bous);
        dos.writeInt(getMessageLength());
        dos.write(getVersion().getVersionByte());
        dos.write(getFlag().getFlagByte());
        dos.write(getMessageType().getMessageTypeByte());
        dos.write(getValueInBytes());
        setTcpMessageBytes(bous.toByteArray());
        return getTcpMessageBytes();
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
     * @throws PKIMessageTCPException
     */
    public void decodeTCPHeader(InputStream is) throws Exception {
        try {
            DataInputStream dis = new DataInputStream(is);
            setMessageLength(dis.readInt());
            byte versionByte = dis.readByte();
            byte flagByte = dis.readByte();
            byte messageTypeByte = dis.readByte();


            if (versionByte == Version.RFC4210_Compliant.getVersionByte())
                setVersion(Version.RFC4210_Compliant);
            else
                throw new PKIMessageTCPException(TCPExceptionInfo.VersionNotSupported,
                        "Invalid CMP Version in TCP AHeader:" + versionByte);
            if (flagByte == Flag.open.getFlagByte())
                setFlag(Flag.open);
            else if (flagByte == Flag.close.getFlagByte())
                setFlag(Flag.close);
            else {
                throw new Exception("Invalid CMP Flag in TCP AHeader:" + flagByte);
            }
            setMessageType(MessageType.getMessageType(messageTypeByte));
            if (getMessageType() == null)
                throw new PKIMessageTCPException(TCPExceptionInfo.InvalidMessageType,
                        "Invalid CMP MessageType in TCP AHeader:" + messageTypeByte);

        } catch (IOException e) {
            throw new Exception("Invalid CMP Message AHeader: " + e.getMessage(), e);
        }

    }

    public void decode(InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        decodeTCPHeader(bis);
        if(messageType == MessageType.finRep
                || messageType == MessageType.errorMsgRep) // yet finish response or errorMsgRep has no PKIMessage.
            return;
        setPKIMessage(new EPKIMessage(bis));
        setValueInBytes(getPkiMessage().getEncoded());
    }

    public PKIMessage decodeValueAsPKIMessage(byte[] value) throws IOException, Asn1Exception {
        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(value);
        PKIMessage message = new PKIMessage();
        message.decode(decBuf);
        return message;
    }
}

