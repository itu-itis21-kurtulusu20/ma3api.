package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASifreliVeri;

/**
 * User: zeldal.ozdemir
 * Date: 1/28/11
 * Time: 8:28 AM
 */
public class EESYASifreliVeri extends BaseASNWrapper<ESYASifreliVeri>{
    public EESYASifreliVeri(byte[] sifreliAnahtar, byte[] sifreliVeri) {
        super(new ESYASifreliVeri(sifreliAnahtar, sifreliVeri));
    }

    public EESYASifreliVeri(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYASifreliVeri());
    }

    public byte[] getSifreliVeri(){
        return mObject.sifreliVeri.value;
    }

    public byte[] getSifreliAnahtar(){
        return mObject.sifreliAnahtar.value;
    }
}
