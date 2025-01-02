package tr.gov.tubitak.uekae.esya.api.asn.x509;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1UTCTime;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

public class ETime extends BaseASNWrapper<Time> {
    public ETime(Time object) {
        super(object);
    }

    public ETime(byte[] aBytes) throws ESYAException {
        super(aBytes, new Time());
    }

    public ETime() {
        super(new Time());
    }

    public Calendar getTime() throws ESYAException {
        try {
            if (mObject.getChoiceID() == Time._UTCTIME) {
                Asn1UTCTime utcObj = (Asn1UTCTime) mObject.getElement();
                return utcObj.getTime();
            } else if (mObject.getChoiceID() == Time._GENERALTIME) {
                Asn1GeneralizedTime generalizedTimeObj = (Asn1GeneralizedTime) mObject.getElement();
                return generalizedTimeObj.getTime();
            }

            throw new ESYAException("Unknown Choice in Asn1 Time Object");
        } catch (Asn1Exception ex) {
            throw new ESYAException("Asn1Decode Error", ex);
        }
    }

    public void setGeneralTime(GregorianCalendar calendar) throws Asn1Exception {
        Asn1GeneralizedTime generalTime = new Asn1GeneralizedTime();
        generalTime.setTime(calendar);
        mObject.set_generalTime(generalTime);
    }

    public void setUTCTime(GregorianCalendar calendar) throws Asn1Exception {
        Asn1UTCTime utcTime = new Asn1UTCTime();
        utcTime.setTime(calendar);
        mObject.set_utcTime(utcTime);
    }


}
