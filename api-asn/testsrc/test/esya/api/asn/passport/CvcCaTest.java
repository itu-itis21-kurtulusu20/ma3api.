package test.esya.api.asn.passport;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.util.UtilBCD;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ahmet.asa on 20.06.2017.
 */
public class CvcCaTest {

    //Test Car field for new Pasaport Self Cv Certificates

    @Test
    public void testFieldsAsBytes() {

        String cvcCaName = "YKKKK";
        String caName = StringUtil.toString(cvcCaName.getBytes());
        char serviceIndicator = '6';
        char discreationaryData = '0';
        int year = 17;
        String algorithReference = "AA";

        byte serviceInd = (byte) (serviceIndicator & 0xF0);
        byte discData = (byte) (discreationaryData & 0x0F);
        byte logicalOr = (byte) (serviceInd | discData);

        byte[] car = ByteUtil.concatAll(StringUtil.toByteArray(caName), new byte[]{logicalOr}, StringUtil.toByteArray(algorithReference), new byte[]{UtilBCD.year(year)});


        System.out.println("*********Before nibble the bytes********* ");
        System.out.println(caName.getBytes().length);
        System.out.println((byte) serviceIndicator);
        System.out.println((byte) discreationaryData);
        System.out.println(algorithReference.getBytes().length);
        System.out.println((byte) year);

        System.out.println("*******After composed******");
        System.out.println(serviceInd);
        System.out.println(discData);
        System.out.println(logicalOr);
        System.out.println("Final car byte must be equal to eight");
        System.out.println(car.length);

    }

    @Test
    public void newPasaportFieldsTest() {
        String countryCode = "TR";
        String holderMnemonic = "CVCA";
        String cCode = StringUtil.toString(countryCode.getBytes());
        String hMnemonic = StringUtil.toString(holderMnemonic.getBytes());
        int SequenceNumber = 00001;
//        convertSeqNo(SequenceNumber);


        byte[] newCar = ByteUtil.concatAll(StringUtil.toByteArray(cCode), StringUtil.toByteArray(hMnemonic));

        System.out.println(newCar.length);


    }


    public static long batol(byte[] buff) {
        return batol(buff, false);
    }

    public static long batol(byte[] buff, boolean littleEndian) {
        ByteBuffer bb = ByteBuffer.wrap(buff);
        if (littleEndian) bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getLong();
    }

    public static byte convertSeqNo(long seqNo) {

        String testLong = Long.toString(seqNo);

        byte[] bytes = testLong.getBytes();

        System.out.println(bytes.length);

        long b = 12345;
        String s1 = String.format("%40s", Long.toBinaryString(b & 0xFF)).replace(' ', '0');

        String temp = Long.toBinaryString(b);

        System.out.println(s1);


        return 0;


    }


    public void testCountryCode(String cCode) {

        byte[] bytes = cCode.getBytes();

        System.out.println(bytes.length);

    }

    public byte[] toBytes(long a) {

        ByteBuffer buffer = ByteBuffer.allocate(5);

        buffer.putLong(a);


        return null;
    }

    @Test
    public void testLongNo() {

        String countryCode = "TR16";
        String hmnemonic = "ABCDEFG";

        Integer tt = new Integer(123456789);


        Long a = new Long(12345L);

        BigInteger te = BigInteger.valueOf(a);


        byte[] bytes = te.toByteArray();

        BigInteger number = new BigInteger("123456789123");

        number.byteValue();

        byte[] to = number.toByteArray();

        BigInteger aa = new BigInteger(to);


        System.out.println(to.length);
        System.out.println(aa);

        ByteBuffer buffer = ByteBuffer.allocate(8);

//        buffer.putLong(a);
        buffer.putDouble(1234567890);
        byte[] bytes1 = buffer.array();

//        System.out.println(bytes1[0]);
//        System.out.println(bytes1[1]);
//        System.out.println(bytes1[2]);
//        System.out.println(bytes1[3]);
//        System.out.println(bytes1[4]);
//        System.out.println(bytes1[5]);
//        System.out.println(bytes1[6]);
//        System.out.println(bytes1[7]);


    }

    @Test
    public void testCed(){

        int year = 2017;
        int month = 8;
        int day = 1;


        byte[] ced = UtilBCD.date(year, month, day);



    }



}
