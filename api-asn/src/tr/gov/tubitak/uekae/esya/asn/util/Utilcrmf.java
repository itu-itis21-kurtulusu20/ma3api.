package tr.gov.tubitak.uekae.esya.asn.util;
/*
import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.asn.crmf.*;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;

/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 *
 * @author Muhammed Serdar SORAN
 * @version 1.0
 *

public class Utilcrmf
{
    private static final Logger LOGCU = LoggerFactory.getLogger(Utilcrmf.class);

    public Utilcrmf()
    {
    }


    public static CertReqMessages istekUret(Name subject, SubjectPublicKeyInfo publicKey)
    {

        CertTemplate template = new CertTemplate(null, //version
                                                 null, //serialnumber
                                                 null, //signingAlg
                                                 null, //issuer
                                                 null, //validity
                                                 subject, //subject
                                                 publicKey, //publickey
                                                 null, //issuerUID
                                                 null, //subjectUID
                                                 null //extensions
        );

        CertRequest istek = new CertRequest(1, template);

        CertReqMsg[] istekler = new CertReqMsg[1];
        istekler[0] = new CertReqMsg(istek);
        //pop eksik kaldi... burada pop hesaplanip arkasina eklenmeli.
        //pop basit bir imzalama islemi olmasina ragmen sm ozel anahtari ile,
        //kartta ve hafizada olmak uzere farkli sekillerde imzalanabilir.

        CertReqMessages certreqmessages = new CertReqMessages(istekler);

        return certreqmessages;
    }


    public static CertReqMessages istekUret(String subject, SubjectPublicKeyInfo publicKey) throws
            Asn1Exception
    {
        return istekUret(UtilName.string2Name(subject, true), publicKey);
    }

    public static CertReqMessages istekUret(String subject, boolean aTurkceSubject, SubjectPublicKeyInfo publicKey) throws
            Asn1Exception
    {
        return istekUret(UtilName.string2Name(subject, aTurkceSubject), publicKey);
    }


    public static CertReqMessages istekUret(String subject, byte[] publicKeybytes,
                                            AlgorithmIdentifier algorithm) throws Asn1Exception
    {
        SubjectPublicKeyInfo publicKey = new SubjectPublicKeyInfo(algorithm,
                                                                  new Asn1BitString(publicKeybytes.length << 3, publicKeybytes));
        return istekUret(UtilName.string2Name(subject, true), publicKey);
    }

    public static CertReqMessages istekUret(String subject, boolean aTurkceSubject, byte[] publicKeybytes,
                                            AlgorithmIdentifier algorithm) throws Asn1Exception
    {
        SubjectPublicKeyInfo publicKey = new SubjectPublicKeyInfo(algorithm,
                                                                  new Asn1BitString(publicKeybytes.length << 3, publicKeybytes));
        return istekUret(UtilName.string2Name(subject, aTurkceSubject), publicKey);
    }


    public static CertReqMessages istekUret(String subject, byte[] publicKeybytes,
                                            int[] algorithmint) throws Asn1Exception
    {
        AlgorithmIdentifier algorithm = new AlgorithmIdentifier(algorithmint);
        SubjectPublicKeyInfo publicKey = new SubjectPublicKeyInfo(algorithm,
                                                                  new Asn1BitString(publicKeybytes.length << 3, publicKeybytes));
        return istekUret(UtilName.string2Name(subject, true), publicKey);
    }

    public static CertReqMessages istekUret(String subject, boolean aTurkceSubject, byte[] publicKeybytes,
                                            int[] algorithmint) throws Asn1Exception
    {
        AlgorithmIdentifier algorithm = new AlgorithmIdentifier(algorithmint);
        SubjectPublicKeyInfo publicKey = new SubjectPublicKeyInfo(algorithm,
                                                                  new Asn1BitString(publicKeybytes.length << 3, publicKeybytes));
        return istekUret(UtilName.string2Name(subject, aTurkceSubject), publicKey);
    }


    public static void istegeRefNoEkle(CertRequest istek, String refno) throws Asn1Exception
    {
        istegeIA5StringEkle(istek, refno, OIDESYA.oid_refno);
    }


    public static String istektenRefNoAl(CertRequest istek) throws Asn1Exception
    {
        return istektenIA5StringAl(istek, OIDESYA.oid_refno);
    }

    public static void istegeKartSeriNoEkle(CertRequest istek, String refno) throws Asn1Exception
    {
        istegeIA5StringEkle(istek, refno, OIDESYA.oid_kartSeriNo);
    }


    public static String istektenKartSeriNoAl(CertRequest istek) throws Asn1Exception
    {
        return istektenIA5StringAl(istek, OIDESYA.oid_kartSeriNo);
    }

    public static void istegeKartUreticiNoEkle(CertRequest istek, int aKartUreticiNo) throws Asn1Exception
    {
        istegeASN1TypeEkle(istek, new Asn1Integer(aKartUreticiNo), OIDESYA.oid_kartUreticiNo);
    }

    public static long istektenKartUreticiNoAl(CertRequest istek) throws Asn1Exception
    {
        Asn1Integer ureticiNo = new Asn1Integer();
        istektenASN1TypeAl(istek, OIDESYA.oid_kartUreticiNo, ureticiNo);
        return ureticiNo.value;
    }

    public static void istegeSertTalepNoEkle(CertRequest aIstek, long aSertTalepNo) throws Asn1Exception
    {
        istegeASN1TypeEkle(aIstek, new Asn1Integer(aSertTalepNo), OIDESYA.oid_sertTalepNo);
    }

    public static long istektenSertTalepNoAl(CertRequest aIstek) throws Asn1Exception
    {
        Asn1Integer sertTalepNo = new Asn1Integer();
        istektenASN1TypeAl(aIstek, OIDESYA.oid_sertTalepNo, sertTalepNo);
        return sertTalepNo.value;
    }

    public static void istegeProtocolEncKeyEkle(CertRequest aIstek, SubjectPublicKeyInfo aPubKey) throws Asn1Exception
    {
        istegeASN1TypeEkle(aIstek, aPubKey, new Asn1ObjectIdentifier(_crmfValues.id_regCtrl_protocolEncrKey));
    }

    public static SubjectPublicKeyInfo istektenProtocolEncKeyAl(CertRequest aIstek) throws Asn1Exception
    {
        SubjectPublicKeyInfo pubKey = new SubjectPublicKeyInfo();
        istektenASN1TypeAl(aIstek, new Asn1ObjectIdentifier(_crmfValues.id_regCtrl_protocolEncrKey),
                           pubKey);
        return pubKey;
    }

    public static void istegeIA5StringEkle(CertRequest istek, String refno, Asn1ObjectIdentifier aOID) throws Asn1Exception
    {
        istegeASN1TypeEkle(istek, new Asn1IA5String(refno), aOID);
    }

//     public static void istegeIA5StringEkle (CertRequest istek, String refno,Asn1ObjectIdentifier aOID) throws Asn1Exception
//     {
//          if (istek == null)
//               throw new Asn1Exception("Istek bos olarak geldi");
//          AttributeTypeAndValue[] cons = null;
//          int yer = 0;
//          if (istek.controls != null)
//          {
//               cons =
//                   new AttributeTypeAndValue[istek.controls.elements.length + 1];
//               System.arraycopy(istek.controls.elements, 0, cons, 0, istek.controls.elements.length);
//               yer = istek.controls.elements.length;
//          }
//          else
//          {
//               cons = new AttributeTypeAndValue[1];
//               istek.controls = new Controls(1);
//               yer = 0;
//          }
//
//          Asn1IA5String refasn = new Asn1IA5String(refno);
//          Asn1DerEncodeBuffer enbuf = new Asn1DerEncodeBuffer();
//          refasn.encode(enbuf);
//          Asn1OpenType aot = new Asn1OpenType(enbuf.getMsgCopy());
//          cons[yer] =
//              new AttributeTypeAndValue(aOID, aot);
//
//          istek.controls.elements = cons;
//     }

    public static void istegeASN1TypeEkle(CertRequest istek, Asn1Type aEklenecek, Asn1ObjectIdentifier aOID) throws Asn1Exception
    {
        if (istek == null)
            throw new Asn1Exception("Istek bos olarak geldi");

        AttributeTypeAndValue[] cons = null;
        int yer = 0;
        if (istek.controls != null) {
            cons =
                    new AttributeTypeAndValue[istek.controls.elements.length + 1];
            System.arraycopy(istek.controls.elements, 0, cons, 0, istek.controls.elements.length);
            yer = istek.controls.elements.length;
        } else {
            cons = new AttributeTypeAndValue[1];
            istek.controls = new Controls(1);
            yer = 0;
        }

        Asn1DerEncodeBuffer enbuf = new Asn1DerEncodeBuffer();
        aEklenecek.encode(enbuf);
        Asn1OpenType aot = new Asn1OpenType(enbuf.getMsgCopy());
        cons[yer] =
                new AttributeTypeAndValue(aOID, aot);

        istek.controls.elements = cons;
    }

    public static String istektenIA5StringAl(CertRequest istek, Asn1ObjectIdentifier aOID) throws Asn1Exception
    {
        Asn1IA5String str = new Asn1IA5String();
        istektenASN1TypeAl(istek, aOID, str);
        return str.value;
    }

//     public static String istektenIA5StringAl (CertRequest istek,Asn1ObjectIdentifier aOID) throws Asn1Exception
//     {
//          if (istek == null || istek.controls == null)
//               throw new Asn1Exception("Istekte "+aOID+" oidli string bulunamadi");
//          AttributeTypeAndValue[] elems = istek.controls.elements;
//
//          Asn1DerDecodeBuffer decBuf;
//          Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
//
//          for (int i = 0; i < elems.length; i++)
//          {
//               if (elems[i].type.equals(aOID))
//               {
//                    Asn1OpenType refopen = elems[i].value;
//                    refopen.encode(encBuf);
//                    decBuf = new Asn1DerDecodeBuffer(encBuf.getByteArrayInputStream());
//                    Asn1IA5String refasn = new Asn1IA5String();
//                    try
//                    {
//                         refasn.decode(decBuf);
//                    } catch (IOException ex)
//                    {
//                         throw new Asn1Exception("Istekten "+aOID+" oidli string alinamadi");
//                    }
//                    return refasn.value;
//               }
//          }
//          throw new Asn1Exception("Istekte "+aOID+" oidli string bulunamadi");
//     }

    public static void istektenASN1TypeAl(CertRequest istek, Asn1ObjectIdentifier aOID, Asn1Type aTip) throws Asn1Exception
    {
        if (istek == null || istek.controls == null)
            throw new Asn1Exception("Istekte " + aOID + " oidli " + aTip.getClass().getName() + " bulunamadi");
        AttributeTypeAndValue[] elems = istek.controls.elements;

        Asn1DerDecodeBuffer decBuf;
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

        for (int i = 0; i < elems.length; i++) {
            if (elems[i].type.equals(aOID)) {
                Asn1OpenType refopen = elems[i].value;
                refopen.encode(encBuf);
                decBuf = new Asn1DerDecodeBuffer(encBuf.getByteArrayInputStream());
                try {
                    aTip.decode(decBuf);
                } catch (IOException ex) {
                    throw new Asn1Exception("Istekte " + aOID + " oidli " + aTip.getClass().getName() + " alinamadi");
                }
                return;
            }
        }
        throw new Asn1Exception("Istekte " + aOID + " oidli " + aTip.getClass().getName() + " bulunamadi");
    }


    public static byte[] encryptedValuedakiSifreliyiAl(BaseCipher aSifreCozucu, EncryptedValue aEncVal)
            throws CryptoException, Asn1Exception, IOException
    {

        LOGCU.debug("EncryptedValue icinden sifrelenmis bilgi alinacak...");
        if ((aEncVal.encValue == null) ||
                (aEncVal.encSymmKey == null) ||
                (aEncVal.intendedAlg == null))
            throw new Asn1Exception("EncryptedValue icinde beklenen deger bulunamadi.");

        byte[] encPrivKey = aEncVal.encValue.value;
        byte[] encSimAnah = aEncVal.encSymmKey.value;
        AlgorithmIdentifier asimAlg = aEncVal.keyAlg;
        AlgorithmIdentifier simAlg = aEncVal.symmAlg;

        int alg = -1;
        int mod = -1;
        int blockSize = -1;
        byte[] iv = null;

        LOGCU.debug("Simetrik algoritmayi olusturuyorum...");
        /*if(simAlg.algorithm.equals(Ozellikler.ASN_OID_AES256_CBC))
         {
              alg = Ozellikler.SIM_ALGO_AES;
              mod = Ozellikler.SIM_MOD_CBC;
              blockSize = 16;
              if(simAlg.parameters == null)
                   throw new Asn1Exception("Simetrik algoritma parametresi bulunamadi.");

              Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(simAlg.parameters.value);
              Asn1OctetString ivOctet = new Asn1OctetString();
              ivOctet.decode(decBuf);
              iv = ivOctet.value;
         }
         else
              throw new CryptoException("Simetrik algoritma taninamadi.");
         SimetrikAlgoritma sa = new SimetrikAlgoritma(
             alg,
             blockSize,
             mod,
             Ozellikler.PAD_PKCS7,
             iv
             );

        *
        Pair<CipherAlg, AlgorithmParams> sa = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(simAlg));

        LOGCU.debug("Encrypted simetrik alg anahtarinin sifresi cozulecek...");
//          AY_HafizadaTumKripto htk = HafizadaTumKripto.getInstance();
//          byte[] simAnah = htk.sifreCoz(encSimAnah,aProtPrivKey,Ozellikler.ASIM_ALGO_RSA);
        byte[] simAnah = aSifreCozucu.doFinal(encSimAnah);

        LOGCU.debug("Simetrik kripto olusturulacak.");
        BufferedCipher simKrip = Crypto.getDecryptor(sa.first());
        simKrip.init(simAnah, sa.second());
        //simKrip = new SimetrikKripto(sa);
        //simKrip.anahtarYerlestir(simAnah);

        LOGCU.debug("Encrypted bilgi sifresi cozulecek.");
        byte[] privKeyBytes = simKrip.doFinal(encPrivKey);

        LOGCU.debug("Bilgi basariyla hesaplandi.");
        return privKeyBytes;
    }


    public static PrivateKey encryptedValuedakiSifreliPrivKeyiAl(PrivateKey aProtPrivKey, EncryptedValue aEncVal)
            throws CryptoException, Asn1Exception, IOException
    {
        final PrivateKey pk = aProtPrivKey;
        BaseCipher sifreCozucu = new BaseCipher()
        {
            public byte[] doFinal(byte[] aSifreli)
                    throws CryptoException
            {
                return CipherUtil.decrypt(CipherAlg.RSA_PKCS1, null, aSifreli, pk);

            }

            public CipherAlg getCipherAlgorithm()
            {
                return CipherAlg.RSA_PKCS1;
            }
        };
        byte[] privKeyBytes = encryptedValuedakiSifreliyiAl(sifreCozucu, aEncVal);

        LOGCU.debug("Veriden PrivKey olusturulacak.");
        PrivateKey privKey = KeyUtil.decodePrivateKey(SignatureAlg.fromOID(aEncVal.intendedAlg.algorithm.value).getAsymmetricAlg(), privKeyBytes);

        LOGCU.debug("Private key basariyla hesaplandi.");

        return privKey;

    }

    public static EncryptedValue encryptedValueOlustur(byte[] aSifrelenecek,
                                                       SubjectPublicKeyInfo aProtokolKey,
                                                       AlgorithmIdentifier aIntendedAlg)
            throws CryptoException, Asn1Exception
    {
        byte[] iv = RandomUtil.generateRandom(16);

        LOGCU.debug("IV olusturuldu.");


        /*SimetrikAlgoritma sa = new SimetrikAlgoritma(
                Ozellikler.SIM_ALGO_AES,
                16,
                Ozellikler.SIM_MOD_CBC,
                Ozellikler.PAD_PKCS7,
                iv
        );
        *
        byte[] simAnahtar = RandomUtil.generateRandom(32);
        LOGCU.debug("Simetrik anahtar olusturuldu.");
        /*byte[] simAnahtar = SimetrikKripto.anahtarUret(sa, 32);
        LOGCU.debug("Simetrik anahtar olusturuldu.");
        SimetrikKripto simKrip = new SimetrikKripto(sa);
        simKrip.anahtarYerlestir(simAnahtar);
        LOGCU.debug("Anahtar yerlestirildi.");
        byte[] encPrivKeyValue = simKrip.sifrele(aSifrelenecek);
        *
        byte[] encPrivKeyValue = CipherUtil.encrypt(CipherAlg.AES256_CBC, new ParamsWithIV(iv),
                                                    aSifrelenecek, simAnahtar);


        LOGCU.debug("privkey encrypt edildi");

        /*byte[] encSimAnah = kr.sifrele(simAnahtar,
                                       kr.pubDecode(aProtokolKey),
                                       Ozellikler.ASIM_ALGO_RSA
        );*

        byte[] encSimAnah = CipherUtil.encrypt(CipherAlg.RSA_PKCS1, null,
                                               simAnahtar,
                                               KeyUtil.decodePublicKey(new ESubjectPublicKeyInfo(aProtokolKey))
        );


        LOGCU.debug("simetrik anahtar encrypt edildi");

        EncryptedValue encVal = new EncryptedValue(
                aIntendedAlg,
                CipherAlg.AES256_CBC.toAlgorithmIdentifier(iv).getObject(),
                new Asn1BitString(encSimAnah.length << 3, encSimAnah),
                new AlgorithmIdentifier(AsymmetricAlg.RSA.getOID()),
                (byte[]) null,
                new Asn1BitString(encPrivKeyValue.length << 3, encPrivKeyValue)
        );

        LOGCU.debug("EncryptedValue basariyla olusturuldu.");

        return encVal;
    }


    public static EncryptedValue sifreliPrivKeyOlustur(PrivateKey aPrivKey, SubjectPublicKeyInfo aProtokolKey, AlgorithmIdentifier aIntendedAlg)
            throws CryptoException, Asn1Exception
    {
        return encryptedValueOlustur(aPrivKey.getEncoded(), aProtokolKey, aIntendedAlg);
        /*
    byte[] iv = new byte[16];

    AY_HafizadaTumKripto kr = HafizadaTumKripto.getInstance();
    kr.rastgeleSayi(iv);
    LOGCU.debug("IV olusturuldu.");
    SimetrikAlgoritma sa = new SimetrikAlgoritma(
        Ozellikler.SIM_ALGO_AES,
        16,
        Ozellikler.SIM_MOD_CBC,
        Ozellikler.PAD_PKCS7,
        iv
        );
    byte[] simAnahtar = SimetrikKripto.anahtarUret(sa, 32);
    LOGCU.debug("Simetrik anahtar olusturuldu.");
    SimetrikKripto simKrip = new SimetrikKripto(sa);
    simKrip.anahtarYerlestir(simAnahtar);
    LOGCU.debug("Anahtar yerlestirildi.");
    byte[] encPrivKeyValue = simKrip.sifrele(aPrivKey.getEncoded());
    LOGCU.debug("privkey encrypt edildi");

    byte[] encSimAnah = kr.sifrele(simAnahtar,
                                   kr.pubDecode(aProtokolKey),
                                   Ozellikler.ASIM_ALGO_RSA
                                   );
    LOGCU.debug("simetrik anahtar encrypt edildi");

    EncryptedValue encVal = new EncryptedValue(
        aIntendedAlg,
        simKrip.getAlgoritma().getAlgorithmID(32),
        new Asn1BitString(encSimAnah.length << 3, encSimAnah),
        new AlgorithmIdentifier(Ozellikler.ASN_OID_RSA),
        (byte[])null,
        new Asn1BitString(encPrivKeyValue.length << 3, encPrivKeyValue)
        );

    LOGCU.debug("EncryptedValue basariyla olusturuldu.");

    return encVal;
        *
    }


    public static void main(String[] args)
    {

//          CertReqMessages crm = null;
//          try
//          {
//               crm = istekUret("cn=serdar,o=tubitak", new byte[]
//                               {1, 2, 8}
//                               ,
//                               tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues.
//                               md5WithRSAEncryption);
//          } catch (Asn1Exception ex)
//          {
//               ex.printStackTrace();
//          }
//          crm.print(System.out, "istekler", 0);

        try {

            KeyPair kpAsil = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
            AlgorithmIdentifier intendedAlg = new AlgorithmIdentifier(SignatureAlg.RSA_SHA1.getOID());

            KeyPair kpProt = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
            SubjectPublicKeyInfo protPubKey = new SubjectPublicKeyInfo();
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(kpProt.getPublic().getEncoded());
            protPubKey.decode(decBuf);

            EncryptedValue encVal = Utilcrmf.sifreliPrivKeyOlustur(kpAsil.getPrivate(),
                                                                   protPubKey,
                                                                   intendedAlg
            );

            System.out.println("------------");

            PrivateKey alinanPrivKey = Utilcrmf.encryptedValuedakiSifreliPrivKeyiAl(kpProt.getPrivate(), encVal);


            System.out.println("------------  1");

            byte[] imzalanacak = "12345".getBytes();
            byte[] imzali = SignUtil.sign(SignatureAlg.RSA_SHA1, imzalanacak, alinanPrivKey);

            System.out.println("Onay sonucu:" +
                    SignUtil.verify(SignatureAlg.RSA_SHA1, imzalanacak, imzali, kpAsil.getPublic()));


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


} */