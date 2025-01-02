package tr.gov.tubitak.uekae.esya.api.asn.passport;

import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.passport.PassportCVCertificateBody;
import tr.gov.tubitak.uekae.esya.asn.util.UtilBCD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmet.asa on 19.09.2017.
 */
public class EPassportCVCertificateBody extends BaseASNWrapper<PassportCVCertificateBody> {

    public EPassportCVCertificateBody() {
        super(new PassportCVCertificateBody());
    }

    public EPassportCVCertificateBody(byte[] encoded) throws ESYAException {
        super(encoded, new PassportCVCertificateBody());
    }

    public EPassportCVCertificateBody(PassportCVCertificateBody aObject){
        super(aObject);
    }


    public EPassportCVCertificateBody(int cpi,
                                      byte [] aAuthorityRef,
                                      EElcPuK elcPuK,
                                      EChr chr,
                                      EChat chat,
                                      Calendar aNotBefore,
                                      Calendar aNotAfter)
    {
        super(new PassportCVCertificateBody());

        mObject.cpi = new Asn1Integer(cpi);
        mObject.car = new Asn1OctetString(aAuthorityRef);

        setElcPuk(elcPuK);

        mObject.chr = new Asn1OctetString(chr.getByteValues());


        setChat(chat);

        mObject.ced = new Asn1OctetString(UtilBCD.date(aNotBefore.get(Calendar.YEAR),
                aNotBefore.get(Calendar.MONTH) + 1, aNotBefore.get(Calendar.DAY_OF_MONTH)));
        mObject.cxd = new Asn1OctetString(UtilBCD.date(aNotAfter.get(Calendar.YEAR),
                aNotAfter.get(Calendar.MONTH) + 1, aNotAfter.get(Calendar.DAY_OF_MONTH)));

    }

    public Calendar getNotBefore() throws ESYAException
    {
        try
        {
            String dateStr = UtilBCD.getAs_yyyyMMdd(mObject.ced.value);
            Date date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }
        catch(ParseException ex)
        {
            throw new ESYAException(ex);
        }
    }


    public Calendar getNotAfter() throws ESYAException
    {
        try
        {
            String dateStr = UtilBCD.getAs_yyyyMMdd(mObject.cxd.value);
            Date date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            return calendar;
        }
        catch(ParseException ex)
        {
            throw new ESYAException(ex);
        }
    }


    public void setElcPuk(EElcPuK _elcPuk) {
        getObject().puk = _elcPuk.getObject();
    }

    public void setChat(EChat _chat) {
        getObject().chat = _chat.getObject();
    }

    public byte[] getAuthorityReferenceAsCar() {
        return getObject().car.value;
    }

    public byte[] getChrField() {
        return getObject().chr.value;
    }
}
