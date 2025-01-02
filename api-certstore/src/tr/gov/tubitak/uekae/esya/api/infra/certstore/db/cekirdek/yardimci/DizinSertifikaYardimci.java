package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;


import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DizinSertifikaYardimci implements ModelNesneYardimci
{
	public static final String COLUMN_DIZINSERTIFIKA_DIZIN_NO = "DizinNo";
	public static final String COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO = "SertifikaNo";

	public static final String DIZINSERTIFIKA_TABLO_ADI = "DIZINSERTIFIKA";
	
	private static final String[] COLUMNS = new String[] 
	{
		COLUMN_DIZINSERTIFIKA_DIZIN_NO,
		COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO
	};
	
	
	
	public String tabloAdiAl()
    {
        return DIZINSERTIFIKA_TABLO_ADI;
    }


    public String[] sutunAdlariAl()
    {
        return COLUMNS;
    }
	
	public void sorguyuDoldur(Object aModelNesnesi, PreparedStatement aPS)
    throws SQLException
    {
		
    }
	
	 public Object nesneyiDoldur(ResultSet aRS)
	 throws SQLException
	 {
		 return null;
	 }
}
