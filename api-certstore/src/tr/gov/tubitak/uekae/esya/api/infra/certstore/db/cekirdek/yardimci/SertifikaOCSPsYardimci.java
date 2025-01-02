package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifikaOcsps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SertifikaOCSPsYardimci implements ModelNesneYardimci {

    public static final String COLUMN_OCSP_NO = "OCSPNo";
    public static final String COLUMN_SERTIFIKA_NO = "SertifikaNo";
    public static final String COLUMN_THIS_UPDATE = "ThisUpdate";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_REVOCATION_TIME = "RevocationTime";
    public static final String COLUMN_REVOCATION_REASON = "RevocationReason";

    public static final String SERTIFIKA_OCSPS_TABLO_ADI = "SERTIFIKAOCSPS";

    public static final String[] COLUMNS = {
            COLUMN_OCSP_NO,
            COLUMN_SERTIFIKA_NO,
            COLUMN_THIS_UPDATE,
            COLUMN_STATUS,
            COLUMN_REVOCATION_TIME,
            COLUMN_REVOCATION_REASON
    };

    public Object nesneyiDoldur(ResultSet aRS)
            throws SQLException {
        DepoSertifikaOcsps sertifikaOcsps = new DepoSertifikaOcsps();
        sertifikaOcsps.setOcspNo(aRS.getLong(COLUMN_OCSP_NO));
        sertifikaOcsps.setSertifikaNo(aRS.getLong(COLUMN_SERTIFIKA_NO));
        sertifikaOcsps.setThisUpdate(aRS.getDate(COLUMN_THIS_UPDATE));
        sertifikaOcsps.setStatus(aRS.getLong(COLUMN_STATUS));
        sertifikaOcsps.setRevocationTime(aRS.getDate(COLUMN_REVOCATION_TIME));
        sertifikaOcsps.setRevocationReason(aRS.getLong(COLUMN_REVOCATION_REASON));
        return sertifikaOcsps;

    }

    public void sorguyuDoldur(Object aModelNesnesi, PreparedStatement aPS)
            throws SQLException {
            throw new ESYARuntimeException("Not Implemented Yet");

    }

    public String[] sutunAdlariAl() {
        return COLUMNS;
    }

    public String tabloAdiAl() {
        return SERTIFIKA_OCSPS_TABLO_ADI;
    }

}
