package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Arrays;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

public class RSAPSS 
{
	public boolean decode(byte[] mHash, byte[] EM, int emBits, int sLen, MessageDigest aDigester)
			throws SmartCardException
			{
			      if (sLen < 0) {
			         throw new IllegalArgumentException("sLen");
			      }
			      int hLen = mHash.length;

			      if (emBits < (8*hLen + 8*sLen + 9)) 
			      {
			         throw new IllegalArgumentException("decoding error");
			      }
			      int emLen = (emBits + 7) / 8;
			      // 4. If the rightmost octet of EM does not have hexadecimal value bc,
			      //    output 'inconsistent' and stop.
			      if ((EM[EM.length - 1] & 0xFF) != 0xBC)
			      {
			         return false;
			      }
			      // 5. Let maskedDB be the leftmost emLen ? hLen ? 1 octets of EM, and let
			      //    H be the next hLen octets.
			      // 6. If the leftmost 8.emLen ? emBits bits of the leftmost octet in
			      //    maskedDB are not all equal to zero, output 'inconsistent' and stop.
			      if ((EM[0] & (0xFF << (8 - (8*emLen - emBits)))) != 0) 
			      {
			         return false;
			      }
			      byte[] DB = new byte[emLen - hLen - 1];
			      byte[] H = new byte[hLen];
			      System.arraycopy(EM, 0,                DB, 0, emLen - hLen - 1);
			      System.arraycopy(EM, emLen - hLen - 1, H,  0, hLen);
			      // 7. Let dbMask = MGF(H, emLen ? hLen ? 1).
			      
			      MGF1 mgf = new MGF1(aDigester);
			      byte[] dbMask;
				try 
				{
					dbMask = mgf.generateMask(H, emLen - hLen - 1);
				} catch (DigestException e) 
				{
					throw new SmartCardException("Mgf generation exception", e);
				}
			      // 8. Let DB = maskedDB XOR dbMask.
			      int i;
			      for (i = 0; i < DB.length; i++) {
			         DB[i] = (byte)(DB[i] ^ dbMask[i]);
			      }
			      // 9. Set the leftmost 8.emLen ? emBits bits of DB to zero.
			      DB[0] &= (0xFF >>> (8*emLen - emBits));
			      // 10. If the emLen -hLen -sLen -2 leftmost octets of DB are not zero or
			      //     if the octet at position emLen -hLen -sLen -1 is not equal to 0x01,
			      //     output 'inconsistent' and stop.
			      // IMPORTANT (rsn): this is an error in the specs, the index of the 0x01
			      // byte should be emLen -hLen -sLen -2 and not -1! authors have been
			      // advised
			      for (i = 0; i < (emLen - hLen - sLen - 2); i++) {
			         if (DB[i] != 0) {
			            return false;
			         }
			      }
			      if (DB[i] != 0x01) { // i == emLen -hLen -sLen -2
			         return false;
			      }
			      // 11. Let salt be the last sLen octets of DB.
			      byte[] salt = new byte[sLen];
			      System.arraycopy(DB, DB.length - sLen, salt, 0, sLen);
			      // 12. Let M0 = 00 00 00 00 00 00 00 00 || mHash || salt;
			      //     M0 is an octet string of length 8 + hLen + sLen with eight initial
			      //     zero octets.
			      // 13. Let H0 = Hash(M0), an octet string of length hLen.
			      byte[] H0;
			      aDigester.reset();
			      synchronized (aDigester)
			      {
			         for (i = 0; i < 8; i++) {
			        	 aDigester.update((byte) 0x00);
			         }
			         aDigester.update(mHash, 0, hLen);
			         aDigester.update(salt, 0, sLen);
			         H0 = aDigester.digest();
			      }
			      // 14. If H = H0, output 'consistent.' Otherwise, output 'inconsistent.'
			      return Arrays.equals(H, H0);
			}
}
