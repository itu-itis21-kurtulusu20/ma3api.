package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OzetYardimci implements ModelNesneYardimci
{
	public static final String COLUMN_HASH_NO ="HashNo";
	public static final String COLUMN_OBJECT_TYPE ="ObjectType";
	public static final String COLUMN_OBJECT_NO ="ObjectNo";
	public static final String COLUMN_HASH_TYPE ="HashType";
	public static final String COLUMN_HASH_VALUE ="HashValue";
	
	
	public static final String OZET_TABLO_ADI = "HASH";
		
	public static final String[] COLUMNS = 
	{
        COLUMN_OBJECT_TYPE,
		COLUMN_OBJECT_NO,
		COLUMN_HASH_TYPE,
		COLUMN_HASH_VALUE,
		COLUMN_HASH_NO
	};
	
	public Object nesneyiDoldur(ResultSet aRS) 
	throws SQLException 
	{
		DepoOzet ozet = new DepoOzet();
		ozet.setObjectType(aRS.getInt(COLUMN_OBJECT_TYPE));
		ozet.setObjectNo(aRS.getLong(COLUMN_OBJECT_NO));
		ozet.setHashType(aRS.getInt(COLUMN_HASH_TYPE));
		ozet.setHashValue(aRS.getBytes(COLUMN_HASH_VALUE));
		ozet.setHashNo(aRS.getLong(COLUMN_HASH_NO));
		
		return ozet;
	}

	public void sorguyuDoldur(Object aModelNesnesi, PreparedStatement aPS) 
	throws SQLException 
	{
		DepoOzet ozet = (DepoOzet) aModelNesnesi;
		
		aPS.setInt(1, ozet.getObjectType());
		aPS.setLong(2, ozet.getObjectNo());
		aPS.setInt(3, ozet.getHashType());
		aPS.setBytes(4, ozet.getHashValue());
		aPS.setObject(5, ozet.getHashNo());
	}

	public String[] sutunAdlariAl()
	{
		return COLUMNS;
	}

	public String tabloAdiAl() 
	{
		return OZET_TABLO_ADI;
	}

}
