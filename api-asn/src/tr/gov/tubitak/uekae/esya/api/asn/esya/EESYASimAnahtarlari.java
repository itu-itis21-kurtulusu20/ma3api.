package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASimAnahtari;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASimAnahtarlari;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 9:03 AM
 */
public class EESYASimAnahtarlari extends BaseASNWrapper<ESYASimAnahtarlari>{

    public EESYASimAnahtarlari(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYASimAnahtarlari());
    }

    public EESYASimAnahtarlari(EESYASimAnahtari[] simAnahtaris) {
        super(new ESYASimAnahtarlari());
        mObject.elements = BaseASNWrapper.unwrapArray(simAnahtaris);
    }

    public EESYASimAnahtari[] getEsyaSimAnahtarlari() {
        EESYASimAnahtari[] eesyaSimAnahtaris= new EESYASimAnahtari[mObject.elements.length];
        for (int i = 0; i < mObject.elements.length; i++) {
            ESYASimAnahtari element = mObject.elements[i];
            eesyaSimAnahtaris[i] = new EESYASimAnahtari(element);

        }
        return eesyaSimAnahtaris;
    }
}
