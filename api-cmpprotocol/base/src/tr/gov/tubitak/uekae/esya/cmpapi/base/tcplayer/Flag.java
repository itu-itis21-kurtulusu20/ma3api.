package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;

/**
     * 4.3. Flags
     * The LSB of the Flags field is used to indicate a connection close;
     * all other bits in the Flags octet MUST be ignored by receivers, and
     * MUST be set to zero by senders.
     */
public enum Flag {

        open((byte) 0x00),
        close((byte) 0x01);

        private byte flagByte;

        Flag(byte flagByte) {
            this.flagByte = flagByte;
        }

        public byte getFlagByte() {
            return flagByte;
        }
    }
