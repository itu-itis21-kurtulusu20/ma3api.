package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class KokSertifikaYardimci implements ModelNesneYardimci
{
	public static final String COLUMN_KOKSERTIFIKA_DATE = "KOKEklenmeTarihi";
	public static final String COLUMN_KOKSERTIFIKA_VALUE = "KOKSertifikaValue";
	//public static final String COLUMN_KOKSERTIFIKA_HASH = "KOKSertifikaHash";
	public static final String COLUMN_KOKSERTIFIKA_SERIAL = "KOKSerialNumber";
	public static final String COLUMN_KOKSERTIFIKA_ISSUER = "KOKIssuerName";
	public static final String COLUMN_KOKSERTIFIKA_START_DATE = "KOKStartDate";
	public static final String COLUMN_KOKSERTIFIKA_END_DATE = "KOKEndDate";
	public static final String COLUMN_KOKSERTIFIKA_SUBJECT = "KOKSubjectName";
	public static final String COLUMN_KOKSERTIFIKA_KEYUSAGE = "KOKKeyUsageStr";
	public static final String COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID = "KOKSubjectKeyIdentifier";
	public static final String COLUMN_KOKSERTIFIKA_TIP = "KOKSertifikaTipi";
	public static final String COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI = "KOKGuvenSeviyesi";
	public static final String COLUMN_KOKSERTIFIKA_SATIR_IMZA = "KOKSatirImzasi";
	public static final String COLUMN_KOKSERTIFIKA_NO = "KOKSertifikaNo";
	
	
	public static final String KOKSERTIFIKA_TABLO_ADI = "KOKSERTIFIKA";
	
	public static final String[] COLUMNS = 
	{
		COLUMN_KOKSERTIFIKA_DATE,
		COLUMN_KOKSERTIFIKA_VALUE,
		//COLUMN_KOKSERTIFIKA_HASH,
		COLUMN_KOKSERTIFIKA_SERIAL,
		COLUMN_KOKSERTIFIKA_ISSUER,
		COLUMN_KOKSERTIFIKA_START_DATE,
		COLUMN_KOKSERTIFIKA_END_DATE,
		COLUMN_KOKSERTIFIKA_SUBJECT,
		COLUMN_KOKSERTIFIKA_KEYUSAGE,
		COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID,
		COLUMN_KOKSERTIFIKA_TIP,
		COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI,
		COLUMN_KOKSERTIFIKA_SATIR_IMZA,
		COLUMN_KOKSERTIFIKA_NO
		
	};

	public Object nesneyiDoldur(ResultSet aRS) 
	throws SQLException
    {
		DepoKokSertifika sertifika = new DepoKokSertifika();
    	sertifika.setKokEklenmeTarihi(aRS.getDate(COLUMN_KOKSERTIFIKA_DATE));
    	sertifika.setEndDate(aRS.getDate(COLUMN_KOKSERTIFIKA_END_DATE));
    	sertifika.setStartDate(aRS.getDate(COLUMN_KOKSERTIFIKA_START_DATE));
   		sertifika.setSerialNumber(aRS.getBytes(COLUMN_KOKSERTIFIKA_SERIAL));
   		sertifika.setValue(aRS.getBytes(COLUMN_KOKSERTIFIKA_VALUE));
   		sertifika.setIssuerName(aRS.getBytes(COLUMN_KOKSERTIFIKA_ISSUER));
   		sertifika.setSubjectName(aRS.getBytes(COLUMN_KOKSERTIFIKA_SUBJECT));
   		sertifika.setKeyUsageStr(aRS.getString(COLUMN_KOKSERTIFIKA_KEYUSAGE));
   		sertifika.setKokTipi(SertifikaTipi.getNesne(aRS.getInt(COLUMN_KOKSERTIFIKA_TIP)));
   		sertifika.setKokGuvenSeviyesi(GuvenlikSeviyesi.getNesne(aRS.getInt(COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI)));
   		sertifika.setSatirImzasi(aRS.getBytes(COLUMN_KOKSERTIFIKA_SATIR_IMZA));
   		sertifika.setSubjectKeyIdentifier(aRS.getBytes(COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID));
   		sertifika.setKokSertifikaNo(aRS.getLong(COLUMN_KOKSERTIFIKA_NO));
    	return sertifika;
    }

	public void sorguyuDoldur(Object aModelNesnesi,PreparedStatement aPS) 
	throws SQLException
    {
		DepoKokSertifika sertifika = (DepoKokSertifika) aModelNesnesi;
		Date eklenmeDate = sertifika.getKokEklenmeTarihi();
		if(eklenmeDate != null)
			aPS.setDate(1, eklenmeDate);
		else
			aPS.setDate(1,new Date(System.currentTimeMillis()));
		aPS.setBytes(2,sertifika.getValue());
		//aPS.setBytes(3, sertifika.getmHash());
		aPS.setBytes(3, sertifika.getSerialNumber());
		aPS.setBytes(4,sertifika.getIssuerName());
		aPS.setDate(5, sertifika.getStartDate());
		aPS.setDate(6, sertifika.getEndDate());
		aPS.setBytes(7,sertifika.getSubjectName());
		aPS.setString(8,sertifika.getKeyUsageStr());
		aPS.setBytes(9, sertifika.getSubjectKeyIdentifier());
		aPS.setInt(10, sertifika.getKokTipi().getIntValue());
		aPS.setInt(11, sertifika.getKokGuvenSeviyesi().getIntValue());
		aPS.setBytes(12,sertifika.getSatirImzasi());
		aPS.setObject(13, sertifika.getKokSertifikaNo());
    }

	public String[] sutunAdlariAl()
    {
	    return COLUMNS;
    }

	public String tabloAdiAl()
    {
	    return KOKSERTIFIKA_TABLO_ADI;
    }
}
