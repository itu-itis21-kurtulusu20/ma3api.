package tr.gov.tubitak.uekae.esya.api.common.lcns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class LV {
    protected static Logger logger = LoggerFactory.getLogger(LV.class);

    public enum Urunler {
        SERTIFIKA_MAKAMI(10), KAYIT_MAKAMI(11), AltSM(12), OCSP(38), ZAMAN_DAMGASI(39), OCSP_SERVISI(13),
        ESYA_ISTEMCI(60), KERMEN(50), KERMEN_LIGHT(51), TEST(90), ESYA_SUNUCU(80),

        ORTAK(40), AKILLIKART(41), SERTIFIKADOGRULAMA(42), CMSIMZA(43), CMSIMZAGELISMIS(44),
        XMLIMZA(45), XMLIMZAGELISMIS(46), CMSZARF(47), MOBILIMZA(48), ANDROIDIMZA(49),

        KEYMANAGER(71), KARTILKLENDIRME(72), GUVENLIPDF(73), ZD_ISTEMCI(52), KARTILKLENDIRME_V2(77), POSTACI(14), IMZAGER(100), IMZAGER_KURUMSAL(99),

        ELIT(66);

        private int mId;

        Urunler(int id) {
            mId = id;
        }

        public int getID() {
            return mId;
        }

        public static Urunler getUrun(int urunID) throws LE {
            Urunler[] urunler = values();
            for (Urunler urun : urunler) {
                if (urun.getID() == urunID)
                    return urun;
            }
            throw new LE("Urun " + urunID + " does not exist in Urunler Enum");
        }
    }

    private final String LISANSID = "LisansID";
    private final String KORUMA_KODU = "KorumaKodu";

    private final String TARIH_BILGI = "TarihBilgi";
    private final String TARIH_FORMAT = "dd/MM/yyyy";
    private final String BASLANGIC_TARIHI = "BaslangicTarihi";
    private final String BITIS_TARIHI = "BitisTarihi";

    private final String KURUM_BILGI = "KurumBilgi";
    private final String KURUM_ADI = "KurumAdi";
    private final String YETKILI = "Yetkili";
    private final String EPOSTA = "EPosta";
    private final String TELNO = "TelNo";
    private final String FAKS = "Faks";
    private final String ADRES = "Adres";

    private final String URUN_BILGI = "UrunBilgi";
    private final String URUN_ID = "UrunID";
    private final String KONTROL_BILGI = "KontrolBilgi";
    private final String AD = "Ad";
    private final String DEGER = "Deger";

    private final String TEST = "Test";
    private final String DEGIL = "degil";

    private final String JAVA = "Java";
    private final String CSHARP = "CSharp";
    private final String VAR = "var";
    private final String YOK = "yok";

    private final String BAKIM_SOZLESME_BITIS_TARIHI = "BakimSozlesmeBitisTarihi";

    private final String SERTIFIKA_ISSUER = "SertifikaIssuer";

    private final String ESNEK = "Esnek";

    private static LV mInstance;

    private Hashtable<Integer, Hashtable<String, String>> urunler;

    private int mLicenseID;
    private byte[] mKorumaKodu;

    private String mKurumAdi;
    private String mYetkili;
    private String mEposta;
    private String mTelno;
    private String mAdres;
    private String mFaks;

    private Date mBaslangicTarihi;
    private Date mBitisTarihi;

    private byte[] mLicenseData;


    /**
     * Get urun attribute
     *
     * @param urun        one of urun from the Urunler enum
     * @param controlName the control name. For example: "IP"
     * @return
     * @throws LE
     */
    public String getUrunAtt(Urunler urun, String controlName) throws LE {
        Hashtable<String, String> urunAttr = urunler.get(urun.getID());
        if (urunAttr != null)
            return urunAttr.get(controlName);

        throw new LE("The requested urun does not exist. LicenseID: " + mLicenseID);
    }


    /**
     * Get urun attribute
     *
     * @param urunID      one of urun from the Urunler enum
     * @param controlName the control name. For example: "IP"
     * @return
     * @throws LE
     */
    public String getUrunAtt(int urunID, String controlName) throws LE {
        Hashtable<String, String> urunAttr = urunler.get(urunID);
        if (urunAttr != null)
            return urunAttr.get(controlName);

        throw new LE("The requested urun does not exist. LicenseID: " + mLicenseID);
    }


    /**
     * Get urun attribute
     *
     * @param urun         one of urun from the Urunler enum
     * @param controlName  the control name. For example: "IP"
     * @param defaultValue the default value in case the corresponding key does not exist or is not paired with a value
     */
    public String getUrunAtt(Urunler urun, String controlName, String defaultValue) {
        Hashtable<String, String> urunAttr = urunler.get(urun.getID());
        if (urunAttr != null) {
            String att = urunAttr.get(controlName);
            if (att != null) {
                return att;
            }
        }
        return defaultValue;
    }

    /**
     * Return attribute in long type
     *
     * @param urun
     * @param controlName
     * @return
     * @throws LE
     */
    public long getUrunLongAtt(Urunler urun, String controlName) throws LE {
        return getUrunLongAtt(urun.getID(), controlName);
    }

    /**
     * Return attribute in long type
     *
     * @param urunID
     * @param controlName
     * @return
     * @throws LE
     */
    public long getUrunLongAtt(int urunID, String controlName) throws LE {
        String attr = getUrunAtt(urunID, controlName);
        try {
            return Long.parseLong(attr);
        } catch (Exception e) {
            throw new LE(" LicenseID: " + mLicenseID, e);
        }
    }


    //isTestLicense
    public boolean isTL(Urunler urun) throws LE {
        String isTest = getUrunAtt(urun.getID(), TEST);
        if (isTest != null && TEST.compareToIgnoreCase(isTest) == 0)
            return true;
        return false;
    }

    public void checkVersion(int urunID, String version) throws LE {
        String licenseVersion = getUrunAtt(urunID, TEST);
        if (licenseVersion.toUpperCase().compareTo(version) < 0)
            throw new LE("You don't have license for version " + version + ". Your license " +
                    "valid until version " + licenseVersion + " LicenseID: " + mLicenseID);
    }

    //Checks license dates. If license dates is not valid,
    //throws LE exception
    public void checkLD(Urunler urun) throws LE {
        checkLD(urun.getID());
    }

    //checkLicenseDate
    public void checkLD(int urunID) throws LE {
        Date today = new Date();
        Date startDate = getLicenseStartDate(urunID);
        Date expirationDate = getLicenseExpirationDate(urunID);
        if (!(today.after(startDate) && today.before(expirationDate)))
            throw new LE("License dates are not valid. LicenseID: " + mLicenseID);
    }

    public void checkIsItJava(int urunID) throws LE {
        String java = getUrunAtt(urunID, JAVA);
        if (java != null && !java.equalsIgnoreCase(VAR))
            throw new LE("You don't have MA3 API java license. LicenseID: " + mLicenseID);
    }

    //getLicenseID
    public int getLid() {
        return mLicenseID;
    }

    public String getKurumAdi() {
        return mKurumAdi;
    }

    public String getYetkili() {
        return mYetkili;
    }

    public String getEposta() {
        return mEposta;
    }

    public String getTelno() {
        return mTelno;
    }

    public String getAdres() {
        return mAdres;
    }

    public String getFaks() {
        return mFaks;
    }

    public byte[] getLicenseData() {
        return mLicenseData;
    }

    public void setLicenseData(byte[] licenseData) {
        this.mLicenseData = licenseData;
    }

    public Date getLicenseStartDate(int urunID) throws LE {
        Hashtable<String, String> urunAttr = urunler.get(urunID);
        if (urunAttr == null)
            return mBaslangicTarihi;

        String urunSpecificDate = urunAttr.get(BASLANGIC_TARIHI);
        if (urunSpecificDate != null) {
            SimpleDateFormat format = new SimpleDateFormat(TARIH_FORMAT);
            try {
                return format.parse(urunSpecificDate);
            } catch (ParseException e) {
                throw new LE("Can not parse License Date. LicenseID: " + mLicenseID);
            }
        } else
            return mBaslangicTarihi;
    }

    public Date getLicenseExpirationDate(int urunID) throws LE {
        Hashtable<String, String> urunAttr = urunler.get(urunID);
        if (urunAttr == null)
            return mBitisTarihi;

        String urunSpecificDate = urunAttr.get(BITIS_TARIHI);
        if (urunSpecificDate != null) {
            SimpleDateFormat format = new SimpleDateFormat(TARIH_FORMAT);
            try {
                return format.parse(urunSpecificDate);
            } catch (ParseException e) {
                throw new LE("Can not parse License Date. LicenseID: " + mLicenseID);
            }
        } else
            return mBitisTarihi;
    }

    public String getCertificateIssuer(Urunler urun) throws LE {

        String  issuerNameStr;
        try {
            issuerNameStr = getUrunAtt(urun, SERTIFIKA_ISSUER);
        } catch (Exception e) {
            throw new LE("Lisans dosyasında sertifika issuer alanı bulunamadı. Lisans dosyasını yenileyiniz! LicenseID: " + mLicenseID, e);
        }

        return issuerNameStr;
    }

    public boolean isPermissiveTimeStamp() throws LE {
        try {
            // default: not permissive
            String urunAtt = getUrunAtt(Urunler.ZD_ISTEMCI, ESNEK, DEGIL);

            if (urunAtt.equalsIgnoreCase(ESNEK)) {
                return true;
            } else if (urunAtt.equals(DEGIL)) { // default: not permissive
                return false;
            }
        } catch (Exception e) {
            throw new LE(GenelDil.mesaj(GenelDil.MESAJ_ESNEKZDYOK) + " LicenseID: " + mLicenseID, e);
        }

        // default: not permissive
        return false;
    }

    public synchronized static LV getInstance() throws LE {
        if (mInstance == null)
            mInstance = new LV();
        return mInstance;
    }

    public static void reset() {
        mInstance = null;
    }

    private LV() throws LE {
        boolean valid = validateSignature();
        if (valid == false)
            throw new LE("Signature not valid. LicenseID: " + mLicenseID);
        try {
            ByteArrayInputStream bs = new ByteArrayInputStream(mLicenseData);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(bs);
            doc.getDocumentElement().normalize();

            bs.close();
            mLicenseData = null;

            Node licenceRoot = doc.getChildNodes().item(0);
            NodeList mainNodes = licenceRoot.getChildNodes();

            urunler = new Hashtable<Integer, Hashtable<String, String>>();

            loadCommonAttributes(mainNodes);

            loadUrunAttributes(mainNodes);

            checkKorumaKodu();
            checkReleaseDate();
            Set<Integer> licensedUruns = urunler.keySet();
            for (Integer urunID : licensedUruns) {
                checkLD(urunID.intValue());
                checkIsItJava(urunID);
            }
        } catch (LE e) {
            throw e;
        } catch (ParseException e) {
            throw new LE("Can not parse license xml. LicenseID: " + mLicenseID, e);
        } catch (ParserConfigurationException e) {
            throw new LE("Can not parse license xml. LicenseID: " + mLicenseID, e);
        } catch (SAXException e) {
            throw new LE("Can not parse license xml. LicenseID: " + mLicenseID, e);
        } catch (IOException e) {
            throw new LE("Can not access license file. LicenseID: " + mLicenseID, e);
        }
    }

    private Urunler getUrun(Integer urunID) {
        Urunler[] registeredUruns = Urunler.values();
        for (Urunler urun : registeredUruns) {
            if (urun.getID() == urunID)
                return urun;
        }

        return null;
    }

    private void loadCommonAttributes(NodeList mainNodes) throws ParseException {
        //LisansID tag
        mLicenseID = Integer.parseInt(readNode(LISANSID, mainNodes));
        String korumaKoduStr = readNode(KORUMA_KODU, mainNodes);
        if (korumaKoduStr != null)
            mKorumaKodu = Base64.decode(korumaKoduStr);


        //TarihBilgi tag
        NodeList tarih = getNodeList(TARIH_BILGI, mainNodes);
        SimpleDateFormat format = new SimpleDateFormat(TARIH_FORMAT);
        mBaslangicTarihi = format.parse(readNode(BASLANGIC_TARIHI, tarih));
        mBitisTarihi = format.parse(readNode(BITIS_TARIHI, tarih));

        //KurumBilgi tag
        NodeList kurumBilgi = getNodeList(KURUM_BILGI, mainNodes);
        mKurumAdi = readNode(KURUM_ADI, kurumBilgi);
        mYetkili = readNode(YETKILI, kurumBilgi);
        mEposta = readNode(EPOSTA, kurumBilgi);
        mTelno = readNode(TELNO, kurumBilgi);
        mFaks = readNode(FAKS, kurumBilgi);
        mAdres = readNode(ADRES, kurumBilgi);
    }

    private void loadUrunAttributes(NodeList mainNodes) throws ParseException {
        NodeList[] urunNodes = getAllNodeList(URUN_BILGI, mainNodes);
        for (NodeList urun : urunNodes) {
            Hashtable<String, String> urunAttributes = new Hashtable<String, String>();
            int urunID = Integer.parseInt(readNode(URUN_ID, urun));

            urunler.put(urunID, urunAttributes);

            NodeList[] attributes = getAllNodeList(KONTROL_BILGI, urun);
            for (NodeList att : attributes) {
                String ad = readNode(AD, att);
                String deger = readNode(DEGER, att);
                if(deger != null)
                    urunAttributes.put(ad, deger);
            }
        }
    }

    private NodeList[] getAllNodeList(String tagName, NodeList nodes) {
        List<NodeList> list = new ArrayList<NodeList>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                if (node.getNodeName().equals(tagName) && node.hasChildNodes())
                    list.add(node.getChildNodes());
        }
        return list.toArray(new NodeList[0]);
    }

    private NodeList getNodeList(String tagName, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
                if (node.getNodeName().equals(tagName) && node.hasChildNodes())
                    return node.getChildNodes();
        }
        return null;
    }

    private String readNode(String tagName, NodeList nodes) {
        NodeList element = getNodeList(tagName, nodes);
        if (element != null)
            return element.item(0).getNodeValue();
        return null;
    }

    private boolean validateSignature() throws LE {

        String mLicenseCertStr = "-----BEGIN CERTIFICATE-----\n" +
                "MIIE6jCCAtKgAwIBAgIBLDANBgkqhkiG9w0BAQsFADAhMR8wHQYDVQQDDBZNQTMg" +
                "TGlzYW5zIFN1bnVjdXN1IDAxMB4XDTEwMDMxODExMzAxNloXDTMwMDMxODExMzAx" +
                "NlowITEfMB0GA1UEAwwWTUEzIExpc2FucyBTdW51Y3VzdSAwMTCCAiIwDQYJKoZI" +
                "hvcNAQEBBQADggIPADCCAgoCggIBAJ6ySMBTleYdNXt4kVPJEOWdWhDPXLPzrmNq" +
                "L/76mR1HzlSsV1NczcE+yuwcUN7lgnh24VhnSRq5mt0nr65GHHbHzILfrt600Obd" +
                "9NlhO+m71lsuMCIxmak6YPb7liQPlXbBAtKV3ZKQdsZ1tbeWm91xZOJWBGHp8SnG" +
                "Q2S7/OsYLpdWRo0m35XyiJeomEDonYTpM+lniws2AQQ4PTfyPoBSXkamQq8CE2ef" +
                "dcraCp3jEZMQ9U1BWqhuZ2s+INDHFFxMwf6a8EfKPgQi//g30DZq00yznE3Hm8Gz" +
                "t24qGnRoQxXyrbWYPxb11lAUDQ65jcv2qpUc3oqLsCWcMnmpgBQ/JZTdcg1QdovM" +
                "Jkiywqrx7ilGPXGh9m14+a8EOq9umdB12LqW1jXstdMYG4y/yBcZoy2tgshJ90ga" +
                "rz3sDOcDJCq5oetUI82YcteiG3MZC+zQPJti7sR4mYOi0oq66ohVyDXiOHfVjSvp" +
                "E4q0oWmvhbzyjep5ga480eEChTH2dDDfl2RaNuRTIf1ume4e1IESy9UbsxveG58c" +
                "QWskXfLOMyAvtLEsC4ck54tBrHEkB411UVx6+8AzNMN//DPE/bYrd5zo3UljklD6" +
                "svoMlDCnZ4ji2FiRVUheTD8am1COredm6lrz8TZk+LS/5SR6n13fXQ1e6UitzzkB" +
                "8R8vcSqNAgMBAAGjLTArMA4GA1UdDwEB/wQEAwIGwDAZBgNVHREEEjAQgQ5saXNh" +
                "bnNAbWEzLm5ldDANBgkqhkiG9w0BAQsFAAOCAgEAAWIBI3nx4TG757HS7tF2+8ZP" +
                "HFNvtRplUn2RlRUNK/HUBaPwasMYI+c+pcgZ+TjwMGSqUSQkyeuNWVQl9rJ7YIj7" +
                "Nwn2/1tbonaoLkRjam+31S8hF3kemmHqjeRDn0D7rGpXM+3ltptGiGFYAGw0T8uy" +
                "LrjrYQb1OlTuYjlHClp66wbn7aFZum/G9PqXM2MMxmRKC513nHVzCDVdWdXgMm+4" +
                "Gez7WggN0Lfj6FuIyxeMKaLTRkcZ6bsKu5sg226CUpZLULm0jiLAMyMH3LKEHxtn" +
                "AFYhm2701O5L6e6MzrXUFEMgGFH196liCfBr0l0qQ9GuSQB9L/7CxZ7mOa14th54" +
                "jLADIIRI95zTTrz2ic7NDCiZD71HXwPdfgoNsGauZc6DVKaTNR6uhRoHiadJCaMY" +
                "Qyaik5TMnXOKil3z6A1tci8JJcOVhzoC9e5ryZA/3mxkx16+UsrBQCGE3zl4GQ+h" +
                "HvlU2ROu0d+EFIeTXpq7qctNb3GGeaW2HCuzR5BvXYUdKzvpM48QdWKWwJsAf8T9" +
                "ewrj07QY9wG/wtrd38n2FdaARH8lAVCIyRPWCrxt6W6DdpanHJ0kOusZTgq02VLg" +
                "ZpN3N063ZCd4g1G/lL9BniiTduj8JZmBWlhA1tA3Sr/WnHpww9Xy+uyX7LsDVjKU" +
                "f+ew1LMAC62qHPSwxEE=" +
                "\n-----END CERTIFICATE-----";


        try {
            //Setted by LicenseUtil
            byte[] fileContent = getLicence();

            //Java System Property
            if (fileContent == null) {
                String license = System.getProperty("ma3License");
                if (license != null && !license.equalsIgnoreCase("EMPTY")) {
                    fileContent = Base64.decode(license);
                }
            }

            //OS System Property
            if (fileContent == null) {
                String license = System.getenv("ma3License");
                if (license != null && !license.equalsIgnoreCase("EMPTY")) {
                    fileContent = Base64.decode(license);
                }
            }

            //Java Enviroment File path
            if (fileContent == null) {
                String filePath = System.getProperty("ma3LicenseFilePath");
                //default path
                if (filePath == null) {
                    //System Envirovement File path
                    filePath = System.getenv("ma3LicenseFilePath");
                }

                //default path
                if (filePath == null)
                    filePath = "lisans/lisans.xml";


                FileInputStream fis = null;
                try{

                    File f = new File(filePath);
                    fis = new FileInputStream(f);

                    int length = (int) f.length();
                    fileContent = new byte[length];
                    fis.read(fileContent);

                }catch (Exception ex){
                    logger.warn("Problem in reading licence file from: " + filePath, ex.getMessage());
                }finally {
                    if(fis != null)
                       fis.close();
                }
            }

            if (fileContent == null)
                throw new LE("Lisans bulunamadi.");

            try
            {
                extractLicenseIDFromContent(fileContent);
            }
            catch (Exception ex)
            {
                throw new LE("Can not get License ID.", ex);
            }

            try {
                return validateSignature(fileContent, mLicenseCertStr);
            } catch (LE ex) {
                throw ex;
            } catch (Exception e) {
                throw new LE(e.getMessage() + " LicenseID: " + mLicenseID, e);
            }
        } catch (LE ex) {
            throw ex;
        } catch (Exception e) {
            throw new LE(e.getMessage() + " LicenseID: " + mLicenseID, e);
        }
    }

    private void extractLicenseIDFromContent(byte[] fileContent)
    {
        int startIndex = ByteUtil.findIndex(fileContent, 0, "<LisansID>".getBytes()) + "<LisansID>".length();
        int endIndex = ByteUtil.findIndex(fileContent, startIndex, "</LisansID>".getBytes()) ;

        String mLicenseIDStr = new String(fileContent, startIndex, endIndex - startIndex).trim();

        mLicenseID = Integer.decode(mLicenseIDStr);

    }

    private boolean validateSignature(byte[] fileContent, String mLicenseCertStr) throws LE {

        try {
            ByteArrayInputStream cerInputStream = new ByteArrayInputStream(mLicenseCertStr.getBytes("ASCII"));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate gecerliSertifika = (X509Certificate) cf.generateCertificate(cerInputStream);

            PKCS7 pkcs7 = new PKCS7(fileContent);
            SignerInfo firstSignerInfo = pkcs7.getFirstSignerInfo();
            boolean verifyResult = firstSignerInfo.verify(gecerliSertifika);

            if(verifyResult == true)
                setLicenseData(pkcs7.getContent());

            return verifyResult;
        } catch (Exception e) {
            if(e.getMessage() == null){
                throw new LE( "License Error. LicenseID: " + mLicenseID, e);
            } else {
                throw new LE("Failed to verify license signature. The content of the license may have been changed. LicenseID: " + mLicenseID, e);
            }
        }
    }

    private byte[] getLicence() throws LE {
        Method method;
        try {
            method = Class.forName("tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil").getMethod("getLicense");
            return (byte[]) method.invoke(null);
        } catch (Exception e) {
            logger.warn("Warning in LV", e);
            return null;
        }

    }

    private void checkReleaseDate() throws LE {
        Bld build = new Bld();
        String releaseDateStr = build.gD();
        String bakimBitisTarihiStr;
        try {
            bakimBitisTarihiStr = getUrunAtt(Urunler.ORTAK, BAKIM_SOZLESME_BITIS_TARIHI);
        } catch (Exception e) {
            throw new LE("Lisans dosyasında bakım sözleşme tarihi bulunamadı. Lisans dosyasını yenileyiniz! LicenseID: " + mLicenseID, e);
        }

        try {
            SimpleDateFormat licenseFileFormat = new SimpleDateFormat(TARIH_FORMAT);
            Date bakimBitisTarihi = licenseFileFormat.parse(bakimBitisTarihiStr);

            SimpleDateFormat releaseDateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
            Date releaseDate = releaseDateFormat.parse(releaseDateStr);

            if (bakimBitisTarihi.before(releaseDate))
                throw new LE("Bakim sözleşmeniz sonlanmıştır. LicenseID: " + mLicenseID);
        } catch (ParseException e) {
            throw new LE("Bakım sözleşme tarihini tarih formatına dönüştürmede hata. LicenseID: " + mLicenseID);
        }
    }

    private void checkKorumaKodu() throws LE {
        //Koruma kodu yoksa kontrol edilmesin.
        if (mKorumaKodu == null)
            return;

        int decryptedLisansID = 0;
        String password;

        try {
            Method method = Class.forName("tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil").getMethod("getPassword");
            password = (String) method.invoke(null);
        } catch (Exception e) {
            throw new LE("Lisans parolanız alınamadı. LicenseID: " + mLicenseID, e);
        }

        if (password == null)
            throw new LE("Lisans parolanız ayarlanmamış. Lisansla birlikte lisans parolanızı da veriniz. LicenseID: " + mLicenseID);


        try {
            /*
            byte[] salt = "87654321".getBytes("ASCII");            
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 192);
            SecretKey tmp = factory.generateSecret(spec);   
            SecretKey secret1 = new SecretKeySpec(tmp.getEncoded(), "TripleDES");
            System.out.println(new String(Base64.encode(tmp.getEncoded())));
              */
            byte[] bytes = derive(password, "87654321", 100, 24);
            SecretKey secret = new SecretKeySpec(bytes, "TripleDES");
            //System.out.println(Base64.encode(bytes));

            byte[] iv = "12345678".getBytes("ASCII");
            Cipher decipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            byte[] decryptedData = decipher.doFinal(mKorumaKodu);
            decryptedLisansID = new Integer(new String(decryptedData, "ASCII"));
        } catch (Exception aEx) {
            throw new LE("Koruma kodu çözmede hata. LicenseID: " + mLicenseID, aEx);
        }

        if (decryptedLisansID != mLicenseID)
            throw new LE("Koruma kodunuz çözülemedi. Lisans parolanız hatalı olabilir. LicenseID: " + mLicenseID);
    }

    /* START RFC 2898 IMPLEMENTATION */
    private static byte[] derive(String P, String S, int c, int dkLen) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            int hLen = 20;

            if (dkLen > ((Math.pow(2, 32)) - 1) * hLen) {
                System.out.println("derived key too long");
            } else {
                int l = (int) Math.ceil((double) dkLen / (double) hLen);
                // int r = dkLen - (l-1)*hLen;

                for (int i = 1; i <= l; i++) {
                    byte[] T = F(P, S, c, i);
                    baos.write(T);
                }
            }
        } catch (Exception e) {
            logger.error("Error in LV", e);
        }

        byte[] baDerived = new byte[dkLen];
        System.arraycopy(baos.toByteArray(), 0, baDerived, 0, baDerived.length);

        return baDerived;
    }

    private static byte[] F(String P, String S, int c, int i) throws Exception {
        byte[] U_LAST = null;
        byte[] U_XOR = null;

        SecretKeySpec key = new SecretKeySpec(P.getBytes("UTF-8"), "HmacSHA1");
        Mac mac = Mac.getInstance(key.getAlgorithm());
        mac.init(key);

        for (int j = 0; j < c; j++) {
            if (j == 0) {
                byte[] baS = S.getBytes("UTF-8");
                byte[] baI = INT(i);
                byte[] baU = new byte[baS.length + baI.length];

                System.arraycopy(baS, 0, baU, 0, baS.length);
                System.arraycopy(baI, 0, baU, baS.length, baI.length);

                U_XOR = mac.doFinal(baU);
                U_LAST = U_XOR;
                mac.reset();
            } else {
                byte[] baU = mac.doFinal(U_LAST);
                mac.reset();

                for (int k = 0; k < U_XOR.length; k++) {
                    U_XOR[k] = (byte) (U_XOR[k] ^ baU[k]);
                }

                U_LAST = baU;
            }
        }

        return U_XOR;
    }

    private static byte[] INT(int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(i);

        return bb.array();
    }
}
