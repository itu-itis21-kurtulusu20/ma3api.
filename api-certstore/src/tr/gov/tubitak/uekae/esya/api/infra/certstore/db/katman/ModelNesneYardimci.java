package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public interface ModelNesneYardimci
{

    String tabloAdiAl();

    String[] sutunAdlariAl();

    void sorguyuDoldur(Object aModelNesnesi, PreparedStatement aPS)
            throws SQLException;

    Object nesneyiDoldur(ResultSet aRS)
            throws SQLException;

}
