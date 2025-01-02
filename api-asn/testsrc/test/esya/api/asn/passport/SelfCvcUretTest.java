package test.esya.api.asn.passport;

import com.objsys.asn1j.runtime.*;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.passport.*;
import tr.gov.tubitak.uekae.esya.api.asn.passport.ECar;
import tr.gov.tubitak.uekae.esya.api.asn.passport.EChr;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.passport.*;
import tr.gov.tubitak.uekae.esya.asn.passport.Chat;
import tr.gov.tubitak.uekae.esya.asn.passport.ElcPuK;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.ECPrivateKey;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilBCD;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by ahmet.asa on 31.07.2017.
 */
public class SelfCvcUretTest {

    public static final byte UNCOMPRESSED_POINT_TAG = 0x04;


    public byte[] encodePoint(ECPoint ecPoint, EllipticCurve curve) {

        byte[] pointX = trimByteArray(ecPoint.getAffineX().toByteArray());
        byte[] pointY = trimByteArray(ecPoint.getAffineY().toByteArray());
        int n = 0;
        if (curve != null) {
            // get fieldsize in bytes (+7 to round up and >>3 to divide by 8)
            n = (curve.getField().getFieldSize() + 7) >> 3;
        } else {
            // Normally n is the curve field size and pointX and pointY has length n.
            // This will simply try to use this size in case we don't have access to the curve.
            n = pointX.length > pointY.length ? pointX.length : pointY.length;
        }

        // In case pointX.length or pointY.length greater
        // the points will be trimmed to the length n

        // pointX.length and pointY.length should be equal to n
        int paddingX_length = 0;
        int paddingY_length = 0;

        // If the length of x was smaller than n we need to pad x on the left with 0
        if (pointX.length < n)
            paddingX_length = n - pointX.length;

        // If the length of y was smaller than n we need to pad y on the left with 0
        if (pointY.length < n)
            paddingY_length = n - pointY.length;

        // the resulting array should be two times n (n << 1) plus 1
        byte[] encoded = new byte[1 + (n << 1)];
        // Initialize result with all zeros (needed for the padding)
        Arrays.fill(encoded, (byte) 0x00);

        // Add 0x04, required tag by the encoding
        encoded[0] = UNCOMPRESSED_POINT_TAG;

        System.arraycopy(pointX, 0, encoded, 1 + paddingX_length, n - paddingX_length);
        System.arraycopy(pointY, 0, encoded, 1 + n + paddingY_length, n - paddingY_length);

        return encoded;
    }

    protected static byte[] trimByteArray(byte[] data) {
        boolean numberFound = false;
        int pos = 0;
        // Locate the first position of a non-zero
        for (pos = 0; pos < data.length; pos++) {
            numberFound = data[pos] != 0;
            if (numberFound) {
                break;
            }
        }

        byte[] result = null;
        if (numberFound) {
            // Non-zero was found - remove leading zeroes
            result = new byte[data.length - pos];
            System.arraycopy(data, pos, result, 0, data.length - pos);
        } else {
            // Only zeroes were found - return one zero
            result = new byte[]{0x00};
        }
        return result;
    }


    /**
     * @throws ESYAException
     * @throws IOException
     * API icin reel degerler iceren sertifika profili
     */
    @Test
    public void cvcCertUret() throws ESYAException, IOException {
        PassportCVCerticate cvCerticate = new PassportCVCerticate();
//        cvCerticate.certicateBody = new PassCertificateBody();
//        cvCerticate.certicateBody.cpi = new Asn1Integer(1);
//        cvCerticate.certicateBody.car = new tr.gov.tubitak.uekae.esya.asn.passport.Car();

        new Asn1OctetString("TRCVCAEPASS00001");


    }

    @Test
    public void testEPassportCVCert()
    {
        try {

            byte[] p = StringUtil.hexToByte("A9FB57DBA1EEA9BC3E660A909D838D726E3BF623D52620282013481D1F6E5377");
            byte[] a = StringUtil.hexToByte("7D5A0975FC2C3057EEF67530417AFFE7FB8055C126DC5C6CE94A4B44F330B5D9");
            byte[] b = StringUtil.hexToByte("26DC5C6CE94A4B44F330B5D9BBD77CBF958416295CF7E1CE6BCCDC18FF8C07B6");
            byte[] g = StringUtil.hexToByte("048BD2AEB9CB7E57CB2C4B482FFC81B7AFB9DE27E1E3BD23C23A4453BD9ACE3262547EF835C3DAC4FD97F8461A14611DC9C27745132DED8E545C1D54C72F046997");
            byte[] r = StringUtil.hexToByte("A9FB57DBA1EEA9BC3E660A909D838D718C397AA3B561A6F7901E0E82974856A7");
            byte[] y = StringUtil.hexToByte("041FED84C3FFDFABFBA36A754D6258ABD33F949CDC1D28AD9648337075484648E49CC230B8A4DD95AE44CBB78178A0C7137C66EA603D0549701997685DD4265F41");


            ElcPuK puk = new ElcPuK();
            puk.oid = new Asn1ObjectIdentifier(new int[]{1, 2, 840, 10045, 4, 3, 2});
            puk.p = new Asn1OctetString(p);
            puk.a = new Asn1OctetString(a);
            puk.b = new Asn1OctetString(b);
            puk.g = new Asn1OctetString(g);
            puk.r = new Asn1OctetString(r);
            puk.y = new Asn1OctetString(y);
            puk.f = new Asn1Integer(1);


            EElcPuK elcPuK = new EElcPuK(puk);
            EChr chr = new EChr("TR", "CVCAEPASS", 1);
            EChat cat = new EChat(new int []{0, 4, 0, 127, 0, 7, 3, 1, 2, 1}, "C1");

            Calendar notAfter = Calendar.getInstance();
            notAfter.add(Calendar.YEAR, 3);


            EPassportCVCertificateBody certificateBody = new EPassportCVCertificateBody(
                    0,
                    chr.getByteValues(),
                    elcPuK,
                    chr,
                    cat,
                    Calendar.getInstance(),
                    notAfter);

            byte [] certificateBodyBytes = certificateBody.getEncoded();
            //simulate for signing--  PKCS8 RSA sign shall be use
            byte [] hashBytes = MessageDigest.getInstance("SHA-256").digest(certificateBodyBytes);

            String certificateBodyBytesStr = StringUtil.toHexString(certificateBodyBytes);

            EPassportCVCertificate passCert = new EPassportCVCertificate(hashBytes, certificateBody);

            byte [] certBytes = passCert.getEncoded();

            String certBytesStr = StringUtil.toHexString(certBytes);

            System.out.println(certBytesStr);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }



    }

    @Test
    public void testPassCVCertificate() throws ESYAException, IOException {
        PassportCVCerticate cvCerticate = new PassportCVCerticate();
        cvCerticate.certificateBody = new PassportCVCertificateBody();
        cvCerticate.certificateBody.cpi = new Asn1Integer(0);
        cvCerticate.certificateBody.car = new Asn1OctetString("TRCVCAEPASS00104");
        cvCerticate.certificateBody.puk = new ElcPuK();

        byte[] p = StringUtil.hexToByte("A9FB57DBA1EEA9BC3E660A909D838D726E3BF623D52620282013481D1F6E5377");
        byte[] a = StringUtil.hexToByte("7D5A0975FC2C3057EEF67530417AFFE7FB8055C126DC5C6CE94A4B44F330B5D9");
        byte[] b = StringUtil.hexToByte("26DC5C6CE94A4B44F330B5D9BBD77CBF958416295CF7E1CE6BCCDC18FF8C07B6");
        byte[] g = StringUtil.hexToByte("048BD2AEB9CB7E57CB2C4B482FFC81B7AFB9DE27E1E3BD23C23A4453BD9ACE3262547EF835C3DAC4FD97F8461A14611DC9C27745132DED8E545C1D54C72F046997");
        byte[] r = StringUtil.hexToByte("A9FB57DBA1EEA9BC3E660A909D838D718C397AA3B561A6F7901E0E82974856A7");
        byte[] y = StringUtil.hexToByte("041FED84C3FFDFABFBA36A754D6258ABD33F949CDC1D28AD9648337075484648E49CC230B8A4DD95AE44CBB78178A0C7137C66EA603D0549701997685DD4265F41");


        cvCerticate.certificateBody.puk.oid = new Asn1ObjectIdentifier(new int[]{1, 2, 840, 10045, 4, 3, 2});
        cvCerticate.certificateBody.puk.p = new Asn1OctetString(p);
        cvCerticate.certificateBody.puk.a = new Asn1OctetString(a);
        cvCerticate.certificateBody.puk.b = new Asn1OctetString(b);
        cvCerticate.certificateBody.puk.g = new Asn1OctetString(g);
        cvCerticate.certificateBody.puk.r = new Asn1OctetString(r);
        cvCerticate.certificateBody.puk.y = new Asn1OctetString(y);
        cvCerticate.certificateBody.puk.f = new Asn1Integer(1);


        cvCerticate.certificateBody.chr = new Asn1OctetString("TRCVCAEPASS00104");

        cvCerticate.certificateBody.chat = new Chat();
        cvCerticate.certificateBody.chat.oid = new Asn1ObjectIdentifier(new int []{0, 4, 0, 127, 0, 7, 3, 1, 2, 1});
        cvCerticate.certificateBody.chat.value = new Asn1OctetString(StringUtil.toByteArray("C1"));

        cvCerticate.certificateBody.ced = new Asn1OctetString(UtilBCD.date(2017, 01, 01));
        cvCerticate.certificateBody.cxd = new Asn1OctetString(UtilBCD.date(2018, 01, 01));

        cvCerticate.signature = new Asn1OctetString("1211221");



        Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
        int len = cvCerticate.encode(buff);
        byte [] encoded = buff.getMsgCopy();

        String encodedStr = StringUtil.toHexString(encoded);




       /* String path = "C:\\Users\\ahmet.asa\\Desktop\\Keys\\anahtar256_PublicKey.bin";
        byte[] publicKeys = AsnIO.dosyadanOKU(path);

        String temp = Base64.encode(publicKeys);
        byte[] temp1 = temp.getBytes();

        byte[] keys = AsnIO.dosyadanOKU(path);

        ECpiSelfDesc cp = new ECpiSelfDesc(1);
        ECar eCar = new ECar("DE" , "CVCAEPASS" , "00104");





        ECPublicKey e = (ECPublicKey) ECPublicKeyImpl.parse(new DerValue(keys));
        int[] oid = new int[]{1, 2, 840, 10045, 4, 3, 2};
        EElcPuK elcpuk = new EElcPuK(oid, e, true);*/


    }



    /**
     * @throws ESYAException
     * @throws IOException
     */
    @Test
    public void elcpukUret() throws ESYAException, IOException {
        ElcPuK elcPuK = new ElcPuK();
        byte[] p = StringUtil.hexToByte("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        byte[] a = StringUtil.hexToByte("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC");
        byte[] b = StringUtil.hexToByte("51953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F00");
        byte[] g = StringUtil.hexToByte("850400C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650");
        byte[] r = StringUtil.hexToByte("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E91386409");
        byte[] y = StringUtil.hexToByte("85040047913C6ADDA14D38FE755ACD1DC79EDDBB1BD7469298C01292BB006307B4CD9ED941AA3DB15EF1BA6B91C66CC63F5E30F8CBB25EE06818F0EDFA6A2C017F33844D00E3C8EFA80133D7360C615B1D97BE2B1D46DB8F8ED5A4F9E7D7E008B0300137E4815ECF3BFA0D752EDF32D71FCC108DC7048EE0EBEA7457B03C5DFD5A7E1658AA4C");





        elcPuK.oid = new Asn1ObjectIdentifier(new int[]{1, 2, 840, 10045, 4, 3, 2});
        elcPuK.p = new Asn1OctetString(p);
        elcPuK.a = new Asn1OctetString(a);
        elcPuK.b = new Asn1OctetString(b);
        elcPuK.g = new Asn1OctetString(g);
        elcPuK.r = new Asn1OctetString(r);
        elcPuK.y = new Asn1OctetString(y);
        elcPuK.f = new Asn1Integer(1);


        Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
        elcPuK.encode(buff);

        byte[] encodedData = buff.getMsgCopy();

    }

    /**
     * @throws ESYAException
     * @throws IOException
     */
    @Test
    public void elcpukCOZ() throws ESYAException, IOException {
        ElcPuK elcPuK = new ElcPuK();

        byte[] germany_ec = StringUtil.hexToByte("7F4982022E06082A8648CE3D040302814201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF824201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC834151953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F00848186850400C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650854201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E9138640986818685040047913C6ADDA14D38FE755ACD1DC79EDDBB1BD7469298C01292BB006307B4CD9ED941AA3DB15EF1BA6B91C66CC63F5E30F8CBB25EE06818F0EDFA6A2C017F33844D00E3C8EFA80133D7360C615B1D97BE2B1D46DB8F8ED5A4F9E7D7E008B0300137E4815ECF3BFA0D752EDF32D71FCC108DC7048EE0EBEA7457B03C5DFD5A7E1658AA4C870101");

        Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(germany_ec);
        elcPuK.decode(buff);

        int[] oid = elcPuK.oid.value;

        byte[] passportCvca = StringUtil.hexToByte("7F2182030F7F4E82027D5F290103421041424550415353504F525430303038327F4982022C06082A8648CE3D040302814201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF824201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC834151953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F008481850400C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650854201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E913864098681850400D61064EE01C5727E3F6AB8BE7D9A64C4405DA477CA7BFFF80474204498EA0B4CB06BB093B71232F0323EA57D106487CEE99F97D9D84D7787479D6EA14C81D944B301B1EC4C8528E553F750CA1FE76D692D78BC793FE1557AFDD8965CF9F85B587F7BF55181012B27A6B6635E1F85A8EA9BD969472D862CD8877747095E1936381EBD408701015F201041424550415353504F525430303038327F4C0E060904007F0007030102015301C15F25060107010000035F24060109010000035F378189308186024178087BB58E5A8E1A16EA4BAFF547D5EC7B274E7E1391F9F3C851BAF2F0E88921B7D59E3473DCBC390AA6A6BB3DAC39757E226880A2C38F4A49900F5D2E45D2A6D60241256B215E8A6975233BC52585DE415B44CCBA5941E815C24EDB67385D72841AF0A8752C38710D5709D76B0FC04981BEED49EF00583289EA610BAC8849C83227DA28");

        PassportCVCerticate passportCVCertificate = new PassportCVCerticate();

        Asn1DerDecodeBuffer buffa = new Asn1DerDecodeBuffer(passportCvca);
        passportCVCertificate.decode(buffa);


    }


    /**
     * @throws ESYAException
     * @throws IOException
     */
    /*@Test
    public void cvcUret() throws ESYAException, IOException {

        Integer tempCpi = new Integer(00);
        byte cpiByte = tempCpi.byteValue();
        ECpiSelfDesc cpi = new ECpiSelfDesc(cpiByte);
        System.out.println();
        System.out.println("Cpi field: " + StringUtil.toString(cpi.getEncoded()));


        String countryCode = "TR";
        String holderMnemonic = "CVCA";
        String seqNo = "12345";
        ECar car = new ECar(countryCode, holderMnemonic, seqNo);
        System.out.println("car field: " + StringUtil.toString(car.getEncoded()));


        String chrTemp = StringUtil.toString(car.getByteValues());
        EChr chr = new EChr(chrTemp);
        System.out.println("chr field: " + StringUtil.toString(chr.getEncoded()));

//        BigInteger algId = new BigInteger("58655239323232");
//        String discDo = "C1";
//        EChat chat = new EChat(algId, discDo);
//        System.out.println("chat field: " + StringUtil.toString(chat.getEncoded()));

//        int year = 2019;
//        int month = 12;
//        int day = 23;
//        ECedSelfDesc ced = new ECedSelfDesc(year, month, day);
//        System.out.println("ced field: " + StringUtil.toString(ced.getEncoded()));
//
//        int yearcxd = 2017;
//        int monthcxd = 12;
//        int daycxd = 23;
//        ECxdSelfDesc cxd = new ECxdSelfDesc(yearcxd, monthcxd, daycxd);
//        System.out.println("cxd field: " + StringUtil.toString(cxd.getEncoded()));

        ElcPuK elcPuK = new ElcPuK();

        String path = "C:\\Users\\ahmet.asa\\Desktop\\Keys\\anahtar256_PublicKey.bin";
//        String path2 = "C:\\Users\\ahmet.asa\\Desktop\\Keys\\rsa_PublicKey.bin";
        byte[] keys = AsnIO.dosyadanOKU(path);

        ECPublicKey e = (ECPublicKey) ECPublicKeyImpl.parse(new DerValue(keys));
        ECParameterSpec spec = e.getParams();
        ECField ecField = spec.getCurve().getField();
        ECFieldFp fp = (ECFieldFp) ecField;
        X509Key a = (X509Key) e;

//        elcPuK.a = new Asn1OctetString(trimByteArray(spec.getCurve().getA().toByteArray()));
//        elcPuK.b = new Asn1OctetString(trimByteArray(spec.getCurve().getB().toByteArray()));
//        elcPuK.g = new Asn1OctetString(encodePoint(spec.getGenerator(), spec.getCurve()));
//        elcPuK.r = new Asn1OctetString(trimByteArray(spec.getOrder().toByteArray()));
//        elcPuK.y = new Asn1OctetString(encodePoint(e.getW(), spec.getCurve()));
//        elcPuK.f = new Asn1Integer((spec.getCofactor()));
//        elcPuK.oid = new Asn1OctetString(String.valueOf(a.getAlgorithmId().getOID()));
//        elcPuK.algRef = new Asn1OctetString(String.valueOf(a.getAlgorithmId().getEncodedParams()));

//        System.out.println(elcPuK.f);

        byte[] key_a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2};
//        elcPuK.a = new Asn1BitString(key_a.length * 8, key_a);

        Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
        elcPuK.encode(buff);

        byte[] encodedData = buff.getMsgCopy();


        Integer number = new Integer(SelfCvcTagEnum.COFACTOR_F.getValue());
        System.out.println(number + " deger");
        System.out.println(number.byteValue());
        System.out.println(StringConverter.toHexString(number.byteValue()));

        Integer testNum = new Integer(SelfCvcTagEnum.COFACTOR_F.getValue());
        byte[] resultaa = ByteUtil.byteToByteArray(testNum.byteValue(), testNum.byteValue(), testNum.byteValue());

        System.out.println(resultaa + " Test");
        System.out.println(StringUtil.toHexString(resultaa) + " Test reverse");


        String result = StringUtil.toHexString(encodePoint(e.getW(), spec.getCurve()));

        System.out.println(result);
        System.out.println(e.getW());
        System.out.println(spec.getCurve().getA().toString());

        byte[] test = new IntegerField(StringUtil.COFACTOR_F, spec.getCofactor()).getEncoded();
        System.out.println(test);


//        System.out.println(a.getAlgorithmId().getOID());

        //public point q generation
//        System.out.println(e.getW());
//        System.out.println(spec.getCurve());
//        System.out.println(new Asn1OctetString(encodePoint(e.getW(), spec.getCurve())));

//        System.out.println(cpi.getTagLen());

//        cpi.getByteValues();

        //proviver  "BC" wil going to  "TR"


//        System.out.println(e.getParams().getCofactor());
//        System.out.println(elcPuK.f);
//        System.out.println(e.getParams().getCofactor());
//        System.out.println(elcPuK.f.toString());
//        System.out.println(trimByteArray(spec.getCurve().getB().toByteArray()).toString());
//        System.out.println(elcPuK.b );


//            System.out.println(e.getAlgorithm());
//            System.out.println(e.getParams().getOrder());
//            System.out.println(e);
//            System.out.println();
//            System.out.println(e.getParams().getCurve().getA());
//            System.out.println();
//            System.out.println(e.getParams().getCurve().getB());
//            System.out.println();
//            System.out.println(e.getW().getAffineX());
//            System.out.println(e.getW().getAffineY());
//            System.out.println(e.getParams().getCurve());


//        byte[] algId = {0,4,0,127,0,7,3,1,2,1};
//        byte[] algId = {0x00 ,0x04 ,0x00 ,0x7F, 0x00, 0x07, 0x03, 0x01 ,0x02 ,0x01};
//        int number = (algId[9] & 0xFF) | (algId[8] & 0xFF) << 8|(algId[7] & 0xFF)<< 16 |(algId[6] & 0xFF)<< 24 |(algId[5] & 0xFF)<< 32 |
//                (algId[4] & 0xFF) << 40|(algId[3] & 0xFF) << 48|(algId[2] & 0xFF)<< 56 |(algId[1] & 0xFF)<< 64 |(algId[0] & 0xFF)<< 72 ;
//
//        byte[] test = {0x06 , 0x03, 0x5F};
//        int numbertest = test[2] & 0xFF | (test[1] & 0xFF)<<8| (test[0] & 0xFF)<<16;
//        System.out.println(numbertest);
//        System.out.println(number);
//        BigInteger tt = new BigInteger("1539");
//        byte[] testrev = tt.toByteArray();
//        System.out.println(testrev.length);
//
//        BigInteger newnumber = new BigInteger("58655239");
//        byte[] testArray = newnumber.toByteArray();
//        System.out.println(testArray.length);

//        BigInteger asdasd = new BigInteger(String.valueOf(spec.getCofactor()));
//
//        byte testCofInt = asd.byteValue();
//        byte[] testCofBig = asdasd.toByteArray();
//        System.out.println(testCofInt);
//        System.out.println(testCofBig);
//        System.out.println(testCofBig.length + " uzunluk");
//        for (int i = 0 ; i < testCofBig.length ; i++ ){
//            System.out.println(testCofBig[i]);
//        }


    }*/

    @Test
    public void testCofactor() {
        int cofactor = 136;
        byte t = ((byte) cofactor);
        System.out.println(t);

        Integer cof = new Integer("11111111");
        byte tt = cof.byteValue();
        System.out.println(tt);


    }



    @Test
    public  void testSubCvcaFields() throws ESYAException, IOException {

        byte[] car =  StringUtil.hexToByte("435343413030303737");
        byte[] chat = StringUtil.hexToByte("060904007F0007030102015301C1");

        ECar _car = new ECar(car);
        System.out.println(_car.getCountryCode());
        System.out.println(_car.getHolderMnemonic());
        System.out.println(_car.getSequence());

        System.out.println(StringUtil.toString(chat));





    }

    @Test
    public void testTagValues() throws ESYAException {
       /* Integer tempCpi = new Integer(02);
        byte cpiByte = tempCpi.byteValue();

        ECpi cpiNon = new ECpi(cpiByte);
        ECpiSelfDesc cpiSelfDesc = new ECpiSelfDesc(cpiByte);
//        System.out.println(cpiSelfDesc.getTagLen());
//        System.out.println();
//        System.out.println(StringConverter.toHexString(cpiSelfDesc.getTagLen()));

        String countryCode = "TR";
        String holderMnemonic = "CVCA";
        String seqNo = "12345";
        ECar car = new ECar(countryCode, holderMnemonic, seqNo);


//        System.out.println(SelfCvcTagEnum.CA_REFERENCE.getValue());
//        System.out.println(car.getTagLen());
//        System.out.println(StringConverter.toHexString(car.getTagLen()));
//
//        System.out.println("car field: " + StringUtil.toString(car.getEncoded()));

        ECParameters param = new ECParameters();
        System.out.println(param.getLength());*/


    }


}
