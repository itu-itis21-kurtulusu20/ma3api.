package tr.gov.tubitak.uekae.esya.api.infra.crl.crlincache;

import com.objsys.asn1j.runtime.Asn1InvalidEnumException;
import tr.gov.tubitak.uekae.esya.asn.x509.CRLReason;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: zeldalo
 * Date: 15.May.2009
 * Time: 14:22:13
 * To change this template use File | Settings | File Templates.
 */
public class RevokedCertificateInfo {
    private long dateInMillis;
    private byte crlReason;

    RevokedCertificateInfo(long dateInMillis, byte crlReason) {
        this.dateInMillis = dateInMillis;
        this.crlReason = crlReason;
    }

    RevokedCertificateInfo(long dateInMillis) {
        this.dateInMillis = dateInMillis;
        this.crlReason = 0;
    }

    public Date getDate() {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        date.setTimeInMillis(dateInMillis);
        return date.getTime();
    }

    public CRLReason getCrlReason() throws Asn1InvalidEnumException {
        return CRLReason.valueOf(crlReason);
    }
}
