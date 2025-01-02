package tr.gov.tubitak.uekae.esya.api.crypto.params;

import java.security.spec.ECParameterSpec;

public class ParamsWithECParameterSpec implements AlgorithmParams 
{
	private ECParameterSpec mDomainParams;

	public ParamsWithECParameterSpec(ECParameterSpec aDomainParams) 
	{
		this.mDomainParams = aDomainParams;
	}

	public ECParameterSpec getECDomainParams() {
		return mDomainParams;
	}

    public byte[] getEncoded()
    {
        // todo move form smartcard ECParameters
        throw new RuntimeException("Not implemented yet!");
    }


}
