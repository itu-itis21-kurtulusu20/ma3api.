package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAVTVeri;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 9:19 AM
 */
public class EESYAVTVeri extends BaseASNWrapper<ESYAVTVeri>{
    public EESYAVTVeri(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYAVTVeri());
    }

    public EESYAVTVeri(long anahtarNo, byte[] sifreli) {
        super(new ESYAVTVeri(anahtarNo,sifreli));
    }

    public long getAnahtarNo(){
        return mObject.anahNo.value;
    }

    public byte[] getVeri(){
        return mObject.veri.value;
    }
}
