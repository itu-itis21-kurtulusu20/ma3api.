package tr.gov.tubitak.uekae.esya.api.infra.mobile;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.IDTBSRetrieverSigner;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;

public class MobileDTBSRetrieverSigner extends MobileSigner implements IDTBSRetrieverSigner {

    protected byte[] dtbs;

    public MobileDTBSRetrieverSigner(MSSPClientConnector connector, UserIdentifier aUserIden, ECertificate aSigningCert, String informativeText, String aSigningAlg, AlgorithmParameterSpec aParams) {
        super(connector, aUserIden, aSigningCert, informativeText, aSigningAlg, aParams);
    }

    @Override
    public byte[] sign(byte[] aData) throws ESYAException {
        this.dtbs = aData;
        return tempSignature;
    }

    @Override
    public ArrayList<MultiSignResult> sign(ArrayList<byte[]> aData, ArrayList<String> informativeText) throws ESYAException {
        // todo: bakılacak
        throw new NotImplementedException();
    }

    @Override
    public byte[] getDtbs() {
        return this.dtbs;
    }
}
