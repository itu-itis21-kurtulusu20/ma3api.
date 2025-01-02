package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ahmety
 */
public class SQLiteBaglantiSaglayici
{
    private Logger logger = LoggerFactory.getLogger(SQLiteBaglantiSaglayici.class);
    private String mURL = null;

    // trivial
    public void init(String dbName) 
    {
    	
        try 
        {
            Class.forName("org.sqlite.JDBC");
            mURL = "jdbc:sqlite:"+dbName;
        }
        catch(ClassNotFoundException cnfx)
        {
            logger.error("org.sqlite.JDBC jar bulunamadi!", cnfx);
            try
            {
            	 Class.forName("org.sqldroid.SQLDroidDriver");
            	 mURL = "jdbc:sqldroid:"+dbName;
            }
            catch(ClassNotFoundException androidEx)
            {
            	 logger.error("org.sqldroid.SqldroidDriver bulunamadi!", androidEx);
            	 throw new ESYARuntimeException("SQLite jdbc jar bulunamadi!", cnfx);
            }
        }
    }

   

	public Connection newConnection() throws SQLException
    {
        Connection conn = DriverManager.getConnection(mURL);
        conn.setAutoCommit(false);
        return conn;
    }

}
