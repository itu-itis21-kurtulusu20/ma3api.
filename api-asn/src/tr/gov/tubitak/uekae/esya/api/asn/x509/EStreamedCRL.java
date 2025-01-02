package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.DigestInfo;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;

/**
 * User: zeldal.ozdemir
 * Date: 2/7/11
 * Time: 8:47 AM
 */
public class EStreamedCRL {
    private static final Logger LOGGER = LoggerFactory.getLogger(EStreamedCRL.class);

    private Asn1BerOutputStream mSil = null;

    private Version mVersion;
    private Name mIssuer;
    private Time mThisUpdate;
    private Time mNextUpdate;

    private Extensions mSilExtensions;
    private RevokedListFetcher mIptalListesi;
    private BaseSigner signer;


    private AlgorithmIdentifier mAlgID ;

    private int mTbsLength = 0;
    private int mRevCerLength = 0;

    private byte[] mImza = null;
    private MessageDigest messageDigest;
    private String digestAlg;
    private EAlgorithmIdentifier digestAlgId;

    //public SilOlusturucu(){}

    public EStreamedCRL(EVersion aVersion,
                        EName aIssuer,
                        Calendar aThisUpdate,
                        Calendar aNextUpdate,
                        EExtensions aSilExtensions,
                        RevokedListFetcher aIptalListesi,
                        BaseSigner signer,
                        String digestAlg,
                        EAlgorithmIdentifier digestAlgId,
                        EAlgorithmIdentifier aSignatureAlgId) {
        this.digestAlg = digestAlg;
        this.digestAlgId = digestAlgId;
        this.mAlgID = aSignatureAlgId.getObject();
        mVersion = aVersion.getObject();
        mIssuer = aIssuer.getObject();
        mThisUpdate = UtilTime.calendarToTimeFor3280(aThisUpdate);
        mNextUpdate = UtilTime.calendarToTimeFor3280(aNextUpdate);
        mSilExtensions = aSilExtensions.getObject();
        mIptalListesi = aIptalListesi;
        this.signer = signer;
    }

    /*public void test() throws Asn1Exception
     {
         TBSCertList tbs = new TBSCertList();
         tbs.version = mVersion;
         tbs.issuer = mIssuer;
         tbs.thisUpdate = mThisUpdate;
         tbs.nextUpdate = mNextUpdate;
         tbs.signature = mAlgID;
         tbs.crlExtensions = mSilExtensions;
         tbs.revokedCertificates = mIptalListesi.tumElemleriAl();

         Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
         tbs.encode(enc);
         byte[] a = enc.getMsgCopy();
         mOzetci.update(a, 0, a.length);
     }*/

    public void streameYaz(OutputStream aOutputStream) throws ESYAException {
        mSil = new Asn1BerOutputStream(aOutputStream);

        try {
            messageDigest = MessageDigest.getInstance(digestAlg);
        } catch (Exception aEx) {
            LOGGER.error("İmzalama algoritması alınırken hata oluştu", aEx);
            throw new ESYAException("İmzalama algoritması alınırken hata oluştu", aEx);
        }
        LOGGER.debug("İmzalama algoritması alındı");

        int len;
        try {
            len = _tbsCertListHazirla();
        } catch (Exception aEx) {
            LOGGER.error("tbsCertList hazırlanırken hata oluştu", aEx);
            throw new ESYAException("tbsCertList hazırlanırken hata oluştu", aEx);
        }
        LOGGER.debug("tbsCertList hazırlandı");

        try {
            mSil.encodeTagAndLength(Asn1Tag.SEQUENCE, len);
        } catch (IOException aEx) {
            LOGGER.error("Tag-length encode edilirken hata oluştu", aEx);
            throw new ESYAException("Tag-length encode edilirken hata oluştu", aEx);
        }
        LOGGER.debug("Tag-length encode edildi");

        try {
            _tbsCertListYaz();
        } catch (Exception aEx) {
            LOGGER.error("tbsCertList yazılırken hata oluştu", aEx);
            throw new ESYAException("tbsCertList yazılırken hata oluştu", aEx);
        }
        LOGGER.debug("tbsCertList yazıldı");

        try {
            _signatureAlgorithmYaz();
        } catch (Exception aEx) {
            LOGGER.error("signatureAlgorithm yazılırken hata oluştu", aEx);
            throw new ESYAException("signatureAlgorithm yazılırken hata oluştu", aEx);
        }
        LOGGER.debug("signatureAlgorithm yazıldı");

        try {
            _signatureDegeriYaz();
        } catch (Exception aEx) {
            LOGGER.error("signatureDegeri yazılırken hata oluştu", aEx);
            throw new ESYAException("signatureDegeri yazılırken hata oluştu", aEx);
        }
        LOGGER.debug("signatureDegeri yazıldı");

        try {
            mSil.close();
            aOutputStream.close();
        } catch (IOException aEx) {
            LOGGER.error("stream kapatılırken hata oluştu", aEx);
            throw new ESYAException("stream kapatılırken hata oluştu", aEx);
        }
        LOGGER.debug("stream kapatıldı");
    }

    private int _tbsCertListHazirla() throws IOException, Asn1Exception, ESYAException {
        //boyutunu hesapla
        int tbsLen = _tbsCertListBoyutuHesapla();

        //ozetini al
        _tbsCertListOzetHesapla();

        int signAlgLen = _signatureAlgorithmBoyutHesapla();
        //tbs'i imzala
        int signLen = _signatureDegeriHesapla();
        return tbsLen + signAlgLen + signLen;
    }

    private int _tbsCertListBoyutuHesapla() throws Asn1Exception, ESYAException {
        int ver = _boyutHesapla(mVersion);
        int algID = _boyutHesapla(mAlgID);
        int issuer = _boyutHesapla(mIssuer);
        int thisUpdate = _boyutHesapla(mThisUpdate);
        int nextUpdate = _boyutHesapla(mNextUpdate);
        int rev = _revokedCertificatesBoyutHesapla();
        int ext = _extensionsBoyutHesapla();

        mTbsLength = ver + algID + issuer + thisUpdate + nextUpdate + rev + ext;

        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        enc.encodeTagAndLength(Asn1Tag.SEQUENCE, mTbsLength);
        int taglen = enc.getMsgLength();

        return mTbsLength + taglen;
    }

    private void _tbsCertListOzetHesapla() throws Asn1Exception, ESYAException {

        _tbsTagLenOzetAl();

        _ozetAl(mVersion);
        _ozetAl(mAlgID);
        _ozetAl(mIssuer);
        _ozetAl(mThisUpdate);
        _ozetAl(mNextUpdate);
        _revokedCertificatesOzetAl();
        _extensionsOzetAl();
    }

    private void _tbsTagLenOzetAl() {
        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        enc.encodeTagAndLength(Asn1Tag.SEQUENCE, mTbsLength);
        byte[] encoded = enc.getMsgCopy();
        messageDigest.update(encoded, 0, encoded.length);
    }

    private void _tbsCertListYaz() throws IOException, Asn1Exception, ESYAException {
        //tag length yaz
        //mSil.encodeTagAndLength(Asn1Tag.SEQUENCE, mTbsLength);
        mSil.encodeTag(Asn1Tag.SEQUENCE);
        //tek basamaklı değerler için encodeLength 82 00 olarak encode ettiğinden elle yazıyorum
        if (mTbsLength <= 255) {
            mSil.write(129);//81
            mSil.write(mTbsLength);
        } else {
            mSil.encodeLength(mTbsLength);
        }

        _yaz(mVersion);
        _yaz(mAlgID);
        _yaz(mIssuer);
        _yaz(mThisUpdate);
        _yaz(mNextUpdate);
        _revokedCertificatesYaz();
        _extensionsYaz();
    }

    private int _revokedCertificatesBoyutHesapla() throws Asn1Exception, ESYAException {
        ERevokedCertificateElement elem = null;
        mIptalListesi.reset();
        while ((elem = mIptalListesi.getNext()) != null) {
            mRevCerLength += _boyutHesapla(elem.getObject());
        }
        //tag length boyutu ekle
        if (mRevCerLength != 0) {
            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            enc.encodeTagAndLength(Asn1Tag.SEQUENCE, mRevCerLength);
            return mRevCerLength + enc.getMsgLength();
        }
        return mRevCerLength;
    }

    private void _revokedCertificatesOzetAl() throws Asn1Exception, ESYAException {
        if (mRevCerLength != 0) {
            //tag length ozeti al
            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            enc.encodeTagAndLength(Asn1Tag.SEQUENCE, mRevCerLength);
            byte[] encoded = enc.getMsgCopy();
            messageDigest.update(encoded, 0, encoded.length);

            //digest
            ERevokedCertificateElement elem = null;
            mIptalListesi.reset();
            while ((elem = mIptalListesi.getNext()) != null) {
                _ozetAl(elem.getObject());
            }
        }
    }

    private int _extensionsBoyutHesapla() throws Asn1Exception {
        if (mSilExtensions != null) {
            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            //tag length olustur
            Asn1Tag tag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);
            int len = mSilExtensions.encode(enc, true);
            enc.encodeTagAndLength(tag, len);
            byte[] encoded = enc.getMsgCopy();
            len = encoded.length;
            return len;
        }
        return 0;
    }

    private void _revokedCertificatesYaz() throws IOException, Asn1Exception, ESYAException {
        if (mRevCerLength != 0) {
            //tag length yaz
            mSil.encodeTagAndLength(Asn1Tag.SEQUENCE, mRevCerLength);

            ERevokedCertificateElement elem = null;
            mIptalListesi.reset();
            while ((elem = mIptalListesi.getNext()) != null) {
                _yaz(elem.getObject());
            }
        }
    }

    private void _extensionsYaz() throws IOException, Asn1Exception {
        if (mSilExtensions != null) {
            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            Asn1Tag tag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);
            int len = mSilExtensions.encode(enc, true);
            enc.encodeTagAndLength(tag, len);
            byte[] encoded = enc.getMsgCopy();
            mSil.write(encoded);
        }
    }

    private void _extensionsOzetAl() throws Asn1Exception {
        if (mSilExtensions != null) {
            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            //tag length olustur
            Asn1Tag tag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);
            int len = mSilExtensions.encode(enc, true);
            enc.encodeTagAndLength(tag, len);
            byte[] encoded = enc.getMsgCopy();
            messageDigest.update(encoded, 0, encoded.length);
        }
    }

    /*private int _encodeAndDigest(Asn1Type aAsn) throws Asn1Exception, IOException
     {
         Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
         aAsn.encode(enc);
         byte[] encoded = enc.getMsgCopy();

         if(mYaz)
         {
             mSil.write(encoded);
             return 0;
         }
         if(mOzet)
         {
             mOzetci.update(encoded, 0, encoded.length);
             return 0;
         }
         return encoded.length;
     }*/

    private int _boyutHesapla(Asn1Type aAsn) throws Asn1Exception {
        byte[] encoded = _encode(aAsn);
        return encoded.length;
    }

    private void _ozetAl(Asn1Type aAsn) throws Asn1Exception {
        byte[] encoded = _encode(aAsn);
        messageDigest.update(encoded, 0, encoded.length);
    }

    private void _yaz(Asn1Type aAsn) throws IOException, Asn1Exception {
        byte[] encoded = _encode(aAsn);
        mSil.write(encoded);
    }

    private byte[] _encode(Asn1Type aAsn) throws Asn1Exception {
        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        aAsn.encode(enc);
        byte[] encoded = enc.getMsgCopy();
        return encoded;
    }

    private void _signatureAlgorithmYaz() throws IOException, Asn1Exception {
        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        mAlgID.encode(enc);
        mSil.write(enc.getMsgCopy());
    }

    private int _signatureAlgorithmBoyutHesapla() throws IOException, Asn1Exception {
        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        mAlgID.encode(enc);
        byte[] encoded = enc.getMsgCopy();
        return encoded.length;
    }

    private int _signatureDegeriHesapla() throws IOException, Asn1Exception, ESYAException {
        //özeti bitirelim
        byte[] ozet = messageDigest.digest();
//		AlgorithmIdentifier ozetAlg = Ozellikler.getOzetOIDFromOID(mAlgID);  // todo wtf?
        //digest info yapısı oluşturalım
        if(!Arrays.equals(mAlgID.algorithm.value, _algorithmsValues.id_RSASSA_PSS)) {
            DigestInfo di = new DigestInfo(digestAlgId.getObject(), ozet);
            ozet = _encode(di);
        }
        //özet yapısını imzalayalım
        byte[] imzali = signer.sign(ozet);
        Asn1BitString bs = new Asn1BitString(imzali.length << 3, imzali);
        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        bs.encode(enc);
        mImza = enc.getMsgCopy();
        return mImza.length;
    }

    private void _signatureDegeriYaz() throws Asn1Exception, IOException {
        mSil.write(mImza);
    }

    public interface RevokedListFetcher {
        public ERevokedCertificateElement getNext() throws ESYAException;
        public void reset() throws ESYAException;
    }

/*   public static void main(String[] args) throws Exception{
        ECertificate issuer = ECertificate.readFromFile("D:\\Projects\\TCKK\\SampleCertToImport.cer");
        PrivateKey privateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU("D:\\Projects\\TCKK\\SamplePrivKeyToImport.bin"));
        final Signer signer1 = Crypto.getSigner(SignatureAlg.RSA_NONE);
        signer1.init(privateKey);
        final Asn1BigInteger serialInt = new Asn1BigInteger(BigInteger.valueOf(123));
        EExtension eExtension = new EExtension(EExtensions.oid_ce_cRLNumber, false, serialInt);
        EExtensions aSilExtensions = new EExtensions(new EExtension[]{eExtension});

        EStreamedCRL streamedCRL = new EStreamedCRL(
                EVersion.v2, issuer.getIssuer(),
                Calendar.getInstance(), Calendar.getInstance(),
                aSilExtensions,
                new MyRevokedListFetcher(),
                signer1,
                DigestAlg.SHA1.getName(),
                new EAlgorithmIdentifier(DigestAlg.SHA1.getOID(),EAlgorithmIdentifier.ASN_NULL),
                new EAlgorithmIdentifier(SignatureAlg.RSA_SHA1.getOID())
        );

        ByteArrayOutputStream aOutputStream = new ByteArrayOutputStream();
        streamedCRL.streameYaz(aOutputStream);
        ECRL ecrl = new ECRL(aOutputStream.toByteArray());
        System.out.println("EStreamedCRL:"+SignUtil.verify(SignatureAlg.RSA_SHA1,ecrl.getTBSEncodedBytes(),ecrl.getSignature(),issuer));


        byte[] bytes = "asdasdasdsa".getBytes();

        byte[] signRSA_SHA1  = SignUtil.sign(SignatureAlg.RSA_SHA1,bytes,privateKey );
        EDigestInfo digestInfo = new EDigestInfo(
                new EAlgorithmIdentifier(
                        DigestAlg.SHA1.getOID(),
                        EAlgorithmIdentifier.ASN_NULL),
                DigestUtil.digest(DigestAlg.SHA1, bytes)
        );

        byte[] encoded = digestInfo.getEncoded();
        byte[] digested = ByteUtil.concatAll(sha1Prefix, DigestUtil.digest(DigestAlg.SHA1, bytes));
        EDigestInfo digestInfo2 = new EDigestInfo(digested);
        byte[] RSA_NONE  = SignUtil.sign(SignatureAlg.RSA_NONE,digested, privateKey );

        System.out.println("Arrays:"+ Arrays.equals(signRSA_SHA1,RSA_NONE));


        SmartCard smartCard = new SmartCard( CardType.UTIMACO);
        long session = smartCard.openSession(0);
        smartCard.login(session,"123456");
        byte[] rest = smartCard.signData(session, "test_cer", ecrl.getTBSEncodedBytes(), PKCS11Constants.CKM_RSA_PKCS);
//        SCSignerWithKeyLabel signer = new SCSignerWithKeyLabel(smartCard,session,0)

//        ecrl.setSignature(sign2);


    }*/


    /*public static void main(String[]args)
     {
         SilOlusturucu so = new SilOlusturucu();
         so.silOlustur();
         //silOlustur();
         //silOku();
         //silImzaKontrolEt();
     }*/

    /*public static void silOku()
     {
         String sil = "c:\\testsil.crl";
         try
         {
             byte[] s = AsnIO.dosyadanOKU(sil);
             System.out.println(s.length);
         } catch (Exception aEx)
         {
             aEx.printStackTrace();
         }
     }*/

    /*public void silOlustur()
     {
         try
         {
             String csil = "D:\\Sertifikalar\\ug\\ESYASIL.crl";
             byte[] s = AsnIO.dosyadanOKU(csil);
             Sil ss = new Sil(s);

             Calendar now = Calendar.getInstance();
             Time time = ServisContext.calendarToTime(now);

             Version aVersion = new Version(1);
              //Name aIssuer = UtilName.string2Name
              //("CN=UG Test Sertifika Makamı, OU=UEKAE, O=TÜBİTAK, L=Gebze, S=KOCAELİ,C = TR", true);
              Time aThisUpdate = time;
              Time aNextUpdate = time;
              AlgorithmIdentifier aAlgID = Ozellikler.getAlgIDFromAlgveOzet("RSA", "SHA-1");
              Extensions aSilExtensions = null;

              IptalListesi aIptalListesi = new VTIptalListesi();
              String aDll = "gclib";
              String aPwd = "12345";
              String aLabel = "test";
              int slot = -1;

              AY_BasitImzaci imzaci = new KarttaImzaci(aDll, aPwd, aLabel, slot);

              SilOlusturucu so = new SilOlusturucu(ss.tbsCertList.version, ss.tbsCertList.issuer,
                      ss.tbsCertList.thisUpdate,
                      ss.tbsCertList.nextUpdate, ss.tbsCertList.crlExtensions,aIptalListesi,imzaci);



             FileOutputStream fo = new FileOutputStream("c:\\testsil.crl");
             so.streameYaz(fo);
             System.out.println("bitti");
         }
         catch(Exception aEx)
         {
             aEx.printStackTrace();
         }
     }*/

    /*public class VTIptalListesi implements IptalListesi
     {
         int i = 1;
         public TBSCertList_revokedCertificates_element sonrakiniAl(){
             //return null;
             if(i < 6)
             {
                 TBSCertList_revokedCertificates_element e =  new TBSCertList_revokedCertificates_element();
                 Calendar now = Calendar.getInstance();
                 Time time = null;
                 try
                 {
                     time = ServisContext.calendarToTime(now);
                 } catch (ESYAServislerException e1)
                 {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                 }
                 e.revocationDate = time;
                 e.userCertificate = new Asn1BigInteger("1000");
                 i++;
             return e;
             }else
                 return null;

         }
         public void resetle(){
             i  =1;
         }
     }*/
    /*public static void silImzaKontrolEt()
     {
         try
         {
             String sild = "c:\\ugtestsila.crl";
             //sild = "D:\\Sertifikalar\\ug\\ESYASIL.crl";
             byte[] s = AsnIO.dosyadanOKU(sild);

             Sil sil = new Sil(s);
             Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
             sil.tbsCertList.encode(enc);
             byte[] imzalanan = enc.getMsgCopy();
             byte[] imza = sil.signature.value;
             enc.reset();
             String sertifikad = "c:\\sm.cer";
             //sertifikad = "D:\\Sertifikalar\\ug\\kok\\ESYA.cer";
             ESYASertifika sm = new ESYASertifika(AsnIO.dosyadanOKU(sertifikad));

             byte [] publickey = null;
             PublicKey key = sm.acikAnahtarAl();
             sm.tbsCertificate.subjectPublicKeyInfo.encode(enc);
             publickey = enc.getMsgCopy();
             AY_HafizadaTumKripto dogrulayici = HafizadaTumKripto.getInstance();
             key = dogrulayici.pubDecode(publickey,Ozellikler.getAsimAlgoFromOID(sm.tbsCertificate.subjectPublicKeyInfo.algorithm));
             System.out.println(dogrulayici.dogrula(imzalanan, imza, key, sil.signatureAlgorithm));

         } catch (Exception aEx)
         {
             aEx.printStackTrace();
         }
     }*/

/*    private static class MyRevokedListFetcher implements RevokedListFetcher {
        private List<ERevokedCertificateElement> certificateElements = new ArrayList<ERevokedCertificateElement>();
        private Iterator<ERevokedCertificateElement> iterator;

        {
            certificateElements.add(get1("125"));
            certificateElements.add(get1("126"));
            iterator = certificateElements.iterator();
        }
        public ERevokedCertificateElement getNext() throws ESYAException {
            return iterator.hasNext()?iterator.next():null;
        }

        private static ERevokedCertificateElement get1(String number) {
            ETime revocationDate = new ETime();
            try {
                revocationDate.setGeneralTime(new GregorianCalendar());
            } catch (Asn1Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return new ERevokedCertificateElement(new BigInteger(number,10), revocationDate);
        }

        public void reset() throws ESYAException {
            iterator = certificateElements.iterator();
        }
    }*/
}
