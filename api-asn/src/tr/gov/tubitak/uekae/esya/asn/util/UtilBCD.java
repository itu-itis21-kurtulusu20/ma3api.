package tr.gov.tubitak.uekae.esya.asn.util;

import tr.gov.tubitak.uekae.esya.api.asn.cvc.ECed;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.ECxd;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/17/11
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class UtilBCD {
    public static byte[] convertToBCD(int aValue) {
        StringBuilder stringBuilder = new StringBuilder(Integer.toString(aValue));
        if (stringBuilder.length() < 2)
            stringBuilder.insert(0, 0);
        byte[] bytes = new byte[2];
        for (int i = 0; i < stringBuilder.length(); i++) {
            bytes[i] = (byte) Integer.parseInt(stringBuilder.substring(i, i + 1));
        }
        return bytes;
    }

    public static byte[] date(int aYear, int aMonth, int aDay) {

        int year = aYear - 2000;

        byte[] yearBytes = convertToBCD(year);
        byte[] monthBytes = convertToBCD(aMonth);
        byte[] dayBytes = convertToBCD(aDay);
        byte[] date = new byte[yearBytes.length + monthBytes.length + dayBytes.length];
        System.arraycopy(yearBytes, 0, date, 0, yearBytes.length);
        //byte[] date = Arrays.copyOf(yearBytes, yearBytes.length + monthBytes.length + dayBytes.length);
        int offset = yearBytes.length;
        System.arraycopy(monthBytes, 0, date, offset, monthBytes.length);
        offset += monthBytes.length;
        System.arraycopy(dayBytes, 0, date, offset, dayBytes.length);

        return date;

    }

    public static byte year(int aYear) {
        String year = Integer.toString(aYear);
        year = year.substring(year.length() - 2, year.length());
        byte highNibble = (byte) Integer.parseInt(String.valueOf(year.charAt(0)));
        highNibble <<= 4;
        byte lowNibble = (byte) Integer.parseInt(String.valueOf(year.charAt(1)));
        return (byte) (highNibble | lowNibble);
    }

    public static void main(String[] args) {
        ECed ced = new ECed(Calendar.getInstance());
        System.out.println(getAs_yyyyMMdd(ced.getByteValues()));
    }

    public static String getAs_yyyyMMdd(byte[] cvcDateBytes) {
        if (cvcDateBytes == null || cvcDateBytes.length != 6)
            throw new IllegalArgumentException("Length of the CVC Date must be 6 !!! ");
        String yyyy = "20" + cvcDateBytes[0] + cvcDateBytes[1];
        String MM = cvcDateBytes[2] + "" + cvcDateBytes[3];
        String dd = cvcDateBytes[4] + "" + cvcDateBytes[5];
        return yyyy + MM + dd;
    }

    public static String getAs_yyyyMMdd(ECed ced) {
        return getAs_yyyyMMdd(ced.getByteValues());
    }

    public static String getAs_yyyyMMdd(ECxd cxd) {
        return getAs_yyyyMMdd(cxd.getByteValues());
    }


    public static int compare(ECed first, ECed second) {
        return getAs_yyyyMMdd(first).compareTo(getAs_yyyyMMdd(second));
    }

    public static int compare(ECxd first, ECxd second) {
        return getAs_yyyyMMdd(first).compareTo(getAs_yyyyMMdd(second));
    }
}
