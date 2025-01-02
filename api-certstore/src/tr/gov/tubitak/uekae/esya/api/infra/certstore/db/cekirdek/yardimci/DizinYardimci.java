package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author ahmety
 */
public class DizinYardimci implements ModelNesneYardimci
{

    public static final String COLUMN_DIZIN_AD = "DIZINADI";
    public static final String COLUMN_DIZIN_EKLENME_TARIHI = "EKLENMETARIHI";
    public static final String COLUMN_DIZIN_NO = "DIZINNO";

    public static final String DIZIN_TABLO_ADI = "DIZIN";
    
    private static final long DEFAULT_DIZIN_NO = 1;
    
    private static final String[] COLUMNS = new String[] {
                       DizinYardimci.COLUMN_DIZIN_EKLENME_TARIHI,
                       DizinYardimci.COLUMN_DIZIN_AD,
                       DizinYardimci.COLUMN_DIZIN_NO
   };

    
    public String tabloAdiAl()
    {
        return DIZIN_TABLO_ADI;
    }

    public String[] sutunAdlariAl()
    {
        return COLUMNS;
    }
    
    public static long getDefaultDizinNo()
    {
    	return DEFAULT_DIZIN_NO;
    }

    public void sorguyuDoldur(Object aMN,PreparedStatement aPS)
    throws SQLException
	{
    	DepoDizin dizin = (DepoDizin) aMN;
		Date eklenmeDate = dizin.getEklenmeTarihi();
		if(eklenmeDate != null)
			aPS.setDate(1, eklenmeDate);
		else
			aPS.setDate(1, new Date(System.currentTimeMillis()));
		aPS.setString(2, dizin.getDizinAdi());
		aPS.setObject(3,dizin.getDizinNo());
	}

    public Object nesneyiDoldur(ResultSet aRS) 
    throws SQLException
	{
		DepoDizin dizin = new DepoDizin();
		dizin.setDizinAdi(aRS.getString(COLUMN_DIZIN_AD));
		dizin.setEklenmeTarihi(aRS.getDate(COLUMN_DIZIN_EKLENME_TARIHI));
		dizin.setDizinNo(aRS.getLong(COLUMN_DIZIN_NO));
		return dizin;
	}

}
