package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.x509.PrivateKeyUsagePeriod;

import java.util.Calendar;

/**
 * Created by ramazan.girgin on 26.12.2016.
 */
public class EPrivateKeyUsagePeriodExtension extends BaseASNWrapper<PrivateKeyUsagePeriod> implements ExtensionType {

    public EPrivateKeyUsagePeriodExtension() {
        super(new PrivateKeyUsagePeriod());
    }

    public EPrivateKeyUsagePeriodExtension(PrivateKeyUsagePeriod aObject) {
        super(aObject);
    }

    public EPrivateKeyUsagePeriodExtension(byte[] encoded) throws ESYAException {
        super(encoded,new PrivateKeyUsagePeriod());
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_privateKeyUsagePeriod, aCritic, this);
    }

    public void setNotBefore(Calendar notBefore){
        Asn1GeneralizedTime generalTime = new Asn1GeneralizedTime();
        try {
            generalTime.setTime(notBefore);
            generalTime.setFraction("");
        } catch (Asn1Exception ex) {
            throw new ESYARuntimeException("Time olustururken tarih hesaplanamadi.", ex);
        }
        mObject.notBefore = generalTime;
    }

    public void setNotAfter(Calendar notAfter){
        Asn1GeneralizedTime generalTime = new Asn1GeneralizedTime();
        try {
            generalTime.setTime(notAfter);
            generalTime.setFraction("");
        } catch (Asn1Exception ex) {
            throw new ESYARuntimeException("Time olustururken tarih hesaplanamadi.", ex);
        }
        mObject.notAfter = generalTime;
    }

    public Calendar getNotAfter() {
        if(mObject == null || mObject.notAfter == null)
            return null;
        return mObject.notAfter.getTime();
    }

    public Calendar getNotBefore(){
        if(mObject == null || mObject.notBefore == null)
            return null;
        return mObject.notBefore.getTime();
    }

    public static void main(String[] args) throws ESYAException {
        EPrivateKeyUsagePeriodExtension ePrivateKeyUsagePeriod = new EPrivateKeyUsagePeriodExtension();
        Calendar notBefore = Calendar.getInstance();
        Calendar notAfter = Calendar.getInstance();
        ePrivateKeyUsagePeriod.setNotBefore(notBefore);
        notAfter.add(Calendar.YEAR,3);

        System.out.println(notBefore.getTime());
        System.out.println(notAfter.getTime());


        ePrivateKeyUsagePeriod.setNotAfter(notAfter);
        byte[] encoded = ePrivateKeyUsagePeriod.getEncoded();
        EPrivateKeyUsagePeriodExtension newPK = new EPrivateKeyUsagePeriodExtension(encoded);

        System.out.println(newPK.getNotBefore().getTime());
        System.out.println(newPK.getNotAfter().getTime());
    }
}
