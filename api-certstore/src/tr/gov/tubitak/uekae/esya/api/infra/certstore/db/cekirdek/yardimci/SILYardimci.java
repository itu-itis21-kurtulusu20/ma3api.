package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSIL;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SILYardimci implements ModelNesneYardimci
{
	public static final String COLUMN_SIL_DATE = "EklenmeTarihi";
	public static final String COLUMN_SIL_VALUE = "SILValue";
	//public static final String COLUMN_SIL_HASH = "SILHash";
	public static final String COLUMN_SIL_ISSUER_NAME = "IssuerName";
	public static final String COLUMN_SIL_THIS_UPDATE = "ThisUpdate";
	public static final String COLUMN_SIL_NEXT_UPDATE = "NextUpdate";
	public static final String COLUMN_SIL_NUMBER = "SILNumber";
	public static final String COLUMN_SIL_BASE_SIL_NUMBER = "BaseSILNumber";
	public static final String COLUMN_SIL_TIPI = "SILTipi";
	public static final String COLUMN_SIL_NO = "SILNo";
	
	public static final String SIL_TABLO_ADI = "SIL";
	
	private static final String[] COLUMNS = 
	{
		COLUMN_SIL_DATE,
		COLUMN_SIL_VALUE,
		//COLUMN_SIL_HASH,
		COLUMN_SIL_ISSUER_NAME,
		COLUMN_SIL_THIS_UPDATE,
		COLUMN_SIL_NEXT_UPDATE,
		COLUMN_SIL_NUMBER,
		COLUMN_SIL_BASE_SIL_NUMBER,
		COLUMN_SIL_TIPI,
		COLUMN_SIL_NO
	};

	public Object nesneyiDoldur(ResultSet aRS) 
	throws SQLException
    {
	    DepoSIL sil = new DepoSIL();
	    sil.setEklenmeTarihi(aRS.getDate(COLUMN_SIL_DATE));
	   	sil.setValue(aRS.getBytes(COLUMN_SIL_VALUE));
	   	sil.setIssuerName(aRS.getBytes(COLUMN_SIL_ISSUER_NAME));
	   	sil.setThisUpdate(aRS.getDate(COLUMN_SIL_THIS_UPDATE));
    	sil.setNextUpdate(aRS.getDate(COLUMN_SIL_NEXT_UPDATE));
    	sil.setSILNumber(aRS.getBytes(COLUMN_SIL_NUMBER));
	   	sil.setBaseSILNumber(aRS.getBytes(COLUMN_SIL_BASE_SIL_NUMBER));
	   	sil.setSILTipi(SILTipi.getNesne(aRS.getInt(COLUMN_SIL_TIPI)));
	   	sil.setSILNo(aRS.getLong(COLUMN_SIL_NO));
	    return sil;
	}

	public void sorguyuDoldur(Object aModelNesnesi,PreparedStatement aPS) 
	throws SQLException
    {
	    DepoSIL sil = (DepoSIL) aModelNesnesi;
	    Date eklenmeDate = sil.getEklenmeTarihi();
	   	if(eklenmeDate != null)
	    	aPS.setDate(1,eklenmeDate);
	    else
	    	aPS.setDate(1, new Date(System.currentTimeMillis()));
	   	aPS.setBytes(2, sil.getValue());
	    //aPS.setBytes(3, sil.getmHash());
	    aPS.setBytes(3, sil.getIssuerName());
	    aPS.setDate(4, sil.getThisUpdate());
	    aPS.setDate(5, sil.getNextUpdate());
	    aPS.setBytes(6, sil.getSILNumber());
	    aPS.setBytes(7, sil.getBaseSILNumber());
	    aPS.setInt(8, sil.getSILTipi().getIntValue());
	    aPS.setObject(9, sil.getSILNo());
	}

	public String[] sutunAdlariAl()
    {
	    return COLUMNS;
    }

	public String tabloAdiAl()
    {
		return SIL_TABLO_ADI;
    }
}
