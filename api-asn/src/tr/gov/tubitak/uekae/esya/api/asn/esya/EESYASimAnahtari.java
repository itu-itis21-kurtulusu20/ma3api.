package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASimAnahtari;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 9:11 AM
 */
public class EESYASimAnahtari extends BaseASNWrapper<ESYASimAnahtari> {
    public EESYASimAnahtari(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYASimAnahtari());
    }

    public EESYASimAnahtari(ESYASimAnahtari aObject) {
        super(aObject);
    }

    public EESYASimAnahtari(long anahNo, EAlgorithmIdentifier hashAlg, EAlgorithmIdentifier simetrikAlg, byte[] anahtarBytes) {
        super(new ESYASimAnahtari(anahNo, hashAlg.getObject(), simetrikAlg.getObject(), anahtarBytes));
    }

    public byte[] getAnahtarBytes(){
        return mObject.anahtarBytes.value;
    }

    public long getAnahtarNo(){
        return mObject.anahNo.value;
    }

    public EAlgorithmIdentifier getAlgorithmIdentifier(){
        return new EAlgorithmIdentifier(mObject.simetrikAlg);
    }
}
