package tr.gov.tubitak.uekae.esya.api.crypto.params;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

public class ParamsWithAlgorithmIdentifier implements AlgorithmParams 
{
	private EAlgorithmIdentifier mAlgIden;

    public ParamsWithAlgorithmIdentifier(AlgorithmIdentifier aAlgIden)
    {
        this.mAlgIden = new EAlgorithmIdentifier(aAlgIden);
    }

    public ParamsWithAlgorithmIdentifier(EAlgorithmIdentifier aAlgIden)
    {
        this.mAlgIden = aAlgIden;
    }

	public EAlgorithmIdentifier getAlgorithmIdentifier() {
		return mAlgIden;
	}

    public byte[] getEncoded()
    {
        return mAlgIden.getEncoded();
    }
}
