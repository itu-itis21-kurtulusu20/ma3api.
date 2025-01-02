package tr.gov.tubitak.uekae.esya.api.asn.esya;

import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASertifikaIptalTalep;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 11:20 AM
 */
public class EESYASertifikaIptalTalep extends BaseASNWrapper<ESYASertifikaIptalTalep>{
    public EESYASertifikaIptalTalep(ESYASertifikaIptalTalep aObject) {
        super(aObject);
    }

    public EESYASertifikaIptalTalep(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYASertifikaIptalTalep());
    }

    public EESYASertifikaIptalTalep(    EESYASertifikaIptalTalep_kartSeriNo kartSeriNo,
                                        String kayitciAdi,
                                        EESYASertifikaIptalTalep_islemTipi islemTipi) {
        super(new ESYASertifikaIptalTalep(
                kartSeriNo.getObject(),
                new Asn1UTF8String(kayitciAdi),
                islemTipi.getObject() ));
    }

    public EESYASertifikaIptalTalep_kartSeriNo getSertifikaIptalTalep_kartSeriNo(){
        return new EESYASertifikaIptalTalep_kartSeriNo(mObject.kartSeriNo);
    }

    public String getKayitciAdi(){
        return mObject.kayitciAdi.value;
    }

    public EESYASertifikaIptalTalep_islemTipi getIslemTipi(){
        return new EESYASertifikaIptalTalep_islemTipi(mObject.islemTipi);
    }

}
