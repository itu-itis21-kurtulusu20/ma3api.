package dev.esya.api.cmssignature.passport;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import org.junit.Test;
import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;

/**
 * Created by ahmet.asa on 20.09.2017.
 */
public class PasaportCvcUret {

    /**
     * @throws ESYAException
     * @throws IOException
     */
    @Test
    public void CvcUret() throws ESYAException, IOException {
//        Cpi cpi;
//        cpi = new Cpi(0);
//        ECpiSelfDesc cp = new ECpiSelfDesc(1);
//        byte[] a = cp.getEncoded();
//        Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
//        cpi.encode(buff);
//        byte[] encodedData = buff.getMsgCopy();
//
//        /*Car*/
//
//        String carValue = "DECVCAEPASS00104";
//        Car car = new Car(carValue);
//        Asn1DerEncodeBuffer buffCar = new Asn1DerEncodeBuffer();
//        car.encode(buffCar);
//        byte[] encodedCar = buffCar.getMsgCopy();
//
//        ECar eCar = new ECar("DE", "CVCAEPASS", "00104");
//        System.out.println(StringConverter.byteToHex(eCar.getEncoded()));
//
//
//        /*ElcPuk*/
//
//        String path = "C:\\Users\\ahmet.asa\\Desktop\\Keys\\anahtar256_PublicKey.bin";
//        byte[] publicKeys = AsnIO.dosyadanOKU(path);
//
//        String temp = Base64.encode(publicKeys);
////        String anotherTemp = "MHQCAQEEIFL3sLnioGcDvHWM/BPlNw96BOx1KKco2qsq4UwhQUosoAcGBSuBBAAKoUQDQgAEXs1Fmq4QdPAbn3NycdEU+HOjc3kW9efbso2kI/vdDTWcSCMk310s53G3tRClDBPPuuJAsKghbPfaTaUpmXFCNA==";
//        byte[] temp1 = temp.getBytes();
//
//        byte[] keys = AsnIO.dosyadanOKU(path);
//        ECPublicKey e = (ECPublicKey) ECPublicKeyImpl.parse(new DerValue(keys));
//        int[] oid = new int[]{1, 2, 840, 10045, 4, 3, 2};
//        EElcPuK elcpuk = new EElcPuK(oid, e, true);
//
//        System.out.println(StringConverter.byteToHex(elcpuk.getEncoded()));
//
//            /*Decode test*/
//        ElcPuK elcPukDecTest = new ElcPuK();
//        byte[] keymanager = StringConverter.hexToByte("7F4982022906082A8648CE3D040302814201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF824201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC834151953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F008481850400C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650854201FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E9138640986818504019FC664142F257438498F6B705CE9FAC367A3B75CC0BFEB8EDE275E4FBE186A799D3846B330B3EE4B300AD0A9025A0037E8519DDC8856379C6359A4FACE85A4C660003CD629F74BAAB170A3FA51F5E05E84D4F0D596B7AC31E807F6BC4114E08086044134993E603C68A26FB2EC3C2F37F219FA362AF17D4C9687298F4828FC500DEACE");
//        Asn1DerDecodeBuffer buff1 = new Asn1DerDecodeBuffer(keymanager);
//        elcPukDecTest.decode(buff1);
//
//        int[] oidTest = elcPukDecTest.oid.value;
//
//        System.out.println(StringConverter.byteToHex(elcpuk.getEncoded()));
//
//
//
//        /*CHR*/
//        String chrValue = "DECVCAEPASS00104";
//        Chr chr = new Chr(chrValue);
//
//        Asn1DerEncodeBuffer buffCar2 = new Asn1DerEncodeBuffer();
//        chr.encode(buffCar2);
//        byte[] encodedChr = buffCar.getMsgCopy();
//
//        EChr chrWrap = new EChr(chrValue);
//        System.out.println(StringConverter.byteToHex(chrWrap.getEncoded()));
//
//
//        /*CHAT*/
//
//        Chat chat = new Chat();
//        int[] chatArr = new int[]{0, 4, 0, 127, 0, 7, 3, 1, 2, 1}; // oid referans authority will add confluence
//        chat.oid = new Asn1ObjectIdentifier(chatArr);
//        byte[] role = StringConverter.hexToByte("C1");
//
//        chat.value = new Asn1OctetString(role);
//
//        Asn1DerEncodeBuffer buffChat = new Asn1DerEncodeBuffer();
//        chat.encode(buffChat);
//        byte[] encodedChat = buffCar.getMsgCopy();
//
//        EChat chatWrap = new EChat(chatArr, "C1");
//
//        System.out.println(StringConverter.byteToHex(chatWrap.getEncoded()));
//
//        /*CED*/
//
//        ECedSelfDesc ced = new ECedSelfDesc(Calendar.getInstance());
//        System.out.println(StringConverter.byteToHex(ced.getEncoded()));
//
//        /*CXD*/
//        ECxdSelfDesc cxd = new ECxdSelfDesc(2019, 9, 19);
//        System.out.println(StringConverter.byteToHex(cxd.getEncoded()));
//
//        EPassportCVCertificateBody ePassCertificateBody = new EPassportCVCertificateBody(cp, eCar, elcpuk, chrWrap, chatWrap, ced, cxd);
//        System.out.println("Certificate body : " + StringConverter.byteToHex(ePassCertificateBody.getEncoded()));
//
//        /*Signature*/
//        /*cp.getEncoded(), eCar.getEncoded(), elcpuk.getEncoded(), chrWrap.getEncoded(), chatWrap.getEncoded(),ced.getEncoded(),cxd.getEncoded()*/
//
//
//        String pathPrivate = "C:\\Users\\ahmet.asa\\Desktop\\Keys\\anahtar256_PrivateKey.bin";
//        byte[] privKeys = AsnIO.dosyadanOKU(pathPrivate);
////        PrivateKey privKey = (PrivateKey) ECPrivateKeyImpl.parse(new DerValue(privKeys));
//        java.security.interfaces.ECPrivateKey privKey = (java.security.interfaces.ECPrivateKey) ECPrivateKeyImpl.parse(new DerValue(privKeys));
//
//
//        byte[] allData = ePassCertificateBody.getEncoded();
//        byte[] signedBody = SignUtil.sign(SignatureAlg.ECDSA_SHA256, allData, privKey);
//
//        System.out.println("Signature : " + StringConverter.byteToHex(signedBody));
//
//
//        EPassportCVCertificate ePassportCVCertificate = new EPassportCVCertificate(allData, ePassCertificateBody);
//
//        System.out.println("Certificate body : " + StringConverter.byteToHex(ePassportCVCertificate.getEncoded()));
    }

    @Test
    public void testEcType() throws IOException, InvalidKeyException {

//        String pathPrivate = "C:\\Users\\ahmet.asa\\Desktop\\Keys\\anahtar256_PrivateKey.bin";
//        byte[] privKeys = AsnIO.dosyadanOKU(pathPrivate);
//        PrivateKey privKey = (PrivateKey) ECPrivateKeyImpl.parse(new DerValue(privKeys));
//        ECKey privKey = (ECKey) PKCS8Key.parse(new DerValue(privKeys));

//       ECParameterSpec spec =  privKey.getParams();
//
//        ECDomainParameter domainParameter = privKey.getParams();
//        ECDSAPrivateKey key = new ECDSAPrivateKey(privKey.getParams(), null);


//        if (privKey instanceof ECDSAPrivateKey) {
//            System.out.println("True");
//        }
//
//        if(privInfo instanceof ECDSAPrivateKey){
//            System.out.println("True2");
//        }

//        String path = "C:\\Users\\ahmet.asa\\Desktop\\Keys\\anahtar256_PrivateKey.bin";
//        byte[] privateKeys = AsnIO.dosyadanOKU(path);
//        String encodedPrivate = Base64.encode(privateKeys);
//        byte[] temp1 = encodedPrivate.getBytes();
    }
//    public static PKCS8Key parse(DerValue var0) throws IOException {
//        PrivateKey var1 = parseKey(var0);
//        if(var1 instanceof PKCS8Key) {
//            return (PKCS8Key)var1;
//        } else {
//            throw new IOException("Provider did not return PKCS8Key");
//        }
//    }


    public void domainParams(ECParameterSpec spec) {

//        ECParameters eParams = null;
//        eParams.cofactor = new com.objsys.asn1j.runtime.Asn1BigInteger(String.valueOf(spec.getCofactor()));
//        eParams.curve = new com.objsys.asn1j.runtime. spec.getCurve();
//        ECDomainParameter domainParam = null;
//        if(spec.getCofactor() != null)
//            domainParam=  ECDomainParameter.getInstance(curve,
//                    g,
//                    ecParams.order.value,
//                    ecParams.cofactor.value);
//        else
//            domainParam=  ECDomainParameter.getInstance(curve,
//                    g,
//                    ecParams.order.value); //TO-DO Cofactor hesaplanmali
//
//        return domainParam;
//
    }
}
