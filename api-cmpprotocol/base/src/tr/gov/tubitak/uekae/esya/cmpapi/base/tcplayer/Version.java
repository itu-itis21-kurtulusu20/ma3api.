package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;

/**
 * 4.2. Version
 * The TCP-Message version is 10 for this document. The number has
 * deliberately been chosen to prevent [RFC2510] compliant applications
 * from treating it as a valid message type. Applications receiving a
 * version less than 10 SHOULD interpret the message as being an
 * [RFC2510] style message.
 */
public enum Version {


    RFC4210_Compliant((byte) 0x0A);

    private byte versionByte;

    Version(byte versionByte) {
        this.versionByte = versionByte;
    }

    public byte getVersionByte() {
        return versionByte;
    }
}
