package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASimetrikAnahtar;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

/**
 * User: zeldal.ozdemir
 * Date: 1/27/11
 * Time: 4:55 PM
 */
public class EESYASimetrikAnahtar extends BaseASNWrapper<ESYASimetrikAnahtar>{

    public EESYASimetrikAnahtar(EAlgorithmIdentifier algorithm, byte[] simetrikAnahtar) {
        super(new ESYASimetrikAnahtar(algorithm.getObject(), simetrikAnahtar));
    }

    public EESYASimetrikAnahtar(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYASimetrikAnahtar());
    }

    public byte[] getSimetrikAnahtar(){
        return mObject.simetrikAnahtar.value;
    }

    public EAlgorithmIdentifier getAlgorithm(){
        return new EAlgorithmIdentifier(mObject.algorithm);
    }
}
