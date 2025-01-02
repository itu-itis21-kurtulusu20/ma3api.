package dev.esya.api.cmssignature.smartcard.hsm;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;

import java.security.spec.AlgorithmParameterSpec;

public class SynchronisedBaseSigner implements BaseSigner
{
    BaseSigner signer;

    public SynchronisedBaseSigner(BaseSigner signer)
    {
        this.signer = signer;
    }

    @Override
    public synchronized byte[] sign(byte[] aData) throws ESYAException
    {
        return signer.sign(aData);
    }

    @Override
    public String getSignatureAlgorithmStr()
    {
        return signer.getSignatureAlgorithmStr();
    }

    @Override
    public AlgorithmParameterSpec getAlgorithmParameterSpec()
    {
        return signer.getAlgorithmParameterSpec();
    }
}
