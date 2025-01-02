package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.Cxd;
import tr.gov.tubitak.uekae.esya.asn.util.UtilBCD;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/13/11
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class ECxd extends BaseASNWrapper<Cxd> {
    public ECxd(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new Cxd());
    }

    public ECxd() {
        super(new Cxd());
    }

    public ECxd(Calendar aEndDate) {
        super(new Cxd());
        byte[] cxd = UtilBCD.date(aEndDate.get(Calendar.YEAR), aEndDate.get(Calendar.MONTH)+1, aEndDate.get(Calendar.DAY_OF_MONTH));
        setCxd(cxd);
    }

    public ECxd(int aYear, int aMonth, int aDay) {
        super(new Cxd());
        byte[] cxd = UtilBCD.date(aYear, aMonth, aDay);
        setCxd(cxd);
    }

    public void setCxd(byte[] aCxdValue) {
        getObject().value = aCxdValue;
    }

    public int getLength() {
        return getByteValues().length;
    }

    public byte[] getByteValues() {
        return getObject().value;
    }

    public static byte[] getTagLen(int aLen) {
        Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
        encodeBuffer.encodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 36, aLen);
        return encodeBuffer.getMsgCopy();
    }

    public byte[] getTagLen() {
        return getTagLen(getLength());
    }

    public static ECxd fromValue(byte[] aCxdValue) throws ESYAException {
        ECxd cxd = new ECxd();
        cxd.setCxd(aCxdValue);
        return cxd;
    }
}
