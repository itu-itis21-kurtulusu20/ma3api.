package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSilinecekKokSertifika;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SilinecekKokSertifikaYardimci implements ModelNesneYardimci
{
	public static final String COLUMN_SILINECEKKOKSERTIFIKA_DATE = "KOKEklenmeTarihi";
	public static final String COLUMN_SILINECEKKOKSERTIFIKA_VALUE = "KOKSertifikaValue";
	public static final String COLUMN_SILINECEKKOKSERTIFIKA_SERIAL = "KOKSerialNumber";
	public static final String COLUMN_SILINECEKKOKSERTIFIKA_ISSUER = "KOKIssuerName";
	public static final String COLUMN_SILINECEKKOKSERTIFIKA_SUBJECT = "KOKSubjectName";
	public static final String COLUMN_SILINECEKKOKSERTIFIKA_SATIR_IMZA = "KOKSatirImzasi";
	public static final String COLUMN_SILINECEKKOKSERTIFIKA_NO = "KOKSertifikaNo";
	
	
	public static final String SILINECEKKOKSERTIFIKA_TABLO_ADI = "SILINECEKKOKSERTIFIKA";
	
	public static final String[] COLUMNS = 
	{
		COLUMN_SILINECEKKOKSERTIFIKA_DATE,
		COLUMN_SILINECEKKOKSERTIFIKA_VALUE,
		COLUMN_SILINECEKKOKSERTIFIKA_SERIAL,
		COLUMN_SILINECEKKOKSERTIFIKA_ISSUER,
		COLUMN_SILINECEKKOKSERTIFIKA_SUBJECT,
		COLUMN_SILINECEKKOKSERTIFIKA_SATIR_IMZA,
		COLUMN_SILINECEKKOKSERTIFIKA_NO
	};
	
	public String tabloAdiAl()
    {
        return SILINECEKKOKSERTIFIKA_TABLO_ADI;
    }


    public String[] sutunAdlariAl()
    {
        return COLUMNS;
    }
	
	public Object nesneyiDoldur(ResultSet aRS) 
	throws SQLException
    {
		DepoSilinecekKokSertifika sertifika = new DepoSilinecekKokSertifika();
		sertifika.setKokEklenmeTarihi(aRS.getDate(1));
		sertifika.setSerialNumber(aRS.getBytes(COLUMN_SILINECEKKOKSERTIFIKA_SERIAL));			sertifika.setValue(aRS.getBytes(COLUMN_SILINECEKKOKSERTIFIKA_VALUE));
		sertifika.setIssuerName(aRS.getBytes(COLUMN_SILINECEKKOKSERTIFIKA_ISSUER));
		sertifika.setSubjectName(aRS.getBytes(COLUMN_SILINECEKKOKSERTIFIKA_SUBJECT));
		sertifika.setSatirImzasi(aRS.getBytes(COLUMN_SILINECEKKOKSERTIFIKA_SATIR_IMZA));
		sertifika.setKokSertifikaNo(aRS.getLong(COLUMN_SILINECEKKOKSERTIFIKA_NO));
		return sertifika;
    }
	
	public void sorguyuDoldur(Object aModelNesnesi,PreparedStatement aPS) 
	throws SQLException
    {
		DepoSilinecekKokSertifika sertifika = (DepoSilinecekKokSertifika) aModelNesnesi;
		Date eklenmeDate = sertifika.getKokEklenmeTarihi();
		if(eklenmeDate != null)
			aPS.setDate(1, eklenmeDate);
		else
			aPS.setDate(1,new Date(System.currentTimeMillis()));
		aPS.setBytes(2,sertifika.getValue());
		aPS.setBytes(3, sertifika.getSerialNumber());
		aPS.setBytes(4,sertifika.getIssuerName());
		aPS.setBytes(5,sertifika.getSubjectName());
		aPS.setBytes(6,sertifika.getSatirImzasi());
		aPS.setObject(7, sertifika.getKokSertifikaNo());
    }
}
