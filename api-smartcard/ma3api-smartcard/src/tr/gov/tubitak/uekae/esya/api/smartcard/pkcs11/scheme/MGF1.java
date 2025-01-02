/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import java.security.DigestException;
import java.security.MessageDigest;

/**
 * @author aslihan.kubilay
 *
 */
public class MGF1 
{
	
	private MessageDigest mDigest = null;
	
	
	public MGF1(MessageDigest aMD)
	{
		mDigest = aMD;
		mDigest.reset();
	}
	

	public byte[] generateMask(byte[] aSeed,int aMaskLen)
	throws DigestException
	{
		byte[]  mask = new byte[aMaskLen];
		
		int mgfhLen = mDigest.getDigestLength();
		byte[]  hashBuf = new byte[mgfhLen];
		byte[]  C = new byte[4];
	    int counter = 0;
	    
	    /*if(aMaskLen < mgfhLen)
	    {
	    	ItoOSP(counter, C);

            mDigest.update(aSeed);
            mDigest.update(C);
            mDigest.digest(hashBuf, 0, mgfhLen);
            
            System.arraycopy(hashBuf, 0, mask, 0, aMaskLen);
            return mask;
	    }*/
	    
	    while (counter < (aMaskLen / mgfhLen))
        {
            ItoOSP(counter, C);

            mDigest.update(aSeed);
            mDigest.update(C);
            mDigest.digest(hashBuf, 0, mgfhLen);

            System.arraycopy(hashBuf, 0, mask, counter * mgfhLen, mgfhLen);

            counter++;
        }
	    
	    
	    if ((counter * mgfhLen) < aMaskLen)
        {
	    	ItoOSP(counter, C);

            mDigest.update(aSeed);
            mDigest.update(C);
            mDigest.digest(hashBuf, 0, mgfhLen);

            System.arraycopy(hashBuf, 0, mask, counter * mgfhLen, mask.length - (counter * mgfhLen));
        }
	    
	    return mask;
		
	}
	
	private void ItoOSP(int i,byte[]  sp)
	{
	    sp[0] = (byte)(i >>> 24);
	    sp[1] = (byte)(i >>> 16);
	    sp[2] = (byte)(i >>> 8);
	    sp[3] = (byte)(i >>> 0);
	}
	

}
