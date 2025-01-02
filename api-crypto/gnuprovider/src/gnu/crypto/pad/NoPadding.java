package gnu.crypto.pad;

import gnu.crypto.Registry;

public class NoPadding extends BasePad {
    /**
     * Trivial constructor for use by concrete subclasses.
     *
     */
    protected NoPadding() {
        super(Registry.NONE_PAD);
    }

    @Override
    public void setup() {
        if (blockSize < 2 || blockSize > 256) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public byte[] pad(byte[] in, int off, int length) {

        if(length % blockSize != 0)
            throw new IllegalArgumentException("Length is not equal to blocksize!");

        return new byte[0];
    }

    @Override
    public int unpad(byte[] in, int off, int len) throws WrongPaddingException {
        return 0;
    }
}


