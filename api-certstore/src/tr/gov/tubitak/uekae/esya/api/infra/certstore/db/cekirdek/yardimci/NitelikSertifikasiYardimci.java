package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoNitelikSertifikasi;

public class NitelikSertifikasiYardimci implements ModelNesneYardimci{

	public static final String COLUMN_NITELIKSERTIFIKASI_ID = "NitelikSertifikasiNo";
	public static final String COLUMN_SERTIFIKA_ID = "SertifikaNo";
	public static final String COLUMN_NITELIKSERTIFIKASI_VALUE = "NitelikSertifikasiValue";
	public static final String COLUMN_HOLDER_DNNAME = "HolderDNName";
	
	
	public static final String NITELIKSERTIFIKASI_TABLO_ADI = "NITELIKSERTIFIKASI";
	
	public static final String[] COLUMNS = 
	{
		COLUMN_SERTIFIKA_ID,
		COLUMN_NITELIKSERTIFIKASI_VALUE,
		COLUMN_HOLDER_DNNAME,
		COLUMN_NITELIKSERTIFIKASI_ID
	};
	
	public String tabloAdiAl() 
	{
		return NITELIKSERTIFIKASI_TABLO_ADI;
	}

	public String[] sutunAdlariAl() 
	{
		return COLUMNS;
	}

	public void sorguyuDoldur(Object aModelNesnesi, PreparedStatement aPS)
	throws SQLException 
	{
		DepoNitelikSertifikasi ns = (DepoNitelikSertifikasi) aModelNesnesi;
		aPS.setLong(1, ns.getSertifikaNo());
		aPS.setBytes(2, ns.getValue());
		aPS.setString(3, ns.getHolderDNName());
		aPS.setObject(4, ns.getNitelikSertifikaNo());
	}

	public Object nesneyiDoldur(ResultSet aRS) 
	throws SQLException 
	{
		DepoNitelikSertifikasi ns = new DepoNitelikSertifikasi();
		ns.setSertifikaNo(aRS.getLong(COLUMN_SERTIFIKA_ID));
		ns.setValue(aRS.getBytes(COLUMN_NITELIKSERTIFIKASI_VALUE));
		ns.setHolderDNName(aRS.getString(COLUMN_HOLDER_DNNAME));
		ns.setNitelikSertifikaNo(aRS.getLong(COLUMN_NITELIKSERTIFIKASI_ID));
		return ns;
	}

}
