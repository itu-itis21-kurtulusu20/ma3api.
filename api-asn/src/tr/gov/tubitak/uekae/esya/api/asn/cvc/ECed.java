package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.Ced;
import tr.gov.tubitak.uekae.esya.asn.util.UtilBCD;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/13/11
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ECed extends BaseASNWrapper<Ced> {
    public ECed(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new Ced());
    }

    public ECed() {
        super(new Ced());
    }

    public ECed(Calendar aStartDate) {
        super(new Ced());
        byte[] cxd = UtilBCD.date(aStartDate.get(Calendar.YEAR), aStartDate.get(Calendar.MONTH)+1, aStartDate.get(Calendar.DAY_OF_MONTH));
        setCed(cxd);
    }

    public ECed(int aYear, int aMonth, int aDay) {
        super(new Ced());
        byte[] ced = UtilBCD.date(aYear, aMonth, aDay);
        setCed(ced);
    }

    public void setCed(byte[] aCedValue) {
        getObject().value = aCedValue;
    }

    public int getLength() {
        return getByteValues().length;
    }

    public byte[] getByteValues() {
        return getObject().value;
    }

    public static byte[] getTagLen(int aLen) {
        Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
        encodeBuffer.encodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 37, aLen);
        return encodeBuffer.getMsgCopy();
    }

    public byte[] getTagLen() {
        return getTagLen(getLength());
    }

    public static ECed fromValue(byte[] aCedValue) throws ESYAException {
        ECed ced = new ECed();
        ced.setCed(aCedValue);
        return ced;
    }
}
