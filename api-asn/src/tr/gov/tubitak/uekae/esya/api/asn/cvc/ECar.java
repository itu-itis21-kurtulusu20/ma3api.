package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.cvc.Car;
import tr.gov.tubitak.uekae.esya.asn.util.UtilBCD;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/5/11
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class ECar extends BaseASNWrapper<Car> {

    public ECar(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new Car());
    }

    public ECar() {
        super(new Car());
    }

    //SmContext.getInstance().getMSmSertifikasi().getmCvcSmName() + aCvcsablon.getmServiceIndicator() + aCvcsablon.getmDiscretionaryData() + aCvcsablon.getmAlgorithmReference() + Calendar.getInstance().get(Calendar.YEAR)
    public ECar(String aSmName, char aServiceIndicator, char aDiscretionaryData, String aAlgorithmReference, long aYear) throws ESYAException {
        super(new Car());
        byte[] car;

        byte serviceIndicator = (byte)(aServiceIndicator & 0xF0);
        byte discretionaryData = (byte)(aDiscretionaryData & 0x0F);
        byte logicalOR = (byte)(serviceIndicator | discretionaryData);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(aYear));
        int year = calendar.get(Calendar.YEAR);

        car = ByteUtil.concatAll(StringUtil.toByteArray(aSmName), new byte[]{logicalOR},StringUtil.toByteArray(aAlgorithmReference),new byte[]{UtilBCD.year(year)});
        setCar(car);

    }

    public void setCar(byte[] aCarValue) throws ESYAException {
        if (aCarValue.length != 8)
            throw new ESYAException("Car field must be 8 bytes. Found: " + aCarValue.length);
        getObject().value = aCarValue;
    }

    public int getLength() {
        return getByteValues().length;
    }

    public byte[] getByteValues() {
        return getObject().value;
    }

    public static ECar fromValue(byte[] aCarValue) throws ESYAException {
        ECar car = new ECar();
        car.setCar(aCarValue);
        return car;
    }

    public byte[] getTagLen() {
        return getTagLen(getLength());
    }

    public static byte[] getTagLen(int aLen) {
        Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
        encodeBuffer.encodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 2, aLen);
        return encodeBuffer.getMsgCopy();
    }
}
