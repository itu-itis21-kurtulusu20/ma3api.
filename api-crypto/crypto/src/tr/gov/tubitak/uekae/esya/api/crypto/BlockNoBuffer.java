package tr.gov.tubitak.uekae.esya.api.crypto;

/**
 * @author ayetgin
 */
public class BlockNoBuffer extends BlockBuffer
{
    static byte[] EMPTY_BYTES = new byte[0];

    public BlockNoBuffer()
    {
        super(0);
    }

    @Override
    public byte[] getRemaining()
    {
        return EMPTY_BYTES;
    }

    @Override
    public byte[] update(byte[] aBytes)
    {
        return update(aBytes, 0, aBytes.length);
    }

    @Override
    public byte[] update(byte[] aBytes, int aOffset, int aLength)
    {
        if (aBytes!=null){
            byte[] part = new byte[aLength];
            System.arraycopy(aBytes, aOffset, part, 0, aLength);
            return part;
        }

        return EMPTY_BYTES;
    }
}
