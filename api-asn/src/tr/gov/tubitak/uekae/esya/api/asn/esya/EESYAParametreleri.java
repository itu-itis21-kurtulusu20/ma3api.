package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAParametre;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAParametreleri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: zeldal.ozdemir
 * Date: 2/3/11
 * Time: 11:49 AM
 */
public class EESYAParametreleri extends BaseASNWrapper<ESYAParametreleri>{
    public EESYAParametreleri(ESYAParametreleri aObject) {
        super(aObject);
    }

    public EESYAParametreleri(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYAParametreleri());
    }

    public EESYAParametreleri(EESYAParametre[] esyaParametres) throws ESYAException {
        super(new ESYAParametreleri());
        mObject.elements = BaseASNWrapper.unwrapArray(esyaParametres);
    }

    public EESYAParametre[] getEsyaParametres(){
        return BaseASNWrapper.wrapArray(mObject.elements,EESYAParametre.class);
    }

    public Map<String,byte[]> getAsMap(){
        Map<String,byte[]> retMap = new HashMap<String, byte[]>();
        EESYAParametre[] esyaParametres = getEsyaParametres();
        for (EESYAParametre esyaParametre : esyaParametres) {
               retMap.put(esyaParametre.getParamName(),esyaParametre.getParamValue());
        }
        return retMap;
    }
}
