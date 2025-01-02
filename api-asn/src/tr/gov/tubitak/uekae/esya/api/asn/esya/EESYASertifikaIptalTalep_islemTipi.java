package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYASertifikaIptalTalep_islemTipi;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 11:24 AM
 */
public class EESYASertifikaIptalTalep_islemTipi extends BaseASNWrapper<ESYASertifikaIptalTalep_islemTipi> {
    protected static EESYASertifikaIptalTalep_islemTipi _askiyaAl = new EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi.askiyaAl());
    protected static EESYASertifikaIptalTalep_islemTipi _askidanIndir = new EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi.askidanIndir());
    protected static EESYASertifikaIptalTalep_islemTipi _iptal = new EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi.iptal());

    public EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi aObject) {
        super(aObject);
    }

    public int getValue(){
        return mObject.getValue();
    }
}
