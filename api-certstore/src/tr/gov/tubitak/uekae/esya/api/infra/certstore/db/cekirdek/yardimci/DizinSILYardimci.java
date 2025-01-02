package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;


import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DizinSILYardimci implements ModelNesneYardimci
{
	public static final String COLUMN_DIZINSIL_DIZIN_NO = "DizinNo";
	public static final String COLUMN_DIZINSIL_SIL_NO = "SILNo";

	public static final String DIZINSIL_TABLO_ADI = "DIZINSIL";
	
	private static final String[] COLUMNS = new String[] 
	{
		COLUMN_DIZINSIL_DIZIN_NO,
		COLUMN_DIZINSIL_SIL_NO
	};
	
	
	
	public String tabloAdiAl()
    {
        return DIZINSIL_TABLO_ADI;
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
