package gnu.crypto.wrapper;

public interface Wrapper 
{
	public byte [] wrap(byte[]  in, int inOff, int inLen, byte []key) throws Exception;
	
	public byte [] unwrap(byte[]  in, int inOff, int inLen, byte []key) throws Exception;
}
