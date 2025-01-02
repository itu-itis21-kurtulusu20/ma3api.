package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder.KeyFinder;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PSSParameterSpec;

public class SignatureSchemeFactory
{	
	public static ISignatureScheme getSignatureScheme(boolean aIsSigning,String aSigningAlg,AlgorithmParameterSpec aParamSpec
			, long [] aMechanisms, KeyFinder aKeyFinder)
	throws SmartCardException, PKCS11Exception
	{	
		if(aSigningAlg.contains("ISO9796"))
		{
			int keyLenght = aKeyFinder.getKeyLength() >> 3;
			return new Iso9796_2_SC1(aSigningAlg, keyLenght);
		}
		else if(aSigningAlg.startsWith("RSAPSS"))
		{
			int modBits = aKeyFinder.getKeyLength();
			
			PSSParameterSpec params = null;
			if(aParamSpec==null)
				params = PSSParameterSpec.DEFAULT;
			else if(aParamSpec instanceof PSSParameterSpec)
				params = (PSSParameterSpec) aParamSpec;
			else if(aParamSpec instanceof RSAPSSParams)
				params = ((RSAPSSParams) aParamSpec).toPSSParameterSpec();
			else
				throw new SmartCardException("Sadece PSSParameterSpec ve RSAPSSParams tipi desteklenmektedir");
			
			
			RSAPSS_SS rsapss = new RSAPSS_SS(aSigningAlg, params, modBits, aMechanisms);
			rsapss.init(aIsSigning);
			return rsapss;
		}
		else if(aSigningAlg.contains("RSA"))
		{
			return new Rsa_SS(aSigningAlg, aMechanisms);
		}
		else if(aSigningAlg.contains("ECDSA"))
		{
			return new ECDSA_SS(aSigningAlg, aMechanisms);
		}
		throw new SmartCardException("Algorithm is not supported.");
		
	}
	
}
