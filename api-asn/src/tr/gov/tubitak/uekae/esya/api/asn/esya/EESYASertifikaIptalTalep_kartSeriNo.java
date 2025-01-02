package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASertifikaIptalTalep_kartSeriNo;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 11:21 AM
 */
public class EESYASertifikaIptalTalep_kartSeriNo extends BaseASNWrapper<ESYASertifikaIptalTalep_kartSeriNo> {
    public EESYASertifikaIptalTalep_kartSeriNo(ESYASertifikaIptalTalep_kartSeriNo aObject) {
        super(aObject);
    }

    public EESYASertifikaIptalTalep_kartSeriNo(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYASertifikaIptalTalep_kartSeriNo());
    }

    public Asn1OctetString[] getKartSeriNolar() {
        return mObject.elements;
    }

    public void setKartSeriNolar(Asn1OctetString[] kartSeriNolar) {
        mObject.elements = kartSeriNolar;
    }
}
