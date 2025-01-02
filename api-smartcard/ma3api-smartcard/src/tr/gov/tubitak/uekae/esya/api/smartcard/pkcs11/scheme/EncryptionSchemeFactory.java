/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder.KeyFinder;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import javax.crypto.spec.OAEPParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author aslihan.kubilay
 *
 */
public class EncryptionSchemeFactory {

	public static IEncryptionScheme getEncryptionScheme(boolean aIsEncryption,String aAlgorithm,AlgorithmParameterSpec aParams, ISmartCard aSC, long aSlotID, KeyFinder aKeyFinder)
	throws SmartCardException,PKCS11Exception
	{
		if(aAlgorithm.equals(Algorithms.CIPHER_RSA_PKCS1))
		{
			return new RSAPKCS_ES();
		}
		else if(aAlgorithm.contains("OAEP"))
		{
			int modulusBits =  aKeyFinder.getKeyLength();
			
			OAEPParameterSpec oaepParams = null;
			if(aParams == null)
				oaepParams = OAEPParameterSpec.DEFAULT;
			else if(aParams instanceof OAEPParameterSpec)
				oaepParams = (OAEPParameterSpec) aParams;
			else
				throw new SmartCardException("Parametre tipi OAEPParameterSpec olmali");
				
			long [] supportedMechs = aSC.getMechanismList(aSlotID);
			RSAOAEP_ES oaeps = new RSAOAEP_ES(oaepParams,modulusBits, supportedMechs);
			oaeps.init(aIsEncryption);
			return oaeps;
		}
		else
			throw new SmartCardException("Desteklenmeyen algoritma:"+aAlgorithm);
	}
	
}
