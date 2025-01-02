package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.PrivateKey;

/**
 * @author ayetgin
 */

public abstract class Signer implements BaseSigner
{
	public abstract	 byte[] sign(byte[] aData) throws CryptoException;

    public abstract void init(PrivateKey aPrivateKey) throws CryptoException;
    public abstract void init(PrivateKey aPrivateKey, AlgorithmParams aParams) throws CryptoException;
    public abstract void reset() throws CryptoException;

	public abstract void update(byte[] aData);
	public abstract void update(byte[] aData, int aOffset, int aLength);

	public abstract SignatureAlg getSignatureAlgorithm();

	public String getSignatureAlgorithmStr()
	{
		return getSignatureAlgorithm().getName();
	}

}
