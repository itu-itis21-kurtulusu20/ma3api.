package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.security.spec.AlgorithmParameterSpec;

public class DTBSRetrieverSigner implements IDTBSRetrieverSigner{

    protected SignatureAlg signatureAlg;
    protected AlgorithmParameterSpec algorithmParams;
    protected byte [] dtbs;

    public DTBSRetrieverSigner(SignatureAlg signatureAlg, AlgorithmParameterSpec algorithmParams) {
        this.signatureAlg = signatureAlg;
        this.algorithmParams = algorithmParams;
    }

    @Override
    public byte[] sign(byte[] aData) throws ESYAException {
        this.dtbs = aData;
        return tempSignature;
    }

    @Override
    public byte[] getDtbs() {
        return this.dtbs;
    }

    @Override
    public String getSignatureAlgorithmStr() {
        return this.signatureAlg.getName();
    }

    @Override
    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return algorithmParams;
    }
}
