package gnu.crypto.sig.rsa;

import gnu.crypto.Registry;
import java.security.interfaces.RSAKey;

public class EME_Encoding implements Registry{
	
	public static byte []decode (byte []encodedMessage,RSAKey key)
	{
		int modBits = key.getModulus().bitLength();
        int k = (modBits + 7) / 8;
        return decode(encodedMessage,k);
	}
	
	public static byte []decode (byte []encodedMessage,int blockSize)
	{
		byte []result = null;
		if(encodedMessage[0] == 0x00 && encodedMessage[1] == 0x02)
		{
			EME_PKCS1_V1_5 obj = EME_PKCS1_V1_5.getInstance(blockSize);
			try
			{
				result = obj.decode(encodedMessage);
			}
			catch(IllegalArgumentException ex)
			{
				result = null;
			}
		}
		//If result is null, it is not decoded.
		if(result == null && encodedMessage[0] == 0x00)
		{
			EME_OAEP obj = EME_OAEP.getInstance(blockSize);
			try
			{
				result = obj.decode(encodedMessage);
			}
			catch(IllegalArgumentException ex)
			{
				result = null;
			}
		}
		
		return result;
		
	}
	
	public static byte [] encode(String pad, int blockSize, byte []message)
	{
		byte []result = null;
		 if (pad == null) {
	         return null;
	      }
		 pad = pad.trim();
		 
		 if (pad.equalsIgnoreCase(EME_PKCS1_V1_5_PAD)){
			 EME_PKCS1_V1_5  pkcs1Encoding = EME_PKCS1_V1_5.getInstance(blockSize);
			 result = pkcs1Encoding.encode(message);
	     }else if(pad.equalsIgnoreCase(EME_OAEP_PAD)){
	    	 EME_OAEP oaepEncoding = EME_OAEP.getInstance(blockSize);
	    	 result = oaepEncoding.encode(message);
	     }
		 
		 return result;
	}
	
}

