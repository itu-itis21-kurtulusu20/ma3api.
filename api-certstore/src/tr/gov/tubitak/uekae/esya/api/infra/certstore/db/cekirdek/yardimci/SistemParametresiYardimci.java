package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSistemParametresi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SistemParametresiYardimci implements ModelNesneYardimci
{
	
	
	public static final String COLUMN_SP_PARAM_ADI = "ParametreAdi";
	public static final String COLUMN_SP_PARAM_DEGERI = "ParametreDegeri";
	
	
	public static final String SP_TABLO_ADI = "SistemParametreleri";
	
	private static final String[] COLUMNS = 
	{
		COLUMN_SP_PARAM_ADI,
		COLUMN_SP_PARAM_DEGERI
	};
	
	public Object nesneyiDoldur(ResultSet aRS) 
	throws SQLException
    {
		DepoSistemParametresi sp = new DepoSistemParametresi();
		sp.setParamAdi(aRS.getString(COLUMN_SP_PARAM_ADI));
		sp.setParamDegeri(aRS.getObject(COLUMN_SP_PARAM_DEGERI));
		return sp;
    }
	
	public void sorguyuDoldur(Object aModelNesnesi,PreparedStatement aPS) 
	throws SQLException
    {
		DepoSistemParametresi sp = (DepoSistemParametresi) aModelNesnesi;
		aPS.setString(1,sp.getParamAdi());
		aPS.setObject(2, sp.getParamDegeri());
	}
	
	public String[] sutunAdlariAl()
    {
	    return COLUMNS;
    }

	public String tabloAdiAl()
    {
		return SP_TABLO_ADI;
    }
}
