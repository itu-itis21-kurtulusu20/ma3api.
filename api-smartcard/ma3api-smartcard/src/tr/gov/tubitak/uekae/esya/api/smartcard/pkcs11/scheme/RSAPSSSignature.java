package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public class RSAPSSSignature {

	protected static Logger logger = LoggerFactory.getLogger(RSAPSSSignature.class);

	 public boolean verifySignature(byte [] aSignature, PublicKey aPublicKey, byte [] aHash, MessageDigest aDigester, int aSlen) throws IllegalStateException {
	      if (aPublicKey == null) {
	         throw new IllegalStateException();
	      }
//	      byte[] S = decodeSignature(sig);
	      byte[] S = aSignature;
	      // 1. If the length of the signature S is not k octets, output 'signature
	      //    invalid' and stop.
	      int modBits = ((RSAPublicKey) aPublicKey).getModulus().bitLength();
	      int k = (modBits + 7) / 8;
	      if (S.length != k) {
	         return false;
	      }
	      // 2. Convert the signature S to an integer signature representative s:
	      //    s = OS2IP(S).
	      BigInteger s = new BigInteger(1, S);
	      // 3. Apply the RSAVP verification primitive to the public key (n, e) and
	      //    the signature representative s to produce an integer message
	      //    representative m: m = RSAVP((n, e), s).
	      //    If RSAVP outputs 'signature representative out of range,' then
	      //    output 'signature invalid' and stop.
	      BigInteger m = null;
	      try {
	         m = RSA.verify(aPublicKey, s);
	      } catch (IllegalArgumentException e) {
	      	 logger.warn("Warning in RSAPSSSignature", e);
	         return false;
	      }
	      // 4. Convert the message representative m to an encoded message EM of
	      //    length emLen = CEILING((modBits - 1)/8) octets, where modBits is
	      //    equal to the bit length of the modulus: EM = I2OSP(m, emLen).
	      //    Note that emLen will be one less than k if modBits - 1 is divisible
	      //    by 8. If I2OSP outputs 'integer too large,' then output 'signature
	      //    invalid' and stop.
	      int emBits = modBits - 1;
	      int emLen = (emBits + 7) / 8;
	      byte[] EM = m.toByteArray();
	     
	      if (EM.length > emLen) {
	         return false;
	      } else if (EM.length < emLen) {
	         byte[] newEM = new byte[emLen];
	         System.arraycopy(EM, 0, newEM, emLen - EM.length, EM.length);
	         EM = newEM;
	      }
	      // 5. Apply the EMSA-PSS decoding operation to the message M and the
	      //    encoded message EM: Result = EMSA-PSS-Decode(M, EM, emBits). If
	      //    Result = 'consistent,' output 'signature verified.' Otherwise,
	      //    output 'signature invalid.'
	      byte[] mHash = aHash;
	      boolean result = false;
	      try {
	    	  RSAPSS pss = new RSAPSS();
	         result = pss.decode(mHash, EM, emBits, aSlen, aDigester);
	      } catch (IllegalArgumentException e) {
			  logger.warn("Warning in RSAPSSSignature", e);
	         result = false;
	      } catch (SmartCardException e) {
			  logger.warn("Warning in RSAPSSSignature", e);
			result = false;
		}
	      return result;
	   }
}
