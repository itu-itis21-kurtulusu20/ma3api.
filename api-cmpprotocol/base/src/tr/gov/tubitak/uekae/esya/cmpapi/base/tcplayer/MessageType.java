package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;

/**
 * 4.4. Message-Types
 * Message-Types 0-127 are reserved and are to be issued under IANA
 * auspices. Message-types 128-255 are reserved for application use.
 * The Message-Types currently defined are:
 * ID Value Message Name
 * -------- ------------
 * ’00’H pkiReq
 * ’01’H pollRep
 * ’02’H pollReq
 * ’03’H finRep
 * ’05’H pkiRep
 * ’06’H errorMsgRep
 * If a server receives an unknown message-type, it MUST reply with an
 * InvalidMessageType errorMsgRep. If a client receives an unknown
 * message-type, it MUST abort the current CMP transaction and terminate
 * the connection.
 * The different TCP-Message-types are discussed in the following
 * sections:
 */
public enum MessageType {

    pkiReq((byte) 0x00),
    pollRep((byte) 0x01),
    pollReq((byte) 0x02),
    finRep((byte) 0x03),
    pkiRep((byte) 0x05),
    errorMsgRep((byte) 0x06);

    private byte messageTypeByte;

    MessageType(byte messageTypeByte) {

        this.messageTypeByte = messageTypeByte;
    }

    public byte getMessageTypeByte() {
        return messageTypeByte;
    }

    public static MessageType getMessageType(int messageType) {
        for (MessageType protocolType : MessageType.values()) {
            if (protocolType.getMessageTypeByte() == messageType)
                return protocolType;
        }
        return null;
    }
}
