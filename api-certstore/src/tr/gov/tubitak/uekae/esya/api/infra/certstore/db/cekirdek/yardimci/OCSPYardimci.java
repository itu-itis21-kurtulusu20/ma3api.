package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOCSP;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OCSPYardimci implements ModelNesneYardimci
{
	public static final String COLUMN_OCSP_NO = "OCSPNo";
	public static final String COLUMN_OCSP_RESP_ID = "OCSPResponderID";
	public static final String COLUMN_OCSP_PRODUCED_AT = "OCSPProducedAt";
	public static final String COLUMN_OCSP_VALUE = "OCSPValue";
	
	public static final String OCSP_TABLO_ADI = "OCSP";
	
	private static final String[] COLUMNS = new String[] 
	{
		COLUMN_OCSP_RESP_ID,
		COLUMN_OCSP_PRODUCED_AT,
		COLUMN_OCSP_VALUE,
		COLUMN_OCSP_NO
	};
	
	public Object nesneyiDoldur(ResultSet aRS)
	throws SQLException 
	{
		DepoOCSP ocsp = new DepoOCSP();
		ocsp.setOCSPResponderID(aRS.getBytes(COLUMN_OCSP_RESP_ID));
		ocsp.setOCSPProducedAt(aRS.getDate(COLUMN_OCSP_PRODUCED_AT));
		ocsp.setBasicOCSPResponse(aRS.getBytes(COLUMN_OCSP_VALUE));
		ocsp.setOCSPNo(aRS.getLong(COLUMN_OCSP_NO));
		return ocsp;
	}

	public void sorguyuDoldur(Object aModelNesnesi, PreparedStatement aPS) 
	throws SQLException 
	{
		DepoOCSP ocsp = (DepoOCSP) aModelNesnesi;
		aPS.setBytes(1, ocsp.getOCSPResponderID());
		aPS.setDate(2, ocsp.getOCSPProducedAt());
		aPS.setBytes(3, ocsp.getBasicOCSPResponse());
		aPS.setObject(4, ocsp.getOCSPNo());
	}

	public String[] sutunAdlariAl() 
	{
		return COLUMNS;
	}

	public String tabloAdiAl() 
	{
		return OCSP_TABLO_ADI;
	}

}
