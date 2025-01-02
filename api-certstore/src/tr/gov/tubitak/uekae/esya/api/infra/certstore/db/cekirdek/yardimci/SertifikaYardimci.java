package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class SertifikaYardimci implements ModelNesneYardimci {
    public static final String COLUMN_SERTIFIKA_DATE = "EklenmeTarihi";
    public static final String COLUMN_SERTIFIKA_VALUE = "SertifikaValue";
    //public static final String COLUMN_SERTIFIKA_HASH_NO = "HashNo";
    public static final String COLUMN_SERTIFIKA_SERIAL = "SerialNumber";
    public static final String COLUMN_SERTIFIKA_ISSUER = "IssuerName";
    public static final String COLUMN_SERTIFIKA_START_DATE = "StartDate";
    public static final String COLUMN_SERTIFIKA_END_DATE = "EndDate";
    public static final String COLUMN_SERTIFIKA_SUBJECT = "SubjectName";
    public static final String COLUMN_SERTIFIKA_SUBJECT_STR = "SubjectNameStr";
    public static final String COLUMN_SERTIFIKA_KEYUSAGE = "KeyUsageStr";
    public static final String COLUMN_SERTIFIKA_SUBJECT_KEY_ID = "SubjectKeyIdentifier";
    public static final String COLUMN_SERTIFIKA_EPOSTA = "EPosta";
    public static final String COLUMN_SERTIFIKA_PRIVATE_KEY = "PrivateKeyValue";
    public static final String COLUMN_SERTIFIKA_PKCS11_LIB = "PKCS11Lib";
    public static final String COLUMN_SERTIFIKA_PKCS11_ID = "PKCS11Id";
    public static final String COLUMN_SERTIFIKA_NO = "SertifikaNo";
    public static final String COLUMN_SERTIFIKA_CARD_SERIAL_NUMBER = "CardSerialNumber";
    public static final String COLUMN_SERTIFIKA_X400ADDRESS = "X400Address";
    public static final Locale EN_LOCALE = new Locale("en");

    public static final String SERTIFIKA_TABLO_ADI = "SERTIFIKA";

    private static final String[] COLUMNS = new String[]
            {
                    COLUMN_SERTIFIKA_DATE,
                    COLUMN_SERTIFIKA_VALUE,
                    //COLUMN_SERTIFIKA_HASH_NO,
                    COLUMN_SERTIFIKA_SERIAL,
                    COLUMN_SERTIFIKA_ISSUER,
                    COLUMN_SERTIFIKA_START_DATE,
                    COLUMN_SERTIFIKA_END_DATE,
                    COLUMN_SERTIFIKA_SUBJECT,
                    COLUMN_SERTIFIKA_SUBJECT_STR,
                    COLUMN_SERTIFIKA_KEYUSAGE,
                    COLUMN_SERTIFIKA_SUBJECT_KEY_ID,
                    COLUMN_SERTIFIKA_EPOSTA,
                    COLUMN_SERTIFIKA_PRIVATE_KEY,
                    COLUMN_SERTIFIKA_PKCS11_LIB,
                    COLUMN_SERTIFIKA_PKCS11_ID,
                    COLUMN_SERTIFIKA_CARD_SERIAL_NUMBER,
                    COLUMN_SERTIFIKA_X400ADDRESS,
                    COLUMN_SERTIFIKA_NO
            };


    public String tabloAdiAl() {
        return SERTIFIKA_TABLO_ADI;
    }


    public String[] sutunAdlariAl() {
        return COLUMNS;
    }

    public void sorguyuDoldur(Object aModelNesnesi, PreparedStatement aPS)
            throws SQLException {
        DepoSertifika sertifika = (DepoSertifika) aModelNesnesi;
        Date eklenmeDate = sertifika.getEklenmeTarihi();
        if (eklenmeDate != null)
            aPS.setDate(1, eklenmeDate);
        else
            aPS.setDate(1, new Date(System.currentTimeMillis()));
        aPS.setBytes(2, sertifika.getValue());
        //aPS.setLong(3, sertifika.getmHashNo());
        aPS.setBytes(3, sertifika.getSerialNumber());
        aPS.setBytes(4, sertifika.getNormalizedIssuerName());
        aPS.setDate(5, sertifika.getStartDate());
        aPS.setDate(6, sertifika.getEndDate());
        aPS.setBytes(7, sertifika.getNormalizedSubjectName());
        aPS.setString(8, sertifika.getSubjectNameStr());
        aPS.setString(9, sertifika.getKeyUsageStr());
        aPS.setBytes(10, sertifika.getSubjectKeyID());

        String eposta = sertifika.getEPosta();
        if (eposta != null)
            eposta = eposta.toLowerCase(EN_LOCALE);
        aPS.setString(11, eposta);
        aPS.setBytes(12, sertifika.getPrivateKey());
        aPS.setString(13, sertifika.getPKCS11Lib());
        aPS.setBytes(14, sertifika.getPKCS11ID());
        aPS.setString(15, sertifika.getCardSerialNumber());
        aPS.setString(16, sertifika.getX400Address());
        aPS.setObject(17, sertifika.getSertifikaNo());
    }

    public Object nesneyiDoldur(ResultSet aRS)
            throws SQLException {
        DepoSertifika sertifika = new DepoSertifika();
        sertifika.setEklenmeTarihi(aRS.getDate(COLUMN_SERTIFIKA_DATE));
        sertifika.setValue(aRS.getBytes(COLUMN_SERTIFIKA_VALUE));
        sertifika.setSerialNumber(aRS.getBytes(COLUMN_SERTIFIKA_SERIAL));
        sertifika.setIssuerName(aRS.getBytes(COLUMN_SERTIFIKA_ISSUER));
        //TODO date index ler degisecek
        sertifika.setStartDate(aRS.getDate(COLUMN_SERTIFIKA_START_DATE));
        sertifika.setEndDate(aRS.getDate(COLUMN_SERTIFIKA_END_DATE));
        sertifika.setSubjectName(aRS.getBytes(COLUMN_SERTIFIKA_SUBJECT));
        sertifika.setSubjectNameStr(aRS.getString(COLUMN_SERTIFIKA_SUBJECT_STR));
        sertifika.setKeyUsageStr(aRS.getString(COLUMN_SERTIFIKA_KEYUSAGE));

        String eposta = aRS.getString(COLUMN_SERTIFIKA_EPOSTA);
        if (eposta != null)
            eposta = eposta.toLowerCase(EN_LOCALE);
        sertifika.setEPosta(eposta);
        sertifika.setPrivateKey(aRS.getBytes(COLUMN_SERTIFIKA_PRIVATE_KEY));
        sertifika.setPKCS11Lib(aRS.getString(COLUMN_SERTIFIKA_PKCS11_LIB));
        sertifika.setPKCS11ID(aRS.getBytes(COLUMN_SERTIFIKA_PKCS11_ID));
        sertifika.setCardSerialNumber(aRS.getString(COLUMN_SERTIFIKA_CARD_SERIAL_NUMBER));
        sertifika.setX400Address(aRS.getString(COLUMN_SERTIFIKA_X400ADDRESS));
        sertifika.setSertifikaNo(aRS.getLong(COLUMN_SERTIFIKA_NO));
        sertifika.setSubjectKeyID(aRS.getBytes(COLUMN_SERTIFIKA_SUBJECT_KEY_ID));
        return sertifika;
    }
}
