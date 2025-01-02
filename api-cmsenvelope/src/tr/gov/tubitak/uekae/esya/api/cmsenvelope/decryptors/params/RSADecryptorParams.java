package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params;


import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

public class RSADecryptorParams implements IDecryptorParams
{
	private byte [] encryptedKey;
	private EAlgorithmIdentifier algorithmIdentifier;
	
	public RSADecryptorParams(byte []encryptedKey)
	{
		this.encryptedKey = encryptedKey;
	}
	
	public byte [] getEncryptedKey()
	{
		return encryptedKey;
	}

	public EAlgorithmIdentifier getAlgorithmIdentifier() {
		return algorithmIdentifier;
	}

	public void setAlgorithmIdentifier(EAlgorithmIdentifier algorithmIdentifier) {
		this.algorithmIdentifier = algorithmIdentifier;
	}

	public Pair<CipherAlg,AlgorithmParams> getCipherAlg() throws CryptoException {
		return CipherAlg.fromAlgorithmIdentifier(getAlgorithmIdentifier());
	}



}
